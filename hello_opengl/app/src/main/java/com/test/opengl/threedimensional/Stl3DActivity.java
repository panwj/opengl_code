package com.test.opengl.threedimensional;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.test.opengl.R;

/**
 * Created by panwenjuan on 18-4-13.
 */

public class Stl3DActivity extends AppCompatActivity {

    private StlGLSurfaceView mStlGLSurfaceView;
    private float rotateDegreen = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            rotate(rotateDegreen);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stl_3d_layout);
        mStlGLSurfaceView = (StlGLSurfaceView) findViewById(R.id.stl_3d);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mStlGLSurfaceView != null) {
            mStlGLSurfaceView.onResume();

            //不断改变rotateDegreen值，实现旋转
            new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            sleep(100);

                            rotateDegreen += 5;
                            handler.sendEmptyMessage(0x001);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mStlGLSurfaceView != null) {
            mStlGLSurfaceView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void rotate(float degree) {
        if (mStlGLSurfaceView != null) {
            StlGLRenderer renderer = mStlGLSurfaceView.getStlGLRenderer();
            renderer.rotate(degree);
            mStlGLSurfaceView.requestRender();
        }
    }
}
