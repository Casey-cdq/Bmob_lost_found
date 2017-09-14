package com.jikexueyuan.bmob_lost_found;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

/**
 * Created by 薄云雨季 on 2017/9/12.
 */

public class SplashActivity extends BaseActivity {


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initListeners() {

    }

    @Override
    public void initData() {
        mHandler.sendEmptyMessageDelayed(GO_HOME, 2000);
    }

    public void goHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
    private static final int GO_HOME=100;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GO_HOME:
                    goHome();
                    break;
            }
        }
    };
}
