package com.fgj.lulushe.sidesliplistview;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;

/**
 * @author sunShine
 * @version 创建时间：2014-11-21 下午3:51:16
 * @description
 */

public class SlideListView extends ListView {

	public static final String TAG = "SlideListView";

	private SlideView mFocusedSlideView;

	/** 是否响应滑动 */
	private boolean isSliding;
	/** 手指按下时的x坐标 */
	private int xDown;
	/** 手指按下时的y坐标 */
	private int yDown;
	/** 手指移动时的x坐标 */
	private int xMove;
	/** 手指移动时的y坐标 */
	private int yMove;
	/** 用户滑动的最小距离 */
	private int touchSlop;

	public SlideListView(Context context) {
		super(context);
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	public SlideListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	public SlideListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	public void shrinkListItem(int position) {
		View item = getChildAt(position);

		if (item != null) {
			try {
				((SlideView) item).shrink();
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			xDown = x;
			yDown = y;
			break;
		case MotionEvent.ACTION_MOVE:
			xMove = x;
			yMove = y;
			int dx = xMove - xDown;
			int dy = yMove - yDown;
			/** 判断是否是从右到左的滑动 */
			if (xMove < xDown && Math.abs(dx) > touchSlop
					&& Math.abs(dy) < touchSlop * 2) {
				isSliding = true;
			}
			/** 判断是否是从左到右的滑动 */
			if (xMove > xDown && Math.abs(dx) > touchSlop
					&& Math.abs(dy) < touchSlop) {
				isSliding = true;
			}
			break;

		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isSliding) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:
				int x = (int) event.getX();
				int y = (int) event.getY();
				int position = pointToPosition(x, y);
				if (position != INVALID_POSITION) {
					Object o = getItemAtPosition(position);
					if (o instanceof SlideBean) {
						SlideBean data = (SlideBean) getItemAtPosition(position);
						mFocusedSlideView = data.slideView;
					}
					// MessageItem data = (MessageItem)
					// getItemAtPosition(position);
					// mFocusedSlideView = data.slideView;
				}
				break;
			case MotionEvent.ACTION_UP:
				isSliding = false;
				break;
			}
			if (mFocusedSlideView != null) {
				mFocusedSlideView.onRequireTouchEvent(event);
			}
			// 相应滑动期间屏幕itemClick事件，避免发生冲突
			return true;
		}
		return super.onTouchEvent(event);
	}

}
