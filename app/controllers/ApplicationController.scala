package controllers
import javax.inject.{Inject, Singleton}

import play.api.mvc._
import model.{Firma, FirmaTable, Teilnehmer, TeilnehmerTable}
import play.api.libs.json
import slick.jdbc.MySQLProfile.api._
import slick.lifted.TableQuery

import model.State

@Singleton
class ApplicationController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  lazy val teilnehmer = TableQuery[TeilnehmerTable]
  lazy val firmen = TableQuery[FirmaTable]
  val db = Database.forConfig("onedayvr")
  val DEFAULT_FIRMENID = 0

  def index=Action{
    db.createSession()
    Ok(views.html.index("Hallo"))
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
      val anrede = getJsonString((json \ "anrede").get.toString())
      state.addDataEntry(anrede, "Frau|Herr")

      val email = getJsonString((json \ "email").get.toString())
      state.addDataEntry(email, "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")

      var firmenname:String = "privat"

      try{
        firmenname = getJsonString((json \ "firmaOptions").get.toString())
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
      state.addDataEntry(strasse, "^[A-z|üÜ|öÖ|äÄ|ß|\\s|\\-\\.]{2,}")

      val telefon = getJsonString((json \ "telefon").get.toString())
      state.addDataEntry(telefon, "^+{0,1}[0-9]*")

      val titel = getJsonString((json \ "titel").get.toString())
      state.addDataEntry(titel, "(Dr.|Prof.)")

      val vorname = getJsonString((json \ "vorname").get.toString())
      state.addDataEntry(vorname, "^[A-z|üÜ|öÖ|äÄ|\\s|\\-\\.]{2,}")

      val fnummer:Int = insertFirma(Firma(firmenname, DEFAULT_FIRMENID, strasse, plz, ort))
      try{
        insertTeilnehmer(Teilnehmer(vorname, nachname, email, fnummer, titel, anrede, telefon))
      } catch{
        case ex: Exception => FAILED_DEPENDENCY
      }

      Ok
    }

    while(state.hasNext()){
      state.validate()
    }
    new Status(state.statuscode)
  }

  def listAllTeilNehmer():Unit={
    db.run(teilnehmer.result)
  }

  def listAllFirma():Unit={
    db.run(firmen.result)
  }

  def insertTeilnehmer(t: Teilnehmer): Unit ={
    db.run(teilnehmer += t)
  }

  def insertFirma(f: Firma): Int={
    val erg = db.run((firmen returning firmen.map(_.fnummer)) += f)
    while(!erg.isCompleted){} //¯\_(ツ)_/¯
    erg.value.get.get

  }
}