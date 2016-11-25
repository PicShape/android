package com.example.android.picshape.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android.picshape.R;

/**
 * This Class is the base fragment of Connect Activity where users will choose
 * how to connect to PicShape Services
 */
public class SignFragment extends Fragment {


    // View
    Button mSigninBtn, mSignupBtn;

    public SignFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sign, container, false);

        initComp(rootView);

        return rootView;
    }


    public void initComp(View rootView){

        mSigninBtn = (Button) rootView.findViewById(R.id.signin_btn);
        mSignupBtn = (Button) rootView.findViewById(R.id.signup_btn);

        mSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ConnectActivity) getActivity()).launchSignInFragment();
            }
        });

        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ConnectActivity) getActivity()).launchSignUpFragment();
            }
        });
    }

}
