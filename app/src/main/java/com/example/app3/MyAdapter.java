package com.example.app3;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends GridRecyclerView.Adapter<MyAdapter.ViewHolder> {

    ArrayList<String> images_names;
    ArrayList<String> images_paths;
    boolean isLinearView = false;
    Context context;
    int currentScale;
    private View.OnClickListener itemClickListener;


    public MyAdapter(Context context, ArrayList<String> images_names, ArrayList<String> images_paths, boolean isLinearView, int currentScale) {
        this.images_names = images_names;
        this.images_paths = images_paths;
        this.context = context;
        this.isLinearView = isLinearView;
        this.currentScale = currentScale;
    }


    public class ViewHolder extends GridRecyclerView.ViewHolder {

        TextView myTextView;
        ImageView imageView;
        private Context context;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            myTextView = (TextView) itemView.findViewById(R.id.textView1);
            imageView = (ImageView) itemView.findViewById(R.id.imageView1);

            itemView.setTag(this);
            itemView.setOnClickListener(itemClickListener);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = null;

        if (isLinearView) {
            itemView = inflater.inflate(R.layout.recycler_view_item, viewGroup, false);
        }else{
            itemView = inflater.inflate(R.layout.recycler_view_item_2, viewGroup, false);
        }
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;


        if(isLinearView) {
            viewHolder.myTextView.setText(images_names.get(i));
            Glide.with(context).load(images_paths.get(i)).centerCrop().into(viewHolder.imageView);

            if(currentScale == 2) {
                viewHolder.imageView.getLayoutParams().height = height/4;
                viewHolder.imageView.getLayoutParams().width = height/4;
                viewHolder.myTextView.setTextSize(18);
            } else if(currentScale == 3) {
                viewHolder.imageView.getLayoutParams().height = height/5;
                viewHolder.imageView.getLayoutParams().width = height/5;
                viewHolder.myTextView.setTextSize(16);
            } else if(currentScale == 4) {
                viewHolder.imageView.getLayoutParams().height = height/6;
                viewHolder.imageView.getLayoutParams().width = height/6;
                viewHolder.myTextView.setTextSize(14);
            }

        } else {
            Glide.with(context).load(images_paths.get(i)).centerCrop().into(viewHolder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return images_names.size();
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }



}
