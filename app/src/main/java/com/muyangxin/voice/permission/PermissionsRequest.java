package com.muyangxin.voice.permission;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.muyangxin.voice.activity.RecordActivity;
import com.muyangxin.voice.activityControl.EndActivity;


import com.muyangxin.voice.R;

public class PermissionsRequest extends AppCompatActivity {

    private static final int REQUEST_CODE = 0; // 请求码

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
//            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };



    private PermissionsChecker mPermissionsChecker; // 权限检测器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_request);
        EndActivity.getInstance().addActivity(this);


        mPermissionsChecker = new PermissionsChecker(this);
    }





    @Override protected void onResume() {
        super.onResume();

        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        } else{

            //存储正在播放曲子
            SharedPreferences.Editor editor = getSharedPreferences("recordNum", MODE_PRIVATE).edit();
            editor.putBoolean("isPlay",false);
            editor.apply();
            Intent intent=new Intent(PermissionsRequest.this,RecordActivity.class);
            startActivity(intent);
        }


    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    //执行oncreate方法
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
//        else{
//            Intent intent=new Intent(PermissionsRequest.this,MainActivity.class);
//            startActivity(intent);
//        }
    }
}