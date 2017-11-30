package controllers

import scala.concurrent.Future
import javax.inject._

import play.api.mvc._
import model.{Teilnehmer, TeilnehmerTable}
import slick.jdbc.MySQLProfile.api._
import slick.lifted.TableQuery

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {

    lazy val teilnehmer = TableQuery[TeilnehmerTable]
    val db = Database.forConfig("mysql")
    db.createSession()

    Ok(views.html.index("Your new application is ready."))
  }

}
