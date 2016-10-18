package com.example.android.picshape.dao;

import android.graphics.Bitmap;

/**
 * Copyright (C) 2016 Emerik Bedouin - All Rights Reserved
 * Created by emerikbedouin on 10/10/2016.
 */

public class PicSingleton {

    private static PicSingleton instance;
    private static Bitmap picToShape;


    private PicSingleton(){

    }

    public static PicSingleton getInstance(){
        if (instance == null) return new PicSingleton();
        else return instance;
    }

    public Bitmap getPicToShape() {
        return picToShape;
    }

    public void setPicToShape(Bitmap picToShape) {
        this.picToShape = picToShape;
    }
}
