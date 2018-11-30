package com.eryushion.demo;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageView iv_test = findViewById(R.id.iv_test);
        iv_test.setImageDrawable(getResources().getDrawable(R.drawable.avd_anim_a));
        iv_test.setContentDescription("OFF");
        iv_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iv_test.getContentDescription().equals("OFF")) {
                    iv_test.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.avd_anim_a));
                    iv_test.setContentDescription("ON");
                } else {
                    iv_test.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.avd_anim_b));
                    iv_test.setContentDescription("OFF");
                }
                Drawable drawable = ((ImageView) v).getDrawable();
                if (drawable instanceof AnimatedVectorDrawableCompat) {
                    AnimatedVectorDrawableCompat drawableCompat = (AnimatedVectorDrawableCompat) drawable;
                    drawableCompat.start();
                } else if (drawable instanceof AnimatedVectorDrawable) {
                    AnimatedVectorDrawable animationDrawable = (AnimatedVectorDrawable) iv_test.getDrawable();
                    animationDrawable.start();
                }
            }
        });


    }
}
