package com.ta.audid.permission;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;

@TargetApi(23)
class AppOpsManagerCompat23 {
    AppOpsManagerCompat23() {
    }

    public static String permissionToOp(String str) {
        return AppOpsManager.permissionToOp(str);
    }

    public static int noteOp(Context context, String str, int i, String str2) {
        return ((AppOpsManager) context.getSystemService(AppOpsManager.class)).noteOp(str, i, str2);
    }

    public static int noteProxyOp(Context context, String str, String str2) {
        return ((AppOpsManager) context.getSystemService(AppOpsManager.class)).noteProxyOp(str, str2);
    }
}