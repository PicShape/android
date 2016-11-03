package com.example.android.picshape.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.picshape.R;
import com.example.android.picshape.Utility;
import com.example.android.picshape.dao.PicSingleton;
import com.example.android.picshape.utility.ExpandAnimation;

import java.io.IOException;


/**
 * Created by Emerik Bedouin
 */

public class WelcomeActivity extends AppCompatActivity {

    final private String TAG_WELCOME_ACTIVITY = "WELCOME ACTIVITY";

    boolean result ;
    private String userChoosenTask;
    private int REQUEST_CAMERA=0,SELECT_FILE=1;
    private boolean mChoosen = false;
    private Bitmap mImgBitMap;

    //View
    private Button mSelectImageBtn,mNextBtn;
    private ImageView mImgChoosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //Init des composants graphiques
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

        mNextBtn = (Button) findViewById(R.id.next_btn);

        mImgChoosen = (ImageView) findViewById(R.id.pic_imageView);

        mImgChoosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImgChoosen();
                showHideButton(false);
            }
        });

        mSelectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PicSingleton.getInstance().getPicToShape() != null) showHideButton(true);
                else showHideButton(true);
                getPicture();
            }
        });

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchParam();
            }
        });
    }


    /**
     * This function hide/show select/next button
     */
    public void showHideButton(boolean showNext){
        if(showNext){
            ((Button) findViewById(R.id.select_btn)).setVisibility(View.GONE);
            ((Button) findViewById(R.id.next_btn)).setVisibility(View.VISIBLE);
        }
        else{
            ((Button) findViewById(R.id.next_btn)).setVisibility(View.GONE);
            ((Button) findViewById(R.id.select_btn)).setVisibility(View.VISIBLE);
        }
    }

    /**
     * This function show image choosen
     */
    public void showImgChoosen(){

        RelativeLayout welcomeLayout = (RelativeLayout) findViewById(R.id.welcome_relativeLayout);
        RelativeLayout picLayout = (RelativeLayout) findViewById(R.id.pic_layout);
        TextView instructionTv = (TextView) findViewById(R.id.instruction_textView);

        if( !mChoosen ) {
            // Reduction of main layout size
            ExpandAnimation animWelcome = new ExpandAnimation(welcomeLayout, 0.9f, 0.4f);
            animWelcome.setDuration(1000);
            welcomeLayout.startAnimation(animWelcome);

            picLayout.setVisibility(View.VISIBLE);

            instructionTv.setText(getString(R.string.touch_change_text));

            mChoosen = true;
        }
        else {
            // Reduction of main layout size
            ExpandAnimation animWelcome = new ExpandAnimation(welcomeLayout, 0.4f, 0.9f);
            animWelcome.setDuration(300);
            welcomeLayout.startAnimation(animWelcome);

            picLayout.setVisibility(View.GONE);

            PicSingleton.getInstance().setPicToShape(null);

            instructionTv.setText(getString(R.string.begin_select_text));

            mChoosen = false;
        }
    }


    /**
     * This function launch ParamActivity
     */
    public void launchParam(){

        Log.v(TAG_WELCOME_ACTIVITY, "Go to param Activity");

        if(PicSingleton.getInstance().getPicToShape() != null ) {

            Intent intentParam = new Intent(this, ParamActivity.class);
            startActivity(intentParam);
        }
        else{
            Toast.makeText(this, "Error select a pic first !",Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * This function get pictures to send
     */
    public void getPicture(){

        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
        builder.setTitle("Select a Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(WelcomeActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        launchCamera();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        launchExplorer();
                } else if (items[item].equals("Cancel")) {

                    dialog.dismiss();
                }
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                showHideButton(false);
            }
        });

        builder.show();


    }

    /**
     * This function kaucnh camera to take a pic
     */
    public void launchCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    /**
     * This function launch gallery to get a pic
     */
    public void launchExplorer(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE);
    }

    /**
     * This function called when return from an intent
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            showHideButton(false);
        }
        else {

            if (resultCode == RESULT_OK) {
                if (requestCode == SELECT_FILE) {
                    onSelectFromGalleryResult(data);
                    showImgChoosen();
                } else if (requestCode == REQUEST_CAMERA) {
                    onCaptureImageResult(data);
                    showImgChoosen();
                }

                showHideButton(true);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        launchCamera();
                    else if(userChoosenTask.equals("Choose from Library"))
                        launchExplorer();
                } else {
                    //code for deny
                    Toast.makeText(this,"Permission refusée", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    /**
     * This function retrieve picture from the gallery
     * @param data
     */
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {

            try {

                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (IllegalArgumentException ex){
                ex.printStackTrace();
                Toast.makeText(this,"Erro : can't retrieve picture",Toast.LENGTH_SHORT).show();
            }
        }
        mImgBitMap = bm;
        PicSingleton.getInstance().setPicToShape(mImgBitMap);
        mImgChoosen.setImageBitmap(bm);
    }

    /**
     * This function save picture take by camera and retrieve it
     * @param data
     */
    private void onCaptureImageResult(Intent data) {

        Bitmap picCaptured = (Bitmap) data.getExtras().get("data");
        mImgBitMap = picCaptured;
        PicSingleton.getInstance().setPicToShape(mImgBitMap);
        mImgChoosen.setImageBitmap(picCaptured);




    }
    

}
