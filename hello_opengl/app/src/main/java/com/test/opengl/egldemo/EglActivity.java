package com.test.opengl.egldemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.test.opengl.R;

/**
 * Created by panwenjuan on 18-5-26.
 */

public class EglActivity extends AppCompatActivity {

    GLRenderer renderer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.egl_activity_layout);

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface);
        renderer = new GLRenderer();
        renderer.start();
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
                renderer.render(surfaceHolder.getSurface(), width, height);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        renderer.release();
        renderer = null;
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
