package com.example.home.trying;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class ScanMedicineActivity extends AppCompatActivity {

    Button button4, alert;
    String name, pharmacy;
    TextView textresult, finalresult;
    AutoCompleteTextView textView;
    ImageButton image;
    Spinner mysinner;
    Intent intent;
    boolean final_state;
    TextView user_name;
    String get_email, get_user_name;
    ImageView user_image;
    static final String[] Medicin =
                    new String[] { "beclomethasone","ciprofloxacin", "cyanocobalamin",
                    "dextromethorphan", "fusiderm", "guaifenesin",
                    "panadol", "prednisolone", "vitamin_c",
                    "vitamin_d"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_medicine);
        intent = getIntent();
        get_user_name = intent.getStringExtra( "username" );
        get_email = intent.getStringExtra( "email" );
        user_name = (TextView)findViewById( R.id.name );
        user_name.setText( get_user_name );
        user_image = (ImageView)findViewById( R.id.img );
        textresult = (TextView)findViewById( R.id.textresult );
        finalresult = (TextView)findViewById( R.id.result );
        image = (ImageButton) findViewById( R.id.imageButton );
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        };
//        Thread thread = new Thread( runnable );
//        thread.start();

        downloadImage(user_image, get_email);
        image.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
                    startActivityForResult(intent, 0);
//                    textresult.setText(intent.getStringExtra("SCAN_RESULT"));
//                    textView.setText(intent.getStringExtra("SCAN_RESULT"));

                    ActivityCompat.requestPermissions(ScanMedicineActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            1);
                } catch (Exception e) {

                    Uri marketUri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);

                    try {
                            startActivity(marketIntent);
                    } catch (ActivityNotFoundException anfe) {
                        Log.e( "EXEX", String.valueOf( anfe ) );

                    }

                }
            }
        } );

        textView = (AutoCompleteTextView) findViewById(R.id.testAutoComplete);

        ArrayAdapter<String> medArray = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, Medicin);
        textView.setAdapter(medArray);

        mysinner = (Spinner) findViewById( R.id.spinner );

        button4 = (Button) findViewById(R.id.button4 );

        alert = (Button) findViewById(R.id.alert );
        alert.setEnabled( false );
        alert.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //insert into history table

                if (alert.isEnabled()){
                    Intent intent1 = new Intent( Intent.ACTION_SEND );
                    intent1.setType("message/rfc822");
                    intent1.putExtra(Intent.EXTRA_EMAIL  , new String[]{"princess4evers@hotmail.com"});
                    intent1.putExtra(Intent.EXTRA_SUBJECT, "Alert Message");
                    intent1.putExtra(Intent.EXTRA_TEXT   , name+" Not Authenticated by User "+intent.getStringExtra( "username" ));
                    startActivity( Intent.createChooser(intent1, "Send mail...") );
                }else {

                }
            }
        } );

        button4.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medicine();
            }
        } );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//                String contents = data.getStringExtra("SCAN_RESULT");
//                String format = data.getStringExtra("SCAN_RESULT_FORMAT");
                textresult.setText(data.getStringExtra("SCAN_RESULT"));
                textView.setText(data.getStringExtra("SCAN_RESULT"));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(ScanMedicineActivity.this, "Permission denied to Access Camera", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void downloadImage(final ImageView user_image, final String mail) {
        StringRequest stringRequest = new StringRequest( Request.Method.POST, "https://secmedchain.000webhostapp.com/DownloadImage.php",
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
                            JSONArray jsonArray = jsonObject.getJSONArray("image");
                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                Picasso.with(ScanMedicineActivity.this)
                                        .load(object.getString( "img" ))
                                        .into(user_image);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ScanMedicineActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("email",mail);
                return hashMap;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void medicine(){
        intialize();
        if(!validate()){
            Toast.makeText(this,"error",Toast.LENGTH_SHORT ).show();
        }
        else {
            final_state = SearchForItem();
            if (final_state){
                success();
            }else {
                failed();
            }
        }
    }

    private void failed() {
        finalresult.setText( "Result:\n"+name+"\n"+"Not Authenticate by "+intent.getStringExtra( "username" )+"\n"+"You Can Alert "+ intent.getStringExtra( "username" )+" by Clicking on Alert Button");
        alert.setEnabled( true );
        insertToHistory();
    }


    private void success() {
        StringRequest stringRequest = new StringRequest( Request.Method.POST, "https://secmedchain.000webhostapp.com/GetMedicinData.php",
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
                            JSONArray jsonArray = jsonObject.getJSONArray("med_data");
                            if (jsonArray.length()!=0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject( i );
                                    finalresult.setText( "Result:\n" + name + "\n"
                                            + "Pharisaical Company:\n"+mysinner.getSelectedItem().toString()+"\n\n"
                                            + "Ingredients :\n" +"SFDA:\n" + object.getString( "sfda" ) +"\n\n"
                                            + "Manufactures:\n" + object.getString( "man" ) +"\n\n"
                                            + "pharmaceutical companies:\n" + object.getString( "pharmac" ) +"\n\n"
                                            + "Authenticated" );
                                    AlertDialog.Builder builder;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        builder = new AlertDialog.Builder(ScanMedicineActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                    } else {
                                        builder = new AlertDialog.Builder(ScanMedicineActivity.this);
                                    }
                                    builder.setTitle("Result!")
                                            .setMessage("Result:\n" + name + "\n"
                                                    + "Pharisaical Company:\n"+mysinner.getSelectedItem().toString()+"\n\n"
                                                    + "Ingredients :\n" +"SFDA:\n" + object.getString( "sfda" ) +"\n\n"
                                                    + "Manufactures:\n" + object.getString( "man" ) +"\n\n"
                                                    + "pharmaceutical companies:\n" + object.getString( "pharmac" ) +"\n\n"
                                                    + "Authenticated" )
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
                            }else {
                                runOnUiThread( new Runnable() {
                                    @Override
                                    public void run() {
                                        finalresult.setText( "Result:\n" + name + "\n" + "Ingredients :\n" + "No Data Available" + "\n\n" + "Authenticated");
                                    }
                                } );
                            }
                            //insert into history table
                            insertToHistory();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ScanMedicineActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
//                Toast.makeText(ScanMedicineActivity.this,name,Toast.LENGTH_SHORT).show();
                hashMap.put("med",name);
                return hashMap;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
        alert.setEnabled( false );

    }

    private void insertToHistory() {
        StringRequest stringRequest = new StringRequest( Request.Method.POST, "https://secmedchain.000webhostapp.com/InsertToHistory.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText( ScanMedicineActivity.this,"Message: "+response,Toast.LENGTH_SHORT ).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ScanMedicineActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("email",get_email);
                hashMap.put("item",name);
                if (SearchForItem())
                    hashMap.put("state","Authenticated");
                else
                    hashMap.put("state","Not Authenticated");
                return hashMap;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }


    public boolean SearchForItem(){
        boolean res = false;
        for (int i = 0; i< Medicin.length; i++) {
            if (Medicin[i].equals( name )){
                res = true;
                return res;
            }
        }
        return res;
    }

    public Boolean validate(){
        boolean valid = true;
        if(name.isEmpty()){
            textView.setError( "Please enter Medicine Name" );
            valid = false;
        }
        return valid;
    }

    public void intialize() {
        name = textView.getText().toString().trim();
        pharmacy = mysinner.getSelectedItem().toString().trim();
    }
}
