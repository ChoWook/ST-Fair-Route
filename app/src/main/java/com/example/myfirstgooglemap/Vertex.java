package com.example.myfirstgooglemap;

public class Vertex {
    double latitude = 0;
    double longitude = 0;
    int id = 0;
    String name = "";

    public Vertex(double lati, double longi, int id, String name) {
        this.latitude = lati;
        this.longitude = longi;
        this.id = id;
        this.name = name;
    }
}