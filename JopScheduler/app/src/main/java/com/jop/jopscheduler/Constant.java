package com.jop.jopscheduler;

import android.Manifest;

/**
 * Created by panwenjuan on 18-5-25.
 */

public class Constant {
    public static final String TAG = "JobService";
    public static final int JOB_ID = 1;

    public static final String[] BOOT_PERMISSION = new String[] {
            Manifest.permission.RECEIVE_BOOT_COMPLETED
    };
}
