package com.faayda.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.faayda.utils.Constants;

import java.util.ArrayList;

public class PieChart extends View {
    Context context;

    public int width;

    public int height;

    Paint paint;
    Path path;
    int numberOfItem;

    float startAngle = 0;
    ArrayList<Long> percentageList;

    double gapAngle = 10;


    ArrayList<Float> angleList = new ArrayList<Float>();
    ArrayList<Paint> paintList = new ArrayList<Paint>();
    ArrayList<String> colorList;
    int nonzeroItem;
    ArrayList<Integer> storkList;
    int radiusOfCircle = 0;
    int centerX, centerY;
    int arcTextSize = (int)(Constants.screenWith*(0.04));


    public PieChart(Context context, ArrayList<Long> percentageList, int centerX, int centerY) {
        super(context);
        this.context = context;
        this.percentageList = percentageList;
        this.centerX = centerX;
        this.centerY = centerY;
        angleList = new ArrayList<Float>();
    }

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public PieChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    private void init() {
        radiusOfCircle = (int)(Constants.screenWith*(0.17));
        storkList = new ArrayList<>();
        nonzeroItem = 0;
        colorList = new ArrayList<>();

        colorList.add("#ffd749");
        colorList.add("#ea5445");
        colorList.add("#46a0de");

        int defaultStork =(int)(Constants.screenWith*(0.13));;
        for (int i = 0; i < percentageList.size(); i++) {
            if (!(percentageList.get(i) <= 0))
                nonzeroItem++;
            storkList.add(defaultStork + 20 * i);
        }


        float angle;
        Paint paintOfCircle;
        paintList = new ArrayList<>();
        angleList = new ArrayList<Float>();
        for (int i = 0; i < percentageList.size(); i++) {

            if (nonzeroItem==1)
               gapAngle=0;

            angle = (float) ((percentageList.get(i) * (360 - (gapAngle * nonzeroItem))) / 100);
            //GraphPagerAdapter.gapAngle;
            angleList.add(angle);
            paintOfCircle = new Paint();
            paintOfCircle.setColor(Color.parseColor(colorList.get(i)));
            paintOfCircle.setStrokeWidth(storkList.get(i));
            paintOfCircle.setStyle(Paint.Style.STROKE);
            paintList.add(paintOfCircle);
        }
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init();
        Paint mPaint = new Paint();
        Paint paintOfText;
        mPaint.setColor(Color.parseColor("#ffffff"));
        canvas.drawRect(0, 0, centerX * 2, centerY * 2, mPaint);

        RectF rectForCircle;


        for (int i = 0; i < angleList.size(); i++) {
            if (angleList.get(i) <= 0)
                continue;
            radiusOfCircle = radiusOfCircle + (10 * i);
            rectForCircle = new RectF(getCordinate(centerX, centerY, radiusOfCircle, 0)[0],
                    getCordinate(centerX, centerY, radiusOfCircle, 0)[1],
                    getCordinate(centerX, centerY, radiusOfCircle, 1)[0],
                    getCordinate(centerX, centerY, radiusOfCircle, 1)[1]);
            canvas.drawArc(rectForCircle, startAngle, (angleList.get(i)), false, paintList.get(i));
            startAngle += angleList.get(i) + gapAngle;
            double trad = (startAngle - (angleList.get(i) / 2) - gapAngle) * (Math.PI / 180d); // = 5.1051
            int x = centerX + (int) ((radiusOfCircle + (storkList.get(i))) / 2 * Math.cos(trad));
            int y = centerY + (int) ((radiusOfCircle + (storkList.get(i))) / 2 * Math.sin(trad));

            paintOfText = new Paint();
            paintOfText.setColor(i > 0 ? Color.BLACK : Color.BLACK);
            paintOfText.setTextSize(arcTextSize);
            paintOfText.setTextAlign(Align.CENTER);
            paintOfText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

            canvas.drawText(percentageList.get(i) + " % ", x, y + arcTextSize / 4, paintOfText);
        }

    }

    public int[] getCordinate(int centerX, int centerY, int radius, int pos) {

        int arr[] = new int[2];

        switch (pos) {
            case 0:
                // fro let corner of react
                arr[0] = centerX - radius;
                arr[1] = centerY - radius;
                break;

            case 1:
                // for right corner of react
                arr[0] = centerX + radius;
                arr[1] = centerY + radius;
                break;

            default:
                break;
        }

        return arr;

    }

}
