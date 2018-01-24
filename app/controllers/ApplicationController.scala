package controllers
import javax.inject.{Inject, Singleton}

import play.api.mvc._
import model.{Firma, FirmaTable, Teilnehmer, TeilnehmerTable}
import slick.jdbc.MySQLProfile.api._
import slick.lifted.TableQuery
import model.State
import com.typesafe.config.ConfigFactory

import scala.concurrent.Future
import org.apache.commons.mail.{HtmlEmail}
@Singleton
class ApplicationController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  lazy val teilnehmer = TableQuery[TeilnehmerTable]
  lazy val firmen = TableQuery[FirmaTable]
  val db = Database.forConfig("onedayvr")
  val DEFAULT_FIRMENID = 0
  val MAXGUESTS = 250

  def index=Action{
    Ok(views.html.index("Hallo"))
  }

  def impressum=Action{
    Ok(views.html.impressum.render())
  }

  def getJsonString(value: String): String ={
    value.slice(1, value.length-1)
  }

  def saveStock=Action{request =>
    val state = State()
    val json = request.body.asJson.get
    if(json == null){
      state.setNull()
    }else{
      if(listAllTeilNehmer() == MAXGUESTS){
        state.statuscode = state.FAIL
      }else{
        val anrede = getJsonString((json \ "anrede").get.toString())
        state.addDataEntry(anrede, "Frau|Herr")

        val email = getJsonString((json \ "email").get.toString())
        state.addDataEntry(email, "(?:[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[A-Za-z0-9-]*[A-Za-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")

        val firmaOptions = getJsonString((json \ "firmaOptions").get.toString())
        state.addDataEntry(firmaOptions, "student|firma|privat|premium")

        var firmenname:String = "privat"

        try{
          firmenname = getJsonString((json \ "firmenname").get.toString())
          state.addDataEntry(firmenname, "^[A-z|üÜ|öÖ|äÄ|\\s|\\-\\.]{2,}")
        }catch{
          case ex: NoSuchElementException => println("[info] use privat teilnehmer")
        }

        val nachname = getJsonString((json \ "nachname").get.toString())
        state.addDataEntry(nachname, "^[A-z|üÜ|öÖ|äÄ|\\s|\\-\\.]{2,}")

        val ort = getJsonString((json \ "ort").get.toString())
        state.addDataEntry(ort, "^[A-z|üÜ|öÖ|äÄ|\\s|\\-\\.]{2,}")

        val plz = getJsonString((json \ "postleitzahl").get.toString())
        state.addDataEntry(plz, "^[0-9]+")

        val strasse = getJsonString((json \ "strasse").get.toString())
        state.addDataEntry(strasse, "^[A-z|üÜ|öÖ|äÄ|ß|\\s|\\-\\.|0-9]{2,}")

        val ticket = getJsonString((json \ "ticket").get.toString())
        //no pattern matching.
        var persons = 1
        /*
        It would be better if the number of perons are saved in the json.
        Instead you need to make deep changes in the backend when the text in the fronted is altered.
         */
        if(ticket.contains("2 Pers")) persons = 2
        if(ticket.contains("4 Pers")) persons = 4

        println("Debug persons: " + persons)
        val titel = getJsonString((json \ "titel").get.toString())
        state.addDataEntry(titel, "(Professor|Dr.|Professor Dr.){0,1}")

        val vorname = getJsonString((json \ "vorname").get.toString())
        state.addDataEntry(vorname, "^[A-z|üÜ|öÖ|äÄ|\\s|\\-\\.]{2,}")

        //validate data
        while(state.hasNext()){
          state.validate()
        }

        //if data is invalid then reject it
        if(state.statuscode == state.OK)
        {
          val fnummer:Int = insertFirma(Firma(firmenname, DEFAULT_FIRMENID, strasse, plz, ort))
          try{
            insertTeilnehmer(Teilnehmer(vorname, nachname, email, fnummer, titel, anrede, ticket)) //the registerd person
            persons -= 1
            for(i <- 1 to persons){
              println("insert additionally " + i)
              insertTeilnehmer(Teilnehmer("Begleitung", nachname, email, fnummer, titel, anrede, ticket)) //the attached persons
            }
            //send email to host
            val sm: HtmlEmail = new HtmlEmail()
            sm.setSmtpPort(ConfigFactory.load().getInt("smtp.port"))
            sm.setAuthentication(ConfigFactory.load().getString("smtp.user"), ConfigFactory.load().getString("smtp.password"))
            sm.setSSLOnConnect(true)
            sm.setHostName("smtp.strato.de")
            sm.setFrom("kontakt@onedayvr.de")
            sm.addTo("kontakt@onedayvr.de")
            sm.setSubject("Registrierungsbenachrichtigung")
            sm.setHtmlMsg("" +
              "<html>" +
              "<body>" +
              "<ul>" +
              "<li>Anrede: "+anrede+"</li>" +
              "<li>Titel: "+titel+"</li>" +
              "<li>Email: "+email+"</li>" +
              "<li>Vorname: "+vorname+"</li>" +
              "<li>Nachname: "+nachname+"</li>" +
              "<li>Art: "+firmaOptions+"</li>" +
              "<li>Firma: "+firmenname+"</li>" +
              "<li>Tickettyp: "+ticket+"</li>" +
              "<li>Ort: "+ort+"</li>" +
              "<li>Postleitzahl: "+plz+"</li>" +
              "<li>Strasse: "+strasse+"</li>" +
              "</ul>" +
              "</body>" +
              "</html>")
            sm.send()
          } catch{
            case ex: Exception => FAILED_DEPENDENCY
          }
        }
      }
    }
    new Status(state.statuscode)
  }

  def listAllTeilNehmer():Int={
    val erg: Future[Int] = db.run(teilnehmer.length.result)
    while(!erg.isCompleted){} //we need to wait till the thread terminates ¯\_(ツ)_/¯
    erg.value.get.get
  }

  def listAllFirma():Unit={
    db.run(firmen.result)
  }

  def insertTeilnehmer(t: Teilnehmer): Unit ={
    db.run(teilnehmer += t)
  }

  def insertFirma(f: Firma): Int={
    val erg = db.run((firmen returning firmen.map(_.fnummer)) += f)
    while(!erg.isCompleted){} //we need to wait till the thread terminates ¯\_(ツ)_/¯
    erg.value.get.get

  }

}
