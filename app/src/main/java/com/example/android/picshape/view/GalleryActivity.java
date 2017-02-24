package com.example.android.picshape.view;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.picshape.R;
import com.example.android.picshape.dao.AccountAccess;
import com.example.android.picshape.model.PictureShape;

import java.util.LinkedList;

public class GalleryActivity extends AppCompatActivity {

    // Views
    private TextView mCounterPicTv, mCounterLikeTv;
    private ListView mPicturesListView;
    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        initComp();
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

        mCounterLikeTv = (TextView) findViewById(R.id.counter_like_textView);

        mCounterPicTv = (TextView) findViewById(R.id.counter_pic_textView);

        mPicturesListView = (ListView) findViewById(R.id.pictures_listView);

        mGridView = (GridView) findViewById(R.id.gallery_gridView);
        mGridView.setAdapter(new GalleryAdapter(this));
    }

    /**
     * This function displays account information
     */
    public void displaysAccountInfo(){
        // TODO fill counters

        //TODO fill listView
        LinkedList<PictureShape> gallery = demoGallery();
        PictureAdapter adapterGallery = new PictureAdapter(this, gallery);
        mPicturesListView.setAdapter(adapterGallery);
    }

    // TODELETE TEST PURPOSE -------------------------------------
    public LinkedList<PictureShape> demoGallery(){
        LinkedList<PictureShape> temp = new LinkedList<>();

        PictureShape pic1 = new PictureShape("Pic1", BitmapFactory.decodeResource(getResources(), R.drawable.genie));
        PictureShape pic2 = new PictureShape("Pic2", BitmapFactory.decodeResource(getResources(), R.drawable.genie_output_2));
        PictureShape pic3 = new PictureShape("Pic3", BitmapFactory.decodeResource(getResources(), R.drawable.picshaped));
        PictureShape pic4 = new PictureShape("Pic4", BitmapFactory.decodeResource(getResources(), R.drawable.back_welcome_picshape));

        temp.add(pic1);
        temp.add(pic2);
        temp.add(pic3);
        temp.add(pic4);

        return temp;
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

        AccountAccess.logOff();

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
        Toast.makeText(this, "You're already on the convert screen", Toast.LENGTH_LONG).show();
    }

}
