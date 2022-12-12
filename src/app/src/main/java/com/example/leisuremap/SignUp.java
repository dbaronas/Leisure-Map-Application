package com.example.leisuremap;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class SignUp extends AppCompatActivity {

    private Button b_sign_up;
    EditText username, password, email;
    TextView guideText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        b_sign_up = findViewById(R.id.signUp);
        b_sign_up.setOnClickListener(view -> printInfo());

        guideText = findViewById(R.id.guide);
        username = findViewById(R.id.inputUsername);
        password = findViewById(R.id.inputPassword);
    }

    public void printInfo() {
        String un = username.getText().toString();
        String pw = password.getText().toString();
        if(un.matches(""))
            guideText.setText("Please enter username");
        else if(pw.matches(""))
            guideText.setText("Please enter password");
        else {
            guideText.setText("Your account was successfully created");
            System.out.println(un);
            System.out.println(pw);
        }
        this.signUp(un, pw);
    }

    //client will send the data to the server in a form of url - GET request, query parameters will be present inside URL
    public void signUp (String username, String password){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://193.219.91.104:1254/user/create?username="+username+"&pass="+password;

        System.out.println("username: " + username + " password: " + password);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("response: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("not connected ");
            }
        });

        queue.add(stringRequest);

    }
}