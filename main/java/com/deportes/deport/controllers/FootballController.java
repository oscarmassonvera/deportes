package com.deportes.deport.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deportes.deport.services.Implements.CsvExportService;

@RestController
@RequestMapping("/api")
public class FootballController {

    private final CsvExportService csvExportService;

    public FootballController(CsvExportService csvExportService) {
        this.csvExportService = csvExportService;
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

    // TEAMS --->>> POR AKI ME QUEDE Y CONTINUO 
}
