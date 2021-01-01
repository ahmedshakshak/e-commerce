package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.CompletionService;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,
                                                                    MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(Constants.accessToken, "");
        String refreshToken = sharedPreferences.getString(Constants.refreshToken, "");
        Boolean rememberMe= sharedPreferences.getBoolean(Constants.rememberMe, false);

        if(rememberMe) {
            Intent product = new Intent( MainActivity.this, ProductActivity.class );
            product.putExtra( Constants.accessToken, accessToken );
            product.putExtra( Constants.refreshToken, refreshToken );
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
