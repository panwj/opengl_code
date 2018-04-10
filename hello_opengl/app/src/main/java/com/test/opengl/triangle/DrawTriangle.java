package com.test.opengl.triangle;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 *
 * OpenGL允许使用三维空间中的坐标定义对象。所以，在画三角形之前，必须定义它的坐标。
 * 在opengl中典型方法是为坐标定义顶点数组。
 *
 * By default, OpenGL ES assumes a coordinate system where [0,0,0] (X,Y,Z)
 * specifies the center of the GLSurfaceView frame, [1,1,0] is the top-right corner
 * of the frame and [-1,-1,0] is bottom-left corner of the frame.
 *
 * Created by panwenjuan on 18-1-5.
 */

public class DrawTriangle {

    //The vertex shader controls how OpenGL positions and draws the vertices of shapes in space.
    public static final String VERTEX_SHADER_CODE =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
                    "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    //The fragment shader controls what OpenGL draws between the vertices of shapes.
    public static final String FRAGMENT_SHADER_CODE =
                    "precision mediump float;  \n"  +
                    "void main(){              \n" +
                    " gl_FragColor = vec4 (0.63671875, 0.76953125, 0.22265625, 1.0); \n" +
                    "}                         \n";

    private FloatBuffer mTriangleVB;

    public DrawTriangle() {
    }

    public void initTriangleShapes() {

        float triangleCoords[] = {
                // X, Y, Z
                -0.5f, -0.25f, 0,
                0.5f, -0.25f, 0,
                0.0f,  1.0f, 0
        };

        // 先初始化buffer，数组的长度*4，因为一个float占4个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(
                triangleCoords.length * 4);

        /**
         * 以本机字节顺序来修改此缓冲区的字节顺序
         * OpenGL在底层的实现是C语言，与Java默认的数据存储字节顺序可能不同，即大端小端问题。
         * 因此，为了保险起见，在将数据传递给OpenGL之前，我们需要指明使用本机的存储顺序
         */
        vbb.order(ByteOrder.nativeOrder());
        // create a floating point buffer from the ByteBuffer
        mTriangleVB = vbb.asFloatBuffer();
        //将给定float[]数据从当前位置开始，依次写入此缓冲区
        mTriangleVB.put(triangleCoords);
        //设置此缓冲区的位置。如果标记已定义并且大于新的位置，则要丢弃该标记。
        mTriangleVB.position(0);
    }

    public int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public FloatBuffer getTriangleFloatBuffer() {
        return mTriangleVB;
    }
}
