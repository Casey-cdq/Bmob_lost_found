package com.jikexueyuan.bmob_lost_found;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jikexueyuan.bmob_lost_found.bean.Found;
import com.jikexueyuan.bmob_lost_found.bean.Lost;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static com.jikexueyuan.bmob_lost_found.R.id.btn_back;
import static com.jikexueyuan.bmob_lost_found.R.id.btn_true;

/**
 * Created by 薄云雨季 on 2017/9/12.
 */

public class AddActivity extends BaseActivity implements View.OnClickListener{


    EditText editTitle,editPhoto,editDescribe;
    Button btnBack,btnTrue;
    TextView tvAdd;

    String from = "";
    String old_title = "";
    String old_describe = "";
    String old_phone = "";

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_add);
    }

    @Override
    public void initViews() {
        tvAdd = (TextView) findViewById(R.id.tv_add);
        btnBack = (Button) findViewById(R.id.btn_back);
        btnTrue = (Button) findViewById(R.id.btn_true);
        editPhoto = (EditText) findViewById(R.id.edit_photo);
        editDescribe = (EditText) findViewById(R.id.edit_describe);
        editTitle = (EditText) findViewById(R.id.edit_title);
    }

    @Override
    public void initListeners() {
        btnBack.setOnClickListener(this);
        btnTrue.setOnClickListener(this);
    }

    @Override
    public void initData() {
        from = getIntent().getStringExtra("from");
        old_title = getIntent().getStringExtra("title");
        old_phone = getIntent().getStringExtra("phone");
        old_describe = getIntent().getStringExtra("describe");

        editTitle.setText(old_title);
        editDescribe.setText(old_describe);
        editPhoto.setText(old_phone);

        if (from.equals("Lost")) {
            tvAdd.setText("添加失物信息");
        } else {
            tvAdd.setText("添加招领信息");
        }
    }

    String title = "";
    String describe = "";
    String phone = "";

    /**
     * 根据类型添加失物/招领
     */
    private void addByType(){
        title = editTitle.getText().toString();
        describe = editDescribe.getText().toString();
        phone = editPhoto.getText().toString();

        if (TextUtils.isEmpty(title)) {
            ShowToast("请填写标题");
        }
        if (TextUtils.isEmpty(describe)) {
            ShowToast("请填写描述");
        }
        if (TextUtils.isEmpty(phone)) {
            ShowToast("请填写手机");
        }
        if (from.equals("Lost")) {
            addLost();
        } else {
            addFound();
        }
    }

    private void addLost() {
        Lost lost = new Lost();
        lost.setDescribe(describe);
        lost.setPhone(phone);
        lost.setTitle(title);
        lost.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                ShowToast("失物信息添加成功！");
                setResult(RESULT_OK);
                finish();
            }
        });

    }

    private void addFound() {
        Found found = new Found();
        found.setDescribe(describe);
        found.setTitle(title);
        found.setPhone(phone);
        found.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                ShowToast("招领信息添加成功！");
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case btn_back:
                finish();
                break;
            case btn_true:
                addByType();
                break;
        }
    }
}
