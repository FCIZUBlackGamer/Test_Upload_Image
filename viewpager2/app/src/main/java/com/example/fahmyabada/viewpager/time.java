package com.example.fahmyabada.viewpager;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class time extends Fragment {
    EditText txt;
    Button btn;
    DatePickerDialog datePickerDialog;
    Calendar calendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_time, container, false );
        txt = (EditText) view.findViewById( R.id.date );

        btn = (Button) view.findViewById( R.id.show );

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        txt.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                int year = calendar.get( Calendar.YEAR );
                int month = calendar.get( Calendar.MONTH );
                int dayofmonth = calendar.get( Calendar.DAY_OF_MONTH );
                datePickerDialog = new DatePickerDialog( getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String meg = dayOfMonth + "/" + (month + 1) + "/" + year;
                        txt.setText( meg );

                    }
                }, year, month, dayofmonth );
                datePickerDialog.show();

            }
        } );

        btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                details d = new details();
//                Bundle bundle = new Bundle();
//                bundle.putString( "message", txt.getText().toString().trim() );
//                d.setArguments( bundle );
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction().replace( R.id.container, d );
//                fragmentTransaction.addToBackStack( null );
//                fragmentTransaction.commit();


            }

        } );

    }
}

