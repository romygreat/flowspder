package com.spbider.luominming.flowspider;

import android.os.CountDownTimer;
import android.util.Log;

/**
 * Created by luominming on 2018/3/16.
 */

public class CounTimeUtils {
    private static String daoji;
    public static void  startCountDownTime(long time) {
        CountDownTimer timer = new CountDownTimer(time * 1000, 1000) {
            /**
             * 最简单的倒计时类，实现了官方的CountDownTimer类（没有特殊要求的话可以使用）
             * 即使退出activity，倒计时还能进行，因为是创建了后台的线程。
             * 有onTick，onFinsh、cancel和start方法
             */
            @Override
            public void onTick(long millisUntilFinished) {
                //每隔countDownInterval秒会回调一次onTick()方法
                Log.d("test", "onTick  " + millisUntilFinished / 1000);
                Log.d("formate", "onTick: "+  formatSecond(millisUntilFinished / 1000));
            }
            @Override
            public void onFinish() {
                Log.d("test", "onFinish -- 倒计时结束");
            }
        };
        timer.start();// 开始计时
    }
    public static long getDateSeconds(){
        long dateSecond=24*60*60;
        return dateSecond;
    }
    public static String formatSecond(long second){
        String  html="0秒";
//        if(second!=null)
        {
            long s=second;
            String format;
            Object[] array;
            Integer hours =(int) (s/(60*60));
            Integer minutes = (int) (s/60-hours*60);
            Integer seconds = (int) (s-minutes*60-hours*60*60);
            if(hours>0){
                format="%1$,d时%2$,d分%3$,d秒";
                array=new Object[]{hours,minutes,seconds};
            }else if(minutes>0){
                format="%1$,d分%2$,d秒";
                array=new Object[]{minutes,seconds};
            }else{
                format="%1$,d秒";
                array=new Object[]{seconds};
            }
            html= String.format(format, array);
            daoji=html;
    }
        return html;
    }
}
