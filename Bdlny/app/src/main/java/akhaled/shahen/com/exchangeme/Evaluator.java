package akhaled.shahen.com.exchangeme;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fci on 03/03/18.
 */

public class Evaluator extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Evaluator_item> evaluator_items;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.evaluator,container,false );
        //   getActivity().getActionBar().setTitle("Department");
        recyclerView = (RecyclerView) view.findViewById(R.id.eva_rec);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Switch_Nav) getActivity())
                .setActionBarTitle("Evaluator");
    }

    @Override
    public void onStart() {
        super.onStart();
        LoadRecyclerViewData();

    }
    private void LoadRecyclerViewData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Data ...");
        progressDialog.show();

        evaluator_items = chargeData(evaluator_items);

        adapter = new Evaluator_Adapter(evaluator_items,getActivity());
        recyclerView.setAdapter(adapter);
        progressDialog.dismiss();
    }

    private List<Evaluator_item> chargeData(List<Evaluator_item> s){
        s = new ArrayList<>();
        Evaluator_item  i = new Evaluator_item("Home Evaluator");
        s.add( i );
        i = new Evaluator_item( "furniture Evaluator" );
        s.add( i );
        i = new Evaluator_item( "Mobile Evaluator" );
        s.add( i );
        i = new Evaluator_item( "Electronics Evaluator" );
        s.add( i );
        i = new Evaluator_item( "Car Evaluator" );
        s.add( i );

        return s;
    }
}
