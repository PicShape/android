package com.example.android.picshape.dao;

import android.util.Log;

import com.example.android.picshape.Utility;
import com.example.android.picshape.model.PicshapeAccount;
import com.example.android.picshape.model.PictureShape;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by emerikbedouin on 27/02/2017.
 */

public class PictureAccess {

    /**
     * This function get picture of the profil from the server
     * @param urlWebService
     * @param name
     * @return
     */
    public static ArrayList<PictureShape> getProfilPicturesList(String urlWebService, String name){

        String returnedJSON = Utility.baseRequest(urlWebService+name, "GET");

        ArrayList<PictureShape> picLists = Utility.getPicturesFromJSON(returnedJSON);

        for (int i = 0; i < picLists.size(); i++) {
            picLists.get(i).setIdUser(name);
        }

        return picLists;

    }



    /**
     * This function get picture of the profil from the server
     * @param urlWebService
     * @param token
     * @return
     */
    public static boolean deletePicture(String urlWebService, String token){

        String returnedJSON = Utility.requestBuilder(urlWebService, "DELETE", token, null);

        if(returnedJSON != null) return true;
        else return false;
    }




}
