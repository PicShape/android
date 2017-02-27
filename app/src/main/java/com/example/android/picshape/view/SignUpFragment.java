package com.example.android.picshape.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.android.picshape.BuildConfig;
import com.example.android.picshape.R;
import com.example.android.picshape.dao.AccountAccess;
import com.example.android.picshape.dao.AccountSingleton;
import com.example.android.picshape.model.PicshapeAccount;

import static android.view.View.GONE;


/**
 *
 */
public class SignUpFragment extends Fragment {


    // Views
    private Button mBtnSubmit;
    private EditText mNameEditText, mEmailEditText, mPasswordEditText;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        initComp(rootView);

        return rootView;
    }

    /**
     * This function initialize components of the fragment
     * @param rootView
     */
    public void initComp(View rootView){

        mNameEditText = (EditText) rootView.findViewById(R.id.pseudo_editText);

        mEmailEditText = (EditText) rootView.findViewById(R.id.email_editText);

        mPasswordEditText = (EditText) rootView.findViewById(R.id.password_editText);

        mBtnSubmit = (Button) rootView.findViewById(R.id.submit_btn);

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoader();
                signIn();
            }
        });
    }


    /**
     * This function get the fields and call the account request function
     */
    public int signIn(){

        String name = mNameEditText.getText().toString();

        String email = mEmailEditText.getText().toString();

        String password = mPasswordEditText.getText().toString();

        if ( !checkField(name, email, password)) return -1;

        SignUpTask task = new SignUpTask();
        //String url = "https://picshape-engine-develop.herokuapp.com/api/account/signup";
        //String url = "http://192.168.0.13:8080/api/account/signup";

        // Retrieve URL from preferences
        String route = "/api/account/signup";
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        String url = preferences.getString(getString(R.string.pref_url_key),getString(R.string.pref_url_default))+route;

        task.execute(url, email, name, password);

        return 0;

    }

    /**
     * This function launch Gallery Activity
     */
    public void launchDesk(){
        Intent deskIntent = new Intent(getActivity(), GalleryActivity.class);

        if (deskIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(deskIntent);
        }
    }

    /**
     * This function shows loader instead of account info
     */
    public void showLoader(){

        LinearLayout formLayout = (LinearLayout) getActivity().findViewById(R.id.signin_form_layout);
        formLayout.setVisibility(GONE);

        RelativeLayout loaderLayout = (RelativeLayout) getActivity().findViewById(R.id.loader_layout);
        loaderLayout.setVisibility(View.VISIBLE);

    }

    /**
     * This function shows loader instead of account info
     */
    public void hideLoader(){

        RelativeLayout loaderLayout = (RelativeLayout) getActivity().findViewById(R.id.loader_layout);
        loaderLayout.setVisibility(View.GONE);

        LinearLayout formLayout = (LinearLayout) getActivity().findViewById(R.id.signin_form_layout);
        formLayout.setVisibility(View.VISIBLE);

    }

    /**
     * This function checks if fields are corrects
     */
    public static boolean checkField(String name, String email, String password){

        if(name == null || email ==  null || password == null) return false;
        // TODO email checks and password contains mandatory value
        if(name.length() > 20) return false;
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

    /**
     * This class manages the http call to webService
     */
    class SignUpTask extends AsyncTask<String, Void, Integer> {

        private final String LOG_TAG = SignInFragment.SignInTask.class.getName();
        private PicshapeAccount userAccount;

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
            String url = params[0];
            String email = params[1];
            String name = params[2];
            String password = params[3];


            Log.v("SIGN IN FRAGMENT","SIGNIN email "+email+" password "+password);

            userAccount = AccountAccess.signUp(url, email, name, password);

            return 0;
        }


        /**
         * This function is called after the end of doInBackground method
         * @param result
         */
        @Override
        protected void onPostExecute(Integer result) {

            if(userAccount != null){
                AccountSingleton.getInstance().setAccountLoaded(userAccount);
                AccountAccess.saveProfilJSON(getContext(), userAccount, BuildConfig.SAVE_FILE_NAME);
                showMsg("Picshape Account creation success");

                launchDesk();

            }
            else {
                showMsg("PicshapeAccount creation failed : Try again later");
                hideLoader();
            }

        }

    }



}
