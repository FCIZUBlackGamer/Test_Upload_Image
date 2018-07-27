package akhaled.shahen.com.exchangeme;

import android.app.ProgressDialog;
import android.content.Intent;
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    Button login;
    TextView signup;
    EditText email, password;
    String get_email, get_pass;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        email = (EditText)findViewById( R.id.email );
        password = (EditText)findViewById( R.id.pass );
        login = (Button)findViewById( R.id.login );
        signup= (TextView)findViewById( R.id.sign_up_text );
        signup.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(Login.this,Register.class ) );
            }
        } );
        login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_email = email.getText().toString();
                get_pass = password.getText().toString();
                login_Now(get_email, get_pass);
            }
        } );
    }

    private void login_Now(final String email, final String password) {
        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Loading Data ...");
        progressDialog.show();
        //
        StringRequest stringRequest = new StringRequest( Request.Method.POST, "https://programmerx.000webhostapp.com/ExchangeMe/Login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        if (response.equals( "Welcome Home!" )) {

                            intent = new Intent( Login.this, Switch_Nav.class );
                            intent.putExtra( "email",get_email );
                            startActivity( intent );
                        }else {
                            //welcome home
                          Toast.makeText(Login.this,response,Toast.LENGTH_SHORT).show();

                        }
                    }
                    // no connection with server
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Login.this,error.getMessage(),Toast.LENGTH_SHORT).show();


            }
        }){
            ////
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("email",email);
                hashMap.put("password",password);
                return hashMap;
            }
        };
        ////
        Volley.newRequestQueue(Login.this).add(stringRequest);
    }

}
