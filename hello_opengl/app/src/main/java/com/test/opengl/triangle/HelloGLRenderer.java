package com.test.opengl.triangle;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.test.opengl.Constant;
import com.test.opengl.R;

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
 * 矩阵变换：
 * 视图变换：从不同位置去观察
 * 模型变换：移动，旋转或缩放物体
 * 视口变换：只拍摄物体一部分，使物体在照片中只显示一部分
 * 投影变换：给物体拍照印成照片。可以做到“近大远小”、裁剪只看部分等等透视效果
 *
 * 在变换之前我们需要声明当前使用的哪种变换
 * 模型视图变换: gl.glMatrixMode(GL10.GL_MODELVIEW);
 *
 * 投影变换：gl.glMatrixMode(GL10.GL_PROJECTION); gl.glLoadIdentity();
 *
 * 视口变换： gl.glViewport(0, 0, width, height);
 *
 * 但是有一点值得注意的是，在此之前，你可能针对模型做了其他的操作，而我们知道，每次操作相当于一次矩阵相乘。
 * OpenGL中，使用“当前矩阵”表示要执行的变化，为了防止前面执行过变换“保留”在“当前矩阵”，我们需要把“当前矩阵”复位，
 * 即变为单位矩阵（对角线上的元素全为1），通过执行如下函数：gl.glLoadIdentity();
 *
 * 关于绘制参考链接：
 * https://blog.csdn.net/huachao1001/article/details/52044602
 * http://hukai.me/android-training-course-in-chinese/graphics/opengl/shapes.html
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
        Log.d(Constant.TAG, "onSurfaceCreated() ...");
        //GLES20 version for android
        //GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);

        uColor = mContext.getResources().getColor(R.color.white);
        float fAlpha = (float)(uColor >> 24) / 0xFF;
        float fRed = (float)((uColor >> 16) & 0xFF) / 0xFF;
        float fGreen = (float)((uColor >> 8) & 0xFF) / 0xFF;
        float fBlue = (float)(uColor & 0xFF) / 0xFF;

        //Log.d(Constant.TAG, "fAlpha = " + fAlpha + "   fRed = " + fRed + "  fGreen = " + fGreen + "  fBlue = " + fBlue);

        //设置清屏颜色，可理解为画布的颜色
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
        Log.d(Constant.TAG, "onSurfaceChanged() ...  width = " + width + "   height = " + height);

        //设置视口
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
        // 可将当前可视空间设置为透视投影空间：
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

        // uMVPMatrix 需要和定义的顶点数组中使用的矩阵变量一致
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // 创建视口矩阵
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, 5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
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

        // 使用glClearColor函数所设置的颜色进行清屏。
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