package model

import slick.jdbc.MySQLProfile.api._

final case class Teilnehmer(
                           vorname: String,
                           nachname: String,
                           email: String,
                           fnummer: Int,
                           titel: String,
                           anrede: String,
                           telefon: String
                           )
final class TeilnehmerTable(tag: Tag) extends Table[Teilnehmer](tag, "teilnehmer"){

  def vorname     = column[String]("vorname")
  def nachname    = column[String]("nachname")
  def email       = column[String]("email")
  def titel       = column[String]("titel")
  def anrede      = column[String]("anrede")
  def telefon      = column[String]("telefon")
  def fnummer     = column[Int]("fnummer")

  def * = (vorname, nachname, email, fnummer, titel, anrede, telefon).mapTo[Teilnehmer]
}
