package com.example.android.picshape.dao;

import android.content.Context;
import android.util.Log;

import com.example.android.picshape.BuildConfig;

import com.example.android.picshape.Utility;
import com.example.android.picshape.model.PicshapeAccount;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Copyright (C) 2016 Emerik Bedouin - All Rights Reserved
 * Created by emerikbedouin on 06/11/2016.
 */

public class AccountAccess {


    /**
     * This function connect user to the service
     * @param urlWebService
     * @param email
     * @param password
     * @return
     */
    public static PicshapeAccount signIn(String urlWebService, String email, String password){

        ArrayList<String> params = new ArrayList<>();
        params.add("email="+email);
        params.add("password="+password);
        String returnedJSON = Utility.requestBuilder(urlWebService, "POST", null, params);

        return Utility.getAccountFromJSON(returnedJSON);

    }

    /**
     * This function register user to the service
     * @param urlWebService
     * @param email
     * @param name
     * @param password
     * @return
     */
    public static PicshapeAccount signUp(String urlWebService, String email, String name, String password){


        ArrayList<String> params = new ArrayList<>();
        params.add("name="+name);
        params.add("email="+email);
        params.add("password="+password);
        String returnedJSON = Utility.requestBuilder(urlWebService, "POST", null, params);

        return Utility.getAccountFromJSON(returnedJSON);
    }

    /**
     * This function delete User token in internal memory
     * @param context
     * @return
     */
    public static boolean logOff(Context context){

        //TODO

        AccountSingleton.getInstance().setAccountLoaded(null);

        if( deleteProfil(context, BuildConfig.SAVE_FILE_NAME) ) return true;


        return false;
    }

    /**
     * This function send a request to get an email to recover password
     * @param urlWebService
     * @param email
     * @return
     */
    public static boolean forgetPassword(String urlWebService, String email){

        ArrayList<String> params = new ArrayList<>();
        params.add("email="+email);
        String returnedJSON = Utility.requestBuilder(urlWebService, "POST", null, params);

        if(returnedJSON != null) return true;
        else return false;

    }

    /**
     * This function send a POST to modify an account
     * @param urlWebService
     * @param password
     * @param token
     * @return
     */
    public static boolean changeAccountInfo(String urlWebService, String password, String token){


        ArrayList<String> params = new ArrayList<>();
        params.add("password="+password);
        params.add("confirm="+password);
        String returnedJSON = Utility.requestBuilder(urlWebService, "PUT", token, params);

        if(returnedJSON != null) return true;
        else return false;

    }

    /**
     * This function send a POST to delete an account
     * @param urlWebService
     * @param token
     * @return
     */
    public static boolean deleteAccount(String urlWebService, String token){

        String returnedJSON = Utility.requestBuilder(urlWebService, "DELETE", token, null);

        if(returnedJSON != null) return true;
        else return false;
    }

    /**
     * This function send a GET to get all users of PicShape
     * @param urlRoute
     * @return
     */
    public static ArrayList<String> getAllUsers(String urlRoute){

        String returnedJSON = Utility.baseRequest(urlRoute, "GET");

        return Utility.getNameAccountsFromJSON(returnedJSON);
    }

    /**
     * This function send a GET to get all users of PicShape
     * @param urlRoute
     * @return
     */
    public static ArrayList<String> getUsersByName(String urlRoute, String name){

        String returnedJSON = Utility.baseRequest(urlRoute+name, "GET");

        return Utility.getNameAccountsFromJSON(returnedJSON);

    }

    /**
     * This function return an account from a name passed in parameters
     * @param urlRoute
     * @param name
     * @return
     */
    public static PicshapeAccount getUserByName(String urlRoute, String name){

        String returnedJSON = Utility.baseRequest(urlRoute+name, "GET");

        return Utility.getAccountFromJSONv2(returnedJSON);

    }


    /**
     * Save account on internal memory in JSON
     * @param context
     * @param account
     * @return
     */
    public static boolean saveProfilJSON(Context context, PicshapeAccount account, String fileName){

        String data = Utility.getJSONFromAccount(account);

        File file = new File(context.getFilesDir(), fileName);

        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Load account from internal memory
     * @param context
     * @param fileName
     * @return
     */
    public static PicshapeAccount loadProfilJSON(Context context, String fileName){

        String json = null;

        File file = new File(context.getFilesDir(), fileName);

        FileInputStream inputStream;

        try{
            inputStream = context.openFileInput(fileName);
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));

            json = r.readLine();

            r.close();
            inputStream.close();

            return Utility.getAccountFromJSON(json);
        }
        catch (Exception e){
            e.printStackTrace();
        }


        return null;
    }

    /**
     * This function delete profil in internal memory
     * @param context
     * @param fileName
     * @return
     */
    public static boolean deleteProfil(Context context, String fileName){
        boolean deleted = false;
        File file = context.getFileStreamPath(fileName);

        if(file != null) deleted = file.delete();

        return deleted;
    }



    // Utility function

}
