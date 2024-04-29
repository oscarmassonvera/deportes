package com.deportes.deport.services.Implements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class listarPaisesporIdCsv {
    public void exportAllCountriesToCsv(String filePath) {
        try {
            // Leer el archivo CSV creado anteriormente
            List<String[]> data = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    data.add(parts);
                }
            }

            // Crear un nuevo archivo CSV para todos los países
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("D:/ProyectosWebJava/deport/excells/all_countries.csv"))) {
                // Escribir encabezados
                writer.write("ID,País\n");

                // Escribir todos los países en el archivo CSV
                for (String[] parts : data) {
                    writer.write(String.format("%s,%s\n", parts[0], parts[2]));
                }
            }

            System.out.println("Todos los países se han exportado correctamente a all_countries.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final String FILE_PATH = "D:/ProyectosWebJava/deport/excells/all_countries.csv";

    public String findCountryIdByName(String countryName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[1].equalsIgnoreCase(countryName)) {
                    return parts[0];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; // Si el país no se encuentra, se devuelve null
    }
}