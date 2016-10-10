package com.example.android.picshape;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Emerik Bedouin
 */

public class ParamActivity extends AppCompatActivity {

    final private String TAG_PARAM_ACTIVITY = "PARAM ACTIVITY";

    //View
    private Button mSelectImageBtn,mSendBtn;
    private EditText mIterationEditText;
    private Spinner mModeSpinner, mFormatSpinner;
    private ImageView mImgChoosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param);

        initComp();
    }

    /**
     * Initialisation of graphics component
     */
    public void initComp(){


        mSelectImageBtn = (Button) findViewById(R.id.select_btn);

        mSendBtn = (Button) findViewById(R.id.send_btn);

        mIterationEditText = (EditText) findViewById(R.id.iteration_editText);

        mModeSpinner = (Spinner) findViewById(R.id.spinner_mode);

        mFormatSpinner = (Spinner) findViewById(R.id.spinner_format);



        fillSpinner();


        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPic();
            }
        });
    }

    /**
     * This function fill spinner with data
     */
    public void fillSpinner(){

        ArrayList<String> modeArray = new ArrayList<String>(Arrays.asList("combo", "triangle", "rect", "ellipse", "circle", "rotatedrect", "beziers", "rotatedellipse", "polygon"));

        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, modeArray);

        ArrayList<String> formatArray = new ArrayList<String>(Arrays.asList("PNG", "JPG", "SVG", "GIF"));
        ArrayAdapter<String> formatAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, formatArray);

        mModeSpinner.setAdapter(modeAdapter);
        mFormatSpinner.setAdapter(formatAdapter);
    }

    /**
     * This function send the pic to PicShape server
     */
    public void sendPic(){

        try {
            int mode = mModeSpinner.getSelectedItemPosition();
            String format = mFormatSpinner.getSelectedItem().toString();
            int nbrIteration = Integer.parseInt(mIterationEditText.getText().toString());

            Log.v(TAG_PARAM_ACTIVITY,"Mode : "+mode+" format "+format+" Iteration "+nbrIteration);

            if(mImgChoosen != null){
                Log.v(TAG_PARAM_ACTIVITY,"Photo : "+ mImgChoosen.toString());
            }

            //TODO Envoi

            LinearLayout paramLayout = (LinearLayout) findViewById(R.id.parameters_layout);
            paramLayout.setVisibility(View.GONE);

            LinearLayout progressLayout = (LinearLayout) findViewById(R.id.progress_layout);
            progressLayout.setVisibility(View.VISIBLE);

        }
        catch (Exception ex){
            Log.e(TAG_PARAM_ACTIVITY, "Error "+ex.getMessage());
            Toast.makeText(this,"Error : Champ incorrect",Toast.LENGTH_SHORT).show();
        }


    }
}
