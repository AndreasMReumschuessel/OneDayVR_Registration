package model

import slick.jdbc.MySQLProfile.api._

final case class Firma(
                             firmenname: String,
                             fnummer: Int,
                             strasse: String,
                             hausnummer: Int,
                             plz: Int,
                             ort: String,
                             land: String
                           )
final class FirmaTable(tag: Tag) extends Table[Firma](tag, "firma"){

  def firmenname = column[String]("firmenname")
  def fnummer    = column[Int]("fnummer", O.PrimaryKey, O.AutoInc)
  def strasse    = column[String]("strasse")
  def hausnummer = column[Int]("hausnummer")
  def plz        = column[Int]("plz")
  def ort        = column[String]("ort")
  def land       = column[String]("land")

  def * = (firmenname, fnummer, strasse, hausnummer, plz, ort, land).mapTo[Firma]
}
