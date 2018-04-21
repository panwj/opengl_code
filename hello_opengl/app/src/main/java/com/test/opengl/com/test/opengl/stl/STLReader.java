package com.test.opengl.com.test.opengl.stl;

import android.content.Context;

import com.test.opengl.com.test.opengl.stl.Model;
import com.test.opengl.com.test.opengl.stl.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by panwenjuan on 18-4-13.
 */

public class STLReader {
    public static interface StlLoadListener {
        void start();
        void onLoading(int cur, int total);
        void onFinished();
        void onFailure(Exception e);
    }

    private StlLoadListener stlLoadListener;

    public Model parserBinStlInSDCard(String path) throws IOException {
        File file = new File(path);
        FileInputStream ins = new FileInputStream(file);
        return parserBinStl(ins);
    }

    public Model parserBinStlInAssets(Context context, String fileName) throws IOException {
        InputStream is = context.getAssets().open(fileName);
        return parserBinStl(is);
    }

    //解析二进制的stl文件
    private Model parserBinStl(InputStream in) throws IOException {
        if (stlLoadListener != null) {
            stlLoadListener.start();
        }

        Model model = new Model();

        //前面80字节是头文件，用于存储文件名
        in.skip(80);

        //紧接着用4个字节的整数来描述模型三角面片个数
        byte[] bytes = new byte[4];
        //读取三角面片个数
        in.read(bytes);
        int facetCount = Util.byte4ToInt(bytes, 0);
        model.setFacetCount(facetCount);
        if (facetCount == 0) {
            in.close();
            return model;
        }

        //每个三角面片占固定的50个字节
        byte[] facetBytes = new byte[50 * facetCount];
        //将所有的三角面片读取到字节数组
        in.read(facetBytes);
        //数据读取完毕后，可以把输入流关闭
        in.close();

        parserModel(model, facetBytes);

        if (stlLoadListener != null) {
            stlLoadListener.onFinished();
        }

        return model;
    }

    /**
     * 解析模型数据，包括顶点数据、法向量数据、所占空间范围等
     * @param model
     * @param facetBytes 所有三角面字节数
     */
    private void parserModel(Model model, byte[] facetBytes) {
        int facetCount = model.getFacetCount();
        /**
         * 每个三角面片占用固定的50个字节，50个字节当中：
         * 三角片的法向量：（1个向量相当于一个点）*（3维/点）*（4字节浮点数/维）=12字节
         * 三角片的三个点坐标：（3个点）*（3维/点）*（4字节浮点数/维）=36字节
         * 最后2个字节用来描述三角片的属性信息
         */

        //保存所有的顶点坐标信息，一个三角片3个点，一个点3个坐标轴
        float[] verts = new float[facetCount * 3 * 3];
        //保存所有三角面对应的法向量位置，一个三角面对应一个法向量，一个法向量3个点
        //而绘制模型时，是针对需要每个顶点对应的法向量，因此存储长度需要*3,又因同一个三角面的三个顶点的法向量
        // 是相同的。因此后面写入法向量数据的时候，只需要连续写入3个相同的法向量即可
        float[] vnorms = new float[facetCount * 3 * 3];
        //保存所有三角面的属性信息
        short[] remarks = new short[facetCount];

        int stloffest = 0;
        try {
            for (int i = 0; i < facetCount; i++) {
                if (stlLoadListener != null) {
                    stlLoadListener.onLoading(i, facetCount);
                }

                for (int j = 0; j < 4; j++) {
                    float x = Util.byte4ToFloat(facetBytes, stloffest);
                    float y = Util.byte4ToFloat(facetBytes, stloffest + 4);
                    float z = Util.byte4ToFloat(facetBytes, stloffest + 8);

                    stloffest += 12;

                    if (j == 0) {
                        //法向量
                        vnorms[i * 9] = x;
                        vnorms[i * 9 + 1] = y;
                        vnorms[i * 9 + 2] = z;
                        vnorms[i * 9 + 3] = x;
                        vnorms[i * 9 + 4] = y;
                        vnorms[i * 9 + 5] = z;
                        vnorms[i * 9 + 6] = x;
                        vnorms[i * 9 + 7] = y;
                        vnorms[i * 9 + 8] = z;
                    } else {
                        //三个顶点
                        verts[i * 9 + (j - 1) * 3] = x;
                        verts[i * 9 + (j - 1) * 3 + 1] = y;
                        verts[i * 9 + (j - 1) * 3 + 2] = z;

                        //记录模型中三个坐标轴方向的最大最小值
                        if (i == 0 && j == 1) {
                            model.minX = model.maxX = x;
                            model.minY = model.maxY = y;
                            model.minZ = model.maxZ = z;
                        } else {
                            model.minX = Math.min(model.minX, x);
                            model.minY = Math.min(model.minY, y);
                            model.minZ = Math.min(model.minZ, z);
                            model.maxX = Math.max(model.maxX, x);
                            model.maxY = Math.max(model.maxY, y);
                            model.maxZ = Math.max(model.maxZ, z);
                        }
                    }
                }
                short r = Util.byte2ToShort(facetBytes, stloffest);
                stloffest = stloffest + 2;
                remarks[i] = r;
            }
        } catch (Exception e) {
            if (stlLoadListener != null) {
                stlLoadListener.onFailure(e);
            } else {
                e.printStackTrace();
            }
        }

        //将读取到的数据保存到model中
        model.setRemarks(remarks);
        model.setVerts(verts);
        model.setVnorms(vnorms);
    }
}
