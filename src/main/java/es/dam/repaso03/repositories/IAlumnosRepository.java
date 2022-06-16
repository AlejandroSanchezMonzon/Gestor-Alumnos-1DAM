package es.dam.repaso03.repositories;

import es.dam.repaso03.models.Alumno;

import java.nio.file.Path;
import java.sql.SQLException;

public interface IAlumnosRepository extends ICRUDRepository<Alumno> {
    void restore(Path path) throws SQLException;

    void backUp() throws SQLException;

    void autoSave();
}
