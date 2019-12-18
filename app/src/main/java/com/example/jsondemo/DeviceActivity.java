package com.example.jsondemo;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.CountDownTimer;
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

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DeviceActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    DeviceModel device;

    //VIEWS
    TextView device_name;

    TextView stats_hour;
    TextView stats_day;
    TextView stats_month;
    TextView bill_hour;
    TextView bill_day;
    TextView bill_month;

    Button btn_start;
    Button btn_end;
    Switch switch_schedule;
    Switch switch_device;
    LinearLayout schedule_layout;

    //json
    private RequestQueue mQueue;

    //datetime
    LocalDateTime currentDateTime;
    DateTimeFormatter format1;

    private static DecimalFormat df2;

    int buttonIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        mQueue = Volley.newRequestQueue(this);

        btn_start = findViewById(R.id.btn_start);
        btn_end = findViewById(R.id.btn_end);
        switch_schedule = findViewById(R.id.switch_schedule);
        switch_device    = findViewById(R.id.switch_device);

        schedule_layout = findViewById(R.id.buttons_layout);

        //textviews
        stats_hour = findViewById(R.id.stats_hour);
        stats_day = findViewById(R.id.stats_day);
        stats_month = findViewById(R.id.stats_month);
        bill_hour = findViewById(R.id.bill_hour);
        bill_day = findViewById(R.id.bill_day);
        bill_month = findViewById(R.id.bill_month);

        device_name = findViewById(R.id.DEVICE_NAME);

        Intent intent = getIntent();
        device = intent.getParcelableExtra("device");

        device_name.setText(device.getName());

        //calendar
        format1 = DateTimeFormatter.ofPattern("yyyy-M-d H:mm:ss");
        currentDateTime = LocalDateTime.now();

        df2 = new DecimalFormat("#.####");

/*
        new CountDownTimer(10000000,10000) {
            @Override
            public void onTick(long millisUntilFinished) {
                jsonParse();

            }

            @Override
            public void onFinish() {
                jsonParse();
            }
        }.start();
        */



        //Toast.makeText(DeviceActivity.this, "minute: " + hour, Toast.LENGTH_SHORT).show();

        jsonParse();

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

        switch_device.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String url = "https://nodemcupractice.000webhostapp.com/api/led/update.php?id=1&status=on";
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("VOLLEY ERROR", error.toString());
                        }

                    });

                    mQueue.add(request);
                } else {
                    String url = "https://nodemcupractice.000webhostapp.com/api/led/update.php?id=1&status=off";
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

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

    public void jsonParse() {

        String url = "https://nodemcupractice.000webhostapp.com/api/current/device_data.php?device=" + device.getId();

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

                                Toast.makeText(DeviceActivity.this, "length: " + userArray.length(), Toast.LENGTH_SHORT).show();

                                double hourly = 0;
                                double daily = 0;
                                double monthly = 0;

                                for (int i = 0; i < userArray.length(); i++) {

                                    JSONObject current = userArray.getJSONObject(i);

                                    LocalDateTime deviceDateTime = LocalDateTime.from(format1.parse(current.getString("datetime")));


                                    if(currentDateTime.getYear() == deviceDateTime.getYear()) {
                                        if(currentDateTime.getMonth() == deviceDateTime.getMonth()) {
                                            monthly += Double.parseDouble(current.getString("current"));
                                            if(currentDateTime.getDayOfMonth() == deviceDateTime.getDayOfMonth()) {
                                                daily += Double.parseDouble(current.getString("current"));
                                                if(deviceDateTime.getHour() == currentDateTime.getHour()) {
                                                    hourly += Double.parseDouble(current.getString("current"));
                                                }
                                            }
                                        }
                                    }


                                }

                                hourly /= 1000;
                                daily /= 1000;
                                monthly /= 1000;

                                stats_hour.setText(df2.format(hourly));
                                bill_hour.setText("P" + df2.format(hourly*9.56));

                                stats_day.setText(df2.format(daily));
                                bill_day.setText("P" + df2.format(daily*9.56));

                                stats_month.setText(df2.format(daily));
                                bill_month.setText("P" + df2.format(monthly*9.56));
                            }

                            else {
                                Toast.makeText(DeviceActivity.this, "server fail", Toast.LENGTH_SHORT).show();
                                Log.i("response",  response.getInt("success") + "");
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
