CREATE TRIGGER max_teilnehmer BEFORE INSERT ON teilnehmer
FOR EACH ROW
BEGIN
  IF (SELECT COUNT(*) FROM teilnehmer) >= 250 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Maximale Teilnehmerzahl erreicht. Daten werden verworfen.';
  END IF;
END;