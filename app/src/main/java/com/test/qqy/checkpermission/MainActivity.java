package com.test.qqy.checkpermission;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.test.qqy.checkpermission.aop.CheckPermission;
import com.test.qqy.checkpermission.aop.CheckPermissionAsp;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "QQ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckPermissionAsp.init(this);
        Button btn1 = (Button) findViewById(R.id.button1);
        Button btn2 = (Button) findViewById(R.id.button2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write2Storage("Hello", "World!");
            }
        });
    }

    @CheckPermission(Manifest.permission.CAMERA)
    private void openCamera() {
        Log.i(TAG, "openCamera -----> ");
    }

    @CheckPermission({Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private void write2Storage(String s1, String s2) {
        Log.i(TAG, "write2Storage -----> " + "\n" + s1 + " " + s2);
    }
}
