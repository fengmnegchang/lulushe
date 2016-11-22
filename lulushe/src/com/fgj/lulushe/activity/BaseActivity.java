package com.fgj.lulushe.activity;

import com.fgj.lulushe.LulusheAppcation;
import com.fgj.lulushe.R;
import com.fgj.lulushe.activity.util.CommTool;
import com.fgj.lulushe.activity.view.TitleBar;

import android.app.Activity;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


/**
 * @author sunShine
 * @version 创建时间：2014-11-17 上午10:38:40
 * @description 所有activity都继承此类
 */

public class BaseActivity extends Activity {

	private LinearLayout linContainers;
	private View viewContent;
	private TitleBar titleBar;

	protected int scW, scH; // 屏幕宽度、高度
	private RelativeLayout group_base;
	public long scrollTopTime = 0; // 记录第一次点击的时间
	public static final int BACK_DISTANCE = 2000; // 时间间隔为两秒钟
	public String  href = "";
	public static final boolean DEBUG = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
		setContentView(R.layout.activity_base);

		linContainers = (LinearLayout) findViewById(R.id.rl_base_container);
		titleBar = (TitleBar) findViewById(R.id.tb_base);
		group_base = (RelativeLayout) findViewById(R.id.group_base);
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			int actionbarSize = CommTool.getStatusBarHeight(BaseActivity.this);
			group_base.setPadding(0,actionbarSize >0 ? actionbarSize:36 , 0, 0);
		}

		scW = CommTool.getScreenWidth(this);
		scH = CommTool.getScreenHeight(this);
		LulusheAppcation.getAppcationCxt().addActivity(this);
	}

	/**
	 * 
	 * @Title setContentLayout
	 * @Description 根据资源ID添加布局
	 * @params reId 布局的资源id
	 * @return void
	 * @throws
	 */
	public void setContentLayout(int resId) {

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		viewContent = inflater.inflate(resId, null);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		viewContent.setLayoutParams(layoutParams);
		viewContent.setBackgroundDrawable(null);
		if (null != linContainers) {
			linContainers.addView(viewContent);
		}
	}

	/***
	 * 设置内容区域
	 * 
	 * @param view
	 *            View对象
	 */
	public void setContentLayout(View view) {
		if (null != linContainers) {
			linContainers.addView(view);
		}
	}

	public View getViewContent() {

		return this.viewContent;
	}

	public TitleBar getTitleBar() {

		return this.titleBar;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LulusheAppcation.getAppcationCxt().removeActivity(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/**
	 * 网络请求
	 */
	protected void loadingData() {

	}

	/**
	 * 初始化组件
	 */
	protected void preparedView() {

	}
	 
}
