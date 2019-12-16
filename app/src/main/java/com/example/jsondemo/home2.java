package com.example.jsondemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class home2 extends AppCompatActivity {

    DatabaseHelper myDB;
    UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        myDB = new DatabaseHelper(this);


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
}
