package com.example.jsondemo;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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

import static java.security.AccessController.getContext;

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
    //ImageView backButton;

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
        //backButton = findViewById(R.id.imageButton_back);

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

        //SWITCHES setters
        if(device.isRelay())
            switch_device.setChecked(true);
        else
            switch_device.setChecked(false);

        if(device.isActive()) {
            switch_schedule.setChecked(true);
            schedule_layout.setVisibility(View.VISIBLE);
        }
        else {
            switch_schedule.setChecked(false);
            schedule_layout.setVisibility(View.GONE);
        }

        //DEVICE NAME Title setter
        device_name.setText(device.getName());

        //calendar
        format1 = DateTimeFormatter.ofPattern("yyyy-M-d H:mm:ss");
        currentDateTime = LocalDateTime.now();

        df2 = new DecimalFormat("#.###");

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

        //ON CLICK LISTENERS
        /*
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButton.setImageResource(R.drawable.back_icon);
                Intent intent = new Intent(DeviceActivity.this, home2.class);
                startActivity(intent);
            }
        });
        */

        //edit device name
        device_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DeviceActivity.this);
                builder.setTitle("Edit Device Name");
                ViewGroup group = (ViewGroup) findViewById(android.R.id.content);
                View viewInflated = LayoutInflater.from(DeviceActivity.this).inflate(R.layout.dialog_edit_device_name, group, false);

                // Set up the input
                final EditText input = (EditText) viewInflated.findViewById(R.id.editDeviceName);

                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(viewInflated);

                //update new name to database
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        device_name.setText(input.getText().toString());

                        //json exec
                        String url = "https://nodemcupractice.000webhostapp.com/api/device/update_name.php?id="+device.getId()+"&name=" + input.getText().toString();
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
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }

        });

        switch_schedule.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {



                if(isChecked) {

                    String url = "https://nodemcupractice.000webhostapp.com/api/device/toggle_active.php?id="+device.getId()+"&active=true";
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

                    device.setActive(true);
                    schedule_layout.setVisibility(View.VISIBLE);
                } else {
                    String url = "https://nodemcupractice.000webhostapp.com/api/device/toggle_active.php?id="+device.getId()+"&active=false";
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

                    device.setActive(false);
                    schedule_layout.setVisibility(View.GONE);
                }
            }
        });

        switch_device.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    String url = "https://nodemcupractice.000webhostapp.com/api/device/toggle_relay.php?id="+device.getId()+"&relay=true";
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

                    device.setRelay(true);
                    mQueue.add(request);
                } else {
                    String url = "https://nodemcupractice.000webhostapp.com/api/device/toggle_relay.php?id="+device.getId()+"&relay=false";
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

                    device.setRelay(false);
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
