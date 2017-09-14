package com.jikexueyuan.bmob_lost_found.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import static com.jikexueyuan.bmob_lost_found.adapter.BaseAdapterHelper.get;

/**
 * Created by 薄云雨季 on 2017/9/13.
 */

public abstract class QuickAdapter<T> extends BaseQuickAdapter<T,BaseAdapterHelper> {
    public QuickAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    public QuickAdapter(Context context, int layoutResId, List<T> data) {
        super(context, layoutResId, data);
    }

    protected BaseAdapterHelper getAdapterHelper(int position, View convertView, ViewGroup parent) {
        return get(context,convertView,parent,layoutResId,position);
    }
}
