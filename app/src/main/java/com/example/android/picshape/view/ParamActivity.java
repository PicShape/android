package com.example.android.picshape.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.picshape.R;
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

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Emerik Bedouin
 */

public class ParamActivity extends AppCompatActivity {

    final private String TAG_PARAM_ACTIVITY = "PARAM ACTIVITY";


    //View
    private Button mSelectImageBtn,mSendBtn;
    private EditText mIterationEditText;
    private Spinner mModeSpinner, mFormatSpinner;
    private ImageView mMinImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param);

        initComp();

        if ( PicSingleton.getInstance().getPicToShape() != null ) setMinImageView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.welcome_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            settings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This function launch Settings activity
     */
    public void settings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        if(intent.resolveActivity(this.getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Initialisation of graphics component
     */
    public void initComp(){


        mSelectImageBtn = (Button) findViewById(R.id.select_btn);

        mSendBtn = (Button) findViewById(R.id.send_btn);

        mIterationEditText = (EditText) findViewById(R.id.iteration_editText);

        mModeSpinner = (Spinner) findViewById(R.id.spinner_mode);

        mFormatSpinner = (Spinner) findViewById(R.id.spinner_format);

        mMinImageView = (ImageView) findViewById(R.id.miniature_imageView);


        fillSpinner();


        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPic();
            }
        });
    }

    /**
     * This function fills spinner with data
     */
    public void fillSpinner(){

        ArrayList<String> modeArray = new ArrayList<String>(Arrays.asList(getResources().getStringArray((R.array.array_mode))));
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, modeArray);

        ArrayList<String> formatArray = new ArrayList<String>(Arrays.asList(getResources().getStringArray((R.array.array_format))));
        ArrayAdapter<String> formatAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, formatArray);

        mModeSpinner.setAdapter(modeAdapter);
        mFormatSpinner.setAdapter(formatAdapter);
    }

    /**
     * This function change miniature ImageView with the Picture passed by parameters
     */
    public void setMinImageView(){

        mMinImageView.setImageBitmap(PicSingleton.getInstance().getPicToShape());
    }

    /**
     * This function send the pic to PicShape server
     */
    public void sendPic(){

        try {
            String mode = mModeSpinner.getSelectedItemPosition()+"";
            String format = mFormatSpinner.getSelectedItem().toString();
            String nbrIteration = mIterationEditText.getText().toString();

            Log.v(TAG_PARAM_ACTIVITY,"Mode : "+mode+" format "+format+" Iteration "+nbrIteration);

            if( PicSingleton.getInstance().getPicToShape() != null){
                Log.v(TAG_PARAM_ACTIVITY,"Photo : "+ PicSingleton.getInstance().getPicToShape().toString());
            }

            // Check internet connectivity
            if (networkAvailable()) {
                // Create task to send the image
                FetchPicTask task = new FetchPicTask();
                task.execute(nbrIteration, mode, format);
            }
            else {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception ex){
            Log.e(TAG_PARAM_ACTIVITY, "Error "+ex.getMessage());
            Toast.makeText(this,"Error : Champ incorrect",Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * This function checks if a connection is available
     * @return
     */
    public boolean networkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * This function replaces parameters layout by a progress bar
     */
    public void loading(boolean on){

        LinearLayout paramLayout = (LinearLayout) findViewById(R.id.parameters_layout);
        LinearLayout progressLayout = (LinearLayout) findViewById(R.id.progress_layout);
        TextView titleTv = (TextView) findViewById(R.id.title_textView);

        if(on) {
            paramLayout.setVisibility(View.GONE);
            progressLayout.setVisibility(View.VISIBLE);
            titleTv.setText("Sending...");
        }
        else{
            progressLayout.setVisibility(View.GONE);
            paramLayout.setVisibility(View.VISIBLE);
            titleTv.setText("Parameters");
        }
    }

    /**
     * This function show pic received to the user
     */
    public void showPicReceived(Drawable pic){

        LinearLayout paramLayout = (LinearLayout) findViewById(R.id.parameters_layout);
        LinearLayout progressLayout = (LinearLayout) findViewById(R.id.progress_layout);
        progressLayout.setVisibility(View.GONE);
        paramLayout.setVisibility(View.VISIBLE);


        // Other solution with another activity
        PicSingleton.getInstance().setPicShaped(pic);

        launchResultActivity();

    }

    /**
     * This function launch Result Activity
     */
    public void launchResultActivity(){
        Intent resultIntent = new Intent(this, ResultActivity.class);
        startActivity(resultIntent);

    }

    /**
     * This function get WebService URL from preferences
     * @return
     */
    public String getWebServiceUrl(){
        // Retrieve URL from preferences
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(this);
        String url = preferences.getString(getString(R.string.pref_url_key),getString(R.string.pref_url_default));

        return url;
    }

    /**
     * This function show Toast to inform user in ParamActivity
     */
    public void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }



    /**
     * This class manages the http call to webService
     */
    class FetchPicTask extends AsyncTask<String, Void, Integer> {

        private final String LOG_TAG = FetchPicTask.class.getName();
        private String mUrlToThePic;
        private Drawable mPicReceived;
        private String mParamIter = "iter";
        private String mParamMode = "mode";
        private String mParamFmt = "format";


        /**
         * This function is executed befor execution of the task, used to show progress bar
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading(true);
        }

        /**
         * This function execute the call to webService in background
         * @param params
         * @return
         */
        @Override
        protected Integer doInBackground(String[] params){

            int send;
            int get;
            send = sendPic(params[0], params[1], params[2]);

            if(send != -1 && mUrlToThePic != null){

                Log.v(LOG_TAG, "url : "+ mUrlToThePic);
                get = getPic();

                if(get != -1){

                    return 0;
                }
            }

            return -1;
        }


        /**
         * This function is called after the end of doInBackground method
         * @param result
         */
        @Override
        protected void onPostExecute(Integer result) {

            loading(false);

            if(result != -1) {

                // Display of the converted picture
                showPicReceived(mPicReceived);

                Log.v(LOG_TAG, "Result code "+result);
                showToast("Pic sent with success !");
            }
            else{
                Log.v(LOG_TAG, "Error connection failed code "+result);
                showToast("Error sorry we failed to convert your picture !");
            }
        }

        /**
         * This function create a file and return its path
         * @param pic
         * @return
         */
        public String createTempFile(Bitmap pic){

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
        public boolean deleteTempFile(String filePath){

            File file = new File(filePath);
            return file.delete();

        }


        /**
         * This function send picture to server to be converted
         * @return code 0 OK | -1 NOK
         */
        protected int sendPic(String iter, String mode, String format){

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


            try {

                String urlString = getWebServiceUrl();

                URL url = new URL(urlString);

                // Create the request to Webservice and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                Log.i(LOG_TAG,"url : "+urlConnection.getURL());


                urlConnection.setDoInput(true); // Allow Inputs
                urlConnection.setDoOutput(true); // Allow Outputs
                urlConnection.setUseCaches(false); // Don't use a Cached Copy
                urlConnection.setConnectTimeout(50000);
                urlConnection.setRequestMethod("POST"); // Request type
                urlConnection.setRequestProperty("Connection", "Keep-Alive");
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
         * This function get the pic converted from the server
         * @return code 0 OK | -1 NOK
         */
        protected int getPic(){

            try {
                InputStream is = (InputStream) new URL(mUrlToThePic).getContent();
                mPicReceived = Drawable.createFromStream(is, "src name");

                return 0;

            } catch (Exception e) {
                Log.v(LOG_TAG, "Error can't get Picture "+e.getMessage());
                return -1;
            }

        }

        /**
         * This function get the URL from JSON String
         * @param jsonString
         */
        protected void getUrlFromJSON(String jsonString){
            //parse JSON data
            try {
                JSONObject jObject = new JSONObject(jsonString);
                mUrlToThePic = jObject.getString("url");

            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            }
        }

    }




}
