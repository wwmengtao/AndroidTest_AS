package com.mt.myapplication.photogallery;

import com.mt.androidtest_as.alog.ALog;

/**
 * Created by Mengtao1 on 2017/2/9.
 */

public class PhotoInfo{
    public String id;
    public String owner;
    public String secret;
    public String server;
    public String farm;
    public String title;
    public String url;
    public int ispublic;
    public int isfriend;
    public int isfamily;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        ALog.Log("setTitle:"+title);
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        ALog.Log("setId:"+id);
        id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        ALog.Log("setUrl:"+url);
        this.url = url;
    }

    private void visitData(){
        ALog.Log("/*********************visitData**********************/");
        ALog.Log("id: "+id);
        ALog.Log("owner: "+owner);
        ALog.Log("secret: "+secret);
        ALog.Log("server: "+server);
        ALog.Log("farm: "+farm);
        ALog.Log("title: "+title);
        ALog.Log("url: "+url);
        ALog.Log("ispublic: "+ispublic);
        ALog.Log("isfriend: "+isfriend);
        ALog.Log("isfamily"+isfamily);
    }
}
