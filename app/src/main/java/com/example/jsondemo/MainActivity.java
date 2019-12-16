package com.example.jsondemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView txt_signUp;
    EditText editUsername;
    EditText editPassword;
    Button btnLogin;

    UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_signUp = findViewById(R.id.txtBtn_signUp);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        user = new UserModel();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);

        int userId = pref.getInt("userId", -1);

        //continue session
        if(userId > -1) {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.i("response", response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");


                        //fetch user data from mysql
                        if(success) {
                            UserModel user = new UserModel(
                                    jsonResponse.getInt("id"),
                                    jsonResponse.getString("username"),
                                    jsonResponse.getString("name"),
                                    jsonResponse.getInt("parentId")
                            );

                            Intent i = new Intent(MainActivity.this, home2.class);
                            i.putExtra("user", user);
                            startActivity(i);
                        }

                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("Login Failed!")
                                    .setNegativeButton("Retry", null)
                                    .show();
                        }
                    } catch (JSONException e) {
                        Log.i("json exception", e.toString());
                    }

                }
            };
            getuserRequest userRequest = new getuserRequest(userId, responseListener);
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            queue.add(userRequest);
        }
        else {
            txt_signUp.setVisibility(View.VISIBLE);
            editUsername.setVisibility(View.VISIBLE);
            editPassword.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
        }

    }


    public void signUpClick(View view) {
        Intent i = new Intent(this, signup.class);
        MainActivity.this.startActivity(i);
    }

    public void loginClick(View view) {
        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("response", response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");



                    //fetch user data from mysql
                    if(success) {
                        UserModel user = new UserModel(
                                jsonResponse.getInt("id"),
                                jsonResponse.getString("username"),
                                jsonResponse.getString("name"),
                                jsonResponse.getInt("parentId")
                        );

                        Intent i = new Intent(MainActivity.this, home2.class);
                        i.putExtra("user", user);
                        startActivity(i);
                    }

                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Login Failed!")
                                .setNegativeButton("Retry", null)
                                .show();
                    }
                } catch (JSONException e) {
                    Log.i("json esception", e.toString());
                }

            }
        };
        LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(loginRequest);
    }

}
