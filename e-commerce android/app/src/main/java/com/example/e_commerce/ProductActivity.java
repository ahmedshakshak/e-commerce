package com.example.e_commerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.listview );

        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = get(HTTP.PRODUCTS);
        queue.add(request);


        final EditText editTextSearch = (EditText) findViewById( R.id.editText_search );
        editTextSearch.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                HashMap<String, String> params = new HashMap<String, String>(  );
                params.put( "name", editTextSearch.getText().toString() );
                StringRequest request =  get( HTTP.addLocationToUrl( HTTP.PRODUCTS, params ) );
                queue.add(request);
                System.out.println( editTextSearch.getText() );
            }
        } );

    }

    public  StringRequest get(String URL) {
        StringRequest request = new StringRequest( Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array= new JSONArray( response );
                    ListView listViewProducts  = (ListView) findViewById( R.id.listView );
                    ProductAdapter productAdapter = new ProductAdapter(getApplicationContext(), array);
                    listViewProducts.setAdapter(productAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.toString());
            }
        });

        return  request;
    }
}
