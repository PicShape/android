package com.example.android.picshape.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Copyright (C) 2016 Emerik Bedouin - All Rights Reserved
 * Created by emerikbedouin on 07/11/2016.
 */

public class PictureShape implements Parcelable{

    private String idUser;
    private String idPic;
    private String description;
    private String urlConverted;
    private String urlPhoto;
    private String urlThumb;

    public PictureShape(String description){
        this.description = description;
    }

    public PictureShape(String idUser, String idPic, String description, String urlConverted, String urlThumb, String urlPhoto) {
        this.idUser = idUser;
        this.idPic = idPic;
        this.description = description;
        this.urlConverted = urlConverted;
        this.urlPhoto = urlPhoto;
        this.urlThumb = urlThumb;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdPic() {
        return idPic;
    }

    public void setIdPic(String idPic) {
        this.idPic = idPic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idUser);
        dest.writeString(idPic);
        dest.writeString(description);
        dest.writeString(urlConverted);
        dest.writeString(urlPhoto);
        dest.writeString(urlThumb);
    }

    public static final Parcelable.Creator<PictureShape> CREATOR = new Parcelable.Creator<PictureShape>()
    {
        @Override
        public PictureShape createFromParcel(Parcel source)
        {
            return new PictureShape(source);
        }

        @Override
        public PictureShape[] newArray(int size)
        {
            return new PictureShape[size];
        }
    };

    public PictureShape(Parcel in) {
        this.idUser = in.readString();
        this.idPic = in.readString();
        this.description = in.readString();
        this.urlConverted = in.readString();
        this.urlPhoto = in.readString();
        this.urlThumb = in.readString();

    }
}
