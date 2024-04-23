package com.deportes.deport.services.Implements;

import com.deportes.deport.entities.Dtos.LeagueData;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class FootballAPIServiceLeagues {
    private final WebClient webClient;
    @Value("${football.api.host}")
    private String apiHost;

    @Value("${football.api.key}")
    private String apiKey;

    public FootballAPIServiceLeagues(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://v3.football.api-sports.io").build();
    }

    public void getLeaguesAndPrint() {
        webClient.get()
                .uri("/leagues")
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        LeagueData leagueData = objectMapper.readValue(response, LeagueData.class);
                        String json = objectMapper.writeValueAsString(leagueData);

                        // Imprime el JSON en la consola
                        System.out.println("Datos de las ligas de fútbol:");
                        System.out.println(json);
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("Error al procesar los datos JSON: " + e.getMessage()));
                    }
                    return Mono.empty(); // Retorno vacío ya que no necesitamos ningún resultado adicional
                })
                .subscribe(
                    // Maneja la respuesta exitosa
                    leagueData -> {
                        // No es necesario hacer nada aquí ya que la impresión ya se ha realizado en el flatMap
                    },
                    // Maneja el error
                    error -> System.out.println("Error al obtener los datos de las ligas de fútbol: " + error.getMessage())
                );
    }
}
