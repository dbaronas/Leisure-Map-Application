package com.example.leisuremap;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class FavoritePlaces extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_places);

    }


}
