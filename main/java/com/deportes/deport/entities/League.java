package com.deportes.deport.entities;

public class League {
    private int id;
    private String name;
    private String type;
    private String logo;

    // Constructor
    public League(int id, String name, String type, String logo) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.logo = logo;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    // toString para imprimir los detalles de la liga
    @Override
    public String toString() {
        return "League{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }
}

