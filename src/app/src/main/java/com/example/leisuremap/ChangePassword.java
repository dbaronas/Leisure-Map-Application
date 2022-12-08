package com.example.leisuremap;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChangePassword extends AppCompatActivity {

    private Button b_send;
    EditText current_pass, new_pass, new_pass2;
    TextView guideText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        guideText = findViewById(R.id.guide);
        current_pass = findViewById(R.id.inputCurrentPass);
        new_pass = findViewById(R.id.inputNewPass);
        new_pass2 = findViewById(R.id.inputNewPass2);
        b_send = findViewById(R.id.changePass);
        b_send.setOnClickListener(view -> printInfo());
    }

    public void printInfo() {
        String crpass = current_pass.getText().toString();
        String newpass = new_pass.getText().toString();
        String newpass2 = new_pass.getText().toString();
        if(crpass.matches(""))
            guideText.setText("Please enter current password");
        else if (newpass.matches(""))
            guideText.setText("Please enter new password");
        else if (newpass2.matches(""))
            guideText.setText("Please confirm new password");
        else {
            guideText.setText("Password changed successfully");
        }
    }
}