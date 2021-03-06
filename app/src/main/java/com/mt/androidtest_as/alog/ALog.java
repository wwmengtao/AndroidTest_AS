package com.mt.androidtest_as.alog;

import android.util.Log;

import com.fernandocejas.android10.sample.domain.UserNT;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModelNT;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ALog {
	private static String TAG_M = "M_T_AT_AS";
	private  static String TAG_M1 = "M_T_AT_AS1";
	private  static String TAG_M2 = "M_T_AT_AS2";
	private  static String TAG_M_T = "M_T_AT_AS_TIME";
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

	public static String toHexString(int mInt){
		return Integer.toHexString(mInt);
	}
	
	//eg:parseHexString("11"), result is 17
	public static int parseHexString(String mData){
		return Integer.parseInt(mData,16);
	}

	private static String formatStr="%-24s";
	private static String regPrefix = "([a-zA-Z0-9]+\\.)+";//匹配开头：小括号在正则表达式的作用是标记一个子表达式的开始和结束位置
	private static String regSuffix = "@[a-zA-Z0-9]+";//匹配结尾
	/**
	 * Activity的toString内容可能类似于"com.mt.androidtest.showview.1s.sdf2.s4rt.ShowViewActivity@7d129f7"，
	 * 下列函数仅仅提取ShowViewActivity之类的内容
	 * @param info
	 * @param obj
	 */
	public static void Log(String info, Object obj){
		String str = getActivityName(obj);
		if(null != str)Log(String.format(formatStr,info)+":"+str);
	}

	public static void fillInStackTrace(String info, Object obj){
		String str = getActivityName(obj);
		if(null != str)fillInStackTrace(info+":"+str);
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

	public static void visitCollection(String TAG, Collection<UserNT> mUserNTCollection){
		if(null != mUserNTCollection && mUserNTCollection.size() > 0){
			ALog.Log(TAG+" VisitUserNTData:");
			for(UserNT mUserNT : mUserNTCollection){
				ALog.Log(mUserNT.toString());
			}
		}
	}

	public static void visitCollection2(String TAG, Collection<UserModelNT> mUserModelNTCollection){
		if(null != mUserModelNTCollection && mUserModelNTCollection.size() > 0){
			ALog.Log(TAG+" VisitUserModelNTData:");
			for(UserModelNT mUserModelNT : mUserModelNTCollection){
				ALog.Log(mUserModelNT.toString());
			}
		}
	}

	public static void sleep(long sleepTime){
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
