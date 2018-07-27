package akhaled.shahen.com.exchangeme;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fci on 03/03/18.
 */

public class Profile extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    //
    List<History_profile_item> historyProfileItems;
    TextView name, phone, email, cap;
    String get_email;

    public static Profile newInstance(String name) {
        Bundle bundle = new Bundle();
        bundle.putString("email", name);

        Profile fragment = new Profile();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            get_email = bundle.getString("email");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.profile,container,false );
        //getActivity().getActionBar().setTitle("Profile");
        readBundle(getArguments());
        name = (TextView)view.findViewById( R.id.profile_name );
        email = (TextView)view.findViewById( R.id.profile_email );
        phone = (TextView)view.findViewById( R.id.profile_phone );
        cap = (TextView)view.findViewById( R.id.profile_capital );
        recyclerView = (RecyclerView) view.findViewById(R.id.profile_rec);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Switch_Nav) getActivity())
                .setActionBarTitle("Profile");
    }
    @Override
    public void onStart() {
        super.onStart();
        historyProfileItems = new ArrayList<>();
        if (isNetworkConnected()) {
            LoadProfileData(get_email);
            LoadRecyclerViewData(get_email);
        }else {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(getActivity());
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
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService( Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    private void LoadRecyclerViewData(final String get_email) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Data ...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest( Request.Method.POST, "https://programmerx.000webhostapp.com/ExchangeMe/GetHistoryProduct.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String s = URLEncoder.encode(response,"ISO-8859-1");
                            response = URLDecoder.decode(s,"UTF-8");
                        }catch (UnsupportedEncodingException e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("product_data");
                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                History_profile_item item = new History_profile_item(
                                        object.getString("photo_url"),
                                        object.getString("product_name"),
                                        object.getString("description"),
                                        object.getString("type"),
                                        object.getString("price"),
                                        object.getString("capital"),
                                        object.getString("phone"),
                                        object.getString("date")
                                );
                                historyProfileItems.add(item);
                            }
                            adapter = new Profile_history_Adapter(historyProfileItems,getActivity());
                            recyclerView.setAdapter(adapter);

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
                hashMap.put("email",get_email);
                return hashMap;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
    private void LoadProfileData(final String get_email) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Data ...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest( Request.Method.POST, "https://programmerx.000webhostapp.com/ExchangeMe/GetUserProfile.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String s = URLEncoder.encode(response,"ISO-8859-1");
                            response = URLDecoder.decode(s,"UTF-8");
                        }catch (UnsupportedEncodingException e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("user_data");
                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                        email.setText(object.getString("email"));
                                        name.setText(object.getString("user_name"));
                                        phone.setText(object.getString("phone"));
                                        cap.setText(object.getString("capital"));
                                }
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
                hashMap.put("email",get_email);
                return hashMap;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}
