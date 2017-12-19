package model

import slick.jdbc.MySQLProfile.api._

final case class Firma(
                             firmenname: String,
                             fnummer: Int,
                             strasse: String,
                             plz: String,
                             ort: String
                           )
final class FirmaTable(tag: Tag) extends Table[Firma](tag, "firma"){

  def firmenname = column[String]("firmenname")
  def fnummer    = column[Int]("fnummer", O.PrimaryKey, O.AutoInc)
  def strasse    = column[String]("strasse")
  def plz        = column[String]("plz")
  def ort        = column[String]("ort")

  def * = (firmenname, fnummer, strasse, plz, ort).mapTo[Firma]
}
