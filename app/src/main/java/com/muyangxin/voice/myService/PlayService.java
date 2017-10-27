package com.muyangxin.voice.myService;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.muyangxin.voice.R;
import com.muyangxin.voice.activity.FilePath;
import com.muyangxin.voice.activity.PlayActivity;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PlayService extends Service implements MediaPlayer.OnCompletionListener {

    //    public String musicName = "";
    public final IBinder binder = new MyBinder();

    //public final static String FILE = "/record/";
    public final static String FILE = "/Recordings/";

    //获取上次的musicIndex
    //正在播放的索引
    public static int musicIndex;

    public static MediaPlayer mp = new MediaPlayer();

    private static boolean isLoop = false;

    public class MyBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;//返回服务本身
        }
    }


    private final PlayServiceListener playListener = new PlayServiceListener() {
        @Override
        public void changeName(String name) {
            getNotificationManager().notify(1, getNotification(name));
        }
    };

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    /***创建前台服务*/
    private Notification getNotification(String name) {
        Intent intent1 = new Intent(this, PlayActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);
        //启用前台服务，主要是startForeground()
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
        notification.setContentTitle("正在播放");
        notification.setContentText(name);
        notification.setWhen(System.currentTimeMillis());
        notification.setContentIntent(pendingIntent);
        notification.setSmallIcon(R.drawable.a);
        return notification.build();
    }

    @Override
    public void onCreate() {

//        Intent intent1 = new Intent(this, PlayActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);
//        //启用前台服务，主要是startForeground()
//        Notification notification= new NotificationCompat.Builder(this)
//                .setContentTitle("正在播放")
//                .setContentText(getVoiceName())
//                .setWhen(System.currentTimeMillis())
//                .setContentIntent(pendingIntent)
//                .setSmallIcon(R.drawable.a)
//
//                .build();
//
//        startForeground(1, notification);

        //获取上次的musicIndex
        SharedPreferences editor = getSharedPreferences("recordNum", MODE_PRIVATE);
        musicIndex = editor.getInt("Index", 0);

        if (musicIndex < musicDir.length) {
        } else {
            musicIndex = 0;
            //存储正在播放曲子
            SharedPreferences.Editor editor1 = getSharedPreferences("recordNum", MODE_PRIVATE).edit();
            editor1.putInt("Index", musicIndex);
            editor1.apply();

        }
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //每次调用startService都会调用此方法
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }


    public PlayService() {}


    // public static File path;
    public static List<File> fileList;
    private static String[] musicDir = setMusicDir();

    /***返回录音文件的字符串形式*/
    public static String[] setMusicDir() {
        FilePath.createDirectory(FILE);

        fileList = FilePath.getFileList(FILE);
        String[] musicDir1 = new String[fileList.size()];

        for (int i = 0; i < fileList.size(); i++) {
            musicDir1[i] = fileList.get(i).toString();

        }
        return musicDir1;
    }

    /***返回录音文件名称*/
    public String getVoiceName() {
        if (musicIndex < musicDir.length) {
            String name = musicDir[musicIndex];
            String nm = name.substring(name.lastIndexOf("/") + 1, name.lastIndexOf("."));
            return nm;
        } else return "录音为空";

    }

    /***返回录音一共的首数*/
    public String getTotalNumberSongs() {
        musicDir = setMusicDir();
        return String.valueOf(musicDir.length);
    }

//    /***仅仅返回首数，供录音完成后对比是否有增删*/
//    public int getCurrentNumSongs() {
//        File[] fileArray = path.listFiles();
//
//        int i = 0;
//        for (File f : fileArray) {
//
//
//            if (f.isFile()) {
//                i++;
//            } else {
//                getCurrentNumSongs();
//            }
//        }
//        return i;
//    }

    /***循环设置*/
    public void setLoop(boolean isLoop) {
        this.isLoop = isLoop;
    }

    public boolean getIsLoop() {
        return isLoop;
    }


    public void prepare() {

        try {
            mp.reset();

            mp.setDataSource(musicDir[musicIndex]);
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /***以字符串数组形式返回名称列表*/
    public String[] getNameList() {
        musicDir = setMusicDir();
        String[] namelist = new String[musicDir.length];
        for (int i = 0; i < musicDir.length; i++) {
            String name = musicDir[i];
            String nm = name.substring(name.lastIndexOf("/") + 1, name.lastIndexOf("."));
            namelist[i] = nm;
        }
        return namelist;
    }

    /***返回播放状态*/
    public boolean isPlay() {
        return mp.isPlaying();
    }


    /*** 删除录音文件*/
    public void deleteThisVoice(int position) {
        //删除录音文件File audioFile
        File file = new File(FilePath.getFilePath() + FILE, fileList.get(position).getName());
        try {
            if (file.isFile() && file.exists()) {
                file.delete();

                Toast.makeText(PlayService.this, "已删除", Toast.LENGTH_SHORT).show();
                //refreshVoiceList();
            } else {
                Toast.makeText(PlayService.this, "删除失败", Toast.LENGTH_SHORT).show();
            }

            //删除之后重新赋值音乐文件列表
            musicDir = setMusicDir();
            if (musicIndex >= 0 && musicIndex < musicDir.length - 1) {
                mp.stop();
                try {
                    mp.reset();

//                    musicIndex ++;
                    mp.setDataSource(musicDir[musicIndex]);

                    mp.prepare();
                    mp.seekTo(0);
//                    startPlay();
                } catch (Exception e) {
                    Log.d("hint", "can't jump next music");
                    e.printStackTrace();
                }
            } else if (musicIndex >= musicDir.length - 1 && musicDir.length != 0) {
                mp.stop();
                try {
                    mp.reset();

                    musicIndex = 0;
                    //存储正在播放曲子
                    SharedPreferences.Editor editor = getSharedPreferences("recordNum", MODE_PRIVATE).edit();
                    editor.putInt("Index", musicIndex);
                    editor.apply();

                    mp.setDataSource(musicDir[musicIndex]);

                    mp.prepare();
                    mp.seekTo(0);
//                    startPlay();
                } catch (Exception e) {
                    Log.d("hint", "can't jump next music");
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            Toast.makeText(PlayService.this, "删除失败", Toast.LENGTH_SHORT).show();

        }


    }


    /***彻底刷新*/
    public void absolutelyUpdate() {

        musicDir = setMusicDir();

        mp.stop();//停止播放
        try {
            mp.reset();
            //刷新音乐列表

            musicIndex = 0;//重置，从第一首开始播放
            mp.setDataSource(musicDir[musicIndex]);

            mp.prepare();
            mp.seekTo(0);
            startPlay();
            mp.pause();


        } catch (Exception e) {

            try {
                mp.reset();
                //刷新音乐列表

                musicIndex = 0;//重置，从第一首开始播放
                mp.setDataSource(musicDir[musicIndex]);

                mp.prepare();
                mp.seekTo(0);
                mp.start();
                mp.setOnCompletionListener(PlayService.this);
                mp.pause();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Log.d("hint", "can't jump next music");
            e.printStackTrace();
        }
    }

    /***播放*/
    public void play() {
        if (mp.isPlaying()) {
//            mp.pause();
        } else {


            startPlay();


        }
    }

    /***开始播放*/
    private void startPlay() {
        mp.start();

        //更新前台服务
        playListener.changeName(getVoiceName());

        mp.setOnCompletionListener(PlayService.this);
    }

    /***暂停*/
    public void pause() {
        if (mp.isPlaying()) {
            mp.pause();
        }
    }

    /***结束*/
    public void stop() {
        if (mp != null) {
            mp.stop();
            try {
                mp.prepare();
                mp.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /***下一首*/
    public void nextMusic() {
        if (mp != null && musicIndex < musicDir.length - 1) {
            mp.stop();
            try {
                mp.reset();

                musicIndex++;
                //存储正在播放曲子
                SharedPreferences.Editor editor = getSharedPreferences("recordNum", MODE_PRIVATE).edit();
                editor.putInt("Index", musicIndex);
                editor.apply();

                mp.setDataSource(musicDir[musicIndex]);

                mp.prepare();
                mp.seekTo(0);
                startPlay();
            } catch (Exception e) {
                Log.d("hint", "can't jump next music");
                e.printStackTrace();
            }
        } else if (mp != null && musicIndex == fileList.size() - 1) {
            mp.stop();
            try {
                mp.reset();

                musicIndex = 0;
                //存储正在播放曲子
                SharedPreferences.Editor editor = getSharedPreferences("recordNum", MODE_PRIVATE).edit();
                editor.putInt("Index", musicIndex);
                editor.apply();

                mp.setDataSource(musicDir[musicIndex]);
                mp.prepare();
                mp.seekTo(0);
                startPlay();
            } catch (Exception e) {
                Log.d("hint", "can't jump next music");
                e.printStackTrace();
            }
        }
    }

    /***上一首*/
    public void preMusic() {
        if (mp != null && musicIndex > 0) {
            mp.stop();
            try {
                mp.reset();

                musicIndex--;
//                //存储正在播放曲子
//                SharedPreferences.Editor editor = getSharedPreferences("recordNum", MODE_PRIVATE).edit();
//                editor.putInt("Index", musicIndex);
//                editor.apply();

                mp.setDataSource(musicDir[musicIndex]);

                mp.prepare();
                mp.seekTo(0);
                startPlay();
            } catch (Exception e) {
                Log.d("hint", "can't jump pre music");
                e.printStackTrace();
            }
        } else if (mp != null && musicIndex == 0) {
            mp.stop();
            try {
                mp.reset();

                musicIndex = fileList.size() - 1;
//                //存储正在播放曲子
//                SharedPreferences.Editor editor = getSharedPreferences("recordNum", MODE_PRIVATE).edit();
//                editor.putInt("Index", musicIndex);
//                editor.apply();

                mp.setDataSource(musicDir[musicIndex]);

                mp.prepare();
                mp.seekTo(0);
                startPlay();
            } catch (Exception e) {
                Log.d("hint", "can't jump next music");
                e.printStackTrace();
            }
        }
    }

    /***播放指定的这一首*/
    public void playThisMusic(int i) {
        musicDir = setMusicDir();

        if (mp != null && i >= 0 && i < musicDir.length) {
            mp.stop();
            try {
                mp.reset();

                musicIndex = i;
                mp.setDataSource(musicDir[musicIndex]);

                mp.prepare();
                mp.seekTo(0);
                startPlay();
            } catch (Exception e) {
                Log.d("hint", "can't jump pre music");
                e.printStackTrace();
            }
        }
    }

    /***自动播放下一首*/
    @Override
    public void onCompletion(MediaPlayer mp) {
//        if (mp != null && musicIndex < fileList.size() - 1) {
//            mp.stop();
//            try {
//                mp.reset();
//
//                musicIndex++;
//                mp.setDataSource(musicDir[musicIndex]);
//
//                mp.prepare();
//                mp.seekTo(0);
//                startPlay();
//            } catch (Exception e) {
//                Log.d("hint", "can't jump next music");
//                e.printStackTrace();
//            }
//        } else if (mp != null && musicIndex == fileList.size() - 1) {
//            mp.stop();
//            try {
//                mp.reset();
//
//                musicIndex = 0;
//                mp.setDataSource(musicDir[musicIndex]);
//
//                mp.prepare();
//                mp.seekTo(0);
//                startPlay();
//            } catch (Exception e) {
//                Log.d("hint", "can't jump next music");
//                e.printStackTrace();
//            }
//        }


        //通知活动更新UI
        if (isLoop) {
            playThisMusic(musicIndex);
        } else {
            nextMusic();
        }
        onChangeListener.haveChange();
    }

    /***定义一个接口*/
    public interface OnChangeListener {
        void haveChange();
    }

    /*** 更新进度的回调接口*/
    private OnChangeListener onChangeListener;

    /*** 注册回调接口的方法，供外部调用
     * @param onChangeListener
     */
    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    @Override
    public void onDestroy() {

        SharedPreferences.Editor editor1 = getSharedPreferences("recordNum", MODE_PRIVATE).edit();
        editor1.putBoolean("isPlay", false);
        editor1.apply();


        super.onDestroy();
    }

}
