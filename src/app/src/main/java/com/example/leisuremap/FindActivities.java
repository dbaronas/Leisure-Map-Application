package com.example.leisuremap;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FindActivities extends AppCompatActivity {

    private Button b_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_activities);
        
        b_pref = findViewById(R.id.choosePref);
        b_pref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PopUp.class);
                startActivity(i);
            }
        });
    }
}