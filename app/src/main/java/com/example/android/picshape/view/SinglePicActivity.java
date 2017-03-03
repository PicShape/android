package com.example.android.picshape.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.picshape.R;
import com.example.android.picshape.model.PictureShape;

public class SinglePicActivity extends AppCompatActivity {

    //View
    private ImageView mPicture;
    private TextView mUsername, mPicTitle;
    private PictureShape mPicShape;

    private boolean converted = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_pic);

        initComp();

        Intent intent = getIntent();

        mPicShape = intent.getParcelableExtra("pic");

        if(mPicShape != null){
            Glide.with(mPicture.getContext())
                    .load(mPicShape.getUrlConverted())
                    .into(mPicture);
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


        mUsername = (TextView) findViewById(R.id.name_textView);

        mPicTitle = (TextView) findViewById(R.id.pic_name_textView);

    }


}
