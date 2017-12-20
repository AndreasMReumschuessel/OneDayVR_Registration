/* Bekomme alle Teilnehmer */
SELECT firmenname, anrede, titel, nachname, vorname, email, telefon, strasse, hausnummer, plz, ort, land FROM teilnehmer JOIN firma ON teilnehmer.fnummer = firma.fnummer;

/* Alle Privatpersonen */
SELECT anrede, titel, nachname, vorname, email, telefon, strasse, hausnummer, plz, ort, land FROM teilnehmer JOIN firma ON teilnehmer.fnummer = firma.fnummer AND firma.firmenname = "privat";

/* Teilnehmer pro Firma, ohne Privatpersonen */
SELECT firmenname, COUNT(*) AS anz_teilnehmer FROM teilnehmer JOIN firma ON teilnehmer.fnummer = firma.fnummer AND firmenname != "privat" GROUP BY firmenname;