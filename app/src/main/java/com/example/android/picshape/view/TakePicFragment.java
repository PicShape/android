package com.example.android.picshape.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.android.picshape.R;
import com.example.android.picshape.dao.PicSingleton;

public class TakePicFragment extends Fragment {

    // Views
    private Button mSelectImageBtn, mNextBtn;

    public TakePicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_take_pic, container, false);

        initComp(rootView);

        return rootView;
    }

    /**
     * Initialisation of graphics component
     */
    public void initComp(View rootView){


        mSelectImageBtn = (Button) rootView.findViewById(R.id.select_btn);

        mNextBtn = (Button) rootView.findViewById(R.id.next_btn);

        ImageView temp = (ImageView) rootView.findViewById(R.id.pic_imageView);

        ((DeskActivity) getActivity()).setImgChoosen( (ImageView) rootView.findViewById(R.id.pic_imageView) );

        ImageView imgChoosen = ((DeskActivity) getActivity()).getImgChoosen();

        imgChoosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DeskActivity) getActivity()).showImgChoosen();
                ((DeskActivity) getActivity()).showHideButton(false);
            }
        });

        mSelectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PicSingleton.getInstance().getPicToShape() != null) ((DeskActivity) getActivity()).showHideButton(true);
                else ((DeskActivity) getActivity()).showHideButton(true);
                ((DeskActivity) getActivity()).getPicture();
            }
        });

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DeskActivity) getActivity()).launchParam();
            }
        });
    }


}
