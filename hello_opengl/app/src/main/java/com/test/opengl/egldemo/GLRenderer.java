package com.test.opengl.egldemo;

import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Surface;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by panwenjuan on 18-5-26.
 */

public class GLRenderer extends HandlerThread {

    private static final String TAG = "GLRenderer";

    // 顶点着色器的脚本
    private static final String verticesShader
            = "attribute vec2 vPosition; \n" // 顶点位置属性vPosition
            + "void main(){ \n"
            + " gl_Position = vec4(vPosition,0,1); \n" // 确定顶点位置
            + "}";
    // 片元着色器的脚本
    private static final String fragmentShader
            = "precision mediump float; \n" // 声明float类型的精度为中等(精度越高越耗资源)
            + "uniform vec4 uColor; \n" // uniform的属性uColor
            + "void main(){ \n"
            + " gl_FragColor = uColor; \n" // 给此片元的填充色
            + "}";

    private EGLConfig eglConfig = null;
    private EGLDisplay eglDisplay = EGL14.EGL_NO_DISPLAY;
    private EGLContext eglContext = EGL14.EGL_NO_CONTEXT;
    private int program;
    private int vPosition;
    private int uColor;

    private Handler handler = new Handler();

    public GLRenderer() {
        super("GLRenderer");
    }

    @Override
    public synchronized void start() {
        super.start();
        new Handler(getLooper()).post(new Runnable() {
            @Override
            public void run() {
                createGL();
            }
        });
    }

    public void release() {
        new Handler(getLooper()).post(new Runnable() {
            @Override
            public void run() {
                destroyGL();
                quit();
            }
        });
    }

    /**
     * 创建OpenGL ES环境
     */
    private void createGL() {
        //获取显示设备（默认的显示设备）
        eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        //初始化, 主版本，子版本
        int[] version = new int[2];
        if (!EGL14.eglInitialize(eglDisplay, version, 0, version, 1)) {
            throw new RuntimeException("EGL init error : " + EGL14.eglGetError());
        }

        //配置属性
        int[] configAttribs = {
          EGL14.EGL_BUFFER_SIZE, 32,
          EGL14.EGL_ALPHA_SIZE, 8,
          EGL14.EGL_BLUE_SIZE, 8,
          EGL14.EGL_GREEN_SIZE, 8,
          EGL14.EGL_RED_SIZE, 8,
          EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,//针对opengl es 2.0
          EGL14.EGL_SURFACE_TYPE, EGL14.EGL_WINDOW_BIT,
          EGL14.EGL_NONE//标志结束
        };

        int[] numConfigs = new int[1];
        EGLConfig[] configs = new EGLConfig[1];
        if (!EGL14.eglChooseConfig(eglDisplay, configAttribs, 0, configs, 0, configs.length, numConfigs, 0)) {
            throw new RuntimeException("EGL eglChooseConfig error : " + EGL14.eglGetError());
        }

        eglConfig = configs[0];
        //创建上下文, 选择的版本是opengl es 2.0
        int[] contextAttribs = {
                EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                EGL14.EGL_NONE
        };
        eglContext = EGL14.eglCreateContext(eglDisplay, eglConfig, EGL14.EGL_NO_CONTEXT, contextAttribs, 0);
        if (eglContext == EGL14.EGL_NO_CONTEXT) {
            throw new RuntimeException("EGL eglCreateContext error : " + EGL14.eglGetError());
        }
    }

    /**
     * 销毁OpenGL环境
     */
    private void destroyGL() {
        EGL14.eglDestroyContext(eglDisplay, eglContext);
        eglDisplay = EGL14.EGL_NO_DISPLAY;
        eglContext = EGL14.EGL_NO_CONTEXT;
    }

    /**
     * 加载shader的方法 GLSL语言
     * @param shaderType shader的类型 GLES20.GL_VERTEX_SHADER， GLES20.GL_FRAGMENT_SHADER
     * @param sourceCode sourceCode shader的脚本
     * @return shader索引
     */
    private int loadShader(int shaderType, String sourceCode) {
        //创建一个新的shader
        int shader = GLES20.glCreateShader(shaderType);
        //若创建成功则加载shader
        if (shader != 0) {
            //加载shader的源码
            GLES20.glShaderSource(shader, sourceCode);
            //编译shader
            GLES20.glCompileShader(shader);
            //存放编译成功shader数量的数组
            int[] compiled = new int[1];
            //获取shader编译的结果
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            ////若编译失败则显示错误日志并删除此shader
            if (compiled[0] == 0) {
                Log.e(TAG, "Could not compile shader " + shaderType + ":");
                Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    /**
     * 创建program程序
     * @param vertexSource
     * @param fragmentSource
     * @return
     */
    private int createProgram(String vertexSource, String fragmentSource) {
        //加载顶点着色器
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) {
            return 0;
        }
        //加载片元着色器
        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (pixelShader == 0) {
            return 0;
        }

        //创建程序
        int program = GLES20.glCreateProgram();
        //若程序创建成功则向程序中加入顶点着色器和片元着色器
        if (program != 0) {
            GLES20.glAttachShader(program, vertexShader);
            GLES20.glAttachShader(program, pixelShader);
            //链接程序
            GLES20.glLinkProgram(program);
            //存放链接成功program数量数组
            int[] linkStatus = new int[1];
            //获取链接情况
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            //若链接失败则报错并删除程序
            if(linkStatus[0] != GLES20.GL_TRUE) {
                Log.e("ES20_ERROR", "Could not link program: ");
                Log.e("ES20_ERROR", GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        Log.d(TAG, "program = " + program);
        return program;
    }

    /**
     * 获取图形的顶点
     * 特别提示:由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
     * 转换,关键是要通过ByteOrder设置nativeOrder(),否则有可能会出问题
     *
     * @return 顶点Buffer
     */
    private FloatBuffer getVertices() {
        float vertices[] = {
                0.0f, 0.5f,
                -0.5f, -0.5f,
                0.5f, -0.5f,
        };
        ByteBuffer buffer = ByteBuffer.allocateDirect(vertices.length * 4);
        buffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = buffer.asFloatBuffer();
        floatBuffer.put(vertices);
        floatBuffer.position(0);
        return floatBuffer;
    }

    public void render(Surface surface, int width, int height) {
        final int[] surfaceAttribs = {EGL14.EGL_NONE};
        EGLSurface eglSurface = EGL14.eglCreateWindowSurface(eglDisplay, eglConfig, surface, surfaceAttribs, 0);
        EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext);
        program = createProgram(verticesShader, fragmentShader);
        // 获取着色器中的属性引用id(传入的字符串就是我们着色器脚本中的属性名)
        vPosition = GLES20.glGetAttribLocation(program, "vPosition");
        uColor = GLES20.glGetUniformLocation(program, "uColor");
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glViewport(0, 0, width, height);
        FloatBuffer floatBuffer = getVertices();
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(program);
        // 为画笔指定顶点位置数据(vPosition)
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 0, floatBuffer);
        // 允许顶点位置数据数组
        GLES20.glEnableVertexAttribArray(vPosition);
        // 设置属性uColor(颜色 索引,R,G,B,A)
        GLES20.glUniform4f(uColor, 0.0f, 1.0f, 0.0f, 1.0f);
        //参数1: 绘制的形状； 参数2：从数组缓存中的哪一位开始绘制，一般都定义为0; 参数3：顶点的数量
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 3);
        // 交换显存(将surface显存和显示器的显存交换)
        EGL14.eglSwapBuffers(eglDisplay, eglSurface);
        EGL14.eglDestroySurface(eglDisplay, eglSurface);
    }

}
