package com.example.android.picshape.utility;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

/**
 * Copyright (C) 2016 Emerik Bedouin - All Rights Reserved
 * Created by emerikbedouin on 30/10/2016.
 */

public class ExpandAnimation extends Animation {

    private final float mStartWeight;
    private final float mDeltaWeight;
    private View mView;

    public ExpandAnimation(View view, float startWeight, float endWeight) {
        mStartWeight = startWeight;
        mDeltaWeight = endWeight - startWeight;
        mView = view;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mView.getLayoutParams();
        lp.weight = (mStartWeight + (mDeltaWeight * interpolatedTime));
        mView.setLayoutParams(lp);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}