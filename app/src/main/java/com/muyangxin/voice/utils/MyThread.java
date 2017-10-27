package com.muyangxin.voice.utils;

/**
 * Created by qxx on 17-9-30.
 */

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;


/**自定义线程*/
public class MyThread extends Thread {

    // 定义向UI线程发送消息的Handler对象
    Handler handler;



    public MyThread(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {


                new Thread() {
                    @Override
                    public void run() {

                        Message msg = new Message();
                        msg.what = 0x234;
                        msg.obj = "更新UI";

                       // Toast.makeText(activity,"发送成功消息",Toast.LENGTH_SHORT).show();

                        handler.sendMessage(msg);

                    }
                }.start();



    }

}
