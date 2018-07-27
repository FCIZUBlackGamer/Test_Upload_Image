package com.example.mcst.cleanyourhome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button button;
    EditText get_email, get_pass;
    String email, pass;
    Database database;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        get_email = (EditText)findViewById( R.id.login_emailid );
        get_pass = (EditText)findViewById( R.id.login_password );
        button = (Button)findViewById(R.id.loginBtn);
        database = new Database(this);
        cursor = database.ShowData();
        while (cursor.moveToNext()){
            if (!cursor.getString(1).equals("0")){
                startActivity(new Intent(LoginActivity.this,Cam.class));
            }
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = get_email.getText().toString();
                pass = get_pass.getText().toString();
                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Loading Data ...");
                progressDialog.show();
                StringRequest stringRequest = new StringRequest( Request.Method.POST, "https://programmerx.000webhostapp.com/Login.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    String s = URLEncoder.encode( response, "ISO-8859-1" );
                                    response = URLDecoder.decode( s, "UTF-8" );
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                                if (response.equals( "Welcome Home!" )) {
                                    progressDialog.dismiss();
                                    database.UpdateData( "1",email,pass,"0" );
                                    startActivity( new Intent( LoginActivity.this, Cam.class ) );
                                }else {
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this,response,Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap hashMap = new HashMap();
                        hashMap.put("email",email);
                        hashMap.put("password",pass);
                        return hashMap;
                    }
                };
                Volley.newRequestQueue(LoginActivity.this).add(stringRequest);
            }
        });
    }
}
