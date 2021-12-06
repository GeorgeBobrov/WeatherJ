package com.example.weatherj2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
//import android.util.Log;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.text.SimpleDateFormat;

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


    private final OkHttpClient client = new OkHttpClient();

    public void buttonQueryClick(View sender) throws Exception {
        editCity.clearFocus();
        //hide keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(linearLayoutH.getWindowToken(), 0);

        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseURLRemote + "weather").newBuilder();
        urlBuilder.addQueryParameter("q", editCity.getText().toString());
        urlBuilder.addQueryParameter("appid", APIkey);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
//				.get()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processWeatherCity(myResponse);
                    }
                });
            }
        });

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
        } catch (JsonSyntaxException e) {
            Log.d("JsonSyntaxException", e.getLocalizedMessage());
        }

    }

    public void queryWeatherForecast(ResponseWeatherCity a_weatherCity) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseURLRemote + "onecall").newBuilder();
        urlBuilder.addQueryParameter("lat", a_weatherCity.coord.lat.toString());
        urlBuilder.addQueryParameter("lon", a_weatherCity.coord.lon.toString());
        urlBuilder.addQueryParameter("appid", APIkey);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processWeatherForecast(myResponse);
                    }
                });
            }
        });
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
