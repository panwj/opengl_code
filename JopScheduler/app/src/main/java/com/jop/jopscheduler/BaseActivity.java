package com.jop.jopscheduler;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by panwenjuan on 6/25/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private int permissionRequestCode = 88;
    private PermissionCallback permissionRunnable;

    public interface PermissionCallback {
        void hasPermission();

        void noPermission();
    }

    protected void handleIntent(Intent intent) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getIntent()) {
            handleIntent(getIntent());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Android M运行时权限请求封装
     *
     * @param permissionDes 权限描述
     * @param runnable      请求权限回调
     * @param permissions   请求的权限（数组类型），直接从Manifest中读取相应的值，比如Manifest.permission.WRITE_CONTACTS
     */
    public void performCodeWithPermission(@NonNull String permissionDes, PermissionCallback runnable, @NonNull String... permissions) {
        if (permissions == null || permissions.length == 0) return;
        this.permissionRunnable = runnable;
        if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.M) || PermissionUtil.hasPermission(this, permissions)) {
            if (permissionRunnable != null) {
                permissionRunnable.hasPermission();
                permissionRunnable = null;
            }
        } else {
            //permission has not been granted.
            requestPermission(permissionDes, permissionRequestCode, permissions);
        }

    }

    private void requestPermission(String permissionDes, final int requestCode, final String[] permissions) {
        if (shouldShowRequestPermissionRationale(permissions)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example, if the request has been denied previously.

            //If the user has refused this permission before, then prompt for authorization related permissions
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.permission_tip))
                    .setMessage(permissionDes)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(BaseActivity.this, permissions, requestCode);
                        }
                    }).show();

        } else {
            // Contact permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(BaseActivity.this, permissions, requestCode);
        }
    }

    private boolean shouldShowRequestPermissionRationale(String[] permissions) {
        boolean flag = false;
        for (String p : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, p)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == permissionRequestCode) {
            if (verifyPermissions(grantResults)) {
                if (permissionRunnable != null) {
                    permissionRunnable.hasPermission();
                    permissionRunnable = null;
                }
            } else {
                if (permissionRunnable != null) {
                    permissionRunnable.noPermission();
                    permissionRunnable = null;
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    public boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
