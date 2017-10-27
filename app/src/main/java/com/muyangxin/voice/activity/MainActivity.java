//package com.muyangxin.voice.activity;
//
//
//import android.app.AlertDialog;
//
//import android.content.Context;
//import android.content.DialogInterface;
//
//import android.content.Intent;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.Environment;
//import android.os.Handler;
//import android.support.design.widget.NavigationView;
//
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.os.Bundle;
//
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.SeekBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//
//import com.muyangxin.voice.R;
//
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//
//public class MainActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnCompletionListener {
//
//
//    private static MediaPlayer mediaPlayer;
//
//
//    private DrawerLayout mDrawerLayout;
//    //尝试实现侧边栏点击
//    private NavigationView navView;
//
//    private Button btn_Start, btn_Stop;
//    //    private Button btn_giveUp;
//    private int  isPlay = 0;//stop =>0  ;   playing =>1  ;  pause =>  2  ;
//
//    private Button btn_Up, btn_Next;
//
//    private TextView tv_totalsong;
//
//    //名称
//    private TextView tv_name;
//
//    private ListView lv_menu2;
//    private List<String> name_list = new ArrayList<>();
//
//    private List<File> voiceList;
//
//    public List<File> fileList;
//    private int num_song = 0;
//    private int num_song1;
//
//    //进度条
//    private SeekBar ProceseekBar2;
//    private TextView nowPlayTime, allTime;
//
//
//    public File path;
//
//    private Uri fileUri;
//    // private Play play;
//
////    public MainActivity(Activity activity, String STR, MediaPlayer mediaPlayer) {
////        super(activity, STR, mediaPlayer);
////
////    }
//
////    private Handler handlerUI;
////    public MyThread myThread;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
////        //检测权限
////        PermissionsChecker mPermissionsChecker; // 权限检测器
////        mPermissionsChecker = new PermissionsChecker(this);
////        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
////            verifyStoragePermissions(this);
////        }
//
//
////在文件夹的子文件中查找指定名称的文件
////
////        // Environment.getDataDirectory() +    File f = new File(Environment.getExternalStorageDirectory() + "/Recordings/");
////        String keyword="com";
////        String result = "";
////        File[] files = new File("/").listFiles();
////        for (File file : files) {
////            if (file.getName().indexOf(keyword) >= 0) {
////                result += file.getPath() + "\n";
////
////            }
////        }
////        Log.d("one",""+result);
////        if (result.equals("")){
////            Log.d("one","lllllllllllll"+result);
////        }
////
////
////        String keyword2="s";
////        String result2 = "";
////        File[] files2 = new File("/mnt/sdcard0/").listFiles();
////        for (File file : files2) {
////            if (file.getName().indexOf(keyword2) >= 0) {
////                result2 += file.getPath() + "\n";
////
////            }
////        }
////        Log.d("two",""+result2);
////        if (result2.equals("")){
////            Log.d("two","lllllllllllll"+result2);
////        }
////
//
//        try {
//            isPlay=savedInstanceState.getInt("isPlay");
//        } catch (Exception e) {
//            isPlay=0;
//        }
//
//
//        init();
//
//
////        /**接收信息 */
////        handlerUI = new Handler() {
////            public void handleMessage(Message msg) {
////
////
////                if (msg.what == 0x234) {
////                    refreshUI();
////
////                }
////            }
////        };
//
//
////        myThread=new MyThread(handlerUI);
////        new Thread(myThread).start();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//
//        refreshUI();
//        Toast.makeText(MainActivity.this, "gengxin onresume" + isPlay, Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.d("hhhhhhhhhhhhh","hhhhhhhhhhh"+isPlay);
//        Toast.makeText(MainActivity.this, "gengxin ondestory" + isPlay, Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        Toast.makeText(MainActivity.this, "onRestoreInstanceState" + isPlay, Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//       outState.putInt("isPlay", isPlay);
//        Toast.makeText(MainActivity.this, "onSaveInstanceState" + isPlay, Toast.LENGTH_SHORT).show();
//
//    }
//
//    /**
//     * 初始化各种按钮
//     */
//    private void init() {
//
//        //初始化录音文件
//
//        voiceList = getFileList("/Recordings/");
//        name_list = getVoiceNameList();
//
//        mediaPlayer = new MediaPlayer();
//
//        name_list = getVoiceNameList();
//
//        //总录音数目
//        tv_totalsong = (TextView) findViewById(R.id.tv_totalsongs);
//        tv_totalsong.setText("目前一共有" + getTotalFileNumber() + "首录音");
//
//
//        //开始暂停键
//        btn_Start = (Button) findViewById(R.id.btn_start);
//        btn_Stop = (Button) findViewById(R.id.btn_Stop);
//        btn_Start.setOnClickListener(this);
//        btn_Stop.setOnClickListener(this);
//
//
//        //上一首下一首键
//        btn_Up = (Button) findViewById(R.id.btn_Up);
//        btn_Next = (Button) findViewById(R.id.btn_Next);
//        btn_Next.setOnClickListener(this);
//        btn_Up.setOnClickListener(this);
//
//        //名称与目录
//        tv_name = (TextView) findViewById(R.id.tv_name);
//        tv_name.setText(name_list.get(0));
//        tv_name.setOnClickListener(this);
//
//        //侧边栏，暂时不用
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        navView = (NavigationView) findViewById(R.id.nav_view);
//
//        //录音列表
//        lv_menu2 = (ListView) findViewById(R.id.lv_menu2);
//
//        AdapterList adapter = new AdapterList(this);
//        // lv_menu.setAdapter(adapter);
//        lv_menu2.setAdapter(adapter);
//
//
//        ProceseekBar2 = (SeekBar) findViewById(R.id.sek_process_play);
//
//
//        ProceseekBar2.setOnSeekBarChangeListener(new ProcessBarListener());
//
//
//        if (isPlay==0|isPlay==2) {
//            playNumSong(num_song);
//
//            pausePlay();
//            Toast.makeText(MainActivity.this, "gengxin  pause22 play" + isPlay, Toast.LENGTH_SHORT).show();
//
//
//            btn_Start.setEnabled(true);
//            btn_Stop.setEnabled(false);
//        }
//
//        //进度条
//        nowPlayTime = (TextView) findViewById(R.id.tv_currentLength);
//        nowPlayTime.setText("" + showTime(0));
//        allTime = (TextView) findViewById(R.id.tv_totalLength);
//        allTime.setText("" + showTime(getPlayingVoiceLength()));
//    }
//
//
//    @Override
//    public void onClick(View v) {
//
//
//        if (v == btn_Start) {
//
//            beginPlay();
//
//
//            btn_Start.setEnabled(false);
//            btn_Stop.setEnabled(true);
//
//        } else if (v == btn_Stop) {
//            pausePlay();
//
//            btn_Start.setEnabled(true);
//            btn_Stop.setEnabled(false);
//
//
//        } else if (v == btn_Next) {
//            num_song++;
//            playNumSong(num_song);
//            refreshUI();
//            strartBarUpdate();
//        } else if (v == btn_Up) {
//            num_song--;
//            playNumSong(num_song);
//            refreshUI();
//            strartBarUpdate();
//        } else if (v == tv_name) {
//
//            mDrawerLayout.openDrawer(GravityCompat.START);
//        } else {
//
//        }
//    }
//
//
//    /**
//     * 目录的适配器
//     */
//    class AdapterList extends BaseAdapter {
//        private Context context;
//
//        private View view;
//        private LayoutInflater inflater = null;
//        private HashMap<Integer, View> mView;
//
//
//        public AdapterList(Context context) {
//            this.context = context;
//
//
//            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            mView = new HashMap<Integer, View>();
//
//            for (int i = 0; i < voiceList.size(); i++) {
//
//                view = inflater.inflate(R.layout.slide_menu_iten, null);
//                mView.put(i, view);
//
//            }
//
//        }
//
//        public int getCount() {
//            // TODO Auto-generated method stub
//            return voiceList.size();
//        }
//
//        public Object getItem(int position) {
//            // TODO Auto-generated method stub
//            return voiceList.get(position);
//        }
//
//        public long getItemId(int position) {
//            // TODO Auto-generated method stub
//            return 0;
//        }
//
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            // TODO Auto-generated method stub
//            view = mView.get(position);
//
//            TextView tx_Name = (TextView) view.findViewById(R.id.lv_Name);
//            //TextView tx_Length = (TextView) view.findViewById(R.id.lv_Length);
//
//            tx_Name.setText(name_list.get(position));
//
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    num_song = position;
//                    playNumSong(num_song);
//                    refreshUI();
//                    strartBarUpdate();
//                }
//            });
//
//            //长按删除
//            view.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    new AlertDialog.Builder(MainActivity.this).setTitle("删除" + voiceList.get(position).toString() + "  ?")
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//
//                                    //删除录音文件
//                                    File name = new File(voiceList.get(position).toString());
//                                    deleteVoice(name);
//
//                                    //删除之后刷新
//                                    refreshVoiceList();
//                                }
//                            })
//                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                }
//                            })
//                            .show();
//
//                    return true;
//                }
//            });
//
//            return view;
//        }
//    }
//
//    /**
//     * 音量进度条监听
//     */
////    class SeekBarListener implements SeekBar.OnSeekBarChangeListener {
////
////        @Override
////        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
////            // TODO Auto-generated method stub
////            if (fromUser) {
////                int SeekPosition = seekBar.getProgress();
////                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, SeekPosition, 0);
////            }
////            volumeView.setText(String.valueOf(progress));
////        }
////
////        @Override
////        public void onStartTrackingTouch(SeekBar seekBar) {
////            // TODO Auto-generated method stub
////
////        }
////
////        @Override
////        public void onStopTrackingTouch(SeekBar seekBar) {
////            // TODO Auto-generated method stub
////
////        }
////
////    }
//
//
//    /**
//     * 播放进度条监听
//     */
//    class ProcessBarListener implements SeekBar.OnSeekBarChangeListener {
//
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            // TODO Auto-generated method stub
//
//            //必须来自用户才能进入，否则会在自动更新进度条时进入，产生混乱
//            if (fromUser == true) {
//                mediaPlayer.seekTo(progress);
//                nowPlayTime.setText(showTime(progress));
//            }
//
//
//        }
//
//        @Override
//        public void onStartTrackingTouch(SeekBar seekBar) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar) {
//            // TODO Auto-generated method stub
//
//        }
//
//    }
//
//    /*** 新建一个线程用于更新进度条*/
//    Handler handler = new Handler();
//
//    //接下来两个方法用于更新当前播放进度
//    public void strartBarUpdate() {
//        handler.post(r);
//    }
//
//    Runnable r = new Runnable() {
//
//        @Override
//        public void run() {
//            // TODO Auto-generated method stub
//            int CurrentPosition = getCurrentTime();
//            nowPlayTime.setText(showTime(CurrentPosition));
//            int mMax = getPlayingVoiceLength();
//            ProceseekBar2.setMax(mMax);
//            ProceseekBar2.setProgress(CurrentPosition);
//            handler.postDelayed(r, 100);//每隔100ms更新一下进度条
//
//            /**待解决*/
//            //线程中不是UI不安全的吗？为什么可以更新进度条？
//        }
//    };
//
//
//    //时间显示函数,我们获得音乐信息的是以毫秒为单位的，把把转换成我们熟悉的00:00格式
//    public String showTime(int time) {
//        time /= 1000;//总时间除以1000就是总秒数
//
//        int second = time % 60;//总秒数再除以60的余数就是折算后的秒
//        int minute = time / 60;//总秒数除以60后的整数部分就是总分钟数
//        int hour = minute / 60;//总分钟数除以60以后的的整数部分就是总小时数
//        minute %= 60;//总分钟数除以60以后的余数就是折算后的分钟数
//
//        return String.format("%02d:%02d:%02d", hour, minute, second);
//    }
//
//
//    /***刷新UI界面
//     * 需要刷新哪几项？
//     * 进度条、正在播放的名称、开始暂停键*/
//    public void refreshUI() {
//        // Toast.makeText(MainActivity.this, "gengxin" + getPlayingVoiceName(), Toast.LENGTH_SHORT).show();
//
//        if (isPlay==1){
//            btn_Start.setEnabled(false);
//            btn_Stop.setEnabled(true);
//        }else if (isPlay==0|isPlay==2){
//            btn_Start.setEnabled(true);
//            btn_Stop.setEnabled(false);
//        }
//        tv_name.setText(getPlayingVoiceName());
//        tv_totalsong.setText("目前一共有" + getTotalFileNumber() + "首录音");
//        allTime.setText("" + showTime(getPlayingVoiceLength()));
//    }
//
//
//    @Override//关闭侧滑页面
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (mDrawerLayout.isDrawerOpen(navView)) {
//                mDrawerLayout.closeDrawers();
//            }
//            else {
//                Intent intent =new Intent(MainActivity.this,RecordActivity.class);
//                startActivity(intent);
////                finish();
//            }
//
//
//        }
//
//        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
//
//            adjustStreamVolume(0, 0, 0);
//            return true;
//        }
//
//        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
//
//            adjustStreamVolume(1, 1, 1);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//
//    //调整音量
//    public void adjustStreamVolume(int streamType, int direction, int flags) {
//
////        //第一个streamType是需要调整音量的类型,这里设的是媒体音量,可以是:
////        STREAM_ALARM 警报
////        STREAM_MUSIC 音乐回放即媒体音量
////        STREAM_NOTIFICATION 窗口顶部状态栏Notification,
////        STREAM_RING 铃声
////        STREAM_SYSTEM 系统
////        STREAM_VOICE_CALL 通话
////        STREAM_DTMF 双音多频,不是很明白什么东西
////
////        第二个direction,是调整的方向,增加或减少,可以是:
////        ADJUST_LOWER 降低音量
////        ADJUST_RAISE 升高音量
////        ADJUST_SAME 保持不变,这个主要用于向用户展示当前的音量
////
////        第三个flags是一些附加参数,只介绍两个常用的
////        FLAG_PLAY_SOUND 调整音量时播放声音
////        FLAG_SHOW_UI 调整时显示音量条,就是按音量键出现的那个
////        0 表示什么也没有
//        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//
//        if (direction == 0) {
//
//            am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
//
//        } else {
//            am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
//
//        }
//
//    }
//
//
//
//
//
//
//    /***返回录音文件*/
//    public List<File> getFileList(String str) {
//        try {
//            path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + str);
///**待解决问题*/
//            //若文件夹不存在则创建一个新的文件夹，但是好像在path找不到路径的时候就会报错，这是怎么回事？
//
//
//            fileList = allFile(path);//此时获得的是默认存储位置的文件
//        } catch (Exception e) {
//
//            if (!path.exists()) {
////               File file= path.mkdirs();  //创建文件夹
//
//            }
//            e.printStackTrace();
//        }
//        return fileList;
//    }
//
//    /***返回录音文件名称列表*/
//    public List<String> getVoiceNameList() {
//        List<String> name_list = new ArrayList<>();
//        for (int i = 0; i < fileList.size(); i++) {
//            String name = fileList.get(i).toString();
//            String nm = name.substring(name.lastIndexOf("/") + 1, name.lastIndexOf("."));
//            name_list.add(nm);
//        }
//        return name_list;
//    }
//
//    /***返回文件夹中录音数目*/
//    public int getTotalFileNumber() {
//        return fileList.size();
//    }
//
//    /*** 返回文件夹中所有文件*/
//    public List<File> allFile(File file) {
//        List<File> mFileList = new ArrayList<File>();
//        File[] fileArray = file.listFiles();
//
//
//        for (File f : fileArray) {
//
//
//            if (f.isFile()) {
//                mFileList.add(f);
//            } else {
//                allFile(f);
//            }
//        }
//        return mFileList;
//    }
//
//    /***开始播放*/
//    public void beginPlay() {
//        mediaPlayer.start();
//
//        isPlay=1;
//        Toast.makeText(MainActivity.this, "gengxin begin play" + isPlay, Toast.LENGTH_SHORT).show();
//
//    }
//
//    /***暂停*/
//    public void pausePlay() {
//        mediaPlayer.pause();
//
//        isPlay=2;
//        Toast.makeText(MainActivity.this, "gengxin  pause play" + isPlay, Toast.LENGTH_SHORT).show();
//
//    }
//
//    /*** 根据传入参数决定播放上一首还是下一首*/
//    public void playNumSong(int num_song) {
//
//
//        mediaPlayer.stop();
//
//        //判断当前是第几首，然后决定播放哪一首，形成循环。
//        if (num_song < fileList.size() && num_song >= 0) {
//            num_song1 = num_song;
//        } else if (num_song < 0) {
//            num_song1 = fileList.size() - 1;
//            num_song = fileList.size() - 1;
//
//        } else if (num_song >= fileList.size()) {
//            num_song1 = 0;
//            num_song = 0;
//        }
//
//        fileUri = Uri.parse(fileList.get(num_song).toString());
//
//        // tv_name.setMovementMethod(ScrollingMovementMethod.getInstance());
//
//        try {
//            mediaPlayer = MediaPlayer.create(MainActivity.this, fileUri);
//            mediaPlayer.setOnCompletionListener(MainActivity.this);
//
//
//            mediaPlayer.start();
//            isPlay=1;
//            Toast.makeText(MainActivity.this, "gengxin playnum song" + isPlay, Toast.LENGTH_SHORT).show();
//
//
//
//        } catch (IllegalArgumentException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IllegalStateException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//
//    }
//
//    /***返回正在播放文件的长度*/
//    public int getPlayingVoiceLength() {
//        int alltime = mediaPlayer.getDuration();
//        return alltime;
//    }
//
////    public boolean isPlaying() {
////
////        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
////        return am.isMusicActive(); //判断Music是否在播放
////
////        return mediaPlayer.isPlaying();
////    }
//
//
//    /***返回播放进度*/
//    public int getCurrentTime() {
//        int CurrentPosition = mediaPlayer.getCurrentPosition();
//        return CurrentPosition;
//    }
//
//    /***返回正在播放的文件名*/
//    public String getPlayingVoiceName() {
//        String name = fileList.get(num_song1).toString();
//        //截取文件名
//
//
//        return name.substring(name.lastIndexOf("/") + 1, name.lastIndexOf("."));
//    }
//
//
//    /*** 重写MediaPlayer.OnCompletionListener的方法，此方法会在每次播放完毕后调用*/
//    @Override
//    public void onCompletion(MediaPlayer mp) {
//
//        //播放首数加一
//        num_song1++;
//
//        playNumSong(num_song1);
//
//    }
//
//
//    /*** 删除录音文件*/
//    public void deleteVoice(File audioFile) {
//        //删除录音文件
//        File file = new File(path, audioFile.getName());
//        if (file.isFile() && file.exists()) {
//            file.delete();
//
//            Toast.makeText(MainActivity.this, "已删除", Toast.LENGTH_SHORT).show();
//            refreshVoiceList();
//        }
//    }
//
//
//    /*** 刷新录音列表，此方法会在新增录音或删除录音后调用*/
//    public void refreshVoiceList() {
//        fileList = allFile(path);
//
//
//
////        MainActivity.AdapterList adapter = new MainActivity.AdapterList(MainActivity.this);
////        lv_menu.setAdapter(adapter);
//
//        playNumSong(0);
//        mediaPlayer.pause();
//        isPlay=2;
//        Toast.makeText(MainActivity.this, "gengxin refresh" + isPlay, Toast.LENGTH_SHORT).show();
//
//
//    }
//
//
//}
//
