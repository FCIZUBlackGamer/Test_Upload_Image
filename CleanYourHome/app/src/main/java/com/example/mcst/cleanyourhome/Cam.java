package com.example.mcst.cleanyourhome;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Cam extends AppCompatActivity {
    ImageView image;
    EditText description;
    Button send;
    String get_description;
// For GPS
private static final int REQUEST_LOCATION = 1;

    private boolean gps_enabled=false;
    private boolean network_enabled=false;
    Location location;

    float MyLat, MyLong;

    // For Image Uploading
    Bitmap FixBitmap;

    String Message_lat = "Message_lat" ;

    String Image_Report_Data = "Image_Data" ;

    String Message_lon = "Message_lon" ;

    String Message_desc = "Message_comment" ;

    String ServerUploadPath ="https://programmerx.000webhostapp.com/InsertNewMessage.php" ;

    ProgressDialog progressDialog ;

    ByteArrayOutputStream byteArrayOutputStream ;

    byte[] byteArray ;

    String ConvertImage ;

    HttpURLConnection httpURLConnection ;

    URL url;

    OutputStream outputStream;

    BufferedWriter bufferedWriter ;

    int RC ;

    BufferedReader bufferedReader ;

    StringBuilder stringBuilder;

    boolean check = true;

    HashMap<String,String > stringStringHashMap;
    Database database;
    float total = 0;
    Cursor cursor;
    String user_email = "";
    float points = 0;
    EditText brass, almunim, copper, tyre, polythene, cans, paper, plastic, other;
    CheckBox cbrass, calmunim, ccopper, ctyre, cpolythene, ccans, cpaper, cplastic, cother;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_cam);
        image = (ImageView)findViewById( R.id.imagecapture );
        description = (EditText)findViewById( R.id.junk_desc );
        send = (Button)findViewById( R.id.send_message );
        database = new Database( this );
        stringStringHashMap = new HashMap<>();
        cursor = database.ShowData();

        while (cursor.moveToNext()){
            user_email = cursor.getString( 1 );
        }

        StringRequest stringRequest = new StringRequest( Request.Method.POST, "https://programmerx.000webhostapp.com/GetPoints.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String s = URLEncoder.encode( response, "ISO-8859-1" );
                            response = URLDecoder.decode( s, "UTF-8" );
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONObject jsonObject = new JSONObject( response );

                            points = Float.parseFloat(jsonObject.getString( "points" ));
                            database.UpdateData( "1",points+"" );

                        }catch (JSONException e){
                            Toast.makeText( Cam.this, "Not JSON Format", Toast.LENGTH_SHORT ).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
                Toast.makeText(Cam.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("email",user_email);
                return hashMap;
            }
        };
        Volley.newRequestQueue(Cam.this).add(stringRequest);

        brass = findViewById( R.id.brass_amount );
        cbrass = findViewById( R.id.brass_check );

        almunim = findViewById( R.id.almunim_amount );
        calmunim = findViewById( R.id.almunim_check );

        copper = findViewById( R.id.copper_amount );
        ccopper = findViewById( R.id.copper_check );

        tyre = findViewById( R.id.tyre_amount );
        ctyre = findViewById( R.id.tyre_check );

        polythene = findViewById( R.id.polythene_amount );
        cpolythene = findViewById( R.id.polythene_check );

        cans = findViewById( R.id.can_amount );
        ccans = findViewById( R.id.can_check );

        paper = findViewById( R.id.paper_amount );
        cpaper = findViewById( R.id.paper_check );

        plastic = findViewById( R.id.plastic_amount );
        cplastic = findViewById( R.id.plastic_check );

        other = findViewById( R.id.other_amount );
        cother = findViewById( R.id.other_check );

        getMyCurrentLocation();

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique

            return;
        }

        byteArrayOutputStream = new ByteArrayOutputStream();

        image.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {


                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);

            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText( Cam.this,user_email,Toast.LENGTH_SHORT ).show();
                if (ccans.isChecked())
                    stringStringHashMap.put( "cat1","can" );

                if (ccopper.isChecked())
                    stringStringHashMap.put( "cat2","copper" );

                if (cbrass.isChecked())
                    stringStringHashMap.put( "cat3","brass" );

                if (calmunim.isChecked())
                    stringStringHashMap.put( "cat4","almunim" );

                if (ctyre.isChecked())
                    stringStringHashMap.put( "cat5","tyre" );

                if (cpolythene.isChecked())
                    stringStringHashMap.put( "cat6","polythene" );

                if (cpaper.isChecked())
                    stringStringHashMap.put( "cat7","paper" );

                if (cplastic.isChecked())
                    stringStringHashMap.put( "cat8","plastic" );

                if (cother.isChecked())
                    stringStringHashMap.put( "cat9","other" );

                if (!cans.getText().toString().isEmpty()) {
                    total += 0.5 * Float.parseFloat( cans.getText().toString() );
                    Toast.makeText( Cam.this, total+"",Toast.LENGTH_SHORT).show();
                }

                if (!paper.getText().toString().isEmpty())
                    total += 0.5 * Float.parseFloat(paper.getText().toString());

                if (!plastic.getText().toString().isEmpty())
                    total += 0.5 * Float.parseFloat(plastic.getText().toString());

                if (!copper.getText().toString().isEmpty())
                    total += 0.5 * Float.parseFloat(copper.getText().toString());

                if (!other.getText().toString().isEmpty())
                    total += 0.5 * Float.parseFloat(other.getText().toString());

                if (!tyre.getText().toString().isEmpty())
                    total += 0.5 * Float.parseFloat(tyre.getText().toString());

                if (!almunim.getText().toString().isEmpty())
                    total += 0.5 * Float.parseFloat(almunim.getText().toString());

                if (!brass.getText().toString().isEmpty())
                    total += 0.5 * Float.parseFloat(brass.getText().toString());

                if (!polythene.getText().toString().isEmpty())
                    total += 0.5 * Float.parseFloat(polythene.getText().toString());

                get_description = description.getText().toString();

                if (!String.valueOf( MyLat ).isEmpty()&& !String.valueOf( MyLong ).isEmpty()) {
                    UploadImageToServer(String.valueOf( MyLat ),String.valueOf( MyLong ));

                    image.setImageResource(R.drawable.camera_carton);
                    description.setText("");
                }else {
                    getMyCurrentLocation();
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.switch__nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        cursor = database.ShowData();
        String point = "";
        while (cursor.moveToNext()){
            point = cursor.getString( 3 );
        }
        switch (item.getItemId())
        {
            case R.id.logout_user:
                Database database;
                database = new Database( Cam.this);
                Intent intent = new Intent( Cam.this,LoginActivity.class );
                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK );
                database.UpdateData( "1","0","0" ,"0");
                startActivity( intent );
                return true;
            case R.id.points:
                final AlertDialog.Builder builder = new AlertDialog.Builder( Cam.this );
                builder.setTitle( "Points" )
                        .setMessage( "Your Points: "+point )
                        .setNegativeButton( "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        } ).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int RC, int RQC, Intent I) {

        super.onActivityResult(RC, RQC, I);

        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                FixBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                image.setImageBitmap(FixBitmap);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public void UploadImageToServer(final String lat, final String lon){

        FixBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byteArray = byteArrayOutputStream.toByteArray();

        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(Cam.this,"Message is Uploading","Please Wait ... ",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                progressDialog.dismiss();

                if(string1.contains( "Your Message Has Been Sent!" )) {
                    Toast.makeText(Cam.this,"Your Message Has Been Sent!",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Cam.this,string1,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put(Message_lat, lat);

                HashMapParams.put(Message_lon, lon);

                HashMapParams.put("points", total+"");

                HashMapParams.put("email", user_email);

                HashMapParams.put(Message_desc, get_description);

                HashMapParams.put(Image_Report_Data, ConvertImage);

                HashMapParams.putAll( stringStringHashMap );

                String FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, HashMapParams);


                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {
                url = new URL(requestURL);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(20000);

                httpURLConnection.setConnectTimeout(20000);

                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoInput(true);

                httpURLConnection.setDoOutput(true);

                outputStream = httpURLConnection.getOutputStream();

                bufferedWriter = new BufferedWriter(

                        new OutputStreamWriter(outputStream, "UTF-8"));

                bufferedWriter.write(bufferedWriterDataFN(PData));

                bufferedWriter.flush();

                bufferedWriter.close();

                outputStream.close();

                RC = httpURLConnection.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReader.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            stringBuilder = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilder.append("&");

                stringBuilder.append( URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilder.append("=");

                stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilder.toString();
        }

    }

    @SuppressLint("MissingPermission")
    void getMyCurrentLocation() {

        if (ActivityCompat.checkSelfPermission( Cam.this, android.Manifest.permission.ACCESS_FINE_LOCATION )
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                ( Cam.this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions( Cam.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION );

        } else {
            LocationManager locManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
            LocationListener locListener = new MyLocationListener();


            try {
                gps_enabled = locManager.isProviderEnabled( LocationManager.GPS_PROVIDER );
            } catch (Exception ex) {
            }
            try {
                network_enabled = locManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER );
            } catch (Exception ex) {
            }

            //don't start listeners if no provider is enabled
            //if(!gps_enabled && !network_enabled)
            //return false;

            if (gps_enabled) {
                locManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, locListener );

            }


            if (gps_enabled) {
                location = locManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );


            }


            if (network_enabled && location == null) {
                locManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, locListener );

            }


            if (network_enabled && location == null) {
                location = locManager.getLastKnownLocation( LocationManager.NETWORK_PROVIDER );

            }

            if (location != null) {

                MyLat = (float)location.getLatitude();
                MyLong = (float)location.getLongitude();


            } else {
                Location loc = getLastKnownLocation( this );
                if (loc != null) {

                    MyLat = (float) loc.getLatitude();
                    MyLong = (float)loc.getLongitude();


                }
            }
            locManager.removeUpdates( locListener ); // removes the periodic updates from location listener to //avoid battery drainage. If you want to get location at the periodic intervals call this method using //pending intent.

            try {
// Getting address from found locations.
                //Geocoder geocoder;

                //List<Address> addresses;
                //geocoder = new Geocoder( this, Locale.getDefault() );
                //addresses = geocoder.getFromLocation( MyLat, MyLong, 1 );

//                StateName = addresses.get( 0 ).getAdminArea();
//                CityName = addresses.get( 0 ).getLocality();
//                CountryName = addresses.get( 0 ).getCountryName();
                // you can get more details other than this . like country code, state code, etc.


          /*  Toast.makeText(this," StateName " + StateName+"\n"+
                    " CityName " + CityName+"\n"+
                    " CountryName " + CountryName,Toast.LENGTH_LONG).show();
                    */
            } catch (Exception e) {
                e.printStackTrace();
            }

//            Toast.makeText( this, "" + MyLat + "\n" +
//                    MyLong ,Toast.LENGTH_LONG ).show();
        }
    }

    // Location listener class. to get location.
    public class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            if (location != null) {
            }
        }

        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }

// below method to get the last remembered location. because we don't get locations all the times .At some instances we are unable to get the location from GPS. so at that moment it will show us the last stored location.

    public static Location getLastKnownLocation(Context context)
    {
        Location location = null;
        @SuppressLint("WrongConstant") LocationManager locationmanager = (LocationManager)context.getSystemService("location");
        List list = locationmanager.getAllProviders();
        boolean i = false;
        Iterator iterator = list.iterator();
        do
        {
            //System.out.println("---------------------------------------------------------------------");
            if(!iterator.hasNext())
                break;
            String s = (String)iterator.next();
            //if(i != 0 && !locationmanager.isProviderEnabled(s))
            if(i != false && !locationmanager.isProviderEnabled(s))
                continue;
            // System.out.println("provider ===> "+s);
            @SuppressLint("MissingPermission") Location location1 = locationmanager.getLastKnownLocation(s);
            if(location1 == null)
                continue;
            if(location != null)
            {
                //System.out.println("location ===> "+location);
                //System.out.println("location1 ===> "+location);
                float f = location.getAccuracy();
                float f1 = location1.getAccuracy();
                if(f >= f1)
                {
                    long l = location1.getTime();
                    long l1 = location.getTime();
                    if(l - l1 <= 600000L)
                        continue;
                }
            }
            location = location1;
            // System.out.println("location  out ===> "+location);
            //System.out.println("location1 out===> "+location);
            i = locationmanager.isProviderEnabled(s);
            // System.out.println("---------------------------------------------------------------------");
        } while(true);
        return location;
    }
    //    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
//    public static final String ALLOW_KEY = "ALLOWED";
//    public static final String CAMERA_PREF = "camera_pref";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cam);
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            if (getFromPref(this, ALLOW_KEY)) {
//                showSettingsAlert();
//            } else if (ContextCompat.checkSelfPermission(this,
//                    Manifest.permission.CAMERA)
//
//                    != PackageManager.PERMISSION_GRANTED) {
//
//                // Should we show an explanation?
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                        Manifest.permission.CAMERA)) {
//                    showAlert();
//                } else {
//                    // No explanation needed, we can request the permission.
//                    ActivityCompat.requestPermissions(this,
//                            new String[]{Manifest.permission.CAMERA},
//                            MY_PERMISSIONS_REQUEST_CAMERA);
//                }
//            }
//        } else {
//            openCamera();
//        }
//
//    }
//    public static void saveToPreferences(Context context, String key, Boolean allowed) {
//        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF,
//                Context.MODE_PRIVATE);
//        SharedPreferences.Editor prefsEditor = myPrefs.edit();
//        prefsEditor.putBoolean(key, allowed);
//        prefsEditor.commit();
//    }
//
//    public static Boolean getFromPref(Context context, String key) {
//        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF,
//                Context.MODE_PRIVATE);
//        return (myPrefs.getBoolean(key, false));
//    }
//
//    private void showAlert() {
//        AlertDialog alertDialog = new AlertDialog.Builder(Cam.this).create();
//        alertDialog.setTitle("Alert");
//        alertDialog.setMessage("App needs to access the camera_carton.");
//
//        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        finish();
//                    }
//                });
//
//        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
//                new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        ActivityCompat.requestPermissions(Cam.this,
//                                new String[]{Manifest.permission.CAMERA},
//                                MY_PERMISSIONS_REQUEST_CAMERA);
//                    }
//                });
//        alertDialog.show();
//    }
//
//    private void showSettingsAlert() {
//        AlertDialog alertDialog = new AlertDialog.Builder(Cam.this).create();
//        alertDialog.setTitle("Alert");
//        alertDialog.setMessage("App needs to access the camera_carton.");
//
//        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
//                new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        //finish();
//                    }
//                });
//
//        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
//                new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        startInstalledAppDetailsActivity(Cam.this);
//                    }
//                });
//
//        alertDialog.show();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_CAMERA: {
//                for (int i = 0, len = permissions.length; i < len; i++) {
//                    String permission = permissions[i];
//
//                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
//                        boolean
//                                showRationale =
//                                ActivityCompat.shouldShowRequestPermissionRationale(
//                                        this, permission);
//
//                        if (showRationale) {
//                            showAlert();
//                        } else if (!showRationale) {
//                            // user denied flagging NEVER ASK AGAIN
//                            // you can either enable some fall back,
//                            // disable features of your app
//                            // or open another dialog explaining
//                            // again the permission and directing to
//                            // the app setting
//                            saveToPreferences(Cam.this, ALLOW_KEY, true);
//                        }
//                    }
//                }
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//    public static void startInstalledAppDetailsActivity(final Activity context) {
//        if (context == null) {
//            return;
//        }
//
//        final Intent i = new Intent();
//        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        i.addCategory(Intent.CATEGORY_DEFAULT);
//        i.setData(Uri.parse("package:" + context.getPackageName()));
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//        context.startActivity(i);
//    }
//
//    private void openCamera() {
//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        startActivity(intent);
//
//    }
}


