package com.example.android.picshape.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
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
import android.widget.Toast;

import com.example.android.picshape.R;
import com.example.android.picshape.dao.PicSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Created by Emerik Bedouin
 */

public class ParamActivity extends AppCompatActivity {

    final private String TAG_PARAM_ACTIVITY = "PARAM ACTIVITY";

    private String mSourceFilePath;

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

        if ( getUriByIntent() ) setMinImageView();

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

        ArrayList<String> modeArray = new ArrayList<String>(Arrays.asList("combo", "triangle", "rect", "ellipse", "circle", "rotatedrect", "beziers", "rotatedellipse", "polygon"));

        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, modeArray);

        ArrayList<String> formatArray = new ArrayList<String>(Arrays.asList("PNG", "JPG", "SVG", "GIF"));
        ArrayAdapter<String> formatAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, formatArray);

        mModeSpinner.setAdapter(modeAdapter);
        mFormatSpinner.setAdapter(formatAdapter);
    }

    /**
     * This function get Uri of image passed by intent
     */
    public boolean getUriByIntent(){
        Bundle extras = getIntent().getExtras();

        if (extras != null){
            mSourceFilePath = extras.getString("imgPath");
            Log.v(TAG_PARAM_ACTIVITY,"Get it "+mSourceFilePath);
            return true;
        }
        else{
            Toast.makeText(this,"No pic passed as parameter", Toast.LENGTH_SHORT).show();
            return false;
        }
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
            int mode = mModeSpinner.getSelectedItemPosition();
            String format = mFormatSpinner.getSelectedItem().toString();
            int nbrIteration = Integer.parseInt(mIterationEditText.getText().toString());

            Log.v(TAG_PARAM_ACTIVITY,"Mode : "+mode+" format "+format+" Iteration "+nbrIteration);

            if( PicSingleton.getInstance().getPicToShape() != null){
                Log.v(TAG_PARAM_ACTIVITY,"Photo : "+ PicSingleton.getInstance().getPicToShape().toString());
            }

            // Check internet connectivity
            if (networkAvailable()) {
                // Create task to send the image
                FetchPicTask task = new FetchPicTask();
                task.execute();
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

        if(on) {
            paramLayout.setVisibility(View.GONE);
            progressLayout.setVisibility(View.VISIBLE);
        }
        else{
            progressLayout.setVisibility(View.GONE);
            paramLayout.setVisibility(View.VISIBLE);
        }
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
     * This class manages the http call to webService
     */
    class FetchPicTask extends AsyncTask<String, Void, Integer> {

        private final String LOG_TAG = FetchPicTask.class.getName();


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
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(mSourceFilePath);
            String fileName = getNameFromPath(mSourceFilePath);
            int serverResponseCode = 0;


            try {

                /*String authority = "192.168.43.233";
                String port = "8080";
                Uri.Builder builder;
                builder = Uri.parse("http://"+authority+":"+port).buildUpon();
                builder.appendPath("api")
                        .appendPath("photos")
                        .appendPath("upload");
                String urlString = builder.build().toString();*/
                String urlString = getWebServiceUrl();

                URL url = new URL(urlString);

                // Create the request to Webservice and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                Log.i(LOG_TAG,"url : "+urlConnection.getURL());

                FileInputStream fileInputStream = new FileInputStream(sourceFile);

                urlConnection.setDoInput(true); // Allow Inputs
                urlConnection.setDoOutput(true); // Allow Outputs
                urlConnection.setUseCaches(false); // Don't use a Cached Copy
                urlConnection.setRequestMethod("POST"); // Request type
                urlConnection.setRequestProperty("Connection", "Keep-Alive");
                urlConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
                urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                urlConnection.setRequestProperty("photo", fileName);


                dos = new DataOutputStream(urlConnection.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data;name=\"photo\";filename=\""
                        + fileName + "\"" + lineEnd);
                dos.writeBytes("Content-Type: image/png" + lineEnd); // To change if PNG doesn't match with file

                dos.writeBytes(lineEnd);

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

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Fin de l'envoi

                // Responses from the server (code and message)
                serverResponseCode = urlConnection.getResponseCode();
                String serverResponseMessage = urlConnection.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){
                    Toast.makeText(ParamActivity.this, "Pic sent !", Toast.LENGTH_SHORT).show();
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();



            } catch (IOException e) {
                Log.e(LOG_TAG, "Error "+e.getMessage(), e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            }
            finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();

                    return serverResponseCode;
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return null;
        }


        /**
         * This function is called after the end of doInBackground method
         * @param result
         */
        @Override
        protected void onPostExecute(Integer result) {

            loading(false);

            if(result != null) {

                //TODO get the pic !

                Log.v(LOG_TAG, "Result code "+result);
            }
            else{
                Log.v(LOG_TAG, "Error connection failed");
            }
        }

        /**
         * This function return file name from its path
         * @param path
         * @return
         */
        private String getNameFromPath(String path){
            String name = "";

            StringTokenizer token = new StringTokenizer(path,"/");
            name = token.toString();
            while(token.hasMoreElements()){
                name = token.nextToken();
            }

            return name;
        }

    }




}
