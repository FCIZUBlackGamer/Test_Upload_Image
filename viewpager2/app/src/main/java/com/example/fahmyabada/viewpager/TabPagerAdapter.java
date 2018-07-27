package com.example.fahmyabada.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by FahmyAbada on 5/1/2018.
 */

public class TabPagerAdapter extends FragmentPagerAdapter {

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new triptype();
        if(position==0){
            fragment=new triptype();
        }else if(position==1){
            fragment=new fromto();
        }else if(position==2){
            fragment=new time();
        }else if(position==3){
            fragment=new details();
        }
        return fragment;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "TripType";
            case 1:
                return "From-To";
            case 2:
                return "Time";
            case 3:
                return "Details";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
