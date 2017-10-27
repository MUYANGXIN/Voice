//package com.muyangxin.voice.recordClass;
//
//import android.app.Activity;
//import android.content.Context;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v7.app.AppCompatActivity;
//import android.widget.Toast;
//
//import com.muyangxin.voice.MyThread;
//import com.muyangxin.voice.activity.MainActivity;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by qxx on 17-9-30.
// */
//
//public class Play {
////
////    private Activity activity;
////    private MediaPlayer mediaPlayer= new MediaPlayer();
////
////    private Uri fileUri;
////    private int num_song1;
////    public File path;
////    List<File> fileList;
////
////
////
////
////    public void  sendBianLiang(Activity activity) {
////        this.activity = activity;
//////        this.mediaPlayer = mediaPlayer;
//////
//////
//////        fileList = getFileList(STR);
////
////    }
////
////    /***返回录音文件*/
////    public List<File> getFileList(String STR) {
////        try {
////            path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + STR);
/////**待解决问题*/
////            //若文件夹不存在则创建一个新的文件夹，但是好像在path找不到路径的时候就会报错，这是怎么回事？
////
////
////            fileList = allFile(path);//此时获得的是默认存储位置的文件
////        } catch (Exception e) {
////
////            if (!path.exists()) {
//////               File file= path.mkdirs();  //创建文件夹
////
////            }
////            e.printStackTrace();
////        }
////        return fileList;
////    }
////
////    /***返回录音文件名称列表*/
////    public List<String> getVoiceNameList() {
////        List<String> name_list = new ArrayList<>();
////        for (int i = 0; i < fileList.size(); i++) {
////            String name = fileList.get(i).toString();
////            String nm = name.substring(name.lastIndexOf("/") + 1, name.lastIndexOf("."));
////            name_list.add(nm);
////        }
////        return name_list;
////    }
////
////    /***返回文件夹中录音数目*/
////    public int getTotalFileNumber() {
////        return fileList.size();
////    }
////
////    /*** 返回文件夹中所有文件*/
////    public List<File> allFile(File file) {
////        List<File> mFileList = new ArrayList<File>();
////        File[] fileArray = file.listFiles();
////
////
////        for (File f : fileArray) {
////
////
////            if (f.isFile()) {
////                mFileList.add(f);
////            } else {
////                allFile(f);
////            }
////        }
////        return mFileList;
////    }
////
////    /***开始播放*/
////    public void beginPlay() {
////        mediaPlayer.start();
////    }
////
////    /***暂停*/
////    public void pausePlay() {
////        mediaPlayer.pause();
////    }
////
////    /*** 根据传入参数决定播放上一首还是下一首*/
////    public void playNumSong(int num_song) {
////
////
////        mediaPlayer.stop();
////
////        //判断当前是第几首，然后决定播放哪一首，形成循环。
////        if (num_song < fileList.size() && num_song >= 0) {
////            num_song1=num_song;
////        } else if (num_song < 0) {
////            num_song1 = fileList.size() - 1;
////            num_song = fileList.size() - 1;
////
////        } else if (num_song >= fileList.size()) {
////            num_song1 = 0;
////            num_song = 0;
////        }
////
////        fileUri = Uri.parse(fileList.get(num_song).toString());
////
////        // tv_name.setMovementMethod(ScrollingMovementMethod.getInstance());
////
////        try {
////            mediaPlayer = MediaPlayer.create(activity, fileUri);
////            mediaPlayer.setOnCompletionListener(this);
////
////
////            mediaPlayer.start();
////
////
////        } catch (IllegalArgumentException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        } catch (IllegalStateException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        }
////
////
////    }
////
////    /***返回正在播放文件的长度*/
////    public int getPlayingVoiceLength() {
////        int alltime = mediaPlayer.getDuration();
////        return alltime;
////    }
////
////    public boolean isPlaying(){
////
////        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
////
////        return am.isMusicActive(); //判断Music是否在播放
////    }
////
////
////    /***返回播放进度*/
////    public int getCurrentTime(){
////        int CurrentPosition = mediaPlayer.getCurrentPosition();
////        return CurrentPosition;
////    }
////
////    /***返回正在播放的文件名*/
////    public String getPlayingVoiceName() {
////        String name = fileList.get(num_song1).toString();
////        //截取文件名
////
////
////        return name.substring(name.lastIndexOf("/") + 1, name.lastIndexOf("."));
////    }
////
////
////    /*** 重写MediaPlayer.OnCompletionListener的方法，此方法会在每次播放完毕后调用*/
////    @Override
////    public void onCompletion(MediaPlayer mp) {
////
////        //播放首数加一
////        num_song1++;
////
////        playNumSong(num_song1);
////
////    }
////
////
////    /*** 删除录音文件*/
////    public void deleteVoice(File audioFile) {
////        //删除录音文件
////        File file = new File(path, audioFile.getName());
////        if (file.isFile() && file.exists()) {
////            file.delete();
////
////            Toast.makeText(activity, "已删除", Toast.LENGTH_SHORT).show();
////            refreshVoiceList();
////        }
////    }
////
////
////    /*** 刷新录音列表，此方法会在新增录音或删除录音后调用*/
////    public void refreshVoiceList() {
////        fileList = allFile(path);
////
//////
////
//////        MainActivity.AdapterList adapter = new MainActivity.AdapterList(MainActivity.this);
//////        lv_menu.setAdapter(adapter);
////
////        playNumSong(0);
////        mediaPlayer.pause();
////
////
////    }
////
////
////    //时间显示函数,我们获得音乐信息的是以毫秒为单位的，把把转换成我们熟悉的00:00格式
////    public String showTime(int time) {
////        time /= 1000;//总时间除以1000就是总秒数
////
////        int second = time % 60;//总秒数再除以60的余数就是折算后的秒
////        int minute = time / 60;//总秒数除以60后的整数部分就是总分钟数
////        int hour = minute / 60;//总分钟数除以60以后的的整数部分就是总小时数
////        minute %= 60;//总分钟数除以60以后的余数就是折算后的分钟数
////
////        return String.format("%02d:%02d:%02d", hour, minute, second);
////    }
////
//
//}
