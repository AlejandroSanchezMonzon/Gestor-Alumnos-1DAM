package es.dam.repaso03.repositories;

import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface ICRUDRepository<T> {
    ObservableList<T> findAll() throws SQLException;

    void save(T entity) throws SQLException;

    void update(T entity) throws SQLException;

    void delete(T entity) throws SQLException;

    void clear(T entity) throws SQLException;
}
