package com.example.testmodule.notification.appinfos;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengtao1 on 2018/1/2.
 */

public class AppInfoFetcher {
    private static volatile AppInfoFetcher mAppInfoFetcher = null;
    private Context mContext = null;
    public static final int MAX_APP_NUM = 5;

    public static AppInfoFetcher get(Context context){
        if(null == mAppInfoFetcher){
            mAppInfoFetcher = new AppInfoFetcher(context);
        }
        return mAppInfoFetcher;
    }

    private AppInfoFetcher(Context context){
        this.mContext = mContext;
    }

    public List<AppInfo> getAppInfos(int modeValue){
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        PackageManager pm = mContext.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        int mode = modeValue;
        int appIdxStart = mode;
        int max_app_num = MAX_APP_NUM;
        if (infos.size() >= max_app_num*4) {
            appIdxStart = mode*4;
        } else if (infos.size() >= (max_app_num-1)*4) {
            appIdxStart = mode*3;
        } else if (infos.size() >= (max_app_num-2)*4) {
            appIdxStart = mode*2;
        }

        int i = 0;
        int num = 0;
        for(ResolveInfo info  : infos ) {
            if ((i >= appIdxStart) && (num < max_app_num)) {
                appInfos.add(getAppInfo(info, pm));
                num++;
            }
            if (num == max_app_num) {
                break;
            }
            i++;
        }
        return appInfos;
    }

    private AppInfo getAppInfo(ResolveInfo info, PackageManager pm) {
        ActivityInfo ai = info.activityInfo;
        String appName = ai.applicationInfo.loadLabel(pm).toString();
        String packageName = ai.packageName;
        String className = ai.name;
        Drawable drawable = ai.loadIcon(pm);
        Bitmap icon =  getBitmapFromDrawable(drawable);
        AppInfo appInfo = new AppInfo(appName, packageName, className, getStyledIcon(mContext,icon));
        appInfo.setIntent(getLaunchIntent(packageName, className));
        return appInfo;
    }

    private Intent getLaunchIntent(String packageName, String activityName){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClassName(packageName, activityName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//FLAG_ACTIVITY_NEW_TASK一般配合FLAG_ACTIVITY_CLEAR_TOP使用
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable != null) {
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable)drawable).getBitmap();
            } else {
                Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                final Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                return bitmap;
            }
        }
        return null;
    }

    private static Bitmap getStyledIcon(Context context, Bitmap icon) {
        Bitmap backImage = ((BitmapDrawable)context.getResources().getDrawable(android.R.drawable.ic_menu_add, context.getTheme())).getBitmap();
        int w = backImage.getWidth();
        int h = backImage.getHeight();

        // create a bitmap for the result
        Bitmap result = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);

        // draw the background first
        mCanvas.drawBitmap(backImage, 0, 0, null);

        // create a mutable mask bitmap with the same mask
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(icon, (int) (w * 1.1), (int) (h * 1.1), true);
        Bitmap mutableMask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas maskCanvas = new Canvas(mutableMask);
        maskCanvas.drawBitmap(backImage, 0, 0, new Paint());

        // paint the bitmap with mask into the result
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(scaledBitmap, (w - scaledBitmap.getWidth()) / 2, (h - scaledBitmap.getHeight()) / 2, null);
        scaledBitmap.recycle();
        mCanvas.drawBitmap(mutableMask, 0, 0, paint);
        mutableMask.recycle();
        paint.setXfermode(null);

        // return it
        return result;
    }

}
