package com.example.leisuremap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CityPopUpActivity extends AppCompatActivity {

    TextView cityText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_pop_up);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.8), (int)(height*0.2));

        String city = getIntent().getStringExtra("City");
        cityText = findViewById(R.id.cityText);
        cityText.setText(city);

        LinearLayout layout = findViewById(R.id.city_pop_up_layout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER;

        Button weather_button = new Button(this);
        weather_button.setLayoutParams(params);
        weather_button.setText("Weather");
        weather_button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        layout.addView(weather_button);
        layout.setOrientation(LinearLayout.VERTICAL);

        weather_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue = Volley.newRequestQueue(CityPopUpActivity.this);
                String url ="http://193.219.91.103:16059/weather?city=" + city;
                //String url ="https://api.meteo.lt/v1/places/" + city + "/forecasts/long-term";

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject city = response.getJSONObject("place");
                            String cityName = city.get("name").toString();
                            JSONArray array = response.getJSONArray("forecastTimestamps");
                            Intent intent = new Intent(getApplicationContext(), PopWeather.class);
                            StringBuilder s = new StringBuilder("City: " + cityName);
                            for(int i = 0; i < array.length(); i++) {
                                JSONObject forecast = (JSONObject) array.get(i);
                                String forecastTimeUtc = forecast.get("forecastTimeUtc").toString();
                                String airTemperature = forecast.get("airTemperature").toString();
                                String windSpeed = forecast.get("windSpeed").toString();
                                String cloudCover = forecast.get("cloudCover").toString();
                                String conditionCode = forecast.get("conditionCode").toString();
                                s.append("\n--------------------------------\n").append("Forecast Time:\n").append(forecastTimeUtc).append("\nAir Temperature: ").append(airTemperature).append("\nWind Speed: ").append(windSpeed).append("\nCloud Cover: ").append(cloudCover).append("\nCondition Code: ").append(conditionCode);
                            }
                            intent.putExtra("Weather", s.toString());
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CityPopUpActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
                request.setRetryPolicy(new DefaultRetryPolicy(60000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(request);
            }
        });

        if(city.matches("Vilnius")) {
            Button events_button = new Button(this);
            events_button.setLayoutParams(params);
            events_button.setText("Events");
            events_button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            layout.addView(events_button);
            layout.setOrientation(LinearLayout.VERTICAL);

            events_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RequestQueue queue_events = Volley.newRequestQueue(CityPopUpActivity.this);
                    String url_events = "http://193.219.91.103:16059/events";
                    JsonObjectRequest request_events = new JsonObjectRequest(Request.Method.GET, url_events, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Intent intent = new Intent(getApplicationContext(), PopWeather.class);
                                StringBuilder s = new StringBuilder("City: Vilnius");
                                JSONArray array = response.getJSONArray("Events");
                                for(int i = 0; i < array.length(); i++) {
                                    JSONObject element = (JSONObject) array.get(i);
                                    String date = element.get("date").toString();
                                    date = androidx.core.text.HtmlCompat.fromHtml(date, HtmlCompat.FROM_HTML_MODE_LEGACY).toString();
                                    String title = element.get("title").toString();
                                    title = androidx.core.text.HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY).toString();
                                    String content = element.get("content").toString();
                                    content = androidx.core.text.HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY).toString();
                                    s.append("\n--------------------------------\n").append("Date: ").append(date).append("\n\nTitle: ").append(title).append("\n\nContent: \n").append(content);
                                }
                                intent.putExtra("Weather", s.toString());
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(CityPopUpActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                    request_events.setRetryPolicy(new DefaultRetryPolicy(60000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue_events.add(request_events);
                }
            });
        }
    }
}