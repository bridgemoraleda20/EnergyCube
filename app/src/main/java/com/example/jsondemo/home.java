package com.example.jsondemo;

import android.app.Activity;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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

import static android.view.View.VISIBLE;

public class home extends AppCompatActivity {

    private RequestQueue mQueue;

    Activity home;
    private static DecimalFormat df2;

    TextView txtWatts;
    TextView txtPrice;
    EditText editPriceRate;
    ToggleButton btnToggle;
    double priceRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtWatts = (TextView) findViewById(R.id.txtWatts);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        editPriceRate = (EditText) findViewById(R.id.editPriceRate);
        btnToggle = (ToggleButton) findViewById(R.id.btnToggle);

        df2 = new DecimalFormat("#.##");
        home = this;

        mQueue = Volley.newRequestQueue(this);

        parseCurrent();


            new CountDownTimer(10000000,10000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    parseCurrent();

                }

                @Override
                public void onFinish() {
                    parseCurrent();
                }
            }.start();

        btnToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

    }


    public void parseCurrent() {
        String url = "https://nodemcupractice.000webhostapp.com/api/current/read_last.php";


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //JSONArray success = response.getJSONArray("user");
                            int querySuccess = response.getInt("success");


                            Log.i("JSON PARSE", Integer.toString(querySuccess));

                            if(querySuccess == 1) {

                                JSONArray userArray = response.getJSONArray("current");

                                    JSONObject user = userArray.getJSONObject(0);

                                    int id = user.getInt("id");
                                    String device = user.getString("device");
                                    String current = user.getString("current");
                                    String dateTime = user.getString("datetime");

                                    double KiloWattHour = Double.parseDouble(current) * 220;
                                    double watts = (KiloWattHour * 5) / 18;
                                    String stringWatts = df2.format(watts).toString();
                                    txtWatts.setText(df2.format(watts)+"w");

                                    //priceRate = Double.parseDouble(editPriceRate.getText().toString());
                                    //txtPrice.setText("Php" + stringWatts +" /h");

                            }

                            else {
                                Toast.makeText(home, "server fail", Toast.LENGTH_SHORT).show();
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
