package com.example.testmodule.activities.sys;

import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.testmodule.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * ANR检测利器：StrictMode
 * StrictMode意思为严格模式，是用来检测程序中违例情况的开发者工具。最常用的场景就是检测主线程中本地磁盘和网络读写等耗时的操作。
 * StrictMode主要检测两大问题，一个是线程策略，即TreadPolicy，另一个是VM策略，即VmPolicy。
 */
public class ANRActivity extends AppCompatActivity {
    private boolean DEVELOPER_MODE = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anr);
        writeToExternalStorage();
    }

    /**
     * 举个例子，直接写外部存储
     */
    public void writeToExternalStorage() {
        File externalStorage = Environment.getExternalStorageDirectory();
        File destFile = new File(externalStorage, "dest.txt");
        try {
            OutputStream output = new FileOutputStream(destFile, true);
            output.write("droidyue.com".getBytes());
            output.flush();
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 改进后的写外部存储
     */
    public void writeToExternalStorage_New() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                File externalStorage = Environment.getExternalStorageDirectory();
                File destFile = new File(externalStorage, "dest.txt");
                OutputStream output = null;
                try {
                    output = new FileOutputStream(destFile, true);
                    output.write("droidyue.com".getBytes());
                    output.flush();
                    output.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {//很重要，不然仍然会出现Closable对象未关闭的违例
                    if (null != output) {
                        try {
                            output.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }
}
