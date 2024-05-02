package com.deportes.deport.services.Implements;


import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.mashape.unirest.http.Unirest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;


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

    public JSONObject getTeamStatisticsJson(String leagueId, String teamId, String season) {
        JSONObject result = new JSONObject();
        try {
            String url = String.format("https://v3.football.api-sports.io/teams/statistics?league=%s&team=%s&season=%s", leagueId, teamId, season);
            HttpResponse<JsonNode> response = Unirest.get(url)
                    .header("x-rapidapi-key", "690bb9329cd6b41bc4665f60473597d3")
                    .header("x-rapidapi-host", "v3.football.api-sports.io")
                    .header("Accept", "application/json") // Agregar este encabezado
                    .asJson();
    
            JSONObject jsonResponse = response.getBody().getObject();
            JSONObject jsonData = jsonResponse.getJSONObject("response");
    
            result = parseJsonData(jsonData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    

    private JSONObject parseJsonData(JSONObject jsonData) {
        JSONObject result = new JSONObject();
        Iterator<String> keys = jsonData.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonData.get(key);
            if (value instanceof JSONObject) {
                JSONObject subObject = (JSONObject) value;
                JSONObject parsedSubObject = parseJsonData(subObject);
                result.put(key, parsedSubObject);
            } else if (value instanceof JSONArray) {
                JSONArray subArray = (JSONArray) value;
                JSONArray parsedSubArray = parseJsonArray(subArray);
                result.put(key, parsedSubArray);
            } else {
                result.put(key, value.getClass().getSimpleName());
            }
        }
        return result;
    }

    private JSONArray parseJsonArray(JSONArray jsonArray) {
        JSONArray result = new JSONArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);
            if (value instanceof JSONObject) {
                JSONObject subObject = (JSONObject) value;
                JSONObject parsedSubObject = parseJsonData(subObject);
                result.put(parsedSubObject);
            } else if (value instanceof JSONArray) {
                JSONArray subArray = (JSONArray) value;
                JSONArray parsedSubArray = parseJsonArray(subArray);
                result.put(parsedSubArray);
            } else {
                result.put(value.getClass().getSimpleName());
            }
        }
        return result;
    }
}
