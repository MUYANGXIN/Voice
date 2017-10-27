package com.muyangxin.voice.myView;

/**
 * Created by qxx on 2017/9/13.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.audiofx.Visualizer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.Random;

/**
 * Created by Administrator on 2016/5/14.
 */
public class BoXingView extends View implements Runnable ,Visualizer.OnDataCaptureListener{
    private static final String TAG = "BoXingView";
    Paint paint;
    Canvas canvasM = new Canvas();
    int audioSampleNum = 2 * 16000;
    int widthPixels;
    int heightPixels;
    float points[];
    short audio[];

    private static byte[] boxing=new byte[ 2 * 16000];


    public BoXingView(Context context) {

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

    public BoXingView(Context context, AttributeSet attrs) {
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

    protected void drawWave2(Canvas canvas, byte audio[]) {
        for (int i = 0; i <audio.length - 1; i++) {

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

    }

    protected void drawWave1(Canvas canvas, byte audio[]) {
        float startX, startY, stopX, stopY;
        for (int i = 0; i < audio.length - 1; i=i+2) {


            startX = (float) i / audioSampleNum * widthPixels;

            startY = heightPixels / 2 + (float) audio[i]* (float) audio[i + 1] / 32768 * heightPixels / 2+100;

            stopX = (float) (i ) / audioSampleNum * widthPixels;

            stopY = heightPixels / 2 + (float) audio[i + 1]* (float) audio[i + 1]/ 32768 * heightPixels / 2-100;

            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
    }


    Random r = new Random();

    protected void onDraw(Canvas canvas) {


        if (points == null) {
            points = new float[audioSampleNum * 4];
            audio = new short[audioSampleNum];
        }
//        //这里产生了随机数组传入audio，如果把声音信号量化为数组就好了
//        for (int i = 0; i < audioSampleNum; i++) {
//            audio[i] = (short) (r.nextInt(65536) - 32768);
//
//        }

         drawWave1(canvas, boxing);
        // Log.d(TAG, "stop drawLine");
        //drawWave1(canvas, audio);
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




    public void setVisualizer(Visualizer visualizer) {


                visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[0]);

            visualizer.setDataCaptureListener(this, Visualizer.getMaxCaptureRate() / 2, false, true);

    }
    //这个回调应该采集的是快速傅里叶变换有关的数据
    @Override
    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
//        byte[] model = new byte[fft.length / 2 + 1];
//        if (mDataEn) {
//            model[0] = (byte) Math.abs(fft[1]);
//            int j = 1;
//            for (int i = 2; i < fft.length; ) {
//                model[j] = (byte) Math.hypot(fft[i], fft[i + 1]);
//                i += 2;
//                j++;
//            }
//        } else {
//            for (int i = 0; i < CYLINDER_NUM; i++) {
//                model[i] = 0;
//            }
//        }
//        for (int i = 0; i < CYLINDER_NUM; i++) {
////            final byte a = (byte) (Math.abs(model[CYLINDER_NUM - i]) / levelStep);
////
////            final byte b = mData[i];
////            if (a > b) {
////                mData[i] = a;
////            } else {
////                if (b > 0) {
////                    mData[i]--;
////                }
////            }
//            mData[i] = (byte) Math.abs(fft[i]);
//            Log.d("sssssssssssssss", "dddddddddd" + mData[i]);
//
//        }
        // postInvalidate();//刷新界面

//        Random ran=new Random();
//
//        for (int i=0;i<data.length;i++) {
//            data[i]=ran.nextInt(10);
//
//        }

        for (int i = 0; i < fft.length; i++) {
            boxing[i]=fft[i];

            Log.d("wwwwwwwwwwwwwwwwwww", "wwww"+boxing[i]);
        }

    }



    //这个回调应该采集的是波形数据
    @Override
    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
        // Do nothing...


    }

}