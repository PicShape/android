package com.example.android.picshape.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.picshape.R;
import com.example.android.picshape.model.PictureShape;

import java.util.LinkedList;

/**
 * Copyright (C) 2016 Emerik Bedouin - All Rights Reserved
 * Created by emerikbedouin on 07/11/2016.
 */

public class PictureAdapter extends BaseAdapter {
    protected Context context;
    protected LinkedList<PictureShape> gallery;

    public PictureAdapter(Context context, LinkedList<PictureShape> gallery){
        this.context = context;
        this.gallery = gallery;
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

        ImageView pic;

        // At the first call ConvertView is null, we inflate our layout
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = mInflater.inflate(R.layout.layout_gallery, parent, false);

            pic = (ImageView) convertView.findViewById(R.id.image);


            // We set our ImageView as tag of convertVie
            convertView.setTag(pic);
        } else {
            // convertView isn't null so no need to retrieve our views
            pic = (ImageView) convertView.getTag();
        }

        // We get the pic at the position
        PictureShape picShape = (PictureShape) getItem(position);

        // Set value
        pic.setImageBitmap(picShape.getPicBitmap());

        return convertView;
    }
}
