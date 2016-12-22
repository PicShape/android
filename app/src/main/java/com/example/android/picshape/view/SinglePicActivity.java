package com.example.android.picshape.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.picshape.R;

public class SinglePicActivity extends AppCompatActivity {

    //View
    private ImageView mImage;
    private TextView mUsername, mPicTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_pic);
    }

    /**
     * This function inialize the component
     */
    public void initComp(){

    }
}
