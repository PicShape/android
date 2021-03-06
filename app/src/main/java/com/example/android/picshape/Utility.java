package com.example.android.picshape;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.android.picshape.model.PicshapeAccount;
import com.example.android.picshape.model.PictureShape;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by emerikbedouin on 05/10/2016.
 * This class is used to check permission
 */

public class Utility {

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * This function get the number of picture posted by the user
     */
    public static int getNumberPicturePosted(PicshapeAccount account){

        //TODO get the number of pic posted

        return 0;
    }

    /**
     * This function get the number of like received by the user
     */
    public static int getNumberLike(PicshapeAccount account){

        //TODO get the number of like received

        return 0;
    }


    /**
     * This function get the account info from JSON String
     * @param jsonString
     */
    public static PicshapeAccount getAccountFromJSON(String jsonString){

        if(jsonString == null) return null;

        //parse JSON data
        try {

            Log.v("ACCOUNT ACCESS", "T : "+jsonString);

            JSONObject jsonAll = new JSONObject(jsonString);


            String token = jsonAll.getString("token");

            JSONObject jObject = jsonAll.getJSONObject("user");

            String name = jObject.getString("name");
            String email = jObject.getString("email");
            String urlGravatar = jObject.getString("gravatar");

            return new PicshapeAccount("1", name, email, token, urlGravatar);

        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
            return null;
        }
    }

    /**
     * This function get the account info from JSON String for the second version of User JSON
     * @param jsonString
     */
    public static PicshapeAccount getAccountFromJSONv2(String jsonString){
        //parse JSON data
        try {

            Log.v("ACCOUNT ACCESS", "T : "+jsonString);

            JSONObject jsonAll = new JSONObject(jsonString);
            JSONArray jsonUsers = jsonAll.getJSONArray("users");


            JSONObject jObject = (JSONObject) jsonUsers.get(0);

            String name = jObject.getString("name");
            //String email = jObject.getString("email");
            String urlGravatar = jObject.getString("gravatar");

            return new PicshapeAccount("1", name, null, null, urlGravatar);

        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
            return null;
        }
    }

    /**
     * This function get accounts name from JSON String
     * @param jsonString
     */
    public static ArrayList<String> getNameAccountsFromJSON(String jsonString){
        //parse JSON data
        try {
            ArrayList<String> nameList = new ArrayList<>();
            Log.v("ACCOUNT ACCESS", "T : "+jsonString);
            JSONObject myJSON = new JSONObject(jsonString);
            JSONArray jsonAll = myJSON.getJSONArray("users");

            for (int i = 0 ; i < jsonAll.length() ; i++) {

                JSONObject jObject = (JSONObject) jsonAll.get(i);

                nameList.add( jObject.getString("name") );

            }

            return nameList;

        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
            return null;
        }
    }


    /**
     * This function get the JSON string from PicShapeAccount
     * @param account
     */
    public static String getJSONFromAccount(PicshapeAccount account){
        String json= "{";

        //Token
        json += "\"token\": \""+account.getToken()+"\", ";

        // User
        json += "\"user\": { ";

        // id
        json+="\"id\":"+account.getId()+", ";
        // Name
        json+="\"name\":\""+account.getName()+"\", ";
        // Email
        json+="\"email\":\""+account.getEmail()+"\", ";
        // Url Gravatar
        json+="\"gravatar\":\""+account.getUrlGravatar()+"\"";

        // End of user
        json += "}";

        json += "}";

        return json;
    }


    /**
     * This function get PictureShape from JSON
     */
    public static PictureShape getPictureFromJSON(String jsonString){

        if(jsonString != null){

            PictureShape tempShape;
            try {
                JSONObject jsonAll = new JSONObject(jsonString);

                String user = jsonAll.getString("user");
                String thumbnail =jsonAll.getString("thumbnail");
                String photo = jsonAll.getString("photo");
                String converted = jsonAll.getString("converted");
                String timestamp = jsonAll.getString("timestamp");

                tempShape = new PictureShape(user, photo, "Description", converted, thumbnail, photo, timestamp);

                return tempShape;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    /**
     * This function get PictureShapes from JSON
     */
    public static ArrayList<PictureShape> getPicturesFromJSON(String jsonString){

        if(jsonString != null){
            ArrayList<PictureShape> listOfShape = new ArrayList<>();


            JSONObject jsonAll = null;
            PictureShape tempShape;
            try {

                JSONArray listUrl = new JSONArray(jsonString);

                for (int i=0; i < listUrl.length() ; i++) {
                    String thumbnail = listUrl.getJSONObject(i).getString("thumbnail");
                    String photo = listUrl.getJSONObject(i).getString("photo");
                    String converted = listUrl.getJSONObject(i).getString("converted");

                    tempShape = new PictureShape("1", "nomPhoto", "Romain", converted, thumbnail, photo);
                    listOfShape.add(tempShape);
                }

                return listOfShape;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    /**
     * This function sort a list
     * @param listToSort
     * @return
     */
    public static ArrayList<String> sortList(ArrayList<String> listToSort, String exp){
        ArrayList<String> listSorted = null;

        if(listToSort != null && listToSort.size() > 0){
            listSorted = new ArrayList<>();
            for (int i = 0; i < listToSort.size(); i++) {
                if(listToSort.get(i).contains(exp)){
                    listSorted.add(listToSort.get(i));
                }
            }
        }

        return listSorted;
    }

    /**
     * This function send a HTTP Request
     * @param urlPath
     * @param method
     * @return
     */
    public static String baseRequest(String urlPath, String method){


        HttpURLConnection urlConnection = null;
        int serverResponseCode = 0;

        try {

            URL url = new URL(urlPath);

            // Create the request to Webservice and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.i("BASE REQUEST","url : "+urlConnection.getURL());
            urlConnection.setConnectTimeout(8000); // TimeOut
            urlConnection.setRequestMethod(method); // Request type


            // Responses from the server (code and message)
            serverResponseCode = urlConnection.getResponseCode();
            String serverResponseMessage = urlConnection.getResponseMessage();

            Log.i("BASE REQUEST","Response code : "+serverResponseCode+" || "+serverResponseMessage);

            if(serverResponseCode == 200){

                // We get the returned from the request
                String returnedJSON;

                InputStream is = urlConnection.getInputStream();

                BufferedReader bReader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }

                is.close();
                returnedJSON = sBuilder.toString();

                Log.v("AccountACCESS","json : "+returnedJSON);

                urlConnection.disconnect();

                return returnedJSON;
            }



        } catch (IOException e) {
            Log.e("BASE REQUEST", "Error "+e.getMessage(), e);

            return null;
        }

        return null;
    }


    /**
     * This function send a HTTP Request with parameters and token
     * @param urlPath
     * @param method
     * @return
     */
    public static String requestBuilder(String urlPath, String method, String token, ArrayList<String> params){


        HttpURLConnection urlConnection = null;
        int serverResponseCode = 0;

        try {

            URL url = new URL(urlPath);

            // Create the request to Webservice and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.i("BASE REQUEST","url : "+urlConnection.getURL());
            urlConnection.setConnectTimeout(8000); // TimeOut
            urlConnection.setRequestMethod(method); // Request type
            //Token
            if(token != null) urlConnection.setRequestProperty("Authorization", "token: "+token);

            //Parameters
            if(params != null && params.size() > 0){
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                String postParameters = "";
                for (int i = 0; i < params.size(); i++) {
                    if (i > 0){
                        postParameters += "&"+params.get(i);
                    }
                    else{
                        postParameters += params.get(i);
                    }
                }

                urlConnection.setFixedLengthStreamingMode(postParameters.getBytes().length);
                PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                out.print(postParameters);
                out.close();
            }

            // Responses from the server (code and message)
            serverResponseCode = urlConnection.getResponseCode();
            String serverResponseMessage = urlConnection.getResponseMessage();

            Log.i("BASE REQUEST","Response code : "+serverResponseCode+" || "+serverResponseMessage);

            if(serverResponseCode == 200){

                // We get the returned from the request
                String returnedJSON;

                InputStream is = urlConnection.getInputStream();

                BufferedReader bReader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }

                is.close();
                returnedJSON = sBuilder.toString();

                Log.v("AccountACCESS","json : "+returnedJSON);

                urlConnection.disconnect();

                return returnedJSON;
            }



        } catch (IOException e) {
            Log.e("BASE REQUEST", "Error "+e.getMessage(), e);

            return null;
        }

        return null;
    }

}
