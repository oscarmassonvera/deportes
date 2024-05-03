package com.deportes.deport.services.Implements;

import org.springframework.stereotype.Service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;



@Service
public class CsvExportService {

//----------------------------------------------------------------------------------------------------------------------------------------//

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

//----------------------------------------------------------------------------------------------------------------------------------------//

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

//----------------------------------------------------------------------------------------------------------------------------------------//

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

//----------------------------------------------------------------------------------------------------------------------------------------//

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

//----------------------------------------------------------------------------------------------------------------------------------------//

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


    // public static void exportTeamStatisticsToCsv(String filePath, String leagueId, String teamId, String season) {
    //     try {
    //         String apiUrl = String.format("https://v3.football.api-sports.io/teams/statistics?league=%s&team=%s&season=%s", leagueId, teamId, season);

    //         JsonNode response = Unirest.get(apiUrl)
    //                 .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
    //                 .header("x-rapidapi-host", "v3.football.api-sports.io")
    //                 .asJson()
    //                 .getBody();

    //         JSONObject jsonResponse = response.getObject();
    //         JSONObject statisticsData = jsonResponse.getJSONObject("response");

    //         // Escribir los datos en el archivo CSV
    //         try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
    //             // Escribir encabezados
    //             writer.write("Statistic, Value\n");

    //             // Recorrer el objeto statisticsData y escribir cada clave y valor en una línea del archivo CSV
    //             writeDataToCsv(statisticsData, "", writer);
    //         }

    //         System.out.println("Los datos de estadísticas del equipo se han exportado correctamente a " + filePath);

    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    // private static void writeDataToCsv(JSONObject data, String parentKey, BufferedWriter writer) throws IOException {
    //     for (String key : data.keySet()) {
    //         Object value = data.get(key);

    //         if (value instanceof JSONObject) {
    //             // Si el valor es un JSONObject, llamamos recursivamente a esta función
    //             writeDataToCsv((JSONObject) value, parentKey + key + ".", writer);
    //         } else if (value instanceof JSONArray) {
    //             // Si el valor es un JSONArray, escribimos cada elemento del array en una nueva línea del archivo CSV
    //             JSONArray jsonArray = (JSONArray) value;
    //             for (int i = 0; i < jsonArray.length(); i++) {
    //                 writer.write(String.format("%s%s[%d], %s\n", parentKey, key, i, jsonArray.get(i).toString()));
    //             }
    //         } else {
    //             // Si el valor no es un JSONObject ni un JSONArray, lo escribimos en el archivo CSV
    //             writer.write(String.format("%s%s, %s\n", parentKey, key, value.toString()));
    //         }
    //     }
    // }



//----------------------------------------------------------------------------------------------------------------------------------------//

    public static void exportTeamStatisticsToCsv(String filePath, String leagueName, String teamName, String season) {
        try {
            System.out.println(leagueName);
            // Buscar el ID de la liga en el archivo football_leagues.csv
            String leagueId = findLeagueIdByName(leagueName);
            if (leagueId.isEmpty()) {
                System.out.println("No se encontró la liga en el archivo football_leagues.csv");
                return;
            }

            // Buscar el ID del equipo usando el método findTeamIdByName
            Integer teamId = findTeamIdByName(teamName);
            if (teamId == null || teamId == 0) {
                System.out.println("No se encontró el equipo");
                return;
            }

            // Llamar al método original exportTeamStatisticsToCsv con los IDs encontrados
            exportTeamStatisticsToCsvId(filePath, leagueId, teamId.toString(), season);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String findLeagueIdByName(String leagueName) {
        String csvFile = "D:/ProyectosWebJava/deport/excells/football_leagues.csv";
        String line;
        String csvSplitBy = ",";
        String leagueId = "";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);
                if (data.length >= 2 && data[1].equalsIgnoreCase(leagueName)) {
                    leagueId = data[0];
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return leagueId;
    }

        public static void exportTeamStatisticsToCsvId(String filePath, String leagueId, String teamId, String season) {
            try {
                String apiUrl = String.format("https://v3.football.api-sports.io/teams/statistics?league=%s&team=%s&season=%s", leagueId, teamId, season);
    
                JsonNode response = Unirest.get(apiUrl)
                        .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                        .header("x-rapidapi-host", "v3.football.api-sports.io")
                        .asJson()
                        .getBody();
    
                JSONObject jsonResponse = response.getObject();
                JSONObject statisticsData = jsonResponse.getJSONObject("response");
    
                // Escribir los datos en el archivo CSV
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                    // Escribir encabezados
                    writer.write("Statistic, Value\n");
    
                    // Recorrer el objeto statisticsData y escribir cada clave y valor en una línea del archivo CSV
                    writeDataToCsv(statisticsData, "", writer);
                }
    
                System.out.println("Los datos de estadísticas del equipo se han exportado correctamente a " + filePath);
    
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
        private static void writeDataToCsv(JSONObject data, String parentKey, BufferedWriter writer) throws IOException {
            for (String key : data.keySet()) {
                Object value = data.get(key);
    
                if (value instanceof JSONObject) {
                    // Si el valor es un JSONObject, llamamos recursivamente a esta función
                    writeDataToCsv((JSONObject) value, parentKey + key + ".", writer);
                } else if (value instanceof JSONArray) {
                    // Si el valor es un JSONArray, escribimos cada elemento del array en una nueva línea del archivo CSV
                    JSONArray jsonArray = (JSONArray) value;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        writer.write(String.format("%s%s[%d], %s\n", parentKey, key, i, jsonArray.get(i).toString()));
                    }
                } else {
                    // Si el valor no es un JSONObject ni un JSONArray, lo escribimos en el archivo CSV
                    writer.write(String.format("%s%s, %s\n", parentKey, key, value.toString()));
                }
            }
        }





    // AKI TERMINA EL CODIGO DE TEAMSTADICTIC   OK 

//----------------------------------------------------------------------------------------------------------------------------------------//

    // team por nombre get("https://v3.football.api-sports.io/teams?name=manchester united");
    // DEVUELVE EL ID DEL EQUIPO DADO
    public static Integer findTeamIdByName(String teamName) {
        try {
            String apiUrl = "https://v3.football.api-sports.io/teams?name=" + teamName;

            // Realizar la solicitud HTTP GET
            HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                    .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                    .header("x-rapidapi-host", "v3.football.api-sports.io")
                    .asJson();

            // Analizar la respuesta JSON
            JSONObject jsonResponse = response.getBody().getObject();
            JSONArray teamsArray = jsonResponse.getJSONArray("response");

            // Verificar si se encontraron equipos
            if (teamsArray.length() > 0) {
                // Tomar el primer equipo de la lista (asumiendo que el nombre es único)
                JSONObject team = teamsArray.getJSONObject(0);
                
                // Verificar si el objeto del equipo contiene la clave "team"
                if (team.has("team")) {
                    // Extraer el objeto del equipo
                    JSONObject teamObject = team.getJSONObject("team");
                    
                    // Verificar si el objeto del equipo contiene la clave "id"
                    if (teamObject.has("id")) {
                        // Devolver el ID del equipo
                        return teamObject.getInt("id");
                    }
                }
            }

            // Si no se encuentra ningún equipo o no se puede extraer el ID, devolver un 0
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            // En caso de error, devolver una cadena vacía
            return 0;
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------//

        // Venues ESTADIOS

        public static void fetchAndSaveVenues(String country, String filePath) {
            try {
                // Construir la URL de la API
                String apiUrl = "https://v3.football.api-sports.io/venues?country=" + country;
    
                // Realizar la solicitud HTTP GET
                HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                        .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                        .header("x-rapidapi-host", "v3.football.api-sports.io")
                        .asJson();
    
                // Obtener el cuerpo de la respuesta
                JsonNode responseBody = response.getBody();
    
                // Obtener el arreglo de venues del cuerpo de la respuesta
                JSONArray venuesArray = responseBody.getObject().getJSONArray("response");
                

                //System.out.println(venuesArray);


                // Guardar los datos en un archivo CSV
                saveDataToCsv(venuesArray, filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
        private static void saveDataToCsv(JSONArray venuesArray, String filePath) {
            try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
                // Escribir encabezados en el archivo CSV
                writer.writeNext(new String[]{"ID", "Name", "Country", "Image", "Address", "City", "Capacity", "Surface"});
        
                // Iterar sobre cada objeto de venue en el JSONArray y escribir los datos en el archivo CSV
                for (int i = 0; i < venuesArray.length(); i++) {
                    JSONObject venue = venuesArray.getJSONObject(i);
                    String id = venue.isNull("id") ? "" : String.valueOf(venue.getInt("id"));
                    String name = venue.isNull("name") ? "" : venue.getString("name");
                    String country = venue.isNull("country") ? "" : venue.getString("country");
                    String image = venue.isNull("image") ? "" : venue.getString("image");
                    String address = venue.isNull("address") ? "" : venue.getString("address");
                    String city = venue.isNull("city") ? "" : venue.optString("city", "");
                    String capacity = venue.isNull("capacity") ? "" : String.valueOf(venue.getInt("capacity"));
                    String surface = venue.isNull("surface") ? "" : venue.getString("surface");
        
                    writer.writeNext(new String[]{id, name, country, image, address, city, capacity, surface});
                }
        
                System.out.println("Los datos se han guardado correctamente en " + filePath);
            } catch (IOException e) {
                System.err.println("Error al guardar los datos en " + filePath + ": " + e.getMessage());
            }
        }
        
//----------------------------------------------------------------------------------------------------------------------------------------//

        // Standings





























}


