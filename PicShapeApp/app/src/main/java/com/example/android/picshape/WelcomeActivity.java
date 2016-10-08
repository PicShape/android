package com.example.android.picshape;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class WelcomeActivity extends AppCompatActivity {

    final private String TAG_WELCOME_ACTIVITY = "WELCOME ACTIVITY";

    boolean result ;
    private String userChoosenTask;
    private int REQUEST_CAMERA=0,SELECT_FILE=1;

    //View
    private Button mSelectImageBtn,mSendBtn;
    private EditText mIterationEditText;
    private Spinner mModeSpinner, mFormatSpinner;

    private ImageView mImgChoosen;

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

        mSendBtn = (Button) findViewById(R.id.send_btn);

        mIterationEditText = (EditText) findViewById(R.id.iteration_editText);

        mModeSpinner = (Spinner) findViewById(R.id.spinner_mode);

        mFormatSpinner = (Spinner) findViewById(R.id.spinner_format);

        mImgChoosen = new ImageView(this);

        fillSpinner();

        mSelectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPicture();
            }
        });

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPic();
            }
        });
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


            if(mImgChoosen != null){
                Log.v(TAG_WELCOME_ACTIVITY,"Photo : "+ mImgChoosen.toString());
            }

            //TODO Envoi

            Log.v(TAG_WELCOME_ACTIVITY,"Mode : "+mode+" format "+format+" Iteration "+nbrIteration+" photo "+ mImgChoosen.toString());
        }
        catch (Exception ex){
            Log.e(TAG_WELCOME_ACTIVITY, "Error "+ex.getMessage());
            Toast.makeText(this,"Error : Champ incorrect",Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * Cette fonction récupere l'image à envoyer
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
     * Cette fonction lance la camera pour prendre une photo
     */
    public void launchCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    /**
     * Cette fonction lance l'explorer de fichier pour selectionner une image
     */
    public void launchExplorer(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE);
    }

    /**
     * Cette fonction est appelé aprés l'envoi d'une Intent
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
     * Cette fonction récupère l'image issu de la gallerie
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
        mImgChoosen.setImageBitmap(bm);
    }

    /**
     * Cette fonction enregistre et récupère l'image prise par l'appareil photo
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

        mImgChoosen.setImageBitmap(thumbnail);
    }

}
