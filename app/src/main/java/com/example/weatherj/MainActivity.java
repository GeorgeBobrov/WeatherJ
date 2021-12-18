package com.example.weatherj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.util.Log;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.text.SimpleDateFormat;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    static final String baseURLRemote = "http://api.openweathermap.org/data/2.5/";
    static final String APIkey = "534e27824fc3e9e6b42bd9076d595c84";
    static final String inputCity = "Kiev";

    TextView textResponse;
    Button buttonQuery;
    EditText editCity;
    LinearLayout linearLayoutH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editCity = findViewById(R.id.editCity);
        buttonQuery = findViewById(R.id.buttonQuery);
        textResponse = findViewById(R.id.textResponse);
        linearLayoutH = findViewById(R.id.linearLayoutH);

        editCity.clearFocus();
        //hides keyboard on start
//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //needs for scrolling to work
        textResponse.setMovementMethod(new ScrollingMovementMethod());
    }


    public void buttonQueryClick(View sender) throws Exception {
        editCity.clearFocus();
        //hide keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(linearLayoutH.getWindowToken(), 0);

        String url = baseURLRemote + "weather" +
            "?" + "q=" + URLEncoder.encode(editCity.getText().toString(), "UTF-8") +
            "&" + "appid=" + APIkey;

        new FetchStringTask(new FunctionWithStrParam(){
            public void run(String srt)	{ processWeatherCity(srt); }
        }).execute(url);
    }




    public void processWeatherCity(String weather) {
        Gson gson = new Gson();

        try {
            ResponseWeatherCity weatherCity = gson.fromJson(weather, ResponseWeatherCity.class);

            String weatherDescription = weatherCity.weather[0].main;
            Date time = new Date(weatherCity.dt * 1000l);
            String innerText = "Now, " + time.toLocaleString() + " in " +
                    weatherCity.name + " is " + normalizeTemp(weatherCity.main.temp) + " °C, " + weatherDescription;

            textResponse.setText(innerText);
//            textResponse.append("\n Humidity " + weatherCity.main.humidity);

            queryWeatherForecast(weatherCity);
        } catch (Exception e) {
            Log.d("Exception", e.getLocalizedMessage());
        }

    }

    public void queryWeatherForecast(ResponseWeatherCity a_weatherCity) throws Exception {
        String url = baseURLRemote + "onecall" +
                "?" + "lat=" + URLEncoder.encode(a_weatherCity.coord.lat.toString(), "UTF-8") +
                "&" + "lon=" + URLEncoder.encode(a_weatherCity.coord.lon.toString(), "UTF-8") +
                "&" + "appid=" + APIkey;

        new FetchStringTask(new FunctionWithStrParam(){
            public void run(String srt)	{ processWeatherForecast(srt); }
        }).execute(url);
    }


    public void processWeatherForecast(String weather) {
        Gson gson = new Gson();

        try {
            ResponseWeatherForecast weatherForecast = gson.fromJson(weather, ResponseWeatherForecast.class);

//            Date time = new Date(weatherCity.dt * 1000l);

            textResponse.append("\n Hourly:");

            for (Hourly hourForecast : weatherForecast.hourly) {
                String innerText = addHourForecast(hourForecast);
                textResponse.append("\n" + innerText);
            }
        } catch (JsonSyntaxException e) {
            Log.d("JsonSyntaxException", e.getLocalizedMessage());
        }

    }

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    String addHourForecast(Hourly hourForecast) {
        String weatherDescription = hourForecast.weather[0].main;
        Date time = new Date(hourForecast.dt * 1000l);

        String innerText = sdf.format(time) +
                ": Temperature " + normalizeTemp(hourForecast.temp) + " °C, " +
                weatherDescription;
        return innerText;
    }

    public float normalizeTemp(float t) {
        final float T0 = 273.15f;
        return Math.round(t - T0);
    }
}

