package com.example.android.picshape.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.picshape.R;

import com.example.android.picshape.dao.PicSingleton;

public class ResultActivity extends AppCompatActivity {

    private boolean mNormalImg;

    // View
    private ImageView mResultImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initComp();

        setImg();

        mNormalImg = false;
    }

    /**
     * Initialisation of graphics component
     */
    public void initComp(){


        mResultImg = (ImageView) findViewById(R.id.result_imageView);

        mResultImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNormalImg){
                    mResultImg.setImageDrawable(PicSingleton.getInstance().getPicShaped());
                    mNormalImg = false;
                }
                else {
                    mResultImg.setImageBitmap(PicSingleton.getInstance().getPicToShape());
                    mNormalImg = true;
                }
            }
        });

        mResultImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                showSaveDialog();
                return true;
            }
        });

    }

    /**
     * This function show pciture converted in an ImageView
     */
    public void setImg(){
        if(PicSingleton.getInstance().getPicToShape() != null){

            mResultImg.setImageDrawable(PicSingleton.getInstance().getPicShaped());
        }
    }

    /**
     * This function shows text on UI
     * @param text
     */
    public void showToast(String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }

    /**
     * This function shows dialog to save picture to memory
     */
    public void showSaveDialog(){
        final CharSequence[] items = { "Save Photo", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);

        builder.setTitle("Save Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Save Photo")) {
                    if ( savePicture() ) showToast("Done !");
                    else showToast("Error");
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
                ((BitmapDrawable)PicSingleton.getInstance().getPicShaped()).getBitmap(),
                "pichsape"+ System.currentTimeMillis(),
                "PicShape");

        if(path != null) return true;

        return false;
    }


    /**
     * This function launch ConvertActivity
     * @param view
     */
    public void backToConvertActivity(View view){
        Intent convertIntent = new Intent(this, ConvertActivity.class);

        startActivity(convertIntent);
    }
}
