package com.example.android.picshape.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.example.android.picshape.dao.AccountSingleton;
import com.example.android.picshape.dao.PicSingleton;

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
import java.net.HttpURLConnection;
import java.net.URL;

import static android.app.Activity.RESULT_OK;


/**
 * Created by emerikbedouin on 03/03/2017.
 */

public class UploadPicShapeService extends IntentService {


    private static String mUrlToThePic;
    public static boolean onLoad;

    public static final String mParamIter = "iter";
    public static final String mParamMode = "mode";
    public static final String mParamFmt = "format";
    public static final String RESULT = "result";
    public static final String URLPIC = "urlpath";

    public static final String RECEIVER = "com.example.android.picshape.view";

    private static final String LOG_TAG = "PICTURE UPLOAD SING";

    public UploadPicShapeService() {
        super("Upload PicShape");
        onLoad = false;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        int send;
        int get;
        String url = intent.getStringExtra("url");
        String nbrIteration = intent.getStringExtra(mParamIter);
        String mode = intent.getStringExtra(mParamMode);
        String format = intent.getStringExtra(mParamFmt);

        send = sendPic(url, nbrIteration, mode, format);

        if(send != -1 && mUrlToThePic != null){
            // Success
            Log.v(LOG_TAG, "url : "+ mUrlToThePic);
            publishResults(mUrlToThePic, RESULT_OK);
        }
        else {
            // Echec
            publishResults(null, -1);
        }
    }

    private void publishResults(String urlToThePic, int result) {
        Intent intent = new Intent(RECEIVER);
        intent.putExtra(URLPIC, urlToThePic);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);

    }



    /**
     * This function send picture to server to be converted
     * @return code 0 OK | -1 NOK
     */
    protected static int sendPic(String urlWebService, String iter, String mode, String format){

        HttpURLConnection urlConnection = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        int serverResponseCode = 0;
        String path = null;

        String token = AccountSingleton.getInstance().getAccountLoaded().getToken();

        try {


            URL url = new URL(urlWebService);

            // Create the request to Webservice and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.i(LOG_TAG,"url : "+urlConnection.getURL());


            urlConnection.setDoInput(true); // Allow Inputs
            urlConnection.setDoOutput(true); // Allow Outputs
            urlConnection.setUseCaches(false); // Don't use a Cached Copy
            urlConnection.setConnectTimeout(50000);
            urlConnection.setRequestMethod("POST"); // Request type
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Authorization", "token: "+token);
            urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);


            // Post of the file

            dos = new DataOutputStream(urlConnection.getOutputStream());

            dos.writeBytes(lineEnd);

            // Add parameter --------
            if(iter != null) {
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                // Iteration number
                dos.writeBytes("Content-Disposition: form-data; name=\"" + mParamIter + "\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(iter);
                dos.writeBytes(lineEnd);
            }


            if(mode != null) {
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                // Mode
                dos.writeBytes("Content-Disposition: form-data;name=\"" + mParamMode + "\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(mode);
                dos.writeBytes(lineEnd);
            }

            if(format != null) {
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                // Format
                dos.writeBytes("Content-Disposition: form-data;name=\"" + mParamFmt + "\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(format);
                dos.writeBytes(lineEnd);
            }


            // Picture upload
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data;name=\"photo\";filename=\""
                    + "PicToShape" + "\"" + lineEnd);
            dos.writeBytes("Content-Type: image/png" + lineEnd); // To change if PNG doesn't match with file

            dos.writeBytes(lineEnd);


            Bitmap bitmapPic = PicSingleton.getInstance().getPicToShape();

            path = createTempFile(bitmapPic);
            FileInputStream fileInputStream = null;

            if (path != null){

                Log.v(LOG_TAG,"File path temp : "+path);

                fileInputStream = new FileInputStream(path);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }



            }

            //----------------------------------------------------------------

            // Close
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // End of POST

            //close the streams //
            //close file stream
            if(fileInputStream != null) fileInputStream.close();
            dos.flush();
            dos.close();


            // Fin de l'envoi

            // Responses from the server (code and message)
            serverResponseCode = urlConnection.getResponseCode();
            String serverResponseMessage = urlConnection.getResponseMessage();


            Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

            if(serverResponseCode == 200){
                Log.v(LOG_TAG, "Request success !");

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

                // Retrieve url from JSON
                getUrlFromJSON(returnedJSON);
            }



        } catch (IOException e) {
            Log.e(LOG_TAG, "Error "+e.getMessage(), e);

            return -1;
        }
        finally{
            if (path != null) {
                // Delete the temp file
                if (deleteTempFile(path)) Log.v(LOG_TAG, "File deleted");
                else Log.v(LOG_TAG, "Error File isn't deleted");

            }

            if (urlConnection != null) {
                // Close connection
                urlConnection.disconnect();

                return serverResponseCode;
            }

        }

        return -1;
    }


    /**
     * This function create a file and return its path
     * @param pic
     * @return
     */
    public static String createTempFile(Bitmap pic){

        String path = Environment.getExternalStorageDirectory()+"/tempPicToShape.png";
        File file = new File(path);
        try {
            FileOutputStream fOut = new FileOutputStream(file);

            if (pic.compress(Bitmap.CompressFormat.PNG, 100, fOut)) return path;

            fOut.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }

    /**
     * This function delete file passed in parameter
     * @return
     */
    public static boolean deleteTempFile(String filePath){

        File file = new File(filePath);
        return file.delete();

    }


    /**
     * This function get the URL from JSON String
     * @param jsonString
     */
    protected static void getUrlFromJSON(String jsonString){
        //parse JSON data
        try {
            JSONObject jObject = new JSONObject(jsonString);
            mUrlToThePic = jObject.getString("url");

        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
        }
    }


}
