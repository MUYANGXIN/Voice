package com.muyangxin.voice.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.os.Handler;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.muyangxin.voice.R;
import com.muyangxin.voice.myService.PlayService;
import com.muyangxin.voice.activityControl.EndActivity;
import com.muyangxin.voice.myView.BoXingView;
import com.muyangxin.voice.myView.VisualizerView;
import com.muyangxin.voice.recordClass.Record;

import java.io.File;

public class RecordActivity extends Record implements View.OnClickListener {

    private ImageView stopBtn;//停止录音

    private ImageView btn_giveUp;//放弃录音
    private ImageView imgbtn_record_list;

    private ImageView startBtn;//开始暂停
    private TextView tv_start_pause;

    public MediaRecorder recorder;
    public File audioFile;//录音文件
    private Boolean isRecording = false;

    private TextView tv_timeShow;//显示录音时长
    private int mSeconds = 0;//单位是毫秒ms

    private LinearLayout ll_give_up, ll_record_list;

    //计时器
    //第一种更新UI
//        private Handler recevHander;
//        private int timeAll;

    //第二种更新UI
    private Handler handler = new Handler();


    //private Record recd;


    private long firstTime2 = 0;

//    public RecordActivity(Activity activity, String STR, MediaPlayer mediaPlayer) {
//        super(activity, STR, mediaPlayer);
//    }


    private VisualizerView mBaseVisualizerView;
    private static final float VISUALIZER_HEIGHT_DIP = 150f;//频谱View高度
    private Visualizer mVisualizer;//频谱器
    private MediaPlayer mMediaPlayer;//音频
    private Equalizer mEqualizer; //均衡器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_record_layout);


        SharedPreferences.Editor editor = getSharedPreferences("recordNum", MODE_PRIVATE).edit();
        editor.putBoolean("isAdd", false);
        editor.apply();

        initView();

        //       setupVisualizerFxAndUi2();
        setupVisualizerFxAndUi();
        // LineView lineView=new LineView(this,null);
        //VadView vadView=new VadView((this));
        // mBaseVisualizerView = new RecordingView(this);
        //WaveView waveView=new WaveView(this,null);
    }


    /**
     * 生成一个VisualizerView对象，使音频频谱的波段能够反映到 VisualizerView上
     */
    private void setupVisualizerFxAndUi() {

        mBaseVisualizerView = new VisualizerView(this);


        mMediaPlayer = MediaPlayer.create(this, R.raw.aaa);//实例化 MediaPlayer 并添加音频


        //实例化Visualizer，参数SessionId可以通过MediaPlayer的对象获得
        mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());


        mBaseVisualizerView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,//宽度
                (int) (VISUALIZER_HEIGHT_DIP * getResources().getDisplayMetrics().density)//高度
        ));

//
//        //采样 - 参数内必须是2的位数 - 如64,128,256,512,1024
//        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[0]);
//       // mVisualizer.setDataCaptureListener(RecordActivity.this);
//


        //设置允许波形表示，并且捕获它
        mBaseVisualizerView.setVisualizer(mVisualizer);

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
            }
        });

        mVisualizer.setEnabled(true);//false 则不显示
//        mMediaPlayer.start();//开始播放
//        mMediaPlayer.setLooping(true);//循环播放
    }

    private void setupVisualizerFxAndUi2() {

        BoXingView boXingView = new BoXingView(this);

        mMediaPlayer = MediaPlayer.create(this, R.raw.aaa);//实例化 MediaPlayer 并添加音频


        //实例化Visualizer，参数SessionId可以通过MediaPlayer的对象获得
        mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());


        boXingView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,//宽度
                (int) (VISUALIZER_HEIGHT_DIP * getResources().getDisplayMetrics().density)//高度
        ));

//
//        //采样 - 参数内必须是2的位数 - 如64,128,256,512,1024
//        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[0]);
//       // mVisualizer.setDataCaptureListener(RecordActivity.this);
//


        //设置允许波形表示，并且捕获它
        boXingView.setVisualizer(mVisualizer);

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
            }
        });

        mVisualizer.setEnabled(true);//false 则不显示
       // mMediaPlayer.start();//开始播放
       // mMediaPlayer.setLooping(true);//循环播放
    }


    private void initView() {

        stopBtn = (ImageView) findViewById(R.id.btn_stop_recording);
        startBtn = (ImageView) findViewById(R.id.iv_start_recording);

        stopBtn.setImageResource(R.drawable.ok2);
        startBtn.setImageResource(R.drawable.re);


        tv_start_pause = (TextView) findViewById(R.id.tv_start_pause);
        tv_timeShow = (TextView) findViewById(R.id.tv_record_time);

        ll_give_up = (LinearLayout) findViewById(R.id.ll_3);
        ll_record_list = (LinearLayout) findViewById(R.id.ll_4);

        btn_giveUp = (ImageView) findViewById(R.id.btn_abandon_recording);
        imgbtn_record_list = (ImageView) findViewById(R.id.imgbtn_recording_list);

        //stopBtn.setText("完成");
        tv_timeShow.setText("" + showTime(0));


        ll_give_up.setVisibility(View.GONE);
        ll_record_list.setVisibility(View.VISIBLE);

        stopBtn.setEnabled(false);
        btn_giveUp.setEnabled(false);
        imgbtn_record_list.setEnabled(true);

        startBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        btn_giveUp.setOnClickListener(this);
        imgbtn_record_list.setOnClickListener(this);

        //第一种更新方法，需要在这个中间不断调用start才能保持线程循环
        // 肯定使我哪里用的不对
//            recevHander = new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
//
//                    String tm = showTime(Integer.parseInt(msg.obj.toString()));
//
//                    tv_timeShow.setText("" + tm);
//
//                    if (isRecording) {
//                        LooperThread thread = new LooperThread();
//                        thread.setHandler(recevHander);
//                        thread.start();
//
//
//                    }
//
//                }
//            };

        sendBianLiang(RecordActivity.this);
//        recd=new Record(RecordActivity.this,path,audioFile);

    }


    @Override
    public void onClick(View v) {
        if (v == stopBtn) {

            isRecording = false;
            //结束时提示重命名
            //renameDialog(path);

            stopRecord();
            renameVoice();

            setZero();
            tv_start_pause.setText("开始");

            ll_give_up.setVisibility(View.GONE);
            ll_record_list.setVisibility(View.VISIBLE);

            stopBtn.setImageResource(R.drawable.ok2);
            startBtn.setImageResource(R.drawable.re);
            stopBtn.setEnabled(false);
            btn_giveUp.setEnabled(false);
            startBtn.setEnabled(true);

        } else if (v == startBtn) {

            if (!isRecording) {
                isRecording = true;

                //开始录音
                beginRecord();

                //开始计时
                //showRecordTime();、、//第一种更新方法
                handler.post(myRunnable);//第二种更新方法

                ll_give_up.setVisibility(View.VISIBLE);
                ll_record_list.setVisibility(View.GONE);

                tv_start_pause.setText("暂停");

                stopBtn.setImageResource(R.drawable.ok);
                startBtn.setImageResource(R.drawable.begin);
                stopBtn.setEnabled(true);
                btn_giveUp.setEnabled(true);
                startBtn.setEnabled(false);
            }

        } else if (v == btn_giveUp) {

            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime2 > 1000) {
                firstTime2 = secondTime;
            } else {
                //先结束录音，然后删除录音
                giveUpRecord();

                isRecording = false;

                setZero();
                tv_start_pause.setText("开始");

                ll_give_up.setVisibility(View.GONE);
                ll_record_list.setVisibility(View.VISIBLE);

                stopBtn.setImageResource(R.drawable.ok2);
                startBtn.setImageResource(R.drawable.re);
                stopBtn.setEnabled(false);
                btn_giveUp.setEnabled(false);
                startBtn.setEnabled(true);
            }

        } else if (v == imgbtn_record_list) {
            Intent intent = new Intent(RecordActivity.this, PlayActivity.class);
//            Intent intent = new Intent(RecordActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_right, R.anim.out_left);
        }

    }

    /***第一种更新UI的方法，看起来使用了循环，可是并没有循环，得自己不停的启动线程才可以
     *
     * 开启更新UI线程
     */
//        private void showRecordTime() {
//
//
//            timeAll = 0;
//
//            LooperThread thread = new LooperThread();
//            thread.setHandler(recevHander);
//            thread.start();
//
//        }

    /**
     * 循环线程不循环？？？？
     */
//        class LooperThread extends Thread {
//            Handler handler;
//
//            public void setHandler(Handler handler) {
//                this.handler = handler;
//            }
//
//            @Override
//            public void run() {
//                Looper.prepare();
//
//                timeAll = timeAll + 1000;
//                Message message = new Message();
//                message.what = timeAll;
//                message.obj = timeAll;
//
//                try {
//                    Thread.sleep(1000);
//                    handler.sendMessage(message);
//
//                } catch (InterruptedException e) {
//                }
//
//                Looper.loop();
//            }
//        }
    /***只有在两个注释之间注释掉的才能折叠吗？果然是*/

    //第二种更新UI方法
    //其实调用post方法只能让此方法块执行一次，但在这个方法块中又延迟1秒调用。
    // 纳尼？和第一种方法原理差不多？
    private Runnable myRunnable = new Runnable() {
        public void run() {

            if (isRecording) {
                handler.postDelayed(this, 1000);

                //接下来这两行不能换位置！否则显示的时间要比实际多一秒
                tv_timeShow.setText("" + showTime(mSeconds));
                mSeconds = mSeconds + 1000;
            }
        }
    };

    /***清零*/
    private void setZero() {
//            timeAll=0;
        mSeconds = 0;
        tv_timeShow.setText(showTime(0));
    }

//    /*** 重命名录音文件*/
//    private void renameDialog(final File path) {
//
//        final EditText editText = new EditText(RecordActivity.this);
//        editText.setText(audioFile.getName());
//
//        //选中默认名称
//        int index = audioFile.getName().lastIndexOf(".");
//        if (index > 0)
//            editText.setSelection(0, index);
//
//
//        //自动显示软键盘,貌似配合dialog里面最后一行才生效
//        InputMethodManager inputManager =
//                (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputManager.showSoftInput(editText, 0);
//        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//
//
//        new AlertDialog.Builder(RecordActivity.this).setTitle("重命名")
//                .setIcon(android.R.drawable.ic_dialog_info)
//                .setView(editText)
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        String input = editText.getText().toString();
//                        if (input.equals("")) {
////                                File oldName = new File(path, audioFile.getName());
////                                File newName = new File(path, audioFile.getName());
////                                oldName.renameTo(newName);
//                        } else {
//                            File oldName = new File(path, audioFile.getName());
//                            File newName = new File(path, input);
//                            oldName.renameTo(newName);
//                        }
//
//                        //刷新录音列表
//                        refreshVoiceList();
//
//
//                    }
//                })
//                .setNegativeButton("删除录音", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        deleteVoice(path, audioFile);
//                    }
//                })
//                .show()
//                .getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);//显示软键盘
//
//    }


    @Override//关闭侧滑页面
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if (keyCode == KeyEvent.KEYCODE_BACK) {

            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime2 > 1000) {
                //如果两次按键时间间隔大于2秒，则不退出
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime2 = secondTime;//更新firstTime

                return true;
            } else {

                stopService(new Intent(this,PlayService.class));


                //存储正在播放曲子
                SharedPreferences.Editor editor = getSharedPreferences("recordNum", MODE_PRIVATE).edit();
                editor.putBoolean("isPlay", false);
                editor.apply();

                //清空通知栏的服务图标
                NotificationManager cancelNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                //cancelNotificationManager.cancel(NOTIFY_ID);
                cancelNotificationManager.cancelAll();

                EndActivity.getInstance().exit();
                finish();
                System.exit(0);

            }
            //这样退出应用有一个问题，就是在首次安装之后第一次退出会重新打开APP。再次返回则可退出
            //不知道怎么回事？
            //解决了，方法是在EndActivity中不调用 System.exit(0);只是结束两个活动，
            // 连击时先调用finish，保证退出动画不黑屏，然后 System.exit(0);即可结束程序
        }

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
        return super.onKeyDown(keyCode, event);
    }


}
