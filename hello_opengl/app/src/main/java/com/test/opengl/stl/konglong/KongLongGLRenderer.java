package com.test.opengl.stl.konglong;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import com.test.opengl.stl.Model;
import com.test.opengl.stl.Point;
import com.test.opengl.stl.STLReader;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by panwenjuan on 18-4-21.
 */

public class KongLongGLRenderer implements GLSurfaceView.Renderer{

    private Context mContext;
    private List models = new ArrayList();
    private float mScalef = 1;
    private float mDegree = 0;
    private Point mCenterPoint;

    public KongLongGLRenderer(Context context) {
        mContext = context;
        STLReader stlReader = new STLReader();
        try {
            for (int i = 1 ; i < 17; i++) {
                Model model = stlReader.parserBinStlInAssets(mContext, "konglong_" + i + ".stl");
                models.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        gl10.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        gl10.glEnable(GL10.GL_DEPTH_TEST);
        //设置深度缓存值
        gl10.glClearDepthf(1.0f);
        //设置深度缓存比较函数
        gl10.glDepthFunc(GL10.GL_LEQUAL);
        gl10.glShadeModel(GL10.GL_SMOOTH);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        gl10.glViewport(0, 0, width, height);

        gl10.glMatrixMode(GL10.GL_PROJECTION);
        gl10.glLoadIdentity();
        GLU.gluPerspective(gl10, 45f, ((float) width) / height, 1f, 100f);

        gl10.glMatrixMode(GL10.GL_MODELVIEW);
        gl10.glLoadIdentity();

        GLU.gluLookAt(gl10, 0, 0, 5, 0, 0, 0, 0, 1, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl10.glLoadIdentity();

        gl10.glRotatef(mDegree, 1, 1, 0);

        gl10.glScalef(mScalef, mScalef, mScalef);
        gl10.glTranslatef(-mCenterPoint.x, -mCenterPoint.y, -mCenterPoint.z);
    }
}
