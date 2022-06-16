package es.dam.repaso03.services;

import es.dam.repaso03.dto.AlumnoDTO;
import es.dam.repaso03.models.Alumno;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StorageCSV implements IStorageCSV<AlumnoDTO> {
    public static StorageCSV instance;

    private final String csvFile = System.getProperty("user.dir") + File.separator + "data" + File.separator + "csv" + File.separator + "alumnos.csv";

    //SINGLETON
    public static StorageCSV getInstance() {
        if (instance == null) {
            instance = new StorageCSV();
        }
        return instance;
    }

    @Override
    public List<AlumnoDTO> restore(Path filePath) {
        File fichero = null;
        BufferedReader f = null;
        List<AlumnoDTO> alumnos = new ArrayList<>();
        try {
            fichero = new File(String.valueOf(filePath));
            f = new BufferedReader(new FileReader(fichero));
            f.skip(35);

            String linea;
            while ((linea = f.readLine()) != null) {
                alumnos.add(getAlumnos(linea));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (f != null) {
                try {
                    f.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return alumnos;
    }

    @Override
    public void autoSave(List<AlumnoDTO> alumnos, boolean append) {
        File fichero = null;
        PrintWriter f = null;
        try {
            fichero = new File(csvFile);
            f = new PrintWriter(new FileWriter(csvFile, append));

            for (AlumnoDTO a : alumnos) {
                f.println(a.toFile());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (f != null) {
                f.close();
            }
        }
    }

    private AlumnoDTO getAlumnos(String linea) {
        String[] campos = linea.split(",");
        String id = campos[0];
        String nombre = campos[1];
        LocalDate nacimiento = LocalDate.parse(campos[2]);
        double nota = Double.parseDouble(campos[3]);
        return new AlumnoDTO(id, nombre, nacimiento, nota);
    }
}
