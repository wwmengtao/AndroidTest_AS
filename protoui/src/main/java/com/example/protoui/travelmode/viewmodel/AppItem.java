package com.example.protoui.travelmode.viewmodel;

import android.content.ComponentName;
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

import com.example.protoui.R;


/**
 * Created by huangzq2 on 2017/11/26.
 */
public class AppItem {
    // The target package of suggestion.
    public String mPackageName;
    // The target component resolved by package manager.
    public ComponentName mComponent;
    // The label of suggestion. Will show app name if label is null.
    public String mLabel;
    // The summary of suggestion like "12 min"
    public String mSummary;
    // The app icon of suggestion.
    public Bitmap mIcon;
    // The intent will be triggered.
    public Intent mIntent;

    AppItem(String packageName, ComponentName component, Intent intent, String label, String summary, Bitmap icon) {
        mPackageName = packageName;
        mComponent = component;
        mIntent = intent;
        mLabel = label;
        mSummary = summary;
        mIcon = icon;
    }

    public boolean isValid() {
        return mPackageName != null && mComponent != null && mIntent != null;
    }

    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable != null) {
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
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

    public static Bitmap getStyledIcon(Context context, Bitmap icon) {
        Bitmap backImage = ((BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_launcher_background, context.getTheme())).getBitmap();
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

    public static AppItem from(Context context, String packageName, ComponentName component,
                               Intent intent, String label, Drawable appIcon, String summary) {
        PackageManager pm = context.getPackageManager();
        Bitmap icon = null;

        if (intent != null) {

        } else if (component != null) {
            intent = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
                    .setComponent(component);
        } else {
            intent = pm.getLaunchIntentForPackage(packageName);
        }

        final ResolveInfo resolveInfo = intent != null ? pm.resolveActivity(intent, 0) : null;
        if (resolveInfo != null) {
            final ActivityInfo activityInfo = resolveInfo.activityInfo;
            component = new ComponentName(activityInfo.packageName, activityInfo.name);
            if (label == null) {
                label = activityInfo.loadLabel(pm).toString();
            }
            if (appIcon == null) {
                appIcon = activityInfo.loadIcon(pm);
            }
        } else {
            component = null;
            intent = null;
            // package may not installed.
        }

        if (appIcon != null) {
            icon = getStyledIcon(context, getBitmapFromDrawable(appIcon));
        }

        return new AppItem(packageName, component, intent, label, summary, icon);
    }
}
