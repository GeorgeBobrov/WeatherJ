package com.example.weatherj2;

public class ResponseWeatherForecast {
    public Float lat;
    public Float lon;
    public String timezone;
    public Integer timezone_offset;
    public Current current;
    public Minutely[] minutely;
    public Hourly[] hourly;
    public Daily[] daily;
}

class Current {
    public Integer dt;
    public Integer sunrise;
    public Integer sunset;
    public Float temp;
    public Float feels_like;
    public Float pressure;
    public Float humidity;
    public Float dew_point;
    public Float uvi;
    public Float clouds;
    public Float visibility;
    public Float wind_speed;
    public Float wind_deg;
    public Weather[] weather;
}

class Daily {
    public Integer dt;
    public Integer sunrise;
    public Integer sunset;
    public Temp temp;
    public Feels_like feels_like;
    public Float pressure;
    public Float humidity;
    public Float dew_point;
    public Float wind_speed;
    public Integer wind_deg;
    public Weather[] weather;
    public Integer clouds;
    public Float pop;
    public Float snow;
    public Float uvi;
    public Float rain;
}

class Feels_like {
    public Float day;
    public Float night;
    public Float eve;
    public Float morn;
}

class Hourly {
    public Integer dt;
    public Float temp;
    public Float feels_like;
    public Float pressure;
    public Float humidity;
    public Float dew_point;
    public Float uvi;
    public Float clouds;
    public Integer visibility;
    public Float wind_speed;
    public Float wind_deg;
    public Weather[] weather;
    public Float pop;
}

class Minutely {
    public Integer dt;
    public Float precipitation;
}

class Temp {
    public Float day;
    public Float min;
    public Float max;
    public Float night;
    public Float eve;
    public Float morn;
}

//class Weather {
//    public Integer id;
//    public String main;
//    public String description;
//    public String icon;
//}



