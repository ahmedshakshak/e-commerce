package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,
                                                                    MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN, "");
        String refreshToken = sharedPreferences.getString(Constants.REFRESH_TOKEN, "");
        Boolean rememberMe= sharedPreferences.getBoolean(Constants.REMEMBER_ME, false);

        if(rememberMe) {
            Intent product = new Intent( MainActivity.this, ProductActivity.class );
            product.putExtra( Constants.ACCESS_TOKEN, accessToken );
            product.putExtra( Constants.REFRESH_TOKEN, refreshToken );
            startActivity( product );
        }


        Button btnLogin = (Button) findViewById( R.id.button_login );
        btnLogin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent( MainActivity.this, LoginActivity.class );
                startActivity( login );
            }
        } );

        Button btnRegister = (Button) findViewById( R.id.button_register );
        btnRegister.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent( MainActivity.this, RegisterActivity.class );
                startActivity( register );
            }
        } );
    }

}
