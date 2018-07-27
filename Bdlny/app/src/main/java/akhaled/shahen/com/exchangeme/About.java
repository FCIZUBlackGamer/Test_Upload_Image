package akhaled.shahen.com.exchangeme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by fci on 03/03/18.
 */

public class About extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.about,container,false );
        //getActivity().getActionBar().setTitle("About");
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((Switch_Nav) getActivity())
                .setActionBarTitle("About");
    }
}
