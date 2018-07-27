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

public class Department extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Department_item> department_items;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.department,container,false );
     //   getActivity().getActionBar().setTitle("Department");
        recyclerView = (RecyclerView) view.findViewById(R.id.dep_rec);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Switch_Nav) getActivity())
                .setActionBarTitle("Department");
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

        department_items = chargeData(department_items);

        adapter = new Department_Adapter(department_items,getActivity());
        recyclerView.setAdapter(adapter);
        progressDialog.dismiss();
    }

    private List<Department_item> chargeData(List<Department_item> s){
        s = new ArrayList<>();
        Department_item i = new Department_item( R.mipmap.car,"Cars" );
        s.add( i );
        i = new Department_item( R.mipmap.fur,"furniture" );
        s.add( i );
        i = new Department_item( R.mipmap.home,"Home" );
        s.add( i );
        i = new Department_item( R.mipmap.electro,"Electronics" );
        s.add( i );
        i = new Department_item( R.mipmap.phone,"Phones" );
        s.add( i );
        i = new Department_item( R.mipmap.others,"Others" );
        s.add( i );
        return s;
    }
}
