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

    // 위도와 경도를 기준으로 두 지점 간의 거리측정
    public int calDistance(Vertex p){
        int R = 6378137; // 지구 반지름
        double dLat = Math.toRadians(p.latitude - this.latitude);
        double dLong = Math.toRadians(p.longitude - this.longitude);

        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(p.latitude)) * Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return (int)d; // returns the distance in meter
    }
}