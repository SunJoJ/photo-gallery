package com.example.app3;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

public class FullScreenImage extends AppCompatActivity {

    String imagePath;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        ImageView imageView = (ImageView) findViewById(R.id.fullImage);
        Bundle extras = getIntent().getExtras();
        imagePath = extras.getString("imagebitmap");
        //bitmap = decodeSampledBitmapFromFile(imagePath);
        //imageView.setImageBitmap(bitmap);

        Glide.with(getApplicationContext()).load(imagePath).into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.full_image_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.share:
                Intent share = new Intent(Intent.ACTION_SEND_MULTIPLE);
                share.setType("image/png");
                ArrayList<Uri> images =new ArrayList<Uri>();
                images.add(Uri.parse("file:///"+imagePath));
                share.putParcelableArrayListExtra(Intent.EXTRA_STREAM, images);
                startActivity(Intent.createChooser(share, "Share Image"));
                return true;
            case R.id.setAsWallpaper:
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                Bitmap myBitmap =  BitmapFactory.decodeFile(imagePath);
                try {
                    wallpaperManager.setBitmap(myBitmap);
                    Toast.makeText(getApplicationContext(), "Wallpaper has been changed", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Bitmap decodeSampledBitmapFromFile(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, 200,200);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


}
