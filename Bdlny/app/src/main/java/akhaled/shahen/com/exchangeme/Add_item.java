package akhaled.shahen.com.exchangeme;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by fci on 03/03/18.
 */

public class Add_item extends Activity {
    ImageView image;
    EditText name, price, description,phone;
    Spinner dep, cap;
    Button send;
    String get_dep, get_name, get_price, get_description,get_phone, get_cap;
    /////
    Bitmap FixBitmap;

    String Product_name = "Product_name" ;

    String Image_Data = "image" ;

    String Product_price = "Product_price" ;

    String Product_disc = "Product_disc" ;

    String Product_dep = "Product_dep" ;

    String Product_capital = "Product_capital" ;

    String Product_phone = "Product_phone" ;

    String user_email = "user_email" ;

    Intent intent;
////////
    String ServerUploadPath ="https://programmerx.000webhostapp.com/ExchangeMe/InsertNewProduct.php" ;

    ProgressDialog progressDialog ;


    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;
    String get_email;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.add_item );
        //Requesting storage permission function
        requestStoragePermission();
        name = (EditText)findViewById( R.id.et_tool_name );
        phone = (EditText)findViewById( R.id.et_tool_phone );
        price = (EditText)findViewById( R.id.et_tool_price );
        description = findViewById( R.id.et_tool_describe );
        image = findViewById( R.id.image1_tool );
        dep = findViewById( R.id.spinner_dept );
        cap = findViewById( R.id.spinner_cap );
        send = findViewById( R.id.btn_ADD_Tool );

        intent = getIntent();

        get_email = intent.getStringExtra( "email" );


//        byteArrayOutputStream = new ByteArrayOutputStream();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                get_description = description.getText().toString();
                get_name = name.getText().toString();
                get_phone = phone.getText().toString();
                get_price = price.getText().toString();
                get_cap = cap.getSelectedItem().toString();
                get_dep = dep.getSelectedItem().toString();

                if (!(get_dep.isEmpty() &&
                        get_cap.isEmpty() &&
                        get_price.isEmpty() &&
                        get_phone.isEmpty() &&
                        get_name.isEmpty() &&
                        get_description.isEmpty() &&
                        get_email.isEmpty())) {

                    //getting the actual path of the image
                    String path = getPath(filePath);

                    //Uploading code
                    try {
                        String uploadId = UUID.randomUUID().toString();

                        //Creating a multi part request
                        new MultipartUploadRequest(Add_item.this, uploadId, ServerUploadPath)
                                .addFileToUpload(path, "image") //Adding file
                                .addParameter(Product_name, get_name) //Adding text parameter to the request
                                .addParameter(Product_capital, get_cap) //Adding text parameter to the request
                                .addParameter(Product_dep, get_dep) //Adding text parameter to the request
                                .addParameter(Product_disc, get_description) //Adding text parameter to the request
                                .addParameter(Product_phone, get_phone) //Adding text parameter to the request
                                .addParameter(Product_price, get_price) //Adding text parameter to the request
                                .addParameter(user_email, get_email) //Adding text parameter to the request
                                .setNotificationConfig(new UploadNotificationConfig())
                                .setMaxRetries(2)
                                .startUpload(); //Starting the upload

                    } catch (Exception exc) {
                        Toast.makeText(Add_item.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                image.setImageResource( R.mipmap.loading_image );
                description.setText( "" );
                name.setText("");
                phone.setText("");


                }};
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
                image.setImageBitmap(bitmap);

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
