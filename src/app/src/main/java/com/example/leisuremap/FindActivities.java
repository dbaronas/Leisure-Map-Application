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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class FindActivities extends AppCompatActivity {

    private Button b_pref;
    List<Object> objects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_activities);

        //distance spinner
        Spinner spinner1 = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.distance_array, R.layout.spinner_item);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner1.setAdapter(adapter1);

        //rating spinner
        Spinner spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.rating_array, R.layout.spinner_item);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner2.setAdapter(adapter2);

        //cities spinner
        //will need to get the values of cities from the webserver instead and add the values to select_qualification array
        String[] select_cities = {"Select city", "Kaunas", "Vilnius", "Joniškis", "Palanga"};
        Spinner spinner3 = findViewById(R.id.spinner3);
        ArrayList<SpinnerState> listVOsCities = new ArrayList<>();

        for (int i = 0; i < select_cities.length; i++) {
            SpinnerState state = new SpinnerState();
            state.setTitle(select_cities[i]);
            state.setSelected(false);
            listVOsCities.add(state);
        }
        MyAdapter myAdapterCities = new MyAdapter(FindActivities.this, 0, listVOsCities);
        spinner3.setAdapter(myAdapterCities);

        //type spinner
        //will need to get the values of type from the webserver instead and add the values to select_qualification array
        String[] select_types = {"Select types", "Restaurant", "Museum", "Gym", "Park"};
        Spinner spinner4 = (Spinner) findViewById(R.id.spinner4);
        ArrayList<SpinnerState> listVOsTypes = new ArrayList<>();

        for (int i = 0; i < select_types.length; i++) {
            SpinnerState state = new SpinnerState();
            state.setTitle(select_types[i]);
            state.setSelected(false);
            listVOsTypes.add(state);
        }
        MyAdapter myAdapterTypes = new MyAdapter(FindActivities.this, 0, listVOsTypes);
        spinner4.setAdapter(myAdapterTypes);

        b_pref = findViewById(R.id.choosePref);
        b_pref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int chosenDist;
                if (!spinner1.getSelectedItem().toString().matches("Choose distance")) {
                    String distance = spinner1.getSelectedItem().toString();
                    String distNumbersOnly = distance.replaceAll("[^0-9]", "");
                    chosenDist = Integer.parseInt(distNumbersOnly);
                } else
                    chosenDist = 400; //if nothing was selected all object would appear (highest possible distance in Lithuania)

                int chosenRating;
                if (!spinner2.getSelectedItem().toString().matches("Choose rating")) {
                    String rating = spinner2.getSelectedItem().toString();
                    String ratingNumbersOnly = rating.replaceAll("[^0-9]", "");
                    chosenRating = Integer.parseInt(ratingNumbersOnly);
                } else
                    chosenRating = 1; //if nothing was selected all object would appear (lowest possible rating)

                ArrayList<String> chosenCities = new ArrayList<>();
                for (SpinnerState s : listVOsCities) {
                    if (s.isSelected()) {
                        chosenCities.add(s.getTitle());
                    }
                }

                ArrayList<String> chosenTypes = new ArrayList<>();
                for (SpinnerState s : listVOsTypes) {
                    if (s.isSelected()) {
                        chosenTypes.add(s.getTitle());
                    }
                }
                searchObject(chosenDist, chosenRating, chosenCities, chosenTypes);
            }
        });

        LatLng userPos = getIntent().getParcelableExtra("Pos");

        RequestQueue queue_museums = Volley.newRequestQueue(FindActivities.this);
        String url_museums = "https://overpass-api.de/api/interpreter?data=%2F*%0AThis%20has%20been%20generated%20by%20the%20overpass-turbo%20wizard.%0AThe%20original%20search%20was%3A%0A%E2%80%9Ctourism%3Dmuseum%20in%20lithuania%E2%80%9D%0A*%2F%0A%5Bout%3Ajson%5D%5Btimeout%3A25%5D%3B%0A%2F%2F%20fetch%20area%20%E2%80%9Clithuania%E2%80%9D%20to%20search%20in%0Aarea%28id%3A3600072596%29-%3E.searchArea%3B%0A%2F%2F%20gather%20results%0A%28%0A%20%20%2F%2F%20query%20part%20for%3A%20%E2%80%9Ctourism%3Dmuseum%E2%80%9D%0A%20%20node%5B%22tourism%22%3D%22museum%22%5D%28area.searchArea%29%3B%0A%20%20way%5B%22tourism%22%3D%22museum%22%5D%28area.searchArea%29%3B%0A%20%20relation%5B%22tourism%22%3D%22museum%22%5D%28area.searchArea%29%3B%0A%29%3B%0A%2F%2F%20print%20results%0Aout%20body%3B%0A%3E%3B%0Aout%20skel%20qt%3B";
        //String url_museums = "http://193.219.91.104:1254/table?name=place";
        JsonObjectRequest request_museusm = new JsonObjectRequest(Request.Method.GET, url_museums, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("elements");
                    //JSONArray array = response.getJSONArray("Table");
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout); // reiks isimt
                    for(int i = 0; i < array.length(); i++) {
                        JSONObject element = (JSONObject) array.get(i);
                        String latitude = element.get("lat").toString();
                        String longitude = element.get("lon").toString();
                        double lon = Double.valueOf(longitude);
                        double lat = Double.valueOf(latitude);
                        LatLng pos = new LatLng(lat, lon);
                        JSONObject json_obj = (JSONObject) element.get("tags");
                        String objName = json_obj.get("name").toString();
                        //String objName = element.get("name").toString();

                        Location startPoint=new Location("UserPos");
                        startPoint.setLatitude(userPos.latitude);
                        startPoint.setLongitude(userPos.longitude);

                        Location endPoint=new Location("Object");
                        endPoint.setLatitude(pos.latitude);
                        endPoint.setLongitude(pos.longitude);

                        double distance=startPoint.distanceTo(endPoint) / 1000;

                        //Randomized data------------------
                        int randomRating = (int)(Math.random()*(5-1+1)+1);

                        final String[] cities = {"Kaunas", "Vilnius", "Joniškis", "Palanga"};
                        Random random1 = new Random();
                        int randomIndex1 = random1.nextInt(cities.length);
                        String randomCity = cities[randomIndex1];

                        final String[] types = {"Restaurant", "Museum", "Gym", "Park"};
                        Random random2 = new Random();
                        int randomIndex2 = random2.nextInt(cities.length);
                        String randomType = types[randomIndex2];
                        //---------------------------------

                        double score = (10 - Math.sqrt(distance)) * 2 + (randomRating * 2);

                        Object obj = new Object(objName, distance, lat, lon, randomRating, randomCity, randomType, score);
                        objects.add(obj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sortList();
                for(Object o:objects) {
                    createTextViews(o);
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

    public void searchObject(int distance, int rating, ArrayList<String> chosenCities, ArrayList<String> chosenTypes) {
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        linearLayout.removeAllViews();
        int n = 0; //counting objects found
        for(Object o:objects) {
            if(o.getDistance() <= distance && o.getRating() >= rating && (chosenCities.stream().anyMatch(o.getCity()::contains) || chosenCities.isEmpty()) && (chosenTypes.stream().anyMatch(o.getType()::contains) || chosenTypes.isEmpty())) {
                createTextViews(o);
                n++;
            }
        }
        if(n == 0) {
            Toast.makeText(FindActivities.this, "No objects found in this distance", Toast.LENGTH_SHORT).show();
        }
    }

    public void createTextViews(Object object) {
        LatLng pos = new LatLng(object.getLat(), object.getLon());
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        TextView textView = new TextView(FindActivities.this);
        LayoutParams layoutParams = new LayoutParams(1000, 300);
        layoutParams.gravity = Gravity.CENTER;
        textView.setLayoutParams(layoutParams);
        layoutParams.setMargins(10, 10, 10, 10);
        textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_up_style));
        textView.setTextColor(Color.BLACK);
        String format = String.format("%.1f", object.getDistance());
        textView.setText("Name: " + object.getName() + "\nDistance: " + format + " km, " + "Rating: " + object.getRating() + " stars, " + "City: " + object.getCity() + ", Type: " + object.getType() + ", Score: " + object.getScore());
        textView.setPadding(20, 20, 20, 20);
        textView.setGravity(Gravity.CENTER);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), LeisureMap.class);
                intent.putExtra("Position", pos); //objektui dadet positiona
                startActivity(intent);
            }
        });
        linearLayout.addView(textView);
    }

    public void sortList() {
        Collections.sort(objects, new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                return Double.compare(o2.getScore(), o1.getScore());
            }
        });
    }
}
