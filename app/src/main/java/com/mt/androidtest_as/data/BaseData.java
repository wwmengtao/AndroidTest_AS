package com.mt.androidtest_as.data;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Mengtao1 on 2016/12/22.
 */

public class BaseData {
    private UUID mUUID = null;
    private String mTitle = null;
    public BaseData() {
        this(UUID.randomUUID());
    }

    public BaseData(UUID id){
        mUUID = id;
    }

    public void setTitle(String str){
        mTitle = str;
    }

    public UUID getID(){
        return mUUID;
    }

    public String getTitle(){
        return mTitle;
    }
}
