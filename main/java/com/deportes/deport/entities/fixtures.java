package com.deportes.deport.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class fixtures {
    @JsonProperty("events")
    private boolean events;
    
    @JsonProperty("lineups")
    private boolean lineups;
    
    @JsonProperty("statistics_fixtures")
    private boolean statisticsFixtures;
    
    @JsonProperty("statistics_players")
    private boolean statisticsPlayers;

    // Getters y setters
    // ...

    public boolean isEvents() {
        return this.events;
    }

    public boolean getEvents() {
        return this.events;
    }

    public void setEvents(boolean events) {
        this.events = events;
    }

    public boolean isLineups() {
        return this.lineups;
    }

    public boolean getLineups() {
        return this.lineups;
    }

    public void setLineups(boolean lineups) {
        this.lineups = lineups;
    }

    public boolean isStatisticsFixtures() {
        return this.statisticsFixtures;
    }

    public boolean getStatisticsFixtures() {
        return this.statisticsFixtures;
    }

    public void setStatisticsFixtures(boolean statisticsFixtures) {
        this.statisticsFixtures = statisticsFixtures;
    }

    public boolean isStatisticsPlayers() {
        return this.statisticsPlayers;
    }

    public boolean getStatisticsPlayers() {
        return this.statisticsPlayers;
    }

    public void setStatisticsPlayers(boolean statisticsPlayers) {
        this.statisticsPlayers = statisticsPlayers;
    }

    @Override
    public String toString() {
        return "FixtureCoverage{" +
                "events=" + events +
                ", lineups=" + lineups +
                ", statisticsFixtures=" + statisticsFixtures +
                ", statisticsPlayers=" + statisticsPlayers +
                '}';
    }
}
