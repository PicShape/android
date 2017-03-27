package com.example.android.picshape.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.picshape.model.PictureShape;

import java.util.List;

/**
 * Created by emerikbedouin on 27/02/2017.
 */

public class GalleryAdapter extends ArrayAdapter<PictureShape> {

    public GalleryAdapter(Context context, int resource, List<PictureShape> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(getContext());
            imageView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 200));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(2, 2, 2, 2);
        } else {
            imageView = (ImageView) convertView;
        }

        PictureShape picture = getItem(position);

        Glide.with(imageView.getContext())
                .load(picture.getUrlConverted())
                .into(imageView);

        //Glide.with(imageView.getContext()).load(picture.getUrlThumb()).into(imageView);

        return imageView;

    }
}
