package com.glazbeni.org.clockt.Layout;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.glazbeni.org.clockt.R;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Glazbeni on 17.7.7.
 * 自定义秒表布局
 */

public class StopWatchView extends LinearLayout implements View.OnClickListener {

    private TextView tvMin, tvSec, tvMSec;
    private Button btnSwStart, btnSwRecord;
    private Timer timer;
    private TimerTask timerTask;
    private int swTime;
    private ArrayAdapter<String> adapter;
    private boolean isStart;
    private DecimalFormat df;
    private static final int MSG_WHAT_SHOW_TIME = 1;

    public StopWatchView(Context context) {
        super(context);
    }

    public StopWatchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StopWatchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvMin = (TextView) findViewById(R.id.tv_min);
        tvSec = (TextView) findViewById(R.id.tv_sec);
        tvMSec = (TextView) findViewById(R.id.tv_mSec);

        btnSwStart = (Button) findViewById(R.id.btn_sw_start);
        btnSwRecord = (Button) findViewById(R.id.btn_sw_record);

        isStart = true;
        df = new DecimalFormat("#00");

        ListView listView = (ListView) findViewById(R.id.lv_stopwatch);
        adapter = new ArrayAdapter<>(getContext(), R.layout.listview_sw_item);
        listView.setAdapter(adapter);

        btnSwStart.setOnClickListener(this);
        btnSwRecord.setOnClickListener(this);

        timer = new Timer();
        TimerTask showTimerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(MSG_WHAT_SHOW_TIME);
            }
        };
        timer.schedule(showTimerTask, 50, 50);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_sw_start:
                if (isStart) {
                    startSw();
                    btnSwStart.setText("暂停");
                    btnSwRecord.setVisibility(VISIBLE);
                    btnSwRecord.setText("记录");
                    isStart = false;
                } else {
                    pauseSw();
                    btnSwStart.setText("开始");
                    btnSwRecord.setText("重置");
                    isStart = true;
                }
                break;

            case R.id.btn_sw_record:
                if (isStart) {
                    pauseSw();
                    swTime = 0;
                    adapter.clear();
                    btnSwRecord.setVisibility(GONE);
                } else {
                    adapter.insert(String.format("%s %s.%s",
                            swTime / 100 / 60 % 60,
                            df.format(swTime / 100 % 60), df.format(swTime % 100)), 0);
                }
                break;
        }
    }

    /**
     * 开始计时
     */
    private void startSw() {

        timer = new Timer();
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    swTime++;
                }
            };
            timer.schedule(timerTask, 10, 10);
        }
    }


    /**
     * 暂停计时
     */
    private void pauseSw() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case MSG_WHAT_SHOW_TIME:
                    tvMin.setText(String.valueOf(swTime / 100 / 60 % 60));
                    tvSec.setText(df.format(swTime / 100 % 60));
                    tvMSec.setText(df.format(swTime % 100));
                    break;

            }
        }
    };
}
