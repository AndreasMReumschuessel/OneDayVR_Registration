package controllers
import play.api.mvc._
import model.{Teilnehmer, TeilnehmerTable, Firma, FirmaTable}
import slick.jdbc.MySQLProfile.api._
import slick.lifted.TableQuery
import play.api.libs.json._

class ApplicationController extends Controller{
  lazy val teilnehmer = TableQuery[TeilnehmerTable]
  lazy val firma = TableQuery[FirmaTable]
  val db = Database.forConfig("onedayvr")

  def index=Action{
    //db.createSession()
    Ok(views.html.index("Hallo"))
  }
  def saveStock =Action{
    request =>
      val json = request.body.asJson.get
      val vorname = (json \ "vorname").get
      val nachname = (json \ "nachname").get
      val email = (json \ "email").get
      val firmaOptions = (json \ "firmaOptions").get
      val firmenname = (json \ "firmenname").get
      val strasse = (json \ "strasse").get
      val hausnummer = (json \ "hausnummer").get
      val plz = (json \ "postleitzahl").get
      val ort = (json \ "ort").get
      val land = (json \ "land").get


      Ok
  }

  def listAll():Unit={
    //db.run(table.result)
  }

  def insertTeilnehmer(t: Teilnehmer): Unit ={
    db.run(teilnehmer += t)
  }
}