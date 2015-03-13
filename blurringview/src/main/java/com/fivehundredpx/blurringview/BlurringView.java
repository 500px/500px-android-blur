package com.fivehundredpx.blurringview;

/**
 * Created by jun on 15-03-12.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jun on 15-02-20.
 */
public class BlurringView extends View {

    public static final int DEFAULT_RADIUS = 15; // maximum allowed is 25

    private static final int DOWNSAMPLE_FACTOR = 8;
    private boolean mBitmapScalingFailed;
    private Bitmap mBitmapToBlur, mBlurredBitmap;
    private Canvas mBlurringCanvas;
    private View mBlurredView;
    private RenderScript mRenderScript;
    private ScriptIntrinsicBlur mBlurScript;
    private Allocation mBlurInput, mBlurOutput;

    public BlurringView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBitmapScalingFailed = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBlurredView != null) {
            if (!mBitmapScalingFailed) {
                prepare();
                mBlurredView.draw(mBlurringCanvas);
                blur();
                canvas.save();
                canvas.translate(mBlurredView.getX() - getX(), mBlurredView.getY() - getY());
                canvas.scale(DOWNSAMPLE_FACTOR, DOWNSAMPLE_FACTOR);
                canvas.drawBitmap(mBlurredBitmap, 0, 0, null);
                canvas.restore();
                canvas.drawColor(0xAAFFFFFF, PorterDuff.Mode.OVERLAY);
            }
        }
    }

    public void setBlurredView(View blurredView) {
        setBlurredView(blurredView, DEFAULT_RADIUS);
    }

    public void setBlurredView(View blurredView, int radius) {
        mBlurredView = blurredView;
        initializeRenderScript(getContext(), radius);
    }

    private void initializeRenderScript(Context context, int radius) {
        if (Build.VERSION.SDK_INT > 16) {
            mRenderScript = RenderScript.create(context);
            mBlurScript = ScriptIntrinsicBlur.create(mRenderScript, Element.U8_4(mRenderScript));
            mBlurScript.setRadius(radius);
        }
    }

    private void prepare() {
        final int width = mBlurredView.getWidth();
        final int height = mBlurredView.getHeight();
        if (mBlurringCanvas == null || mBitmapToBlur.getWidth() != width || mBitmapToBlur.getHeight() != height) {

            int scaledWidth = width / DOWNSAMPLE_FACTOR;
            int scaledHeight = height / DOWNSAMPLE_FACTOR;

            // The following manipulation of scale is to get rid of some artifacts at the edge.
            scaledWidth = scaledWidth - scaledWidth % 4 + 4;
            scaledHeight = scaledHeight - scaledHeight % 4 + 4;

            mBitmapToBlur = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
            if (mBitmapToBlur == null) {
                mBitmapScalingFailed = true;
                return;
            }

            mBlurredBitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
            if (mBlurredBitmap == null) {
                mBitmapScalingFailed = true;
                return;
            }

            mBlurringCanvas = new Canvas(mBitmapToBlur);
            mBlurringCanvas.scale(1f/ DOWNSAMPLE_FACTOR, 1f/ DOWNSAMPLE_FACTOR);
            if (Build.VERSION.SDK_INT > 16) {
                mBlurInput = Allocation.createFromBitmap(mRenderScript, mBitmapToBlur, Allocation.MipmapControl.MIPMAP_NONE,
                        Allocation.USAGE_SCRIPT);
                mBlurOutput = Allocation.createTyped(mRenderScript, mBlurInput.getType());
            }
        }
    }

    @SuppressLint("NewApi")
    public void blur() {
        if (Build.VERSION.SDK_INT > 16) {
            mBlurInput.copyFrom(mBitmapToBlur);
            mBlurScript.setInput(mBlurInput);
            mBlurScript.forEach(mBlurOutput);
            mBlurOutput.copyTo(mBlurredBitmap);
        }
    }
}
