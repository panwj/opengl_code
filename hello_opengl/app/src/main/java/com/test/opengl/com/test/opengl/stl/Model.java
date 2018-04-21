package com.test.opengl.com.test.opengl.stl;

import java.nio.FloatBuffer;

/**
 * Created by panwenjuan on 18-4-12.
 */

public class Model {
    //三角面个数
    private int facetCount;
    //顶点坐标数组
    private float[] verts;
    //每个顶点对应的法向量数组
    private float[] vnorms;
    //每个三角面的属性性息
    private short[] remarks;

    //顶点坐标数组转换而来的buffer;
    private FloatBuffer vertBuffer;

    //每个顶点坐标对应的法向量转换而来的buffer；
    private FloatBuffer vnormBuffer;

    //以下分别保存所有点在x,y,z方向的最大值、最小值
    float maxX;
    float maxY;
    float maxZ;
    float minX;
    float minY;
    float minZ;

    //获取模型的中心点
    public Point getCenterPoint() {
        float cx = minX + (maxX - minX) / 2;
        float cy = minY + (maxY - minY) / 2;
        float cz = minZ + (maxZ - minZ) / 2;
        return new Point(cx, cy, cz);
    }

    //获取包裹模型的最大半径
    public float getR() {
        float dx = (maxX - minX);
        float dy = (maxY - minY);
        float dz = (maxZ - minZ);
        float max = dx;
        if (dy > max) max = dy;
        if (dz > max) max = dz;
        return max;
    }

    //设置顶点数组的同时，设置对应的buffer
    public void setVerts(float[] verts) {
        this.verts = verts;
        vertBuffer = Util.floatToBuffer(verts);
    }

    //设置对应顶点数组向量的同时，设置对应的buffer
    public void setVnorms(float[] vnorms) {
        this.vnorms = vnorms;
        vnormBuffer = Util.floatToBuffer(vnorms);
    }

    public float[] getVerts() {
        return verts;
    }

    public float[] getVnorms() {
        return vnorms;
    }

    public short[] getRemarks() {
        return remarks;
    }

    public void setRemarks(short[] remarks) {
        this.remarks = remarks;
    }

    public int getFacetCount() {
        return facetCount;
    }

    public void setFacetCount(int facetCount) {
        this.facetCount = facetCount;
    }

    public FloatBuffer getVertBuffer() {
        return vertBuffer;
    }

    public void setVertBuffer(FloatBuffer vertBuffer) {
        this.vertBuffer = vertBuffer;
    }

    public FloatBuffer getVnormBuffer() {
        return vnormBuffer;
    }

    public void setVnormBuffer(FloatBuffer vnormBuffer) {
        this.vnormBuffer = vnormBuffer;
    }
}
