package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Make_Notification1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    String text;
    Spinner spinner;
    FragmentTransaction transaction;
    Fragment newFragment;
    FragmentManager fragmentManager;
    byte[] paramtersbyt;
    String connectionparamters;
    String url;
    String getemail;
    String emailkey, contentkey, doctornamekey, reqstatekey, sponserkey;
    Bundle bundle;
    ImageView report;
    ConnectivityManager connManager;
    NetworkInfo mWifi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_noti1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        report = (ImageView)findViewById(R.id.add_report);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Make_Notification1.this,Send_report.class));
            }
        });

        spinner = (Spinner)findViewById(R.id.spinner1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Make_Notification1.this,Complain.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void onStart(){
        super.onStart();
        getemail = getIntent().getStringExtra("Email_login");
        Bundle extras = getIntent().getExtras();
        if (extras != null || getemail != null) {
            String value = getIntent().getStringExtra("Email_login");
            //Toast.makeText(Make_Notification1.this,value+"   "+getemail , Toast.LENGTH_LONG).show();
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text = spinner.getSelectedItem().toString();

                if (text.equals("Blood Notification")){
                    for(Fragment fragment:getSupportFragmentManager().getFragments()){

                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    }
                    fragmentManager = getSupportFragmentManager();
                    transaction = fragmentManager.beginTransaction();
                    newFragment = new Make_Blood_noti();
                    transaction.replace(R.id.noti_type,newFragment).commit();

                }else if (text.equals("Money Notification")){
                    for(Fragment fragment:getSupportFragmentManager().getFragments()){

                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    }
                    fragmentManager = getSupportFragmentManager();
                    transaction = fragmentManager.beginTransaction();
                    newFragment = new Make_money_noti();
                    transaction.replace(R.id.noti_type,newFragment).commit();

                }else if (text.equals("Points Notification")){
                    for(Fragment fragment:getSupportFragmentManager().getFragments()){

                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    }
                    fragmentManager = getSupportFragmentManager();
                    transaction = fragmentManager.beginTransaction();
                    newFragment = new Make_Point_noti();
                    transaction.replace(R.id.noti_type,newFragment).commit();

                }else {}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (getIntent().getStringExtra("Blood_DocName") != null) {
            Toast.makeText(Make_Notification1.this, getIntent().getStringExtra("Blood_DocName")
                    + getIntent().getStringExtra("Blood_Content") +
                    getIntent().getStringExtra("Blood_SelectedItem"), Toast.LENGTH_SHORT).show();

            Toast.makeText(Make_Notification1.this,getemail , Toast.LENGTH_LONG).show();
            connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connManager != null;
            mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (mWifi.isConnected()) {
            url = "http://momenshaheen.16mb.com/InsertBloodNotification.php";
            emailkey = "email=";
            contentkey = "&content=";
            doctornamekey = "&doctorname=";
            reqstatekey = "&reqstate=";
            try {
                connectionparamters = emailkey + URLEncoder.encode("momen@g.com", "UTF-8") + contentkey
                        + URLEncoder.encode(getIntent().getStringExtra("Blood_Content"), "UTF-8") +
                        doctornamekey + URLEncoder.encode(getIntent().getStringExtra("Blood_DocName"),
                        "UTF-8") + reqstatekey + URLEncoder.encode(getIntent().getStringExtra("Blood_SelectedItem"), "UTF-8");

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
                                Toast.makeText(Make_Notification1.this, result, Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            Thread thread = new Thread(runnable);
            thread.start();
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
        }else if (getIntent().getStringExtra("Money_Content") != null) {
            Toast.makeText(Make_Notification1.this,getIntent().getStringExtra("Money_Content")
                    , Toast.LENGTH_SHORT).show();

            connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connManager != null;
            mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (mWifi.isConnected()) {
            url = "http://momenshaheen.16mb.com/InsertMoneyNotification.php";
            emailkey = "email=";
            contentkey = "&content=";
            try {
                connectionparamters = emailkey + URLEncoder.encode(getemail, "UTF-8") + contentkey
                        + URLEncoder.encode(getIntent().getStringExtra("Money_Content"), "UTF-8");
                paramtersbyt = connectionparamters.getBytes("UTF-8");

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
                                Toast.makeText(Make_Notification1.this, result, Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            Thread thread = new Thread(runnable);
            thread.start();
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
        }else if (getIntent().getStringExtra("Point_Sponser") != null) {
            Toast.makeText(Make_Notification1.this,getIntent().getStringExtra("Point_Sponser")
                            +getIntent().getStringExtra("Point_Content")
                    , Toast.LENGTH_SHORT).show();

            connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connManager != null;
            mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (mWifi.isConnected()) {
            url = "http://momenshaheen.16mb.com/InsertPointNotification.php";
            emailkey = "email=";
            sponserkey = "&sponser=";
            contentkey = "&content=";
            try {
                connectionparamters = emailkey + URLEncoder.encode(getemail, "UTF-8")+
                        sponserkey + URLEncoder.encode(getIntent().getStringExtra("Point_Sponser"), "UTF-8") + contentkey
                        + URLEncoder.encode(getIntent().getStringExtra("Point_Content"), "UTF-8");
                paramtersbyt = connectionparamters.getBytes("UTF-8");

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
                                Toast.makeText(Make_Notification1.this, result, Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            Thread thread = new Thread(runnable);
            thread.start();
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(Make_Notification1.this,Settings_activity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startActivity(new Intent(Make_Notification1.this,Home.class));
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(Make_Notification1.this,Profile.class));
        } else if (id == R.id.make_notification) {

        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(Make_Notification1.this,Reports.class));
        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(Make_Notification1.this,About.class));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            startActivity(new Intent(Make_Notification1.this,MainActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
