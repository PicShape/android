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

import org.json.JSONException;
import org.json.JSONObject;

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
}
