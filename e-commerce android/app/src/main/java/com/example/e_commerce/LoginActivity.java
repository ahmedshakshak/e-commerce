package com.example.e_commerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.login );

        final EditText editTextUsername = (EditText) findViewById( R.id.editText_username );
        final EditText editTextPassword = (EditText) findViewById( R.id.editText_password );

        Button btnLogin = (Button) findViewById( R.id.button_login );
        btnLogin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener res = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String accessToken = response.getString( Constants.accessToken );
                            String refreshToken = response.getString( Constants.refreshToken );
                            boolean rememberMe = ((CheckBox) findViewById( R.id.checkBox_remember_me )).isChecked();

                            SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();

                            myEdit.putString( Constants.accessToken, accessToken);
                            myEdit.putString( Constants.refreshToken, refreshToken);
                            myEdit.putBoolean( Constants.rememberMe, rememberMe);
                            myEdit.commit();
                            Toast.makeText( LoginActivity.this, "Logged-in successfully",
                                    Toast.LENGTH_LONG).show();
                            Intent product = new Intent( LoginActivity.this, ProductActivity.class );
                            product.putExtra( Constants.accessToken, accessToken );
                            product.putExtra( Constants.refreshToken, refreshToken );
                            startActivity( product );


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                try {
                    JSONObject postData = new JSONObject();

                    postData.put("username", editTextUsername.getText().toString());
                    postData.put("password", editTextPassword.getText().toString());

                    HTTP.post( LoginActivity.this, HTTP.LOGIN_URl, postData, res );
                } catch (JSONException e) {
                    System.out.println( "Error" );
                    e.printStackTrace();

                }
            }
        } );
    }
}
