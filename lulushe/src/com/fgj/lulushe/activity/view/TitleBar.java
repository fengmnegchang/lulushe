package com.fgj.lulushe.activity.view;

import com.fgj.lulushe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * @author sunShine
 * @version 创建时间：2014-11-17 上午10:57:30
 * @description 共有的头部view
 */

public class TitleBar extends RelativeLayout {
	private LayoutInflater layoutInflater;
	private RelativeLayout msgLayout;// 左侧消息布局
	private TextView msgNums;// 消息条数
	private LinearLayout linBack;// 返回布局
	private TextView ivBackTxt;// 返回文字描述
	private TextView tvTitle; // 头部名称
	private LinearLayout selectLayout;// 右侧筛选布局
	private ImageView setting;// 设置
	private RelativeLayout parent_layout;
	
	public TitleBar(Context context) {
		super(context);
		initialization(context);
	}

	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialization(context);
	}

	public TitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialization(context);
	}

	/**
	 * 
	 * @Title 初始化view
	 * @Description
	 * @params
	 * @return
	 * @throws
	 */
	private void initialization(Context ctx) {
		layoutInflater = LayoutInflater.from(ctx);
		View titleBarView = layoutInflater.inflate(R.layout.title_bar, this,
				true);
		linBack = (LinearLayout) titleBarView.findViewById(R.id.LinBack);
		tvTitle = (TextView) titleBarView.findViewById(R.id.titleName);

		msgLayout = (RelativeLayout) titleBarView.findViewById(R.id.msg_layout);
		msgNums = (TextView) titleBarView.findViewById(R.id.msg_nums);
		ivBackTxt = (TextView) titleBarView.findViewById(R.id.ivBackTxt);
		selectLayout = (LinearLayout) titleBarView
				.findViewById(R.id.select_layout);
		setting = (ImageView) titleBarView.findViewById(R.id.setting);
		parent_layout = (RelativeLayout) titleBarView.findViewById(R.id.parent_layout); 

	}

	/**
	 * 
	 * @Title leftBackListener
	 * @Description 返回
	 * @params OnClickListener onclickListener
	 * @return void
	 * @throws
	 */
	public void leftBackListener(OnClickListener onclickListener) {
		linBack.setVisibility(View.VISIBLE);
		linBack.setOnClickListener(onclickListener);
	}

	/**
	 * 
	 * @Title rightSettingListener
	 * @Description 设置
	 * @params OnClickListener onclickListener
	 * @return void
	 * @throws
	 */
	public void rightSettingListener(OnClickListener onclickListener) {
		setting.setVisibility(View.VISIBLE);
		setting.setOnClickListener(onclickListener);
	}

	/**
	 * 
	 * @Title rightSelectListener
	 * @Description 右侧筛选
	 * @params OnClickListener onclickListener
	 * @return void
	 * @throws
	 */
	public void rightSelectListener(OnClickListener onclickListener) {
		selectLayout.setVisibility(View.VISIBLE);
		selectLayout.setOnClickListener(onclickListener);
	}

	/**
	 * 
	 * @Title leftMsgListener
	 * @Description 左侧消息
	 * @params OnClickListener onclickListener
	 * @return void
	 * @throws
	 */
	public void leftMsgListener(OnClickListener onclickListener) {
		msgLayout.setVisibility(View.VISIBLE);
		msgLayout.setOnClickListener(onclickListener);
	}

	/**
	 * 
	 * @Title
	 * @Description
	 * @params
	 * @return
	 * @throws
	 */
	public TextView getTitleText() {

		return this.tvTitle;
	}

	/**
	 * 设置title
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	/**
	 * 设置消息
	 * 
	 * @param msgNums
	 */
	public void setMsgNums(String msgNums) {
//		if(Integer.parseInt(msgNums) == 0){
//			this.msgNums.setVisibility(View.GONE);
//		}else{
//			this.msgNums.setVisibility(View.VISIBLE);
//			this.msgNums.setText(msgNums);	
//		}
		this.msgNums.setText(msgNums);	
	}

	/**
	 * 是否显示消息信息
	 * 
	 * @param visible
	 */

	public void setMsgVisible(boolean visible) {
		if (visible) {
			msgLayout.setVisibility(View.VISIBLE);
		} else {
			msgLayout.setVisibility(View.GONE);
		}
	}

	/**
	 * 返回布局是否显示文字
	 * 
	 * @param visible
	 */
	public void setBackTextVisible(boolean visible) {
		if (visible) {
			ivBackTxt.setVisibility(View.VISIBLE);
		} else {
			ivBackTxt.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 右侧布局是否显示
	 * 
	 * @param visible
	 */
	public void setSelectVisible(boolean visible) {
		if (visible) {
			selectLayout.setVisibility(View.VISIBLE);
		} else {
			selectLayout.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 
	 * @Title scrollTopListener
	 * @Description  双击顶部，listview默认显示第一条数据
	 * @params OnClickListener onclickListener
	 * @return void
	 * @throws
	 */
	public void scrollTopListener(OnClickListener onclickListener) {
		parent_layout.setOnClickListener(onclickListener);
	}

}
