package com.example.android.picshape.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.example.android.picshape.R;
import com.example.android.picshape.dao.AccountSingleton;
import com.example.android.picshape.dao.PicSingleton;
import com.example.android.picshape.dao.PictureAccess;
import com.example.android.picshape.model.PicshapeAccount;
import com.example.android.picshape.model.PictureShape;

import java.util.ArrayList;

public class SinglePicActivity extends AppCompatActivity {

    //View
    private ImageView mPicture;
    private Button mDeleteButton;
    private TextView mUsername, mPicTitle;
    private PictureShape mPicShape;
    private ProgressBar mDeleteProgressBar;

    private boolean converted = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_pic);


        Intent intent = getIntent();

        mPicShape = intent.getParcelableExtra("pic");

        initComp();

        mUsername.setText( intent.getStringExtra("username") );

        if(mPicShape != null){
            Glide.with(mPicture.getContext())
                    .load(mPicShape.getUrlConverted())
                    .into(mPicture);

            initComp();
        }

    }

    /**
     * This function inialize the component
     */
    public void initComp(){

        mPicture = (ImageView) findViewById(R.id.picture_imageView);

        mPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(converted) {
                    Glide.with(mPicture.getContext())
                            .load(mPicShape.getUrlPhoto())
                            .into(mPicture);
                    converted = false;
                }
                else {
                    Glide.with(mPicture.getContext())
                            .load(mPicShape.getUrlConverted())
                            .into(mPicture);
                    converted = true;
                }
            }
        });

        mPicture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                showSaveDialog();
                return true;
            }
        });


        mUsername = (TextView) findViewById(R.id.name_textView);

        mUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((TextView)v).getText().toString();
                Intent galleryIntent = new Intent(SinglePicActivity.this, GalleryActivity.class);
                galleryIntent.putExtra("account", new PicshapeAccount("-1", name, null, null));

                if (galleryIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(galleryIntent);
                }
            }
        });

        mPicTitle = (TextView) findViewById(R.id.pic_name_textView);


        // If user is the picture owner

        if(mPicShape.getIdUser().equals(AccountSingleton.getInstance().getAccountLoaded().getName())) {
            mDeleteButton = (Button) findViewById(R.id.delete_button);

            mDeleteButton.setVisibility(View.VISIBLE);

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletePicture();
                }
            });
        }

        mDeleteProgressBar = (ProgressBar) findViewById(R.id.single_pic_progressBar);
    }

    /**
     * This function makes call to delete picture
     */
    public void deletePicture(){
        DeleteTask task = new DeleteTask();

        String url = mPicShape.getUrlConverted();
        String token = AccountSingleton.getInstance().getAccountLoaded().getToken();

        task.execute(url, token);
    }


    public void startLoading(){
        mPicture.setVisibility(View.GONE);
        mDeleteProgressBar.setVisibility(View.VISIBLE);

    }

    public void stopLoading(){
        mDeleteProgressBar.setVisibility(View.GONE);
        mPicture.setVisibility(View.VISIBLE);
    }

    public void launchGallery(){
        Intent gallIntent = new Intent(this, GalleryActivity.class);

        startActivity(gallIntent);
    }

    /**
     * This function shows text on screen
     * @param text
     */
    public void showText(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }


    /**
     * This function shows dialog to save picture to memory
     */
    public void showSaveDialog(){
        final CharSequence[] items = { "Save Photo", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(SinglePicActivity.this);

        builder.setTitle("Save Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Save Photo")) {
                    if ( savePicture() ) showText("Done !");
                    else showText("Error");
                }
                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    /**
     * This function saves picture in internal memory
     * @return
     */
    public boolean savePicture(){
        String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                ((GlideBitmapDrawable) mPicture.getDrawable()).getBitmap(),
                "pichsape"+ System.currentTimeMillis(),
                "PicShape");

        if(path != null) return true;

        return false;
    }

    /**
     * This class manages the http call to webService
     */
    class DeleteTask extends AsyncTask<String, Void, Integer> {


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

            if(params.length < 2) return -1;

            String url = params[0];
            String token = params[1];

            if( PictureAccess.deletePicture(url, token) ) return 0;
            else return 1;

        }


        /**
         * This function is called after the end of doInBackground method
         * @param result
         */
        @Override
        protected void onPostExecute(Integer result) {
            stopLoading();
            if(result == 0){
                // Success
                launchGallery();
            }
            else{
                showText("Error, try again");
            }

        }

    }

}
