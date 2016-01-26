package com.fkzhang.wechatcloudbackup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

import de.robv.android.xposed.XC_MethodHook;

import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

/**
 * Created by fkzhang on 1/18/2016.
 */
public class CloudBackupHook {
    private final PackageNames mP;

    public CloudBackupHook(PackageNames packageNames) {
        this.mP = packageNames;
    }

    public void hook(ClassLoader loader) {
        findAndHookMethod("android.support.a.a", loader, "a", Object.class, String.class,
                Object[].class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        for (Object o : (Object[]) param.args[2]) {
                            String v;
                            try{
                                v = ((File) getObjectField(o, "file")).getName();
                            }
                            catch (Throwable t){
                                v = ((File) getObjectField(o, "zip")).getName();
                            }
                            if (v.contains(mP.dexName)) {
                                hookBackup((ClassLoader) getObjectField(
                                        param.args[0], "definingContext"));
                                break;
                            }
                        }
                    }
                });
    }

    protected void hookBackup(final ClassLoader loader) {
        findAndHookMethod(mP.packageName + ".plugin.backup.moveui.BakMoveUI", loader,
                "onCreate", Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);

                        TextView textView = (TextView) getObjectField(param.thisObject, mP.textView);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Activity activity = (Activity) param.thisObject;
                                Intent intent = new Intent();
                                intent.setClassName(activity, mP.packageName +
                                        ".plugin.backup.ui.BakChatUI");
                                // look for 68416
                                int uin = (int) callMethod(callMethod(
                                        callStaticMethod(findClass(mP.class1, loader), mP.method1),
                                        mP.method2), "get", 68416, Integer.valueOf(0));

                                intent.putExtra("downloadUin", uin);
                                activity.startActivity(intent);
                            }
                        });
                    }
                });
        findAndHookMethod(mP.packageName + ".plugin.backup.ui.BakChatUI", loader, "onResume",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Button upload = (Button) getObjectField(param.thisObject, mP.uploadButton);
                        upload.setVisibility(View.VISIBLE);
                    }
                });
    }

}
