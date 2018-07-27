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
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fci on 03/03/18.
 */

public class Home extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Home_item> homeItems;
    Button search;
    EditText search_field;
    String search_word;
    String type;

    public static Home newInstance(String name) {
        Bundle bundle = new Bundle();
        bundle.putString("name", name);

        Home fragment = new Home();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            type = bundle.getString("name");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.home,container,false );
        search = view.findViewById( R.id.search_home );
        readBundle(getArguments());
        //getActivity().getActionBar().setTitle("Home");
        search_field = view.findViewById( R.id.home_search_filed );
        recyclerView = (RecyclerView) view.findViewById(R.id.home_rec);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        ((Switch_Nav) getActivity())
                .setActionBarTitle("Home");
    }

    @Override
    public void onStart() {
        super.onStart();
        homeItems = new ArrayList<>();
        //type = null;
        if (isNetworkConnected()) {

            if (type ==null ){
                LoadRecyclerViewData();
            }else {
                Toast.makeText( getActivity(),type,Toast.LENGTH_SHORT ).show();
                LoadRecyclerViewDepartmentItems( type ); //GetProductWithDepartment.php
                type = null;
            }

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
        search.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_word = search_field.getText().toString();
                if (isNetworkConnected()) {
                    LoadRecyclerViewSearchItems(search_word);
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
        } );
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService( Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    private void LoadRecyclerViewSearchItems(final String word) {
        final int size = homeItems.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                homeItems.remove(0);
            }
            adapter.notifyItemRangeRemoved(0, size);
        }
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Data ...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest( Request.Method.POST, "https://programmerx.000webhostapp.com/ExchangeMe/GetProductWithSearch.php",
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
                            if (response.isEmpty()){
                                Toast.makeText( getActivity(),"No Items",Toast.LENGTH_SHORT ).show();
                            }else {
                                JSONObject jsonObject = new JSONObject( response );
                                JSONArray jsonArray = jsonObject.getJSONArray( "product_data" );
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject( i );
                                    Home_item item = new Home_item(
                                            object.getString( "photo_url" ),
                                            object.getString( "product_name" ),
                                            object.getString( "description" ),
                                            object.getString( "user_email" ),
                                            object.getString( "type" ),
                                            object.getString( "price" ),
                                            object.getString( "capital" ),
                                            object.getString( "phone" ),
                                            object.getString( "date" )
                                    );
                                    homeItems.add( item );
                                }
                                adapter = new Home_Adapter( homeItems, getActivity() );
                                recyclerView.setAdapter( adapter );
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
                hashMap.put("word",word);
                return hashMap;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private void LoadRecyclerViewDepartmentItems(final String type) {
        final int size = homeItems.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                homeItems.remove(0);
            }
            adapter.notifyItemRangeRemoved(0, size);
        }
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Data ...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest( Request.Method.POST, "https://programmerx.000webhostapp.com/ExchangeMe/GetProductWithDepartment.php",
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
                            if (response.isEmpty()){
                                Toast.makeText( getActivity(),"No Items",Toast.LENGTH_SHORT ).show();
                            }else {
                                JSONObject jsonObject = new JSONObject( response );
                                JSONArray jsonArray = jsonObject.getJSONArray( "product_data" );
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject( i );
                                    Home_item item = new Home_item(
                                            object.getString( "photo_url" ),
                                            object.getString( "product_name" ),
                                            object.getString( "description" ),
                                            object.getString( "user_email" ),
                                            object.getString( "type" ),
                                            object.getString( "price" ),
                                            object.getString( "capital" ),
                                            object.getString( "phone" ),
                                            object.getString( "date" )
                                    );
                                    homeItems.add( item );
                                }
                                adapter = new Home_Adapter( homeItems, getActivity() );
                                recyclerView.setAdapter( adapter );
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
                hashMap.put("type",type);
                return hashMap;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private void LoadRecyclerViewData() {

        final int size = homeItems.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                homeItems.remove(0);
            }
            adapter.notifyItemRangeRemoved(0, size);
        }
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Data ...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest( Request.Method.POST, "https://programmerx.000webhostapp.com/ExchangeMe/GetRandomProducts.php",
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
                                //Img, Name, Discription,user_email,type,price,capital
                                Home_item item = new Home_item(
                                        object.getString("photo_url"),
                                        object.getString("product_name"),
                                        object.getString("description"),
                                        object.getString("user_email"),
                                        object.getString("type"),
                                        object.getString("price"),
                                        object.getString("capital"),
                                        object.getString("phone"),
                                        object.getString("date")
                                );
                                homeItems.add(item);
                            }
                            adapter = new Home_Adapter(homeItems,getActivity());
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
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

}
