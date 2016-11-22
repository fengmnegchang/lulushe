/**
 * @author   fengguangjing
 * @version  2015 下午1:48:49
 * @description 
 */
package com.fgj.lulushe.activity.util;

import java.util.List;


import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;


/**
 * @author EX-FENGGUANGJING001
 * 
 * 服务或者activity管理工具
 */
public class ManagerUtil {
	
	public static void startService(Context context, int seconds,
			Class<?> cls, String action) {
		// 获取AlarmManager系统服务
		AlarmManager manager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		// 包装需要执行Service的Intent
		Intent intent = new Intent(context, cls);
		intent.setAction(action);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		// 触发服务的起始时间
		long triggerAtTime = SystemClock.elapsedRealtime();

		// 使用AlarmManger的setRepeating方法设置定期执行的时间间隔（seconds秒）和需要执行的Service
		manager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime,
				seconds, pendingIntent);
	}
	// 停止轮询服务
	public static void stopService(Context context, Class<?> cls,
			String action) {
		AlarmManager manager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, cls);
		intent.setAction(action);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// 取消正在执行的服务
		manager.cancel(pendingIntent);

	}
	
	/**
	 * 用来判断服务是否运行.
	 * 
	 * @param context
	 * @param className
	 *            判断的服务名字
	 * @return true 在运行 false 不在运行
	 */
	public static boolean isServiceRunning(Context mContext,String serviceName) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(30);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(serviceName) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}
}
