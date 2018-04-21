package com.test.opengl.stl.konglong;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by panwenjuan on 18-4-21.
 */

public class KongLongGLSurfaceView extends GLSurfaceView {

    private KongLongGLRenderer renderer;

    public KongLongGLSurfaceView(Context context) {
        super(context);
        renderer = new KongLongGLRenderer(context);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public KongLongGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        renderer = new KongLongGLRenderer(context);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
