package com.example.jsondemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class home2 extends AppCompatActivity {

    private DatabaseHelper myDB;
    private UserModel user;

    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        myDB = new DatabaseHelper(this);

        mQueue = Volley.newRequestQueue(this);
        //storeCurrentData();

        //bottom navigation manager
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome()).commit();

        //LOAD USER DATA
        Intent i = getIntent();
        user = i.getParcelableExtra("user");

        //store to shared preferences
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("userId", user.getId());
        editor.commit();


    }

    //bottom navigation setter
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new FragmentHome();
                            break;
                        case R.id.nav_reports:
                            selectedFragment = new FragmentReports();
                            break;
                        case R.id.nav_users:
                            selectedFragment = new FragmentUsers();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

    //get user
    public UserModel getUserObject() {
        return user;
    }

    //store current from json to mysql
    private void storeCurrentData() {

        String url = "https://nodemcupractice.000webhostapp.com/api/current/read_all.php";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //JSONArray success = response.getJSONArray("user");ß
                            int querySuccess = response.getInt("success");


                            Log.i("JSON PARSE", Integer.toString(querySuccess));

                            if(querySuccess == 1) {

                                JSONArray userArray = response.getJSONArray("current");

                                for (int i = 0; i < userArray.length(); i++) {
                                    JSONObject current = userArray.getJSONObject(i);

                                    if (
                                        myDB.insertCurrent(
                                                current.getInt("id"),
                                                current.getInt("device"),
                                                current.getDouble("current"),
                                                current.getString("datetime")
                                        )
                                    ) {
                                        //Toast.makeText(home2.this, "current data loaded!", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        //Toast.makeText(home2.this, "databasee save fail", Toast.LENGTH_SHORT).show();
                                    }

                                }

                            }

                            else {
                                Toast.makeText(home2.this, "server fail", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Log.i("JSON ERROR", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("VOLLEY ERROR", error.toString());
            }

        });

        mQueue.add(request);
    }
}
