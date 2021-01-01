package com.example.e_commerce;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        final EditText editTextFirstName = (EditText) findViewById( R.id.editText_first_name );
        final EditText editTextLastName = (EditText) findViewById( R.id.editText_last_name );
        final EditText editTextUsername = (EditText) findViewById( R.id.editText_username );
        final EditText editTextEmail = (EditText) findViewById( R.id.editText_email );
        final EditText editTextPassword = (EditText) findViewById( R.id.editText_password );
        final EditText editTextJob = (EditText) findViewById( R.id.editText_job );
        final RadioGroup radioGroupgender = (RadioGroup) findViewById( R.id.radioGroup_gender );

        final EditText editTextBirthDay = (EditText) findViewById( R.id.editText_birthday );
        editTextBirthDay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog( RegisterActivity.this,
                        R.style.Theme_AppCompat_DayNight_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                editTextBirthDay.setText( String.valueOf( year ) + '-' + String.valueOf( month + 1 ) + '-' + String.valueOf( day ) );
                            }
                        }, year, month, day ).show();


            }
        } );


        final Button btnRegister = (Button) findViewById(R.id.button_register);
        btnRegister.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allRegisterFieldsNotEmpty()) {
                    // connect api
                    Response.Listener res = new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String accessToken = response.getString( Constants.accessToken );
                                String refreshToken = response.getString( Constants.refreshToken );

                                SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();

                                myEdit.putString( Constants.accessToken, accessToken);
                                myEdit.putString( Constants.refreshToken, refreshToken);
                                myEdit.commit();

                                Toast.makeText(RegisterActivity.this, "Registered successfully",
                                        Toast.LENGTH_LONG).show();
                                Intent login = new Intent( RegisterActivity.this, LoginActivity.class );
                                startActivity( login );

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    try {
                        JSONObject postData = new JSONObject();

                        postData.put("first_name", editTextFirstName.getText().toString());
                        postData.put("last_name", editTextLastName.getText().toString());
                        postData.put("username", editTextUsername.getText().toString());
                        postData.put("email", editTextEmail.getText().toString());
                        postData.put("password", editTextPassword.getText().toString());
                        postData.put("birthdate", editTextBirthDay.getText().toString());
                        postData.put("job", editTextJob.getText().toString());
                        int selectedId = radioGroupgender.getCheckedRadioButtonId();
                        RadioButton radioGeneder = (RadioButton)findViewById(selectedId);
                        postData.put("gender", radioGeneder.getText().toString());

                        HTTP.post( RegisterActivity.this, HTTP.REGISTER_URl , postData, res );
                    } catch (JSONException e) {
                        System.out.println( "Error" );
                        e.printStackTrace();

                    }

                }
                else {
                    Toast.makeText(RegisterActivity.this, "All Fields are Required",
                            Toast.LENGTH_LONG).show();

                }
            }
        } );
    }

    private boolean allRegisterFieldsNotEmpty() {
        ViewGroup group = (ViewGroup)findViewById(R.id.layout_register);
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                if(((EditText) view).getText().toString().isEmpty()) {
                    return false;
                }
            }
            else if(view instanceof RadioGroup) {
                if(((RadioGroup) view).getCheckedRadioButtonId() == -1) {
                    return false;
                }
            }
        }

        return true;
    }
}
