package com.fgj.lulushe;

import java.util.ArrayList;

import android.app.Application;

import com.fgj.lulushe.activity.BaseActivity;


public class LulusheAppcation extends Application {

	private static LulusheAppcation appCxt; // appcation 上下文
	private ArrayList<BaseActivity> activityLists = new ArrayList<BaseActivity>();
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		appCxt = this;
	}

	 
	/**
	 * 
	 * @Title: getAppcationCxt
	 * @Description: 获取整个app的对象
	 * @return
	 * @throws
	 */
	public static synchronized LulusheAppcation getAppcationCxt() {
		if (appCxt == null) {
			appCxt = new LulusheAppcation();
		}
		return appCxt;
	}

	/**
	 * 
	 * @Title: addActivity
	 * @Description: 向集合中添加activity
	 * @return void
	 * @throws
	 */
	public void addActivity(BaseActivity activity) {
		activityLists.add(activity);
	}

	/**
	 * 
	 * @Title removeActivity
	 * @Description 从集合中移除activity
	 * @params BaseActivity activity
	 * @return void
	 * @throws
	 */
	public void removeActivity(BaseActivity activity) {
		activityLists.remove(activity);
	}

 
	/**
	 * 
	 * @Title exitAppcation
	 * @Description 退出程序
	 * @params
	 * @return void
	 * @throws
	 */
	public void exitAppcation() {
		for (BaseActivity activity : activityLists) {
			activity.finish(); // 销毁集合中所有activity
		}

		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
}
