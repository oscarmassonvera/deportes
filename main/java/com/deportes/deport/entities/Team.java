package com.deportes.deport.entities;

public class Team {
    private int id;
    private String name;
    private String code;
    private String country;
    private int founded;
    private boolean national;
    private String logo;
    private Venue venue;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getFounded() {
        return this.founded;
    }

    public void setFounded(int founded) {
        this.founded = founded;
    }

    public boolean isNational() {
        return this.national;
    }

    public boolean getNational() {
        return this.national;
    }

    public void setNational(boolean national) {
        this.national = national;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Venue getVenue() {
        return this.venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

}
