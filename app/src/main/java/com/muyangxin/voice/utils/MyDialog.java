package com.muyangxin.voice.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.muyangxin.voice.R;

/**
 * Created by qxx on 2017/10/17.
 */

//自定义dialog ，用于长按录音列表时使用
public abstract class MyDialog extends AlertDialog implements View.OnClickListener {


    private Context context;

    protected MyDialog(Context context) {
        super(context);
        this.context = context;
        // TODO Auto-generated constructor stub
    }

private TextView tv_title;
    /**
     * 布局中的其中一个组件
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // 加载自定义布局
        LayoutInflater inflater = LayoutInflater.from(context);
        final View layout = inflater.inflate(R.layout.view_dialog, null);

        layout.findViewById(R.id.tv_rename).setOnClickListener(this);
        layout.findViewById(R.id.tv_delete).setOnClickListener(this);
        tv_title=(TextView)layout.findViewById(R.id.tv_title);

        setContentView(layout);
    }

    public void setMyTitle(String title) {
        tv_title.setText(title);
    }



    /**
     * 修改 框体大小
     *
     * @param width
     * @param height
     */
    public void setDialogSize(int width, int height) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = 350;
        params.height = 200;
        this.getWindow().setAttributes(params);
    }

    public abstract void clickDelete();

    public abstract void clickRename();


    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        //switch 和if用法的区别要懂
        switch (v.getId()) {
            case R.id.tv_delete:
                clickDelete();
                break;
            case R.id.tv_rename:
                clickRename();
                break;
            default:
                break;
        }

    }

}
