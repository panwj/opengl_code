package com.test.opengl;

import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.ConfigurationInfo;
import android.os.Build;
import android.util.Log;

/**
 * Created by panwenjuan on 18-4-10.
 */

public class OpenGlApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * 判断设备支持opengl版本，因为模拟器不能直接使用ConfigurationInfo做判断，所以增加了模拟器的判断
         */
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x2000;
        boolean isEmulator = Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                && (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86"));

        supportsEs2 = supportsEs2 || isEmulator;
        Log.d(Constant.TAG, "supportsEs2 = " + supportsEs2);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
