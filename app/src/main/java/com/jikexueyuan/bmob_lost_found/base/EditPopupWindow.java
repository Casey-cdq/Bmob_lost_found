package com.jikexueyuan.bmob_lost_found.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jikexueyuan.bmob_lost_found.R;
import com.jikexueyuan.bmob_lost_found.i.IPopupItemClick;

/**
 * Created by 薄云雨季 on 2017/9/13.
 */

public class EditPopupWindow extends BasePopupWindow implements View.OnClickListener{
    private TextView mEdit;
    private TextView mDelete;
    private IPopupItemClick mOnPopupItemClickListner;

    public EditPopupWindow(Context context, int width, int height) {
        super(LayoutInflater.from(context).inflate(R.layout.pop_device,null),dpToPx(context,width),dpToPx(context,height));
        setAnimationStyle(R.style.PopupAnimation);
    }

    private static int dpToPx(Context context,int dp){
        return (int) (context.getResources().getDisplayMetrics().density * dp + 0.5f);
    }
    @Override
    protected void initViews() {
        mEdit = (TextView) findViewById(R.id.tv_pop_edit);
        mDelete = (TextView) findViewById(R.id.tv_pop_delete);
    }

    @Override
    protected void initEvents() {
        mEdit.setOnClickListener(this);
        mDelete.setOnClickListener(this);
    }

    @Override
    protected void init() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_pop_edit:
                if (mOnPopupItemClickListner != null) {
                    mOnPopupItemClickListner.onEdit(view);
                }
                break;
            case R.id.tv_pop_delete:
                if (mOnPopupItemClickListner != null){
                    mOnPopupItemClickListner.onDelete(view);
                }
                break;
        }
        dismiss();
    }

    public void setOnPopupItemClickListner(IPopupItemClick l) {
        mOnPopupItemClickListner = l;
    }
}
