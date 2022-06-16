package es.dam.repaso03.services;

import es.dam.repaso03.dto.AlumnoDTO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.dam.repaso03.utils.LocalDateAdapter;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

public class StorageJSON implements IStorageJSON {
    private static StorageJSON instance;

    private final String backupFile = System.getProperty("user.dir") + File.separator + "data" + File.separator + "json" + File.separator +"alumnos.json";

    //SINGLETON
    public static StorageJSON getInstance() {
        if (instance == null) {
            instance = new StorageJSON();
        }
        return instance;
    }

    @Override
    public void backUp(List<AlumnoDTO> alumnos) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();

        boolean result = false;
        PrintWriter f = null;
        try {
            f = new PrintWriter(new FileWriter(backupFile));
            f.println(gson.toJson(alumnos));
            result = true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (f != null) {
                f.close();
            }
        }
    }
}
