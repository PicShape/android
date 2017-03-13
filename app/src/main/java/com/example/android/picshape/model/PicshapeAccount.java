package com.example.android.picshape.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.accessibility.AccessibilityNodeProvider;

/**
 * Copyright (C) 2016 Emerik Bedouin - All Rights Reserved
 * Created by emerikbedouin on 06/11/2016.
 */

public class PicshapeAccount implements Parcelable{

    private String id;
    private String name;
    private String email;
    private String token;

    public PicshapeAccount(String id, String name, String email, String token){
        this.id = id ;
        this.name = name;
        this.email = email;
        this.token = token;
    }


    // Getters & Setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(token);
    }

    public static final Parcelable.Creator<PicshapeAccount> CREATOR = new Parcelable.Creator<PicshapeAccount>()
    {
        @Override
        public PicshapeAccount createFromParcel(Parcel source)
        {
            return new PicshapeAccount(source);
        }

        @Override
        public PicshapeAccount[] newArray(int size)
        {
            return new PicshapeAccount[size];
        }
    };

    public PicshapeAccount(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.email = in.readString();
        this.token = in.readString();


    }

}
