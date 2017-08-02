package com.komect.wifi;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsl on 2017/8/1.
 */
public class AppUtil {

    public static boolean isGpsOPen(Context context) {
        // For Android < Android M, self permissions are always granted.
        boolean result = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getTargetSdkVersion(context) >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M
                result = checkPermissions(context, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION);
            } else {
                // targetSdkVersion < Android M
                result = selfPermissionGranted(context, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }
        return result;
    }


    /**
     * 检查系统权限是否被授权，如果没有授权，则执行请求授权的操作
     *
     * @return 权限检查通过返回true，不通过返回false
     */
    public static boolean checkPermissions(Context context, String... permissions) {
        List<String> lackPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager
                    .PERMISSION_GRANTED) {
                lackPermissions.add(permission);
            }
        }
        if (!lackPermissions.isEmpty()) {
            return false;
        }
        return true;
    }


    /**
     * targetSdkVersion < Android M
     * 检查系统权限是否被授权
     *
     * @return 权限检查通过返回true，不通过返回false
     */
    public static boolean selfPermissionGranted(Context context, String... permissions) {
        List<String> lackPermissions = new ArrayList<>();
        for (String permission : permissions) {
            // targetSdkVersion < Android M, we have to use PermissionChecker
            if (PermissionChecker.checkSelfPermission(context, permission) != PackageManager
                    .PERMISSION_GRANTED) {
                lackPermissions.add(permission);
            }
        }
        if (!lackPermissions.isEmpty()) {
            return false;
        }
        return true;
    }


    /**
     * 目标sdk版本
     */
    private static int getTargetSdkVersion(Context context) {
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
