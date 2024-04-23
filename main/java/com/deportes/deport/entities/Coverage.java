package com.deportes.deport.entities;

public class Coverage {
    private fixtures fixtures;
    private boolean standings;
    private boolean players;
    private boolean top_scorers;
    private boolean top_assists;
    private boolean top_cards;
    private boolean injuries;
    private boolean predictions;
    private boolean odds;

    // Getters y setters
    // ...

    public fixtures getFixtures() {
        return this.fixtures;
    }

    public void setFixtures(fixtures fixtures) {
        this.fixtures = fixtures;
    }

    public boolean isStandings() {
        return this.standings;
    }

    public boolean getStandings() {
        return this.standings;
    }

    public void setStandings(boolean standings) {
        this.standings = standings;
    }

    public boolean isPlayers() {
        return this.players;
    }

    public boolean getPlayers() {
        return this.players;
    }

    public void setPlayers(boolean players) {
        this.players = players;
    }

    public boolean isTop_scorers() {
        return this.top_scorers;
    }

    public boolean getTop_scorers() {
        return this.top_scorers;
    }

    public void setTop_scorers(boolean top_scorers) {
        this.top_scorers = top_scorers;
    }

    public boolean isTop_assists() {
        return this.top_assists;
    }

    public boolean getTop_assists() {
        return this.top_assists;
    }

    public void setTop_assists(boolean top_assists) {
        this.top_assists = top_assists;
    }

    public boolean isTop_cards() {
        return this.top_cards;
    }

    public boolean getTop_cards() {
        return this.top_cards;
    }

    public void setTop_cards(boolean top_cards) {
        this.top_cards = top_cards;
    }

    public boolean isInjuries() {
        return this.injuries;
    }

    public boolean getInjuries() {
        return this.injuries;
    }

    public void setInjuries(boolean injuries) {
        this.injuries = injuries;
    }

    public boolean isPredictions() {
        return this.predictions;
    }

    public boolean getPredictions() {
        return this.predictions;
    }

    public void setPredictions(boolean predictions) {
        this.predictions = predictions;
    }

    public boolean isOdds() {
        return this.odds;
    }

    public boolean getOdds() {
        return this.odds;
    }

    public void setOdds(boolean odds) {
        this.odds = odds;
    }

    @Override
    public String toString() {
        return "Coverage{" +
                "fixtures=" + fixtures +
                ", standings=" + standings +
                ", players=" + players +
                ", top_scorers=" + top_scorers +
                ", top_assists=" + top_assists +
                ", top_cards=" + top_cards +
                ", injuries=" + injuries +
                ", predictions=" + predictions +
                ", odds=" + odds +
                '}';
    }
}
