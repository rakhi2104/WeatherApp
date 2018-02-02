package com.example.crackjack.weatherapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by CRACK-JACK on 19-12-2017.
 */

public class JSONParser extends Application{

//    Context c = getClass().getCon
    public Context mContext = this;


    private static Response response;

    public JSONParser(MainActivity.GetWeatherData getWeatherData) {

    }

    public static JSONObject getData(String MAIN_URL) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(MAIN_URL).build();

            response = client.newCall(request).execute();

            return new JSONObject(response.body().string());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
