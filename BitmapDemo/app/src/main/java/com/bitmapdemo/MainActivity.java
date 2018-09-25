package com.bitmapdemo;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bitmapdemo.util.ImageUtils;

/**
 * Android开发中的Bitmap的mutable和imMutable详解
 *
 * 在Android开发中有时候我们需要获取一张图片为Bitmap，然后在这个bitmap上进行二次开发，比如new Canvas（Bitmap），
 * 然后再次在画布上进行绘制时就是对bitmap的二次开发，但是在newCanvas的时候，系统会判定bitmap对象是否是isMutable（ 是否可变），
 * if (!bitmap.isMutable()) {  throw new IllegalStateException("Immutable bitmap passed to Canvas constructor");  }
 * 如果不可变，将会抛出异常信息，另外在我们通过getPixel()获取图片信息后，在提过setPixel()设置时，如果bitmap是imMutable的也会抛出异常，
 * 下面就通过代码来把bitmap转换成为mutable的，以便于对bitmap的二次开发。
 *
 * 方法一：
 * 可以通过复制原来的bitmap对象，来改变可变性，这个方法不设计到内存的读写，会快一些；
 * Bitmap mutableBitmap = immutableBitmap.copy(Bitmap.Config.ARGB_8888, true);
 *
 * 方法二：
 * BitmapFactory.Options opt = new BitmapFactory.Options();
 * opt.inMutable = true;
 * Bitmap bp = BitmapFactory.decodeResource(getResources(),R.drawable.image, opt)
 */

public class MainActivity extends AppCompatActivity {

    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bitmap = ImageUtils.decodeBmpFromResource("/storage/emulated/0/BeaconFlashlight/wallpapers/41hpyq6x057cg5ze7igui7utf.png", 0, 0);
        ImageUtils.resizeBitmap(bitmap, 500, 500);
    }

    @Override
    protected void onResume() {
        super.onResume();

        bitmap = ImageUtils.decodeBmpFromResource("/storage/emulated/0/BeaconFlashlight/wallpapers/41hpyq6x057cg5ze7igui7utf.png", 0, 0);
        ImageUtils.resizeBitmap(bitmap, 500, 500);
    }
}
