package com.example.android.picshape.view;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.example.android.picshape.dao.AccountSingleton;
import com.example.android.picshape.dao.PicSingleton;
import com.example.android.picshape.service.UploadPicShapeService;

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

    final private int mMaxIter = 100;

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
                // Create Service to send the image
                Intent intent = new Intent(this, UploadPicShapeService.class);
                // add infos for the service which file to download and where to store
                intent.putExtra("url", getWebServiceUrl());
                intent.putExtra(UploadPicShapeService.mParamIter, nbrIteration);
                intent.putExtra(UploadPicShapeService.mParamFmt, format);
                intent.putExtra(UploadPicShapeService.mParamMode, mode);

                startService(intent);

                //TODO Launch Gallery
                Intent galleryIntent = new Intent(this, GalleryActivity.class);
                galleryIntent.putExtra("service","ON");
                startActivity(galleryIntent);

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

        String route = "/api/picshape/convert";

        // Retrieve URL from preferences
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(this);
        String url = preferences.getString(getString(R.string.pref_url_key),getString(R.string.pref_url_default))+route;

        return url;
    }

    /**
     * This function show Toast to inform user in ParamActivity
     */
    public void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }



}
