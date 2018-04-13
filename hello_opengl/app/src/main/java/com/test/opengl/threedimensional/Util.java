package com.test.opengl.threedimensional;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by panwenjuan on 18-4-12.
 */

public class Util {

    public static FloatBuffer floatToBuffer(float[] floatArray) {
        if (floatArray == null) return null;
        ByteBuffer buffer = ByteBuffer.allocateDirect(floatArray.length * 4);
        buffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = buffer.asFloatBuffer();
        floatBuffer.put(floatArray);
        floatBuffer.position(0);
        return floatBuffer;
    }

    public static int byte4ToInt(byte[] bytes, int offset) {
        int b3 = bytes[offset + 3] & 0xFF;
        int b2 = bytes[offset + 2] & 0xFF;
        int b1 = bytes[offset + 1] & 0xFF;
        int b0 = bytes[offset + 0] & 0xFF;
        return (b3 << 24) | (b2 << 16) | (b1 << 8) | b0;
    }

    public static short byte2ToShort(byte[] bytes, int offset) {
        int b1 = bytes[offset + 1] & 0xFF;
        int b0 = bytes[offset + 0] & 0xFF;
        return (short) ((b1 << 8) | b0);
    }

    public static float byte4ToFloat(byte[] bytes, int offset) {

        return Float.intBitsToFloat(byte4ToInt(bytes, offset));
    }
}
