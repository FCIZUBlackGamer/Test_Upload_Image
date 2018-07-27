package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fci on 31/01/18.
 */

public class Prof_don extends Fragment {
    //GetProfileDonData.php
    Database database;
    Cursor cursor;
    String getemail;
    EditText name, age, last_don, point,email;
    Profile_don_data item;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        database = new Database(getActivity());
        View view = inflater.inflate(R.layout.prfo_doner,container,false);
        name = view.findViewById(R.id.prof_don_name);
        age = view.findViewById(R.id.prof_don_age);
        last_don = view.findViewById(R.id.prof_don_last_don);
        point = view.findViewById(R.id.prof_don_point);
        email = view.findViewById(R.id.prof_don_email);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        LoadData();

    }
    private void LoadData() {
        cursor = database.ShowData();

        if (cursor.getCount()==0)
        {
            Toast.makeText(getActivity(),"No Privileges to Send",Toast.LENGTH_SHORT).show();

        }
        else {
            while (cursor.moveToNext()) {
                getemail = cursor.getString(1);
            }
        }
        Toast.makeText(getActivity(),getemail,Toast.LENGTH_LONG).show();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Data ...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://momenshaheen.16mb.com/GetDonarData.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String s = URLEncoder.encode(response,"ISO-8859-1");
                            response = URLDecoder.decode(response,"UTF-8");
                        }catch (UnsupportedEncodingException e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("don_data");
                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                item = new Profile_don_data(
                                        object.getString("name"),
                                        object.getString("Age"),
                                        object.getString("Last_Donation"),
                                        object.getString("Email"),
                                        object.getString("points")
                                );

                            }
                            name.setText(item.getName());
                            age.setText(item.getAge());
                            last_don.setText(item.getLast_don());
                            email.setText(item.getEmail());
                            point.setText(item.getPoints());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("email",getemail);
                return hashMap;
            }
        }
                ;

        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}
