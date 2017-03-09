package com.example.android.picshape.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.picshape.R;
import com.example.android.picshape.dao.AccountAccess;
import com.example.android.picshape.dao.AccountSingleton;
import com.example.android.picshape.dao.PictureAccess;
import com.example.android.picshape.model.PicshapeAccount;
import com.example.android.picshape.model.PictureShape;

import java.util.ArrayList;

public class WallActivity extends AppCompatActivity {

    // View
    private ListView mPicturesListView;
    private ProgressBar mWallProBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);

        initComp();
        displaysAccountInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.wall_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if(null!=searchManager ) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setIconifiedByDefault(false);

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
        else if(id == R.id.action_account){
            account();
            return true;
        }
        else if(id == R.id.action_logoff){
            logoff();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This function initialize components of the fragment
     */
    public void initComp(){


        mPicturesListView = (ListView) findViewById(R.id.pictures_listView);

        mWallProBar = (ProgressBar) findViewById(R.id.wall_progressBar);
    }

    /**
     * This function displays account information
     */
    public void displaysAccountInfo(){
        // TODO fill counters

        //TODO fill listView
        /*LinkedList<PictureShape> gallery = demoGallery();
        PictureAdapter adapterGallery = new PictureAdapter(this, gallery);
        mPicturesListView.setAdapter(adapterGallery);*/
        getUsersPictures();
    }


    /**
     * This function create a task to get profil pictures
     */
    public void getUsersPictures(){

        String name = AccountSingleton.getInstance().getAccountLoaded().getName();


        WallTask task = new WallTask();
        //String url = "https://picshape-engine-develop.herokuapp.com/api/account/login";
        //String url = "http://192.168.0.13:8080/api/account/login";

        // Retrieve URL from preferences
        String route = "/api/";
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(this);
        String url = preferences.getString(getString(R.string.pref_url_key),getString(R.string.pref_url_default))+route;
        String routeUsers = "users/";
        String routePictures = "gallery/photos/";

        task.execute(url,routeUsers, routePictures, name, "1");
    }

    /**
     * This function fill the GridView with picture in parameters
     * @param listShape
     */
    public void fillListPicture(final ArrayList<PictureShape> listShape){

        PictureAdapter adpater = new PictureAdapter(this, listShape);

        mPicturesListView.setAdapter(adpater);

        mPicturesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent singlePicIntent = new Intent(WallActivity.this, SinglePicActivity.class);
                singlePicIntent.putExtra("pic", ((PictureShape) ((parent).getItemAtPosition(position))));

                startActivity(singlePicIntent);
            }
        });
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
     * This function launch PicshapeAccount activity
     */
    public void account(){
        Intent intent = new Intent(this, AccountActivity.class);
        if(intent.resolveActivity(this.getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * This function log off user and launch Sign Activity
     */
    public void logoff(){

        AccountAccess.logOff(this);

        Intent intent = new Intent(this, ConnectActivity.class);
        if(intent.resolveActivity(this.getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void launchGallery(View v){
        Intent galleryIntent = new Intent(this, GalleryActivity.class);

        if (galleryIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(galleryIntent);
        }
    }

    public void launchConvert(View v){
        Intent convertIntent = new Intent(this, ConvertActivity.class);

        if (convertIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(convertIntent);
        }
    }

    public void launchWall(View v){
        Toast.makeText(this, "You're already on the convert screen", Toast.LENGTH_LONG).show();
    }


    public void startLoading(){
        mWallProBar.setVisibility(View.VISIBLE);
    }

    public void stopLoading(){
        mWallProBar.setVisibility(View.GONE);
    }

    /**
     * This class manages the http call to webService
     */
    class WallTask extends AsyncTask<String, Void, Integer> {

        private final String LOG_TAG = SignInFragment.SignInTask.class.getName();
        private PicshapeAccount userAccount;
        private String mode ;
        private ArrayList<PictureShape> listShape;

        /**
         * This function is executed before execution of the task, used to show progress bar
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startLoading();
        }

        /**
         * This function execute the call to webService in background
         * @param params
         * @return
         */
        @Override
        protected Integer doInBackground(String[] params){

            if(params.length < 5) return -1;

            String url = params[0];
            String urlRouteUsers = params[1];
            String urlRoutePictures = params[2];
            String name = params[3];

            mode = params[4];

            if("1".equals(mode)){
                ArrayList<String> nameList = AccountAccess.getAllUsers( (url+urlRouteUsers) );
                ArrayList<PictureShape> listTemp;
                listShape = new ArrayList<>();

                if(nameList != null) {
                    for (int i = 0; i < nameList.size(); i++) {
                        listTemp = PictureAccess.getProfilPicturesList( (url+urlRoutePictures), nameList.get(i));
                        Log.v(LOG_TAG,"List : "+listTemp);
                        if (listTemp != null) listShape.addAll(listTemp);
                    }
                }
            }


            return 0;
        }


        /**
         * This function is called after the end of doInBackground method
         * @param result
         */
        @Override
        protected void onPostExecute(Integer result) {

            stopLoading();

            if("1".equals(mode)){

                if(listShape != null){
                    fillListPicture(listShape);
                }
                else{
                    // NO pic ?
                }
            }

        }

    }



}
