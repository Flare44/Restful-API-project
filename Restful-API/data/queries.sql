-- Geben Sie genau die Projekte (alle Attribute) aus, die mindestens drei Aufgaben mit hoher Prioritaet haben.

SELECT * 
FROM Projekt 
WHERE id IN 
    (
    SELECT Projekt_id 
    FROM Aufgabe 
    WHERE Priorisierung = 'hoch' 
    GROUP BY Projekt_id 
    HAVING COUNT(*) >= 3
    );

-- Geben Sie genau das Projekt mit der besten Durchschnittsbewertung aus.

-- Verbesserte Abfrage:
SELECT * 
FROM Projekt 
WHERE id = (
    SELECT Projekt_id FROM (
        SELECT Projekt_id, AVG(Bepunktung) AS Durchschnitt
        FROM Bewertung
        GROUP BY Projekt_id
        ORDER BY Durchschnitt DESC
        LIMIT 1
        )
);
-- Geben Sie in alphabetischer Reihenfolge die E-Mail-Adresse genau der Nutzer aus, die noch kein Projekt in Auftrag gegeben haben.

SELECT E_Mail_Adresse
FROM Nutzer 
WHERE E_Mail_Adresse NOT IN
    (
    SELECT E_Mail_Adresse
    FROM Kunde
    WHERE Telefonnummer IN 
        (
        SELECT Telefonnummer 
        FROM Projekt
        )
    )
ORDER BY E_Mail_Adresse ASC;


