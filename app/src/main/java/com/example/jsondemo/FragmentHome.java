package com.example.jsondemo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;

public class FragmentHome extends Fragment {

    ArrayList<DeviceModel> deviceList;
    //layout
    private RecyclerView recyclerView;
    private DeviceAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //json
    private RequestQueue mQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        home2 home = (home2) getActivity();
        mQueue = Volley.newRequestQueue(home);
        deviceList = new ArrayList<>();
        int userId = home.getUserObject().getId();

        jsonParse(view);

        return view;
    }

    //---card view---//
    //change item
    public void changeItem(int position, String text) {
        //deviceList.get(position).changeText(text);
        mAdapter.notifyItemChanged(position);
    }

    //recycler view builder
    public void buildRecyclerView(View view) {
        final home2 home = (home2) getActivity();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        mAdapter = new DeviceAdapter(deviceList);
        mLayoutManager = new GridLayoutManager(home, 2);

        //recyclerView builder
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new DeviceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent i = new Intent(home, DeviceActivity.class);

                DeviceModel device = deviceList.get(position);

                i.putExtra("device", device);
                getActivity().startActivity(i);
            }
        });
    }

    //Devices JSON data download
    private void jsonParse(View view) {

        final View homeView = view;

        home2 home = (home2) getActivity();

        int userId = home.getUserObject().getId();

        String url = "https://nodemcupractice.000webhostapp.com/api/device/user_devices.php?userId="+userId;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        home2 home = (home2) getActivity();

                        try {
                            //JSONArray success = response.getJSONArray("user");
                            int querySuccess = response.getInt("success");


                            Log.i("JSON PARSE", Integer.toString(querySuccess));

                            if(querySuccess == 1) {

                                JSONArray userArray = response.getJSONArray("devices");

                                for (int i = 0; i < userArray.length(); i++) {

                                    JSONObject user = userArray.getJSONObject(i);

                                    int id = user.getInt("id");
                                    String name = user.getString("name");
                                    int userId = user.getInt("userId");
                                    int roomId = user.getInt("roomId");
                                    boolean active = Boolean.valueOf(user.getString("active"));
                                    boolean relay = Boolean.valueOf(user.getString("relay"));
                                    String start_time = user.getString("start_time");
                                    String end_time = user.getString("end_time");

                                    deviceList.add(new DeviceModel(id, userId, name, roomId, active, relay, R.drawable.ic_tv_black_, start_time, end_time));

                                    Log.i("devices", name + active + relay);
                                }

                                buildRecyclerView(homeView);

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
