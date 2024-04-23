package com.deportes.deport.entities.Dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class LeagueData {

    @JsonProperty("response")
    private List<LeagueResponse> response;

    public List<LeagueResponse> getResponse() {
        return response;
    }

    public void setResponse(List<LeagueResponse> response) {
        this.response = response;
    }
}
