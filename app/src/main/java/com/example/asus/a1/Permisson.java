package com.example.asus.a1;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

public class Permisson {

    private String[] permissions;
    public void checkPermission(Activity activity){

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(activity, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            System.out.print(i);
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                showDialogTipUserRequestPermission(activity);
            }
        }
    }
    private void showDialogTipUserRequestPermission(final Activity activity) {

        new AlertDialog.Builder(activity)
                .setTitle("存储权限不可用")
                .setMessage("由于需要获取存储空间，读取表格；\n否则，您将无法正常使用支付宝")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        startRequestPermission(activity);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setCancelable(false).show();
    }
    private void startRequestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, permissions, 321);
    }
}
