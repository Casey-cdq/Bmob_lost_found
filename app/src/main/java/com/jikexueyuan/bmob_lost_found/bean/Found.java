package com.jikexueyuan.bmob_lost_found.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 薄云雨季 on 2017/9/13.
 */

public class Found extends BmobObject{
    private String title;
    private String describe;
    private String phone;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
