package com.glazbeni.org.clockt;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.IntDef;

import com.glazbeni.org.clockt.Activity.PlayAlarm;

public class AlarmService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // 设置闹钟在十分钟后重新响起
        int delayTime = 1000 * 60 * 10;

        // 获取系统开机至今所经历时间的毫秒数
        // 可通过System.currentTimeMillis()方法获取到1970年1月1日0点至今所经历时间的毫秒数
        long triggerTime = SystemClock.elapsedRealtime() + delayTime;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        manager.set(AlarmManager.RTC_WAKEUP, triggerTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }
}
