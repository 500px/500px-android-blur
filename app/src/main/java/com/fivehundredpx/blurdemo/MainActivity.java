/*
 * Copyright (C) 2015 500px Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fivehundredpx.blurdemo;

import android.animation.ValueAnimator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.fivehundredpx.android.blur.BlurringView;

import java.util.Random;

/**
 * Demonstrates the use of the blurring view.
 */
public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBlurringView = (BlurringView) findViewById(R.id.blurring_view);
        View blurredView = findViewById(R.id.blurred_view);

        // Give the blurring view a reference to the blurred view.
        mBlurringView.setBlurredView(blurredView);

        mImageViews[0] = (ImageView) findViewById(R.id.image0);
        mImageViews[1] = (ImageView) findViewById(R.id.image1);
        mImageViews[2] = (ImageView) findViewById(R.id.image2);
        mImageViews[3] = (ImageView) findViewById(R.id.image3);
        mImageViews[4] = (ImageView) findViewById(R.id.image4);
        mImageViews[5] = (ImageView) findViewById(R.id.image5);
        mImageViews[6] = (ImageView) findViewById(R.id.image6);
        mImageViews[7] = (ImageView) findViewById(R.id.image7);
        mImageViews[8] = (ImageView) findViewById(R.id.image8);
    }

    public void shuffle(View view) {

        // Randomly pick a different start in the array of available images.
        int newStartIndex;
        do {
            newStartIndex = mImageIds[mRandom.nextInt(mImageIds.length)];
        } while (newStartIndex == mStartIndex);
        mStartIndex = newStartIndex;

        // Update the images for the image views contained in the blurred view.
        for (int i = 0; i < mImageViews.length; i++) {
            int drawableId = mImageIds[(mStartIndex + i) % mImageIds.length];
            mImageViews[i].setImageDrawable(getResources().getDrawable(drawableId));
        }

        // Invalidates the blurring view when the content of the blurred view changes.
        mBlurringView.invalidate();
    }

    private ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
        public void onAnimationUpdate(ValueAnimator animation) {
            // Invalidates the blurring view for animation updates.
            mBlurringView.invalidate();
        }
    };

    public void shift(View view) {
        if (!mShifted) {
            for (int i = 0; i < mImageViews.length; i++) {
                mImageViews[i].animate().setUpdateListener(listener).translationX((mRandom.nextFloat() - 0.5f) * 500).translationY((mRandom.nextFloat() - 0.5f) * 500).setDuration(5000).start();
            }
            mShifted = true;
        } else {
            for (int i = 0; i < mImageViews.length; i++) {
                mImageViews[i].animate().setUpdateListener(listener).translationX(0).translationY(0).setDuration(5000).start();
            }
            mShifted = false;
        }
    }

    private BlurringView mBlurringView;

    private int[] mImageIds = {
            R.drawable.p0, R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4,
            R.drawable.p5, R.drawable.p6, R.drawable.p7, R.drawable.p8, R.drawable.p9
    };

    private ImageView[] mImageViews = new ImageView[9];
    private int mStartIndex;

    private Random mRandom = new Random();

    private boolean mShifted;
}
