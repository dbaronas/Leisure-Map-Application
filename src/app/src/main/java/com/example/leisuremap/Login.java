package com.example.leisuremap;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    private Button b_login, b_sign_up, b_forgot_pass;
    EditText username, password;
    TextView guideText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        b_login = findViewById(R.id.signIn);
        b_login.setOnClickListener(view -> printInfo());

        b_forgot_pass = findViewById(R.id.changePass);
        b_forgot_pass.setOnClickListener(view -> openForgotPass());

        b_sign_up = findViewById(R.id.signUp);
        b_sign_up.setOnClickListener(view -> openSignUp());

        guideText = findViewById(R.id.guide);
        username = findViewById(R.id.inputUsername);
        password = findViewById(R.id.inputPassword);
    }

    public void openForgotPass() {
        Intent intent = new Intent(this, ChangePassword.class);
        startActivity(intent);
    }

    public void openSignUp() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    public void printInfo() {
        String un = username.getText().toString();
        String pw = password.getText().toString();
        if(un.matches(""))
            guideText.setText("Please enter username");
        else if(pw.matches(""))
            guideText.setText("Please enter password");
        else {
            guideText.setText("You have successfully logged in");
            System.out.println(un);
            System.out.println(pw);
            //TODO email address confirmation
            // 1. when email is confirmed send user information to database
            // 2. send user information to database right away but only make registered users' functionalities available when email is confirmed
        }
    }
}