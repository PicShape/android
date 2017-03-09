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
        //parse JSON data
        try {

            Log.v("ACCOUNT ACCESS", "T : "+jsonString);

            JSONObject jsonAll = new JSONObject(jsonString);


            String token = jsonAll.getString("token");

            JSONObject jObject = jsonAll.getJSONObject("user");

            String name = jObject.getString("name");
            String email = jObject.getString("email");

            return new PicshapeAccount(1, name, email, token);

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
        json += "\"token\": "+account.getToken()+", ";

        // User
        json += "\"user\": { ";

        // id
        json+="\"id\":"+account.getId()+", ";
        // Name
        json+="\"name\":"+account.getName()+", ";
        // Email
        json+="\"email\":"+account.getEmail();


        // End of user
        json += "}";

        json += "}";

        return json;
    }


    /**
     * This function get PictureShape's from JSON
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

                    tempShape = new PictureShape("1", "Romain", converted, thumbnail, photo);
                    listOfShape.add(tempShape);
                }

                return listOfShape;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
