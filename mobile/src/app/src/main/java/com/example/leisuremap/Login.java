package com.example.leisuremap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Login extends AppCompatActivity {

    private Button b_login, b_sign_up, b_change_pass;
    EditText username, password;
    TextView guideText;
    boolean isLoggedIn = false;
    boolean status = true;
    public static Activity fa;
    private CheckBox passwordCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fa = this;

        b_login = findViewById(R.id.signIn);
        b_login.setOnClickListener(view -> {
            try {
                printInfo();
            } catch (InvalidAlgorithmParameterException | IllegalBlockSizeException | NoSuchPaddingException | BadPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
                e.printStackTrace();
            }
        });

        b_change_pass = findViewById(R.id.changePass);
        b_change_pass.setOnClickListener(view -> openForgotPass());

        b_sign_up = findViewById(R.id.signUp);
        b_sign_up.setOnClickListener(view -> openSignUp());

        guideText = findViewById(R.id.guide);
        username = findViewById(R.id.inputUsername);
        password = findViewById(R.id.inputPassword);
        passwordCheckbox = findViewById(R.id.passwordCheckbox);
        passwordCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // To hide password
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // To show password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

    }


    protected void onStart() {
        super.onStart();

        //Date endTime = getIntent().getParcelableExtra("EndTime"); // getting session endTime
        //Date endTime = new Date(System.currentTimeMillis());
        //check if a user is logged in, if - yes, then move him to MainMenu
        SessionManagement sessionManagement = new SessionManagement(Login.this);
        String un = sessionManagement.getSessionUsername();
        String pw = sessionManagement.getSessionPassword();
        boolean st = sessionManagement.getSessionStatus();

        if (un != null && pw != null && st == true ) {
            Login login = new Login();
            //login.moveToMainMenu();
            SimpleDateFormat formatter= new SimpleDateFormat("HH:mm");
            Date startTime = new Date(System.currentTimeMillis());
            System.out.println("startTime: " + formatter.format(startTime));
        }
//        else if (st != true && isAppRunning.isRunning() == true ) {
//            Login login = new Login();
//            login.moveToMainMenu();
//        }
//        else if (isRunning() == false) {
//            SimpleDateFormat formatter= new SimpleDateFormat("HH:mm");
//            Date endTime = new Date(System.currentTimeMillis());
//            System.out.println("endTime: " + formatter.format(endTime));
//        }
        else {

        }
        System.out.println(isRunning());

    }

    public int isRunning() {
        ActivityManager m = (ActivityManager) this.getSystemService( ACTIVITY_SERVICE );
        List<ActivityManager.RunningTaskInfo> runningTaskInfoList =  m.getRunningTasks(10);
        Iterator<ActivityManager.RunningTaskInfo> itr = runningTaskInfoList.iterator();
        int n=0;
        while(itr.hasNext()){
            n++;
            itr.next();
        }
        if(n==0){ // App is killed
            //return false;
        }
        return n;
        //return true; // App is in background or foreground
    }

    public void openForgotPass() {
        Intent intent = new Intent(this, ChangePassword.class);
        startActivity(intent);
    }

    public void openSignUp() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    public void printInfo() throws InvalidAlgorithmParameterException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        String un = username.getText().toString();
        String pw = password.getText().toString();
        if(un.matches(""))
            guideText.setText("Please enter username");
        else if(pw.matches(""))
            guideText.setText("Please enter password");
        else {
            logIn(un, pw);
            System.out.println("Login: " + "username: " + un + " password: " + pw);
        }
    }

    //client will send the data to the server in a form of url - GET request, query parameters will be present inside URL
    public void logIn(String username, String password) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://193.219.91.103:16059/user/login?username=" + username + "&pass=" + password;

        Intent intent = new Intent(this, MainMenu.class);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String message = "";
                try {
                    JSONObject status = response.getJSONObject("STATUS");
                    isLoggedIn = (boolean) status.get("LOGIN");
                    message = (String) status.get("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(isLoggedIn) {
                    guideText.setText("Logged in");
                    intent.putExtra("UserStatus", isLoggedIn);
                    startActivity(intent);
                    MainMenu.fa.finish();
                    finish();
                }
                else
                    guideText.setText(message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                guideText.setText("Connection problem");
            }
        });
        queue.add(request);
    }

    //log in to app and save the session of a user and move user to leisuremap activity
    public void saveLoggedInSession(View view){
        SessionManagement sessionManagement = new SessionManagement(Login.this);
        sessionManagement.saveSession(username.getText().toString(), password.getText().toString(), status);
        moveToMainMenu();
    }

    public void moveToMainMenu(){
        Intent intent = new Intent(Login.this, MainMenu.class);
        //any existing task that are associated with the activity are cleared before the activity is started
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}