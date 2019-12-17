package com.example.jsondemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DeviceActivity extends AppCompatActivity {

    DeviceModel device;
    TextView device_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        device_name = findViewById(R.id.DEVICE_NAME);

        Intent i = getIntent();
        device = i.getParcelableExtra("device");

        device_name.setText(device.getName());

    }
}
