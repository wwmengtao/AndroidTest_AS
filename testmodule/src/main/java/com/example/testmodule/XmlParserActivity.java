package com.example.testmodule;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
import android.view.View;
import android.widget.Button;

import com.fernandocejas.android10.sample.data.ALog;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class XmlParserActivity extends AppCompatActivity {
    private static final String TAG = "XmlParserActivity";
    private Unbinder mUnbinder;
    private AssetManager mAssetManager = null;
    private String ioEncoding="UTF-8";
    private InputStream mInputStream=null;
    public static final String NoviceAssetsXmlDir = "novicetutorial"+File.separator+"xmlfiles";//玩家教程存储xml文件的Assets根目录


    @BindView(R.id.btn) Button btn;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, XmlParserActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml_parser);
        mAssetManager = getAssets();
        mUnbinder = ButterKnife.bind(this);
    }

    @OnClick(R.id.btn)
    public void onClick(View view){
        ALog.Log("getFirstEleSum: "+getFirstEleSum("mainscreen.xml", FIRST_ELEMENT_TAG));
        ALog.Log("getFirstEleSum: "+getFirstEleSum("message.xml", FIRST_ELEMENT_TAG));

    }

    private XmlPullParser parser = Xml.newPullParser();
    public static final String ROOT_ELEMENT_TAG = "functionkeyscontent";//文件根标签
    public static final String FIRST_ELEMENT_TAG = "item";//文件第一标签

    public int getFirstEleSum(String file, String firstEleTag){
        ALog.Log(TAG+"readFromXmlFile");
        int sum = 0;
        String fileName = NoviceAssetsXmlDir + File.separator + file;
        InputStream mInputStream = null;
        try {
            mInputStream = mAssetManager.open(fileName);
            parser.setInput(new BufferedInputStream(mInputStream), ioEncoding);
//            filterBeforeRootElement(parser, ROOT_ELEMENT_TAG);
            int type;
            while ((type= parser.next())!=XmlPullParser.END_DOCUMENT) {
                if(XmlPullParser.END_TAG==type && parser.getName().equals(firstEleTag)){
                    ALog.Log(TAG+"_attr:"+FIRST_ELEMENT_TAG);
                    sum ++;
                }//if
            }//while
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        } finally{
            if(null!=mInputStream){
                try {
                    mInputStream.close();
                    mInputStream=null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return sum;
    }

    public void filterBeforeRootElement(XmlPullParser parser, String rootElementName)
            throws XmlPullParserException, IOException {

        int type;
        //首先过滤掉非标签类事件
        while ((type=parser.next()) != XmlPullParser.START_TAG && type != XmlPullParser.END_DOCUMENT) {
            ;
        }
        //已经到了END_DOCUMENT
        if (type != XmlPullParser.START_TAG) {
            throw new XmlPullParserException("No start tag");
        }
        //已经到了START_TAG
        if (!parser.getName().equals(rootElementName)) {
            throw new XmlPullParserException("Unexpected start tag: "+parser.getName()+", expected: " + rootElementName);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mUnbinder.unbind();
    }
}
