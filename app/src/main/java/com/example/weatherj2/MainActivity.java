package com.example.weatherj2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
//import android.util.Log;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

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

public class MainActivity extends AppCompatActivity {
    static final String baseURLRemote = "http://api.openweathermap.org/data/2.5/";
    static final String APIkey = "534e27824fc3e9e6b42bd9076d595c84";
    static final String inputCity = "Kiev";

    TextView textResponse;
    Button buttonQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonQuery = findViewById(R.id.buttonQuery);
        textResponse = findViewById(R.id.textResponse);
    }


    private final OkHttpClient client = new OkHttpClient();

    public void buttonQueryClick(View sender) throws Exception {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseURLRemote + "weather").newBuilder();
        urlBuilder.addQueryParameter("q", inputCity);
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
                        processWeather(myResponse);
                    }
                });

            }
        });

    }

    public void processWeather(String weather) {
        Gson gson = new Gson();

        try {
            ResponseWeatherCity weatherCity = gson.fromJson(weather, ResponseWeatherCity.class);

            String weatherDescription = weatherCity.weather[0].main;
            Date time = new Date(weatherCity.dt * 1000l);
            String innerText = "Now, " + time.toLocaleString() + " in " +
                    weatherCity.name + " is " + normalizeTemp(weatherCity.main.temp) + " °C, " + weatherDescription;

            textResponse.setText(innerText);
//            textResponse.
        } catch (JsonSyntaxException e) {
            Log.d("JsonSyntaxException", e.getLocalizedMessage());
        }


    }

    public float normalizeTemp(float t) {
        final float T0 = 273.15f;
        return Math.round(t - T0);
    }
}
