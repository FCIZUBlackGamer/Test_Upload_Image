package akhaled.shahen.com.exchangeme;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import static android.provider.Settings.System.AIRPLANE_MODE_ON;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

public class Result_Activity extends AppCompatActivity {

    String Img, Name, Discription, user_email, type, price, capital, phone, date,l,ln;
    ImageView imageView;
    TextView F_Name, F_Discription, F_user_email, F_type, F_price, F_capital, F_date;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_result_ );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        intent = getIntent();

        imageView = (ImageView)findViewById( R.id.pro_img );
        F_Name = (TextView) findViewById( R.id.product_name );
        F_Discription = (TextView) findViewById( R.id.owner_description );
        F_user_email = (TextView) findViewById( R.id.product_user_email );
        F_type = (TextView) findViewById( R.id.product_type );
        F_price = (TextView) findViewById( R.id.price );
        F_capital = (TextView) findViewById( R.id.owner_capital );
        F_date = (TextView) findViewById( R.id.product_time );



            if (isNetworkConnected()) {

                if (intent.getStringExtra( "From" ).equals( "Profile" )) {
                    Name = intent.getStringExtra( "product_name" ) ;
                    Discription = intent.getStringExtra( "description" ) ;
                    Img = intent.getStringExtra( "product_img" ) ;
                    type = intent.getStringExtra( "type" ) ;
                    phone = intent.getStringExtra( "phone" ) ;
                    price = intent.getStringExtra( "price" ) ;
                    capital = intent.getStringExtra( "capital" ) ;
                    date = intent.getStringExtra( "date" ) ;

                    F_Name.setText( Name );
                    F_Discription.setText( Discription );
                    F_user_email.setText( "" );
                    F_type.setText( type );
                    F_price.setText( price );
                    F_date.setText( date );
                    F_capital.setText( capital );

                    Picasso.with( this )
                            .load( Img )
                            .into( imageView );

                }else if (intent.getStringExtra( "From" ).equals( "Home" )) {
                    Name = intent.getStringExtra( "product_name" ) ;
                    Discription = intent.getStringExtra( "description" ) ;
                    Img = intent.getStringExtra( "product_img" ) ;
                    user_email = intent.getStringExtra( "user_email" ) ;
                    type = intent.getStringExtra( "type" ) ;
                    phone = intent.getStringExtra( "phone" ) ;
                    price = intent.getStringExtra( "price" ) ;
                    capital = intent.getStringExtra( "capital" ) ;
                    date = intent.getStringExtra( "date" ) ;

                    F_Name.setText( Name );
                    F_Discription.setText( Discription );
                    F_user_email.setText( user_email );
                    F_type.setText( type );
                    F_price.setText( price );
                    F_date.setText( date );
                    F_capital.setText( capital );

                    Picasso.with( this )
                            .load( Img )
                            .into( imageView );

                }
            }else {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(Result_Activity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(Result_Activity.this);
                }
                builder.setTitle("Error Message!")
                        .setMessage("Make Sure You Are Connected To Wifi!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

        FloatingActionButton fab = (FloatingActionButton) findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null)));
            }
        } );
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public void getpath(View view) {
        StringRequest stringRequest = new StringRequest( Request.Method.POST, "https://programmerx.000webhostapp.com/ExchangeMe/getlocation.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String s = URLEncoder.encode( response, "ISO-8859-1" );
                            response = URLDecoder.decode( s, "UTF-8" );
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                                JSONObject object = jsonArray.getJSONObject(0);

                                       l=object.getString("lat");
                                        ln=object.getString("long");
                            Log.d("ggggggggggg",l+" "+ln);


                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //progressDialog.dismiss();


                     //   Toast.makeText( Result_Activity.this,response,Toast.LENGTH_SHORT ).show();
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?daddr="+Double.parseDouble(l)+","+Double.parseDouble(ln)));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER );
                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                        startActivity(intent);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                Toast.makeText(Result_Activity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();

                hashMap.put("email",user_email);
                return hashMap;
            }
        };
        Volley.newRequestQueue(Result_Activity.this).add(stringRequest);

    }


}
