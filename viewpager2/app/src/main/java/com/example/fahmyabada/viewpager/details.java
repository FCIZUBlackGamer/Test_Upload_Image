package com.example.fahmyabada.viewpager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class details extends Fragment {

    TextView txt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_details, container, false );
        txt = (TextView) view.findViewById( R.id.date );
        savedInstanceState = getActivity().getIntent().getExtras();
        if (savedInstanceState != null) {
            String value = savedInstanceState.getString( "message" );
            Toast.makeText( getActivity(), value, Toast.LENGTH_LONG ).show();
            txt.setText( value );
        } else {
            Toast.makeText( getActivity(), "Null", Toast.LENGTH_LONG ).show();
        }

        return view;

    }
}
