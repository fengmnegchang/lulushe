
package com.fgj.lulushe.activity.util;

import java.lang.reflect.Field;

import com.fgj.lulushe.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


//BadgeUtil provides static utility methods to set "badge count" on Launcher (by Samsung, LG). 
//Currently, it's working from Android 4.0. 
//But some devices, which are released from the manufacturers, are not working.

public class BadgeUtil {
    private static final String TAG = "BadgeUtil";
    
    /**
     * Set badge count
     * 
     * @param context The context of the application package.
     * @param count Badge count to be set
     */
    public static void setBadgeCount(Context context, int count) {
    	String manufactuer = Build.MANUFACTURER;
//    	Toast.makeText(context, manufactuer, Toast.LENGTH_LONG).show();
    	try {
    		if(manufactuer.equalsIgnoreCase("samsung") || manufactuer.equalsIgnoreCase("lg")){
        		setBadgeSamsumgLG(context,count);
        	}else if(manufactuer.equalsIgnoreCase("sony")){
        		setBadgeSony(context,count);
        	}else if(manufactuer.equalsIgnoreCase("Xiaomi")){
        		setBadgeMIUI(context,count);
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    /**
     * Reset badge count. The badge count is set to "0"
     * 
     * @param context The context of the application package.
     */
    public static void resetBadgeCount(Context context) {
        setBadgeCount(context, 0);
    }

    /**
     * Retrieve launcher activity name of the application from the context
     *
     * @param context The context of the application package.
     * @return launcher activity name of this application. From the
     *         "android:name" attribute.
     */
    private static String getLauncherClassName(Context context) {
        PackageManager packageManager = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        // To limit the components this Intent will resolve to, by setting an
        // explicit package name.
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        // All Application must have 1 Activity at least.
        // Launcher activity must be found!
        ResolveInfo info = packageManager
                .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        // get a ResolveInfo containing ACTION_MAIN, CATEGORY_LAUNCHER
        // if there is no Activity which has filtered by CATEGORY_DEFAULT
        if (info == null) {
            info = packageManager.resolveActivity(intent, 0);
        }

        return info.activityInfo.name;
    }
    
    /**
     * MIUI 5/6
     * **/
    private static void setBadgeMIUI(Context context,int count) throws Exception{
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = null;
		boolean isMiUIV6 = true;
		try {
			NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
			builder.setContentTitle("您有" + count + "未读消息");
			builder.setTicker("您有" + count + "未读消息");
			builder.setAutoCancel(true);
			builder.setSmallIcon(R.drawable.ic_launcher);
			builder.setDefaults(Notification.DEFAULT_LIGHTS);
			
			notification = builder.build();
			Class miuiNotificationClass = Class.forName("android.app.MiuiNotification");
			Object miuiNotification = miuiNotificationClass.newInstance();
			Field field = miuiNotification.getClass().getDeclaredField("messageCount");
			field.setAccessible(true);
			field.set(miuiNotification, count+""); 
			field = notification.getClass().getField("extraNotification");
			field.setAccessible(true);
			field.set(notification, miuiNotification);
			
//			Toast.makeText(context, "MIUIV6", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace(); 
			// miui 6之前的版本
			isMiUIV6 = false;
			Intent localIntent = new Intent("android.intent.action.APPLICATION_MESSAGE_UPDATE");
			localIntent.putExtra("android.intent.extra.update_application_component_name",
					context.getPackageName() + "/" + getLauncherClassName(context));
			localIntent.putExtra("android.intent.extra.update_application_message_text",count+"");
			context.sendBroadcast(localIntent);
			Log.d(TAG, "BRAND = MIUIV5; BadgeCount = " + count);
//			Toast.makeText(context, "MIUIV5", Toast.LENGTH_LONG).show();
		} finally {
			if (notification != null && isMiUIV6) { // miui6以上版本需要使用通知发送
				nm.notify(101010, notification);
				Log.d(TAG, "BRAND = MIUIV6; BadgeCount = " + count);
			}
		}
	}

    /**
	 * Sony
	 * **/
	private static void setBadgeSony(Context context,int count) throws Exception{
		boolean isShow = false;        
		if (count>0) { 
			isShow = true;  
		} 
		Intent localIntent = new Intent();
		localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE",isShow);
		localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
		localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME",getLauncherClassName(context));
		localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", count);
		localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME",context.getPackageName());
		context.sendBroadcast(localIntent);
		Log.d(TAG, "BRAND = Sony; BadgeCount = " + count);
	}

	
	private static final String ACTION_BADGE_COUNT_UPDATE = "android.intent.action.BADGE_COUNT_UPDATE";
    private static final String EXTRA_BADGE_COUNT = "badge_count";
    private static final String EXTRA_BADGE_COUNT_PACKAGE_NAME = "badge_count_package_name";
    private static final String EXTRA_BADGE_COUNT_CLASS_NAME = "badge_count_class_name";
    /**
	 * Samsumg LG
	 * sdk >=14
	 * **/
	private static void setBadgeSamsumgLG(Context context,int count) throws Exception{
		Intent localIntent = new Intent(ACTION_BADGE_COUNT_UPDATE);
		localIntent.putExtra(EXTRA_BADGE_COUNT, count); 
		localIntent.putExtra(EXTRA_BADGE_COUNT_PACKAGE_NAME, context.getPackageName()); 
		localIntent.putExtra(EXTRA_BADGE_COUNT_CLASS_NAME, getLauncherClassName(context));  
		context.sendBroadcast(localIntent);
		Log.d(TAG, "BRAND = SamsumgLG; BadgeCount = " + count);
	}
	
	/**
	 * 是否支持 桌面小气泡
	 * 目前支持SAMSUNG，LG sdk大于14以上
	 * **/
	public static boolean isSupportBadge(Context context) {
		String manufactuer = Build.MANUFACTURER;
		if ((manufactuer.equalsIgnoreCase("samsung") || manufactuer.equalsIgnoreCase("lg") 
				|| manufactuer.equalsIgnoreCase("Xiaomi") || manufactuer.equalsIgnoreCase("sony"))
				&& Build.VERSION.SDK_INT >= 14) {
			return true;
		}
		return false;
	}
}
