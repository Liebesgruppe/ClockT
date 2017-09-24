package com.glazbeni.org.clockt;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.glazbeni.org.clockt.Activity.PlayAlarm;
import com.glazbeni.org.clockt.Layout.AlarmTextView;

/**
 * Created by Glazbeni on 17.7.5.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(PendingIntent.getBroadcast(context, getResultCode(), new Intent(context,AlarmTextView.class), 0));

        Intent i = new Intent(context, PlayAlarm.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
