package com.deportes.deport.services.Implements;

import org.springframework.stereotype.Service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
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

    public void exportCountriesToCsv(String filePath) {
        try {
            HttpResponse<JsonNode> response = Unirest.get("https://v3.football.api-sports.io/countries")
                    .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                    .header("x-rapidapi-host", "v3.football.api-sports.io")
                    .asJson();
    
            JSONObject jsonObject = response.getBody().getObject();
            JSONArray countries = jsonObject.getJSONArray("response");
    
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write("Nombre, Código, Bandera\n");
    
                for (int i = 0; i < countries.length(); i++) {
                    JSONObject country = countries.getJSONObject(i);
                    String countryCode = country.get("code").toString();
                    String flag = country.get("flag").toString();  // Convertir el valor a String
                    writer.write(country.getString("name") + "," + countryCode + "," + flag + "\n");
                }
            }
    
            System.out.println("Los datos de los países se han exportado correctamente a " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exportLeaguesSeasonsToCsv(String filePath) {
        try {
            HttpResponse<JsonNode> response = Unirest.get("https://v3.football.api-sports.io/leagues/seasons")
                    .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                    .header("x-rapidapi-host", "v3.football.api-sports.io")
                    .asJson();
    
            JSONObject jsonObject = response.getBody().getObject();
            JSONArray seasons = jsonObject.getJSONArray("response");
    
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write("Temporada\n");
    
                for (int i = 0; i < seasons.length(); i++) {
                    int season = seasons.getInt(i);
                    writer.write(season + "\n");
                }
            }
    
            System.out.println("Los datos de las temporadas de la liga se han exportado correctamente a " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        public void exportTeamsToCsv(String filePath, String leagueName, String season) throws UnirestException {
    try {
        // Buscar el ID de la liga en el archivo football_leagues.csv
        String leagueId = "";
        try (BufferedReader br = new BufferedReader(new FileReader("D:/ProyectosWebJava/deport/excells/football_leagues.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[1].equalsIgnoreCase(leagueName)) {
                    leagueId = data[0];
                    break;
                }
            }
        }if (leagueId.isEmpty()) {
            System.out.println("No se encontró la liga en el archivo football_leagues.csv");
            return;
        }
    
        String apiUrl = String.format("https://v3.football.api-sports.io/teams?league=%s&season=%s", leagueId, season);
    
        HttpResponse<JsonNode> response = Unirest.get(apiUrl)
            .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
            .header("x-rapidapi-host", "v3.football.api-sports.io")
            .asJson();
    
        JSONObject jsonResponse = response.getBody().getObject();
        JSONArray teamsArray = jsonResponse.getJSONArray("response");
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("ID, Nombre Equipo, Código Equipo, País, Fundado, Nacional, Logo, Nombre Sede, Dirección, Ciudad, Capacidad, Superficie, Imagen Sede\n");
    
            for (int i = 0; i < teamsArray.length(); i++) {
                JSONObject teamData = teamsArray.getJSONObject(i);
    
                String logo = teamData.getJSONObject("team").optString("logo", "");
                String name = teamData.getJSONObject("team").getString("name");
                String code = teamData.getJSONObject("team").optString("code", "");
                String country = teamData.getJSONObject("team").optString("country", "");
                int founded = teamData.getJSONObject("team").optInt("founded", 0);
                boolean national = teamData.getJSONObject("team").optBoolean("national", false);
                JSONObject venueData = teamData.getJSONObject("venue");
                String venueName = venueData.optString("name", "");
                String address = venueData.optString("address", "");
                String city = venueData.optString("city", "");
                int capacity = venueData.optInt("capacity", 0);
                String surface = venueData.optString("surface", "");
                String venueImage = venueData.optString("image", "");
    
                writer.write(String.format("%d, %s, %s, %s, %d, %b, %s, %s, %s, %s, %d, %s, %s\n",
                        i + 1, name, code, country, founded, national, logo, venueName, address, city, capacity, surface, venueImage));
            }
        }
    
        System.out.println("Los datos de todos los equipos y sus sedes se han exportado correctamente a " + filePath);
    
    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    // // Obtén todas las estadísticas de un {equipo} en una {liga} y una {temporada}
    // get("https://v3.football.api-sports.io/teams/statistics?league=39&team=33&season=2019");

   
    

    public void exportTeamStatisticsToCsv(String filePath, String leagueId, String teamId, String season) {
        try {
            String apiUrl = String.format("https://v3.football.api-sports.io/teams/statistics?league=%s&team=%s&season=%s", leagueId, teamId, season);

            JsonNode response = Unirest.get(apiUrl)
                    .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                    .header("x-rapidapi-host", "v3.football.api-sports.io")
                    .asJson()
                    .getBody();

            JSONObject jsonResponse = response.getObject();
            JSONObject statisticsData = jsonResponse.getJSONObject("response");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write("Statistic, Value\n");

                for (String key : statisticsData.keySet()) {
                    Object value = statisticsData.get(key);
                    writer.write(String.format("%s, %s\n", key, value.toString()));
                }
            }

            System.out.println("Los datos de estadísticas del equipo se han exportado correctamente a " + filePath);

        } catch (UnirestException | IOException e) {
            e.printStackTrace();
        }
    }



    
}