package com.example.testmodule.image;


import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.example.testmodule.ALog;
import com.example.testmodule.ui.listview.ViewHolder;

import static com.example.testmodule.image.ImageLoader.IsLogRun;
import static com.example.testmodule.image.PicConstants.strangeSTR;

public class ImageProcess {
	private volatile static ImageProcess mInstance = null;
	private Context mContext = null;
	private static int displayMetricsWidth = 0;
	private static int displayMetricsHeight = 0;
	private static final String regPrefix = "[0-9]+"+strangeSTR;
	private static Pattern mPattern = Pattern.compile(regPrefix);
	public static enum StreamType{
		Asset
	}

	public static ImageProcess getInstance(Context context)	{
		if (mInstance == null){
			synchronized (ImageProcess.class){
				if (mInstance == null){
					mInstance = new ImageProcess(context);
				}
			}
		}
		return mInstance;
	}

	public ImageProcess(Context context){
		mContext = context.getApplicationContext();
	}

	/**
	 * 解析出最终想要的图片URL地址
	 * @param picUrl
	 * @return
	 */
	public String parsePicUrl(String picUrl){
		if(null==picUrl)return null;
		String picUrlNew=null;
		Matcher mMatcher = mPattern.matcher(picUrl);
		if(null!=mMatcher && mMatcher.find()){
			picUrlNew=picUrl.replace(mMatcher.group(),"");
		}
		return picUrlNew;
	}

	/**
	 * 获取特定采样率后解析图片
	 * @param Url 图片地址描述字符串
	 * @param mType 图片资源种类
	 * @param reqWidth 显示图片的ImageView宽度
	 * @param reqHeight 显示图片的ImageView高度
	 * @param isSample 是否对图片进行采样
	 * @return 采样(不采样)后的Bitmap
	 */
	public Bitmap decodeSampledBitmap(String Url, StreamType mType, int reqWidth, int reqHeight, boolean isSample){
		if(null == Url)return null;
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片信息
		if(!isSample){//不进行采样压缩图片
			return BitmapFactory.decodeStream(ImageDecodeInfo.getInstance(mContext).getInputStream(Url, mType));
		}
		InputStream mInputStream = null;
		Bitmap mBitmap = null;
		try{
			mInputStream = ImageDecodeInfo.getInstance(mContext).getInputStream(Url, mType);
			final BitmapFactory.Options options = new BitmapFactory.Options();
			//inJustDecodeBounds属性设置为true就可以让解析方法禁止为bitmap分配内存，返回值也不再是一个Bitmap对象，
			//而是null。虽然Bitmap是null了，但是BitmapFactory.Options的outWidth、outHeight和outMimeType属性都会被赋值。
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(mInputStream, null, options);
			if(IsLogRun)ALog.Log("imageHeight:"+options.outHeight+" imageWidth:"+options.outWidth+" imageType:"+options.outMimeType);
			//计算sampleSize值
			options.inSampleSize = calculatesampleSize(options, reqWidth, reqHeight);//options中宽高单位是像素
			//第二次解析将inJustDecodeBounds设置为false，结合获取到的sampleSize值再次解析图片
			options.inJustDecodeBounds = false;
			mInputStream = resetStream(mInputStream,Url, mType);
			mBitmap = BitmapFactory.decodeStream(mInputStream, null, options);  //decodeStream会尝试为已经构建的bitmap分配内存，这时就会很容易导致OOM出现
		}catch (OutOfMemoryError e) {// 捕获OutOfMemoryError，避免直接崩溃
			e.printStackTrace();
			ALog.Log("OutOfMemoryError");
		} finally{
			closeSilently(mInputStream);
		}
		return mBitmap;
	}

	protected InputStream resetStream(InputStream imageStream, String Url, StreamType mType){
		if (imageStream.markSupported()) {
			try {
				imageStream.reset();
				return imageStream;
			} catch (IOException e) {
			}
		}
		closeSilently(imageStream);
		return  ImageDecodeInfo.getInstance(mContext).getInputStream(Url, mType);
	}

	public void closeSilently(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
			}
		}
	}

	//根据期望控件的大小确定图片采样率，让宽高等比例压缩
	public int calculatesampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int outHeight = options.outHeight;
		final int outWidth = options.outWidth;
		int inSampleSize = 1;
		if (outHeight > reqHeight || outWidth > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int heightRatio = Math.round((float) outHeight / (float) reqHeight);
			final int widthRatio = Math.round((float) outWidth / (float) reqWidth);
			// 选择宽和高中最小的比率作为sampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高。
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		if(IsLogRun)ALog.Log("inSampleSize:"+inSampleSize);
		return inSampleSize;
	}


	/**
	 * getViewSize：获取mView的宽高，由于GridView获取单元格宽高需要多次测量最终才能确定，因此在最终获取真实宽高
	 * 之前必须给定默认值，此时的默认值是mView.getxxx()后的各个if判断中的数值。
	 * 注意：采用convertView.measure(0,0)后mView.getMeasuredXXX的方法获取的尺寸是不准确的
	 * @param mView
	 * @return
	 */
	public ViewSize getViewSize(View mView){
		if(null==mView)return null;
		LayoutParams lp = mView.getLayoutParams();
		int width = mView.getWidth();// 获取mView的实际宽度
		if (width <= 0){
			width = lp.width;// 获取mView在layout中声明的宽度
		}
		if (width <= 0){
			width = ViewHolder.ImageViewParas.defaultWidth;
		}
		if (width <= 0){
			width = getImageViewFieldValue(mView, "mMaxWidth");
		}
		//上述几种方法无法获取控件宽高，那么只能使用最大默认值，即屏幕宽高。

		//以下获取屏幕的宽高
		if(displayMetricsWidth <= 0){
			DisplayMetrics displayMetrics = null;
			displayMetrics = mView.getContext().getResources().getDisplayMetrics();
			displayMetricsWidth = displayMetrics.widthPixels;
			displayMetricsHeight = displayMetrics.heightPixels;
		}
		//
		if (width <= 0){
			width = displayMetricsWidth;
		}

		int height = mView.getHeight();// 获取imageview的实际高度
		if (height <= 0){
			height = lp.height;// 获取imageview在layout中声明的宽度
		}
		if (height <= 0){
			height = ViewHolder.ImageViewParas.defaultHeight;
		}
		if (height <= 0){
			height = getImageViewFieldValue(mView, "mMaxHeight");// 检查最大值
		}
		if (height <= 0){
			height = displayMetricsHeight;
		}
		if(IsLogRun)ALog.Log("width:"+width+" height:"+height);
		return new ViewSize(width, height);
	}

	private int getImageViewFieldValue(Object object, String fieldName){
		int value = 0;
		try{
			Field field = ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = (Integer) field.get(object);
			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE){
				value = fieldValue;
				if(IsLogRun)ALog.Log("value" + value);
			}
		} catch (Exception e){
		}
		return value;
	}

	public static class ViewSize{
		private int width;
		private int height;

		public ViewSize(int width, int height){
			this.width = width;
			this.height = height;
		}

		public int getWidth(){
			return width;
		}

		public int getHeight(){
			return height;
		}
	}
}
