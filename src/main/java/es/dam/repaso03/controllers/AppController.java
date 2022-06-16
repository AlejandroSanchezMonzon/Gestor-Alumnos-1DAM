package es.dam.repaso03.controllers;

import es.dam.repaso03.models.Alumno;
import es.dam.repaso03.repositories.AlumnosRepository;
import es.dam.repaso03.services.StorageJSON;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import java.util.UUID;

public class AppController {
    AlumnosRepository alumnosRepository = AlumnosRepository.getInstance();

    @FXML
    public MenuBar menubar;
    @FXML
    public Menu application;
    @FXML
    public MenuItem cerrar;
    @FXML
    public Menu archivos;
    @FXML
    public MenuItem csvbutton;
    @FXML
    public MenuItem jsonbutton;
    @FXML
    public Menu informe;
    @FXML
    public MenuItem txtbutton;
    @FXML
    public TableView<Alumno> alumnotabla;
    @FXML
    public TableColumn<Alumno, String> nombrecolumn;
    @FXML
    public TableColumn<Alumno, String> notacolumn;
    @FXML
    public TextField nombrefield;
    @FXML
    public TextField notafield;
    @FXML
    public DatePicker datepicker;
    @FXML
    public Button nuevobutton;
    @FXML
    public Button editarbutton;
    @FXML
    public Button eliminarbutton;
    @FXML
    public Button limpiarbutton;

    public AppController() {
    }

    public void initialize() {
        try {
            alumnotabla.setItems(alumnosRepository.findAll());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        nombrecolumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        notacolumn.setCellValueFactory(new PropertyValueFactory<>("nota"));

        alumnotabla.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> onAlumnoSelected(newValue));

        setVacio();
    }

    private void onAlumnoSelected(Alumno alumno) {
        if (alumno != null) {
            setDatos(alumno);
        } else {
            setVacio();
        }
    }

    private void setVacio() {
        nombrefield.setText("");
        nombrefield.setEditable(true);
        datepicker.setValue(LocalDate.parse("2000-01-01"));
        datepicker.setEditable(true);
        notafield.setText("");
        notafield.setEditable(true);
    }

    private void setDatos(Alumno alumno) {
        nombrefield.setText(alumno.getNombre());
        nombrefield.setEditable(true);
        datepicker.setValue(alumno.getNacimiento());
        datepicker.setEditable(true);
        notafield.setText(String.valueOf(alumno.getNota()));
        notafield.setEditable(true);
    }

    public void onActionNewAlumno(ActionEvent actionEvent) {
        nombrefield.setEditable(true);
        datepicker.setEditable(true);
        notafield.setEditable(true);

        Alumno alumno = new Alumno(UUID.randomUUID().toString(), nombrefield.getText(), datepicker.getValue(), Double.parseDouble(notafield.getText()));

        try {
            alumnosRepository.save(alumno);
            alumnotabla.setItems(alumnosRepository.findAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onActionUpdateAlumno(ActionEvent actionEvent) {
        nombrefield.setEditable(true);
        datepicker.setEditable(true);
        notafield.setEditable(true);

        Alumno alumno = alumnotabla.getSelectionModel().getSelectedItem();
        alumno.setNombre(nombrefield.getText());
        alumno.setNacimiento(datepicker.getValue());
        alumno.setNota(Double.parseDouble(notafield.getText()));

        try {
            alumnosRepository.update(alumno);
            alumnotabla.setItems(alumnosRepository.findAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onActionDeleteAlumno(ActionEvent actionEvent) {
        Alumno alumno = alumnotabla.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminando alumno...");
        alert.setHeaderText("Esta a punto de borrar a " + alumno.getNombre());
        alert.setContentText("¿Está seguro de que desea eliminar el alumno?");
        Optional<ButtonType> button = alert.showAndWait();
        if(button.get() == ButtonType.OK) {
            try {
                alumnosRepository.delete(alumno);
                alumnotabla.setItems(alumnosRepository.findAll());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            alert.close();
        }
    }

    public void onActionClearAlumno(ActionEvent actionEvent) {
        Alumno alumno = alumnotabla.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Limpiando datos del alumno...");
        alert.setHeaderText("Esta a punto de resetear a " + alumno.getNombre());
        alert.setContentText("¿Está seguro de que desea resetear el alumno?");
        Optional<ButtonType> button = alert.showAndWait();
        if(button.get() == ButtonType.OK) {
            try {
                nombrefield.clear();
                notafield.clear();

                alumnosRepository.clear(alumno);
                alumnotabla.setItems(alumnosRepository.findAll());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            alert.close();
        }
    }

    public void onActionExit(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrando aplicación...");
        alert.setContentText("¿Está seguro de que desea abandonar la sesión?");
        Optional<ButtonType> button = alert.showAndWait();
        if(button.get() == ButtonType.OK) {
            alumnosRepository.autoSave();
            Platform.exit();
        } else {
            alert.close();
        }
    }

    public void onActionCSVImport(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Importando CSV...");
        alert.setContentText("A continuación va a importar un CSV de su dispositivo.");
        alert.showAndWait();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione su CSV:");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Documentos", "*.csv"));
        Path filePath = fileChooser.showOpenDialog(nombrefield.getScene().getWindow()).toPath();

        try {
            alumnosRepository.restore(filePath);
            alumnotabla.setItems(alumnosRepository.findAll());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onActionJSONExport(ActionEvent actionEvent) throws SQLException, IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exportando JSON...");
        alert.setContentText("A continuación va a exportar un JSON a su dispositivo.");
        alert.showAndWait();

        alumnosRepository.backUp();
    }

    public void onActionTXTInform(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Creando informe...");
        alert.setContentText("Informe creado con éxito.");
        alert.showAndWait();

        FileOutputStream fos;
        ObjectOutputStream oos;
        try {
            fos = new FileOutputStream(System.getProperty("user.dir") + File.separator + "data" + File.separator + "txt" + File.separator + "informe.txt");
            oos = new ObjectOutputStream(fos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DoubleSummaryStatistics stats;
        try {
            stats = alumnosRepository.findAll().stream().mapToDouble(Alumno::getNota).summaryStatistics();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String h = "=== ESTADISTICAS DE ALUMNOS ===" + "\n"
                + "· Alumno con la nota más alta: " + stats.getMax() + "\n"
                + "· Alumno con la nota más baja: " + stats.getMin() +"\n"
                + "· Nota media: " + stats.getAverage() +"\n"
                ;

        try {
            oos.writeBytes(h);
            oos.close();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}