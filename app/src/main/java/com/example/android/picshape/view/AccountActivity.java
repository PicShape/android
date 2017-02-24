package com.example.android.picshape.view;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.picshape.R;
import com.example.android.picshape.dao.AccountAccess;
import com.example.android.picshape.dao.AccountSingleton;


public class AccountActivity extends AppCompatActivity {

    //Views
    private Button mModifyBtn, mDeleteBtn;
    private EditText mPseudoEditText, mEmailEditText, mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        initViews();
    }

    public void initViews(){

        mPseudoEditText = (EditText) findViewById(R.id.pseudo_editText);

        mEmailEditText = (EditText) findViewById(R.id.email_editText);

        mPasswordEditText = (EditText) findViewById(R.id.password_editText);

        mModifyBtn = (Button) findViewById(R.id.modify_button);

        mModifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyAccountInfo();
            }
        });

        mDeleteBtn = (Button) findViewById(R.id.delete_button);

        mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
            }
        });
    }

    public void modifyAccountInfo(){
        String password = mPasswordEditText.getText().toString();

        String token = AccountSingleton.getInstance().getAccountLoaded().getToken();

        AccountActivity.AccountTask task = new AccountActivity.AccountTask();
        //String url = "https://picshape-engine-develop.herokuapp.com/api/account/";
        //String url = "http://192.168.0.13:8080/api/account/";

        // Retrieve URL from preferences
        String route = "/api/account/";
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(this);
        String url = preferences.getString(getString(R.string.pref_url_key),getString(R.string.pref_url_default))+route;

        task.execute(url, token, password, "1");
    }

    public void deleteAccount(){
        String password = mPasswordEditText.getText().toString();

        String token = AccountSingleton.getInstance().getAccountLoaded().getToken();

        AccountActivity.AccountTask task = new AccountActivity.AccountTask();
        //String url = "https://picshape-engine-develop.herokuapp.com/api/account/";
        //String url = "http://192.168.0.13:8080/api/account/";

        // Retrieve URL from preferences
        String route = "/api/account/";
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(this);
        String url = preferences.getString(getString(R.string.pref_url_key),getString(R.string.pref_url_default))+route;

        task.execute(url, token, "", "2");
    }

    /**
     * Thus function show message to the user
     */
    public void showMsg(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * This class manages the http call to webService
     */
    class AccountTask extends AsyncTask<String, Void, Integer> {

        private boolean modified = false;
        private boolean deleted = false;
        private String mode;

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
            String token = params[1];
            String password = params[2];
            mode = params[3];

            if("1".equals(mode) ) modified = AccountAccess.changeAccountInfo(url, password, token);
            else if ("2".equals(mode)) deleted = AccountAccess.deleteAccount(url, token);



            return 0;
        }


        /**
         * This function is called after the end of doInBackground method
         * @param result
         */
        @Override
        protected void onPostExecute(Integer result) {

            if ("1".equals(mode)) {
                if (modified) showMsg("Modification succeed");
                else showMsg("Modification failed : Try again later");
            }
            else if("2".equals(mode)){
                if (deleted) showMsg("Deletion succeed");
                else showMsg("Deletion failed : Try again later");
            }





        }

    }
}
