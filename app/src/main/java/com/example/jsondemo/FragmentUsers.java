package com.example.jsondemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FragmentUsers extends Fragment {

    TextView name, username;
    Button btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        final home2 home = (home2) getActivity();

        name = (TextView) view.findViewById(R.id.viewName);
        username = (TextView) view.findViewById(R.id.viewUsername);
        btnLogout = (Button) view.findViewById(R.id.btnLogout);

        name.setText(home.getUserObject().getName());
        username.setText(home.getUserObject().getUsername());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = home.getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("userId", -1);
                editor.commit();

                Intent i = new Intent(home, MainActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

}
