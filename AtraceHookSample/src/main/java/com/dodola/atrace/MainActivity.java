package com.dodola.atrace;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Trace;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 23) {
            PermissionsChecker permissionChecher = new PermissionsChecker(this);
            if (permissionChecher.isPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                requestPermission();
            }
        }
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // boolean b = Atrace.hasHacks();
              //  if (b) {
                    Atrace.enableSystrace();
                    Trace.beginSection("--start---");
                    Toast.makeText(MainActivity.this, "开启成功", Toast.LENGTH_SHORT).show();
                    Trace.endSection();
                    Atrace.aSystraceStop();
               // }
            }
        });
    }
    public void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET},
                1);
    }
}
