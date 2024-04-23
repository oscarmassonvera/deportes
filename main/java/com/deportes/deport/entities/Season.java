package com.deportes.deport.entities;

public class Season {
    private int year;
    private String start;
    private String end;
    private boolean current;
    private Coverage coverage;

    // Getters y setters
    // ...

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getStart() {
        return this.start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return this.end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public boolean isCurrent() {
        return this.current;
    }

    public boolean getCurrent() {
        return this.current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public Coverage getCoverage() {
        return this.coverage;
    }

    public void setCoverage(Coverage coverage) {
        this.coverage = coverage;
    }

    @Override
    public String toString() {
        return "Season{" +
                "year=" + year +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", current=" + current +
                ", coverage=" + coverage +
                '}';
    }
}
