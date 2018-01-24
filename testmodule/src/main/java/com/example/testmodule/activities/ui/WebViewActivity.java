package com.example.testmodule.activities.ui;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.testmodule.BaseAcitivity;
import com.example.testmodule.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebViewActivity extends BaseAcitivity {
    @BindView(R.id.rl)
    LinearLayout ll;
    @BindView(R.id.wv)
    WebView wv;
    @BindView(R.id.wv2)
    WebView wv2;
    @BindView(R.id.btn)
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mUnbinder = ButterKnife.bind(this);

    }

    /**
     * htmlFiles：assets文件夹中html的文件名称
     */
    private static final String[] htmlFiles = {"sample3.html","sample2.html","sample.html"};
    private int index = 0;

    @OnClick(R.id.btn)
    public void onClick() {
        wv.setWebViewClient(new WebViewClient());
        wv.getSettings().setJavaScriptEnabled(true);
        // Get the Android assets folder path
        String folderPath = "file:android_asset/webview/";
        // Get the exact file location
        String file = null;
        if(index<htmlFiles.length){
            file = folderPath + htmlFiles[index++];
            wv.loadUrl(file);
        }else{
            wv.loadUrl("https://www.baidu.com/");//加载普通网页
        }

    }

    @OnClick(R.id.btn2)
    public void onClick2() {
        wv2.setWebViewClient(new WebViewClient());
                /*
                    WebSettings
                        Manages settings state for a WebView. When a
                        WebView is first created, it obtains a set
                        of default settings.

                    setJavaScriptEnabled(boolean flag)
                        Tells the WebView to enable JavaScript execution.
                 */
        wv2.getSettings().setJavaScriptEnabled(true);

        // Create a String of HTML
        String htmlString = "<h1>This is header one.</h1>\n" +
                "<h2>This is header two.</h2>\n" +
                "<h3>This is header three.</h3>";
        wv2.loadData(htmlString, "text/html", null);
    }
}
