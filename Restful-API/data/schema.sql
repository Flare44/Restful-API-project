PRAGMA auto_vacuum = 1;
PRAGMA encoding = "UTF-8";
PRAGMA foreign_keys = 1;
PRAGMA journal_mode = WAL;
PRAGMA synchronous = NORMAL;

--Entitaeten:

CREATE TABLE IF NOT EXISTS Nutzer (
  E_Mail_Adresse VARCHAR COLLATE NOCASE NOT NULL PRIMARY KEY,
  Passwort VARCHAR NOT NULL, 
  CHECK (
    E_Mail_Adresse LIKE '%_@%_.%_' AND
    (substr(E_Mail_Adresse, INSTR(E_Mail_Adresse,'@')+1, ((INSTR(E_Mail_Adresse,'.')-1) - (INSTR(E_Mail_Adresse,'@')))) NOT GLOB '*[^A-Za-z0-9]*') AND
    (substr(E_Mail_Adresse,1, INSTR(E_Mail_Adresse,'@')-1) NOT GLOB '*[^A-Za-z0-9]*') AND
    (substr(E_Mail_Adresse, INSTR(E_Mail_Adresse,'.')+1) NOT GLOB '*[^A-Za-z]*')
  ),
  CHECK (
    LENGTH(Passwort) BETWEEN 4 AND 9 AND 
    Passwort GLOB '*[0-9]*[0-9]*' AND 
    Passwort GLOB '*[A-Z]*' AND 
    Passwort NOT GLOB '*[aeiouAEIOU][13579]*'
  )
);

CREATE TABLE IF NOT EXISTS Fachliche_Kompetenz (
  id INTEGER NOT NULL PRIMARY KEY, 
  Eigenschaft VARCHAR NOT NULL,
  CHECK (
    Eigenschaft NOT GLOB '*[^a-zA-Z]*' AND Length(Eigenschaft) > 0
  ),
  CHECK (
    id > -1
  )
);

CREATE TABLE IF NOT EXISTS Programmiersprache (
  Name VARCHAR NOT NULL PRIMARY KEY,
  CHECK (
    Name NOT GLOB '*[^ -~]*' AND Length(Name) > 0
  )
);

CREATE TABLE IF NOT EXISTS Kunde (
  Telefonnummer VARCHAR NOT NULL PRIMARY KEY,
  E_Mail_Adresse VARCHAR NOT NULL COLLATE NOCASE UNIQUE, 
  FOREIGN KEY (E_Mail_Adresse) REFERENCES Nutzer(E_Mail_Adresse) ON UPDATE CASCADE ON DELETE CASCADE,
  CHECK (
    Telefonnummer NOT GLOB '[^0-9]'
  )
);

CREATE TABLE IF NOT EXISTS Kompetenz (
  id INTEGER NOT NULL PRIMARY KEY, 
  Eigenschaft VARCHAR NOT NULL,
  CHECK (
    Eigenschaft NOT GLOB '*[^a-zA-Z]*' AND Length(Eigenschaft) > 0
  ),
  CHECK (
    id > -1
  )
);

CREATE TABLE IF NOT EXISTS Spezifikation (
  id INTEGER NOT NULL PRIMARY KEY, 
  Merkmal VARCHAR NOT NULL,
  CHECK (
    Merkmal NOT GLOB '*[^ -~]*' AND Length(Merkmal) > 0
  ),
  CHECK (
    id > -1
  )
);

CREATE TABLE IF NOT EXISTS Projekt (
  id INTEGER NOT NULL PRIMARY KEY, 
  Projektdeadline DATE NOT NULL, 
  Projektname VARCHAR NOT NULL,
  E_Mail_Adresse VARCHAR NOT NULL COLLATE NOCASE, 
  Telefonnummer VARCHAR NOT NULL,
  FOREIGN KEY (E_Mail_Adresse) REFERENCES Nutzer (E_Mail_Adresse) ON UPDATE CASCADE ON DELETE CASCADE, 
  FOREIGN KEY (Telefonnummer) REFERENCES Kunde (Telefonnummer) ON UPDATE CASCADE ON DELETE CASCADE,
  CHECK (
    Projektname NOT GLOB '*[^ -~]*' AND Length(Projektname) > 0
  ),
  CHECK (
    id > -1
  ),
  CHECK (
    Projektdeadline IS DATE(Projektdeadline)
  )
);

CREATE TABLE IF NOT EXISTS Bewertung (
  id INTEGER NOT NULL PRIMARY KEY, 
  Text TEXT, 
  Bepunktung INTEGER NOT NULL, 
  Telefonnummer VARCHAR NOT NULL,
  Projekt_id INTEGER NOT NULL, 
  FOREIGN KEY (Telefonnummer) REFERENCES Kunde (Telefonnummer) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (Projekt_id) REFERENCES Projekt (id) ON UPDATE CASCADE ON DELETE CASCADE,
  CHECK (
    Bepunktung BETWEEN 1 AND 9
  ),
  CHECK (
    Text NOT GLOB '*[^ -~]*'
  ),
  CHECK (
    id > -1
  )
);

CREATE TABLE IF NOT EXISTS Aufgabe (
  id INTEGER NOT NULL PRIMARY KEY, 
  Deadline DATE NOT NULL, 
  Beschreibung TEXT NOT NULL, 
  Status VARCHAR NOT NULL,
  Priorisierung VARCHAR NOT NULL, 
  Projekt_id INTEGER NOT NULL, 
  FOREIGN KEY (Projekt_id) REFERENCES Projekt(id) ON UPDATE CASCADE ON DELETE CASCADE,
  CHECK (
    Beschreibung NOT GLOB '*[^ -~]*' AND Length(Beschreibung) > 0
  ),
  CHECK (
    Status NOT GLOB '*[^ -~]*' AND Length(Status) > 0
  ),
  CHECK (
    id > -1
  ),
  CHECK (
    Priorisierung NOT GLOB '*[^ -~]*' AND Length(Priorisierung) > 0
  ),
  CHECK (
    Deadline IS DATE(Deadline)
  )
);

CREATE TABLE IF NOT EXISTS Spezialist (
  E_Mail_Adresse VARCHAR NOT NULL COLLATE NOCASE PRIMARY KEY, 
  Verfuegbarkeitsstatus VARCHAR NOT NULL, 
  FOREIGN KEY (E_Mail_Adresse) REFERENCES Nutzer (E_Mail_Adresse) ON UPDATE CASCADE ON DELETE CASCADE,
  CHECK (
    Verfuegbarkeitsstatus NOT GLOB '*[^ -~]*' AND Length(Verfuegbarkeitsstatus) > 0
  )
);

CREATE TABLE IF NOT EXISTS Designer (
  E_Mail_Adresse VARCHAR NOT NULL COLLATE NOCASE PRIMARY KEY, 
  Alias VARCHAR, 
  id INTEGER NOT NULL, 
  FOREIGN KEY (E_Mail_Adresse) REFERENCES Spezialist (E_Mail_Adresse) ON UPDATE CASCADE ON DELETE CASCADE, 
  FOREIGN KEY (id) REFERENCES Spezifikation (id) ON UPDATE CASCADE ON DELETE CASCADE,
  CHECK (
    Alias NOT GLOB '*[^ -~]*'
  )
);

CREATE TABLE IF NOT EXISTS Entwickler (
  Kuerzel VARCHAR NOT NULL PRIMARY KEY, 
  E_Mail_Adresse VARCHAR NOT NULL COLLATE NOCASE UNIQUE,
  FOREIGN KEY (E_Mail_Adresse) REFERENCES Spezialist (E_Mail_Adresse) ON UPDATE CASCADE ON DELETE CASCADE,
  CHECK (
    Length(Kuerzel) = 8 AND 
    substr(Kuerzel, 1, 5) NOT GLOB '*[^a-zA-Z]*' AND 
    substr(Kuerzel, 6) NOT GLOB '*[^0-9]*'
  )
);

CREATE TABLE IF NOT EXISTS Projektleiter (
  E_Mail_Adresse VARCHAR NOT NULL PRIMARY KEY, 
  Gehalt DOUBLE NOT NULL, 
  FOREIGN KEY (E_Mail_Adresse) REFERENCES Nutzer (E_Mail_Adresse) ON UPDATE CASCADE ON DELETE CASCADE,
  CHECK (
    Gehalt GLOB '[0-9]' OR Gehalt GLOB '*.[0-9]' OR Gehalt GLOB '*.[0-9][0-9]' AND Gehalt >= 0 
  )
);

-- Beziehungen:

CREATE TABLE IF NOT EXISTS Spezialist_arbeitet_an_Projekt (
  E_Mail_Adresse VARCHAR NOT NULL, 
  id INTEGER NOT NULL, 
  PRIMARY KEY (E_Mail_Adresse, id),
  FOREIGN KEY (E_Mail_Adresse) REFERENCES Spezialist (E_Mail_Adresse) ON UPDATE CASCADE ON DELETE CASCADE, 
  FOREIGN KEY (id) REFERENCES Projekt (id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Spezialist_hat_fachliche_Kompetenz (
  E_Mail_Adresse VARCHAR NOT NULL, 
  id INTEGER NOT NULL, 
  PRIMARY KEY (E_Mail_Adresse, id), 
  FOREIGN KEY (E_Mail_Adresse) REFERENCES Spezialist (E_Mail_Adresse) ON UPDATE CASCADE ON DELETE CASCADE, 
  FOREIGN KEY (id) REFERENCES Fachliche_Kompetenz (id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Designer_hat_Kompetenz (
  E_Mail_Adresse VARCHAR NOT NULL, 
  id INTEGER NOT NULL, 
  PRIMARY KEY (E_Mail_Adresse, id), 
  FOREIGN KEY (E_Mail_Adresse) REFERENCES Designer (E_Mail_Adresse) ON UPDATE CASCADE ON DELETE CASCADE, 
  FOREIGN KEY (id) REFERENCES Kompetenz (id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Entwickler_beherrscht_Programmiersprache (
  Kuerzel VARCHAR NOT NULL, 
  Name VARCHAR NOT NULL, 
  Erfahrungsstufe INTEGER NOT NULL, 
  PRIMARY KEY (Kuerzel, Name), 
  FOREIGN KEY (Kuerzel) REFERENCES Entwickler (Kuerzel) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (Name) REFERENCES Programmiersprache (Name) ON UPDATE CASCADE ON DELETE CASCADE,
  CHECK (Erfahrungsstufe BETWEEN 1 AND 3)
);

CREATE TABLE IF NOT EXISTS Spezialist_betreut_Spezialist (
  E_Mail_Adresse1 VARCHAR NOT NULL COLLATE NOCASE, 
  E_Mail_Adresse2 VARCHAR NOT NULL COLLATE NOCASE,
  PRIMARY KEY (E_Mail_Adresse1),
  FOREIGN KEY (E_Mail_Adresse1) REFERENCES Spezialist (E_Mail_Adresse) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (E_Mail_Adresse2) REFERENCES Spezialist (E_Mail_Adresse) ON UPDATE CASCADE ON DELETE CASCADE
);

--Trigger:

CREATE TRIGGER IF NOT EXISTS Programmiersprache_Trigger 
  BEFORE DELETE ON Programmiersprache
  WHEN (
    SELECT COUNT(Entwickler_beherrscht_Programmiersprache.Kuerzel)
    FROM Entwickler_beherrscht_Programmiersprache
    WHERE Entwickler_beherrscht_Programmiersprache.name = old.name
  ) > 0 
  BEGIN
    SELECT RAISE(
      ABORT, 'Darf nicht geloescht werden, da die jemand kann'  
    );
  END;

CREATE TRIGGER IF NOT EXISTS Bewertung_Max_Anzahl BEFORE
  INSERT ON Bewertung
  WHEN(
    SELECT COUNT(NEW.Telefonnummer)
    FROM Kunde,Bewertung 
    WHERE Kunde.Telefonnummer = Bewertung.Telefonnummer
      AND Kunde.Telefonnummer LIKE NEW.Telefonnummer
    GROUP BY new.Telefonnummer
  ) >= 3 
  BEGIN
    SELECT RAISE(
      ABORT, 'Gleiches Projekt darf nur 3 mal bewertet werden'
    );
    
  END;

-- Verbesserter Trigger:

CREATE TRIGGER IF NOT EXISTS Betreuender_Spezialist_betreut_keinen_Spezialist 
  BEFORE INSERT ON Spezialist_betreut_Spezialist
  WHEN(
    SELECT COUNT(NEW.E_Mail_Adresse1)
    FROM Spezialist_betreut_Spezialist
    WHERE Spezialist_betreut_Spezialist.E_Mail_Adresse2 LIKE NEW.E_Mail_Adresse1
  ) > 0  
  BEGIN
    SELECT RAISE(
      ABORT, 'Ein Spezialist, der andere betreut, kann nicht selbst betreut werden'
    );
  END;






































































