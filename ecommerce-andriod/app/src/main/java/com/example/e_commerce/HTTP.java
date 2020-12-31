package com.example.e_commerce;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HTTP {
    public static String url = "http://10.0.2.2:5000/api" + "/register";

    static public void post(Context context, JSONObject postData, Response.Listener resListener) {
       try {
           postData.put("Content-Type", "application/json; charset=utf-8");
       } catch (JSONException e) {
           e.printStackTrace();
       }

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        Response.ErrorListener resErr =  new Response.ErrorListener(){
            @Override
            public void onErrorResponse (VolleyError error){
                error.printStackTrace();
            }
        };

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, HTTP.url, postData, resListener, resErr);
        requestQueue.add(jsonObjectRequest);
    }
}
