package com.example.android.picshape.dao;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.android.picshape.BuildConfig;
import com.example.android.picshape.R;
import com.example.android.picshape.Utility;
import com.example.android.picshape.model.PicshapeAccount;
import com.example.android.picshape.view.ConnectActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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

                return Utility.getAccountFromJSON(returnedJSON);
            }



        } catch (IOException e) {
            Log.e("ACCOUNTACCESSS", "Error "+e.getMessage(), e);

            return null;
        }



        return null;
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

                return Utility.getAccountFromJSON(returnedJSON);
            }



        } catch (IOException e) {
            Log.e("ACCOUNTACCESSS", "Error "+e.getMessage(), e);

            return null;
        }



        return null;
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

    /**
     * This function send a POST to modify an account
     * @param urlWebService
     * @param password
     * @param token
     * @return
     */
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

    /**
     * This function send a POST to delete an account
     * @param urlWebService
     * @param token
     * @return
     */
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

    /**
     * This function send a GET to get all users of PicShape
     * @param urlRoute
     * @return
     */
    public static ArrayList<String> getAllUsers(String urlRoute){


        HttpURLConnection urlConnection = null;
        int serverResponseCode = 0;


        try {

            String urlString = urlRoute;

            URL url = new URL(urlString);

            // Create the request to Webservice and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.i("AccountACCESS","url : "+urlConnection.getURL());
            urlConnection.setConnectTimeout(8000);
            urlConnection.setRequestMethod("GET"); // Request type


            // Responses from the server (code and message)
            serverResponseCode = urlConnection.getResponseCode();
            String serverResponseMessage = urlConnection.getResponseMessage();

            Log.i("AccountACCESS","Response code : "+serverResponseCode+" || "+serverResponseMessage);

            if(serverResponseCode == 200){

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
                Log.v("AccountACCESS","json : "+returnedJSON);

                urlConnection.disconnect();

                return Utility.getNameAccountsFromJSON(returnedJSON);
            }



        } catch (IOException e) {
            Log.e("PICTURE ACCESSS", "Error "+e.getMessage(), e);

            return null;
        }

        return null;
    }

    /**
     * This function send a GET to get all users of PicShape
     * @param urlRoute
     * @return
     */
    public static ArrayList<String> getUsers(String urlRoute, String name){


        HttpURLConnection urlConnection = null;
        int serverResponseCode = 0;


        try {

            String urlString = urlRoute+name;

            URL url = new URL(urlString);

            // Create the request to Webservice and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.i("AccountACCESS","url : "+urlConnection.getURL());
            urlConnection.setConnectTimeout(8000);
            urlConnection.setRequestMethod("GET"); // Request type


            // Responses from the server (code and message)
            serverResponseCode = urlConnection.getResponseCode();
            String serverResponseMessage = urlConnection.getResponseMessage();

            Log.i("AccountACCESS","Response code : "+serverResponseCode+" || "+serverResponseMessage);

            if(serverResponseCode == 200){

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
                Log.v("AccountACCESS","json : "+returnedJSON);

                urlConnection.disconnect();

                return Utility.getNameAccountsFromJSON(returnedJSON);
            }



        } catch (IOException e) {
            Log.e("PICTURE ACCESSS", "Error "+e.getMessage(), e);

            return null;
        }

        return null;
    }


    public void handleUnauthorized(){
        //TODO tell user to reconnect

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


    public static boolean deleteProfil(Context context, String fileName){
        boolean deleted = false;
        File file = context.getFileStreamPath(fileName);

        if(file != null) deleted = file.delete();

        return deleted;
    }



    // Utility function



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
