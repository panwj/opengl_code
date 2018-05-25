/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jop.jopscheduler;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Yan Zhenjie on 2016/9/9.
 */
public class PermissionUtil {

    public static final int OVERLAY_PERMISSION_REQ_CODE = 1000;
    public static final int SETTINGS_REQUEST_CODE = 1001;

    /**
     * Check if the calling context has a set of permissions.
     *
     * @param context     {@link Context}.
     * @param permissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasPermission(@NonNull Context context, @NonNull String... permissions) {
        return hasPermission(context, Arrays.asList(permissions));
    }

    /**
     * Check if the calling context has a set of permissions.
     *
     * @param context     {@link Context}.
     * @param permissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasPermission(@NonNull Context context, @NonNull List<String> permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
        for (String permission : permissions) {
            String op = AppOpsManagerCompat.permissionToOp(permission);
            if (TextUtils.isEmpty(op)) continue;
            int result = AppOpsManagerCompat.noteProxyOp(context, op, context.getPackageName());
            if (result == AppOpsManagerCompat.MODE_IGNORED) return false;
            result = ContextCompat.checkSelfPermission(context, permission);
            if (result != PackageManager.PERMISSION_GRANTED) return false;
        }
        return true;
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param activity          {@link Activity}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(@NonNull Activity activity, @NonNull List<String> deniedPermissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;

        if (deniedPermissions.size() == 0) return false;

        for (String permission : deniedPermissions) {
            boolean rationale = activity.shouldShowRequestPermissionRationale(permission);
            if (!rationale) return true;
        }
        return false;
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param fragment          {@link android.support.v4.app.Fragment}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(
            @NonNull android.support.v4.app.Fragment fragment,
            @NonNull List<String> deniedPermissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;

        if (deniedPermissions.size() == 0) return false;

        for (String permission : deniedPermissions) {
            boolean rationale = fragment.shouldShowRequestPermissionRationale(permission);
            if (!rationale) return true;
        }
        return false;
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param fragment          {@link android.app.Fragment}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(
            @NonNull android.app.Fragment fragment,
            @NonNull List<String> deniedPermissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;

        if (deniedPermissions.size() == 0) return false;

        for (String permission : deniedPermissions) {
            boolean rationale = fragment.shouldShowRequestPermissionRationale(permission);
            if (!rationale) return true;
        }
        return false;
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param context           {@link Context}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(
            @NonNull Context context,
            @NonNull List<String> deniedPermissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;

        if (deniedPermissions.size() == 0) return false;

        if (!(context instanceof Activity)) return false;

        for (String permission : deniedPermissions) {
            boolean rationale = ((Activity) context).shouldShowRequestPermissionRationale(permission);
            if (!rationale) return true;
        }
        return false;
    }

    public static void openNotify(Context context) {
        // TODO Auto-generated method stub
        int currentApiVersion = Build.VERSION.SDK_INT;
        try {
            Object service = context.getSystemService("statusbar");
            Class<?> statusbarManager = Class
                    .forName("android.app.StatusBarManager");
            Method expand = null;
            if (service != null) {
                if (currentApiVersion <= 16) {
                    expand = statusbarManager.getMethod("expand");
                } else {
                    expand = statusbarManager
                            .getMethod("expandNotificationsPanel");
                }
                expand.setAccessible(true);
                expand.invoke(service);
            }

        } catch (Exception e) {
            Log.d("sss", "openNotify e = " + e.toString());
        }

    }

    /**
     * 判断 悬浮窗口权限是否打开
     * 由于android未提供直接跳转到悬浮窗设置页的api，此方法使用反射去查找相关函数进行跳转
     * 部分第三方ROM可能不适用
     *
     * @param context
     * @return true 允许  false禁止
     */
    public static boolean isAppOps(Context context) {
        try {
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = Integer.valueOf(24);
            arrayOfObject1[1] = Integer.valueOf(Binder.getCallingUid());
            arrayOfObject1[2] = context.getPackageName();
            int m = ((Integer) method.invoke(object, arrayOfObject1)).intValue();
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {

        }
        return false;
    }

    /**
     * 打开应用详情页
     *
     * @param context
     * @return true if it's open success.
     */
    public static boolean openAppSettings(Activity activity) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", activity.getApplicationContext().getPackageName(), null);
            intent.setData(uri);
            activity.startActivityForResult(intent, SETTINGS_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 打开悬浮窗设置页
     * 部分第三方ROM无法直接跳转可使用{@link #openAppSettings(Context)}跳到应用详情页
     *
     * @param context
     * @return true if it's open successful.
     */
    public static void openOpsSettings(Activity context) {
        try {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            context.startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            openAppSettings(context);
        }
    }

    /**
     * Drop notification bar
     * @param context
     */
    public static void collapseStatusBar(Context context) {
        try {
            Object statusBarManager = context.getSystemService("statusbar");
            Method collapse;

            if (Build.VERSION.SDK_INT <= 16) {
                collapse = statusBarManager.getClass().getMethod("collapse");
            } else {
                collapse = statusBarManager.getClass().getMethod("collapsePanels");
            }
            collapse.invoke(statusBarManager);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }
}
