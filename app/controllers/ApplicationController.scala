package controllers
import javax.inject.{Inject, Singleton}

import play.api.mvc._
import model.{Firma, FirmaTable, Teilnehmer, TeilnehmerTable}
import play.api.libs.json
import slick.jdbc.MySQLProfile.api._
import slick.lifted.TableQuery

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

  def getJsonInt(value: String): Int={
    value.slice(1, value.length-1).toInt
  }

  def saveStock =Action{
    request =>
      val json = request.body.asJson.get
      val vorname = getJsonString((json \ "vorname").get.toString())
      val nachname = getJsonString((json \ "nachname").get.toString())
      val email = getJsonString((json \ "email").get.toString())
      //val firmaOptions = (json \ "firmaOptions").get
      var firmenname:String = "privat"

      try{
        firmenname = getJsonString((json \ "firmenname").get.toString())
      }catch{
        case ex: NoSuchElementException => println("[info] use privat teilnehmer")
      }

      val strasse = getJsonString((json \ "strasse").get.toString())
      val hausnummer = getJsonInt((json \ "hausnummer").get.toString())
      val plz = getJsonInt((json \ "postleitzahl").get.toString())
      val ort = getJsonString((json \ "ort").get.toString())
      val land = getJsonString((json \ "land").get.toString())


      val fnummer:Int = insertFirma(Firma(firmenname, DEFAULT_FIRMENID, strasse, hausnummer, plz, ort,land))
      try{
        insertTeilnehmer(Teilnehmer(vorname, nachname, email, fnummer))
      } catch{
        case ex: Exception => FAILED_DEPENDENCY
      }

      Ok
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