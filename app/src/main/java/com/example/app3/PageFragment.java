package com.example.app3;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

import static android.app.Activity.RESULT_OK;


public class PageFragment extends Fragment {

    private String imageResource;
    View rootView;
    ImageView imageView;
    MyInterface mCallback;

    public interface MyInterface {
        void onTrigger(ArrayList<String> images_paths);

    }

    public static PageFragment getInstance(String resourceID) {
        PageFragment f = new PageFragment();
        Bundle args = new Bundle();
        args.putString("image_source", resourceID);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (MyInterface ) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement MyInterface ");
        }

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

                String[] filePath = imageResource.split("\\.");
                String distImage = filePath[0] + "_CROPPED.jpg";

                startActivityForResult(UCrop.of(Uri.fromFile(new File(imageResource)), Uri.fromFile(new File(distImage)))
                        .getIntent(getContext()), UCrop.REQUEST_CROP);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {

            final Uri resultUri = UCrop.getOutput(data);

            Intent intent1 = new Intent(getContext(), MainActivity.class);
            startActivity(intent1);

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);

        } else if (requestCode == RESULT_OK && data != null) {

        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.photo_fragment, container, false);


        return inflater.inflate(R.layout.photo_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = (ImageView) view.findViewById(R.id.imageFragment);
        Glide.with(getContext()).load(imageResource).into(imageView);

        final PhotoViewAttacher pAttacher = new PhotoViewAttacher(imageView);
        pAttacher.setOnMatrixChangeListener(new PhotoViewAttacher.OnMatrixChangedListener() {
            @Override
            public void onMatrixChanged(RectF rect) {
                pAttacher.setAllowParentInterceptOnEdge(pAttacher.getScale()==1);
            }
        });
        pAttacher.update();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}