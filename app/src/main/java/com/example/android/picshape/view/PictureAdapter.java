package com.example.android.picshape.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.picshape.R;
import com.example.android.picshape.model.PictureShape;

import java.util.ArrayList;

/**
 * Copyright (C) 2016 Emerik Bedouin - All Rights Reserved
 * Created by emerikbedouin on 07/11/2016.
 */

public class PictureAdapter extends BaseAdapter {
    protected Context context;
    protected ArrayList<PictureShape> gallery;
    public View.OnClickListener mOnClickListener;

    public PictureAdapter(Context context, ArrayList<PictureShape> gallery){
        this.context = context;
        this.gallery = gallery;
    }

    public PictureAdapter(Context context, ArrayList<PictureShape> gallery, View.OnClickListener onClickListener){
        this.context = context;
        this.gallery = gallery;
        this.mOnClickListener = onClickListener;
    }

    @Override
    public int getCount() {
        return gallery.size();
    }

    @Override
    public Object getItem(int position) {
        return gallery.get(position);
    }

    @Override
    public long getItemId(int position) {
        return gallery.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        myConvertView viewHolder = null;

        ImageView pic;

        // At the first call ConvertView is null, we inflate our layout
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = mInflater.inflate(R.layout.layout_wall_picture, parent, false);

            viewHolder = new myConvertView();
            viewHolder.userTv = (TextView) convertView.findViewById(R.id.name_user_textView);
            viewHolder.titleTv = (TextView) convertView.findViewById(R.id.title_pic_textView);
            viewHolder.picIv = (ImageView) convertView.findViewById(R.id.pic_imageView);


            // We set our ImageView as tag of convertVie
            convertView.setTag(viewHolder);
        } else {
            // convertView isn't null so no need to retrieve our views
            viewHolder = (myConvertView) convertView.getTag();
        }

        // We get the pic at the position
        PictureShape picShape = (PictureShape) getItem(position);

        // Set value
        //pic.setImageBitmap(picShape.getPicBitmap());

        PictureShape picture = gallery.get(position);

        Glide.with(viewHolder.picIv.getContext()).load(picture.getUrlConverted()).into(viewHolder.picIv);
        viewHolder.userTv.setText(picture.getIdUser());
        viewHolder.titleTv.setText(picture.getDescription());

        viewHolder.userTv.setOnClickListener(mOnClickListener);

        return convertView;
    }

    private class myConvertView{
        TextView userTv;
        TextView titleTv;
        ImageView picIv;
    }
}
