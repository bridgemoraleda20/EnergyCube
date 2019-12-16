package com.example.jsondemo;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class signup extends AppCompatActivity {

    EditText et_username;
    EditText et_password;
    EditText et_password_c;
    EditText et_name;
    Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.pw_password);
        et_password_c = findViewById(R.id.pw_password_c);
        et_name = findViewById(R.id.et_name);
        btn_register = findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = et_username.getText().toString();
                final String password = et_password.getText().toString();
                final String password_c = et_password_c.getText().toString();
                final String name = et_name.getText().toString();

                //check password
                if(password.equals(password_c)) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("response", response);
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");


                                if(success) {
                                    Intent i = new Intent(signup.this, MainActivity.class);
                                    signup.this.startActivity(i);
                                }

                                else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(signup.this);
                                    builder.setMessage("Register Failed!")
                                            .setNegativeButton("Retry", null)
                                            .show();

                                }

                            } catch (JSONException e) {
                                Log.i("error", e.toString());
                            }
                        }
                    };

                    RegisterRequest registerRequest = new RegisterRequest(username, password, name, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(signup.this);
                    queue.add(registerRequest);
                }

                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(signup.this);
                    builder.setMessage("Password Did Not Match!")
                            .setNegativeButton("Retry", null)
                            .show();
                }

            }
        });


    }



}
