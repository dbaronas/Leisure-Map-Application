package com.example.leisuremap;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FindActivities extends AppCompatActivity {

    private Button b_pref;
    List<Object> objects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_activities);

        Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.distance_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        b_pref = findViewById(R.id.choosePref);
        b_pref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String state = spinner.getSelectedItem().toString();
                String numbersOnly= state.replaceAll("[^0-9]", "");
                int chosenDist = Integer.parseInt(numbersOnly);
                searchObject(chosenDist);
            }
        });

        LatLng userPos = getIntent().getParcelableExtra("Pos");

        RequestQueue queue_museums = Volley.newRequestQueue(FindActivities.this);
        //String url_museums = "https://overpass-api.de/api/interpreter?data=%2F*%0AThis%20has%20been%20generated%20by%20the%20overpass-turbo%20wizard.%0AThe%20original%20search%20was%3A%0A%E2%80%9Ctourism%3Dmuseum%20in%20lithuania%E2%80%9D%0A*%2F%0A%5Bout%3Ajson%5D%5Btimeout%3A25%5D%3B%0A%2F%2F%20fetch%20area%20%E2%80%9Clithuania%E2%80%9D%20to%20search%20in%0Aarea%28id%3A3600072596%29-%3E.searchArea%3B%0A%2F%2F%20gather%20results%0A%28%0A%20%20%2F%2F%20query%20part%20for%3A%20%E2%80%9Ctourism%3Dmuseum%E2%80%9D%0A%20%20node%5B%22tourism%22%3D%22museum%22%5D%28area.searchArea%29%3B%0A%20%20way%5B%22tourism%22%3D%22museum%22%5D%28area.searchArea%29%3B%0A%20%20relation%5B%22tourism%22%3D%22museum%22%5D%28area.searchArea%29%3B%0A%29%3B%0A%2F%2F%20print%20results%0Aout%20body%3B%0A%3E%3B%0Aout%20skel%20qt%3B";
        String url_museums = "http://193.219.91.104:1254/table?name=locations";
        JsonObjectRequest request_museusm = new JsonObjectRequest(Request.Method.GET, url_museums, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //JSONArray array = response.getJSONArray("elements");
                    JSONArray array = response.getJSONArray("Table");
                    int k = 1;
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
                    for(int i = 0; i < array.length(); i++) {
                        JSONObject element = (JSONObject) array.get(i);
                        String latitude = element.get("lat").toString();
                        String longitude = element.get("lon").toString();
                        double lon = Double.valueOf(longitude);
                        double lat = Double.valueOf(latitude);
                        LatLng pos = new LatLng(lat, lon);
                        //JSONObject json_obj = (JSONObject) element.get("tags");
                        //String objName = json_obj.get("name").toString();
                        String objName = element.get("name").toString();


                        Location startPoint=new Location("UserPos");
                        startPoint.setLatitude(userPos.latitude);
                        startPoint.setLongitude(userPos.longitude);

                        Location endPoint=new Location("Object");
                        endPoint.setLatitude(pos.latitude);
                        endPoint.setLongitude(pos.longitude);

                        double distance=startPoint.distanceTo(endPoint) / 1000;
                        String format = String.format("%.1f", distance);

                        Object obj = new Object(objName, distance, lat, lon);
                        objects.add(obj);

                        TextView textView = new TextView(FindActivities.this);
                        LayoutParams layoutParams = new LayoutParams(1000, 200);
                        layoutParams.gravity = Gravity.CENTER;
                        textView.setLayoutParams(layoutParams);
                        layoutParams.setMargins(10, 10, 10, 10);
                        textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_up_style));
                        textView.setTextColor(Color.BLACK);
                        textView.setText("Name: " + objName + "\n Distance: " + format + " km");
                        textView.setPadding(20, 20, 20, 20);
                        textView.setGravity(Gravity.CENTER);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getBaseContext(), LeisureMap.class);
                                intent.putExtra("Position", pos);
                                startActivity(intent);
                            }
                        });
                        linearLayout.addView(textView);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FindActivities.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
        request_museusm.setRetryPolicy(new DefaultRetryPolicy(60000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue_museums.add(request_museusm);
    }

    public void searchObject(int distance) {
        System.out.println(distance + "\n");
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        linearLayout.removeAllViews();
        int n = 0; //counting objects found
        for(Object o:objects) {
            if(o.getDistance() <= distance) {
                LatLng pos = new LatLng(o.getLat(), o.getLon());
                String dist_formated = String.format("%.1f", o.getDistance());

                TextView textView = new TextView(FindActivities.this);
                LayoutParams layoutParams = new LayoutParams(1000, 200);
                layoutParams.gravity = Gravity.CENTER;
                textView.setLayoutParams(layoutParams);
                layoutParams.setMargins(10, 10, 10, 10);
                textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_up_style));
                textView.setTextColor(Color.BLACK);
                textView.setText("Name: " + o.getName() + "\n Distance: " + dist_formated + " km");
                textView.setPadding(20, 20, 20, 20);
                textView.setGravity(Gravity.CENTER);

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getBaseContext(), LeisureMap.class);
                        intent.putExtra("Position", pos);
                        startActivity(intent);
                    }
                });
                linearLayout.addView(textView);
                n++;
            }
        }
        if(n == 0) {
            Toast.makeText(FindActivities.this, "No objects found in this distance", Toast.LENGTH_SHORT).show();
        }
    }
}