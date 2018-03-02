package com.example.samri.notebookapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class SwipeScreen extends AppCompatActivity {
    private GestureDetectorCompat gestureObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_screen);
        gestureObject=new GestureDetectorCompat(this,new learnGesture());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    class learnGesture extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e2.getX()>e1.getX()){


            }
            else
                if (e2.getX()<e1.getX()){
                    Intent swipeOne=new Intent(SwipeScreen.this,MainActivity.class);
                    startActivity(swipeOne);

            }

            return true;
        }
    }
}
