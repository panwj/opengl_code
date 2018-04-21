package com.test.opengl.com.test.opengl.stl.threedimensional;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

import com.test.opengl.Constant;
import com.test.opengl.com.test.opengl.stl.Model;
import com.test.opengl.com.test.opengl.stl.Point;
import com.test.opengl.com.test.opengl.stl.STLReader;
import com.test.opengl.com.test.opengl.stl.Util;

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

    float[] ambient = {0.9f, 0.9f, 0.9f, 1.0f,};
    float[] diffuse = {0.5f, 0.5f, 0.5f, 1.0f,};
    float[] specular = {1.0f, 1.0f, 1.0f, 1.0f,};
    float[] lightPosition = {0.5f, 0.5f, 0.5f, 0.0f,};

    float[] materialAmb = {0.4f, 0.4f, 1.0f, 1.0f};
//    float[] materialAmb = {1.0f, 0.0f, 0.0f, 1.0f};
//    float[] materialDiff = {1.0f, 0.0f, 0.0f, 1.0f};//漫反射设置红色
    float[] materialDiff = {0.0f, 0.0f, 1.0f, 1.0f};//漫反射设置蓝色
    float[] materialSpec = {1.0f, 0.5f, 0.0f, 1.0f};

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

        //设置光源
        openLight(gl10);
        //设置材料
        enableMaterial(gl10);

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
        gl10.glTranslatef(-mCenterPoint.x, -mCenterPoint.y, -mCenterPoint.z);

        gl10.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl10.glNormalPointer(GL10.GL_FLOAT, 0, model.getVnormBuffer());
        gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, model.getVertBuffer());

        gl10.glDrawArrays(GL10.GL_TRIANGLES, 0, model.getFacetCount() * 3);

        gl10.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);

        gl10.glFinish();
    }

    /**
     * 设置模型的光源
     *
     * 光照效果
     * 自然界中看到物体表面的光分为：镜面反射（Specular 一般为白色）、漫反射（Diffuse 一般为物体表面的颜色）、环境光照射（Ambient 在黑暗环境中为黑色）
     * 系统提供了0-7共8种光源，0号光源为RGBA为（1.0,1.0,1.0,1.0）即白色其他光源均为黑色
     *
     * glLightfv(int light,int pname, FloatBuffer params):
     * light: 指光源的序号，OpenGL ES可以设置从0到7共八个光源。
     * pname: 光源参数名称:GL_SPOT_EXPONENT,GL_SPOT_CUTOFF,GL_CONSTANT_ATTENUATION,GL_LINEAR_ATTENUATION,GL_QUADRATIC_ATTENUATION,
     * GL_AMBIENT(用于设置环境光颜色),GL_DIFFUSE(用于设置漫反射光颜色),GL_SPECULAR(用于设置镜面反射光颜色),GL_POSITION（用于设置光源位置),GL_SPOT_DIRECTION,
     * params: 参数的值（数组或是Buffer类型），数组里面含有4个值分别表示R,G,B,A。
     */
    private void openLight(GL10 gl10) {
        gl10.glEnable(GL10.GL_LIGHTING);
        gl10.glEnable(GL10.GL_LIGHT0);
        gl10.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, Util.floatToBuffer(specular));
        gl10.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, Util.floatToBuffer(diffuse));
        gl10.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, Util.floatToBuffer(ambient));
        gl10.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, Util.floatToBuffer(lightPosition));
    }

    /**
     * 设置模型的材料属性
     *
     * 同一束光照射在不同颜色材料的物体上，我们所看到的是不同的，反射出来的颜色、光泽是不同的，因此不同材质对渲染影响很大
     *
     * glMaterialfv(int face,int pname,FloatBuffer params)：
     * face : 在OpenGL ES中只能使用GL_FRONT_AND_BACK，表示修改物体的前面和后面的材质光线属性。
     * pname: 参数类型，这些参数用在光照方程。可以取如下值：GL_AMBIENT、GL_DIFFUSE、GL_SPECULAR、GL_EMISSION、GL_SHININESS
     * param：指定反射的颜色。
     */
    private void enableMaterial(GL10 gl10) {
        gl10.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, Util.floatToBuffer(materialSpec));
        gl10.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, Util.floatToBuffer(materialDiff));
        gl10.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, Util.floatToBuffer(materialAmb));
    }
}
