package com.example.sky.environment;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

public class PropertyAnimatedActivity extends AppCompatActivity {
    ProgressDialog progressBar;
    boolean end;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_animated);
        settingProcessBar();
        //wheel animation
        ImageView wheel = (ImageView)findViewById(R.id.wheel);
        //load the wheel animation
        AnimatorSet wheelSet = (AnimatorSet)
                AnimatorInflater.loadAnimator(this, R.animator.wheel_spin);
        //set the view as target
        wheelSet.setTarget(wheel);
        //start the animation
        wheelSet.start();

        //get the sun view
        ImageView sun = (ImageView)findViewById(R.id.sun);
        //load the sun movement animation
        AnimatorSet sunSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.sun_swing);
        //set the view as target
        sunSet.setTarget(sun);
        //start the animation
        sunSet.start();

        //darken sky
        ValueAnimator skyAnim = ObjectAnimator.ofInt
                (findViewById(R.id.car_layout), "backgroundColor",
                        Color.rgb(0x66, 0xcc, 0xff), Color.rgb(0x00, 0x66, 0x99));
        skyAnim.setDuration(2000);
        skyAnim.setRepeatCount(ValueAnimator.INFINITE);
        skyAnim.setRepeatMode(ValueAnimator.REVERSE);
        skyAnim.setEvaluator(new ArgbEvaluator());
        skyAnim.start();

        //move clouds
        ObjectAnimator cloudAnim =
                ObjectAnimator.ofFloat(findViewById(R.id.cloud1), "x", -350);
        cloudAnim.setDuration(2000);
        cloudAnim.setRepeatCount(ValueAnimator.INFINITE);
        cloudAnim.setRepeatMode(ValueAnimator.REVERSE);
        cloudAnim.start();
        //other cloud
        ObjectAnimator cloudAnim2 = ObjectAnimator.ofFloat(findViewById(R.id.cloud2), "x", -300);
        cloudAnim2.setDuration(2000);
        cloudAnim2.setRepeatCount(ValueAnimator.INFINITE);
        cloudAnim2.setRepeatMode(ValueAnimator.REVERSE);
        cloudAnim2.start();
    }

    private void settingProcessBar() {
//        progressBar= new ProgressDialog(this);
//        progressBar.setMessage("Waiting for second :) ");
//        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progressBar.setIndeterminate(false);
//        progressBar.show();
        end = false;
        final int totalProgressTime = 100;
        final Thread t = new Thread() {
            @Override
            public void run() {
                int jumpTime = 0;
                while(jumpTime < totalProgressTime) {
                    try {
                        sleep(200);
                        jumpTime += 2;
                        Log.e("time",jumpTime+" ");

                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                intent = new Intent(PropertyAnimatedActivity.this,Start.class);
                startActivity(intent);
            }
        };
        t.start();
    }
}



