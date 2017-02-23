package com.mt.myapplication.photogallery;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.mt.androidtest_as.alog.ALog;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * HandlerThreadImageDownloader：采用HandlerThread方式的串行图片加载器
 * @param <T>
 */
public class HandlerThreadImageDownloader<T> extends HandlerThread {
    private static final String TAG = "ThumbnailDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;
    
    private static volatile HandlerThreadImageDownloader<ViewHolder> mHandlerThreadImageDownloader;
    private ConcurrentMap<T,String> mRequestMap = new ConcurrentHashMap<>();
    private Handler mRequestHandler;
    private Handler mResponseHandler;
    private ImageDownloadListener<T> mImageDownloadListener;

    public interface ImageDownloadListener<T> {
        void onImageDownloaded(T target, Bitmap bitmap);
    }

    public void setImageLoadListener(ImageDownloadListener<T> listener) {
        mImageDownloadListener = listener;
    }

    public static HandlerThreadImageDownloader getImageLoader(Activity mActivity){
        if(null == mHandlerThreadImageDownloader){
            synchronized (HandlerThreadImageDownloader.class){
                if(null == mHandlerThreadImageDownloader){
                    mHandlerThreadImageDownloader = new HandlerThreadImageDownloader<>(mActivity);
                    mHandlerThreadImageDownloader.start();
                    mHandlerThreadImageDownloader.getLooper();
                }
            }
        }
        return mHandlerThreadImageDownloader;
    }

    public HandlerThreadImageDownloader(Activity mActivity) {
        super(TAG);
        mResponseHandler = new Handler(mActivity.getMainLooper());
    }

    public void queueToDownLoad(T target, String url) {
        ALog.Log("Got a URL: " + url);

        if (url == null) {
            mRequestMap.remove(target);
        } else {
            mRequestMap.put(target, url);
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, target)
                    .sendToTarget();
        }
    }

    /**
     * HandlerThread.start方法会导致线程run方法的调用，里面就有onLooperPrepared方法
        public void run() {
            mTid = Process.myTid();
            Looper.prepare();
            synchronized (this) {
                mLooper = Looper.myLooper();
                notifyAll();
            }
            Process.setThreadPriority(mPriority);
            onLooperPrepared();
            Looper.loop();
            mTid = -1;
        }
     */

    @Override
    protected void onLooperPrepared() {
        ALog.Log("onLooperPrepared");
        mRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    T target = (T) msg.obj;
                    ALog.Log("Got a request for URL: " + mRequestMap.get(target));
                    handleRequest(target);
                }
            }
        };
    }

    private void handleRequest(final T target) {
        try {
            final String url = mRequestMap.get(target);

            if (url == null) {
                return;
            }

            byte[] bitmapBytes = new FlickrFetchr().getUrlBytes(url);
            final Bitmap bitmap = BitmapFactory
                    .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            ALog.Log("Bitmap created");

            mResponseHandler.post(new Runnable() {
                public void run() {
                    if (mRequestMap.get(target) != url) {
                        return;
                    }

                    mRequestMap.remove(target);
                    if(null != mImageDownloadListener)mImageDownloadListener.onImageDownloaded(target, bitmap);
                }
            });
        } catch (IOException ioe) {
            Log.e(TAG, "Error downloading image", ioe);
        }
    }

    /**
     * HandlerThread类型的图片加载器停止工作
     */
    public void stopWorking(){
        mImageDownloadListener = null;
        mRequestMap.clear();
        mHandlerThreadImageDownloader.quit();
        mHandlerThreadImageDownloader = null;//如果不置为null，那么退出后进入后，由于非空，那么不会执行后续start以及getLooper
        mRequestHandler.removeCallbacksAndMessages(null);
        mResponseHandler.removeCallbacksAndMessages(null);
    }
}
