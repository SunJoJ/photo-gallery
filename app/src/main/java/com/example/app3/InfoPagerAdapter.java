package com.example.app3;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

import java.util.ArrayList;


public class InfoPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<String> image_info;

    public InfoPagerAdapter(FragmentManager fm, ArrayList<String> image_info)   {
        super(fm);
        this.image_info = image_info;
    }

    @Override
    public Fragment getItem(int index) {
        return InfoFragment.getInstance(image_info.get(index));
    }

    @Override
    public int getCount() {
        return image_info.size();
    }
}
