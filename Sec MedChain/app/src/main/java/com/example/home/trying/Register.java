package com.example.home.trying;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

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
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

public class Register extends AppCompatActivity {

    EditText etName;
    EditText etEmail;
    EditText etPassword;
    EditText etConfirm;
    Button btRegister;
    ImageView pic;
    Button impor;

    Bitmap FixBitmap;

    String ServerUploadPath ="https://secmedchain.000webhostapp.com/Register.php" ;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;

    ByteArrayOutputStream byteArrayOutputStream ;

    String username;
    String email;
    String password;
    String emailPattern1 = "[a-z0-9._-]+@[a-z]+.[a-z]+";
    String emailPattern2 = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    String emailPattern3 = "^[\\\\w\\\\.-]+@([\\\\w\\\\-]+\\\\.)+[A-Z]{2,4}$";
    String emailPattern4 = "[a-zA-Z0-9._-]+@[a-z]+\\\\.+[a-z]+";
    boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        requestStoragePermission();
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirm = (EditText) findViewById(R.id.etConfirm);
        btRegister = (Button) findViewById(R.id.btRegister);
        impor = (Button) findViewById(R.id.button2);
        pic = (ImageView)findViewById( R.id.imageView3 );
        email = etEmail.getText().toString().trim();
        byteArrayOutputStream = new ByteArrayOutputStream();

        impor.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        } );

        etEmail.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if ((email.matches(emailPattern1) || android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                ||email.matches(emailPattern2) || email.matches(emailPattern3) || email.matches(emailPattern4)) && s.length() > 0)
                {
                    valid = true;
                }else
                {
                    etEmail.setError( "Invalid Email" );
                    valid = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etName.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                String cpassword = etConfirm.getText().toString();

                if (etName.getText().length()<=2){
                    etName.setError( "Please write a valid Name" );
                    valid = false;
                }
                if (password.equals( cpassword ) && password.length()>=6) {
                    if (email.isEmpty()){
                        etEmail.setError( "Please Fill Email" );
                    }else {

                            //getting the actual path of the image
                            String path = getPath(filePath);

                            //Uploading code
                            try {
                                String uploadId = UUID.randomUUID().toString();

                                //Creating a multi part request
                                new MultipartUploadRequest(Register.this, uploadId, ServerUploadPath)
                                        .addFileToUpload(path, "pic") //Adding file
                                        .addParameter("user_name", username) //Adding text parameter to the request
                                        .addParameter("email", email) //Adding text parameter to the request
                                        .addParameter("password", password) //Adding text parameter to the request
                                        .setNotificationConfig(new UploadNotificationConfig())
                                        .setMaxRetries(2)
                                        .startUpload(); //Starting the upload

                            } catch (Exception exc) {
                                Toast.makeText(Register.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            startActivity( new Intent( Register.this,MainActivity.class ) );
                            finish();
                        }

                } else {
                    etConfirm.setError( "Password Doesn't Match or Less than 6 Letters" );
                }
            }
        });
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                pic.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
}
