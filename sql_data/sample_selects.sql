/* Bekomme alle Teilnehmer */
SELECT firmenname, nachname, vorname, email, strasse, hausnummer, plz, ort, land FROM teilnehmer JOIN firma ON teilnehmer.fnummer = firma.fnummer;

/* Alle Privatpersonen */
SELECT nachname, vorname, email, strasse, hausnummer, plz, ort, land FROM teilnehmer JOIN firma ON teilnehmer.fnummer = firma.fnummer AND firma.firmenname IS NULL;

/* Teilnehmer pro Firma, ohne Privatpersonen */
SELECT firmenname, COUNT(*) AS anz_teilnehmer FROM teilnehmer JOIN firma ON teilnehmer.fnummer = firma.fnummer AND firmenname IS NOT NULL GROUP BY firmenname;