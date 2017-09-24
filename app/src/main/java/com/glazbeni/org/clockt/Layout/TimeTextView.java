package com.glazbeni.org.clockt.Layout;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glazbeni.org.clockt.Fragment.TimeFragment;
import com.glazbeni.org.clockt.R;

import java.text.DecimalFormat;
import java.util.Calendar;

import static android.content.ContentValues.TAG;

/**
 * Created by Glazbeni on 17.6.26.
 * 显示当前时间的主布局
 */

public class TimeTextView extends LinearLayout {

    private TextView tvTime;

    public TimeTextView(Context context) {
        super(context);
    }

    public TimeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvTime = (TextView) findViewById(R.id.fm_tv_time);
        mHandler.sendEmptyMessage(0);
    }

    /**
     * 判断当前view的可见性
     * @param changedView
     * @param visibility
     */
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.VISIBLE) {
            // 如果可见则刷新时间
            mHandler.sendEmptyMessage(0);
        } else {
            // 若不可见则停止刷新并清空Message
            mHandler.removeMessages(0);
        }
    }

    /**
     * 刷新时间
     */
    private void refreshTime() {
        DecimalFormat df = new DecimalFormat("#00");
        Calendar calendar = Calendar.getInstance();

        tvTime.setText(String.format("%s:%s:%s",df.format(calendar.get(Calendar.HOUR_OF_DAY)),
                df.format(calendar.get(Calendar.MINUTE)),df.format(calendar.get(Calendar.SECOND))));
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            refreshTime();

            if (getVisibility() == View.VISIBLE) {
                mHandler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };
}
