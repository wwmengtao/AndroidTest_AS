package com.fernandocejas.android10.sample.data;

import android.util.Log;

import com.fernandocejas.android10.sample.data.entity.UserEntityNT;

import java.util.Collection;

public class ALog {
	private static String TAG_M = "M_T_AT_AS_data";
	private  static String TAG_M1 = "M_T_AT_AS_data1";
	private  static String TAG_M2 = "M_T_AT_AS_data2";
	private  static String TAG_M_T = "M_T_AT_AS_data_TIME";
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

	public static void visitCollection(String TAG, Collection<UserEntityNT> mUserEntityCollection){
		if(null != mUserEntityCollection && mUserEntityCollection.size() > 0){
			ALog.Log(TAG+"VisitUserEntityNTData:");
			for(UserEntityNT mUserEntityNT : mUserEntityCollection){
				ALog.Log(mUserEntityNT.toString());
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
