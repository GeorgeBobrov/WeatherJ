package com.example.weatherj;

import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.util.Scanner;

@FunctionalInterface
interface FunctionWithStrParam {
    public abstract void run(String srt);
}

public class FetchStringTask extends AsyncTask<String, Void, String> {
    public FetchStringTask(FunctionWithStrParam onFinish) {
        this.onFinish = onFinish;
    }

    FunctionWithStrParam onFinish;

    protected String doInBackground(String... urls) {
        String url = urls[0];
        try {
            InputStream inputStream = new java.net.URL(url).openStream();
            Scanner s = new Scanner(inputStream).useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";
            return result;
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            return null;
        }
    }

    protected void onPostExecute(String result) {
        onFinish.run(result);
    }
}
