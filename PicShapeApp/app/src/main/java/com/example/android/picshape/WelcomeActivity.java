package com.example.android.picshape;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class WelcomeActivity extends AppCompatActivity {

    //View
    private Button mSelectImageBtn;
    private EditText mIterationEditText;
    private Spinner mModeSpinner, mFormatSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //Init des composants graphiques
        initComp();
    }


    /**
     * Initialisation des composants graphique de l'activité
     */
    public void initComp(){


        mSelectImageBtn = (Button) findViewById(R.id.select_btn);

        mIterationEditText = (EditText) findViewById(R.id.iteration_editText);

        mModeSpinner = (Spinner) findViewById(R.id.spinner_mode);

        mFormatSpinner = (Spinner) findViewById(R.id.spinner_format);

        fillSpinner();
    }

    /**
     * Cette fonction ajoute les items aux listes déroulante
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
     * Envoi de la photo avec les parametres au serveur
     */
    public void sendPic(){

        try {
            int mode = mModeSpinner.getSelectedItemPosition();
            String format = mFormatSpinner.getSelectedItem().toString();
            int nbrIteration = Integer.parseInt(mIterationEditText.getText().toString());
        }
        catch (Exception ex){
            Log.e("WELCOME ACTIVITY", "Error "+ex.getMessage());
        }

        //TODO getImage

        //TODO Envoi
    }
}
