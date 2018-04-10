package com.test.opengl.comtriangle;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by panwenjuan on 18-4-10.
 */

public class CommonGLSurfaceView extends GLSurfaceView {

    private CommonGLRenderer mCommonGLRenderer;

    public CommonGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        mCommonGLRenderer = new CommonGLRenderer(context);
        setRenderer(mCommonGLRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public CommonGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        mCommonGLRenderer = new CommonGLRenderer(context);
        setRenderer(mCommonGLRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
