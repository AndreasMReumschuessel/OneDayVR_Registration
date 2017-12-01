package model

import slick.jdbc.MySQLProfile.api._

final case class Firma(
                             firmenname: String,
                             strasse: String,
                             hausnummer: Int,
                             plz: Int,
                             ort: String,
                             land: String
                           )
final class TeilnehmerTable(tag: Tag) extends Table[Firma](tag, "firma"){

  def firmenname = column[String]("firmenname")
  def strasse    = column[String]("strasse")
  def hausnummer = column[Int]("hausnummer")
  def plz        = column[Int]("plz")
  def ort        = column[String]("ort")
  def land       = column[String]("land")

  def * = (firmenname, strasse, hausnummer, plz, ort, land).mapTo[Firma]
}
