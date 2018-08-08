package com.filter.test;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRGBFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageToonFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageWhiteBalanceFilter;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;
    private ImageView mBaseView;
    private SeekBar mSaturation;
    private SeekBar mBrightness;
    private TextView mCount;
    private TextView mAdd;
    private TextView mFilter;
    private int mNum = 1;
    private GPUImage gpuImage;
    private Bitmap mBitmap = null;
    private float fRed;
    private float fGreen;
    private float fBlue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        gpuImage = new GPUImage(this);

        int uColor = getApplicationContext().getResources().getColor(R.color.colorAccent);
        float fAlpha = (float)(uColor >> 24) / 0xFF;
        fRed = (float)((uColor >> 16) & 0xFF) / 0xFF;
        fGreen = (float)((uColor >> 8) & 0xFF) / 0xFF;
        fBlue = (float)(uColor & 0xFF) / 0xFF;

        mBaseView = (ImageView) findViewById(R.id.iv_picture1);
        mImageView = (ImageView) findViewById(R.id.iv_picture);
        mSaturation = (SeekBar) findViewById(R.id.seekbar_saturation);
        mBrightness = (SeekBar) findViewById(R.id.seekbar_brightness);
        mCount = (TextView) findViewById(R.id.tv_low);
        mAdd = (TextView) findViewById(R.id.tv_add);
        mFilter = (TextView) findViewById(R.id.tv_num);

        mBitmap = getBaseBitmap();
        if (mBitmap != null) {
            mBaseView.setImageBitmap(mBitmap);
//            mImageView.setImageBitmap(mBitmap);
            test();
        }

        mSaturation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Bitmap bitmap = GPUImageUtil.getGPUImageSaturation(MainActivity.this, mBitmap, progress);
                if (bitmap != null)
                    mImageView.setImageBitmap(bitmap);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Bitmap bitmap = GPUImageUtil.getGPUImageBrightness(MainActivity.this, mBitmap, progress);
                if (bitmap != null)
                    mImageView.setImageBitmap(bitmap);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNum <= 1) return;
                --mNum;
                Bitmap bitmap = GPUImageUtil.getGPUImageAfterFilter(getApplicationContext(), mBitmap, mNum);
                if (bitmap != null) {
                    mImageView.setImageBitmap(bitmap);
                }
                mFilter.setText(String.valueOf(mNum));
            }
        });

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNum >= 74) return;
                ++mNum;
                Bitmap bitmap = GPUImageUtil.getGPUImageAfterFilter(getApplicationContext(), mBitmap, mNum);
                if (bitmap != null) {
                    mImageView.setImageBitmap(bitmap);
                }
                mFilter.setText(String.valueOf(mNum));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mSaturation != null) mSaturation.setOnSeekBarChangeListener(null);
        if (mBrightness != null) mBrightness.setOnSeekBarChangeListener(null);
        if (mBitmap != null && !mBitmap.isRecycled()) mBitmap.recycle();
    }

    private Bitmap getBaseBitmap() {
        AssetManager as = getApplicationContext().getAssets();
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = as.open("image_2.jpeg");
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            Log.e("GPUImage", "Error");
        }

        return bitmap;
    }

    private void test() {
        if (mBitmap == null) return;

        // 使用GPUImage处理图像
        GPUImage gpuImage = new GPUImage(this);
        gpuImage.setImage(mBitmap);
        gpuImage.setFilter(new GPUImageToonFilter((float) 0.2, (float) 15));
        Bitmap bitmap = gpuImage.getBitmapWithFilterApplied();
        mImageView.setImageBitmap(bitmap);
    }
}
