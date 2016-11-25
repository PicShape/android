package com.example.android.picshape.model;

import android.graphics.Bitmap;

/**
 * Copyright (C) 2016 Emerik Bedouin - All Rights Reserved
 * Created by emerikbedouin on 07/11/2016.
 */

public class PictureShape {

    private String description;
    private Bitmap picBitmap;

    public PictureShape(String description, Bitmap picBitmap){
        this.description = description;
        this.picBitmap = picBitmap;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getPicBitmap() {
        return picBitmap;
    }

    public void setPicBitmap(Bitmap picBitmap) {
        this.picBitmap = picBitmap;
    }
}
