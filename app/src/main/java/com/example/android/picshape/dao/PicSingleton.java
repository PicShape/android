package com.example.android.picshape.dao;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Copyright (C) 2016 Emerik Bedouin - All Rights Reserved
 * Created by emerikbedouin on 10/10/2016.
 */

public class PicSingleton {

    private static PicSingleton mInstance;
    private static Bitmap mPicToShape;
    private static Drawable mPicShape;


    private PicSingleton(){

    }

    public static PicSingleton getInstance(){
        if (mInstance == null) return new PicSingleton();
        else return mInstance;
    }


    public Bitmap getPicToShape() {
        return mPicToShape;
    }

    public void setPicToShape(Bitmap picToShape) {
        this.mPicToShape = picToShape;
    }

    public static Drawable getPicShaped() {
        return mPicShape;
    }

    public static void setPicShaped(Drawable mPicShape) {
        PicSingleton.mPicShape = mPicShape;
    }


}
