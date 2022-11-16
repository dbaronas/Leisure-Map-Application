package com.example.leisuremap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.MapStyleOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LeisureMap extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap gMap;
    private static final String TAG = LeisureMap.class.getSimpleName();
    SupportMapFragment mapFragment;
    SearchView searchView;
    List<Marker> cityMarkers = new ArrayList<>();
    List<Marker> museumMarkers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leisure_map);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        searchView = findViewById(R.id.sv_location);
        searchView.setQueryHint("Type here to search");
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
    }

    private void pointToPosition(LatLng position) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(18).build();
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        LatLngBounds bounds = new LatLngBounds(
                new LatLng(53.8967893, 20.653783), // SW bounds
                new LatLng(56.4504213, 26.8355198)  // NE bounds
        );
        gMap.setLatLngBoundsForCameraTarget(bounds);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 5));
        gMap.setMinZoomPreference(8.0f);

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        RequestQueue queue_museums = Volley.newRequestQueue(LeisureMap.this);
        //String url_museums = "https://overpass-api.de/api/interpreter?data=%2F*%0AThis%20has%20been%20generated%20by%20the%20overpass-turbo%20wizard.%0AThe%20original%20search%20was%3A%0A%E2%80%9Ctourism%3Dmuseum%20in%20lithuania%E2%80%9D%0A*%2F%0A%5Bout%3Ajson%5D%5Btimeout%3A25%5D%3B%0A%2F%2F%20fetch%20area%20%E2%80%9Clithuania%E2%80%9D%20to%20search%20in%0Aarea%28id%3A3600072596%29-%3E.searchArea%3B%0A%2F%2F%20gather%20results%0A%28%0A%20%20%2F%2F%20query%20part%20for%3A%20%E2%80%9Ctourism%3Dmuseum%E2%80%9D%0A%20%20node%5B%22tourism%22%3D%22museum%22%5D%28area.searchArea%29%3B%0A%20%20way%5B%22tourism%22%3D%22museum%22%5D%28area.searchArea%29%3B%0A%20%20relation%5B%22tourism%22%3D%22museum%22%5D%28area.searchArea%29%3B%0A%29%3B%0A%2F%2F%20print%20results%0Aout%20body%3B%0A%3E%3B%0Aout%20skel%20qt%3B";
        String url_museums = "http://193.219.91.104:1254/table?name=locations";
        JsonObjectRequest request_museusm = new JsonObjectRequest(Request.Method.GET, url_museums, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //JSONArray array = response.getJSONArray("elements");
                    JSONArray array = response.getJSONArray("Table");
                    for(int i = 0; i < array.length(); i++) {
                        JSONObject element = (JSONObject) array.get(i);
                        String latitude = element.get("lat").toString();
                        String longitude = element.get("lon").toString();
                        String objName = element.get("name").toString();
                        //JSONObject obj = (JSONObject) element.get("tags");
                        //String objName = obj.get("name").toString();
                        double lon = Double.valueOf(longitude);
                        double lat = Double.valueOf(latitude);
                        Marker marker = gMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(objName).snippet("Museum").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        marker.setVisible(false);
                        museumMarkers.add(marker);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LeisureMap.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
        request_museusm.setRetryPolicy(new DefaultRetryPolicy(60000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue_museums.add(request_museusm);

        RequestQueue queue_cities = Volley.newRequestQueue(LeisureMap.this);
        //String url_cities = "https://overpass-api.de/api/interpreter?data=[out:json][timeout:25];area(id:3600072596)-%3E.searchArea;(node[%22place%22=%22city%22](area.searchArea);way[%22place%22=%22city%22](area.searchArea);relation[%22place%22=%22city%22](area.searchArea););out%20body;%3E;out%20skel%20qt;";
        String url_cities = "http://193.219.91.104:1254/table?name=cities";
        JsonObjectRequest request_cities = new JsonObjectRequest(Request.Method.GET, url_cities, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("Table");
                    //JSONArray array = response.getJSONArray("elements");
                    for(int i = 0; i < array.length(); i++) {
                        JSONObject element = (JSONObject) array.get(i);
                        String latitude = element.get("lat").toString();
                        String longitude = element.get("lon").toString();
                        String cityName = element.get("name").toString();
                        //JSONObject obj = (JSONObject) element.get("tags");
                        //String cityName = obj.get("name").toString();
                        double lon = Double.valueOf(longitude);
                        double lat = Double.valueOf(latitude);
                        Marker marker = gMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(cityName).snippet("City").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                        marker.setVisible(false);
                        cityMarkers.add(marker);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LeisureMap.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
        request_cities.setRetryPolicy(new DefaultRetryPolicy(60000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue_cities.add(request_cities);

        gMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                CameraPosition cameraPosition = gMap.getCameraPosition();

                for(Marker m:cityMarkers)
                    m.setVisible(cameraPosition.zoom > 9);
                for(Marker m:museumMarkers)
                    m.setVisible(cameraPosition.zoom > 12);
            }
        });

        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                String city = marker.getTitle();
                String snip = marker.getSnippet();
                if(Objects.equals(snip, "City"))
                {
                    RequestQueue queue = Volley.newRequestQueue(LeisureMap.this);
                    String url ="http://193.219.91.104:1254/weather?city=" + city;
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
                            Toast.makeText(LeisureMap.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                    request.setRetryPolicy(new DefaultRetryPolicy(60000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(request);
                }
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                if (location != null || !location.equals("")){
                    Geocoder geocoder = new Geocoder(LeisureMap.this);

                    try {
                        addressList = geocoder.getFromLocationName(location, 2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    //LatLng pos = getIntent().getParcelableExtra("Position");
                    for (Marker m : cityMarkers) {
                        if (m.getPosition().toString().equals(latLng.toString()))
                            pointToPosition(m.getPosition()); {
                            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 9));
                        }
                    }
                    for (Marker m : museumMarkers) {
                        if (m.getPosition().toString().equals(latLng.toString()))
                            pointToPosition(m.getPosition()); {
                            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                        }
                    }
                }
                return false;
            }
            private void pointToPosition(LatLng position) {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(18).build();
                gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        LatLng pos = getIntent().getParcelableExtra("Position");
        if(pos != null) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    for (Marker m : museumMarkers) {
                        if (m.getPosition().toString().equals(pos.toString()))
                            pointToPosition(m.getPosition());
                    }
                }
            }, 2000);
        }
    }
}