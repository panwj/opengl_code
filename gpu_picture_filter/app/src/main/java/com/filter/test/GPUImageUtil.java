package com.filter.test;

/**
 * Created by panwenjuan on 18-8-8.
 */

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImage3x3ConvolutionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImage3x3TextureSamplingFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageAddBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageAlphaBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBilateralFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBoxBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageCGAColorspaceFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageChromaKeyBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorBalanceFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorBurnBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorDodgeBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorMatrixFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageCrosshatchFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDarkenBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDifferenceBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDilationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDirectionalSobelEdgeDetectionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDissolveBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDivideBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageEmbossFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageExclusionBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFalseColorFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGlassSphereFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHalftoneFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHardLightBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHazeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHighlightShadowFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageKuwaharaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLaplacianFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLevelsFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLightenBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLinearBurnBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLookupFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLuminosityBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageMonochromeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageMultiplyBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageNonMaximumSuppressionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageNormalBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageOpacityFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageOverlayBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePixelationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePosterizeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRGBDilationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRGBFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageScreenBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSketchFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSmoothToonFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSobelEdgeDetection;
import jp.co.cyberagent.android.gpuimage.GPUImageSobelThresholdFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSoftLightBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSourceOverBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSphereRefractionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSubtractBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSwirlFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageToneCurveFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageToonFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageTransformFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageVignetteFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageWeakPixelInclusionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageWhiteBalanceFilter;

public class GPUImageUtil {

    private static GPUImageFilter filter;

    //饱和度、亮度等参数指数
    private static int count;

    /**
     * 获取过滤器
     *
     * @param GPUFlag
     * @return 滤镜类型
     */
    public static GPUImageFilter getFilter(int GPUFlag) {
        switch (GPUFlag) {
            case 1:
                filter = new GPUImageGrayscaleFilter();
                break;
            case 2:
                //通常用于创建两个图像之间的动画变亮模糊效果
                filter = new GPUImageAddBlendFilter();
                break;
            case 3:
                //透明混合,通常用于在背景上应用前景的透明度
                filter = new GPUImageAlphaBlendFilter();
                break;
            case 4:
                //双边模糊
                filter = new GPUImageBilateralFilter();
                break;
            case 5:
                //盒状模糊
                filter = new GPUImageBoxBlurFilter();
                break;
            case 6:
                filter = new GPUImageBrightnessFilter();
                break;
            case 7:
                filter = new GPUImageBulgeDistortionFilter();
                break;
            case 8:
                filter = new GPUImageCGAColorspaceFilter();
                break;
            case 9:
                filter = new GPUImageChromaKeyBlendFilter();
                break;
            case 10:
                filter = new GPUImageColorBalanceFilter();
                break;
            case 11:
                //高斯模糊
                filter = new GPUImageGaussianBlurFilter();
                break;
            case 12:
                //RGB扩展边缘模糊，有色彩, 可以设置参数
                filter = new GPUImageRGBDilationFilter();
                break;
            case 13:
                filter = new GPUImage3x3ConvolutionFilter();
                break;
            case 14:
                filter = new GPUImage3x3TextureSamplingFilter();
                break;
            case 15:
                filter = new GPUImageColorBlendFilter();
                break;
            case 16:
                //色彩加深混合
                filter = new GPUImageColorBurnBlendFilter();
                break;
            case 17:
                //色彩减淡混合
                filter = new GPUImageColorDodgeBlendFilter();
                break;
            case 18:
                filter = new GPUImageColorInvertFilter();
                break;
            case 19:
                filter = new GPUImageColorMatrixFilter();
                break;
            case 20:
                filter = new GPUImageContrastFilter();
                break;
            case 21:
                filter = new GPUImageCrosshatchFilter();
                break;
            case 22:
                filter = new GPUImageDarkenBlendFilter();
                break;
            case 23:
                filter = new GPUImageDifferenceBlendFilter();
                break;
            case 24:
                //设置参数1, 2, 3, 4   扩展边缘模糊，变黑白
                filter = new GPUImageDilationFilter();
                break;
            case 25:
                filter = new GPUImageDirectionalSobelEdgeDetectionFilter();
                break;
            case 26:
                //可以设置参数
                filter = new GPUImageDissolveBlendFilter();
                break;
            case 27:
                filter = new GPUImageDivideBlendFilter();
                break;
            case 28:
                //浮雕效果，可以设置参数 from 0.0 to 4.0, with 1.0 as the normal level
                filter = new GPUImageEmbossFilter();
                break;
            case 29:
                filter = new GPUImageExclusionBlendFilter();
                break;
            case 30:
                //曝光量 可以设置参数 -10 ~ 10
                filter = new GPUImageExposureFilter();
                break;
            case 31:
                //可以设置参数
                filter = new GPUImageFalseColorFilter();
                break;
            case 32:
                //可以设置参数
                filter = new GPUImageGammaFilter();
                break;
            case 33:
                //高斯模糊， 可以设置参数
                filter = new GPUImageGlassSphereFilter();
                break;
            case 34:
                filter = new GPUImageHalftoneFilter();
                break;
            case 35:
                filter = new GPUImageHardLightBlendFilter();
                break;
            case 36:
                //可以设置参数
                filter = new GPUImageHazeFilter();
                break;
            case 37:
                //可以设置参数
                filter = new GPUImageHighlightShadowFilter();
                break;
            case 38:
                filter = new GPUImageHueBlendFilter();
                break;
            case 39:
                filter = new GPUImageHueFilter();
                break;
            case 40:
                //可以设置参数
                filter = new GPUImageKuwaharaFilter();
                break;
            case 41:
                //可以设置参数
                filter = new GPUImageLaplacianFilter();
                break;
            case 42:
                //可以设置参数
                filter = new GPUImageLevelsFilter();
                break;
            case 43:
                filter = new GPUImageLightenBlendFilter();
                break;
            case 44:
                filter = new GPUImageLinearBurnBlendFilter();
                break;
            case 45:
                //可以设置参数
                filter = new GPUImageLookupFilter();
                break;
            case 46:
                filter = new GPUImageLuminosityBlendFilter();
                break;
            case 47:
                //可以设置参数
                filter = new GPUImageMonochromeFilter();
                break;
            case 48:
                filter = new GPUImageMultiplyBlendFilter();
                break;
            case 49:
                //非最大抑制，只显示亮度最高的像素，其他为黑
                filter = new GPUImageNonMaximumSuppressionFilter();
                break;
            case 50:
                filter = new GPUImageNormalBlendFilter();
                break;
            case 51:
                //设置参数
                filter = new GPUImageOpacityFilter();
                break;
            case 52:
                filter = new GPUImageOverlayBlendFilter();
                break;
            case 53:
                filter = new GPUImagePixelationFilter();
                break;
            case 54:
                //设置参数
                filter = new GPUImagePosterizeFilter();
                break;
            case 55:
                //可以设置参数
                filter = new GPUImageRGBFilter();
                break;
            case 56:
                filter = new GPUImageSaturationBlendFilter();
                break;
            case 57:
                filter = new GPUImageScreenBlendFilter();
                break;
            case 58:
                //可以设置参数
                filter = new GPUImageSepiaFilter();
                break;
            case 59:
                //可以设置参数
                filter = new GPUImageSharpenFilter();
                break;
            case 60:
                filter = new GPUImageSketchFilter();
                break;
            case 61:
                //可以设置参数
                filter = new GPUImageSmoothToonFilter();
                break;
            case 62:
                //可以设置参数
                filter = new GPUImageSobelEdgeDetection();
                break;
            case 63:
                //可以设置参数
                filter = new GPUImageSobelThresholdFilter();
                break;
            case 64:
                filter = new GPUImageSoftLightBlendFilter();
                break;
            case 65:
                filter = new GPUImageSourceOverBlendFilter();
                break;
            case 66:
                //可以设置参数
                filter = new GPUImageSphereRefractionFilter();
                break;
            case 67:
                filter = new GPUImageSubtractBlendFilter();
                break;
            case 68:
                //可以设置参数
                filter = new GPUImageSwirlFilter();
                break;
            case 69:
                //可以设置参数
                filter = new GPUImageToneCurveFilter();
                break;
            case 70:
                //可以设置参数, 卡通
                filter = new GPUImageToonFilter();
                break;
            case 71:
                //可以设置参数
                filter = new GPUImageTransformFilter();
                break;
            case 72:
                //可以设置参数
                filter = new GPUImageVignetteFilter();
                break;
            case 73:
                filter = new GPUImageWeakPixelInclusionFilter();
                break;
            case 74:
                //可以设置参数
                filter = new GPUImageWhiteBalanceFilter();
                break;
        }
        return filter;
    }

    /**
     * 获取添加滤镜后的 bitmap
     * @param context
     * @param bitmap
     * @param FilterFlag
     * @return
     */
    public static Bitmap getGPUImageAfterFilter(Context context, Bitmap bitmap, int FilterFlag) {

        if (bitmap == null) return null;

        // 使用GPUImage处理图像
        GPUImage gpuImage = new GPUImage(context);
        gpuImage.setImage(bitmap);
        gpuImage.setFilter(getFilter(FilterFlag));
        bitmap = gpuImage.getBitmapWithFilterApplied();
        return bitmap;
    }

    public static Bitmap getGPUImageFromURL(String url) {
        Bitmap bitmap = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;
            int length = http.getContentLength();
            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    //根据传进来的数值设置素材饱和度
    public static Bitmap getGPUImageSaturation(Context context, Bitmap bitmap, int progress){

        if (bitmap == null) return null;

        // 使用GPUImage处理图像
        GPUImage gpuImage = new GPUImage(context);
        gpuImage.setImage(bitmap);
        gpuImage.setFilter(new GPUImageSaturationFilter(progress));
        bitmap = gpuImage.getBitmapWithFilterApplied();
        return bitmap;
    }

    //根据传进来的数值设置素材亮度
    public static Bitmap getGPUImageBrightness(Context context, Bitmap bitmap, int progress){

        if (bitmap == null) return null;

        // 使用GPUImage处理图像
        GPUImage gpuImage = new GPUImage(context);
        gpuImage.setImage(bitmap);
        gpuImage.setFilter(new GPUImageBrightnessFilter(progress * 0.01f));
        bitmap = gpuImage.getBitmapWithFilterApplied();
        return bitmap;
    }


    //调整饱和度、亮度等
    public static void changeSaturation(int curCount) {
        GPUImageUtil.count = curCount;
    }
}

