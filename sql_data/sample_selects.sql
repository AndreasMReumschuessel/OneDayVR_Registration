/* Bekomme alle Teilnehmer */
SELECT firmenname, ticket, anrede, titel, nachname, vorname, email, strasse, plz, ort, land FROM teilnehmer JOIN firma ON teilnehmer.fnummer = firma.fnummer;

/* Alle Privatpersonen */
SELECT anrede, ticket, titel, nachname, vorname, email, strasse, plz, ort, land FROM teilnehmer JOIN firma ON teilnehmer.fnummer = firma.fnummer AND firma.firmenname = "privat";

/* Teilnehmer pro Firma, ohne Privatpersonen */
SELECT firmenname, COUNT(*) AS anz_teilnehmer FROM teilnehmer JOIN firma ON teilnehmer.fnummer = firma.fnummer AND firmenname != "privat" GROUP BY firmenname;