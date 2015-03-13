package com.fivehundredpx.blurringviewsample;

import android.animation.ValueAnimator;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.fivehundredpx.blurringview.BlurringView;

import java.util.Random;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT > 16) {
            mBlurringView = (BlurringView) findViewById(R.id.blurring_view);
            View blurredView = findViewById(R.id.blurred_view);
            mBlurringView.setBlurredView(blurredView);
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void shuffleImages(View view) {
        int newStartIndex;
        do {
            newStartIndex = mImageIds[mRandom.nextInt(mImageIds.length)];
        } while (newStartIndex == mStartIndex);
        mStartIndex = newStartIndex;

        for (int i = 0; i < mImageViews.length; i++) {
            int drawableId = mImageIds[(mStartIndex + i) % mImageIds.length];
            mImageViews[i].setImageDrawable(getResources().getDrawable(drawableId));
        }
        mBlurringView.invalidate();
    }

    private ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
        public void onAnimationUpdate(ValueAnimator animation) {
            mBlurringView.invalidate();
        }
    };

    public void jiggleImages(View view) {
        if (!mJiggled) {
            for (int i = 0; i < mImageViews.length; i++) {
                mImageViews[i].animate().setUpdateListener(listener).translationX((mRandom.nextFloat() - 0.5f) * 500).translationY((mRandom.nextFloat() - 0.5f) * 500).setDuration(5000).start();
            }
            mJiggled = true;
        } else {
            for (int i = 0; i < mImageViews.length; i++) {
                mImageViews[i].animate().setUpdateListener(listener).translationX(0).translationY(0).setDuration(5000).start();
            }
            mJiggled = false;
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

    private boolean mJiggled;
}
