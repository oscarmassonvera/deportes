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

import org.apache.http.HttpStatus;
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

        // Get all Standings from one {league} & {season} Informacion de clasificacion de toda la liga en una season especifica
        // get("https://v3.football.api-sports.io/standings?league=39&season=2019");


        // imprimir por consola la estructura de datos 

        public void fetchAndPrintStandings(int leagueId, int season) {
            try {
                // Construir la URL de la API con los parámetros league y season
                String apiUrl = String.format("https://v3.football.api-sports.io/standings?league=%d&season=%d", leagueId, season);
        
                // Realizar la solicitud HTTP GET
                HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                        .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                        .header("x-rapidapi-host", "v3.football.api-sports.io")
                        .asJson();
        
                // Obtener el cuerpo de la respuesta
                JsonNode responseBody = response.getBody();
        
                // Imprimir los datos de la respuesta JSON por consola
                System.out.println(responseBody.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        // Guardar en archivo csv 


        public static int getLeagueId(String leagueName, String leaguesFilePath) {
            try {
                try (BufferedReader reader = new BufferedReader(new FileReader(leaguesFilePath))) {
                    String line;
        
                    // Leer el archivo CSV línea por línea
                    while ((line = reader.readLine()) != null) {
                        // Dividir la línea en partes utilizando la coma como separador
                        String[] parts = line.split(",");
                        if (parts.length >= 2) {
                            // Obtener el nombre de la liga (asumiendo que es el segundo campo)
                            String leagueNameFromFile = parts[1].trim();
                            // Verificar si el nombre de la liga coincide aproximadamente con el nombre proporcionado
                            if (leagueNameFromFile.equalsIgnoreCase(leagueName.trim())) {
                                // Devolver el ID de la liga
                                return Integer.parseInt(parts[0].trim());
                            }
                        }
                    }
                }
                // Si no se encuentra el nombre de la liga, devolver -1
                return -1;
        
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
        


        public static void fetchAndSaveStandings(int leagueId, int season, String filePath) {
            try {
                // Construir la URL de la API con los parámetros league y season
                String apiUrl = String.format("https://v3.football.api-sports.io/standings?league=%d&season=%d", leagueId, season);
        
                // Realizar la solicitud HTTP GET
                HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                        .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                        .header("x-rapidapi-host", "v3.football.api-sports.io")
                        .asJson();
        
                // Obtener el cuerpo de la respuesta
                JsonNode responseBody = response.getBody();
        
                // Extraer los datos de la respuesta JSON
                JSONObject leagueData = responseBody.getObject().getJSONArray("response").getJSONObject(0).getJSONObject("league");
                JSONArray standingsArray = leagueData.getJSONArray("standings").getJSONArray(0);
        
                // Crear un objeto FileWriter para escribir en el archivo CSV
                FileWriter csvWriter = new FileWriter(filePath);
        
                // Escribir la cabecera en el archivo CSV
                csvWriter.append("Posición,Equipo,PJ,PG,PE,PP,GF,GC,DG,Puntos,Liga,País,Bandera,Logo,Descripción,Actualización\n");
        
                // Iterar sobre los datos y escribir cada fila en el archivo CSV
                for (int i = 0; i < standingsArray.length(); i++) {
                    JSONObject teamData = standingsArray.getJSONObject(i);
                    String position = teamData.isNull("rank") ? "" : String.valueOf(teamData.getInt("rank"));
                    String teamName = teamData.getJSONObject("team").isNull("name") ? "" : teamData.getJSONObject("team").getString("name");
                    String played = teamData.getJSONObject("all").isNull("played") ? "0" : String.valueOf(teamData.getJSONObject("all").getInt("played"));
                    String win = teamData.getJSONObject("all").isNull("win") ? "0" : String.valueOf(teamData.getJSONObject("all").getInt("win"));
                    String draw = teamData.getJSONObject("all").isNull("draw") ? "0" : String.valueOf(teamData.getJSONObject("all").getInt("draw"));
                    String lose = teamData.getJSONObject("all").isNull("lose") ? "0" : String.valueOf(teamData.getJSONObject("all").getInt("lose"));
                    String goalsFor = teamData.getJSONObject("all").getJSONObject("goals").isNull("for") ? "0" : String.valueOf(teamData.getJSONObject("all").getJSONObject("goals").getInt("for"));
                    String goalsAgainst = teamData.getJSONObject("all").getJSONObject("goals").isNull("against") ? "0" : String.valueOf(teamData.getJSONObject("all").getJSONObject("goals").getInt("against"));
                    String goalsDiff = teamData.isNull("goalsDiff") ? "0" : String.valueOf(teamData.getInt("goalsDiff"));
                    String points = teamData.isNull("points") ? "0" : String.valueOf(teamData.getInt("points"));
                    String league = leagueData.isNull("name") ? "" : leagueData.getString("name");
                    String country = leagueData.isNull("country") ? "" : leagueData.getString("country");
                    String flag = leagueData.isNull("flag") ? "" : leagueData.getString("flag");
                    String logo = leagueData.isNull("logo") ? "" : leagueData.getString("logo");
                    String description = teamData.isNull("description") ? "" : teamData.getString("description");
                    String update = teamData.isNull("update") ? "" : teamData.getString("update");
        
                    // Escribir la fila en el archivo CSV
                    csvWriter.append(position + "," + teamName + "," + played + "," + win + "," + draw + "," + lose + ","
                            + goalsFor + "," + goalsAgainst + "," + goalsDiff + "," + points + "," + league + ","
                            + country + "," + flag + "," + logo + "," + description + "," + update + "\n");
                }
        
                // Cerrar el objeto FileWriter
                csvWriter.flush();
                csvWriter.close();
        
                // Imprimir mensaje de éxito
                System.out.println("Datos guardados en el archivo CSV: " + filePath);
        
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



//----------------------------------------------------------------------------------------------------------------------------------------//


    // Fixtures
    // Rounds


    // IMPRIMIR POR CONSOLA 

    public static void fetchRounds(int leagueId, int season) throws UnirestException {
        String apiUrl = String.format("https://v3.football.api-sports.io/fixtures/rounds?league=%d&season=%d", leagueId, season);

        // Realizar la solicitud HTTP GET
        HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                        .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                        .header("x-rapidapi-host", "v3.football.api-sports.io")
                        .asJson();

        // Obtener el cuerpo de la respuesta
        JsonNode responseBody = response.getBody();

        // Mostrar el JSON por consola
        System.out.println(responseBody);
    }


    // save csv



    public static void fetchRounds(int leagueId, int season, String filePath) throws UnirestException {
        try {
            String apiUrl = String.format("https://v3.football.api-sports.io/fixtures/rounds?league=%d&season=%d", leagueId, season);

            // Realizar la solicitud HTTP GET
            HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                            .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                            .header("x-rapidapi-host", "v3.football.api-sports.io")
                            .asJson();

            // Obtener el cuerpo de la respuesta
            JsonNode responseBody = response.getBody();

            // Extraer los datos del JSON
            JSONArray roundsArray = responseBody.getObject().getJSONArray("response");

            // Crear un objeto FileWriter para escribir en el archivo CSV
            FileWriter csvWriter = new FileWriter(filePath);

            // Escribir las rondas en el archivo CSV
            for (int i = 0; i < roundsArray.length(); i++) {
                String round = roundsArray.getString(i);
                csvWriter.append(round + "\n");
            }

            // Cerrar el objeto FileWriter
            csvWriter.flush();
            csvWriter.close();

            // Imprimir mensaje de éxito
            System.out.println("Datos guardados en el archivo CSV: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//----------------------------------------------------------------------------------------------------------------------------------------//


    // Fixtures - calendario imprimir en consola 
    // Obtener todos los partidos disponibles de una liga y temporada específica
    // https://v3.football.api-sports.io/fixtures?league=39&season=2019



    // Imprimir por consola     

    public static void fetchFixtures(int leagueId, int season) throws UnirestException {
        String apiUrl = String.format("https://v3.football.api-sports.io/fixtures?league=%d&season=%d", leagueId, season);
    
        // Realizar la solicitud HTTP GET
        HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                        .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                        .header("x-rapidapi-host", "v3.football.api-sports.io")
                        .asJson();
    
        // Obtener el cuerpo de la respuesta
        JsonNode responseBody = response.getBody();
    
        // Mostrar el JSON por consola
        System.out.println(responseBody);
    }


    // save csv 


    public static void fetchAndSaveFixtures(int leagueId, int season, String filePath) {
        try {
            // Construir la URL de la API con los parámetros league y season
            String apiUrl = String.format("https://v3.football.api-sports.io/fixtures?league=%d&season=%d", leagueId, season);
    
            // Realizar la solicitud HTTP GET
            HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                    .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                    .header("x-rapidapi-host", "v3.football.api-sports.io")
                    .asJson();
    
            // Obtener el cuerpo de la respuesta
            JsonNode responseBody = response.getBody();
    
            // Extraer los datos del JSON
            JSONArray fixturesArray = responseBody.getObject().getJSONArray("response");
    
            // Crear un objeto FileWriter para escribir en el archivo CSV
            FileWriter csvWriter = new FileWriter(filePath);
    
            // Escribir la cabecera en el archivo CSV
            csvWriter.append("ID, Fecha, Ciudad, Estadio, Equipo Local, Logo Equipo Local, Equipo Visitante, Logo Equipo Visitante\n");
    
            // Iterar sobre los datos y escribir cada fila en el archivo CSV
            for (int i = 0; i < fixturesArray.length(); i++) {
                JSONObject fixture = fixturesArray.getJSONObject(i);
                String id = String.valueOf(fixture.getJSONObject("fixture").getInt("id"));
                String date = fixture.getJSONObject("fixture").getString("date").split("T")[0];
                String city = fixture.getJSONObject("fixture").getJSONObject("venue").getString("city");
                String stadium = fixture.getJSONObject("fixture").getJSONObject("venue").getString("name");
                String homeTeam = fixture.getJSONObject("teams").getJSONObject("home").getString("name");
                String homeTeamLogo = fixture.getJSONObject("teams").getJSONObject("home").getString("logo");
                String awayTeam = fixture.getJSONObject("teams").getJSONObject("away").getString("name");
                String awayTeamLogo = fixture.getJSONObject("teams").getJSONObject("away").getString("logo");
    
                // Escribir la fila en el archivo CSV
                csvWriter.append(id + "," + date + "," + city + "," + stadium + "," + homeTeam + "," + homeTeamLogo + "," + awayTeam + "," + awayTeamLogo + "\n");
            }
    
            // Cerrar el objeto FileWriter
            csvWriter.flush();
            csvWriter.close();
    
            // Imprimir mensaje de éxito
            System.out.println("Datos guardados en el archivo CSV: " + filePath);
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//----------------------------------------------------------------------------------------------------------------------------------------//

    // Calendario partido por su id info importante para apostar 
    
    // imprimir por pantalla

    public static void fetchFixtureById(String fixtureId) {
        try {
            // Construir la URL de la API
            String apiUrl = "https://v3.football.api-sports.io/fixtures?id=" + fixtureId;

            // Realizar la solicitud HTTP GET
        HttpResponse<JsonNode> response = Unirest.get(apiUrl)
        .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
        .header("x-rapidapi-host", "v3.football.api-sports.io")
        .asJson();

        // Obtener el cuerpo de la respuesta
        JsonNode responseBody = response.getBody();

        // Mostrar el JSON por consola
        System.out.println(responseBody);
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // save csv

    public static void fetchAndSaveFixturesById(String fixtureId, String filePath) {
        try {
            // Construir la URL de la API con el ID del fixture
            String apiUrl = "https://v3.football.api-sports.io/fixtures?id=" + fixtureId;
    
            // Realizar la solicitud HTTP GET
            HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                    .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                    .header("x-rapidapi-host", "v3.football.api-sports.io")
                    .asJson();
    
            // Verificar si la solicitud fue exitosa
            if (response.getStatus() == HttpStatus.SC_OK) {
                // Obtener el cuerpo de la respuesta como un objeto JSON
                JSONObject fixtureObj = response.getBody().getObject();
    
                // Extraer los datos del JSON
                JSONArray responseArray = fixtureObj.getJSONArray("response");
                JSONObject fixtureData = responseArray.getJSONObject(0).getJSONObject("fixture");
                JSONObject venueData = fixtureData.getJSONObject("venue");
                JSONObject leagueData = responseArray.getJSONObject(0).getJSONObject("league");
                JSONObject homeTeamData = responseArray.getJSONObject(0).getJSONObject("teams").getJSONObject("home");
                JSONObject awayTeamData = responseArray.getJSONObject(0).getJSONObject("teams").getJSONObject("away");
                JSONObject scoreData = responseArray.getJSONObject(0).getJSONObject("score");
                JSONObject halftimeData = scoreData.getJSONObject("halftime");
                JSONObject fulltimeData = scoreData.getJSONObject("fulltime");
                JSONObject extratimeData = scoreData.getJSONObject("extratime");
                JSONObject penaltyData = scoreData.getJSONObject("penalty");
    
                // Crear un objeto FileWriter para escribir en el archivo CSV
                FileWriter csvWriter = new FileWriter(filePath);
    
                // Escribir los datos en el archivo CSV
                writeCsvLine(csvWriter, "Date", fixtureData.getString("date").split("T")[0]);
                writeCsvLine(csvWriter, "City", venueData.getString("city"));
                writeCsvLine(csvWriter, "Stadium ID", String.valueOf(venueData.getInt("id")));
                writeCsvLine(csvWriter, "Stadium Name", venueData.getString("name"));
                writeCsvLine(csvWriter, "Home Team ID", String.valueOf(homeTeamData.getInt("id")));
                writeCsvLine(csvWriter, "Home Team Name", homeTeamData.getString("name"));
                writeCsvLine(csvWriter, "Home Team Logo", homeTeamData.getString("logo"));
                writeCsvLine(csvWriter, "Away Team ID", String.valueOf(awayTeamData.getInt("id")));
                writeCsvLine(csvWriter, "Away Team Name", awayTeamData.getString("name"));
                writeCsvLine(csvWriter, "Away Team Logo", awayTeamData.getString("logo"));
                writeCsvLine(csvWriter, "League Country", leagueData.getString("country"));
                writeCsvLine(csvWriter, "League Flag", leagueData.getString("flag"));
                writeCsvLine(csvWriter, "League Name", leagueData.getString("name"));
                writeCsvLine(csvWriter, "League Logo", leagueData.getString("logo"));
                writeCsvLine(csvWriter, "Season", String.valueOf(leagueData.getInt("season")));
                writeCsvLine(csvWriter, "Round", leagueData.getString("round"));
                writeCsvLine(csvWriter, "Fixture ID", String.valueOf(fixtureData.getInt("id")));
                writeCsvLine(csvWriter, "Referee", fixtureData.optString("referee", ""));
                writeCsvLine(csvWriter, "Timestamp", String.valueOf(fixtureData.getInt("timestamp")));
                writeCsvLine(csvWriter, "Timezone", fixtureData.getString("timezone"));
                writeCsvLine(csvWriter, "Status Short", fixtureData.getJSONObject("status").getString("short"));
                writeCsvLine(csvWriter, "Status Long", fixtureData.getJSONObject("status").getString("long"));
                writeCsvLine(csvWriter, "Halftime Home Goals", String.valueOf(halftimeData.optInt("home", 0)));
                writeCsvLine(csvWriter, "Halftime Away Goals", String.valueOf(halftimeData.optInt("away", 0)));
                writeCsvLine(csvWriter, "Fulltime Home Goals", String.valueOf(fulltimeData.optInt("home", 0)));
                writeCsvLine(csvWriter, "Fulltime Away Goals", String.valueOf(fulltimeData.optInt("away", 0)));
                writeCsvLine(csvWriter, "Extratime Home Goals", String.valueOf(extratimeData.optInt("home", 0)));
                writeCsvLine(csvWriter, "Extratime Away Goals", String.valueOf(extratimeData.optInt("away", 0)));
                writeCsvLine(csvWriter, "Penalty Home Goals", String.valueOf(penaltyData.optInt("home", 0)));
                writeCsvLine(csvWriter, "Penalty Away Goals", String.valueOf(penaltyData.optInt("away", 0)));
    
                // Cerrar el objeto FileWriter
                csvWriter.flush();
                csvWriter.close();
    
                // Imprimir mensaje de éxito
                System.out.println("Datos guardados en el archivo CSV: " + filePath);
            } else {
                // Si la solicitud no fue exitosa, imprimir el código de estado
                System.err.println("Error al realizar la solicitud HTTP: " + response.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void writeCsvLine(FileWriter csvWriter, String key, String value) throws IOException {
        csvWriter.append(key + ": " + value + "\n");
    }


//----------------------------------------------------------------------------------------------------------------------------------------//

    // Partidos Ya jugados info                                         (ESTA DE MAS ?)

        // imprimir por pantalla 

        public static void fetchHeadToHead(int teamId1, int teamId2) {
            try {
                // Construir la URL de la API
                String apiUrl = "https://v3.football.api-sports.io/fixtures/headtohead?h2h=" + teamId1 + "-" + teamId2;
        
                // Realizar la solicitud HTTP GET
                HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                        .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                        .header("x-rapidapi-host", "v3.football.api-sports.io")
                        .asJson();
        
                // Obtener el cuerpo de la respuesta
                JsonNode responseBody = response.getBody();
        
                // Mostrar el JSON por consola
                System.out.println(responseBody);
        
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // save csv 

        public static void fetchAndSaveHeadToHead(int teamId1, int teamId2, String filePath) {
            try {
                // Construir la URL de la API con los parámetros teamId1 y teamId2
                String apiUrl = "https://v3.football.api-sports.io/fixtures/headtohead?h2h=" + teamId1 + "-" + teamId2;
    
                // Realizar la solicitud HTTP GET utilizando Unirest
                HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                        .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                        .header("x-rapidapi-host", "v3.football.api-sports.io")
                        .asJson();
    
                // Obtener el cuerpo de la respuesta
                JsonNode responseBody = response.getBody();
    
                // Obtener el arreglo "response" del objeto JSON
                JSONArray responseArray = responseBody.getObject().getJSONArray("response");
    
                // Crear un objeto FileWriter para escribir en el archivo CSV
                FileWriter csvWriter = new FileWriter(filePath);
    
                // Escribir la cabecera en el archivo CSV
                csvWriter.append("Fixture ID,Referee,Timezone,Date,Timestamp,First Period,Second Period,Venue ID,Venue Name,Venue City,Status Long,Status Short,Status Elapsed,League ID,League Name,League Country,League Logo,League Flag,League Season,League Round,Home Team ID,Home Team Name,Home Team Logo,Home Team Winner,Away Team ID,Away Team Name,Away Team Logo,Away Team Winner,Home Goals,Away Goals,Halftime Home Goals,Halftime Away Goals,Fulltime Home Goals,Fulltime Away Goals,Extratime Home Goals,Extratime Away Goals,Penalty Home Goals,Penalty Away Goals\n");
    
                // Iterar sobre los elementos de "response" y escribir cada fila en el archivo CSV
                for (int i = 0; i < responseArray.length(); i++) {
                    JSONObject fixtureObject = responseArray.getJSONObject(i).getJSONObject("fixture");
                    JSONObject periodsObject = fixtureObject.getJSONObject("periods");
                    JSONObject venueObject = fixtureObject.getJSONObject("venue");
                    JSONObject statusObject = fixtureObject.getJSONObject("status");
                    JSONObject leagueObject = responseArray.getJSONObject(i).getJSONObject("league");
                    JSONObject homeTeamObject = responseArray.getJSONObject(i).getJSONObject("teams").getJSONObject("home");
                    JSONObject awayTeamObject = responseArray.getJSONObject(i).getJSONObject("teams").getJSONObject("away");
                    JSONObject goalsObject = responseArray.getJSONObject(i).getJSONObject("goals");
                    JSONObject scoreObject = responseArray.getJSONObject(i).getJSONObject("score");
                
                    // Obtener los datos relevantes de cada objeto JSON y validar si están vacíos
                    String fixtureId = String.valueOf(fixtureObject.optInt("id", 0));
                    String referee = fixtureObject.optString("referee", "");
                    String timezone = fixtureObject.optString("timezone", "");
                    String date = fixtureObject.optString("date", "");
                    String timestamp = String.valueOf(fixtureObject.optLong("timestamp", 0));
                    String firstPeriod = String.valueOf(periodsObject.optLong("first", 0));
                    String secondPeriod = String.valueOf(periodsObject.optLong("second", 0));
                    String venueId = String.valueOf(venueObject.optInt("id", 0));
                    String venueName = venueObject.optString("name", "");
                    String venueCity = venueObject.optString("city", "");
                    String statusLong = statusObject.optString("long", "");
                    String statusShort = statusObject.optString("short", "");
                    String statusElapsed = String.valueOf(statusObject.optInt("elapsed", 0));
                    String leagueId = String.valueOf(leagueObject.optInt("id", 0));
                    String leagueName = leagueObject.optString("name", "");
                    String leagueCountry = leagueObject.optString("country", "");
                    String leagueLogo = leagueObject.optString("logo", "");
                    String leagueFlag = leagueObject.optString("flag", "");
                    String leagueSeason = String.valueOf(leagueObject.optInt("season", 0));
                    String leagueRound = leagueObject.optString("round", "");
                    String homeTeamId = String.valueOf(homeTeamObject.optInt("id", 0));
                    String homeTeamName = homeTeamObject.optString("name", "");
                    String homeTeamLogo = homeTeamObject.optString("logo", "");
                    String homeTeamWinner = String.valueOf(homeTeamObject.optBoolean("winner", false));
                    String awayTeamId = String.valueOf(awayTeamObject.optInt("id", 0));
                    String awayTeamName = awayTeamObject.optString("name", "");
                    String awayTeamLogo = awayTeamObject.optString("logo", "");
                    String awayTeamWinner = String.valueOf(awayTeamObject.optBoolean("winner", false));
                    String homeGoals = String.valueOf(goalsObject.optInt("home", 0));
                    String awayGoals = String.valueOf(goalsObject.optInt("away", 0));
                    String halftimeHomeGoals = String.valueOf(scoreObject.getJSONObject("halftime").optInt("home", 0));
                    String halftimeAwayGoals = String.valueOf(scoreObject.getJSONObject("halftime").optInt("away", 0));
                    String fulltimeHomeGoals = String.valueOf(scoreObject.getJSONObject("fulltime").optInt("home", 0));
                    String fulltimeAwayGoals = String.valueOf(scoreObject.getJSONObject("fulltime").optInt("away", 0));
                    String extratimeHomeGoals = String.valueOf(scoreObject.getJSONObject("extratime").optInt("home", 0));
                    String extratimeAwayGoals = String.valueOf(scoreObject.getJSONObject("extratime").optInt("away", 0));
                    String penaltyHomeGoals = String.valueOf(scoreObject.getJSONObject("penalty").optInt("home", 0));
                    String penaltyAwayGoals = String.valueOf(scoreObject.getJSONObject("penalty").optInt("away", 0));
                
                    // Escribir la fila en el archivo CSV
                    csvWriter.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        fixtureId, referee, timezone, date, timestamp, firstPeriod, secondPeriod, venueId, venueName, venueCity, statusLong, statusShort, statusElapsed, leagueId, leagueName, leagueCountry, leagueLogo, leagueFlag, leagueSeason, leagueRound,
                        homeTeamId, homeTeamName, homeTeamLogo, homeTeamWinner, awayTeamId, awayTeamName, awayTeamLogo, awayTeamWinner, homeGoals, awayGoals, halftimeHomeGoals, halftimeAwayGoals, fulltimeHomeGoals, fulltimeAwayGoals, extratimeHomeGoals, extratimeAwayGoals, penaltyHomeGoals, penaltyAwayGoals));
                }
    
                // Cerrar el objeto FileWriter
                csvWriter.flush();
                csvWriter.close();
    
                // Imprimir mensaje de éxito
                System.out.println("Datos guardados en el archivo CSV: " + filePath);
    
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
        


//----------------------------------------------------------------------------------------------------------------------------------------//
 
    // Statistics Estadisticas de partido especificado por su id ( funciona como la anterior con el id del partido )

    // URL: https://v3.football.api-sports.io/fixtures/statistics?fixture=215662
    // Esta solicitud devuelve todas las estadísticas disponibles para el partido con el ID de fixture 215662. 
    // Estas estadísticas pueden incluir datos como posesión del balón, tiros a puerta, faltas, tarjetas, entre otros.

    // imprimir pantalla

    public static void fetchFixtureStatistics(int fixtureId) {
        try {
            // Construir la URL de la API
            String apiUrl = "https://v3.football.api-sports.io/fixtures/statistics?fixture=" + fixtureId;
    
            // Realizar la solicitud HTTP GET
            HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                    .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                    .header("x-rapidapi-host", "v3.football.api-sports.io")
                    .asJson();
    
            // Obtener el cuerpo de la respuesta
            JsonNode responseBody = response.getBody();
    
            // Mostrar el JSON por consola
            System.out.println(responseBody);
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // save csv 

    public static void fetchAndSaveFixtureStatistics(String fixtureId, String filePath) throws IOException, UnirestException {
        // Construir la URL de la API con el ID del fixture
        String apiUrl = "https://v3.football.api-sports.io/fixtures/statistics?fixture=" + fixtureId;

        // Realizar la solicitud HTTP GET utilizando Unirest
                HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                        .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                        .header("x-rapidapi-host", "v3.football.api-sports.io")
                        .asJson();

        // Verificar si la solicitud fue exitosa
        if (response.getStatus() == 200) {
            // Obtener el cuerpo de la respuesta como un objeto JSON
            JSONObject responseBody = response.getBody().getObject();

            // Extraer los datos del JSON
            JSONArray responseArray = responseBody.getJSONArray("response");
            FileWriter csvWriter = new FileWriter(filePath);

            // Escribir las cabeceras en el archivo CSV
            csvWriter.append("Team,Statistic Type,Value\n");

            // Iterar sobre los datos de cada equipo en el partido
            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject fixtureData = responseArray.getJSONObject(i);
                JSONObject teamData = fixtureData.getJSONObject("team");
                String teamName = teamData.getString("name");
                JSONArray statisticsArray = fixtureData.getJSONArray("statistics");

                // Escribir las estadísticas del equipo en el archivo CSV
                for (int j = 0; j < statisticsArray.length(); j++) {
                    JSONObject statistic = statisticsArray.getJSONObject(j);
                    String statisticType = statistic.getString("type");
                    String value = statistic.optString("value", ""); // Si el valor es nulo, poner un string vacío
                    writeCsvLine(csvWriter, teamName, statisticType, value);
                }
            }

            // Cerrar el objeto FileWriter
            csvWriter.flush();
            csvWriter.close();

            // Imprimir mensaje de éxito
            System.out.println("Datos guardados en el archivo CSV: " + filePath);
        } else {
            // Si la solicitud no fue exitosa, imprimir el código de estado
            System.err.println("Error al realizar la solicitud HTTP: " + response.getStatus());
        }
    }

    private static void writeCsvLine(FileWriter csvWriter, String team, String statisticType, String value) throws IOException {
        csvWriter.append(team + "," + statisticType + "," + value + "\n");
    }



//----------------------------------------------------------------------------------------------------------------------------------------//

    // Events

    // URL: https://v3.football.api-sports.io/fixtures/events?fixture=215662
    // Esta solicitud devuelve todos los eventos disponibles para el partido con el ID de fixture 215662. 
    // Los eventos pueden incluir goles, tarjetas, sustituciones, faltas, entre otros.

    // imprimir pantalla

    public static void fetchFixtureEvents(int fixtureId) {
        try {
            // Construir la URL de la API
            String apiUrl = "https://v3.football.api-sports.io/fixtures/events?fixture=" + fixtureId;
    
            // Realizar la solicitud HTTP GET
            HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                    .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                    .header("x-rapidapi-host", "v3.football.api-sports.io")
                    .asJson();
    
            // Obtener el cuerpo de la respuesta
            JsonNode responseBody = response.getBody();
    
            // Mostrar el JSON por consola
            System.out.println(responseBody);
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // save csv 

    public void fetchAndSaveFixtureEvents(int fixtureId, String filePath) throws IOException {
        String apiUrl = "https://v3.football.api-sports.io/fixtures/events?fixture=" + fixtureId;

        try {
            // Realizar la solicitud HTTP GET con Unirest
            HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                    .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                    .header("x-rapidapi-host", "v3.football.api-sports.io")
                    .asJson();

            // Guardar los eventos en un archivo CSV
            saveEventsToCSV(response.getBody(), filePath);

            System.out.println("Datos guardados correctamente en " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error al obtener y guardar los eventos del partido.", e);
        }
    }

    private void saveEventsToCSV(JsonNode responseBody, String filePath) throws IOException {
        // Verificar si responseBody es nulo
        if (responseBody == null) {
            throw new IllegalArgumentException("El cuerpo de respuesta es nulo.");
        }

        // Convertir el objeto JsonNode a JSONObject
        JSONObject responseJson = responseBody.getObject();

        // Obtener el arreglo de eventos de la respuesta
        JSONArray eventsArray = responseJson.getJSONArray("response");

        // Crear un FileWriter para escribir en el archivo CSV
        try (FileWriter writer = new FileWriter(filePath)) {
            // Escribir la cabecera del CSV
            writer.append("Team Name,Team id,Team Logo,Player Name,Player Id,Time Elapsed,Time Extra,Assist Id,Assist Name,Event Type,Event Detail,Event Comments\n");

            // Iterar sobre cada evento y escribir los datos en el archivo CSV
            for (int i = 0; i < eventsArray.length(); i++) {
                JSONObject event = eventsArray.getJSONObject(i);
                // Obtener los datos de cada evento
                String teamName = event.getJSONObject("team").optString("name", "");
                int teamId = event.getJSONObject("team").optInt("id", 0);
                String teamLogo  = event.getJSONObject("team").optString("logo", "");
            
                String playerName = event.getJSONObject("player").optString("name", "");
                int playerId = event.getJSONObject("player").optInt("id", 0);
            
                int timeElapsed = event.getJSONObject("time").optInt("elapsed", 0);
                String timeExtra = event.getJSONObject("time").optString("extra", "");
            
                int assistId = event.getJSONObject("assist").optInt("id", 0);
                String assistName = event.getJSONObject("assist").optString("name", "");
            
                String eventType = event.optString("type", "");
                String eventDetail = event.optString("detail", "");
                String eventComments = event.optString("comments", "");
            
                // Escribir los datos en una línea del CSV
                writer.append(String.format("%s,%d,%s,%s,%d,%d,%s,%d,%s,%s,%s,%s\n",
                teamName, teamId, teamLogo, playerName, playerId, timeElapsed, timeExtra, assistId, assistName, eventType, eventDetail, eventComments));
            }
        } catch (IOException e) {
            throw new IOException("Error al escribir en el archivo CSV: " + e.getMessage(), e);
        }
    }


    //  Que informacion da este metodo ? 
    //  Team Name: Nombre del equipo.
    //  Team id: Identificador del equipo.
    //  Team Logo: Enlace al logotipo del equipo.
    //  Player Name: Nombre del jugador involucrado en el evento.
    //  Player Id: Identificador del jugador.
    //  Time Elapsed: Tiempo transcurrido en el partido cuando ocurrió el evento.
    //  Time Extra: Tiempo extra, si aplica.
    //  Assist Id: Identificador de asistencia (si hubo alguna).
    //  Assist Name: Nombre del jugador que proporcionó la asistencia.
    //  Event Type: Tipo de evento (por ejemplo, gol, tarjeta).
    //  Event Detail: Detalle adicional sobre el evento.
    //  Event Comments: Comentarios adicionales sobre el evento.

//----------------------------------------------------------------------------------------------------------------------------------------//

        //     Lineups

        // https://v3.football.api-sports.io/fixtures/lineups?fixture=592872: Esta URL devuelve todas las 
        // alineaciones disponibles para el partido con el ID de fixture 592872. Proporciona información 
        // sobre los jugadores alineados en ambos equipos

    // imprimir pantalla

    public static void fetchFixtureLineups(int fixtureId) {
        try {
            // Construir la URL de la API
            String apiUrl = "https://v3.football.api-sports.io/fixtures/lineups?fixture=" + fixtureId;
    
            // Realizar la solicitud HTTP GET
            HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                    .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                    .header("x-rapidapi-host", "v3.football.api-sports.io")
                    .asJson();
    
            // Obtener el cuerpo de la respuesta
            JsonNode responseBody = response.getBody();
    
            // Mostrar el JSON por consola
            System.out.println(responseBody);
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // save csv 



    public void fetchAndSaveLineups(int fixtureId, String filePath) throws IOException {
        String apiUrl = "https://v3.football.api-sports.io/fixtures/lineups?fixture=" + fixtureId;

        try {
            // Realizar la solicitud HTTP GET con Unirest
            HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                    .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                    .header("x-rapidapi-host", "v3.football.api-sports.io")
                    .asJson();

            // Guardar las alineaciones en un archivo CSV
            saveLineupsToCSV(response.getBody(), filePath);

            System.out.println("Datos guardados correctamente en " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error al obtener y guardar las alineaciones del partido.", e);
        }
    }

    private void saveLineupsToCSV(JsonNode responseBody, String filePath) throws IOException {
        // Verificar si responseBody es nulo
        if (responseBody == null) {
            throw new IllegalArgumentException("El cuerpo de respuesta es nulo.");
        }

        // Convertir el objeto JsonNode a JSONObject
        JSONObject responseJson = responseBody.getObject();

        // Obtener el arreglo de alineaciones de la respuesta
        JSONArray lineupsArray = responseJson.getJSONArray("response");

        // Crear un FileWriter para escribir en el archivo CSV
        try (FileWriter writer = new FileWriter(filePath)) {
            // Escribir la cabecera del CSV
            writer.append("Team Name,Formation,Coach Name,Coach Photo,Substitute Number,Substitute Position,Substitute Name,Substitute ID,Starting XI Number,Starting XI Position,Starting XI Name,Starting XI ID\n");

            // Iterar sobre cada alineación y escribir los datos en el archivo CSV
            for (int i = 0; i < lineupsArray.length(); i++) {
                JSONObject lineup = lineupsArray.getJSONObject(i);
                JSONObject team = lineup.getJSONObject("team");
                JSONArray startingXI = lineup.getJSONArray("startXI");
                JSONArray substitutes = lineup.getJSONArray("substitutes");
                JSONObject coach = lineup.getJSONObject("coach");

                // Escribir datos del equipo
                // Escribir datos del equipo
                String teamName = team.isNull("name") ? "" : team.getString("name");
                String formation = lineup.isNull("formation") ? "" : lineup.getString("formation");
                String coachName = coach.isNull("name") ? "" : coach.getString("name");
                String coachPhoto = coach.isNull("photo") ? "" : coach.getString("photo");

                // Escribir datos de los suplentes
                writePlayersToCSV(substitutes, writer, teamName, formation, coachName, coachPhoto);

                // Escribir datos de los titulares
                writePlayersToCSV(startingXI, writer, teamName, formation, coachName, coachPhoto);
            }
        } catch (IOException e) {
            throw new IOException("Error al escribir en el archivo CSV: " + e.getMessage(), e);
        }
    }

    private void writePlayersToCSV(JSONArray playersArray, FileWriter writer, String teamName, String formation, String coachName, String coachPhoto) throws IOException {
        // Iterar sobre cada jugador en el arreglo JSON
        for (int j = 0; j < playersArray.length(); j++) {
            JSONObject player = playersArray.getJSONObject(j);
            JSONObject playerInfo = player.getJSONObject("player");

            // Escribir datos del jugador
            int number = playerInfo.isNull("number") ? 0 : playerInfo.getInt("number");
            String pos = playerInfo.isNull("pos") ? "" : playerInfo.getString("pos");
            String playerName = playerInfo.isNull("name") ? "" : playerInfo.getString("name");
            int playerId = playerInfo.isNull("id") ? 0 : playerInfo.getInt("id");

            // Escribir los datos en una línea del CSV
            writer.append(String.format("%s,%s,%s,%s,%d,%s,%s,%d,%d,%s,%s,%d\n",
                    teamName, formation, coachName, coachPhoto, number, pos, playerName, playerId, number, pos, playerName, playerId));
        }
    }

    // Que da informacion da este metodo ?

    // Team Name: Nombre del equipo.
    // Formation: Formación táctica utilizada por el equipo.
    // Coach Name: Nombre del entrenador del equipo.
    // Coach Photo: Enlace a la foto del entrenador.
    // Substitute Number: Número del jugador suplente.
    // Substitute Position: Posición del jugador suplente.
    // Substitute Name: Nombre del jugador suplente.
    // Substitute ID: ID del jugador suplente.
    // Starting XI Number: Número del jugador titular.
    // Starting XI Position: Posición del jugador titular.
    // Starting XI Name: Nombre del jugador titular.
    // Starting XI ID: ID del jugador titular.

//----------------------------------------------------------------------------------------------------------------------------------------//

    //  Players statistics

    // Get all available players statistics from one {fixture} & {team}
    // get("https://v3.football.api-sports.io/fixtures/players?fixture=169080&team=2284");
    // te dará una visión más detallada y específica de cómo los jugadores de un equipo en particular
    // se desempeñaron en un partido específico

    // funciona con el id del partido y el id del equipo

        // imprimir pantalla

        public static void fetchFixturePlayers(int fixtureId, int teamId) {
            try {
                // Construir la URL de la API
                String apiUrl = "https://v3.football.api-sports.io/fixtures/players?fixture=" + fixtureId + "&team=" + teamId;
    
                // Realizar la solicitud HTTP GET
                HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                        .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                        .header("x-rapidapi-host", "v3.football.api-sports.io")
                        .asJson();
    
                // Obtener el cuerpo de la respuesta
                JsonNode responseBody = response.getBody();
    
                // Mostrar el JSON por consola
                System.out.println(responseBody);
    
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        // save csv
        
        
        public void fetchAndSavePlayersData(int fixtureId, int teamId, String filePath) {
            try {
                // Construir la URL con fixtureId y teamId
                String apiUrl = "https://v3.football.api-sports.io/fixtures/players?fixture=" + fixtureId + "&team=" + teamId;
    
                // Hacer la solicitud HTTP con Unirest
                HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                        .header("x-rapidapi-host", "v3.football.api-sports.io")
                        .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                        .asJson();
    
                // Convertir la respuesta a formato JSON
                JSONObject jsonResponse = response.getBody().getObject();
    
                // Obtener el objeto de respuesta
                JSONArray responseArray = jsonResponse.getJSONArray("response");
    
                // Crear y escribir en el archivo CSV
                FileWriter csvWriter = new FileWriter(filePath);
                csvWriter.append("Team ID,Team Logo,Team Name,Team Update,Player Name,Player ID,Player Photo,Player Position,Player Minutes,Player Number,Player Rating,Player Captain,Player Substitute,Player Total Shots,Player Shots On Target,Player Goals Total,Player Goals Conceded,Player Assists,Player Saves,Player Total Passes,Player Key Passes,Player Passes Accuracy,Player Total Tackles,Player Blocks,Player Interceptions,Player Total Duels,Player Duels Won,Player Dribble Attempts,Player Dribble Success,Player Dribble Past,Player Fouls Drawn,Player Fouls Committed,Player Yellow Cards,Player Red Cards,Player Penalty Won,Player Penalty Committed,Player Penalty Scored,Player Penalty Missed,Player Penalty Saved\n");
    
                // Iterar sobre cada objeto de respuesta
                for (int i = 0; i < responseArray.length(); i++) {
                    JSONObject teamObject = responseArray.getJSONObject(i).getJSONObject("team");
                    String teamName = teamObject.getString("name");
                    String teamid = String.valueOf(teamObject.getInt("id"));
                    String teamlogo = teamObject.getString("logo");
                    String teamupdate = teamObject.getString("update");
    
                    // Obtener la lista de jugadores para cada equipo
                    JSONArray playersArray = responseArray.getJSONObject(i).getJSONArray("players");
    
// Iterar sobre la lista de jugadores
for (int j = 0; j < playersArray.length(); j++) {
    JSONObject playerObject = playersArray.getJSONObject(j).getJSONObject("player");
    String playerName = playerObject.optString("name", "");
    String playerId = String.valueOf(playerObject.optInt("id", 0));
    String playerPhoto = playerObject.optString("photo", "");

    // Obtener estadísticas del jugador si están disponibles
    JSONObject statisticsObject = playersArray.getJSONObject(j).optJSONArray("statistics").optJSONObject(0);
    if (statisticsObject != null) {
        // Obtener estadísticas de juegos
        JSONObject gamesObject = statisticsObject.optJSONObject("games");
        String playerPosition = gamesObject.optString("position", "");
        String playerMinutes = String.valueOf(gamesObject.optInt("minutes", 0));
        String playernumber = String.valueOf(gamesObject.optInt("number", 0));
        String playerrating = gamesObject.optString("rating", "");
        String playercaptain = String.valueOf(gamesObject.optBoolean("captain", false));
        String playersubstitute = String.valueOf(gamesObject.optBoolean("substitute", false));

        // Obtener estadísticas de tiros
        JSONObject shotsObject = statisticsObject.optJSONObject("shots");
        String playertotal = String.valueOf(shotsObject.optInt("total", 0));
        String playeron = String.valueOf(shotsObject.optInt("on", 0));

        // Obtener estadísticas de goles
        JSONObject goalsObject = statisticsObject.optJSONObject("goals");
        String playergoalstotal = String.valueOf(goalsObject.optInt("total", 0));
        String playerconceded = String.valueOf(goalsObject.optInt("conceded", 0));
        String playerassistcs = String.valueOf(goalsObject.optInt("assists", 0));
        String playersaves = String.valueOf(goalsObject.optInt("saves", 0));

        // Obtener estadísticas de pases
        JSONObject passesObject = statisticsObject.optJSONObject("passes");
        String playerpassestotal = String.valueOf(passesObject.optInt("total", 0));
        String playerpasseskey = String.valueOf(passesObject.optInt("key", 0));
        String playerpasesacuracy = passesObject.optString("accuracy", "");

        // Obtener estadísticas de tackles
        JSONObject tacklesObject = statisticsObject.optJSONObject("tackles");
        String playertacklestotal = String.valueOf(tacklesObject.optInt("total", 0));
        String playerblocks = String.valueOf(tacklesObject.optInt("blocks", 0));
        String playerinterceptions = String.valueOf(tacklesObject.optInt("interceptions", 0));

        // Obtener estadísticas de duelos
        JSONObject duelsObject = statisticsObject.optJSONObject("duels");
        String playerduelstotal = String.valueOf(duelsObject.optInt("total", 0));
        String playerwon = String.valueOf(duelsObject.optInt("won", 0));

        // Obtener estadísticas de dribbles
        JSONObject dribblesObject = statisticsObject.optJSONObject("dribbles");
        String playerattempts = String.valueOf(dribblesObject.optInt("attempts", 0));
        String playersuccess = String.valueOf(dribblesObject.optInt("success", 0));
        String playerpast = String.valueOf(dribblesObject.optInt("past", 0));

        // Obtener estadísticas de faltas
        JSONObject foulsObject = statisticsObject.optJSONObject("fouls");
        String playerdrawn = String.valueOf(foulsObject.optInt("drawn", 0));
        String playercommitted = String.valueOf(foulsObject.optInt("committed", 0));

        // Obtener estadísticas de tarjetas
        JSONObject cardsObject = statisticsObject.optJSONObject("cards");
        String playeryellow = String.valueOf(cardsObject.optInt("yellow", 0));
        String playerred = String.valueOf(cardsObject.optInt("red", 0));

        // Obtener estadísticas de penaltis
        JSONObject penaltyObject = statisticsObject.optJSONObject("penalty");
        String playerpenaltywon = String.valueOf(penaltyObject.optInt("won", 0));
        String playerpenaltycommite = String.valueOf(penaltyObject.optInt("commited", 0));
        String playerscored = String.valueOf(penaltyObject.optInt("scored", 0));
        String playermissed = String.valueOf(penaltyObject.optInt("missed", 0));
        String playerpenaltysaved = String.valueOf(penaltyObject.optInt("saved", 0));

        // Escribir en el archivo CSV
        csvWriter.append(teamid).append(",").append(teamlogo).append(",").append(teamName).append(",").append(teamupdate).append(",").
                append(playerName).append(",").append(playerId).append(",").append(playerPhoto).append(",").
                append(playerPosition).append(",").append(playerMinutes).append(",").append(playernumber).append(",").
                append(playerrating).append(",").append(playercaptain).append(",").append(playersubstitute).append(",").
                append(playertotal).append(",").append(playeron).append(",").
                // goles
                append(playergoalstotal).append(",").append(playerconceded).append(",").append(playerassistcs).append(",").append(playersaves).append(",").
                // pases
                append(playerpassestotal).append(",").append(playerpasseskey).append(",").append(playerpasesacuracy).append(",").
                // tackles
                append(playertacklestotal).append(",").append(playerblocks).append(",").append(playerinterceptions).append(",").
                // duels
                append(playerduelstotal).append(",").append(playerwon).append(",").
                // dribbles
                append(playerattempts).append(",").append(playersuccess).append(",").append(playerpast).append(",").
                // fouls
                append(playerdrawn).append(",").append(playercommitted).append(",").
                // tarjetas
                append(playeryellow).append(",").append(playerred).append(",").
                // penaltis
                append(playerpenaltywon).append(",").append(playerpenaltycommite).append(",").append(playerscored).append(",").append(playermissed).append(",").append(playerpenaltysaved).append(",")
                .append("\n");
    }
}


}
    
                csvWriter.flush();
                csvWriter.close();
    
                System.out.println("Datos de jugadores guardados en " + filePath);
    
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        



//----------------------------------------------------------------------------------------------------------------------------------------//

    // Injuries - Lesiones

    // https://v3.football.api-sports.io/injuries?league=2&season=2020: Proporciona todas las lesiones
    // disponibles para una liga específica y una temporada específica.

        // imprimir pantalla

        public static void fetchInjuries(int leagueId, int seasonYear) {
            try {
                // Construir la URL de la API
                String apiUrl = "https://v3.football.api-sports.io/injuries?league=" + leagueId + "&season=" + seasonYear;
    
                // Realizar la solicitud HTTP GET
                HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                        .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                        .header("x-rapidapi-host", "v3.football.api-sports.io")
                        .asJson();
    
                // Obtener el cuerpo de la respuesta
                JsonNode responseBody = response.getBody();
    
                // Mostrar el JSON por consola
                System.out.println(responseBody);
    
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // save csv

        public void fetchAndSaveInjuries(int leagueId, int seasonYear, String filePath) throws UnirestException {
            String apiUrl = "https://v3.football.api-sports.io/injuries?league=" + leagueId + "&season=" + seasonYear;
        
            // Make the API request
            HttpResponse<JsonNode> jsonResponse = Unirest.get(apiUrl)
                    .header("Accept", "application/json")
                    .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                    .asJson();
        
            // Process the response
            JSONObject response = jsonResponse.getBody().getObject();
            JSONArray injuries = response.getJSONArray("response");
        
            // Write data to CSV file
            try (FileWriter writer = new FileWriter(filePath)) {
                // Write CSV header
                writer.append("Player Id,Player Name,Player Photo,Player Type,Player Reason," +
                        "Team Id,Team Name,Team Logo," +
                        "Fixture Id,Fixture Timezone,Fixture Date,Fixture Timestamp," +
                        "League Id,League Season,League Name,League Country,League Logo,League Flag\n");
        
                // Write injury data to CSV
                for (int i = 0; i < injuries.length(); i++) {
                    JSONObject injury = injuries.getJSONObject(i);
                    JSONObject player = injury.getJSONObject("player");
                    JSONObject team = injury.getJSONObject("team");
                    JSONObject fixture = injury.getJSONObject("fixture");
                    JSONObject league = injury.getJSONObject("league");
        
                    String playerId = player.has("id") ? String.valueOf(player.getInt("id")) : "0";
                    String playerName = player.has("name") ? "\"" + player.getString("name") + "\"" : "\"\"";
                    String playerPhoto = player.has("photo") ? "\"" + player.getString("photo") + "\"" : "\"\"";
                    String playerType = player.has("type") ? "\"" + player.getString("type") + "\"" : "\"\"";
                    String playerReason = player.has("reason") ? "\"" + player.getString("reason") + "\"" : "\"\"";
        
                    String teamId = team.has("id") ? String.valueOf(team.getInt("id")) : "0";
                    String teamName = team.has("name") ? "\"" + team.getString("name") + "\"" : "\"\"";
                    String teamLogo = team.has("logo") ? "\"" + team.getString("logo") + "\"" : "\"\"";
        
                    String fixtureId = fixture.has("id") ? String.valueOf(fixture.getInt("id")) : "0";
                    String fixtureTimezone = fixture.has("timezone") ? "\"" + fixture.getString("timezone") + "\"" : "\"\"";
                    String fixtureDate = fixture.has("date") ? "\"" + fixture.getString("date") + "\"" : "\"\"";
                    String fixtureTimestamp = fixture.has("timestamp") ? String.valueOf(fixture.getInt("timestamp")) : "0";
        
                    String leagueIdStr = league.has("id") ? String.valueOf(league.getInt("id")) : "0";
                    String leagueSeason = league.has("season") ? String.valueOf(league.getInt("season")) : "0";
                    String leagueName = league.has("name") ? "\"" + league.getString("name") + "\"" : "\"\"";
                    String leagueCountry = league.has("country") ? "\"" + league.getString("country") + "\"" : "\"\"";
                    String leagueLogo = league.has("logo") ? "\"" + league.getString("logo") + "\"" : "\"\"";
                    String leagueFlag = league.has("flag") ? "\"" + league.getString("flag") + "\"" : "\"\"";
        
                    writer.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                            playerId, playerName, playerPhoto, playerType, playerReason,
                            teamId, teamName, teamLogo,
                            fixtureId, fixtureTimezone, fixtureDate, fixtureTimestamp,
                            leagueIdStr, leagueSeason, leagueName, leagueCountry, leagueLogo, leagueFlag
                    ));
                }
        
                System.out.println("Injuries data has been saved to " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


//----------------------------------------------------------------------------------------------------------------------------------------//


    // Top Scorers

        // imprimir pantalla


        // save csv

        
}


