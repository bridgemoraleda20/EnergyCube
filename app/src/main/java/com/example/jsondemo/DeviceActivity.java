package com.example.jsondemo;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

public class DeviceActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    DeviceModel device;

    //VIEWS
    TextView device_name;
    Button btn_start;
    Button btn_end;
    Switch switch_schedule;
    LinearLayout schedule_layout;

    int buttonIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        btn_start = findViewById(R.id.btn_start);
        btn_end = findViewById(R.id.btn_end);
        switch_schedule = findViewById(R.id.switch_schedule);
        schedule_layout = findViewById(R.id.buttons_layout);

        switch_schedule.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    schedule_layout.setVisibility(View.VISIBLE);
                } else {
                    schedule_layout.setVisibility(View.GONE);
                }
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonIndex = 1;
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "SCHEDULE ON");
            }
        });

        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonIndex = 2;
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "SCHEDULE OFF");
            }
        });

        device_name = findViewById(R.id.DEVICE_NAME);

        Intent i = getIntent();
        device = i.getParcelableExtra("device");

        device_name.setText(device.getName());

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String str_minute;
        String str_hour;
        String am_pm;

        if(minute < 9)
            str_minute = "0" + minute;
        else
            str_minute = minute + "";

        if(hourOfDay > 11 && hourOfDay != 12) {
            hourOfDay = hourOfDay - 12;
            am_pm= "pm";
        }
        else if (hourOfDay == 0) {
            hourOfDay = 12;
            am_pm = "am";
        }
        else if (hourOfDay == 12) {
            hourOfDay = 12;
            am_pm = "pm";
        }
        else {
            am_pm= "am";
        }
        str_hour = hourOfDay +"";

        if(buttonIndex == 1)
            btn_start.setText("ON\n"+ str_hour+ ":" + str_minute+am_pm);
        else
            btn_end.setText("OFF\n" + str_hour+ ":" + str_minute+am_pm);

    }
}
