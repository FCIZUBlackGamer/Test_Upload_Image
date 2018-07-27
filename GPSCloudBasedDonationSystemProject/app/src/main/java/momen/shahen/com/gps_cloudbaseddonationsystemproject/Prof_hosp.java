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

public class Prof_hosp extends Fragment {

    Database database;
    Cursor cursor;
    String getemail;
    EditText name,email, city_name;
    Profile_hosp_data item;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        database = new Database(getActivity());
        View view = inflater.inflate(R.layout.prof_hosp,container,false);
        name = view.findViewById(R.id.prof_hosp_name);
        email = view.findViewById(R.id.prof_hosp_email);
        city_name = view.findViewById(R.id.prof_hosp_city_name);
        return view;
    }
    private void LoadRecyclerViewData() {
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
                                item = new Profile_hosp_data(
                                        object.getString("name"),
                                        object.getString("Email"),
                                        object.getString("CityName")
                                );

                            }
                            name.setText(item.getName());
                            email.setText(item.getEmail());
                            city_name.setText(item.getCity_name());

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
