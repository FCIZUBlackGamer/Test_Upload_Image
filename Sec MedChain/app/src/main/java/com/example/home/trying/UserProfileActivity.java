package com.example.home.trying;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {

    ImageView image;
    TextView name;
    String getName, email;
    Intent intent;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<history_row_item> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        name = (TextView)findViewById( R.id.textView7 );
        image = (ImageView)findViewById( R.id.imageView4 );

        recyclerView = (RecyclerView) findViewById(R.id.listView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        final ProgressDialog progressDialog = new ProgressDialog( this );
//        progressDialog.setMessage( "Loading ..." );
//        progressDialog.show();
        items = new ArrayList<>();

        if (isNetworkConnected()) {
                    intent = getIntent();
                    getName = intent.getStringExtra( "username");
                    //getName = "m";
                    email = intent.getStringExtra( "email");
                    //email = "m@y.com";
                    Toast.makeText( UserProfileActivity.this,getName+" "+email,Toast.LENGTH_SHORT ).show();
                    //name.setText( getName );

//                    Runnable runnable = new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    };
//                    Thread thread = new Thread( runnable );
//                    thread.start();

                    downloadImage( image, name, email);
                    LoadHistory();

           // progressDialog.dismiss();
        }else {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
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
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private void LoadHistory() {
        final ProgressDialog progressDialog = new ProgressDialog( this );
        progressDialog.setMessage("Loading Data ...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest( Request.Method.POST, "https://secmedchain.000webhostapp.com/Profile.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String s = URLEncoder.encode(response,"ISO-8859-1");
                            response = URLDecoder.decode(s,"UTF-8");
                        }catch (UnsupportedEncodingException e){
                            e.printStackTrace();
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("history");
                            if (jsonObject.getJSONArray( "history" ).length()!=0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject( i );
                                    history_row_item row_item = new history_row_item(
                                            object.getString( "item" ) + " (" + object.getString( "state" ) + " )"
                                    );
                                    items.add( row_item );
                                    progressDialog.dismiss();
                                }
                                adapter = new History_Adapter( items, UserProfileActivity.this );
                                recyclerView.setAdapter( adapter );
                            }else {
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(UserProfileActivity.this,"2",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(UserProfileActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                Toast.makeText(UserProfileActivity.this,"4",Toast.LENGTH_SHORT).show();

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
    }

    private void downloadImage(final ImageView user_image,final TextView na, final String email) {
        final ProgressDialog progressDialog = new ProgressDialog( this );
        progressDialog.setMessage( "Getting Profile Picture ..." );
        progressDialog.show();
        StringRequest stringRequest = new StringRequest( Request.Method.POST, "https://secmedchain.000webhostapp.com/DownloadImage.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("image");
                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                na.setText( object.getString( "username" ) );
                                Picasso.with(UserProfileActivity.this)
                                        .load(object.getString( "img" ))
                                        .into(user_image);
                            }
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(UserProfileActivity.this,"1",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserProfileActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                Toast.makeText(UserProfileActivity.this,"3",Toast.LENGTH_SHORT).show();

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
    }
}
