package com.example.leisuremap;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUp extends AppCompatActivity {

    private Button b_sign_up;
    EditText username, password, email;
    TextView guideText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        b_sign_up = findViewById(R.id.signIn);
        b_sign_up.setOnClickListener(view -> printInfo());

        guideText = findViewById(R.id.guide);
        username = findViewById(R.id.inputUsername);
        password = findViewById(R.id.inputPassword);
        email = findViewById(R.id.inputEmail);
    }

    public void printInfo() {
        String un = username.getText().toString();
        String pw = password.getText().toString();
        String em = email.getText().toString();
        if(un.matches(""))
            guideText.setText("Please enter username");
        else if(pw.matches(""))
            guideText.setText("Please enter password");
        else if(em.matches(""))
            guideText.setText("Please enter email address");
        else if(!Patterns.EMAIL_ADDRESS.matcher(em).matches())
            guideText.setText("Incorrect email address, try again");
        else {
            guideText.setText("Confirmation email was sent to your email address");
            System.out.println(un);
            System.out.println(pw);
            System.out.println(em);
            //TODO email address confirmation
            // 1. when email is confirmed send user information to database
            // 2. send user information to database right away but only make registered users' functionalities available when email is confirmed
        }
    }
}