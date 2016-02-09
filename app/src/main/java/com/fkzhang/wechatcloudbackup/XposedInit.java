package com.fkzhang.wechatcloudbackup;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

/**
 * Created by fkzhang on 1/16/2016.
 */
public class XposedInit implements IXposedHookLoadPackage {
    private SparseArray<CloudBackupHook> mWechatHooks;
    private String[] mSupportedVersions = {"6.3.13", "6.3.11", "6.3.9", "6.3.8",
            "6.3.5.50", "6.3.5"};

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        String packageName = loadPackageParam.packageName;
        if (!(packageName.contains("com.tencen") && packageName.contains("mm")))
            return;

        try {
            Object activityThread = callStaticMethod(findClass("android.app.ActivityThread", null), "currentActivityThread");
            Context mContext = (Context) callMethod(activityThread, "getSystemContext");

            try {

                String versionName = mContext.getPackageManager().getPackageInfo(packageName,
                        0).versionName;

                CloudBackupHook hooks = getHooks(packageName, versionName,
                        loadPackageParam.appInfo.uid);
                if (hooks != null)
                    hooks.hook(loadPackageParam.classLoader);
            } catch (Throwable e) {
                XposedBridge.log(e);
            }
        } catch (Throwable e) {
            XposedBridge.log(e);
        }
    }

    private CloudBackupHook getHooks(String packageName, String versionName, int uid) {
        if (mWechatHooks == null) {
            mWechatHooks = new SparseArray<>();
        }
        if (mWechatHooks.indexOfKey(uid) != -1) {
            return mWechatHooks.get(uid);
        }

        if (TextUtils.isEmpty(versionName))
            return null;

        if (isVersionSupported(versionName)) {
            mWechatHooks.put(uid, new CloudBackupHook(new PackageNames(packageName, versionName)));
        } else {
            XposedBridge.log("wechat version " + versionName + " not supported, please upgrade");
            return null;
        }
        return mWechatHooks.get(uid);
    }


    private boolean isVersionSupported(String versionName) {
        if (TextUtils.isEmpty(versionName))
            return false;

        for (String v : mSupportedVersions) {
            if (versionName.contains(v)) {
                return true;
            }
        }
        return false;
    }

}
