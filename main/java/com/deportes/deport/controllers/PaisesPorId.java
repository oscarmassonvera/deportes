package com.deportes.deport.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deportes.deport.services.Implements.listarPaisesporIdCsv;

@RestController
@RequestMapping("/paisxid")
public class PaisesPorId {

    @Autowired
    private listarPaisesporIdCsv service;

    @GetMapping
    public String exportAllCountriesToCsv() {
        // Ruta a buscar los ids 
        String filePath = "D:/ProyectosWebJava/deport/excells/football_leagues.csv";
        service.exportAllCountriesToCsv(filePath);
        return "Se han exportado todos los países correctamente a all_countries.csv";
    }
    
}
