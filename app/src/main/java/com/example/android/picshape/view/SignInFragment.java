package com.example.android.picshape.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.picshape.BuildConfig;
import com.example.android.picshape.R;
import com.example.android.picshape.dao.AccountAccess;
import com.example.android.picshape.dao.AccountSingleton;
import com.example.android.picshape.dao.PicSingleton;
import com.example.android.picshape.model.PicshapeAccount;

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

/**
 * This Fragment
 */
public class SignInFragment extends Fragment {

    // Views
    private Button mBtnSubmit;
    private TextView mForgotPassTv;
    private EditText mMailEditText, mPasswordEditText;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);

        initViews(rootView);

        return rootView;
    }

    /**
     * This function initialize components of the fragment
     * @param rootView
     */
    public void initViews(View rootView){

        mMailEditText = (EditText) rootView.findViewById(R.id.email_editText);

        mForgotPassTv = (TextView) rootView.findViewById(R.id.forgot_pass_TextView);

        mForgotPassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });

        mPasswordEditText = (EditText) rootView.findViewById(R.id.password_editText);

        mBtnSubmit = (Button) rootView.findViewById(R.id.submit_btn);

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launchGallery();
                signIn();
            }
        });
    }


    /**
     * This function launch the sign in processing
     */
    public int signIn(){

        String email = mMailEditText.getText().toString();

        String password = mPasswordEditText.getText().toString();

        if( !checkFields(email, password)) return -1;

        SignInTask task = new SignInTask();
        //String url = "https://picshape-engine-develop.herokuapp.com/api/account/login";
        //String url = "http://192.168.0.13:8080/api/account/login";

        // Retrieve URL from preferences
        String route = "/api/account/login";
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        String url = preferences.getString(getString(R.string.pref_url_key),getString(R.string.pref_url_default))+route;

        task.execute(url, email, password, "1");

        return 0;
    }

    /**
     * This function called ForgetPassword function to allow user to retriev his password
     */
    public void forgotPassword(){

        String email = mMailEditText.getText().toString();

        if(email != null){
            SignInTask task = new SignInTask();
            //String url = "https://picshape-engine-develop.herokuapp.com/api/account/login";
            //String url = "http://192.168.0.13:8080/api/account/forgot";

            // Retrieve URL from preferences
            String route = "/api/account/forgot";
            SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(this.getActivity());
            String url = preferences.getString(getString(R.string.pref_url_key),getString(R.string.pref_url_default))+route;

            task.execute(url, email, "", "2");
        }
    }

    /**
     * This function launch Gallery Activity
     */
    public void launchGallery(){
        Intent galleryIntent = new Intent(getActivity(), DeskActivity.class);

        if (galleryIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(galleryIntent);
        }
    }

    /**
     * This function checks if fields are corrects
     */
    public static boolean checkFields(String email, String password){

        if(email ==  null || password == null) return false;
        // TODO email checks and password contains mandatory value
        if(password.length() > 20) return false;
        if(email.length() > 50) return false;

        return true;
    }


    /**
     * Thus function show message to the user
     */
    public void showMsg(String msg){
        Toast.makeText(this.getActivity(),msg,Toast.LENGTH_SHORT).show();
    }


    public void saveAccountInfo(PicshapeAccount userAccount){
        AccountSingleton.getInstance().setAccountLoaded(userAccount);
        PicshapeAccount account;
        if( AccountAccess.saveProfilJSON(getContext(), userAccount, BuildConfig.SAVE_FILE_NAME) ){
            Log.v("SIGNIN FRAGMENT", "SUCCESS to save");
            account = AccountAccess.loadProfilJSON(getContext(), BuildConfig.SAVE_FILE_NAME);
        }
        else{
            showMsg("Failed to save");
            Log.v("SIGNIN FRAGMENT", "Failed to save");
        }
    }


    /**
     * This class manages the http call to webService
     */
    class SignInTask extends AsyncTask<String, Void, Integer> {

        private final String LOG_TAG = SignInFragment.SignInTask.class.getName();
        private PicshapeAccount userAccount;
        private String mode ;
        private boolean forgot = false;

        /**
         * This function is executed befor execution of the task, used to show progress bar
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /**
         * This function execute the call to webService in background
         * @param params
         * @return
         */
        @Override
        protected Integer doInBackground(String[] params){

            if(params.length < 4) return -1;

            String url = params[0];
            String email = params[1];
            String password = params[2];

            mode = params[3];

            if("1".equals(mode)){
                userAccount = AccountAccess.signIn(url, email, password);
            }
            else if("2".equals(mode)){
                forgot = AccountAccess.forgetPassword(url, email);
            }


            return 0;
        }


        /**
         * This function is called after the end of doInBackground method
         * @param result
         */
        @Override
        protected void onPostExecute(Integer result) {

            if("1".equals(mode)){
                if(userAccount != null){
                    saveAccountInfo(userAccount);
                    launchGallery();
                }
                else showMsg("Login failed : Check your email and your password");
            }
            else if("2".equals(mode)){
                if( forgot )showMsg("We sent an email to retrieve your password ");
                else showMsg("Error we can't find your email in our database");
            }


        }

    }

}
