package com.example.app3;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements PageFragment.MyInterface {

    SharedPreferences sp;
    private MyAdapter adapter;
    private GridRecyclerView recyclerView;
    private ScaleGestureDetector scaleGestureDetector;
    private int DEFAULT_SCALE = 3;
    private float mScaleTriggerDistance = 170;
    private float mInitialDistance;

    ArrayList<String> images_names = new ArrayList<String>();
    ArrayList<String> images_paths = new ArrayList<String>();
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setVisibility(View.GONE);

        SharedPreferences sharedPreferences = getSharedPreferences("Directory search", MODE_PRIVATE);
        boolean isFirst = sharedPreferences.getBoolean("Directory search", false);
        if(!isFirst){
            SharedPreferences.Editor ed = sharedPreferences.edit();
            ed.putBoolean("Directory search", true);
            ed.commit();
            performFileSearch();
            getImages();
        }
        getImages();

        sp = getSharedPreferences("Layout", MODE_PRIVATE);
        boolean isLinearView = sp.getBoolean("Layout",true);

        adapter = new MyAdapter(this, images_names, images_paths, isLinearView,DEFAULT_SCALE);
        recyclerView.setLayoutManager(isLinearView ? new LinearLayoutManager(this) : new GridLayoutManager(this, 3));

        LayoutAnimationController animation = AnimationUtils
                .loadLayoutAnimation(getApplicationContext(), isLinearView ? R.anim.layout_animation_left_to_right : R.anim.grid_layout_animation_from_bottom );
        recyclerView.setLayoutAnimation(animation);

        adapter.setOnItemClickListener(onItemClickListener);

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);
                return false;
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            boolean isViewWithCatalog = sp.getBoolean("Layout", true);

            if(!isViewWithCatalog) {
                if (gestureTolerance(detector)) {
                    if (scaleFactor > 1.0) {
                        if (DEFAULT_SCALE != 2)
                            DEFAULT_SCALE--;

                        GridLayoutManager recyclerViewLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                        recyclerViewLayoutManager.setSpanCount(DEFAULT_SCALE);
                        TransitionManager.beginDelayedTransition(recyclerView);
                        adapter.notifyDataSetChanged();

                        mInitialDistance = detector.getCurrentSpan();

                    } else {
                        if (DEFAULT_SCALE != 5)
                            DEFAULT_SCALE++;

                        GridLayoutManager recyclerViewLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                        recyclerViewLayoutManager.setSpanCount(DEFAULT_SCALE);
                        TransitionManager.beginDelayedTransition(recyclerView);
                        adapter.notifyDataSetChanged();

                        mInitialDistance = detector.getCurrentSpan();
                    }
                }
            } else {
                if (gestureTolerance(detector)) {
                    if (scaleFactor > 1.0) {
                        if (DEFAULT_SCALE != 2)
                            DEFAULT_SCALE--;

                        int currentVisiblePosition = 0;
                        currentVisiblePosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

                        adapter = new MyAdapter(getApplicationContext(), images_names, images_paths, isViewWithCatalog, DEFAULT_SCALE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                        adapter.setOnItemClickListener(onItemClickListener);
                        recyclerView.getLayoutManager().scrollToPosition(currentVisiblePosition);

                        mInitialDistance = detector.getCurrentSpan();

                    } else {
                        if (DEFAULT_SCALE != 4)
                            DEFAULT_SCALE++;

                        int currentVisiblePosition = 0;
                        currentVisiblePosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

                        adapter = new MyAdapter(getApplicationContext(), images_names, images_paths, isViewWithCatalog, DEFAULT_SCALE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                        adapter.setOnItemClickListener(onItemClickListener);

                        recyclerView.getLayoutManager().scrollToPosition(currentVisiblePosition);

                        mInitialDistance = detector.getCurrentSpan();
                    }
                }
            }
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
           mInitialDistance = detector.getCurrentSpan();
           return true;
        }

    }


    private boolean gestureTolerance(@NonNull ScaleGestureDetector detector) {
        final float currentDistance = detector.getCurrentSpan();
        final float distanceDelta = Math.abs(mInitialDistance - currentDistance);
        return distanceDelta > mScaleTriggerDistance;
    }


    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            GridRecyclerView.ViewHolder viewHolder = (GridRecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            Bundle extras = new Bundle();
            extras.putString("imagebitmap", images_paths.get(position));

            mPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), images_paths));
            mPager.setVisibility(View.VISIBLE);
            mPager.setPageTransformer(true, new DepthPageTransformer());
            mPager.setCurrentItem(position);


        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        boolean isViewLinear = sp.getBoolean("Layout", true);
        if(isViewLinear) {
            menu.getItem(0).setTitle(R.string.Grid);
        }else{
            menu.getItem(0).setTitle(R.string.Linear);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item1:
                boolean isViewWithCatalog = sp.getBoolean("Layout", true);

                supportInvalidateOptionsMenu();
                isViewWithCatalog = !isViewWithCatalog;
                SharedPreferences.Editor ed = sp.edit();
                ed.putBoolean("Layout", isViewWithCatalog);
                ed.commit();

                if(isViewWithCatalog){
                    item.setTitle(R.string.Linear);
                }else{
                    item.setTitle(R.string.Grid);
                }

                adapter = new MyAdapter(this, images_names, images_paths, isViewWithCatalog, DEFAULT_SCALE);

                recyclerView.setLayoutManager(isViewWithCatalog ? new LinearLayoutManager(this) : new GridLayoutManager(this, 3));

                LayoutAnimationController animation = AnimationUtils
                        .loadLayoutAnimation(getApplicationContext(), isViewWithCatalog ? R.anim.layout_animation_left_to_right : R.anim.grid_layout_animation_from_bottom );
                recyclerView.setLayoutAnimation(animation);

                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(onItemClickListener);

                return true;
            case R.id.item2:
                performFileSearch();
                getImages();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void performFileSearch() {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(Intent.createChooser(i, "Choose directory"), 1);
    }


    @AfterPermissionGranted(123)
    private void getImages(){
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            File[] images_list;

            SharedPreferences sharedPreferences = getSharedPreferences("ChooseDirectory", MODE_PRIVATE);
            String path = sharedPreferences.getString("currentDirectory", "");

            File folder = new File(Environment.getExternalStorageDirectory() + "/" + path);

            if (folder.isDirectory() && folder.exists()) {
                images_list = folder.listFiles();
                for (File anImages_list : images_list) {
                    String[] fileNameArray = anImages_list.getName().split("\\.");
                    String extension = fileNameArray[fileNameArray.length-1];
                    if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png")) {
                        images_names.add(anImages_list.getName());
                        images_paths.add(anImages_list.getAbsolutePath());
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(),"Folder is unreachable", Toast.LENGTH_SHORT).show();
            }
        } else {
            EasyPermissions.requestPermissions(this, "We need this permission to load your photos here",
                    123, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode == 1) {
            try {
                final Uri uri = imageReturnedIntent.getData();
                File file = new File(uri.getPath());
                String s = file.getAbsolutePath();
                Log.i("TEST", s.split(":")[1]);
                SharedPreferences sharedPreferences = getSharedPreferences("ChooseDirectory", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("currentDirectory", s.split(":")[1]);
                editor.commit();

                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            } catch (NullPointerException e) {
                e.printStackTrace();
                Toast.makeText(this, "You didn't choose the directory", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onTrigger(ArrayList<String> image) {

        ArrayList<String> imagetorotate = image;
        mPager.setAdapter(new RotatePagerAdapter(getSupportFragmentManager(), imagetorotate));
        mPager.setVisibility(View.VISIBLE);

    }
}
