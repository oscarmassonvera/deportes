package com.deportes.deport.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deportes.deport.services.Implements.FootballAPIService;

@RestController
@RequestMapping("/api")
public class FootballController {

    @Autowired
    private FootballAPIService footballAPIService;

    @GetMapping("/football/leagues")
    public String getFootballLeagues() {
        return footballAPIService.getFootballLeagues();
    }
}
