package com.jikexueyuan.bmob_lost_found;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.Toast;

import cn.bmob.v3.Bmob;

/**
 * Created by 薄云雨季 on 2017/9/12.
 */

public abstract class BaseActivity extends Activity {
    protected int mScreenWidth;
    protected int mScreenHeight;

    public static final String TAG = "bmob";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this,"efbfaf1aa1a3412f5408bb67fd061152");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenHeight = metrics.heightPixels;
        mScreenWidth = metrics.widthPixels;

        setContentView();
        initViews();
        initListeners();
        initData();
    }

    /**
     * 设置布局文件
     */
    public abstract void setContentView();

    /**
     * 初始化控件
     */
    public abstract void initViews();

    /**
     * 初始化监听事件
     */
    public abstract void initListeners();

    /**
     * 进行数据初始化
     */
    public abstract void initData();

    Toast mToast;
    public void ShowToast(String text){
        if (!TextUtils.isEmpty(text)){
            if (mToast == null) {
                mToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
            }
            mToast.show();
        }
    }

    /**
     * 获取当前状态栏高度
     */
    public int getStateBar(){
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        return statusBarHeight;
    }

    public static int dip2px(Context context,float dipValue){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (scale*dipValue+0.5f);
    }
}
