package com.mt.myapplication.photogallery.tools;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mt.androidtest_as.alog.ALog;
import com.mt.myapplication.photogallery.data.PhotoInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FlickrFetchr {

    private static final String TAG = "FlickrFetchr";

    private static final String API_KEY = "f2b8c44717fdd9a9a4a238692096e3d4";

    /**
     * getUrlBytes：从网络获取urlSpec所对应的信息
     * @param urlSpec
     * @return
     * @throws IOException
     */
    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    /**
     * fetchItems：直接从网络获取Json信息并解析
     * @return
     */
    public List<PhotoInfo> fetchItems() {

        List<PhotoInfo> items = null;

        try {
            String url = Uri.parse("https://api.flickr.com/services/rest/")
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.photos.getRecent")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("extras", "url_s")
                    .appendQueryParameter("page", "2")//指明获取第几页的内容
                    .build().toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            parseJson(items, jsonString);//直接解析
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
            return null;
        }

        return items;
    }

    /**
     * parseJson：直接解析Json信息
     * @param items
     * @param jsonString
     */
    private void parseJson(List<PhotoInfo> items, String jsonString){
        try {
            JSONObject jsonBody = new JSONObject(jsonString);
            JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
            JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");

            for (int i = 0; i < photoJsonArray.length(); i++) {
                JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

                PhotoInfo item = new PhotoInfo();
                item.setId(photoJsonObject.getString("id"));
                item.setTitle(photoJsonObject.getString("title"));
                ALog.Log("url_s:"+photoJsonObject.getString("url_s"));
                if (photoJsonObject.has("url_s")) {
                    item.setUrl(photoJsonObject.getString("url_s"));
                } else {
                    item.setUrl(null);
                }
                items.add(item);
            }
        }catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
    }


    /**
     * fetchItems2:直接从Json文件中获取Json信息
     * @return
     */
    public List<PhotoInfo> fetchItems2(Context mContext, String fileName) {
        List<PhotoInfo> items = gsonParseJson(getJsonInfoFromFile(mContext,fileName));
        return items;
    }

    /**
     * gsonParseJson：使用Gson解析JSON_INFO中保存的Json信息
     * @param JSON_INFO：保存Json信息的字串
     */
    public List<PhotoInfo> gsonParseJson(String JSON_INFO){
        List<PhotoInfo> items = null;
        try {
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(JSON_INFO).getAsJsonObject();
            //将data节点下的内容转为JsonArray
            JsonArray jsonArray = jsonObject.getAsJsonObject("photos").getAsJsonArray("photo"); //getAsJsonObject用于过滤photo的上级节点
            items = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                //获取第i个数组元素
                JsonElement el = jsonArray.get(i);
                //映射为类实例
                PhotoInfo pi = gson.fromJson(el, PhotoInfo.class);
                items.add(pi);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return items;
    }

    /**
     * getJsonInfoFromFile：从Json文件中获取Json数据
     * @return
     */
    public String getJsonInfoFromFile(Context mContext, String assetFileName) {
        String json = "";
        try {
            AssetManager mAssetManager = mContext.getAssets();
            InputStream mInputStream = mAssetManager.open(assetFileName);//从Asset文件夹中读取高清图片
            InputStreamReader inputStreamReader = new InputStreamReader(mInputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append('\n');
                line = br.readLine();
            }
            json = sb.toString();

            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
