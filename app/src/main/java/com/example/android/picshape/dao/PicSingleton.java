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

    /**
     * This function save picture to internal memory
     */
    public static String savePictureToMemory(Bitmap picture, Context context){

        Log.v("PIC SINGLETON","Trying to save");

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + context.getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create imageDir
        File mypath=new File(mediaStorageDir,"picshape"+System.currentTimeMillis()+".png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            picture.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();

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
