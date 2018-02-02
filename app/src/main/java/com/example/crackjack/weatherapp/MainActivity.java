package com.example.crackjack.weatherapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView dateTv = findViewById(R.id.date_tv);
        TextView tomorrow_dateTv = findViewById(R.id.tomorrow);
        TextView day_after_dateTv = findViewById(R.id.day_after_tomorrow);



        Date date = new Date();
        DateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        dateTv.setText(df.format(date));

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        date = c.getTime();
        tomorrow_dateTv.setText(df.format(date));

        c.add(Calendar.DATE, 1);
        date = c.getTime();
        day_after_dateTv.setText(df.format(date));

        String MAIN_URL = "https://api.darksky.net/forecast/" + getResources().getString(R.string.weather_api_key) + "/17.6868,83.2185";

        new GetWeatherData().execute();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.location:
                Toast toast = Toast.makeText(this, "Location", Toast.LENGTH_SHORT);
                toast.show();
                break;
            case R.id.refresh:
                new GetWeatherData().execute();
                break;
            default:
                break;

        }
        return id == R.id.location || super.onOptionsItemSelected(item);

    }


    public class GetWeatherData extends AsyncTask<Void, Void, JSONObject> {

        ProgressDialog progressDialog;
        String MAIN_URL = "https://api.darksky.net/forecast/" + getResources().getString(R.string.weather_api_key) + "/17.6868,83.2185";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Fetching JSON Data");
            progressDialog.setMessage("Please wait . . .");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            JSONObject jsonObject = JSONParser.getData(MAIN_URL);
            System.out.println(jsonObject);
            try {
                assert jsonObject != null;
                System.out.println(jsonObject.getJSONObject("currently"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject data) {
            super.onPostExecute(data);
            final Activity mActivity = MainActivity.this;

            TextView current_icon = findViewById(R.id.main_weather);
            TextView temp_data = findViewById(R.id.temp_data);
            TextView humidity = findViewById(R.id.humidity);
            TextView wind = findViewById(R.id.wind);

            JSONObject icons = new JSONObject();
            try {
                icons.put("clear-day", "&#xf00d;");
                icons.put("clear-night", "&#xf02e;");
                icons.put("rain", "&#xf019;");
                icons.put("snow", "&#xf01b;");
                icons.put("sleet", "&#xf0b5;");
                icons.put("wind", "&#xf021;");
                icons.put("fog", "&#xf014;");
                icons.put("cloudy", "&#xf013;");
                icons.put("partly-cloudy-day", "&#xf002;");
                icons.put("partly-cloudy-night", "&#xf031;");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
//                current_icon.setText((char)Integer.parseInt(icons.getString(data.getJSONObject("currently").getString("icon")), 16));
                current_icon.setText(Html.fromHtml(icons.getString(data.getJSONObject("currently").getString("icon"))));

                double temp = (data.getJSONObject("currently").getInt("temperature")-32)*0.56;
                DecimalFormat df = new DecimalFormat("#.00");

                String typeDay = data.getJSONObject("currently").getString("summary");
                temp_data.setText(""+ df.format(temp)+ "° - "+ typeDay);

                double humidity_val = data.getJSONObject("currently").getDouble("humidity");
                humidity.append(""+humidity_val);

                double wind_val = data.getJSONObject("currently").getDouble("windSpeed");
                wind.append(""+wind_val + " km/h");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialog.dismiss();
        }
    }

}
