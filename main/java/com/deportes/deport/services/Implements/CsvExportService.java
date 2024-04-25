package com.deportes.deport.services.Implements;

import org.springframework.stereotype.Service;
import com.mashape.unirest.http.Unirest;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class CsvExportService {

    public void exportFootballLeaguesToCsv(String filePath) {
        try {
            // Hacer la solicitud HTTP
            com.mashape.unirest.http.HttpResponse<com.mashape.unirest.http.JsonNode> response = 
                Unirest.get("https://v3.football.api-sports.io/leagues")
                    .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                    .header("x-rapidapi-host", "v3.football.api-sports.io")
                    .asJson();

            // Obtener el cuerpo de la respuesta como cadena
            String responseBody = response.getBody().toString();

            // Parsear la respuesta JSON
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray leagues = jsonObject.getJSONArray("response");

            // Crear un nuevo archivo CSV
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                // Escribir encabezados
                writer.write("ID,Nombre,País,Inicio Temporada,Fin Temporada\n");

                // Iterar sobre las ligas y escribir en el archivo CSV
                for (int i = 0; i < leagues.length(); i++) {
                    JSONObject league = leagues.getJSONObject(i).getJSONObject("league");
                    JSONObject country = leagues.getJSONObject(i).getJSONObject("country");
                    JSONArray seasons = leagues.getJSONObject(i).getJSONArray("seasons");

                    for (int j = 0; j < seasons.length(); j++) {
                        JSONObject season = seasons.getJSONObject(j);

                        // Escribir datos de la liga en el archivo CSV
                        writer.write(String.format("%d,%s,%s,%s,%s\n",
                                league.getInt("id"),
                                league.getString("name"),
                                country.getString("name"),
                                season.getString("start"),
                                season.getString("end")));
                    }
                }
            }

            System.out.println("Los datos se han exportado correctamente a " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exportTimezonesToCsv(String filePath) {
        try {
            // Hacer la solicitud HTTP
            com.mashape.unirest.http.HttpResponse<com.mashape.unirest.http.JsonNode> response = 
                Unirest.get("https://v3.football.api-sports.io/timezone")
                    .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                    .header("x-rapidapi-host", "v3.football.api-sports.io")
                    .asJson();

            // Obtener el cuerpo de la respuesta como cadena
            String responseBody = response.getBody().toString();

            // Parsear la respuesta JSON
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray timezones = jsonObject.getJSONArray("response");

            // Crear un nuevo archivo CSV
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                // Escribir encabezados
                writer.write("Zona Horaria\n");

                // Iterar sobre las zonas horarias y escribir en el archivo CSV
                for (int i = 0; i < timezones.length(); i++) {
                    // Escribir datos de la zona horaria en el archivo CSV
                    writer.write(timezones.getString(i) + "\n");
                }
            }

            System.out.println("Los datos de las zonas horarias se han exportado correctamente a " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}