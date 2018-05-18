package com.test.opengl.stl.konglong;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.test.opengl.R;

/**
 * Created by panwenjuan on 18-4-21.
 */

public class KongLongSTLActivity extends AppCompatActivity {

    private KongLongGLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.konglong_layout);
        mGLSurfaceView = findViewById(R.id.konglong);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mGLSurfaceView != null) {
            mGLSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGLSurfaceView != null) {
            mGLSurfaceView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
