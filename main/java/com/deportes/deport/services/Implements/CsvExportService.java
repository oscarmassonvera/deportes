package com.deportes.deport.services.Implements;

import org.springframework.stereotype.Service;

import com.jayway.jsonpath.JsonPath;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
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

        // TEAMS AND SEDES 
        // SOLO SE PUEDE POR ID  PERO HAY MUCHAS MANERAS MAS CON RUTAS DIFERENTES 
        //     // Get one team from one team {id}
        //     get("https://v3.football.api-sports.io/teams?id=33");

        //     // Get one team from one team {name}
        //     get("https://v3.football.api-sports.io/teams?name=manchester united");

        //     // Get all teams from one {league} & {season}
        //     get("https://v3.football.api-sports.io/teams?league=39&season=2019");

        //     // Get teams from one team {country}
        //     get("https://v3.football.api-sports.io/teams?country=england");

        //     // Get teams from one team {code}
        //     get("https://v3.football.api-sports.io/teams?code=FRA");

        //     // Get teams from one venue {id}
        //     get("https://v3.football.api-sports.io/teams?venue=789");

        //     // Allows you to search for a team in relation to a team {name} or {country}
        //     get("https://v3.football.api-sports.io/teams?search=manches");
        //     get("https://v3.football.api-sports.io/teams?search=England");

        // SE PODRIA TOMAS TODOS LOS PAISES Y LLAMAR A CADA EQUIPO POR PAIS PARA TENER LA DATA 
        // DE TODOS LOS EQUIPOS O TEAMS POR SUS IDS LEIENDO EL CSV DE PAISES Y LLENANDO EL CAMPO 
        // Get teams from one team {country} POR CADA PAIS

        public void exportTeamsToCsv(String filePath) {
            try {
                HttpResponse<JsonNode> response = Unirest.get("https://v3.football.api-sports.io/teams")
                        .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                        .header("x-rapidapi-host", "v3.football.api-sports.io")
                        .asJson();
        
                JSONArray teamsArray = response.getBody().getObject().getJSONArray("response");
        
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                    writer.write("ID, Nombre Equipo, Nombre Sede, Dirección, Ciudad, Capacidad, Superficie, Imagen Logo Equipo, Imagen Sede\n");
        
                    for (Object teamDataObj : teamsArray) {
                        JSONObject teamData = (JSONObject) teamDataObj;
                        writer.write(String.format("%d, %s, %s, %s, %s, %d, %s, %s, %s\n",
                                JsonPath.read(teamData, "$.team.id"), JsonPath.read(teamData, "$.team.name"),
                                JsonPath.read(teamData, "$.venue.name"), JsonPath.read(teamData, "$.venue.address"),
                                JsonPath.read(teamData, "$.venue.city"), JsonPath.read(teamData, "$.venue.capacity"),
                                JsonPath.read(teamData, "$.venue.surface"), JsonPath.read(teamData, "$.team.logo"),
                                JsonPath.read(teamData, "$.venue.image")));
                    }
                }
        
                System.out.println("Los datos del equipo y su sede se han exportado correctamente a " + filePath);
        
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

}