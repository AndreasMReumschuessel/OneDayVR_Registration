package model

import slick.jdbc.MySQLProfile.api._

final case class Teilnehmer(
                           vorname: String,
                           nachname: String,
                           email: String
                           )
final class TeilnehmerTable(tag: Tag) extends Table[Teilnehmer](tag, "teilnehmer"){

  def vorname     = column[String]("vorname")
  def nachname    = column[String]("nachname")
  def email       = column[String]("email")

  def * = (vorname, nachname, email).mapTo[Teilnehmer]
}
