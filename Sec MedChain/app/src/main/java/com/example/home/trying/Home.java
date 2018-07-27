package com.example.home.trying;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {

    Button check, profile;
    String username;
    String email;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        check = (Button)findViewById( R.id.button3 );
        profile = (Button)findViewById( R.id.profile );
        final TextView welcomeMessage = (TextView) findViewById(R.id.tvWelcomeMsg);

        intent = getIntent();
        email = intent.getStringExtra("email");
        final ProgressDialog progressDialog = new ProgressDialog( this );
        progressDialog.setMessage( "Loading ..." );
        progressDialog.show();
        StringRequest stringRequest = new StringRequest( Request.Method.POST, "https://secmedchain.000webhostapp.com/GetUserName.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String s = null;
                        try {
                            s = URLEncoder.encode(response,"ISO-8859-1");
                            response = URLDecoder.decode(s,"UTF-8");
                            username = response;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Home.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("email",email);
                return hashMap;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);



        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                String message = username + " welcome to your secMedChain";
                welcomeMessage.setText(message);
                progressDialog.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 3000);
        check.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent( Home.this,ScanMedicineActivity.class );
                intent1.putExtra( "username",username );
                intent1.putExtra( "email",email );
                startActivity( intent1 );
            }
        } );

        profile.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent( Home.this,UserProfileActivity.class );
                intent1.putExtra( "username",username );
                intent1.putExtra( "email",email );
                //Toast.makeText( Home.this,username+" "+email,Toast.LENGTH_SHORT ).show();

                startActivity( intent1 );

            }
        } );

    }

}
