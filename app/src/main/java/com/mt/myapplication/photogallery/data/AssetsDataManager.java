package com.mt.myapplication.photogallery.data;

import android.app.Activity;

import com.mt.myapplication.photogallery.tools.FlickrFetchr;

import java.io.File;
import java.util.List;

/**
 * Created by Mengtao1 on 2017/2/22.
 */

public class AssetsDataManager {
    private static volatile AssetsDataManager mAssetsDataManager = null;
    private Activity mActivity = null;

    public static AssetsDataManager getDataManager(Activity mActivity){
        if(null == mAssetsDataManager){
            synchronized (AssetsDataManager.class){
                if(null == mAssetsDataManager){
                    mAssetsDataManager = new AssetsDataManager(mActivity);
                }
            }
        }
        return mAssetsDataManager;
    }

    private AssetsDataManager(Activity mActivity){
        this.mActivity = mActivity;
    }

    /**
     * getData:从Assets资源中获取数据
     * @param dataLoadCount：标识文件序号
     * @return
     */
    public List<PhotoInfo> getData(int dataLoadCount){
        //从assets文件中读取Json信息
        String assetDir = "Json";
        String fileName = String.format("photosJsonInfo%d.txt", dataLoadCount);
        if(!ifFileExist(assetDir, fileName))return null;
        String assetFileName = assetDir + File.separator + fileName;
        List<PhotoInfo> data = new FlickrFetchr().fetchItems2(mActivity, assetFileName);//直接从assets文件中解析Json信息
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
            assetFiles = mActivity.getAssets().list(assetDir);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return assetFiles;
    }

    public void clear(){
        if(null != assetFiles)assetFiles = null;
        mAssetsDataManager = null;
    }
}
