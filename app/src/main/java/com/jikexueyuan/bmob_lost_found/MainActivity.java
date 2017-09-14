package com.jikexueyuan.bmob_lost_found;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jikexueyuan.bmob_lost_found.adapter.BaseAdapterHelper;
import com.jikexueyuan.bmob_lost_found.adapter.QuickAdapter;
import com.jikexueyuan.bmob_lost_found.base.EditPopupWindow;
import com.jikexueyuan.bmob_lost_found.bean.Found;
import com.jikexueyuan.bmob_lost_found.bean.Lost;
import com.jikexueyuan.bmob_lost_found.i.IPopupItemClick;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static android.R.attr.tag;
import static com.jikexueyuan.bmob_lost_found.R.id.btn_add;
import static com.jikexueyuan.bmob_lost_found.R.id.layout_all;
import static com.jikexueyuan.bmob_lost_found.R.id.tv_lost;

public class MainActivity extends BaseActivity implements IPopupItemClick, AdapterView.OnItemLongClickListener, View.OnClickListener {


    RelativeLayout layoutAction;
    LinearLayout layoutAll;
    TextView tvLost;
    ListView lostList;
    Button btnAdd;
    RelativeLayout progress;
    LinearLayout layoutNo;
    TextView tvNo;

    protected QuickAdapter<Lost> LostAdapter;
    protected QuickAdapter<Found> FoundAdapter;

    private Button layout_found;
    private Button layout_lost;

    PopupWindow morePop;


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_main);
    }


    @Override
    public void initViews() {
        progress = (RelativeLayout) findViewById(R.id.progress);
        layoutNo = (LinearLayout) findViewById(R.id.layout_no);
        tvNo = (TextView) findViewById(R.id.tv_no);

        layoutAction = (RelativeLayout) findViewById(R.id.layout_action);
        layoutAll = (LinearLayout) findViewById(layout_all);
        // 默认是失物界面
        tvLost = (TextView) findViewById(tv_lost);
        tvLost.setTag("Lost");
        lostList = (ListView) findViewById(R.id.lost_list);
        btnAdd = (Button) findViewById(btn_add);
        // 初始化长按弹窗
        initEditPop();
    }

    @Override
    public void initListeners() {
        lostList.setOnItemLongClickListener(MainActivity.this);
        btnAdd.setOnClickListener(MainActivity.this);
        layoutAll.setOnClickListener(MainActivity.this);
    }

    @Override
    public void initData() {
        if (LostAdapter == null) {
            LostAdapter = new QuickAdapter<Lost>(this, R.layout.item_list) {
                @Override
                protected void convert(BaseAdapterHelper helper, Lost item) {
                    helper.setText(R.id.tv_title, item.getTitle())
                            .setText(R.id.tv_describe, item.getDescribe())
                            .setText(R.id.tv_time, item.getCreatedAt())
                            .setText(R.id.tv_phone, item.getPhone());
                }
            };
        }
        if (FoundAdapter == null) {
            FoundAdapter = new QuickAdapter<Found>(this, R.layout.item_list) {
                @Override
                protected void convert(BaseAdapterHelper helper, Found item) {
                    helper.setText(R.id.tv_title, item.getTitle())
                            .setText(R.id.tv_describe, item.getDescribe())
                            .setText(R.id.tv_time, item.getCreatedAt())
                            .setText(R.id.tv_phone, item.getPhone());
                }
            };
        }
        lostList.setAdapter(LostAdapter);
        queryLosts();
    }

    private void showErrorView(int i) {
        progress.setVisibility(View.GONE);
        lostList.setVisibility(View.GONE);
        layoutNo.setVisibility(View.VISIBLE);
        if (tag == 0) {
            tvNo.setText(getResources().getText(R.string.list_no_data_lost));
        } else {
            tvNo.setText(getResources().getText(R.string.list_no_data_found));
        }
    }

    private void showView() {
        lostList.setVisibility(View.VISIBLE);
        layoutNo.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_all:
                showListPop();
                break;
            case R.id.btn_add:
                Intent intent = new Intent(this, AddActivity.class);
                intent.putExtra("from", tvLost.getTag().toString());
                startActivityForResult(intent, 1);
                break;
            case R.id.layout_found:
                changeTextView(view);
                morePop.dismiss();
                queryFounds();
                break;
            case R.id.layout_lost:
                changeTextView(view);
                morePop.dismiss();
                queryLosts();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 1:
                String tag = tvLost.getTag().toString();
                if (tag.equals("Lost")) {
                    queryLosts();
                } else {
                    queryFounds();
                }
                break;
        }
    }

    private void changeTextView(View view) {
        if (view == layout_found) {
            tvLost.setTag("Found");
            tvLost.setText("Found");
        } else {
            tvLost.setTag("Lost");
            tvLost.setText("Lost");
        }
    }

    private void queryFounds() {
        showView();
        BmobQuery<Found> query = new BmobQuery<Found>();
        query.order("-createdAt");
        query.findObjects(new FindListener<Found>() {
            @Override
            public void done(List<Found> list, BmobException e) {
                LostAdapter.clear();
                FoundAdapter.clear();
                if (list == null || list.size() == 0) {
                    showErrorView(1);
                    FoundAdapter.notifyDataSetChanged();
                    return;
                }
                FoundAdapter.addAll(list);
                lostList.setAdapter(FoundAdapter);
                progress.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 查询全部失物信息
     */
    private void queryLosts() {
        showView();
        BmobQuery<Lost> query = new BmobQuery<Lost>();
        query.order("-createdAt");//按时间降序
        query.findObjects(new FindListener<Lost>() {
            @Override
            public void done(List<Lost> list, BmobException e) {
                LostAdapter.clear();
                FoundAdapter.clear();
                if (list == null || list.size() == 0) {
                    showErrorView(0);
                    LostAdapter.notifyDataSetChanged();
                    return;
                }
                progress.setVisibility(View.GONE);
                LostAdapter.addAll(list);
                lostList.setAdapter(LostAdapter);
            }

        });
    }

    private void showListPop() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_lost, null);
        layout_found = view.findViewById(R.id.layout_found);
        layout_lost = view.findViewById(R.id.layout_lost);
        layout_found.setOnClickListener(this);
        layout_lost.setOnClickListener(this);
        morePop = new PopupWindow(view, mScreenWidth, 600);
        morePop.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    morePop.dismiss();
                    return true;
                }
                return false;
            }
        });
        morePop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        morePop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        morePop.setTouchable(true);
        morePop.setFocusable(true);
        morePop.setOutsideTouchable(true);
        morePop.setBackgroundDrawable(new BitmapDrawable());
        //动画效果从顶部弹下
        morePop.setAnimationStyle(R.style.MenuPop);
        morePop.showAsDropDown(layoutAction, 0, -dip2px(this, 2.0f));
    }

    EditPopupWindow mPopupWindow;
    int position;

    private void initEditPop() {
        mPopupWindow = new EditPopupWindow(this, 200, 48);
        mPopupWindow.setOnPopupItemClickListner(this);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
        position = arg2;
        int[] location = new int[2];
        arg1.getLocationOnScreen(location);
        mPopupWindow.showAtLocation(arg1, Gravity.RIGHT | Gravity.TOP,
                location[0], getStateBar() + location[1]);
        return false;
    }

    @Override
    public void onEdit(View v) {
        String tag = tvLost.getTag().toString();
        Intent intent = new Intent(this, AddActivity.class);
        String title = "";
        String describe = "";
        String phone = "";
        if (tag.equals("Lost")) {
            title = LostAdapter.getItem(position).getTitle();
            describe = LostAdapter.getItem(position).getDescribe();
            phone = LostAdapter.getItem(position).getPhone();
        } else {
            title = FoundAdapter.getItem(position).getTitle();
            describe = FoundAdapter.getItem(position).getDescribe();
            phone = FoundAdapter.getItem(position).getPhone();
        }
        intent.putExtra("describe", describe);
        intent.putExtra("phone", phone);
        intent.putExtra("title", title);
        intent.putExtra("from", tag);
        startActivityForResult(intent, 1);

    }

    @Override
    public void onDelete(View v) {
        String tag = tvLost.getTag().toString();
        if (tag.equals("Lost")) {
            deleteLost();
        } else {
            deleteFound();
        }
    }

    private void deleteLost() {
        Lost lost = new Lost();
        lost.setObjectId(LostAdapter.getItem(position).getObjectId());
        lost.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                LostAdapter.remove(position);
            }
        });
    }

    private void deleteFound() {
        Found found = new Found();
        found.setObjectId(FoundAdapter.getItem(position).getObjectId());
        found.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                FoundAdapter.remove(position);
            }
        });
    }


}
