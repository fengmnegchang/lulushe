package com.fgj.lulushe.activity.util;


import android.util.Log;

/**
 * @author sunShine
 * @version 创建时间：2014-11-17 下午1:58:14
 * @description 日志输出文件
 */

public class PALog {

	public static final String TAG = PALog.class.getName();
	public static final boolean  ISDUG = true; 
	/**
	 * 
	 * @Title
	 * @Description 错误信息log输出
	 * @params
	 * @return
	 * @throws
	 */
	public static void e(String msg) {

		if (ISDUG) {

			Log.e(TAG, msg);
		}
	}

	public static void e(Class<?> cls, String msg) {

		if (ISDUG) {

			Log.e(cls.getName(), msg);
		}
	}

	public static void w(String msg) {

		if (ISDUG) {

			Log.w(TAG, msg);
		}
	}

	public static void i(String msg) {

		if (ISDUG) {

			Log.i(TAG, msg);
		}
	}
}
