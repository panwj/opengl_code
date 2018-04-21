package com.test.opengl.stl.threedimensional;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by panwenjuan on 18-4-13.
 */

public class StlGLSurfaceView extends GLSurfaceView {

    private StlGLRenderer mStlGLRenderer;
    public StlGLSurfaceView(Context context) {
        super(context);
        mStlGLRenderer = new StlGLRenderer(context);
        setRenderer(mStlGLRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public StlGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        setEGLContextClientVersion(1);
        mStlGLRenderer = new StlGLRenderer(context);
        setRenderer(mStlGLRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public StlGLRenderer getStlGLRenderer() {
        return mStlGLRenderer;
    }
}
