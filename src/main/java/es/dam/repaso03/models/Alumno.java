package es.dam.repaso03.models;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.util.UUID;

public class Alumno {
    private String id = UUID.randomUUID().toString();
    private StringProperty nombre;
    private ObjectProperty<LocalDate> nacimiento;
    private DoubleProperty nota;

    public Alumno(String nombre, LocalDate nacimiento, double nota) {
        this.nombre = new SimpleStringProperty(nombre);
        this.nacimiento = new SimpleObjectProperty<LocalDate>(nacimiento);
        this.nota = new SimpleDoubleProperty(nota);
    }

    public Alumno(String id, String nombre, LocalDate nacimiento, double nota) {
        this.id = id;
        this.nombre = new SimpleStringProperty(nombre);
        this.nacimiento = new SimpleObjectProperty<LocalDate>(nacimiento);
        this.nota = new SimpleDoubleProperty(nota);
    }

    public Alumno() {
        this(null, null, null, 0.00);
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre.get();
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public LocalDate getNacimiento() {
        return nacimiento.get();
    }

    public void setNacimiento(LocalDate nacimiento) {
        this.nacimiento.set(nacimiento);
    }

    public double getNota() {
        return nota.get();
    }

    public DoubleProperty notaProperty() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota.set(nota);
    }
}
