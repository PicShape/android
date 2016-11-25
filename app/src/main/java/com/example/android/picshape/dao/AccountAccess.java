package com.example.android.picshape.dao;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.android.picshape.R;
import com.example.android.picshape.model.Account;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Copyright (C) 2016 Emerik Bedouin - All Rights Reserved
 * Created by emerikbedouin on 06/11/2016.
 */

public class AccountAccess {

    public static Account getAccountByName(String name, String credential){
        // TODO
        return null;
    }

    public static Account getAccountByEmail(String email, String credential){

        // TODO
        return null;
    }

    public static Account signIn(String urlWebService, String email, String password){

        HttpURLConnection urlConnection = null;
        int serverResponseCode = 0;


        try {

            String urlString = urlWebService;

            URL url = new URL(urlString);

            // Create the request to Webservice and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.i("ACCOUNTACCESS","url : "+urlConnection.getURL());


            urlConnection.setDoOutput(true); // Allow Outputs
            urlConnection.setConnectTimeout(8000);
            urlConnection.setRequestMethod("POST"); // Request type
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


            // Post of the login info
            String postParameters = "email="+email+"&password="+password;

            urlConnection.setFixedLengthStreamingMode(postParameters.getBytes().length);
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(postParameters);
            out.close();

            // Fin de l'envoi

            // Responses from the server (code and message)
            serverResponseCode = urlConnection.getResponseCode();
            String serverResponseMessage = urlConnection.getResponseMessage();


            Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

            if(serverResponseCode == 200){
                Log.v("AccountAccess", "Request success !");

                // We get the returned from the request
                String returnedJSON;

                InputStream is = urlConnection.getInputStream();

                BufferedReader bReader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line2 = null;
                while ((line2 = bReader.readLine()) != null) {
                    sBuilder.append(line2 + "\n");
                }

                is.close();
                returnedJSON = sBuilder.toString();

                // TODO clean
                Log.v("RETOUR MYGOULE","json : "+returnedJSON);

                urlConnection.disconnect();

                return getAccountFromJSON(returnedJSON);
            }



        } catch (IOException e) {
            Log.e("ACCOUNTACCESSS", "Error "+e.getMessage(), e);

            return null;
        }



        return null;
    }

    public static Account signUp(String urlWebService, String email, String name, String password){

        HttpURLConnection urlConnection = null;
        int serverResponseCode = 0;


        try {

            String urlString = urlWebService;

            URL url = new URL(urlString);

            // Create the request to Webservice and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.i("ACCOUNTACCESS","url : "+urlConnection.getURL());

            urlConnection.setDoOutput(true); // Allow Outputs
            urlConnection.setConnectTimeout(8000);
            urlConnection.setRequestMethod("POST"); // Request type
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


            // Post of the login info
            String postParameters = "name="+name+"&email="+email+"&password="+password;

            urlConnection.setFixedLengthStreamingMode(postParameters.getBytes().length);
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(postParameters);
            out.close();

            // Fin de l'envoi

            // Responses from the server (code and message)
            serverResponseCode = urlConnection.getResponseCode();
            String serverResponseMessage = urlConnection.getResponseMessage();


            Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

            if(serverResponseCode == 200){
                Log.v("AccountAccess", "Request success !");

                // We get the returned from the request
                String returnedJSON;

                InputStream is = urlConnection.getInputStream();

                BufferedReader bReader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line2 = null;
                while ((line2 = bReader.readLine()) != null) {
                    sBuilder.append(line2 + "\n");
                }

                is.close();
                returnedJSON = sBuilder.toString();

                // TODO clean
                Log.v("RETOUR MYGOULE","json : "+returnedJSON);

                urlConnection.disconnect();

                return getAccountFromJSON(returnedJSON);
            }



        } catch (IOException e) {
            Log.e("ACCOUNTACCESSS", "Error "+e.getMessage(), e);

            return null;
        }



        return null;
    }

    public static boolean logOff(){

        //TODO

        return false;
    }

    public static boolean forgetPassword(String urlWebService, String email){


        HttpURLConnection urlConnection = null;
        int serverResponseCode = 0;


        try {

            String urlString = urlWebService;

            URL url = new URL(urlString);

            // Create the request to Webservice and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.i("ACCOUNTACCESS","url : "+urlConnection.getURL());


            urlConnection.setDoOutput(true); // Allow Outputs
            urlConnection.setConnectTimeout(8000);
            urlConnection.setRequestMethod("POST"); // Request type
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


            // Post of the login info
            String postParameters = "email="+email;

            urlConnection.setFixedLengthStreamingMode(postParameters.getBytes().length);
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(postParameters);
            out.close();

            // Fin de l'envoi

            // Responses from the server (code and message)
            serverResponseCode = urlConnection.getResponseCode();
            String serverResponseMessage = urlConnection.getResponseMessage();


            Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

            if(serverResponseCode == 200){
                Log.v("AccountAccess", "Request success !");

                // We get the returned from the request
                String returnedJSON;

                InputStream is = urlConnection.getInputStream();

                BufferedReader bReader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line2 = null;
                while ((line2 = bReader.readLine()) != null) {
                    sBuilder.append(line2 + "\n");
                }

                is.close();
                returnedJSON = sBuilder.toString();

                JSONObject jsonObject = new JSONObject(returnedJSON);

                Log.v("ACCOUNTACCESS"," json : "+jsonObject.getString("msg"));

                urlConnection.disconnect();

                return true;
            }



        } catch (IOException e) {
            Log.e("ACCOUNTACCESSS", "Error "+e.getMessage(), e);

            return false;
        }
        catch (JSONException jsonEx){
            Log.e("ACCOUNTACCESSS", "Error "+jsonEx.getMessage(), jsonEx);

            return false;
        }

        return false;
    }

    public static boolean changeAccountInfo(String urlWebService, String password, String token){



        HttpURLConnection urlConnection = null;
        int serverResponseCode = 0;


        try {

            String urlString = urlWebService;

            URL url = new URL(urlString);

            // Create the request to Webservice and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.i("ACCOUNTACCESS","url : "+urlConnection.getURL());


            urlConnection.setDoOutput(true); // Allow Outputs
            urlConnection.setConnectTimeout(8000);
            urlConnection.setRequestMethod("PUT"); // Request type
            urlConnection.setRequestProperty("Authorization", "token: "+token);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


            // Post of the login info
            String postParameters = "password="+password+"&confirm="+password;

            urlConnection.setFixedLengthStreamingMode(postParameters.getBytes().length);
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(postParameters);
            out.close();


            // Fin de l'envoi

            // Responses from the server (code and message)
            serverResponseCode = urlConnection.getResponseCode();
            String serverResponseMessage = urlConnection.getResponseMessage();


            Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

            if(serverResponseCode == 200){
                Log.v("AccountAccess", "Request success !");

                // We get the returned from the request
                String returnedJSON;

                InputStream is = urlConnection.getInputStream();

                BufferedReader bReader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line2 = null;
                while ((line2 = bReader.readLine()) != null) {
                    sBuilder.append(line2 + "\n");
                }

                is.close();
                returnedJSON = sBuilder.toString();

                JSONObject jsonObject = new JSONObject(returnedJSON);

                Log.v("ACCOUNTACCESS"," json : "+jsonObject.getString("msg"));

                urlConnection.disconnect();

                return true;
            }



        } catch (IOException e) {
            Log.e("ACCOUNTACCESSS", "Error "+e.getMessage(), e);

            return false;
        }
        catch (JSONException jsonEx){
            Log.e("ACCOUNTACCESSS", "Error "+jsonEx.getMessage(), jsonEx);

            return false;
        }

        return false;

    }

    public static boolean deleteAccount(String urlWebService, String token){



        HttpURLConnection urlConnection = null;
        int serverResponseCode = 0;


        try {

            String urlString = urlWebService;

            URL url = new URL(urlString);

            // Create the request to Webservice and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.i("ACCOUNTACCESS","url : "+urlConnection.getURL());


            urlConnection.setDoOutput(true); // Allow Outputs
            urlConnection.setConnectTimeout(8000);
            urlConnection.setRequestMethod("DELETE"); // Request type
            urlConnection.setRequestProperty("Authorization", "token: "+token);



            // Fin de l'envoi

            // Responses from the server (code and message)
            serverResponseCode = urlConnection.getResponseCode();
            String serverResponseMessage = urlConnection.getResponseMessage();


            Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

            if(serverResponseCode == 200){
                Log.v("AccountAccess", "Request success !");

                // We get the returned from the request
                String returnedJSON;

                InputStream is = urlConnection.getInputStream();

                BufferedReader bReader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line2 = null;
                while ((line2 = bReader.readLine()) != null) {
                    sBuilder.append(line2 + "\n");
                }

                is.close();
                returnedJSON = sBuilder.toString();

                JSONObject jsonObject = new JSONObject(returnedJSON);

                Log.v("ACCOUNTACCESS"," json : "+jsonObject.getString("msg"));

                urlConnection.disconnect();

                return true;
            }



        } catch (IOException e) {
            Log.e("ACCOUNTACCESSS", "Error "+e.getMessage(), e);

            return false;
        }
        catch (JSONException jsonEx){
            Log.e("ACCOUNTACCESSS", "Error "+jsonEx.getMessage(), jsonEx);

            return false;
        }

        return false;
    }

    // Utility function

    /**
     * This function get the account info from JSON String
     * @param jsonString
     */
    protected static Account getAccountFromJSON(String jsonString){
        //parse JSON data
        try {

            JSONObject jsonAll = new JSONObject(jsonString);

            JSONObject jObject = jsonAll.getJSONObject("user");

            String name = jObject.getString("name");
            String email = jObject.getString("email");

            String token = jsonAll.getString("token");

            return new Account(1, name, email, token);

        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
            return null;
        }
    }

    /**
     * This function get the URL from JSON String
     * @param jsonString
     */
    protected static String getUrlFromJSON(String jsonString){
        //parse JSON data
        try {
            JSONObject jObject = new JSONObject(jsonString);
            return jObject.getString("url");

        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
            return null;
        }
    }

}
