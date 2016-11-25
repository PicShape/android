package com.example.android.picshape.model;

import android.view.accessibility.AccessibilityNodeProvider;

/**
 * Copyright (C) 2016 Emerik Bedouin - All Rights Reserved
 * Created by emerikbedouin on 06/11/2016.
 */

public class Account {

    private int id;
    private String name;
    private String email;
    private String token;

    public Account(int id, String name, String email, String token){
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
