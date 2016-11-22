package com.fgj.lulushe.wheelpicker;

import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * TimePicker
 */
public class TimePicker extends LinearLayout{
	
	private Calendar calendar = Calendar.getInstance(); //日历类
	private boolean isHourOfDay = true; //24小时制?  默认 是 （现在都是24小时制的 那个12小时制没做呢）
	private WheelView hours, mins; //Wheel picker
	private OnChangeListener onChangeListener; //onChangeListener
	private int mWidth = 0;
	private int mHeight = 0;
	private int screenWidth = 0;
	private int screenHeight = 0;
	
	//Constructors
	public TimePicker(Context context) {
		super(context);
		init(context);
	}
	
	public TimePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * 初始化组件
	 * @param context
	 */
	private void init(Context context){
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
	
		mWidth = screenWidth/10;
		
		hours = new WheelView(context);
		LayoutParams lparams_hours = new LayoutParams(mWidth,LayoutParams.WRAP_CONTENT);
		lparams_hours.setMargins(20, 0, 20, 0);
		hours.setLayoutParams(lparams_hours);
		hours.setAdapter(new NumericWheelAdapter(0, 23,"%02d"));
		hours.setVisibleItems(3);
		hours.addChangingListener(onHoursChangedListener);		
		addView(hours);		
		
		mins = new WheelView(context);
		mins.setLayoutParams(new LayoutParams(mWidth,LayoutParams.WRAP_CONTENT));
		mins.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		mins.setVisibleItems(3);
		mins.setCyclic(true);
		mins.addChangingListener(onMinsChangedListener);		
		addView(mins);			
	}
	
	
	
	//listeners
	private OnWheelChangedListener onHoursChangedListener = new OnWheelChangedListener(){
		@Override
		public void onChanged(WheelView hours, int oldValue, int newValue) {
			calendar.set(Calendar.HOUR_OF_DAY, newValue);
			onChangeListener.onChange(getHourOfDay(), getMinute());
		}
	};
	private OnWheelChangedListener onMinsChangedListener = new OnWheelChangedListener(){
		@Override
		public void onChanged(WheelView mins, int oldValue, int newValue) {
			calendar.set(Calendar.MINUTE, newValue);
			onChangeListener.onChange(getHourOfDay(), getMinute());
		}
	};
	
	/**
	 * 定义了监听时间改变的监听器借口
	 * @author Wang_Yuliang
	 *
	 */
	public interface OnChangeListener {
		void onChange(int hour, int munite);
	}
	
	/**
	 * 设置监听器的方法
	 * @param onChangeListener
	 */
	public void setOnChangeListener(OnChangeListener onChangeListener){
		this.onChangeListener = onChangeListener;
	}
	
	/**
	 * 设置小时
	 * @param hour
	 */
	public void setHourOfDay(int hour){
		hours.setCurrentItem(hour);
	}
	
	/**
	 * 获得24小时制小时
	 * @return
	 */
	public int getHourOfDay(){
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * 设置分钟
	 */
	public void setMinute(int minute){
		mins.setCurrentItem(minute);
	}
	
	/**
	 * 获得分钟
	 * @return
	 */
	public int getMinute(){
		return calendar.get(Calendar.MINUTE);
	}
		
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//默认设置为系统时间
		setHourOfDay(getHourOfDay());
		setMinute(getMinute());
	}
}
