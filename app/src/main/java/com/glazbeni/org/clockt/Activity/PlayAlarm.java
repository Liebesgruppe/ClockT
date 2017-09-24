package com.glazbeni.org.clockt.Activity;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.glazbeni.org.clockt.AlarmService;
import com.glazbeni.org.clockt.R;

import static android.content.ContentValues.TAG;

/**
 * Created by Glazbeni on 17.7.5.
 * 闹钟响起时调用的Activity
 */

public class PlayAlarm extends Activity implements View.OnClickListener {

    private MediaPlayer mp;
    private Vibrator vibrator;
    private ImageView iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_alarm);

        Button btnCancel = (Button) findViewById(R.id.btn_cancel);
        Button btnDelay = (Button) findViewById(R.id.btn_delay);
        iv = (ImageView) findViewById(R.id.iv_alarm);
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);

        mp = MediaPlayer.create(this, R.raw.alarm);
        mp.setLooping(true);
        mp.start();
        draw();

        // vibrate()接收两个参数
        // 第一个参数为一个长整型数组，其中下标为0的表示等待多长时间才开始震动，其后依次为震动、静止的时间
        // 第二个参数若为-1则表示不重复震动
        vibrator.vibrate(new long[]{0, 300, 1500, 300}, 1);

        btnCancel.setOnClickListener(this);
        btnDelay.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        mp.stop();
        mp.release();
        vibrator.cancel();
        super.onDestroy();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_delay:
                Intent i = new Intent(this, AlarmService.class);
                startService(i);
                finish();
                break;
        }
    }

    private void draw() {
        Drawable drawable = iv.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

}
