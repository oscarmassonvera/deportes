package com.deportes.deport.entities.Dtos;

import java.util.List;

import com.deportes.deport.entities.Country;
import com.deportes.deport.entities.League;
import com.deportes.deport.entities.Season;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LeagueResponse {
    @JsonProperty("league")
    private League league;
    
    @JsonProperty("country")
    private Country country;
    
    @JsonProperty("seasons")
    private List<Season> seasons;

    // Getters y setters
    // ...

    public League getLeague() {
        return this.league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<Season> getSeasons() {
        return this.seasons;
    }

    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }

    @Override
    public String toString() {
        return "LeagueResponse{" +
                "league=" + league +
                ", country=" + country +
                ", seasons=" + seasons +
                '}';
    }
}
