package com.fkzhang.wechatcloudbackup;

/**
 * Created by fkzhang on 1/16/2016.
 */
public class PackageNames {
    public String packageName;
    public String uploadButton;
    public String textView;
    public String class1;
    public String method1;
    public String method2;
    public String dexName;

    public PackageNames(String packageName, String versionName) {
        this.packageName = packageName;


        initNames();

        if (versionName.contains("6.3.13")) {
            set6313();
        } else if (versionName.contains("6.3.11")) {
            set6311();
        } else if (versionName.contains("6.3.9")) {
            set639();
        } else if (versionName.contains("6.3.8")) {
            set638();
        } else if (versionName.contains("6.3.5")) {
            set635();
        }
    }

    private void set635() {
        dexName = "secondary-2";
        uploadButton = "cpe";
        textView = "cmU";
        class1 += "ai";
        method1 = "tO";
        method2 = "rH";
    }

    private void set638() {
        dexName = "secondary-2";
        uploadButton = "ctn";
        textView = "crd";
        class1 += "ah";
        method1 = "tl";
        method2 = "re";
    }

    private void set639() {
        dexName = "secondary-2";
        uploadButton = "cwt";
        textView = "gWq";
        class1 += "ah";
        method1 = "tr";
        method2 = "rf";
    }

    private void set6311() {
        dexName = "secondary-1";
        uploadButton = "czZ";
        textView = "cxQ";
        class1 += "ah";
        method1 = "tD";
        method2 = "rn";
    }

    private void set6313() {
        dexName = "secondary-2";
        uploadButton = "czZ";
        textView = "cxQ";
        class1 += "ah";
        method1 = "tD";
        method2 = "rn";
    }

    private void initNames() {
        uploadButton = "";
        textView = "";
        class1 = packageName + ".model.";
        method1 = "";
        method2 = "";
        dexName = "";
    }
}
