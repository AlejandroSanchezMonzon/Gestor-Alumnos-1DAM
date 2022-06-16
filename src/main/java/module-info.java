module es.dam.repaso {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires lombok;
    requires com.google.gson;

    opens es.dam.repaso03 to javafx.fxml;
    exports es.dam.repaso03;

    opens es.dam.repaso03.controllers to javafx.fxml;
    exports es.dam.repaso03.controllers;

    opens es.dam.repaso03.models to com.google.gson;
    exports es.dam.repaso03.models;

    opens es.dam.repaso03.dto to com.google.gson;
    exports es.dam.repaso03.dto;
}