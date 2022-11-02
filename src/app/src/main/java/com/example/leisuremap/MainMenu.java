package com.example.leisuremap;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    private Button b_map, b_activities, b_sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        b_map = findViewById(R.id.map);
        b_map.setOnClickListener(view -> openMap());

        b_activities = findViewById(R.id.findActivities);
        b_activities.setOnClickListener(view -> openActivities());

        b_sign_in = findViewById(R.id.signIn);
        b_sign_in.setOnClickListener(view -> openLogin());
    }

    public void openMap() {
        Intent intent = new Intent(this, LeisureMap.class);
        startActivity(intent);
    }

    public void openActivities() {
        Intent intent = new Intent(this, FindActivities.class);
        startActivity(intent);
    }

    public void openLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}