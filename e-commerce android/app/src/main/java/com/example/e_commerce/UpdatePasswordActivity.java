package com.example.e_commerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdatePasswordActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.update_password );
        Button btnUpdatePassword = (Button) findViewById( R.id.button_update_password );
        btnUpdatePassword.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextNewPassword = (EditText) findViewById( R.id.editText_new_password );
                EditText editTextToken = (EditText) findViewById( R.id.editText_token );

                Response.Listener res = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString( Constants.MESSAGE );
                            Toast.makeText(UpdatePasswordActivity.this, message,
                                    Toast.LENGTH_LONG).show();
                            Intent login = new Intent( UpdatePasswordActivity.this, LoginActivity.class );
                            startActivity( login );


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                try {
                    JSONObject postData = new JSONObject();

                    postData.put("password", editTextNewPassword.getText().toString());
                    postData.put(Constants.AUTHORIZATION, editTextToken.getText().toString());

                    HTTP.post( UpdatePasswordActivity.this, HTTP.UPDATE_PASSWORD, postData, res );
                } catch (JSONException e) {
                    System.out.println( "Error" );
                    e.printStackTrace();

                }
            }
        } );

    }
}
