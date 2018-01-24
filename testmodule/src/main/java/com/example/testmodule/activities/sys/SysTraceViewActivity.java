package com.example.testmodule.activities.sys;

import android.os.Bundle;
import android.os.Debug;
import android.os.Trace;
import android.widget.TextView;

import com.example.testmodule.ALog;
import com.example.testmodule.BaseAcitivity;
import com.example.testmodule.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * SysTraceViewActivity：展示应用层的systrace和traceview的使用方法
 */
public class SysTraceViewActivity extends BaseAcitivity {
    private static final boolean SYSTRACE = true;
    private static final boolean TRACEVIEW = true;
    @BindView(R.id.traceviewlocation) TextView mTVTraceviewlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_trace_view);
        mUnbinder = ButterKnife.bind(this);

        if(TRACEVIEW) {//以下存储traceview log信息
            File path =  getExternalFilesDir(null);
            ALog.Log("path: "+path);//path即为最终的xx.trace文件的存储位置
            mTVTraceviewlocation.setText(path.getAbsolutePath());
            SimpleDateFormat date =
                    new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
            String logDate = date.format(new Date());
            // Applies the date and time to the name of the trace log.
            Debug.startMethodTracing("testmodule-SysTraceViewActivity" + logDate);
        }
        if(SYSTRACE){
            //应用层搜集Systrace信息，以DDMS为例，搜集此应用的systrace下列"SysTraceViewActivity.onCreate"和
            //"SysTraceViewActivity.onCreate.sleep"方法时，需要选择“Enable Application Traces from:”为
            //“com.example.testmodule”，否则此应用的systrace中将不出现下列两个方法
            Trace.beginSection("SysTraceViewActivity.onCreate");
            try {
                try {
                    Trace.beginSection("SysTraceViewActivity.onCreate.sleep");
                    ALog.sleep(2000);
                } finally {
                    Trace.endSection();
                }
                ALog.Log("SysTraceViewActivity.onDestroy.sleep end");
            } finally {
                Trace.endSection();
            }
        }//end if(SYSTRACE)
    }

    public void onDestroy(){
        if(TRACEVIEW) {//终止traceview log信息的收集
            Debug.stopMethodTracing();
        }
        super.onDestroy();
    }
}
