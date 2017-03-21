package com.example.android.picshape.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.android.picshape.R;
import com.example.android.picshape.Utility;
import com.example.android.picshape.dao.AccountSingleton;
import com.example.android.picshape.dao.PicSingleton;
import com.example.android.picshape.model.PictureShape;
import com.example.android.picshape.view.GalleryActivity;
import com.example.android.picshape.view.SinglePicActivity;

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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * Created by emerikbedouin on 03/03/2017.
 */

public class UploadPicShapeService extends IntentService {


    private static PictureShape mPicShaped;
    public static boolean onLoad;

    public static final String mParamIter = "iter";
    public static final String mParamMode = "mode";
    public static final String mParamFmt = "format";
    public static final String RESULT = "result";
    public static final String URLPIC = "urlpath";

    public static final String RECEIVER = "com.example.android.picshape.view";

    private static final String LOG_TAG = "PICTURE UPLOAD SING";

    public static final int ERROR_UNAUTHORIZED = 401;

    public static final int mId = 12;

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

        if(send != -1 && mPicShaped != null){
            // Success
            Log.v(LOG_TAG, "Pic : "+ mPicShaped);
            publishResults(mPicShaped, RESULT_OK);
        }
        else if (send == ERROR_UNAUTHORIZED){
            // Echec
            publishResults(null, ERROR_UNAUTHORIZED);
        }
        else {
            // Echec
            publishResults(null, RESULT_CANCELED);
        }
    }

    private void publishResults(PictureShape picShaped, int result) {

        if(result == RESULT_OK) createNotification("PicShape", "Your picture was successfully uploaded", result);
        else createNotification("PicShape", "Picture upload failed :(", result);
        Intent intent = new Intent(RECEIVER);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);

    }

    private void createNotification(String title, String text, int result){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo2)
                        .setContentTitle(title)
                        .setContentText(text);
        // Creates an explicit intent for an Activity in your app
                Intent resultIntent = null;
                if(result == RESULT_OK) resultIntent = new Intent(this, SinglePicActivity.class);
                else resultIntent = new Intent(this, GalleryActivity.class);

                String username = AccountSingleton.getInstance().getAccountLoaded().getName();

                resultIntent.putExtra("username", username);
                resultIntent.putExtra("pic", mPicShaped);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
                stackBuilder.addParentStack(GalleryActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
                mNotificationManager.notify(mId, mBuilder.build());
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

                // Retrieve urls from JSON
                mPicShaped =  Utility.getPictureFromJSON(returnedJSON);

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
            else if(serverResponseCode == ERROR_UNAUTHORIZED){
                return serverResponseCode;
            }



        } catch (IOException e) {
            Log.e(LOG_TAG, "Error "+e.getMessage(), e);

            return -1;
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





}
