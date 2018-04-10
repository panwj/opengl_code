package com.test.opengl.comtriangle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.test.opengl.R;

/**
 * Created by panwenjuan on 18-4-10.
 */

public class CommonTriangleActivity extends AppCompatActivity {

    private CommonGLSurfaceView mCommonGLSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_triangle_layout);
        mCommonGLSurfaceView = (CommonGLSurfaceView) findViewById(R.id.common_triangle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCommonGLSurfaceView != null) {
            mCommonGLSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCommonGLSurfaceView != null) {
            mCommonGLSurfaceView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
