package com.fgj.lulushe.activity.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.util.EncodingUtils;

import com.fgj.lulushe.wheelpicker.DatePicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;


public class CommTool {

	public static final String YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
	public static final String MMDDHHMM = "MM-dd HH:mm";
	public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	/**
	 * @Title: getScreenWidth
	 * @Description: 获取手机屏幕宽度
	 * @param context
	 * @return
	 * @throws
	 */
	public static int getScreenWidth(Context context) {
		if (context != null) {
			WindowManager wm = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics dm = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(dm);
			return dm.widthPixels;
		}
		return 0;
	}

	/**
	 * 得到状态栏高度
	 * 
	 * @return
	 */
	public static int getStatusBarHeight(Activity act) {

		/*
		 * 方法一，荣耀3c无效 Rect frame = new Rect();
		 * act.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		 * int statusBarHeight = frame.top; return statusBarHeight;
		 */

		/*
		 * 方法二，荣耀3c无效 Rect rectgle= new Rect(); Window window= act.getWindow();
		 * window.getDecorView().getWindowVisibleDisplayFrame(rectgle); int
		 * StatusBarHeight= rectgle.top; int contentViewTop=
		 * window.findViewById(Window.ID_ANDROID_CONTENT).getTop(); int
		 * statusBar = contentViewTop - StatusBarHeight; return statusBar;
		 */

		// 方法三，荣耀3c有效
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, sbar = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			sbar = act.getResources().getDimensionPixelSize(x);
			return sbar;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return 0;
	}

	/**
	 * @Title: getScreenHeight
	 * @Description: 获取手机屏幕高度
	 * @param context
	 * @return
	 * @throws
	 */
	public static int getScreenHeight(Context context) {
		if (context != null) {
			WindowManager wm = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics dm = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(dm);
			return dm.heightPixels;
		}
		return 0;
	}

	/**
	 * @Title: dpToPx
	 * @Description: dp转换为px
	 * @param context
	 * @param dp
	 * @return
	 * @throws
	 */
	public static int dpToPx(Context context, int dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int pxToDp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 首字母转大写
	 * 
	 * @param s
	 * @return
	 */
	public static String toUpperCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder())
					.append(Character.toUpperCase(s.charAt(0)))
					.append(s.substring(1)).toString();
	}

	/**
	 * 
	 * @param secMills
	 * @return
	 */
	public static String date2CNStr(long secMills) {
		String str = "";
		try {
			Date date = new Date(secMills);
			str = new SimpleDateFormat("yyyy-MM-dd").format(date);
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return str;
	}

	/**
	 * 
	 * @param secMills
	 * @return
	 */
	public static String date2CNStr(String secMills,String pattern) {
		String str = "";
		try {
			Date date = new Date(Long.parseLong(secMills));
			str = new SimpleDateFormat(pattern).format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 * 日期转换毫秒数
	 */
	public static long str2Long(String resource) {

		long l = 0;
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.parse(resource);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			l = calendar.getTimeInMillis();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;
	}

	/**
	 * 将毫秒数转换成天数
	 */
	public static int millis2Day(long secMills) {
		int days = 0;
		days = (int) (secMills / (1000 * 60 * 60 * 24));
		return days;
	}

	/**
	 * 检查网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}

		return false;
	}

	/**
	 * 判断程序权限
	 * 
	 * @param context
	 */
	public void hasContactPermission(Context context) {
		StringBuffer appNameAndPermissions = new StringBuffer();
		PackageManager pm = context.getPackageManager();
		List<ApplicationInfo> packages = pm
				.getInstalledApplications(PackageManager.GET_META_DATA);
		for (ApplicationInfo applicationInfo : packages) {
			try {
				PackageInfo packageInfo = pm.getPackageInfo(
						applicationInfo.packageName,
						PackageManager.GET_PERMISSIONS);
				appNameAndPermissions.append(packageInfo.packageName + "*:\n");
				// Get Permissions
				String[] requestedPermissions = packageInfo.requestedPermissions;
				if (requestedPermissions != null) {
					for (int i = 0; i < requestedPermissions.length; i++) {
						Log.d("test", requestedPermissions[i]);
						appNameAndPermissions.append(requestedPermissions[i]
								+ "\n");
					}
					appNameAndPermissions.append("\n");
				}
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取程序版本号
	 * 
	 * @return 当前应用的版本号
	 */
	public static String getAppVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取当前设备品牌
	 */
	public static String getDeviceBrand() {
		Build bd = new Build();
		String brand = bd.BRAND;
		return brand;
	}

	/**
	 * 获取当前设备型号
	 */
	public static String getDeviceModel() {
		Build bd = new Build();
		String model = bd.MODEL;
		return model;
	}

	/**
	 * 获取当前设备系统版本
	 * 
	 * @param context
	 * @return
	 */
	public static String getSDKVersion() {
		String model = Build.VERSION.SDK;
		return model;
	}

	/**
	 * 获取当前手机的设备id
	 * 
	 * @params Returns the unique device ID, for example, the IMEI for GSM and
	 *         the MEID or ESN for CDMA phones. Return null if device ID is not
	 *         available.
	 */
	public static String getDeviceID(Context ctx) {

		TelephonyManager tm = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = tm.getDeviceId();
		if (deviceId == null) {
			deviceId = "0000000000000";
		}
		return deviceId;
	}

	public static String removeTagFromText(String content) {
		Pattern p = null;
		Matcher m = null;
		String value = null;

		// 去掉<>标签
		p = Pattern.compile("(<[^>]*>)");
		m = p.matcher(content);
		String temp = content;
		while (m.find()) {
			value = m.group(0);
			temp = temp.replace(value, "");
		}

		// 去掉换行或回车符号
		p = Pattern.compile("(/r+|/n+)");
		m = p.matcher(temp);
		while (m.find()) {
			value = m.group(0);
			temp = temp.replace(value, " ");
		}
		return temp;
	}

	/**
	 * 获取程序版本号
	 * 
	 * @return 当前应用的版本号
	 */
	public static String getVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取手机的网络制式
	 */
	public static String getNetType(Context context) {

		String type = null; // 网络制式
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			int networkType = networkInfo.getType();
			if (networkType == ConnectivityManager.TYPE_WIFI) { // wifi
				type = "wifi";
			} else if (networkType == ConnectivityManager.TYPE_MOBILE) { // 手机网络
				try {
					int subType = networkInfo.getSubtype();
					String providersName = getMobileType(tm);
					if(providersName==null){
						return "";
					}
					// * NETWORK_TYPE_CDMA 网络类型为CDMA
					// * NETWORK_TYPE_EDGE 网络类型为EDGE
					// * NETWORK_TYPE_EVDO_0 网络类型为EVDO0
					// * NETWORK_TYPE_EVDO_A 网络类型为EVDOA
					// * NETWORK_TYPE_GPRS 网络类型为GPRS
					// * NETWORK_TYPE_HSDPA 网络类型为HSDPA
					// * NETWORK_TYPE_HSPA 网络类型为HSPA
					// * NETWORK_TYPE_HSUPA 网络类型为HSUPA
					// * NETWORK_TYPE_UMTS 网络类型为UMTS
					// 联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EDGE，电信的2G为CDMA，电信
					// 的3G为EVDO
					if (subType == TelephonyManager.NETWORK_TYPE_LTE) { // 4G
						type = "4G";
					} else {
						if (providersName.equals("电信")) {
							if (subType == TelephonyManager.NETWORK_TYPE_CDMA) { // 电信2G
								type = providersName + " 2G";
							} else if (subType == TelephonyManager.NETWORK_TYPE_EVDO_0
									|| subType == TelephonyManager.NETWORK_TYPE_EVDO_A
									|| subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
								// 电信3G
								type = providersName + " 3G";
							}
						} else if (providersName.equals("移动")) {

							if (subType == TelephonyManager.NETWORK_TYPE_GPRS
									|| subType == TelephonyManager.NETWORK_TYPE_EDGE) { // 移动2G

								type = providersName + " 2G";
							} else if (subType == TelephonyManager.NETWORK_TYPE_HSPA
									|| subType == TelephonyManager.NETWORK_TYPE_HSDPA) { // 移动3G

								type = providersName + " 3G";
							}
						} else if (providersName.equals("联通")) {

							if (subType == TelephonyManager.NETWORK_TYPE_GPRS
									|| subType == TelephonyManager.NETWORK_TYPE_EDGE) { // 联通2G

								type = providersName + " 2G";
							} else if (subType == TelephonyManager.NETWORK_TYPE_HSDPA
									|| subType == TelephonyManager.NETWORK_TYPE_UMTS) { // 联通3G
								type = providersName + " 3G";
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			// 没有网络连接
			type = "disconnected";
		}
     return type;
	}

	/***
	 * 获取手机网络制式 如：电信，联通，移动
	 */
	public static String getMobileType(TelephonyManager telephonyManager) {
		String providersName = null;
		/**
		 * Returns the unique subscriber ID, for example, the IMSI for a GSM
		 * phone. Return null if it is unavailable
		 */
		String subscriberId = telephonyManager.getSubscriberId();
		if (subscriberId == null) {

			return "unkown";
		}
		// 中国移动
		if (subscriberId.startsWith("46000")
				|| subscriberId.startsWith("46002")) {

			providersName = "移动";
		} else if (subscriberId.startsWith("46001")) { // 中国联通

			providersName = "联通";
		} else if (subscriberId.startsWith("46003")) { // 中国电信

			providersName = "电信";
		}
		return providersName;
	}

	/**
	 * 读取当前程序包名
	 * 
	 * @param context
	 * @return
	 */
	public static String getPackageName(Context context) {
		PackageInfo info;
		try {
			info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return info.packageName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 日期转换
	 * */

	public static String parseDate(long milliseconds) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String date = sdf.format(new Date(milliseconds));
		return date;
	}

	// 从资源文件获取数据
	public static String fromAssert(Context ctx, String fileName) {
		String result = null;
		AssetManager am = ctx.getAssets();
		InputStream inputStream = null;
		try {
			inputStream = am.open(fileName);
			int len = inputStream.available();
			byte[] buffer = new byte[len];
			inputStream.read(buffer);
			inputStream.close();
			result = EncodingUtils.getString(buffer, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	//android获取一个用于打开图片文件的intent
	  public static Intent getImageFileIntent(String param)
	  {
	    Intent intent = new Intent("android.intent.action.VIEW");
	    intent.addCategory("android.intent.category.DEFAULT");
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    Uri uri = Uri.fromFile(new File(param));
	    intent.setDataAndType(uri, "image/*");
	    return intent;
	  }
	  
	  /**
	   * 获取手机的根目录
	   */
	  public static String getCachePath(){
		  String path =null;
		  try{
		 boolean sdcardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		 if(sdcardExist){
			 String footerPath = Environment.getExternalStorageDirectory().getPath();
//			 path = footerPath +INI.LOC_PIC;
			 File file = new File(path);
			 if(!file.exists()){
				 file.mkdirs();
			 }
			 path = file.getAbsolutePath();
		 }
		  }catch(Exception e){
			  e.printStackTrace();
		  }
		  return path;
	  }
	  
	  public static  long ONE_DAY = 1*24*60*60*1000; //一天的毫秒数
	  public static  long TWO_DAY = 2*24*60*60*1000; //两天的毫秒数
	  public static  long SEV_DAY = 7*24*60*60*1000; //七天的毫秒数
	  /**
	   *聊天时间
	   */
	  public static String chatTime(long lastTime){
		  String result =null;
		  SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		  long currentTime = System.currentTimeMillis();
		  long distance = currentTime -lastTime;
		  if(distance<=ONE_DAY){
			  result = sdf.format(new Date(lastTime));
		  }else if(distance>ONE_DAY && distance<=TWO_DAY){
			  result = "昨天"+" "+sdf.format(new Date(lastTime));
		  }else if(distance>TWO_DAY && distance<=SEV_DAY){ //大于2天但小于7天
			  Calendar calendar = Calendar.getInstance();
			  calendar.setTimeInMillis(lastTime);
			  int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			 result = "星期" + DatePicker.getDayOfWeekCN(dayOfWeek)+ " "+sdf.format(new Date(lastTime));
		  }else { //大于7天
			  SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			  result = sdf2.format(new Date(lastTime));
		  }
		  return result;
	  }
	  
	  /**
	   * 1小时至一天的毫秒数
	   */
	  public static final long ONE_HOURE = 1*60*60*1000; 
	  public static final long TWO_HOURE = 2*60*60*1000;
	  public static final long THREE_HOURE = 3*60*60*1000;
	  public static final long FOUR_HOURE = 4*60*60*1000;
	  public static final long FIVE_HOURE = 5*60*60*1000;
	  public static final long SIX_HOURE = 6*60*60*1000;
	  public static final long SEVEN_HOURE = 7*60*60*1000;
	  public static final long EIGHT_HOURE = 8*60*60*1000;
	  public static final long NINE_HOURE = 9*60*60*1000;
	  public static final long TEN_HOURE = 10*60*60*1000;
	  public static final long ELEVEN_HOURE = 11*60*60*1000;
	  public static final long TWELVE_HOURE = 12*60*60*1000;
	  
	  /**
	   *最后触网时间
	   */
	  public static String lastTouchTime(long lastTime){
		  String result =null;
		  long currentTime = System.currentTimeMillis();
		  long distance = currentTime -lastTime;
		  if(distance<=ONE_HOURE){
			  SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			  result = sdf.format(new Date(lastTime));
		  }else if(distance>ONE_HOURE && distance<=TWO_HOURE){
			  result = "一小时前";
		  }else if(distance>TWO_HOURE && distance<=THREE_HOURE){ 
			  result = "二小时前";
		  }else if(distance>THREE_HOURE && distance<=FOUR_HOURE){ 
			  result = "三小时前";
		  }else if(distance>FOUR_HOURE && distance<=FIVE_HOURE){ 
			  result = "四小时前";
		  }else if(distance>FIVE_HOURE && distance<=SIX_HOURE){ 
			  result = "五小时前";
		  }else if(distance>SIX_HOURE && distance<=SEVEN_HOURE){ 
			  result = "六小时前";
		  }else if(distance>SEVEN_HOURE && distance<=EIGHT_HOURE){ 
			  result = "七小时前";
		  }else if(distance>EIGHT_HOURE && distance<=NINE_HOURE){ 
			  result = "八小时前";
		  }else if(distance>NINE_HOURE && distance<=TEN_HOURE){ 
			  result = "九小时前";
		  }else if(distance>TEN_HOURE && distance<=ELEVEN_HOURE){ 
			  result = "十小时前";
		  }else if(distance>ELEVEN_HOURE && distance<=TWELVE_HOURE){ 
			  result = "十一小时前";
		  }else if(distance>TWELVE_HOURE && distance<=ONE_DAY){
			  result = "半天前"; 
		  }else { //大于1天
			  result = "一天前";
		  }
		  return result;
	  }
}
