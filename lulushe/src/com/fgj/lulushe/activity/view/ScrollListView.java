/**
 * @author   fengguangjing
 * @version  2015 下午4:58:43
 * @description 
 */
package com.fgj.lulushe.activity.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author EX-FENGGUANGJING001
 * 
 * @category ScrollView与ListView嵌套使用冲突 解决：view的高度不显示问题。复写onMeasure方法，重新计算
 */
public class ScrollListView extends ListView {

	/**
	 * @param context
	 */
	public ScrollListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ScrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
