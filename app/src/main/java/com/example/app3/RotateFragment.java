package com.example.app3;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;
import com.theartofdev.edmodo.cropper.CropImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class RotateFragment extends Fragment implements View.OnTouchListener {

    private String imageResource;
    View rootView;
    ImageView imageView;

    public static RotateFragment getInstance(String resourceID) {
        RotateFragment f = new RotateFragment();
        Bundle args = new Bundle();
        args.putString("image_source", resourceID);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageResource = getArguments().getString("image_source");
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.full_image_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.share:
                Intent share = new Intent(Intent.ACTION_SEND_MULTIPLE);
                share.setType("image/png");
                ArrayList<Uri> images =new ArrayList<Uri>();
                images.add(Uri.parse("file:///"+imageResource));
                share.putParcelableArrayListExtra(Intent.EXTRA_STREAM, images);
                startActivity(Intent.createChooser(share, "Share Image"));
                return true;
            case R.id.setAsWallpaper:
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getContext());
                Bitmap myBitmap =  BitmapFactory.decodeFile(imageResource);
                try {
                    wallpaperManager.setBitmap(myBitmap);
                    Toast.makeText(getContext(), "Wallpaper has been changed", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.back:
                Intent intent1 = new Intent(getContext(), MainActivity.class);
                startActivity(intent1);

                return true;
            case R.id.info:

                Intent intent = new Intent(getContext(), InfoActivity.class);
                Bundle extras = new Bundle();
                extras.putString("imageinfo", imageResource);
                intent.putExtras(extras);
                startActivity(intent);

                return true;
            case R.id.crop:



                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.rotate_fragment, container, false);


        return inflater.inflate(R.layout.rotate_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CropImage.activity(Uri.fromFile(new File(imageResource)))
                .start(getContext(), this);

    }

    private double mCurrAngle = 0;
    private double mPrevAngle = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final float xc = imageView.getWidth() / 2;
        final float yc = imageView.getHeight() / 2;

        final float x = event.getX();
        final float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                imageView.clearAnimation();
                mCurrAngle = Math.toDegrees(Math.atan2(x - xc, yc - y));
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                mPrevAngle = mCurrAngle;
                mCurrAngle = Math.toDegrees(Math.atan2(x - xc, yc - y));
                animate(mPrevAngle, mCurrAngle, 0);
                System.out.println(mCurrAngle);
                break;
            }
            case MotionEvent.ACTION_UP : {
                mPrevAngle = mCurrAngle = 0;
                break;
            }
        }
        return true;
    }

    private void animate(double fromDegrees, double toDegrees, long durationMillis) {
        final RotateAnimation rotate = new RotateAnimation((float) fromDegrees, (float) toDegrees,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(durationMillis);
        rotate.setFillEnabled(true);
        rotate.setFillAfter(true);
        imageView.startAnimation(rotate);
        System.out.println(mCurrAngle);
    }


}
