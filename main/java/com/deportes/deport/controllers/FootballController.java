package com.deportes.deport.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deportes.deport.services.Implements.CsvExportService;
import com.deportes.deport.services.Implements.FootballAPIService;
import com.mashape.unirest.http.exceptions.UnirestException;

@RestController
@RequestMapping("/api")
public class FootballController {

    private final CsvExportService csvExportService;

    public FootballController(CsvExportService csvExportService) {
        this.csvExportService = csvExportService;
    }




















    // PARA VER LA ESTRUCTURA DE LOS DATOS QUE ME PASA EL JSON


    @Autowired
    private FootballAPIService footballService;

    @GetMapping("/footballLeagues")
    public ResponseEntity<String> getFootballLeagues() {
        String leaguesResponse = footballService.getFootballLeagues();

        if (leaguesResponse != null) {
            return ResponseEntity.ok(leaguesResponse);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener las ligas de fútbol");
        }
    }

    @GetMapping("/export/football-leagues")
    public String exportFootballLeaguesToCsv() {
        String filePath = "D:/ProyectosWebJava/deport/excells/football_leagues.csv";
        csvExportService.exportFootballLeaguesToCsv(filePath);
        return "Los datos se han exportado correctamente a " + filePath;
    }

    @GetMapping("/export/timezones")
    public String exportTimezonesToCsv() {
        String filePath = "D:/ProyectosWebJava/deport/excells/timezones.csv";
        csvExportService.exportTimezonesToCsv(filePath);
        return "Los datos de las zonas horarias se han exportado correctamente a " + filePath;
    }

    @GetMapping("/export/countries")
    public String exportCountriesToCsv() {
        String filePath = "D:/ProyectosWebJava/deport/excells/countries.csv";
        csvExportService.exportCountriesToCsv(filePath);
        return "Los datos de los países se han exportado correctamente a " + filePath;
    }

    @GetMapping("/export/seasons")
    public String exportLeaguesSeasonsToCsv() {
        String filePath = "D:/ProyectosWebJava/deport/excells/leagues_seasons.csv";
        csvExportService.exportLeaguesSeasonsToCsv(filePath);
        return "Los datos de las temporadas de la liga se han exportado correctamente a " + filePath;
    }

    // pasar el nombre de la liga y la sesion o temporada para obtener todos los equipos de esa session
    @GetMapping("/export/teams/{league}/{season}")
    public String exportTeamsToCsv(@PathVariable String league, @PathVariable String season) {
        String filePath = "D:/ProyectosWebJava/deport/excells/teams.csv";
        try {
            csvExportService.exportTeamsToCsv(filePath, league, season);
        } catch (UnirestException e) {
            e.printStackTrace();
            return "Error al exportar los datos de los equipos";
        }
        return "Los datos de los equipos se han exportado correctamente a " + filePath;
    }
    
}
