package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by fci on 14/01/18.
 */

public class Sign_up extends AppCompatActivity {
    String text;
    Spinner spinner;
    FragmentTransaction transaction;
    Fragment newFragment;
    FragmentManager fragmentManager;
    Bundle bundle;
    String email, pass, name, city, age,lat,lang;
    byte[] paramtersbyt;
    String connectionparamters;
    String url;
    String getemail;
    String emailkey, namekey, citynamekey, passkey, agekey;
    ConnectivityManager connManager;
    NetworkInfo mWifi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        spinner = (Spinner)findViewById(R.id.spinner1);

        bundle = getIntent().getExtras();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text = spinner.getSelectedItem().toString();

                if (text.equals("Hospital")){
                    fragmentManager = getSupportFragmentManager();
                    transaction = fragmentManager.beginTransaction();
                    newFragment = new Hosp_frag();
                    transaction.replace(R.id.frag_don_hosp,newFragment).commit();

                }else if (text.equals("Donner")){
                    fragmentManager = getSupportFragmentManager();
                    transaction = fragmentManager.beginTransaction();
                    newFragment = new donner_frag();
                    transaction.replace(R.id.frag_don_hosp,newFragment).commit();

                }else {}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        bundle = getIntent().getExtras();
        if (getIntent().getStringExtra("Hosp_name") != null){
            /*
            url = "http://momenshaheen.16mb.com/InsertNewHospital.php";
            namekey = "name=";
            citynamekey = "&city_name=";
            emailkey = "&email=";
            passkey = "&password=";
            lat = "&lat=";
            lang = "&lan=";
            try {
                connectionparamters = namekey + URLEncoder.encode("Hosp_name", "UTF-8") + citynamekey
                        + URLEncoder.encode(getIntent().getStringExtra("Hosp_city"), "UTF-8") +
                        emailkey + URLEncoder.encode(getIntent().getStringExtra("Hosp_email"),
                        "UTF-8") + passkey + URLEncoder.encode(getIntent().getStringExtra("Hosp_pass"), "UTF-8")
                        + lat + URLEncoder.encode(getIntent().getStringExtra("Hosp_lat"), "UTF-8")
                        + lang + URLEncoder.encode(getIntent().getStringExtra("Hosp_lan"), "UTF-8");

                paramtersbyt = connectionparamters.getBytes("UTF-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Runnable runnable = new Runnable() {
                public void run() {
                    try {

                        URL insertUserUrl = new URL(url);
                        HttpURLConnection insertConnection = (HttpURLConnection) insertUserUrl.openConnection();
                        insertConnection.setRequestMethod("POST");
                        insertConnection.getOutputStream().write(paramtersbyt);
                        InputStreamReader resultStreamReader = new InputStreamReader(insertConnection.getInputStream());
                        BufferedReader resultReader = new BufferedReader(resultStreamReader);
                        final String result = resultReader.readLine();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Sign_up.this, result, Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            Thread thread = new Thread(runnable);
            thread.start();
            */
        }else if (getIntent().getStringExtra("Don_name") != null) {
            connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connManager != null;
            mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (mWifi.isConnected()) {
                url = "http://momenshaheen.16mb.com/InsertNewUser.php";
                namekey = "name=";
                agekey = "&age=";
                emailkey = "&email=";
                passkey = "&password=";
                lat = "&lat=";
                lang = "&lan=";
                try {
                    connectionparamters = emailkey + URLEncoder.encode("Don_email", "UTF-8") + passkey
                            + URLEncoder.encode(getIntent().getStringExtra("Don_pass"), "UTF-8") +
                            namekey + URLEncoder.encode(getIntent().getStringExtra("Don_name"),
                            "UTF-8") + agekey + URLEncoder.encode(getIntent().getStringExtra("Don_age"), "UTF-8")
                            + lat + URLEncoder.encode(getIntent().getStringExtra("Don_lat"), "UTF-8")
                            + lang + URLEncoder.encode(getIntent().getStringExtra("Don_lan"), "UTF-8");

                    paramtersbyt = connectionparamters.getBytes("UTF-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Runnable runnable = new Runnable() {
                    public void run() {
                        try {

                            URL insertUserUrl = new URL(url);
                            HttpURLConnection insertConnection = (HttpURLConnection) insertUserUrl.openConnection();
                            insertConnection.setRequestMethod("POST");
                            insertConnection.getOutputStream().write(paramtersbyt);
                            InputStreamReader resultStreamReader = new InputStreamReader(insertConnection.getInputStream());
                            BufferedReader resultReader = new BufferedReader(resultStreamReader);
                            final String result = resultReader.readLine();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Sign_up.this, result, Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Thread thread = new Thread(runnable);
                thread.start();

            }
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
}
