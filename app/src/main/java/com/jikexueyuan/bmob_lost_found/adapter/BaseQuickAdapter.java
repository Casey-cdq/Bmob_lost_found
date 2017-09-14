package com.jikexueyuan.bmob_lost_found.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 薄云雨季 on 2017/9/13.
 */

public abstract class BaseQuickAdapter<T,H extends BaseAdapterHelper> extends BaseAdapter {
    protected static final String TAG = BaseQuickAdapter.class.getSimpleName();
    protected final Context context;
    protected final int layoutResId;
    protected final List<T> data;
    protected boolean displayIndeterminateProgress = false;
    public BaseQuickAdapter(Context context,int layoutResId) {
        this(context, layoutResId,null);
    }

    public BaseQuickAdapter(Context context, int layoutResId, List<T> data) {
        this.data = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
        this.context = context;
        this.layoutResId = layoutResId;
    }

    @Override
    public int getCount() {
        int extra = displayIndeterminateProgress ? 1 : 0;
        return data.size()+extra;
    }

    @Override
    public T getItem(int i) {
        if (i >= data.size()) return null;
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position >= data.size() ? 1:0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (getItemViewType(i) == 0) {
            final H helper = getAdapterHelper(i, view, viewGroup);
            convert(helper,getItem(i));
            return helper.getView();
        }
        return createIndeterminateProgressView(view,viewGroup);
    }

    private View createIndeterminateProgressView(View view, ViewGroup viewGroup) {
        if (view == null) {
            FrameLayout container = new FrameLayout(context);
            container.setForegroundGravity(Gravity.CENTER);
            ProgressBar progress = new ProgressBar(context);
            container.addView(progress);
            view = container;
        }
        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        return position < data.size();
    }

    public void add(T elem) {
        data.add(elem);
    }

    public void addAll(List<T> elem) {
        data.addAll(elem);
        notifyDataSetChanged();
    }

    public void set(T oldElem, T newElem) {
        set(data.indexOf(oldElem),newElem);
    }

    public void set(int index, T elem) {
        data.set(index, elem);
        notifyDataSetChanged();
    }

    public void remove(T elem) {
        data.remove(elem);
        notifyDataSetChanged();
    }

    public void remove(int index) {
      data.remove(index);
        notifyDataSetChanged();
    }

    public boolean contains(T elem) {
        return data.contains(elem);
    }

    public void clear(){
        data.clear();
        notifyDataSetChanged();
    }

    public void showIndeterminateProgress(boolean display) {
        if (display == displayIndeterminateProgress) return;
        displayIndeterminateProgress = display;
        notifyDataSetChanged();
    }
    protected abstract void convert(H helper, T item) ;

    protected abstract H getAdapterHelper(int i, View view, ViewGroup viewGroup) ;
}
