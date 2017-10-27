package com.muyangxin.voice.myView;

/**
 * Created by qxx on 2017/9/13.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.Random;

/**
 * Created by Administrator on 2016/5/14.
 */
public class VadView extends View implements Runnable {
    private static final String TAG = "VadView";
    Paint paint;
    Canvas canvasM = new Canvas();
    int audioSampleNum = 2 * 16000;
    int widthPixels;
    int heightPixels;
    float points[];
    short audio[];


    public VadView(Context context) {

        super(context);

        paint = new Paint();


        paint.setColor(Color.BLUE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(1);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                widthPixels = getWidth();
                heightPixels = getHeight();
            }
        });


        new Thread(this).start();

    }

    public VadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //设置画布
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(1);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                widthPixels = getWidth();
                heightPixels = getHeight();
            }
        });
    }

    protected void drawWave2(Canvas canvas, short audio[]) {
        for (int i = 0; i < audioSampleNum - 1; i++) {

            points[4 * i] = (float) i / audioSampleNum * widthPixels;
            points[4 * i + 1] = heightPixels / 2 + (float) audio[i] / 32768 * heightPixels / 2;
            points[4 * i + 2] = (float) (i + 1) / audioSampleNum * widthPixels;
            points[4 * i + 3] = heightPixels / 2 + (float) audio[i + 1] / 32768 * heightPixels / 2;


        }
        //画出图像

        paint.setColor(Color.BLUE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(1);
        canvas.drawColor(Color.BLACK);
        canvas.drawLines(points, paint);
        Log.d("wwwwwwwwwwwwwwwwwww", "wwww");
    }

    protected void drawWave1(Canvas canvas, short audio[]) {
        float startX, startY, stopX, stopY;
        for (int i = 0; i < audio.length - 1; i++) {


            startX = (float) i / audioSampleNum * widthPixels;

            startY = heightPixels / 2 + (float) audio[i] / 32768 * heightPixels / 2;

            stopX = (float) (i + 50) / audioSampleNum * widthPixels;

            stopY = heightPixels / 2 + (float) audio[i + 1] / 32768 * heightPixels / 2;

            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
    }


    Random r = new Random();

    protected void onDraw(Canvas canvas) {


        if (points == null) {
            points = new float[audioSampleNum * 4];
            audio = new short[audioSampleNum];
        }
        //这里产生了随机数组传入audio，如果把声音信号量化为数组就好了
        for (int i = 0; i < audioSampleNum; i++) {
            audio[i] = (short) (r.nextInt(65536) - 32768);

        }

       // drawWave2(canvas, audio);
       // Log.d(TAG, "stop drawLine");
        drawWave1(canvas, audio);
        delay();

    }

    //延迟200ms
    protected void delay() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        }, 500);
    }

    @Override
    public void run() {

    }


}