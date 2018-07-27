package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by fci on 17/01/18.
 */

public class Make_money_noti extends Fragment {


    EditText cont;
    Button button;
    Bundle bundle;
    Activity context;
    byte[] paramtersbyt;
    String connectionparamters;
    String url;
    String getemail;
    String emailkey, contentkey, citykey;
    Cursor cursor;
    Database database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.make_money_noti,container,false);
        context = getActivity();
        database = new Database(context);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        button=(Button)getActivity().findViewById(R.id.moneypost);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                cont = (EditText)context.findViewById(R.id.moneycon);

                cursor = database.ShowData();
                if (cursor.getCount()==0)
                {
                    Toast.makeText(getActivity(),"No Email Found",Toast.LENGTH_SHORT).show();
                }else {

                    while (cursor.moveToNext()) {
                        getemail = cursor.getString(1);
                    }
                    url = "http://momenshaheen.16mb.com/InsertMoneyNotification.php";

                    emailkey = "email=";
                    contentkey = "&content=";
                    try {
                        connectionparamters = emailkey + URLEncoder.encode(getemail, "UTF-8") + contentkey
                                + URLEncoder.encode(cont.getText().toString(), "UTF-8");

                        paramtersbyt = connectionparamters.getBytes("UTF-8");
                        Toast.makeText(getActivity(), getemail+"\n"
                                +cont.getText().toString()+"\n"
                                , Toast.LENGTH_SHORT).show();
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
                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
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
            }

        });
    }
}
