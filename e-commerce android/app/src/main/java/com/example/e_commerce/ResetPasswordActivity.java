package com.example.e_commerce;

import android.content.Intent;
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

public class ResetPasswordActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.reset_password );
        Button btnResetPassword = (Button) findViewById( R.id.button_reset_password );
        final EditText editTextEmail = (EditText) findViewById( R.id.editText_email );
        btnResetPassword.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener res = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString( Constants.MESSAGE );
                            Toast.makeText(ResetPasswordActivity.this, message,
                                    Toast.LENGTH_LONG).show();
                            Intent updatePassword = new Intent( ResetPasswordActivity.this, UpdatePasswordActivity.class );
                            startActivity( updatePassword );


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                try {
                    JSONObject postData = new JSONObject();
                    postData.put("email", editTextEmail.getText().toString());
                    HTTP.post( ResetPasswordActivity.this, HTTP.RESET_PASSWORD, postData, res );
                } catch (JSONException e) {
                    System.out.println( "Error" );
                    e.printStackTrace();

                }
            }
        } );

    }
}
