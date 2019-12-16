package com.example.jsondemo;

import android.os.AsyncTask;
import android.os.Bundle;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

        /*
        DownloadTask task = new DownloadTask();
        task.execute("\"https://nodemcupractice.000webhostapp.com/api/device/user_devices.php?userId="+ userId);
        */

        buildRecyclerView(view);

        return view;
    }

    //---card view---//
    //change item
    public void changeItem(int position, String text) {
        //deviceList.get(position).changeText(text);
        mAdapter.notifyItemChanged(position);
    }

    /*
    //device list creator
    public void createDeviceList() {
        deviceList = new ArrayList<>();
        deviceList.add(new DeviceModel(0, 1, "TV", null, true, R.drawable.ic_tv_black_));
        deviceList.add(new DeviceModel(2, 1, "Air con", null, true, R.drawable.ic_tv_black_));
        deviceList.add(new DeviceModel(3, 1, "CONSOLE", null, true, R.drawable.ic_tv_black_));
    }
    */

    public void addDeviceList(ArrayList deviceList, int id, int userId, String name, int roomId, boolean active, boolean relay) {
        deviceList.add(new DeviceModel(id, userId, name, roomId, active, relay, R.drawable.ic_tv_black_));
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
                changeItem(position, "clicked!");
            }
        });


    }
    //---end card view---//

    //---json---//
    private void jsonParse() {

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

                                    deviceList.add(new DeviceModel(id, userId, name, roomId, active, relay, R.drawable.ic_tv_black_));
                                    addDeviceList(deviceList,id, userId, name, roomId, active, relay);

                                    Log.i("devices", name + active + relay);
                                }

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

    //---end json---//

    //AsyncTask
    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                }

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                JSONObject jsonObject = new JSONObject(result);
                JSONArray userArray = jsonObject.getJSONArray("devices");

                for (int i = 0; i < userArray.length(); i++) {

                    JSONObject user = userArray.getJSONObject(i);

                    int id = user.getInt("id");
                    String name = user.getString("name");
                    int userId = user.getInt("userId");
                    int roomId = user.getInt("roomId");
                    boolean active = Boolean.valueOf(user.getString("active"));
                    boolean relay = Boolean.valueOf(user.getString("relay"));

                    deviceList.add(new DeviceModel(id, userId, name, roomId, active, relay, R.drawable.ic_tv_black_));
                    addDeviceList(deviceList,id, userId, name, roomId, active, relay);

                    Log.i("devices", name + active + relay);
                }

                /*
                JSONObject jsonObject = new JSONObject(result);

                String weatherInfo = jsonObject.getString("weather");

                Log.i("Weather content", weatherInfo);

                JSONArray arr = new JSONArray(weatherInfo);

                for (int i = 0; i < arr.length(); i++) {

                    JSONObject jsonPart = arr.getJSONObject(i);

                    Log.i("main", jsonPart.getString("main"));
                    Log.i("description", jsonPart.getString("description"));

                }
                */


            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }
}
