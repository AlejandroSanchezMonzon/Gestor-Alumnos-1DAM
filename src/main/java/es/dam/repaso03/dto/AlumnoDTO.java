package es.dam.repaso03.dto;

import es.dam.repaso03.models.Alumno;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.time.LocalDate;

public class AlumnoDTO implements Serializable {
    private String id;
    private String nombre;
    private LocalDate nacimiento;
    private double nota;

    public AlumnoDTO(Alumno alumno) {
        this.id = alumno.getId();
        this.nombre = alumno.getNombre();
        this.nacimiento = alumno.getNacimiento();
        this.nota = alumno.getNota();
    }

    public AlumnoDTO(String id, String nombre, LocalDate nacimiento, double nota) {
        this.id = id;
        this.nombre = nombre;
        this.nacimiento = nacimiento;
        this.nota = nota;
    }

    public Alumno fromDTO() {
        return new Alumno(id, nombre, nacimiento, nota);
    }

    public String toFile() {
        return id + "," + nombre + "," + nacimiento + "," + nota;
    }
}
