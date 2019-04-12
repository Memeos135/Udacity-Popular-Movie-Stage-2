package com.example.popularmoviesstage1;

public class TrailerInfo {
    private String key;
    private String name;

    public TrailerInfo() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TrailerInfo(String key, String name) {
        this.key = key;
        this.name = name;
    }
}
