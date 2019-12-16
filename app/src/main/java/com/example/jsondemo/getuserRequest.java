package com.example.jsondemo;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class getuserRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "https://nodemcupractice.000webhostapp.com/api/user/getuser.php";

    private Map<String, String> params;

    public getuserRequest(int id, Response.Listener<String> listener) {
        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("id", Integer.toString(id));
    }

    @Override
    public Map<String, String> getParams() {
        return params;

    }
}
