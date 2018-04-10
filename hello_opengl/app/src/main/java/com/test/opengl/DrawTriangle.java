package com.test.opengl;

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

        // initialize vertex Buffer for triangle
        ByteBuffer vbb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                triangleCoords.length * 4);

        vbb.order(ByteOrder.nativeOrder()); // use the device hardware's native byte order
        mTriangleVB = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
        mTriangleVB.put(triangleCoords);    // add the coordinates to the FloatBuffer
        mTriangleVB.position(0);            // set the buffer to read the first coordinate
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
