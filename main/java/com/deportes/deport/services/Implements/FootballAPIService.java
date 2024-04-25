package com.deportes.deport.services.Implements;


import org.springframework.stereotype.Service;

import com.mashape.unirest.http.Unirest;

@Service
public class FootballAPIService {

    public String getFootballLeagues() {
        try {
            // Hacer la solicitud HTTP
            com.mashape.unirest.http.HttpResponse<com.mashape.unirest.http.JsonNode> response = 
                Unirest.get("https://v3.football.api-sports.io/teams")
                    .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                    .header("x-rapidapi-host", "v3.football.api-sports.io")
                    .asJson();

            // Obtener el cuerpo de la respuesta como cadena
            String responseBody = response.getBody().toString();

            return responseBody;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Maneja errores adecuadamente en tu aplicación
        }
    }
}
