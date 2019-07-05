package com.example.app3;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<String> images_paths;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<String> images_paths) {
        super(fm);
        this.images_paths = images_paths;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.getInstance(images_paths.get(position));
    }

    @Override
    public int getCount() {
        return images_paths.size();
    }
}