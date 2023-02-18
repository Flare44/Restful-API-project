-- Nutzer

INSERT INTO Nutzer (E_Mail_Adresse, Passwort)
VALUES('jakob12@gmail.com', 'sp5C12#§');
INSERT INTO Nutzer (E_Mail_Adresse, Passwort)
VALUES('kevintaylor@web.de', 'DBlove66');
INSERT INTO Nutzer (E_Mail_Adresse, Passwort)
VALUES('leber1763@icloud.com', 'Power44');
INSERT INTO Nutzer (E_Mail_Adresse, Passwort)
VALUES('tommi@gmail.com', '1965Rebel');
INSERT INTO Nutzer (E_Mail_Adresse, Passwort)
VALUES('coolKitten50@web.de', 'Rosi28');
INSERT INTO Nutzer (E_Mail_Adresse, Passwort)
VALUES('Roberto@gmail.com', '11LGBTQI+');
INSERT INTO Nutzer (E_Mail_Adresse, Passwort)
VALUES('ChristianBerger@gmail.com', 'Chrisba22');
INSERT INTO Nutzer (E_Mail_Adresse, Passwort)
VALUES('AlexVoigt@gmail.com', 'aleX98');

-- Fachliche Kompetenz

INSERT INTO Fachliche_Kompetenz (id, Eigenschaft)
VALUES (0, 'Cybersecurity');
INSERT INTO Fachliche_Kompetenz (id, Eigenschaft)
VALUES (1, 'KI');

-- Programmiersprache 

INSERT INTO Programmiersprache (Name)
VALUES ('Java');
INSERT INTO Programmiersprache (Name)
VALUES ('JavaScript');
INSERT INTO Programmiersprache (Name)
VALUES ('C');

-- Kunde

INSERT INTO Kunde (Telefonnummer, E_Mail_Adresse)
VALUES ('0128451512', 'jakob12@gmail.com');
INSERT INTO Kunde (Telefonnummer, E_Mail_Adresse)
VALUES ('0190919569', 'kevintaylor@web.de');
INSERT INTO Kunde (Telefonnummer, E_Mail_Adresse)
VALUES ('0124581245', 'ChristianBerger@gmail.com');
INSERT INTO Kunde (Telefonnummer, E_Mail_Adresse)
VALUES ('01256126641234', 'AlexVoigt@gmail.com');

-- Kompetenz

INSERT INTO Kompetenz (id, Eigenschaft)
VALUES (0, 'Teamgeist');
INSERT INTO Kompetenz (id, Eigenschaft)
VALUES (1, 'Anpassungsfaehigkeit');

-- Spezikikation

INSERT INTO Spezifikation (id, Merkmal)
VALUES (0, 'Schattierungen');
INSERT INTO Spezifikation (id, Merkmal)
VALUES (1, 'Holzkunst');

-- Projekt

INSERT INTO Projekt (id, Projektdeadline, Projektname, E_Mail_Adresse, Telefonnummer)
VALUES (0, '2023-06-06', 'WebseiteFuerJakob', 'leber1763@icloud.com', '0128451512');
INSERT INTO Projekt (id, Projektdeadline, Projektname, E_Mail_Adresse, Telefonnummer)
VALUES (1, '2022-12-24', 'OptimierungKIVonKevin', 'tommi@gmail.com', '0190919569');

-- Bewertung

INSERT INTO Bewertung (id, Text, Bepunktung, Telefonnummer, Projekt_id)
VALUES (0, 'Super, vielen Dank!', 9, '0128451512', 0);
INSERT INTO Bewertung (id, Bepunktung, Telefonnummer, Projekt_id)
VALUES (1, 2, '0190919569', 1);

-- Aufgabe 

INSERT INTO Aufgabe (id, Deadline, Beschreibung, Status, Priorisierung, Projekt_id)
VALUES (0, '2023-04-02', 'Fronted fertig machen', 'In Arbeit', 'hoch', 0);
INSERT INTO Aufgabe (id, Deadline, Beschreibung, Status, Priorisierung, Projekt_id)
VALUES (1, '2023-05-02', 'Backend fertig machen', 'Bald', 'hoch', 0);
INSERT INTO Aufgabe (id, Deadline, Beschreibung, Status, Priorisierung, Projekt_id)
VALUES (2, '2023-05-12', 'Datenbank einbinden', 'In Arbeit', 'hoch', 0);
INSERT INTO Aufgabe (id, Deadline, Beschreibung, Status, Priorisierung, Projekt_id)
VALUES (3, '2022-12-19', 'KI trainieren', 'Fast fertig', 'mittel', 1);

-- Spezialist

INSERT INTO Spezialist (E_Mail_Adresse, Verfuegbarkeitsstatus)
VALUES ('leber1763@icloud.com', 'Noch im Urlaub bis zum 2023-01-05');
INSERT INTO Spezialist (E_Mail_Adresse, Verfuegbarkeitsstatus)
VALUES ('tommi@gmail.com', 'Verfuegbar');
INSERT INTO Spezialist (E_Mail_Adresse, Verfuegbarkeitsstatus)
VALUES ('coolKitten50@web.de', 'Verfuegbar');
INSERT INTO Spezialist (E_Mail_Adresse, Verfuegbarkeitsstatus)
VALUES ('Roberto@gmail.com', 'Derzeit nicht verfuegbar');

-- Designer

INSERT INTO Designer (E_Mail_Adresse, Alias, id)
VALUES ('coolKitten50@web.de', 'Sabine', 0);
INSERT INTO Designer (E_Mail_Adresse, Alias, id)
VALUES ('Roberto@gmail.com', 'Robert', 1);

-- Entwickler

INSERT INTO Entwickler (Kuerzel, E_Mail_Adresse)
VALUES ('Dirki499', 'leber1763@icloud.com');
INSERT INTO Entwickler (Kuerzel, E_Mail_Adresse)
VALUES ('Tommi187', 'tommi@gmail.com');

-- Projektleiter

INSERT INTO Projektleiter (E_Mail_Adresse, Gehalt)
VALUES ('leber1763@icloud.com', 145050.5);
INSERT INTO Projektleiter (E_Mail_Adresse, Gehalt)
VALUES ('tommi@gmail.com', 65000);

-- Spezialist_arbeitet_an_Projekt

INSERT INTO Spezialist_arbeitet_an_Projekt (E_Mail_Adresse, id)
VALUES ('leber1763@icloud.com', 0);
INSERT INTO Spezialist_arbeitet_an_Projekt (E_Mail_Adresse, id)
VALUES ('tommi@gmail.com', 1);

-- Spezialist_hat_fachliche_Kompetenz

INSERT INTO Spezialist_hat_fachliche_Kompetenz (E_Mail_Adresse, id)
VALUES ('leber1763@icloud.com', 0);
INSERT INTO Spezialist_hat_fachliche_Kompetenz (E_Mail_Adresse, id)
VALUES ('tommi@gmail.com', 1);

-- Designer_hat_Kompetenz

INSERT INTO Designer_hat_Kompetenz (E_Mail_Adresse, id)
VALUES ('coolKitten50@web.de', 0);
INSERT INTO Designer_hat_Kompetenz (E_Mail_Adresse, id)
VALUES ('Roberto@gmail.com', 1);

-- Entwickler_beherrscht_Programmiersprache

INSERT INTO Entwickler_beherrscht_Programmiersprache (Kuerzel, Name, Erfahrungsstufe)
VALUES ('Dirki499', 'JavaScript', 3);
INSERT INTO Entwickler_beherrscht_Programmiersprache (Kuerzel, Name, Erfahrungsstufe)
VALUES ('Tommi187', 'C', 1);

-- Spezialist_betreut_Spezialist

INSERT INTO Spezialist_betreut_Spezialist (E_Mail_Adresse1, E_Mail_Adresse2)
VALUES ('tommi@gmail.com', 'leber1763@icloud.com');
INSERT INTO Spezialist_betreut_Spezialist (E_Mail_Adresse1, E_Mail_Adresse2)
VALUES ('Roberto@gmail.com', 'coolKitten50@web.de');

