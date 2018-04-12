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
        setEGLContextClientVersion(1);
        mCommonGLRenderer = new CommonGLRenderer(context);
        //如果你的模拟器没有显示效果：请检查“Use Host GPU”选项已经勾选。如果已经勾选，在setRenderer（）前加上该语句
        //setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setRenderer(mCommonGLRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public CommonGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(1);
        mCommonGLRenderer = new CommonGLRenderer(context);
        setRenderer(mCommonGLRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
