package com.example.testmodule.activities.sys;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.testmodule.BaseActivity;
import com.example.testmodule.R;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

public class OOMActivity extends BaseActivity {
    //错误使用
    private SoftReference<List<Bitmap>> softReference = new SoftReference<List<Bitmap>>(new ArrayList<Bitmap>());
    private WeakReference<List<Bitmap>> weakReference = new WeakReference<List<Bitmap>>(new ArrayList<Bitmap>());
    //正确使用
    private List<SoftReference<Bitmap>> rightRefrence = new ArrayList<>();
    //
    Resources mResources = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oom);
        mResources = getResources();
    }

    @OnClick(R.id.btn11)
    public void OOMCase1(){//此时软引用并不会被回收，原因是用法错误
        List<Bitmap> list = softReference.get();
        if (list == null) {
            list = new ArrayList<>();
            softReference = new SoftReference<>(list);
        }

        for (int i = 0; i < 100; i++) {
            Bitmap bmp = BitmapFactory.decodeResource(mResources, R.drawable.cat);
            list.add(bmp);
        }
    }

    @OnClick(R.id.btn12)
    public void OOMCase2(){//此时弱引用并不会被回收，原因是用法错误
        List<Bitmap> list = null;
        Resources res = getResources();
        for (int i = 0; i < 100; i++) {
            Bitmap bmp = BitmapFactory.decodeResource(mResources, R.drawable.grassland);
            list = weakReference.get();
            if (list == null) {
                list = new ArrayList<>();
                weakReference = new WeakReference<>(list);
            }
            list.add(bmp);
        }
    }

    @OnClick(R.id.btn20)
    public void notOOM(){//避免OOM的争取用法
        for (int i = 0; i < 100; i++) {
            Bitmap bmp = BitmapFactory.decodeResource(mResources, R.drawable.cat);
            rightRefrence.add(new SoftReference<>(bmp));
        }
    }
}
