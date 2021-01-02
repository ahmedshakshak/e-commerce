package com.example.e_commerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
                            String accessToken = response.getString( Constants.ACCESS_TOKEN );
                            String refreshToken = response.getString( Constants.REFRESH_TOKEN );
                            boolean rememberMe = ((CheckBox) findViewById( R.id.checkBox_remember_me )).isChecked();

                            SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();

                            myEdit.putString( Constants.ACCESS_TOKEN, accessToken);
                            myEdit.putString( Constants.REFRESH_TOKEN, refreshToken);
                            myEdit.putBoolean( Constants.REMEMBER_ME, rememberMe);
                            myEdit.commit();
                            Toast.makeText( LoginActivity.this, "Logged-in successfully",
                                    Toast.LENGTH_LONG).show();
                            Intent product = new Intent( LoginActivity.this, ProductActivity.class );
                            product.putExtra( Constants.ACCESS_TOKEN, accessToken );
                            product.putExtra( Constants.REFRESH_TOKEN, refreshToken );
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

        Button btnForgetPassword = (Button) findViewById( R.id.button_forget_password );
        btnForgetPassword.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println( "a7na gowa el button now" );
                Intent resetPassword = new Intent( LoginActivity.this, ResetPasswordActivity.class );
                startActivity( resetPassword );
            }
        } );
    }
}
