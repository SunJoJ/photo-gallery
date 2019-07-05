package com.example.app3;

import android.media.ExifInterface;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity{

    private ViewPager viewPager;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Bundle extras = getIntent().getExtras();
        imagePath = extras.getString("imageinfo");

        ArrayList<String> image_info = new ArrayList<String>();

        String metadata = "";
        String info = "";

        try {
            File file = new File(imagePath);
            ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());

            metadata += "Date: " + (exifInterface.getAttribute(ExifInterface.TAG_DATETIME)) + "\n";
            metadata += "Artist: " + (exifInterface.getAttribute(ExifInterface.TAG_ARTIST)) + "\n";
            metadata += "GPS altitude: " + (exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE)) + "\n";
            metadata += "GPS datestamp: " + (exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP)) + "\n";
            metadata += "GPS area information: " + (exifInterface.getAttribute(ExifInterface.TAG_GPS_AREA_INFORMATION)) + "\n";
            metadata += "Model: " + (exifInterface.getAttribute(ExifInterface.TAG_MODEL)) + "\n";
            metadata += "Brightness value: " + (exifInterface.getAttribute(ExifInterface.TAG_BRIGHTNESS_VALUE)) + "\n";
            metadata += "ISO speed ratings: " + (exifInterface.getAttribute(ExifInterface.TAG_ISO_SPEED_RATINGS)) + "\n";
            metadata += "White balance: " + (exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE)) + "\n";
            metadata += "Image length: " + (exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)) + "\n";
            metadata += "Image width: " + (exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)) + "\n";
            metadata += "Color space: " + (exifInterface.getAttribute(ExifInterface.TAG_COLOR_SPACE)) + "\n";
            metadata += "Copy right: " + (exifInterface.getAttribute(ExifInterface.TAG_COPYRIGHT)) + "\n";
            metadata += "Focal length: " + (exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH)) + "\n";
            metadata += "Software: " + (exifInterface.getAttribute(ExifInterface.TAG_SOFTWARE)) + "\n";

        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] fileName =imagePath.split("/");
        File file = new File(imagePath);
        info += "Image path: " + imagePath + "\n";
        info += "Image size: " + file.length()/1024 + " KB\n";
        info += "Image name: " + fileName[fileName.length-1] + "\n";

        image_info.add(metadata);
        image_info.add(info);

        viewPager = (ViewPager) findViewById(R.id.InfoPager);
        viewPager.setAdapter(new InfoPagerAdapter(getSupportFragmentManager(), image_info));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Meta data");
        tabLayout.getTabAt(1).setText("Detalis");

    }


}
