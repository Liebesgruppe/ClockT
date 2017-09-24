package com.glazbeni.org.clockt.Layout;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import com.glazbeni.org.clockt.AlarmReceiver;
import com.glazbeni.org.clockt.R;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by Glazbeni on 17.7.5.
 * 自定义闹钟布局
 */

public class AlarmTextView extends LinearLayout {

    private ArrayAdapter<AlarmDate> adapter;
    public static final String KEY_ALARM_LIST = "alarm";
    private AlarmManager manager;

    public AlarmTextView(Context context) {
        super(context);
        init();
    }

    public AlarmTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlarmTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        manager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        Button btnAdd = (Button) findViewById(R.id.btn_add);
        ListView lv = (ListView) findViewById(R.id.lv_alarm);
        adapter = new ArrayAdapter<>(getContext(), R.layout.listview_item, R.id.tv_list_time);
        lv.setAdapter(adapter);

        // 从SharedPreference中读取闹钟列表
        read();

        btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // 添加一个闹钟
                addAlarm();
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                new AlertDialog.Builder(getContext()).setTitle("选项").setItems(new CharSequence[]{"删除"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                // 长按删除闹钟条目
                                delete(position);
                                break;
                            default:
                                break;
                        }
                    }
                }).setNegativeButton("取消", null).show();
                return true;
            }
        });
    }

    private static class AlarmDate {

        private String timeLabel;
        private long date;
        private Calendar calendar;
        private DecimalFormat df;

        private AlarmDate(long date) {
            this.date = date;
            calendar = Calendar.getInstance();
            df = new DecimalFormat("#00");

            calendar.setTimeInMillis(date);
            timeLabel = String.format("%s:%s",
                    df.format(calendar.get(Calendar.HOUR_OF_DAY)),
                    df.format(calendar.get(Calendar.MINUTE)));
        }


        private String getTimeLabel() {
            return timeLabel;
        }

        private long getDate() {
            return date;
        }

        // 将当前时间作为requestCode
        public int getId() {
            // 由于long类型范围要比int类型大，进行运算后可以在一定程度上减小强转时的风险
            return (int) (getDate() / 1000 / 60);
        }

        @Override
        public String toString() {
            return getTimeLabel();
        }
    }

    /**
     * 添加一个闹钟
     */
    public void addAlarm() {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, i);
                c.set(Calendar.MINUTE, i1);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);

                Calendar currentTime = Calendar.getInstance();

                if (c.getTimeInMillis() <= currentTime.getTimeInMillis()) {
                    c.setTimeInMillis(c.getTimeInMillis() + 24 * 60 * 60 * 1000);
                }
                AlarmDate ad = new AlarmDate(c.getTimeInMillis());
                adapter.add(ad);
                manager.set(AlarmManager.RTC_WAKEUP, ad.getDate(), PendingIntent.getBroadcast(getContext(),
                        ad.getId(),
                        new Intent(getContext(), AlarmReceiver.class),
                        0));
                save();
            }
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }

    private void save() {
        SharedPreferences.Editor editor = getContext().getSharedPreferences("AlarmList", Context.MODE_PRIVATE).edit();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < adapter.getCount(); i++) {
            builder.append(adapter.getItem(i).getDate()).append(",");
        }

        if (builder.length() > 1) {
            String content = builder.toString().substring(0, builder.length() - 1);
            editor.putString(KEY_ALARM_LIST, content);
        } else {
            editor.putString(KEY_ALARM_LIST, null);
        }
        editor.apply();
    }

    private void read() {
        SharedPreferences preferences = getContext().getSharedPreferences("AlarmList", Context.MODE_PRIVATE);
        String content = preferences.getString(KEY_ALARM_LIST, null);
        if (content != null) {
            String[] timeStrings = content.split(",");
            for (String string : timeStrings) {
                adapter.add(new AlarmDate(Long.parseLong(string)));
            }
        }
    }

    private void delete(int position) {
        AlarmDate ad = adapter.getItem(position);
        adapter.remove(ad);
        save();
        manager.cancel(PendingIntent.getBroadcast(getContext(),
                ad.getId(), new Intent(getContext(), AlarmReceiver.class), 0));
    }
}
