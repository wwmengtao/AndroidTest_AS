package com.example.testmodule;

import android.util.Log;

public class ALog {
	private static String TAG_M = "M_T_AT_AS_testmodule.alog";
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
	
}
