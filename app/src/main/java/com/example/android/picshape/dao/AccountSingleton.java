package com.example.android.picshape.dao;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.example.android.picshape.model.Account;

/**
 * Copyright (C) 2016 Emerik Bedouin - All Rights Reserved
 * Created by emerikbedouin on 09/11/2016.
 */

public class AccountSingleton {

    private static AccountSingleton mInstance;
    private static Account accountLoaded;


    private AccountSingleton(){

    }

    public static AccountSingleton getInstance(){
        if (mInstance == null) return new AccountSingleton();
        else return mInstance;
    }

    public static Account getAccountLoaded() {
        return accountLoaded;
    }

    public static void setAccountLoaded(Account accountLoaded) {
        AccountSingleton.accountLoaded = accountLoaded;
    }
}