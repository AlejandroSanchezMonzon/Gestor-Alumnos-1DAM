BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "alumnos" (
	"id"	INTEGER NOT NULL,
	"nombre"	TEXT,
	"nacimiento"	TEXT,
	"calificacion"	INTEGER,
	PRIMARY KEY("id" AUTOINCREMENT)
);
INSERT INTO "alumnos" VALUES (1,'Alejandro Sánchez','2002-10-23','5,75');
INSERT INTO "alumnos" VALUES (2,'Mireya Sánchez','2002-07-26','9,00');
INSERT INTO "alumnos" VALUES (3,'Alvaro Mingo','2002-01-13','5,25');
INSERT INTO "alumnos" VALUES (4,'Jorge Sánchez','2003-12-07','7,50');
INSERT INTO "alumnos" VALUES (5,'Rubén García-Redondo','2002-09-15','10,00');
COMMIT;
