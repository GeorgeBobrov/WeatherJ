package com.example.weatherj;

public class ResponseWeatherCity {
    public Coord coord;
    public Weather[] weather;
    public String base;
    public Main main;
    public Integer visibility;
    public Wind wind;
    public Rain rain;
    public Clouds clouds;
    public Integer dt;
    public Sys sys;
    public Integer timezone;
    public Integer id;
    public String name;
    public Integer cod;
}

//-----------------------------------com.example.Clouds.java-----------------------------------
class Clouds {
    public Integer all;
}

//-----------------------------------com.example.Coord.java-----------------------------------
class Coord {
    public Float lon;
    public Float lat;
}

//-----------------------------------com.example.Main.java-----------------------------------
class Main {
    public Float temp;
    public Float feels_like;
    public Float temp_min;
    public Float temp_max;
    public Float pressure;
    public Float humidity;
}

//-----------------------------------com.example.Rain.java-----------------------------------
class Rain {
    public Float _1h;
}

//-----------------------------------com.example.Sys.java-----------------------------------
class Sys {
    public Integer type;
    public Integer id;
    public String country;
    public Integer sunrise;
    public Integer sunset;
}

//-----------------------------------com.example.Weather.java-----------------------------------
class Weather {
    public Integer id;
    public String main;
    public String description;
    public String icon;
}

//-----------------------------------com.example.Wind.java-----------------------------------
class Wind {
    public Float speed;
    public Float deg;
}