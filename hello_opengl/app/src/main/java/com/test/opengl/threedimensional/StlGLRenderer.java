package com.test.opengl.threedimensional;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

import com.test.opengl.Constant;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by panwenjuan on 18-4-13.
 */

public class StlGLRenderer implements GLSurfaceView.Renderer {

    private Context mContext;
    private Model model;
    private Point mCenterPoint;
    private Point eye = new Point(0, 0, -3);
    private Point up = new Point(0, 1, 0);
    private Point center = new Point(0, 0, 0);
    private float mScalef = 1;
    private float mDegree = 0;

    public StlGLRenderer(Context context) {
        try {
            mContext = context;
            model = new STLReader().parserBinStlInAssets(context, "ponicorn1.stl");
            Log.d(Constant.TAG, "StlGLRenderer() model = " + model);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Constant.TAG, "StlGLRenderer() exception = " + e.toString());
        }
    }

    public void rotate(float degree) {
        mDegree = degree;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        gl10.glEnable(GL10.GL_DEPTH_TEST);
        //设置深度缓存值
        gl10.glClearDepthf(1.0f);
        //设置深度缓存比较函数
        gl10.glDepthFunc(GL10.GL_LEQUAL);
        gl10.glShadeModel(GL10.GL_SMOOTH);

        float r = model.getR();
        //r是半径，不是直径，因此用0.5/r可以算出缩放比例
        mScalef = 0.5f / r;
//        mScalef = (0.5f / r) * 2;
        mCenterPoint = model.getCenterPoint();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        gl10.glViewport(0, 0, width, height);

        gl10.glMatrixMode(GL10.GL_PROJECTION);
        gl10.glLoadIdentity();
        GLU.gluPerspective(gl10, 45f, ((float) width) / height, 1f, 100f);

        gl10.glMatrixMode(GL10.GL_MODELVIEW);
        gl10.glLoadIdentity();
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl10.glLoadIdentity();

        GLU.gluLookAt(gl10, eye.x, eye.y, eye.z, center.x, center.y,
                center.z, up.x, up.y, up.z);

        gl10.glRotatef(mDegree, 1, 1, 0);

        gl10.glScalef(mScalef, mScalef, mScalef);
        gl10.glTranslatef(mCenterPoint.x, mCenterPoint.y, mCenterPoint.z);

        gl10.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl10.glNormalPointer(GL10.GL_FLOAT, 0, model.getVnormBuffer());
        gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, model.getVertBuffer());

        gl10.glDrawArrays(GL10.GL_TRIANGLES, 0, model.getFacetCount() * 3);

        gl10.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);

        gl10.glFinish();
    }
}
