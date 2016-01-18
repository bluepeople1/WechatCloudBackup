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

    public PackageNames(String packageName, String version) {
        this.packageName = packageName;

        initNames();
        switch (version) {
            case "6.3.9":
                set639();
                break;
            case "6.3.8":
                set638();
                break;
        }
    }

    private void set638() {
        uploadButton = "ctn";
        textView = "crd";
        class1 += "ah";
        method1 = "tl";
        method2 = "re";
    }

    private void set639() {
        uploadButton = "cwt";
        textView = "gWq";
        class1 += "ah";
        method1 = "tr";
        method2 = "rf";
    }

    private void initNames() {
        uploadButton = "";
        textView = "";
        class1 = packageName + ".model.";
        method1 = "";
        method2 = "";
    }
}
