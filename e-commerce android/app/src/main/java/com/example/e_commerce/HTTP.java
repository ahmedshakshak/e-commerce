package com.example.e_commerce;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HTTP {
    private static final String BASE_URL = "http://10.0.2.2:5000/api";
    public static final String REGISTER_URl = HTTP.BASE_URL + "/register";
    public static final String LOGIN_URl = HTTP.BASE_URL + "/login";
    public static final String RESET_PASSWORD = HTTP.BASE_URL + "/reset-password";
    public static final String UPDATE_PASSWORD = HTTP.BASE_URL + "/update-password";


    static public void post(Context context, String URL, JSONObject postData, Response.Listener resListener) {
       try {
           postData.put("Content-Type", "application/json; charset=utf-8");
       } catch (JSONException e) {
           e.printStackTrace();
       }

       String Authorization = "";
       try {
           Authorization = postData.getString( Constants.AUTHORIZATION );
       } catch (Exception e) {

       }
       final String auth = Authorization;
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        Response.ErrorListener resErr =  new Response.ErrorListener(){
            @Override
            public void onErrorResponse (VolleyError error){
                error.printStackTrace();
            }
        };

        if(Authorization.isEmpty()) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, postData, resListener, resErr);
            requestQueue.add(jsonObjectRequest);
        }
        else {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, postData, resListener, resErr) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    //headers.put("Content-Type", "application/json");
                    headers.put(Constants.AUTHORIZATION, "Bearer " + auth);
                    return headers;
                }
            };
            requestQueue.add(jsonObjectRequest);
        }
    }

}
