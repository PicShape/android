package com.example.android.picshape.model;

import android.graphics.Bitmap;

/**
 * Copyright (C) 2016 Emerik Bedouin - All Rights Reserved
 * Created by emerikbedouin on 07/11/2016.
 */

public class PictureShape {

    private String description;
    private String urlConverted;
    private String urlPhoto;
    private String urlThumb;
    private Bitmap picBitmap;

    public PictureShape(String description, Bitmap picBitmap){
        this.description = description;
        this.picBitmap = picBitmap;
    }

    public PictureShape(String description, String urlConverted, String urlThumb, String urlPhoto) {
        this.description = description;
        this.urlConverted = urlConverted;
        this.urlPhoto = urlPhoto;
        this.urlThumb = urlThumb;
        this.picBitmap = null;
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

    public String getUrlThumb() {
        return urlThumb;
    }

    public void setUrlThumb(String urlThumb) {
        this.urlThumb = urlThumb;
    }

    public String getUrlConverted() {
        return urlConverted;
    }

    public void setUrlConverted(String urlConverted) {
        this.urlConverted = urlConverted;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }
}
