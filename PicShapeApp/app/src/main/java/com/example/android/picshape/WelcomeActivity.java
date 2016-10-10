package com.example.android.picshape;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Emerik Bedouin
 */

public class WelcomeActivity extends AppCompatActivity {

    final private String TAG_WELCOME_ACTIVITY = "WELCOME ACTIVITY";

    boolean result ;
    private String userChoosenTask;
    private int REQUEST_CAMERA=0,SELECT_FILE=1;
    private boolean normalImg = true;

    private Bitmap mImgBitMap;

    //View
    private Button mSelectImageBtn,mNextBtn;
    private ImageView mImgChoosen, mImgFixed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //Init des composants graphiques
        initComp();
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
                if (normalImg){
                    mImgChoosen.setImageResource(R.drawable.genie_output_2);
                    normalImg = false;
                }
                else {
                    mImgChoosen.setImageResource(R.drawable.genie);
                    normalImg = true;
                }
            }
        });

        mSelectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
     * This function launch ParamActivity
     */
    public void launchParam(){

        Log.v(TAG_WELCOME_ACTIVITY, "Go to param Activity");

        Intent intentParam = new Intent(this, ParamActivity.class);
        // Impossible de passer l'image par intent, il faut trouver un autre moyen
        //intentParam.putExtra("img", mImgBitMap);
        startActivity(intentParam);

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
                boolean result=Utility.checkPermission(WelcomeActivity.this);
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
     * This function laucnh gallery to get a pic
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
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            }
            else if(requestCode == REQUEST_CAMERA){
                onCaptureImageResult(data);
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
     * This fucntion retrieve picture from the gallery
     * @param data
     */
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mImgBitMap = bm;
        mImgChoosen.setImageBitmap(bm);
    }

    /**
     * This function save picture take by camera and retrieve it
     * @param data
     */
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mImgBitMap = thumbnail;
        mImgChoosen.setImageBitmap(thumbnail);
    }

}
