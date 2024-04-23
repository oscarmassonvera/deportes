package com.deportes.deport.controllers;

import com.deportes.deport.services.Implements.FootballAPIServiceLeagues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leagues")
public class FootballAPIController {

    private final FootballAPIServiceLeagues footballAPIServiceLeagues;

    @Autowired
    public FootballAPIController(FootballAPIServiceLeagues footballAPIServiceLeagues) {
        this.footballAPIServiceLeagues = footballAPIServiceLeagues;
    }

    @GetMapping
    public ResponseEntity<String> getLeagues() {
        try {
            footballAPIServiceLeagues.getLeaguesAndPrint();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
