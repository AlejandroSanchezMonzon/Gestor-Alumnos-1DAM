package es.dam.repaso03.services;

import es.dam.repaso03.dto.AlumnoDTO;

import java.io.IOException;
import java.util.List;

public interface IStorageJSON {
    void backUp(List<AlumnoDTO> alumnos) throws IOException;
}
