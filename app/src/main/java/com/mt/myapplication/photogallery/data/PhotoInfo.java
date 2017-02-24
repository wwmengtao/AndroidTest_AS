package com.mt.myapplication.photogallery.data;

import com.mt.androidtest_as.alog.ALog;

/**
 * Created by Mengtao1 on 2017/2/9.
 */

public class PhotoInfo{
    private String id;
    private String owner;
    private String secret;
    private String server;
    private String farm;
    private String title;
    private String url_s;
    private int ispublic;
    private int isfriend;
    private int isfamily;
    private int height_s;
    private int width_s;

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
        this.id = id;
    }

    public String getUrl() {
        return url_s;
    }

    public void setUrl(String url_s) {
        ALog.Log("setUrl:"+url_s);
        this.url_s = url_s;
    }

    public int getWidth(){
        return width_s;
    }

    public int getHeight(){
        return height_s;
    }

    public void visitData(){
        ALog.Log("/*********************visitData**********************/");
        ALog.Log("id: "+id);
        ALog.Log("owner: "+owner);
        ALog.Log("secret: "+secret);
        ALog.Log("server: "+server);
        ALog.Log("farm: "+farm);
        ALog.Log("title: "+title);
        ALog.Log("url: "+url_s);
        ALog.Log("ispublic: "+ispublic);
        ALog.Log("isfriend: "+isfriend);
        ALog.Log("isfamily"+isfamily);
        
    }
}
