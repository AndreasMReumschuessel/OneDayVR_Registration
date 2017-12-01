package controllers
import play.api.mvc._
import model.{Teilnehmer, TeilnehmerTable, Firma, FirmaTable}
import slick.jdbc.MySQLProfile.api._
import slick.lifted.TableQuery

class ApplicationController(cc: ControllerComponents) extends AbstractController(cc){
  lazy val teilnehmer = TableQuery[TeilnehmerTable]
  lazy val firma = TableQuery[FirmaTable]
  val db = Database.forConfig("onedayvr")

  def index():Result={
    db.createSession()
    Results.ok(views.html.index("Hallo"))
  }
  def saveStock(): Request ={
    request =>
      val json = request.body.asJson.get
      val stock = json.as[Stock]
      println(stock)
      Results.Ok("")
  }

  def listAll():Unit={
    //db.run(table.result)
  }

  def insertTeilnehmer(t: Teilnehmer): Unit ={
    db.run(teilnehmer += t)
  }
}