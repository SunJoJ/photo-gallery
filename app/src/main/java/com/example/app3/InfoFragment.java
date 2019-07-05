package com.example.app3;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class InfoFragment extends Fragment {

    private String imageInfo;
    View rootView;
    ViewPager mPager;
    TextView textView;

    public static InfoFragment getInstance(String resourceID) {
        InfoFragment f = new InfoFragment();
        Bundle args = new Bundle();
        args.putString("image_info", resourceID);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageInfo = getArguments().getString("image_info");
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.info_fragment, container, false);


        return inflater.inflate(R.layout.info_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView = (TextView) view.findViewById(R.id.textFragment);
        textView.setText(imageInfo);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
