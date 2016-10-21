package com.example.android.picshape.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

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

    }

    /**
     * This function
     */
    public void setImg(){
        if(PicSingleton.getInstance().getPicToShape() != null){

            mResultImg.setImageDrawable(PicSingleton.getInstance().getPicShaped());
        }
    }
}
