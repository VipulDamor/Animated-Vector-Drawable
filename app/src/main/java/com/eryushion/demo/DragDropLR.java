package com.eryushion.demo;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Path;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DragDropLR extends AppCompatActivity implements View.OnTouchListener, View.OnDragListener {

    boolean isDragging = false;
    float deltaX;
    float dX, dY;
    int height, width;
    int lastAction;
    LinearLayout leftContainer;

    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                //Do your stuff on GPS status change
                Log.d("aaaGps", intent.getData().toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_drop_lr);

        ImageView box1 = findViewById(R.id.box1);
        ImageView box2 = findViewById(R.id.box2);
        ImageView box3 = findViewById(R.id.box3);

        leftContainer = findViewById(R.id.leftContainer);

        box1.setOnTouchListener(this);
        //box2.setOnTouchListener(this);
        box3.setOnTouchListener(this);

        box1.setOnDragListener(this);
        box2.setOnDragListener(this);
        box3.setOnDragListener(this);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;


        //todo move view animation
        /*ObjectAnimator animation = ObjectAnimator.ofFloat(box3, "translationX", 500f);
        animation.setDuration(2000);
        animation.start();*/

        //todo arc animation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Path path = new Path();
            path.arcTo(0f, 0f, 1000f, 1000f, 270f, -180f, true);
            ObjectAnimator animator = ObjectAnimator.ofFloat(box2, View.X, View.Y, path);
            animator.setDuration(2000);
            animator.start();
        }


        //todo fling animation
        /*FlingAnimation fling = new FlingAnimation(box2, DynamicAnimation.SCROLL_X);
        fling.setStartVelocity(-velocityX)
                .setMinValue(0)
                .setMaxValue(maxScroll)
                .setFriction(1.1f)
                .start();
        anim.setMinimumVisibleChange(DynamicAnimation.MIN_VISIBLE_CHANGE_SCALE);*/

        String permissions[] = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        registerReceiver(gpsReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));


    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        float newX, newY;
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN && !isDragging) {
            isDragging = true;
            dX = view.getX() - event.getRawX();
            dY = view.getY() - event.getRawY();
            deltaX = event.getX();
            lastAction = MotionEvent.ACTION_DOWN;
            return true;
        } else if (isDragging) {
            if (action == MotionEvent.ACTION_MOVE) {
                //view.startDrag(null, new View.DragShadowBuilder(view), null, 0);
                float widthPer = (view.getX() * 100) / width;
                if (widthPer > 75.00) {
                    Toast.makeText(DragDropLR.this, "yp done..!!", Toast.LENGTH_SHORT).show();

                }
                Log.d("aaaawidth", widthPer + "%");
                newX = event.getRawX() + dX;
                newY = event.getRawY() + dY;
                // check if the view out of screen
                if ((newX <= 0 || newX >= width - view.getWidth()) || (newY <= 0 || newY >= height - view.getHeight())) {
                    lastAction = MotionEvent.ACTION_MOVE;
                    return false;
                }
                view.setX(view.getX() + event.getX() - deltaX);
                view.setY(view.getY());
                lastAction = MotionEvent.ACTION_MOVE;

                //todo
               /* View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.setBackgroundResource(android.R.color.holo_red_dark);
                view.startDrag(null, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);*/
                return true;

            } else if (action == MotionEvent.ACTION_UP) {
                isDragging = false;
                if (lastAction == MotionEvent.ACTION_DOWN)
                    Toast.makeText(DragDropLR.this, "Clicked!", Toast.LENGTH_SHORT).show();
                return true;
            } else if (action == MotionEvent.ACTION_CANCEL) {
                Log.d("aaaaadragcancel", "yeee");
                isDragging = false;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {

        Log.d("aaaaaaa", event.getAction() + "");
        View dragView = (View) event.getLocalState();
        switch (event.getAction()) {
            // signal for the start of a drag and drop operation
            case DragEvent.ACTION_DRAG_STARTED:
                // do nothing
                break;

            // the drag point has entered the bounding box of the View
            case DragEvent.ACTION_DRAG_ENTERED:
                v.setBackgroundColor(0xFFFFF6F9);
                break;

            // the user has moved the drag shadow outside the bounding box of the View
            case DragEvent.ACTION_DRAG_EXITED:
                v.setBackgroundColor(v.getId() == R.id.leftContainer ? 0xFFE8E6E7 : 0xFFB1BEC4);
                break;

            // the drag and drop operation has concluded
            case DragEvent.ACTION_DRAG_ENDED:
                v.setBackgroundColor(v.getId() == R.id.leftContainer ? 0xFFE8E6E7 : 0xFFB1BEC4);
                break;

            //drag shadow has been released,the drag point is within the bounding box of the View
            case DragEvent.ACTION_DROP:
                View view = (View) event.getLocalState();
                // we want to make sure it is dropped only to left and right parent view
                if (v.getId() == R.id.leftContainer || v.getId() == R.id.rightContainer) {

                    ViewGroup source = (ViewGroup) view.getParent();
                    source.removeView(view);

                    LinearLayout target = (LinearLayout) v;
                    target.addView(view);

                    String id = event.getClipData().getItemAt(0).getText().toString();
                    Toast.makeText(this, id + " dropped!", Toast.LENGTH_SHORT).show();
                }
                // make view visible as we set visibility to invisible while starting drag
                view.setVisibility(View.VISIBLE);
                break;
        }
        return true;
    }

}
