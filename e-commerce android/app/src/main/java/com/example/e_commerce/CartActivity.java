package com.example.e_commerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.list_view_order );
        final GoogleMap[] googleMap = new GoogleMap[1];
        SupportMapFragment mapFregment = (SupportMapFragment) getSupportFragmentManager().findFragmentById( R.id.map );
        final EditText editTextAddress = findViewById( R.id.editText_addresss );
        mapFregment.getMapAsync( new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap it) {
                googleMap[0] = it;
                GoogleMap map = it;
                map.setOnMapClickListener( new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        try {
                            Geocoder geocoder;
                            List<Address> addresses;
                            geocoder = new Geocoder(CartActivity.this, Locale.getDefault());

                            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                            String address = addresses.get(0).getAddressLine(0);
                            editTextAddress.setText( address );
                        }catch (Exception e) {

                        }
                    }
                } );
            }
        } );



        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,
                MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN, "");
        String refreshToken = sharedPreferences.getString(Constants.REFRESH_TOKEN, "");
        final JSONArray jsonArray = new JSONArray(  );
        long total = 0;
        for (JSONObject item : Cart.getCart()) {
            try {
                jsonArray.put( item );
                total += item.getLong( "price" ) * item.getLong( "quantity" );
            }catch (Exception e) {

            }
        }
        TextView textView = findViewById( R.id.textViewTotal );
        textView.setText( String.valueOf( total ) );
        ListView listView = (ListView) findViewById( R.id.listview_orders );
        ProductAdapter productAdapter = new ProductAdapter(getApplicationContext(), jsonArray, false);
        listView.setAdapter(productAdapter);

        Button btn_order = (Button) findViewById( R.id.button_order );
        btn_order.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener res = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String msg = response.getString( "message" );

                            Toast.makeText(CartActivity.this, msg,
                                    Toast.LENGTH_LONG).show();
                            Intent products = new Intent( CartActivity.this, ProductActivity.class );
                            startActivity( products );

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                try {
                    JSONObject postData = new JSONObject();
                    postData.put("array", jsonArray);

                    SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,
                            MODE_PRIVATE);
                    String accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN, "");
                    postData.put( Constants.AUTHORIZATION, accessToken );

                    HTTP.post( CartActivity.this, HTTP.PRODUCTS , postData, res );
                } catch (JSONException e) {
                    System.out.println( "Error" );
                    e.printStackTrace();

                }
            }
        } );


    }
}
