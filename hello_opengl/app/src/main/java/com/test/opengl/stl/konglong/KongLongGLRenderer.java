package com.test.opengl.stl.konglong;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

import com.test.opengl.Constant;
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

    private Point eye = new Point(0, 0, -3);
    private Point up = new Point(0, 1, 0);
    private Point center = new Point(0, 0, 0);

    private Context mContext;
    private List<Model> models = new ArrayList();
    private float mScalef = 1;
    private float mDegree = 0;
    private Point mCenterPoint;
    private float mMaxX, mMaxY, mMaxZ;
    private float mMinX, mMinY, mMinZ;

    public KongLongGLRenderer(Context context) {
        mContext = context;
        STLReader stlReader = new STLReader();
        try {
            for (int i = 1 ; i < 17; i++) {
                Log.d(Constant.TAG, "i = " + i);
                Model model = stlReader.parserBinStlInAssets(mContext, "konglong_" + i + ".stl");
                models.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Constant.TAG, "exception = " + e.toString());
        }

        getMaxX(models);
        getMaxY(models);
        getMaxZ(models);
        getMinX(models);
        getMinY(models);
        getMinZ(models);

        Log.d(Constant.TAG, "mMaxX = " + mMaxX + "   mMaxY = " + mMaxY + "   mMaxZ = " + mMaxZ);
        Log.d(Constant.TAG, "mMinX = " + mMinX + "   mMinY = " + mMinY + "   mMinZ = " + mMinZ);
        mCenterPoint = getCenterPoint();
        Log.d(Constant.TAG, "mCenterPoint : " + mCenterPoint.x + "   " + mCenterPoint.y + "   " + mCenterPoint.z);
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

        float r = getR();
        //r是半径，不是直径，因此用0.5/r可以算出缩放比例
        mScalef = 0.5f / r;
//        mScalef = 1/1000;
        Log.d(Constant.TAG, "mScalef = " + mScalef);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        gl10.glViewport(0, 0, width, height);

        gl10.glMatrixMode(GL10.GL_PROJECTION);
        gl10.glLoadIdentity();
        GLU.gluPerspective(gl10, 45f, ((float) width) / height, 1f, 100f);

        gl10.glMatrixMode(GL10.GL_MODELVIEW);
        gl10.glLoadIdentity();

//        GLU.gluLookAt(gl10, 0, 0, 5, 0, 0, 0, 0, 1, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        Log.d(Constant.TAG, "onDrawFrame() start...");

        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl10.glLoadIdentity();

        GLU.gluLookAt(gl10, eye.x, eye.y, eye.z, center.x, center.y,
                center.z, up.x, up.y, up.z);

        gl10.glRotatef(mDegree, 1, 1, 0);

        gl10.glScalef(mScalef, mScalef, mScalef);
//        gl10.glTranslatef(-mCenterPoint.x, -mCenterPoint.y, -mCenterPoint.z);
//        gl10.glTranslatef(-mCenterPoint.x, -mCenterPoint.y, -mCenterPoint.z);

        for (Model model : models) {

            Log.d(Constant.TAG, "" + models.indexOf(model));

            if (model.getVertBuffer() == null || model.getVnormBuffer() == null) {
                Log.d(Constant.TAG, "model = " + model.toString());
                continue;
            }

            gl10.glEnableClientState(GL10.GL_NORMAL_ARRAY);
            gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);

            gl10.glNormalPointer(GL10.GL_FLOAT, 0, model.getVnormBuffer());
            gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, model.getVertBuffer());

            gl10.glDrawArrays(GL10.GL_TRIANGLES, 0, model.getFacetCount() * 3);

            gl10.glDisableClientState(GL10.GL_NORMAL_ARRAY);
            gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        }
        gl10.glFinish();

        Log.d(Constant.TAG, "onDrawFrame() end...");
    }

    private void getMaxX(List<Model> models) {
        if (models == null) {
            return;
        }
        for (int i = 0; i < models.size(); i++) {
            Model model = models.get(i);
            if (model.maxX > mMaxX) {
                mMaxX = model.maxX;
            }
        }
    }

    private void getMaxY(List<Model> models) {
        if (models == null) {
            return;
        }
        for (int i = 0; i < models.size(); i++) {
            Model model = models.get(i);
            if (model.maxY > mMaxY) {
                mMaxY = model.maxY;
            }
        }
    }

    private void getMaxZ(List<Model> models) {
        if (models == null) {
            return;
        }
        for (int i = 0; i < models.size(); i++) {
            Model model = models.get(i);
            if (model.maxZ > mMaxZ) {
                mMaxZ = model.maxZ;
            }
        }
    }

    private void getMinX(List<Model> models) {
        if (models == null) {
            return;
        }
        for (int i = 0; i < models.size(); i++) {
            Model model = models.get(i);
            if (model.minX < mMinX) {
                mMinX = model.minX;
            }
        }
    }

    private void getMinY(List<Model> models) {
        if (models == null) {
            return;
        }
        for (int i = 0; i < models.size(); i++) {
            Model model = models.get(i);
            if (model.minY < mMinY) {
                mMinY = model.minY;
            }
        }
    }

    private void getMinZ(List<Model> models) {
        if (models == null) {
            return;
        }
        for (int i = 0; i < models.size(); i++) {
            Model model = models.get(i);
            if (model.minZ < mMinZ) {
                mMinZ = model.minZ;
            }
        }
    }

    public Point getCenterPoint() {
        float cx = mMinX + (mMaxX - mMinX) / 2;
        float cy = mMinY + (mMaxY - mMinY) / 2;
        float cz = mMinZ + (mMaxZ - mMinZ) / 2;
        return new Point(cx, cy, cz);
    }

    public float getR() {
        float dx = (mMaxX - mMinX);
        float dy = (mMaxY - mMinY);
        float dz = (mMaxZ - mMinZ);
        float max = dx;
        if (dy > max) max = dy;
        if (dz > max) max = dz;
        return max;
    }
}
