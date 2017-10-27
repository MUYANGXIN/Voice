package com.muyangxin.voice.activity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.muyangxin.voice.R;
import com.muyangxin.voice.myService.PlayService;
import com.muyangxin.voice.utils.MyDialog;


import java.io.File;
import java.util.HashMap;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {

    public PlayService playService;

    private Button btn_Start, btn_Stop;//开始暂停

    private ImageView btn_Up, btn_Next;//上一首，下一首

    private TextView tv_totalsong;//总共多少首
    //名称
    private TextView tv_name;

    //进度条
    private SeekBar ProceseekBar2;
    private TextView nowPlayTime, allTime;

    private ListView lv_menu;
    private String[] name_list1;


    private boolean isEmpity = false;

    private ImageView iv_loop;
    private boolean isLoop = false;

    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            playService = ((PlayService.MyBinder) iBinder).getService();

            //注册回调接口来接收变化
            playService.setOnChangeListener(new PlayService.OnChangeListener() {
                @Override
                public void haveChange() {
                    //当服务自动播放下一首时，通过接口回调自动更新UI
                    smallRefresh();
                }

            });


        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            playService = null;
        }
    };

    /***绑定服务*/
    private void bindServiceConnection() {
        Intent intent = new Intent(PlayActivity.this, PlayService.class);
        startService(intent);
        bindService(intent, sc, this.BIND_AUTO_CREATE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_play_layout);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_play_layout);

        playService = new PlayService();

        if (Integer.parseInt(playService.getTotalNumberSongs()) <= 0) {
            Toast.makeText(PlayActivity.this, "没有录音", Toast.LENGTH_SHORT).show();

            isEmpity = true;

        } else {
            //绑定服务
            bindServiceConnection();


            SharedPreferences editor = getSharedPreferences("recordNum", MODE_PRIVATE);
            //获取刚才/上次播放的曲子索引
            playService.musicIndex = editor.getInt("Index", 0);


            boolean isp = editor.getBoolean("isPlay", false);


            if (isp) {
            } else {
                //如果没有播放
                playService.prepare();

//            存储正在播放曲子
                SharedPreferences.Editor editor1 = getSharedPreferences("recordNum", MODE_PRIVATE).edit();
                editor1.putBoolean("isPlay", true);
                editor1.apply();
            }


            init();
        }


    }


    /***初始化各种按钮*/
    private void init() {
        //总录音数目
        tv_totalsong = (TextView) findViewById(R.id.tv_totalsongs_play);
        tv_totalsong.setText("目前一共有" + playService.getTotalNumberSongs() + "首录音");


        //开始暂停键
        btn_Start = (Button) findViewById(R.id.btn_start);
        btn_Stop = (Button) findViewById(R.id.btn_Stop);
        btn_Start.setOnClickListener(this);
        btn_Stop.setOnClickListener(this);


        if (playService.isPlay()) {
            btn_Start.setEnabled(false);
            btn_Stop.setEnabled(true);

        } else {
            btn_Start.setEnabled(true);
            btn_Stop.setEnabled(false);
        }


        //上一首下一首键
        btn_Up = (ImageView) findViewById(R.id.btn_Up);
        btn_Next = (ImageView) findViewById(R.id.btn_Next);
        btn_Next.setOnClickListener(this);
        btn_Up.setOnClickListener(this);


        //名称与目录
        tv_name = (TextView) findViewById(R.id.tv_name_play);
        tv_name.setText(playService.getVoiceName());

        //循环
        iv_loop = (ImageView) findViewById(R.id.iv_loop_order);
        isLoop = playService.getIsLoop();
        if (isLoop) {
            iv_loop.setImageResource(R.drawable.loop);
        } else {
            iv_loop.setImageResource(R.drawable.order);
        }
        iv_loop.setOnClickListener(this);


        //进度条
        nowPlayTime = (TextView) findViewById(R.id.tv_currentLength_play);
        nowPlayTime.setText("" + showTime1(0));
        allTime = (TextView) findViewById(R.id.tv_totalLength_play);
        allTime.setText("" + showTime1(playService.mp.getDuration()));


        ProceseekBar2 = (SeekBar) findViewById(R.id.sek_process_play);
        ProceseekBar2.setProgress(playService.mp.getCurrentPosition());
        ProceseekBar2.setMax(playService.mp.getDuration());
        ProceseekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    playService.mp.seekTo(seekBar.getProgress());
                    nowPlayTime.setText(showTime1(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //所有录音的名称列表
        name_list1 = playService.getNameList();

        //录音列表
        lv_menu = (ListView) findViewById(R.id.lv_menu2);
        AdapterList1 adapter = new AdapterList1(this);
        lv_menu.setAdapter(adapter);

    }

    /***重新返回活动都会调用此方法*/
    @Override
    protected void onResume() {

        if (Integer.parseInt(playService.getTotalNumberSongs()) <= 0) {
            Toast.makeText(PlayActivity.this, "没有录音", Toast.LENGTH_SHORT).show();
            isEmpity = true;
        }


        if (!isEmpity) {
            //设置进度条
            ProceseekBar2.setProgress(playService.mp.getCurrentPosition());
            ProceseekBar2.setMax(playService.mp.getDuration());
            handler.post(runnable);//开启线程更新UI


            if (isLoop) {

                iv_loop.setImageResource(R.drawable.loop);
            } else {
                iv_loop.setImageResource(R.drawable.order);
            }
//
//        try {
//            SharedPreferences editorRead = getSharedPreferences("recordNum", MODE_PRIVATE);
//            int lastNum = editorRead.getInt("length", 0);
//            int nowNum = playService.getCurrentNumSongs();
//            if (lastNum == nowNum) {
//                Toast.makeText(PlayActivity.this, "无改变", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(PlayActivity.this, "有改变", Toast.LENGTH_SHORT).show();
//                playService.absolutelyUpdate();
//                bigUpDateList1();
//            }
//        } catch (Exception e) {
//            Toast.makeText(PlayActivity.this, "无改变", Toast.LENGTH_SHORT).show();
//
//            e.printStackTrace();
//        }
            //取出数据检验录音是否有新增



            SharedPreferences editor = getSharedPreferences("recordNum", MODE_PRIVATE);


            //获取刚才/上次播放的曲子索引
            playService.musicIndex = editor.getInt("Index", 0);
            lv_menu.setSelection(playService.musicIndex);

            if (editor.getBoolean("isAdd", false)) {


                SharedPreferences.Editor editor1 = getSharedPreferences("recordNum", MODE_PRIVATE).edit();
                editor1.putBoolean("isAdd", false);
                editor1.apply();

                playService.absolutelyUpdate();//有新增的话更新服务中的一切列表，并停止播放音乐

                //更新UI
                bigUpDateList1();
                Toast.makeText(PlayActivity.this, "录音有新增", Toast.LENGTH_SHORT).show();

            }

            smallRefresh();
        }

        super.onResume();

    }

    /***点击事件*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start://开始按钮

                playService.play();//开始播放
                smallRefresh();//开始更新进度条
                btn_Start.setEnabled(false);
                btn_Stop.setEnabled(true);
                break;
            case R.id.btn_Stop://暂停
                playService.pause();
                btn_Start.setEnabled(true);
                btn_Stop.setEnabled(false);
                break;
            case R.id.btn_Next://下一曲
                playService.nextMusic();
                smallRefresh();//更新进度条
//                btn_Start.setEnabled(false);
//                btn_Stop.setEnabled(true);
                break;
            case R.id.btn_Up://上一曲
                playService.preMusic();
                smallRefresh();//更新进度条
//                btn_Start.setEnabled(false);
//                btn_Stop.setEnabled(true);
                break;
            case R.id.iv_loop_order:
                if (isLoop) {
                    isLoop = false;
                    playService.setLoop(isLoop);
                    iv_loop.setImageResource(R.drawable.order);
                } else {
                    isLoop = true;
                    playService.setLoop(isLoop);
                    iv_loop.setImageResource(R.drawable.loop);
                }

            default:
                break;
        }
    }

    private  MyDialog myDialog;

    /*** 目录的适配器*/
    class AdapterList1 extends BaseAdapter {
        private Context context;

        private View view;
        private LayoutInflater inflater = null;
        private HashMap<Integer, View> mView;




        public AdapterList1(Context context) {
            this.context = context;


            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = new HashMap<Integer, View>();


            for (int i = 0; i < name_list1.length; i++) {

                //本句不能拿到循环体外面，否则list只有一个view得到填充
                view = inflater.inflate(R.layout.slide_menu_iten, null);

                mView.put(i, view);

            }

        }

        public int getCount() {
            // TODO Auto-generated method stub
            return name_list1.length;
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return name_list1[position];
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            view = mView.get(position);

            TextView tx_Name = (TextView) view.findViewById(R.id.lv_Name);
            //填充名称
            tx_Name.setText(name_list1[position]);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    playService.playThisMusic(position);

                    smallRefresh();
                }
            });

            //长按
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    myDialog = new MyDialog(PlayActivity.this) {
                        @Override
                        public void clickDelete() {
                            deleteDialog(position);
                        }

                        @Override
                        public void clickRename() {
                            renameDialog(PlayActivity.this, position);
                        }
                    };
                    myDialog.show();
                    myDialog.setMyTitle(name_list1[position]);
//                    LayoutInflater inflater = LayoutInflater.from(PlayActivity.this);
//                    final View layout = inflater.inflate(R.layout.view_dialog, null);

//                    builder
//                            .setTitle("" + name_list1[position] + "  ")
//                            .setView(layout)
////                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
////                                @Override
////                                public void onClick(DialogInterface dialog, int which) {
////
////                                    //删除录音文件//其实这个删除方法里面在删除之后已经做了一部分的后续工作，
////                                    // 比如自动播放下一首之类
////                                    playService.deleteThisVoice(position);
////
////                                    //删除之后刷新
////                                    bigUpDateList1();
////                                }
////                            })
////                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
////                                @Override
////                                public void onClick(DialogInterface dialog, int which) {
////
////                                }
////                            })
//                            .show();
//
//
//                    //删除
//                    layout.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            //删除录音文件//其实这个删除方法里面在删除之后已经做了一部分的后续工作，
//                            // 比如自动播放下一首之类
//                            playService.deleteThisVoice(position);
//                            //删除之后刷新
//                            bigUpDateList1();
//
//                        }
//                    });
//
//                    layout.findViewById(R.id.btn_rename).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            renameDialog(PlayActivity.this,position);
//
//                            Toast.makeText(PlayActivity.this, "rename", Toast.LENGTH_SHORT).show();
//                        }
//                    });
                    return true;
                }
            });
            return view;
        }
    }

    private void deleteDialog(final int pos) {
        myDialog.cancel();

        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("确定删除录音    "+name_list1[pos]+"  ?")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //                            删除录音文件//其实这个删除方法里面在删除之后已经做了一部分的后续工作，
                        // 比如自动播放下一首之类
                        playService.deleteThisVoice(pos);
                        //删除之后刷新
                        bigUpDateList1();
                    }
                })
                .show();
    }

    /***重命名*/
    public void renameDialog(Context context, int position) {

        myDialog.cancel();
        final File path = new File(FilePath.getFilePath() + "/Recordings");
        final String oldName = playService.fileList.get(position).getName();

        final EditText editText = new EditText(context);
        editText.setText(oldName);

        //选中默认名称
        int index = oldName.lastIndexOf(".");
        if (index > 0)
            editText.setSelection(0, index);


        //自动显示软键盘,貌似配合dialog里面最后一行才生效
        InputMethodManager inputManager =
                (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);


        new AlertDialog.Builder(context).setTitle("重命名")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = editText.getText().toString();
                        if (input.equals("")) {
//                                File oldName = new File(path, audioFile.getName());
//                                File newName = new File(path, audioFile.getName());
//                                oldName.renameTo(newName);
                        } else {
                            File oldName1 = new File(path, oldName);
                            File newName = new File(path, input);
                            oldName1.renameTo(newName);

                            bigUpDateList1();
                        }


                    }
                })
                .setNegativeButton("放弃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show()
                .getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);//显示软键盘

    }


    /***刷新名称列表、总曲数、时间进度条、按钮状态*/
    public void bigUpDateList1() {
        //重新获取名称数组
        name_list1 = playService.getNameList();

        //刷新listview
        AdapterList1 adapter = new AdapterList1(PlayActivity.this);
        lv_menu.setAdapter(adapter);

        //一共有多少首歌曲
        tv_totalsong.setText("目前一共有" + playService.getTotalNumberSongs() + "首录音");

        //总时间与当前时间
        nowPlayTime.setText("" + showTime1(0));
        allTime.setText("" + showTime1(0));

        //正在播放的曲子名称
        tv_name.setText(playService.getVoiceName());

        //Log.d("index", "cccccccccccccccccccccccccccc"+playService.musicIndex);
        //更新按钮状态
        if (playService.isPlay()) {
            btn_Start.setEnabled(false);
            btn_Stop.setEnabled(true);

        } else {
            btn_Start.setEnabled(true);
            btn_Stop.setEnabled(false);
        }
    }

    /***更新按钮进度条和歌曲名称*/
    public void smallRefresh() {

        //用于更新当前播放进度
        handler.post(runnable);

        //改变按钮状态
        if (playService.isPlay()) {
            btn_Start.setEnabled(false);
            btn_Stop.setEnabled(true);

        } else {
            btn_Start.setEnabled(true);
            btn_Stop.setEnabled(false);
        }

        // Toast.makeText(PlayActivity.this, ""+playService.getVoiceName(), Toast.LENGTH_SHORT).show();
        //设置正在播放的曲子名称
        tv_name.setText(playService.getVoiceName());
    }

    /***定时刷新的线程*/
    public android.os.Handler handler = new android.os.Handler();
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            if (playService.mp.isPlaying()) {
////                musicStatus.setText(getResources().getString(R.string.playing));
////                btnPlayOrPause.setText(getResources().getString(R.string.pause).toUpperCase());
//            } else {
////                musicStatus.setText(getResources().getString(R.string.pause));
////                btnPlayOrPause.setText(getResources().getString(R.string.play).toUpperCase());
//            }
////            musicTime.setText(time.format(playService.mp.getCurrentPosition()) + "/"
////                    + time.format(playService.mp.getDuration()));

            //正在播放的进度
            nowPlayTime.setText(showTime1(playService.mp.getCurrentPosition()));
            allTime.setText(showTime1(playService.mp.getDuration()));//总长度

            ProceseekBar2.setMax(playService.mp.getDuration());
            ProceseekBar2.setProgress(playService.mp.getCurrentPosition());//更新进度条


            //    Log.d("正在更新","di"+playService.mp.getCurrentPosition());

            //每100ms更新一次
            handler.postDelayed(runnable, 100);
        }
    };


    /***时间格式转换*/
    public String showTime1(int time) {
        //时间显示函数,我们获得音乐信息的是以毫秒为单位的，把把转换成我们熟悉的00:00格式
        time /= 1000;//总时间除以1000就是总秒数

        int second = time % 60;//总秒数再除以60的余数就是折算后的秒
        int minute = time / 60;//总秒数除以60后的整数部分就是总分钟数
        int hour = minute / 60;//总分钟数除以60以后的的整数部分就是总小时数
        minute %= 60;//总分钟数除以60以后的余数就是折算后的分钟数
        if (time < 3600) {
            return String.format("%02d:%02d", minute, second);
        } else {
            return String.format("%02d:%02d:%02d", hour, minute, second);
        }


    }


    /***返回的时候存储正在播放的曲子索引*/
    @Override
    protected void onPause() {

        //存储正在播放曲子
        SharedPreferences.Editor editor = getSharedPreferences("recordNum", MODE_PRIVATE).edit();
        editor.putInt("Index", playService.musicIndex);

        editor.apply();


        super.onPause();
    }

    /***销毁活动时解绑服务**/
    @Override
    public void onDestroy() {

        if (!isEmpity) {//当为空时没有启动服务，不用解绑,如果解绑会崩溃
            unbindService(sc);
        }


        super.onDestroy();
    }

}
