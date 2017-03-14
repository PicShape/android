package com.example.android.picshape.view;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.android.picshape.R;
import com.example.android.picshape.dao.AccountAccess;
import com.example.android.picshape.dao.AccountSingleton;
import com.example.android.picshape.dao.PictureAccess;
import com.example.android.picshape.model.PicshapeAccount;
import com.example.android.picshape.model.PictureShape;
import com.example.android.picshape.service.UploadPicShapeService;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.ArrayList;

import static android.view.View.GONE;

public class GalleryActivity extends AppCompatActivity {

    private PicshapeAccount mProfil;

    // Views
    private TextView mAccountName, mCounterPicTv, mCounterLikeTv;
    private ListView mPicturesListView;
    private LinearLayout mLoadinglayout;
    private GridView mGridView;
    private GalleryAdapter mAdpaterGallery;
    private ProgressBar mGalleryProBar;
    private CircularImageView mProfilPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        initComp();

        Bundle galleryBundle = getIntent().getExtras();
        PicshapeAccount profil = null;
        if( galleryBundle != null) {
            profil = (PicshapeAccount) galleryBundle.get("account");
        }
        if( profil != null){
            mProfil = profil;
        }
        else{
            mProfil = AccountSingleton.getInstance().getAccountLoaded();
        }


        if(mProfil != null) displaysAccountInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gallery_menu, menu);
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



    @Override
    protected void onResume() {
        super.onResume();
        if(AccountSingleton.getInstance().getAccountLoaded() != null && AccountSingleton.getInstance().getAccountLoaded().equals(mProfil)) {
            registerReceiver(receiver, new IntentFilter(
                    UploadPicShapeService.RECEIVER));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(AccountSingleton.getInstance().getAccountLoaded() != null && AccountSingleton.getInstance().getAccountLoaded().equals(mProfil)) {
            unregisterReceiver(receiver);
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                int resultCode = bundle.getInt(UploadPicShapeService.RESULT);
                if (resultCode == RESULT_OK) {
                    String urlNewPic = bundle.getString(UploadPicShapeService.URLPIC);
                    Toast.makeText(GalleryActivity.this,
                            "Upload complete. Download URI: " + urlNewPic,
                            Toast.LENGTH_LONG).show();
                    if(mLoadinglayout != null) mLoadinglayout.setVisibility(GONE);
                    getAccountPicture(mProfil.getName());
                } else {
                    Toast.makeText(GalleryActivity.this, "Upload failed",
                            Toast.LENGTH_LONG).show();

                }
            }
        }
    };

    /**
     * This function initialize components of the fragment
     */
    public void initComp(){

        mAccountName = (TextView) findViewById(R.id.account_name_textView);

        mCounterLikeTv = (TextView) findViewById(R.id.counter_like_textView);

        mCounterPicTv = (TextView) findViewById(R.id.counter_pic_textView);

        mPicturesListView = (ListView) findViewById(R.id.pictures_listView);

        mGridView = (GridView) findViewById(R.id.gallery_gridView);


        if ( isMyServiceRunning() ) {
            mLoadinglayout = (LinearLayout) findViewById(R.id.loading_layout);
            mLoadinglayout.setVisibility(View.VISIBLE);
        }

        mGalleryProBar = (ProgressBar) findViewById(R.id.gallery_progressBar);

        mProfilPic = (CircularImageView) findViewById(R.id.profile_pic_imageView);
    }

    /**
     * This function check if Upload Service is running
     * @return
     */
    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.example.android.picshape.service.UploadPicShapeService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * This function displays account information
     */
    public void displaysAccountInfo(){

        // TODO fill name & picture
        mAccountName.setText(mProfil.getName());

        // TODO fill counters

        //TODO fill listView
       getAccountPicture(mProfil.getName());
    }


    /**
     * This function create a task to get profil pictures
     */
    public void getAccountPicture(String name){


        GalleryTask task = new GalleryTask();
        //String url = "https://picshape-engine-develop.herokuapp.com/api/account/login";
        //String url = "http://192.168.0.13:8080/api/account/login";

        // Retrieve URL from preferences
        String route = "/api/gallery/photos/";
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(this);
        String url = preferences.getString(getString(R.string.pref_url_key),getString(R.string.pref_url_default))+route;

        task.execute(url, name, "1");
    }


    /**
     * This function fill the GridView with picture in parameters
     * @param listShape
     */
    public void fillGridPicture(final ArrayList<PictureShape> listShape){

        mAdpaterGallery = new GalleryAdapter(this, R.layout.thumb, listShape);

        mGridView.setAdapter(mAdpaterGallery);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent singlePicIntent = new Intent(GalleryActivity.this, SinglePicActivity.class);
                singlePicIntent.putExtra("username",mProfil.getName());
                singlePicIntent.putExtra("pic", ((PictureShape) ((parent).getItemAtPosition(position))));

                startActivity(singlePicIntent);
            }
        });
    }


    public void updateProfilPic(PictureShape pic){

        Glide.with(mProfilPic.getContext())
                .load(pic.getUrlConverted()).asBitmap()
                .into(mProfilPic);

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

    public void launchWall(View v){
        Intent galleryIntent = new Intent(this, WallActivity.class);

        if (galleryIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(galleryIntent);
        }
    }

    public void launchConvert(View v){
        Intent galleryIntent = new Intent(this, ConvertActivity.class);

        if (galleryIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(galleryIntent);
        }
    }

    public void launchGallery(View v){
        Toast.makeText(this, "You're already on the gallery screen", Toast.LENGTH_LONG).show();
    }

    public void startLoading(){
        mGalleryProBar.setVisibility(View.VISIBLE);
    }

    public void stopLoading(){
        mGalleryProBar.setVisibility(View.GONE);
    }

    /**
     * This class manages the http call to webService
     */
    class GalleryTask extends AsyncTask<String, Void, Integer> {

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

            if(params.length < 3) return -1;

            String url = params[0];
            String name = params[1];

            mode = params[2];

            if("1".equals(mode)){
                listShape = PictureAccess.getProfilPicturesList(url, name);
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

                if(listShape != null && listShape.size() > 0){
                    fillGridPicture(listShape);
                    updateProfilPic(listShape.get(0));
                }
                else{
                    // NO pic ?
                }
            }

        }

    }



}
