package com.glazbeni.org.clockt.Activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.glazbeni.org.clockt.Fragment.AlarmFragment;
import com.glazbeni.org.clockt.Fragment.StopwatchFragment;
import com.glazbeni.org.clockt.Fragment.TimeFragment;
import com.glazbeni.org.clockt.Fragment.TimerFragment;
import com.glazbeni.org.clockt.MFragmentAdapter;
import com.glazbeni.org.clockt.R;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    private TextView tvTime, tvAlarm, tvTimer, tvStopwatch;
    private ImageView ivLine;
    private ViewPager mViewPager;
    private int position_one, position_two, position_three;
    private int currIndex;
    private ArrayList<Fragment> fragmentArrayList;
    private FragmentManager fragmentManager;
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTextView();
        initImageView();
        initFragment();
        initViewPager();
    }

    private void initTextView() {

        tvTime = (TextView) findViewById(R.id.tv_time);
        tvAlarm = (TextView) findViewById(R.id.tv_alarm);
        tvTimer = (TextView) findViewById(R.id.tv_timer);
        tvStopwatch = (TextView) findViewById(R.id.tv_stopwatch);

        tvTime.setOnClickListener(new MyOnClickListener(1));
        tvAlarm.setOnClickListener(new MyOnClickListener(0));
        tvTimer.setOnClickListener(new MyOnClickListener(2));
        tvStopwatch.setOnClickListener(new MyOnClickListener(3));
    }

    private void initViewPager() {

        mViewPager = (ViewPager) findViewById(R.id.vp_clock);
        mViewPager.setAdapter(new MFragmentAdapter(fragmentManager, fragmentArrayList));

        //让ViewPager缓存3个页面
        mViewPager.setOffscreenPageLimit(3);

        //设置默认打开第1页
        mViewPager.setCurrentItem(0);

        //将顶部文字恢复默认值
        restTextViewTextColor();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tvAlarm.setTextColor(getResources().getColor(R.color.colorSet, null));
        }

        //设置viewPager页面滑动监听事件
        mViewPager.addOnPageChangeListener(new MyAddPageChangeListener());
    }

    private void initImageView() {
        ivLine = (ImageView) findViewById(R.id.iv_line);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //获取分辨率宽度
        int screenW = dm.widthPixels;

        int bmpW = screenW / 4;

        //设置动画图片宽度
        setBmpW(ivLine, bmpW);

        //动画图片偏移量赋值
        position_one = screenW / 4;
        position_two = position_one * 2;
        position_three = position_one * 3;
    }

    /**
     * 初始化Fragment，并加入到ArrayList中
     */
    private void initFragment() {

        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new AlarmFragment());
        fragmentArrayList.add(new TimeFragment());
        fragmentArrayList.add(new TimerFragment());
        fragmentArrayList.add(new StopwatchFragment());

        fragmentManager = getSupportFragmentManager();
    }

    /**
     * 将顶部文字恢复默认值
     */
    private void restTextViewTextColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tvTime.setTextColor(getResources().getColor(R.color.colorAuto, null));
            tvAlarm.setTextColor(getResources().getColor(R.color.colorAuto, null));
            tvTimer.setTextColor(getResources().getColor(R.color.colorAuto, null));
            tvStopwatch.setTextColor(getResources().getColor(R.color.colorAuto, null));
        }
    }

    /**
     * 设置动画图片宽度
     */
    private void setBmpW(ImageView imageView, int mWidth) {
        ViewGroup.LayoutParams params;
        params = imageView.getLayoutParams();
        params.width = mWidth;
        imageView.setLayoutParams(params);
    }

    /**
     * 点击监听事件
     */
    private class MyOnClickListener implements View.OnClickListener {

        private int index = 0;

        private MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View view) {
            mViewPager.setCurrentItem(index);
        }
    }

    /**
     * 页面切换监听
     */
    private class MyAddPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            Animation animation = null;
            switch (position) {

                //跳转到页面1
                case 0:
                    if (currIndex == 1) { //如果当前为页面2
                        //TranslateAnimation为位移动画效果
                        animation = new TranslateAnimation(position_one, 0, 0, 0);
                    } else if (currIndex == 3) { //如果当前为页面3
                        animation = new TranslateAnimation(position_three, 0, 0, 0);
                    } else if (currIndex == 2) { //如果当前为页面4
                        animation = new TranslateAnimation(position_two, 0, 0, 0);
                    }
                    restTextViewTextColor();
                    tvAlarm.setTextColor(getResources().getColor(R.color.colorSet, null));
                    break;

                //跳转到页面2
                case 1:
                    if (currIndex == 0) { //如果当前为页面1
                        //TranslateAnimation为位移动画效果
                        animation = new TranslateAnimation(0, position_one, 0, 0);
                    } else if (currIndex == 2) { //如果当前为页面3
                        animation = new TranslateAnimation(position_two, position_one, 0, 0);
                    } else if (currIndex == 3) { //如果当前为页面4
                        animation = new TranslateAnimation(position_three, position_one, 0, 0);
                    }
                    restTextViewTextColor();
                    tvTime.setTextColor(getResources().getColor(R.color.colorSet, null));
                    break;

                //跳转到页面3
                case 2:
                    if (currIndex == 0) { //如果当前为页面1
                        //TranslateAnimation为位移动画效果
                        animation = new TranslateAnimation(0, position_two, 0, 0);
                    } else if (currIndex == 1) { //如果当前为页面2
                        animation = new TranslateAnimation(position_one, position_two, 0, 0);
                    } else if (currIndex == 3) { //如果当前为页面4
                        animation = new TranslateAnimation(position_three, position_two, 0, 0);
                    }
                    restTextViewTextColor();
                    tvTimer.setTextColor(getResources().getColor(R.color.colorSet, null));
                    break;

                //跳转到页面4
                case 3:
                    if (currIndex == 0) { //如果当前为页面1
                        //TranslateAnimation为位移动画效果
                        animation = new TranslateAnimation(0, position_three, 0, 0);
                    } else if (currIndex == 1) { //如果当前为页面2
                        animation = new TranslateAnimation(position_one, position_three, 0, 0);
                    } else if (currIndex == 2) { //如果当前为页面3
                        animation = new TranslateAnimation(position_two, position_three, 0, 0);
                    }
                    restTextViewTextColor();
                    tvStopwatch.setTextColor(getResources().getColor(R.color.colorSet, null));
                    break;
            }
            currIndex = position;

            if (animation != null) {
                animation.setFillAfter(true);//图片停在动画结束位置
                animation.setDuration(150);
                ivLine.startAnimation(animation);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
