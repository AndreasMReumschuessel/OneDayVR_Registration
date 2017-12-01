package controllers
import play.api.mvc._
import model.{Teilnehmer, TeilnehmerTable, Firma, FirmaTable}
import slick.jdbc.MySQLProfile.api._
import slick.lifted.TableQuery

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
      println(json)
      Ok
  }

  def listAll():Unit={
    //db.run(table.result)
  }

  def insertTeilnehmer(t: Teilnehmer): Unit ={
    db.run(teilnehmer += t)
  }
}