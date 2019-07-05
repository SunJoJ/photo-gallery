package com.example.app3;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class RotatePagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<String> image_info;

    public RotatePagerAdapter(FragmentManager fm, ArrayList<String> image_info) {
        super(fm);
        this.image_info = image_info;
    }

    @Override
    public Fragment getItem(int index) {
        return RotateFragment.getInstance(image_info.get(index));
    }

    @Override
    public int getCount() {
        return image_info.size();
    }
}