package com.fkzhang.wechatcloudbackup;

import android.content.Context;
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
            } catch (Exception e) {
                XposedBridge.log(e);
            }
        } catch (Exception e) {
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

        if (versionName == null)
            return null;

        String version = versionName.substring(0, versionName.lastIndexOf("."));

        switch (version) {
            case "6.3.11":
            case "6.3.9":
            case "6.3.8":
            case "6.3.5":
                mWechatHooks.put(uid, new CloudBackupHook(new PackageNames(packageName, version)));
                break;
            default:
                XposedBridge.log("wechat version not supported, please upgrade");
                return null;
        }

        return mWechatHooks.get(uid);
    }

}
