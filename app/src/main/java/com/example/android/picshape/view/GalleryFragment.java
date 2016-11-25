package com.example.android.picshape.view;


import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.picshape.R;
import com.example.android.picshape.model.PictureShape;

import java.util.LinkedList;

public class GalleryFragment extends Fragment {

    // Views
    private TextView mCounterPicTv, mCounterLikeTv;
    private ListView mPicturesListView;

    public GalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);

        initComp(rootView);
        displaysAccountInfo();

        return rootView;
    }

    /**
     * This function initialize components of the fragment
     */
    public void initComp(View rootView){

        mCounterLikeTv = (TextView) rootView.findViewById(R.id.counter_like_textView);

        mCounterPicTv = (TextView) rootView.findViewById(R.id.counter_pic_textView);

        mPicturesListView = (ListView) rootView.findViewById(R.id.pictures_listView);
    }

    /**
     * This function displays account information
     */
    public void displaysAccountInfo(){
        // TODO fill counters

        //TODO fill listView
        LinkedList<PictureShape> gallery = demoGallery();
        PictureAdapter adapterGallery = new PictureAdapter(getActivity(), gallery);
        mPicturesListView.setAdapter(adapterGallery);
    }

    // TODELETE TEST PURPOSE -------------------------------------
    public LinkedList<PictureShape> demoGallery(){
        LinkedList<PictureShape> temp = new LinkedList<>();

        PictureShape pic1 = new PictureShape("Pic1", BitmapFactory.decodeResource(getResources(), R.drawable.genie));
        PictureShape pic2 = new PictureShape("Pic2", BitmapFactory.decodeResource(getResources(), R.drawable.genie_output_2));
        PictureShape pic3 = new PictureShape("Pic3", BitmapFactory.decodeResource(getResources(), R.drawable.picshaped));
        PictureShape pic4 = new PictureShape("Pic4", BitmapFactory.decodeResource(getResources(), R.drawable.back_welcome_picshape));

        temp.add(pic1);
        temp.add(pic2);
        temp.add(pic3);
        temp.add(pic4);

        return temp;
    }


}
