package com.mt.myapplication.photogallery.data;

import android.content.Context;
import android.view.ContextThemeWrapper;

import com.mt.myapplication.photogallery.tools.FlickrFetchr;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.mt.myapplication.photogallery.data.JsonConstants.*;

/**
 * Created by Mengtao1 on 2017/2/22.
 */

public class DataManager {
    private static volatile DataManager mDataManager = null;
    private Context mContext = null;
    private static ContextThemeWrapper mContextThemeWrapper = null;
    //

    private static AtomicInteger dataLoadCountAtomic = null;//用来标识数据加载次数

    public static DataManager getDataManager(Context mContext){
        if(null == mContextThemeWrapper && mContext instanceof ContextThemeWrapper){
            mContextThemeWrapper = (ContextThemeWrapper)mContext;
        }
        if(null == mDataManager){
            synchronized (DataManager.class){
                if(null == mDataManager){
                    mDataManager = new DataManager(mContext);
                }
            }
        }
        return mDataManager;
    }

    private DataManager(Context mContext){
        this.mContext = mContext;
        dataLoadCountAtomic = new AtomicInteger(JSON_FILE_START_INDEX);
    }

    /**
     *getData:获取数据
     * @return
     */
    public List<PhotoInfo> getData(){
        //从assets文件中读取Json信息
        int dataLoadCount = dataLoadCountAtomic.get();
        String fileName = String.format("photosJsonInfo%d"+FILE_NAME_SUFFIX, dataLoadCount);
        if(!ifFileExist(AssetJsonDir, fileName))return null;
        String assetFileName = AssetJsonDir + File.separator + fileName;
        List<PhotoInfo> data = new FlickrFetchr().fetchItemsFromAssetsJson(mContext, assetFileName);//直接从assets文件中解析Json信息
        dataLoadCountAtomic.incrementAndGet();
        return data;
    }

    private boolean ifFileExist(String assetDir, String assetFileName){
        if(null == assetDir || null == assetFileName)return false;
        if(null == assetFiles)assetFiles=getAssetFiles(assetDir);
        if(null == assetFiles)return false;
        for(String str:assetFiles){
            if(str.equals(assetFileName))return true;
        }
        return false;
    }

    private String[] assetFiles = null;
    private String[] getAssetFiles(String assetDir){
        if(null != assetFiles)return assetFiles;
        try {
            assetFiles = mContextThemeWrapper.getAssets().list(assetDir);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return assetFiles;
    }

    public void clear(){
        if(null != assetFiles)assetFiles = null;
        mDataManager = null;
        mContextThemeWrapper = null;
        dataLoadCountAtomic = null;
    }
}
