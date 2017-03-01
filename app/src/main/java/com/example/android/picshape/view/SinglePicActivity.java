package com.example.android.picshape.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.picshape.R;

public class SinglePicActivity extends AppCompatActivity {

    //View
    private ImageView mPicture;
    private TextView mUsername, mPicTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_pic);

        initComp();

        Intent intent = getIntent();

        String urlPic = intent.getStringExtra("urlPic");

        if(urlPic != null){
            Glide.with(mPicture.getContext())
                    .load(urlPic)
                    .into(mPicture);
        }
    }

    /**
     * This function inialize the component
     */
    public void initComp(){

        mPicture = (ImageView) findViewById(R.id.picture_imageView);

        mUsername = (TextView) findViewById(R.id.name_textView);

        mPicTitle = (TextView) findViewById(R.id.pic_name_textView);

    }


}
