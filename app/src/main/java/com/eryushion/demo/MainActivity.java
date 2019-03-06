package com.eryushion.demo;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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
        final View androidRobotView = findViewById(R.id.rl_test);

        findViewById(R.id.btn_go).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this,DragDropLR.class));
                Intent intent = new Intent(MainActivity.this, DragDropLR.class);
                // create the transition animation - the images in the layouts
                // of both activities are defined with android:transitionName="robot"
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(MainActivity.this, androidRobotView, "robot");
                // start the new activity
                startActivity(intent, options.toBundle());

            }
        });

        iv_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iv_test.getContentDescription().equals("OFF")) {
                    // iv_test.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.drawer));
                    iv_test.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.avd_anim_a));
                    iv_test.setContentDescription("ON");
                } else {
                    //iv_test.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.close));
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

        testPermissions();


    }

    private void testPermissions() {
        String permissions[] = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestLocationPermission(permissions, 201);
        } else {

        }
    }

    private void requestLocationPermission(String[] permissions, int i) {
        ActivityCompat.requestPermissions(MainActivity.this, permissions, i);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean openActivityOnce = true;
        boolean openDialogOnce = true;
        boolean isGranted = false;

        if (requestCode == 201) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                isGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED;

                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    boolean rational = false;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        rational = shouldShowRequestPermissionRationale(permissions[i]);
                        if (rational) {
                            Toast.makeText(this, "rational", Toast.LENGTH_SHORT).show();
                        } else {
                            if (openDialogOnce) {
                                showAlert();
                                openDialogOnce = false;
                            }
                        }
                    }
                }

            }
            if (isGranted) {
                Toast.makeText(this, "grant", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showAlert() {
        new AlertDialog.Builder(MainActivity.this)
                .setIcon(getResources().getDrawable(R.mipmap.ic_launcher))
                .setTitle("Baaamm..,")
                .setMessage("Hey , You have Denied Permission Please Allow From Setting... ")
                .setPositiveButton("OK", null)
                .show();

    }
}
