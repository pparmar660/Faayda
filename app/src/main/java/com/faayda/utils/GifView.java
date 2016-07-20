package com.faayda.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;

import com.faayda.R;

import java.io.InputStream;


public class GifView extends View {
    public Movie mMovie;
    public long movieStart;
    LinearLayout linearLayout;
    int width;
    int height;

    float density = getResources().getDisplayMetrics().density;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public GifView(Context context, Display display) {
        super(context);
        this.linearLayout = linearLayout;
        initializeView();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        Constants.screenHeight = height;
        Constants.screenWith = width;
    }

    public GifView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView();
    }

    public GifView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeView();
    }


    private void initializeView() {
//R.drawable.loader - our animated GIF

        InputStream is = getContext().getResources().openRawResource(R.drawable.animated_logo);
        mMovie = Movie.decodeStream(is);

    }


    @Override

    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        super.onDraw(canvas);
        long now = android.os.SystemClock.uptimeMillis();
        if (movieStart == 0) {
            movieStart = now;
        }

        if (mMovie != null) {
            int relTime = (int) ((now - movieStart) % mMovie.duration());
            mMovie.setTime(relTime);
            /*getLayoutParams().width*//*getWidth() - mMovie.width()*/
            /*Drawable im = getResources().getDrawable(R.drawable.animated_logo);
            int hi = im.getIntrinsicHeight();
            int wi = im.getIntrinsicWidth();*/
//            mdpi
            if (density <= 1.0) {
                mMovie.draw(canvas, (width / 2) - 176 / 2, (height / 2) - 244 / 2);
            }
//            hdpi
            else if (density == 1.5) {
                mMovie.draw(canvas, (width / 2) - 263 / 2, (height / 2) - 364 / 2);
            }
//        xhdpi
            else if (density == 2.0) {
                mMovie.draw(canvas, (width / 2) - 349 / 2, (height / 2) - 546 / 2);
            }
//            xxhdpi
            else if (density > 2.0) {
                mMovie.draw(canvas, (width / 2) - 590 / 2, (height / 2) - 817 / 2);
            }

            // Log.e("Value : ",mMovie.)

            this.invalidate();
        }
    }

    private int gifId;

    public void setGIFResource(int resId) {
        this.gifId = resId;
        initializeView();

    }


    public int getGIFResource() {
        return this.gifId;

    }

}
