package com.muyangxin.voice.recordClass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.muyangxin.voice.activity.FilePath;

import java.io.File;
import java.io.IOException;

/**
 * Created by qxx on 2017/9/30.
 */

public class Record extends AppCompatActivity {

    private Activity activity;

    private File path;
    private File audioFile;

    private MediaRecorder recorder;

    //   public static String STR = "/record/";
    public static String STR = "/Recordings/";//这里的路径设置要与playService里面的路径一致。
    // 问题是怎样才能在一个地方设置路径变量然后全局通用？


    public void sendBianLiang(Activity activity) {
        this.activity = activity;


    }

    public String showTime(int time) {
        time /= 1000;//总时间除以1000就是总秒数

        int second = time % 60;//总秒数再除以60的余数就是折算后的秒
        int minute = time / 60;//总秒数除以60后的整数部分就是总分钟数
        int hour = minute / 60;//总分钟数除以60以后的的整数部分就是总小时数
        minute %= 60;//总分钟数除以60以后的余数就是折算后的分钟数

        if(time<3600){
            return String.format("%02d:%02d",  minute, second);
        }else {
            return String.format("%02d:%02d:%02d", hour, minute, second);
        }
    }


    /***开始录音*/
    public void beginRecord() {
        recorder = new MediaRecorder();
        recorder.reset();

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);//这个参数设置输出文件的编码格式

        //File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Recordings/");

        FilePath.createDirectory(STR);
        path = new File(FilePath.getFilePath() + STR);


        try {
            audioFile = File.createTempFile("one", ".m4a", path);
            recorder.setOutputFile(audioFile.getAbsolutePath());
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorder.start();

    }

    /***结束录音*/
    public void stopRecord() {
        recorder.stop();
        recorder.release();//先停止再释放
    }

    /***重命名*/
    public void renameVoice() {

        final EditText editText = new EditText(activity);
        editText.setText(audioFile.getName());

        //选中默认名称
        int index = audioFile.getName().lastIndexOf(".");
        if (index > 0)
            editText.setSelection(0, index);


        //自动显示软键盘,貌似配合dialog里面最后一行才生效
        InputMethodManager inputManager =
                (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);


        new AlertDialog.Builder(activity).setTitle("重命名")
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
                            File oldName = new File(path, audioFile.getName());
                            File newName = new File(path, input);
                            oldName.renameTo(newName);
                        }

                        SharedPreferences.Editor editor = getSharedPreferences("recordNum", MODE_PRIVATE).edit();
                        editor.putBoolean("isAdd",true);
                        editor.apply();

                    }
                })
                .setNegativeButton("删除录音", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteVoice(path, audioFile);
                    }
                })
                .show()
                .getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);//显示软键盘

    }

    /***放弃录音*/
    public void giveUpRecord() {
        recorder.stop();
        recorder.release();
        deleteVoice(path, audioFile);
    }

    /**
     * 删除录音文件
     */
    public void deleteVoice(File path, File audioFile) {
        //删除录音文件
        File file = new File(path, audioFile.getName());
        if (file.isFile() && file.exists()) {
            file.delete();

            Toast.makeText(activity, "已删除", Toast.LENGTH_SHORT).show();
            // refreshVoiceList();
        }
    }


}
