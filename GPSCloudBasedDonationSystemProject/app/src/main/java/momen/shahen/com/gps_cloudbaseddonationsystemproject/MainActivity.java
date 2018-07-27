package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {


    Button donn, hosp, signup;
    EditText email, pass;
    Database database;
    Cursor cursor;
    boolean b = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        database = new Database(this);

        email = (EditText)findViewById(R.id.loginemail);
        pass = (EditText)findViewById(R.id.loginpass);
        database.InsertData(" ", " "," ");
        cursor = database.ShowData();
        if (cursor.getCount()==0)
        {
            database.InsertData(" ", " "," ");
        }
        else if (cursor.getCount()>1){
            for (int i=2;i<cursor.getCount();i++) {
                database.DeleteData(i+"");
            }
        }
        donn = (Button)findViewById(R.id.donne);
        donn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MainActivity.this,Home.class);
                b = database.UpdateData("1",email.getText().toString(), pass.getText().toString(),"DONNER");
                if (b)
                {
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Failed Login",Toast.LENGTH_SHORT).show();
                }

            }
        });
        hosp = (Button)findViewById(R.id.hos);
        hosp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MainActivity.this,Home.class);
                b = database.UpdateData("1",email.getText().toString(), pass.getText().toString(),"HOSPITAL");
                if (b)
                {
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Failed Login",Toast.LENGTH_SHORT).show();
                }

            }
        });
        signup = (Button)findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Sign_up.class));
            }
        });
    }
}
