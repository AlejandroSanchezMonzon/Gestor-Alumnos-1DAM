package es.dam.repaso03.repositories;
import es.dam.repaso03.dto.AlumnoDTO;
import es.dam.repaso03.managers.DataBaseManager;
import es.dam.repaso03.models.Alumno;
import es.dam.repaso03.services.StorageCSV;
import es.dam.repaso03.services.StorageJSON;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AlumnosRepository implements IAlumnosRepository {
    DataBaseManager db = DataBaseManager.getInstance();

    private static AlumnosRepository instance;

    ObservableList<Alumno> alumnos = FXCollections.observableArrayList();

    StorageCSV storageCSV = StorageCSV.getInstance();

    StorageJSON storageJSON = StorageJSON.getInstance();

    public AlumnosRepository() {
    }

    //Singleton
    public static AlumnosRepository getInstance() {
        if (instance == null) {
            instance = new AlumnosRepository();
        }

        return instance;
    }


    @Override
    public ObservableList<Alumno> findAll() throws SQLException {
        String sql = "SELECT * FROM alumnos";
        db.open();
        var res = db.select(sql).orElseThrow(() -> new SQLException("Imposible recopilar la información de todos los alumnos."));
        alumnos.clear();
        while(res.next()) {
            alumnos.add(
                    new Alumno(
                            res.getString("id"),
                            res.getString("nombre"),
                            LocalDate.parse(res.getString("nacimiento")),
                            res.getDouble("calificacion")
                    )
            );
        }
        db.close();
        if(alumnos.isEmpty()) {
            System.out.println("La lista de alumnos está vacía.");
        }

        return alumnos;
    }

    @Override
    public void save(Alumno alumno) throws SQLException {
        String sql = "INSERT INTO alumnos(id, nombre, nacimiento, calificacion) VALUES (?, ?, ?, ?)";
        db.open();
        var res = db.insert(sql, alumno.getId(), alumno.getNombre(), alumno.getNacimiento(), alumno.getNota());
        db.close();
    }

    @Override
    public void update(Alumno alumno) throws SQLException {
        String sql = "UPDATE alumnos SET nombre = ?, nacimiento = ?, calificacion = ? WHERE id = ?";
        db.open();
        var res = db.update(sql, alumno.getNombre(), alumno.getNacimiento(), alumno.getNota(), alumno.getId());
        db.close();

        alumnos.set(alumnos.indexOf(alumno), alumno);
    }

    @Override
    public void delete(Alumno alumno) throws SQLException {
        String sql = "DELETE FROM alumnos WHERE id = ?";
        db.open();
        db.delete(sql, alumno.getId());
        db.close();

        alumnos.remove(alumno);
    }

    @Override
    public void clear(Alumno alumno) throws SQLException {
        String sql = "UPDATE alumnos SET nombre = ?, nacimiento = ?, calificacion = ? WHERE id = ?";
        db.open();
        //He puesto null en el nombre y cambiado en el .sql pero no se si se habrá actuaizado.
        // CUIDADO.
        db.update(sql, "", "2000-01-01", "", alumno.getId());
        db.close();

        alumnos.set(alumnos.indexOf(alumno), alumno);
    }

    @Override
    public void restore(Path filePath) throws SQLException {
        List<AlumnoDTO> alumnosRestore = storageCSV.restore(filePath);
        alumnos.clear();
        String sql = "DELETE FROM alumnos";
        db.open();
        db.update(sql);
        db.close();
        alumnosRestore.forEach(p -> {
            try {
                save(p.fromDTO());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        );
    }

    @Override
    public void backUp() throws SQLException {
        List<AlumnoDTO> alumnosDTO = alumnos.stream().map(AlumnoDTO::new).toList();
        storageJSON.backUp(alumnosDTO);
    }

    @Override
    public void autoSave() {
        List<AlumnoDTO> alumnosDTO = alumnos.stream().map(AlumnoDTO::new).toList();
        storageCSV.autoSave(alumnosDTO, false);
    }
}
