package com.mt.myapplication.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.io.File;

public class PictureUtils {
    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay()
                .getSize(size);

        return getScaledBitmap(path, size.x, size.y);
    }

    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        // read in the dimensions of the image on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / destHeight);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(path, options);
    }

    public static void updateImageView(final ImageView mPhotoView, final File mPhotoFile, final Activity mActiviy) {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            mPhotoView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                int widthOfView = mPhotoView.getWidth();
                int heightOfView = mPhotoView.getHeight();
                Bitmap bitmap = null;
                if(0 == widthOfView||0 == heightOfView){
                    bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),mActiviy);
                }else {
                    bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),
                            widthOfView, heightOfView);
                }

                mPhotoView.setImageBitmap(bitmap);
                mPhotoView.getViewTreeObserver().removeOnGlobalLayoutListener(this);//取消监听
                }
            });

        }
    }
}
