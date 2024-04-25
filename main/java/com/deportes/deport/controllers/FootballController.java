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
}
