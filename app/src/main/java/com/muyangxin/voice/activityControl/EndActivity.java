package com.muyangxin.voice.activityControl;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by qxx on 2017/5/31.
 */

/**
 * 结束程序
 */
public class EndActivity extends Application {

    private List<Activity> mList = new LinkedList<Activity>();
    private static EndActivity instance;

    private EndActivity() {
    }

    public synchronized static EndActivity getInstance() {
        if (null == instance) {
            instance = new EndActivity();
        }
        return instance;
    }

    //调用此方法将活动添加到mList中，然后可以一次性全部结束
    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //这个命令会使整个JVM停止运行，0代表正常退出，1代表异常退出
//            System.exit(0);
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

}
