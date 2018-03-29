package com.example.testmodule.activities.sys;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.Trace;
import android.widget.TextView;

import com.example.testmodule.ALog;
import com.example.testmodule.BaseActivity;
import com.example.testmodule.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * SysTraceViewActivity：展示应用层的systrace和traceview的使用方法
 */
public class SysTraceViewActivity extends BaseActivity {
    private static final boolean SYSTRACE = true;
    private static final boolean TRACEVIEW = true;
    @BindView(R.id.traceviewlocation) TextView mTVTraceviewlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_trace_view);
        mUnbinder = ButterKnife.bind(this);

        if(TRACEVIEW) {//以下存储traceview log信息，traceview仅仅用于查看当前运行app的信息
            File path =  getExternalFilesDir(null);
            ALog.Log("path: "+path);//path即为最终的xx.trace文件的存储位置
            mTVTraceviewlocation.setText(path.getAbsolutePath());
            SimpleDateFormat date =
                    new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
            String logDate = date.format(new Date());
            ALog.sleep(2000);
            // Applies the date and time to the name of the trace log.
            Debug.startMethodTracing("testmodule-TraceViewActivity" + logDate);
        }
        if(SYSTRACE){//systrace可以查看整个系统各个方面的性能信息
            //应用层搜集Systrace信息，以DDMS为例，搜集此应用的systrace下列"SysTraceViewActivity.onCreate"和
            //"SysTraceViewActivity.onCreate.sleep"方法时，需要选择“Enable Application Traces from:”为
            //“com.example.testmodule”，否则此应用的systrace中将不出现下列两个方法
            Trace.beginSection("SysTraceViewActivity.onCreate");
            try {
                try {
                    Trace.beginSection("SysTraceViewActidvity.onCreate.sleep");
                    ALog.sleep(3000);
                } finally {
                    Trace.endSection();
                }
                ALog.Log("SysTraceViewActivity.onDestroy.sleep end");
            } finally {
                Trace.endSection();
            }
        }//end if(SYSTRACE)
    }

    @Override
    protected void onResume(){
        super.onResume();
        new reportFullyDrawnTask().execute();
    }

    public void onDestroy(){
        if(TRACEVIEW) {//终止traceview log信息的收集
            Debug.stopMethodTracing();
        }
        super.onDestroy();
    }

    //警惕内部类FetchItemsTask可能对SysTraceViewActivity造成的内存泄漏
    private class reportFullyDrawnTask extends AsyncTask<Void,Integer,List<String>> {
        @Override
        protected List<String> doInBackground(Void... params) {
            List<String> data = new ArrayList<>();
            ALog.sleep(2 * 1000);
            return data;
        }

        @Override
        protected void onPostExecute(List<String> items) {
            /**
             * reportFullyDrawn衡量这些异步加载资源所耗费的时间，一般在数据全部加载完毕后，手动调用，这样就会在Log中增加一条日志：
             * 03-28 03:27:48.998  1752  2401 I ActivityManager: Fully drawn com.example.testmodule/.activities.sys.SysTraceViewActivity: +7s325ms
             */

            /**下列ActivityManager: Displayed反应了Activity从启动到UI显示出来的耗时，比较适合测量程序的启动时间。
             * 系统日志中的Display Time只是布局的显示时间，并不包括一些数据的懒加载等消耗的时间
             * 03-28 03:30:53.203  1752  1801 I ActivityManager: Displayed com.example.testmodule/.activities.sys.SysTraceViewActivity: +5754
             */
            reportFullyDrawn();
        }
    }
}
