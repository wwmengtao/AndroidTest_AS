package com.example.framework_o;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ALog {
	private static String TAG_M = "M_T_AT_AS_framework_o.alog";
	private  static String TAG_M1 = TAG_M+"_1";
	private  static String TAG_M2 = TAG_M+"_2";
	private  static String TAG_M_T = TAG_M+"_TIME";
	public static void Log(String info){
		Log.e(TAG_M,info);
	}

	public static void Log1(String info){
		Log.e(TAG_M1, info);
	}

	public static void Log2(String info){
		Log.e(TAG_M2, info+" ThreadID:"+Thread.currentThread().getId());
	}

	
	public static void fillInStackTrace(String info){
		RuntimeException RTE = new RuntimeException(info);
		RTE.fillInStackTrace();
		Log.e(TAG_M,"Called:", RTE);
	}

	private static String formatStr="%-24s";
	private static String regPrefix = "([a-zA-Z0-9]+\\.)+";//匹配开头：小括号在正则表达式的作用是标记一个子表达式的开始和结束位置
	private static String regSuffix = "@[a-zA-Z0-9]+";//匹配结尾
	/**
	 * Activity的toString内容可能类似于"com.xxx.showview.1s.sdf2.s4rt.ShowViewActivity@7d129f7"，
	 * 下列函数仅仅提取ShowViewActivity之类的内容
	 * @param info
	 * @param obj
	 */
	public static void Log(String info, Object obj){
		String str = getActivityName(obj);
		if(null != str)Log(String.format(formatStr,info)+":"+str);
	}

	public static String getActivityName(Object obj){
		String str = null;
		if(null!=obj){
			str = obj.toString();
			if(null==str)return null;
			Pattern mPattern = null;
			Matcher mMatcher = null;
			mPattern = Pattern.compile(regPrefix);
			mMatcher = mPattern.matcher(str);
			if(mMatcher.find()){
				str=str.replace(mMatcher.group(), "");
			}
			mPattern = Pattern.compile(regSuffix);
			mMatcher = mPattern.matcher(str);
			if(mMatcher.find()){
				str=str.replace(mMatcher.group(),"");
			}
		}
		return str;
	}
}
