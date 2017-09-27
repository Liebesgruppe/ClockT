package com.glazbeni.org.clockt.Layout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.glazbeni.org.clockt.R;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Glazbeni on 17.7.6.
 * 自定义计时器布局
 */

public class TimerView extends LinearLayout implements View.OnClickListener {

    private EditText etHour, etMin, etSec;
    private Button btnStartPause, btnReset;
    private Timer timer;
    private TimerTask task;
    private int totalTime;
    private int hour, min, sec;
    private DecimalFormat df;
    private boolean checkEt;
    private static final int TIMER_STOP = 0;
    private static final int TIMER_UPDATE = 1;

    public TimerView(Context context) {
        super(context);
    }

    public TimerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        df = new DecimalFormat("#00");
        checkEt = true;

        etHour = (EditText) findViewById(R.id.et_hour);
        etMin = (EditText) findViewById(R.id.et_min);
        etSec = (EditText) findViewById(R.id.et_sec);

        btnStartPause = (Button) findViewById(R.id.btn_start);
        btnReset = (Button) findViewById(R.id.btn_reset);

        btnStartPause.setOnClickListener(this);
        btnReset.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                if (checkEnableBtnStart()) {
                    if (checkEt) {
                        start();
                        btnStartPause.setText(getResources().getText(R.string.pause));
                        checkEt = false;
                    } else {
                        stop();
                        btnStartPause.setText(getResources().getText(R.string.start));
                        checkEt = true;
                    }
                }
                break;
            case R.id.btn_reset:
                stop();
                btnStartPause.setText(getResources().getText(R.string.start));
                checkEt = true;
                etHour.setText(getResources().getText(R.string.zeros));
                etMin.setText(getResources().getText(R.string.zeros));
                etSec.setText(getResources().getText(R.string.zeros));
                break;
        }
    }

    private boolean checkEnableBtnStart() {
        return  !TextUtils.isEmpty(etHour.getText()) &&
                Integer.parseInt(etHour.getText().toString()) > 0 ||
                !TextUtils.isEmpty(etMin.getText()) &&
                        Integer.parseInt(etMin.getText().toString()) > 0 ||
                !TextUtils.isEmpty(etSec.getText()) &&
                        Integer.parseInt(etSec.getText().toString()) > 0;
    }

    private void start() {
        if (task == null) {
            timer = new Timer();
            checkEt = true;
            totalTime = Integer.parseInt(etHour.getText().toString()) * 60 * 60
                    + Integer.parseInt(etMin.getText().toString()) * 60
                    + Integer.parseInt(etSec.getText().toString());
            task = new TimerTask() {
                @Override
                public void run() {
                    totalTime--;
                    hour = totalTime / 60 / 60;
                    min = totalTime / 60;
                    sec = totalTime % 60;
                    handler.sendEmptyMessage(TIMER_UPDATE);
                    if (totalTime <= 0) {
                        handler.sendEmptyMessage(TIMER_STOP);
                        stop();
                    }
                }
            };
            timer.schedule(task, 1000, 1000);
        }
    }

    private void stop() {
        if (task != null) {

            // 终止计时器取消当前计划的任何任务
            timer.cancel();

            // 从计时器队列中清除所有已取消的任务
            timer.purge();
            task = null;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIMER_STOP:
                    new AlertDialog.Builder(getContext()).setTitle("提示").setMessage("吉时已到")
                            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    btnReset.setVisibility(GONE);
                                    btnStartPause.setText(getResources().getText(R.string.start));
                                    checkEt = false;
                                }
                            }).setCancelable(false).show();
                    break;
                case TIMER_UPDATE:
                    etHour.setText(df.format(hour));
                    etMin.setText(df.format(min));
                    etSec.setText(df.format(sec));
                    break;
            }
        }
    };
}
