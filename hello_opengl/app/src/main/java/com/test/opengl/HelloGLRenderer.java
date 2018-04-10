package com.test.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
/**
 * onSurfaceCreated() is called once to set up the GLSurfaceView environment.
 *
 * onDrawFrame() is called for each redraw of the GLSurfaceView.
 *
 * onSurfaceChanged() is called if the geometry of the GLSurfaceView changes,
 * for example when the device's screen orientation changes.
 *
 * 注意：形状和其他静态物体应该被初始化一次为最佳性能在你的onsurfacecreated()方法。
 * 避免在ondrawframe()重新对对象的初始化，这会导致系统重新创建对象，每帧重绘和减慢你的应用。
 *
 *
 * Created by panwenjuan on 18-1-5.
 */


public class HelloGLRenderer implements GLSurfaceView.Renderer {

    private Context mContext;
    /**
     * In OpenGL ES 2.0, you attach vertex and fragment shaders to
     * a Program and then apply the program to the OpenGL graphics pipeline.
     */
    private int mProgram;
    private int maPositionHandle;
    private long uColor; // #AARRGGBB format
    private int muMVPMatrixHandle;
    private float[] mMVPMatrix = new float[16];
    private float[] mMMatrix = new float[16];
    private float[] mVMatrix = new float[16];
    private float[] mProjMatrix = new float[16];
    private DrawTriangle mTriangleView;
    public float mAngle;

    public HelloGLRenderer(Context context) {
        mContext = context;
        mTriangleView = new DrawTriangle();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        Log.d("sss", "onSurfaceCreated() ...");
        //GLES20 version for android
        // Set the background frame color
//        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);

        uColor = mContext.getResources().getColor(R.color.white);
        float fAlpha = (float)(uColor >> 24) / 0xFF;
        float fRed = (float)((uColor >> 16) & 0xFF) / 0xFF;
        float fGreen = (float)((uColor >> 8) & 0xFF) / 0xFF;
        float fBlue = (float)(uColor & 0xFF) / 0xFF;

//        Log.d("sss", "fAlpha = " + fAlpha + "   fRed = " + fRed + "  fGreen = " + fGreen + "  fBlue = " + fBlue);

        GLES20.glClearColor(fRed, fGreen, fBlue, fAlpha);

        // initialize the triangle vertex array
        mTriangleView.initTriangleShapes();

        // to load the shaders and attach them to an OpenGL Program.
        int vertexShader = mTriangleView.loadShader(GLES20.GL_VERTEX_SHADER, mTriangleView.VERTEX_SHADER_CODE);
        int fragmentShader = mTriangleView.loadShader(GLES20.GL_FRAGMENT_SHADER, mTriangleView.FRAGMENT_SHADER_CODE);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // creates OpenGL program executables
        // you are ready to draw the triangle object in the OpenGL view.

        // get handle to the vertex shader's vPosition member
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        Log.d("sss", "onSurfaceChanged() ...  width = " + width + "   height = " + height);

        GLES20.glViewport(0, 0, width, height);

        /*//方法一 ： 归一化设备坐标
        final float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;

        if (width > height) {
            // Landscape
            Matrix.orthoM(mProjMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            // Portrait or square
            Matrix.orthoM(mProjMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }*/


        //方法二 ： 合并视口矩阵，投影矩阵
        float ratio = (float) width / height;
        // 创建投影矩阵
        // this projection matrix is applied to object coodinates
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

        // uMVPMatrix 需要和定义的顶点数组中使用的矩阵变量一致
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // 创建视口矩阵
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, 5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
//        Log.d("sss", "onDrawFrame() ...");
        //一直在回调绘制

        // Apply a ModelView Projection transformation
        // 合并投影矩阵和视口矩阵 ---> mMVPMatrix 用作投影变换
        //采用合并投影矩阵和视口的方式实现上没有生效，有待测试
        // Create a rotation for the triangle
//        long time = SystemClock.uptimeMillis() % 4000L;
//        float angle = 0.090f * ((int) time);
        //图像旋转
        Matrix.setRotateM(mMMatrix, 0, mAngle, 0, 0, 1.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mMMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
//        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);//合并视投

        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // Prepare the triangle data
        GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 12, mTriangleView.getTriangleFloatBuffer());
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);//合并视投
//        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mProjMatrix, 0);//归一化

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

//        //done : close
        GLES20.glDisableVertexAttribArray(maPositionHandle);
//        GLES20.glUseProgram(0);
    }

    private void initMatrix() {
        mMVPMatrix = new float[16];
        Matrix.setIdentityM(mMVPMatrix, 0);
    }
}
