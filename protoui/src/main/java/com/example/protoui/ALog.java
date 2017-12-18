package com.example.protoui;

import android.util.Log;

public class ALog {
	private static String TAG_M = "M_T_AT_AS_com.example.protoui";
	private  static String TAG_M1 = TAG_M+"_1";
	private  static String TAG_M2 = TAG_M+"_2";
	private  static String TAG_M3 = TAG_M+"_3";
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

	public static void Log3(String info){
		Log.e(TAG_M3, info+" ThreadID:"+Thread.currentThread().getId());
	}

	public static void fillInStackTrace(String info){
		RuntimeException RTE = new RuntimeException(info);
		RTE.fillInStackTrace();
		Log.e(TAG_M,"Called:", RTE);
	}
	
}
