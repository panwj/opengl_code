package com.test.opengl.triangle;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by panwenjuan on 18-1-5.
 */

public class HelloGLSurfaceView extends GLSurfaceView {

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private HelloGLRenderer mRenderer;
    private float mPreviousX;
    private float mPreviousY;

    public HelloGLSurfaceView(Context context) {
        super(context);
        // OpenGL 设置使用的版本
        setEGLContextClientVersion(2);
        // Set the Renderer for drawing on the GLSurfaceView'
        mRenderer = new HelloGLRenderer(context);
        setRenderer(mRenderer);

        // Render the view only when there is a change
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public HelloGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // OpenGL 设置使用的版本
        setEGLContextClientVersion(2);
        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new HelloGLRenderer(context);
        setRenderer(mRenderer);

        /**
         * 设置渲染模式，仅在绘制数据发生变化时才在视图中进行绘制操作：如果选用这一配置选项
         ，那么除非调用了requestRender()，否则GLSurfaceView不会被重新绘制，这样做可以让应用的性能及效率得到提高。
         */
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                // reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                    dx = dx * -1 ;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                    dy = dy * -1 ;
                }

                mRenderer.mAngle += (dx + dy) * TOUCH_SCALE_FACTOR;
                //因为设置了绘制模式，所以不能省略
                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
}
