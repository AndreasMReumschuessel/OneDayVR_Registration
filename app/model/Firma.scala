package model

import slick.jdbc.MySQLProfile.api._

final case class Firma(
                             firmenname: String,
                             fnummer: Int,
                             strasse: String,
                             hausnummer: String,
                             plz: String,
                             ort: String,
                             land: String
                           )
final class FirmaTable(tag: Tag) extends Table[Firma](tag, "firma"){

  def firmenname = column[String]("firmenname")
  def fnummer    = column[Int]("fnummer", O.PrimaryKey, O.AutoInc)
  def strasse    = column[String]("strasse")
  def hausnummer = column[String]("hausnummer")
  def plz        = column[String]("plz")
  def ort        = column[String]("ort")
  def land       = column[String]("land")

  def * = (firmenname, fnummer, strasse, hausnummer, plz, ort, land).mapTo[Firma]
}
