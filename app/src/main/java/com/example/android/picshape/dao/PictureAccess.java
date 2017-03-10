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

        HttpURLConnection urlConnection = null;
        int serverResponseCode = 0;


        try {

            String urlString = urlWebService+name;

            URL url = new URL(urlString);

            // Create the request to Webservice and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.i("PictureACCESS","url : "+urlConnection.getURL());
            urlConnection.setConnectTimeout(8000);
            urlConnection.setRequestMethod("GET"); // Request type


            // Responses from the server (code and message)
            serverResponseCode = urlConnection.getResponseCode();
            String serverResponseMessage = urlConnection.getResponseMessage();

            Log.i("PictureACCESS","Response code : "+serverResponseCode+" || "+serverResponseMessage);

            if(serverResponseCode == 200){
                Log.v("PictureAccess", "Request success !");

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
                Log.v("PICTURE ACCESS","json : "+returnedJSON);

                urlConnection.disconnect();


                ArrayList<PictureShape> picLists = Utility.getPicturesFromJSON(returnedJSON);

                for (int i = 0; i < picLists.size(); i++) {
                    picLists.get(i).setIdUser(name);
                }

                return picLists;
            }



        } catch (IOException e) {
            Log.e("PICTURE ACCESSS", "Error "+e.getMessage(), e);

            return null;
        }



        return null;
    }

    /**
     * This function get all picture from the server
     * @param urlWebService
     * @param name
     * @return
     */
    //UNUSED ---------------------
    public static ArrayList<PictureShape> getAllPicturesList(String urlWebService, ArrayList<String> name){

        HttpURLConnection urlConnection = null;
        int serverResponseCode = 0;


        try {

            String urlString = urlWebService+name;

            URL url = new URL(urlString);

            // Create the request to Webservice and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.i("PictureACCESS","url : "+urlConnection.getURL());
            urlConnection.setConnectTimeout(8000);
            urlConnection.setRequestMethod("GET"); // Request type


            // Responses from the server (code and message)
            serverResponseCode = urlConnection.getResponseCode();
            String serverResponseMessage = urlConnection.getResponseMessage();

            Log.i("PictureACCESS","Response code : "+serverResponseCode+" || "+serverResponseMessage);

            if(serverResponseCode == 200){
                Log.v("PictureAccess", "Request success !");

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
                Log.v("PICTURE ACCESS","json : "+returnedJSON);

                urlConnection.disconnect();

                return Utility.getPicturesFromJSON(returnedJSON);
            }



        } catch (IOException e) {
            Log.e("PICTURE ACCESSS", "Error "+e.getMessage(), e);

            return null;
        }



        return null;
    }

}
