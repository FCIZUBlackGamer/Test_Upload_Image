package com.example.mcst.cleanyourhome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    Button button;
    EditText full_name, email, pass, c_pass, phone;
    Spinner country;
    String S_full_name, S_email, S_pass, S_c_pass, S_phone, S_country;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    boolean valid = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        full_name = (EditText)findViewById( R.id.fullName );
        email = (EditText)findViewById( R.id.userEmailId );
        pass = (EditText)findViewById( R.id.password );
        c_pass = (EditText)findViewById( R.id.confirmPassword );
        phone = (EditText)findViewById( R.id.mobileNumber );
        country = (Spinner) findViewById( R.id.location );
        button =(Button) findViewById(R.id.signUpBtn);
        email.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (email.getText().toString().matches(emailPattern) && charSequence.length() > 0)
                {
                    //Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
                    valid = true;
                }else
                {
                    //Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
                    valid = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        } );
        button.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        S_c_pass = c_pass.getText().toString();
        S_pass = pass.getText().toString();
        S_country = country.getSelectedItem().toString();
        S_email = email.getText().toString();
        S_full_name = full_name.getText().toString();
        S_phone = phone.getText().toString();
        if (email.getText().length()<=2){
            email.setError( "Please write a valid Email" );
            valid = false;
        }
        if (full_name.getText().length()<=2){
            full_name.setError( "Please write a valid Name" );
            valid = false;
        }

            if (S_pass.equals( S_c_pass ) && S_pass.length()>=6) {
                if (S_email.isEmpty()){
                    email.setError( "Please Fill Email" );
                }else {
                    if (!valid){
                        //Toast.makeText(Register.this,"Invalid email address",Toast.LENGTH_SHORT).show();
                        email.setError( "Invalid email address" );
                    }else {
                        Register( S_full_name, S_pass, S_email, S_country, S_phone );
                    }
                }
            } else {
                //Toast.makeText( Register.this, "Password Doesn't Match", Toast.LENGTH_LONG ).show();
                c_pass.setError( "Password Doesn't Match or Less than 6 Letters" );
            }


    }

    private void Register(final String name, final String pass, final String email, final String country, final String phone) {
        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Loading Data ...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest( Request.Method.POST, "https://programmerx.000webhostapp.com/UserRegister.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String s = URLEncoder.encode( response, "ISO-8859-1" );
                            response = URLDecoder.decode( s, "UTF-8" );
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();

                        Toast.makeText( RegisterActivity.this,response,Toast.LENGTH_SHORT ).show();
                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity( intent );
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("name",name);
                hashMap.put("email",email);
                hashMap.put("password",pass);
                hashMap.put("capital",country);
                hashMap.put("phone",phone);
                return hashMap;
            }
        };
        Volley.newRequestQueue(RegisterActivity.this).add(stringRequest);
    }

}
