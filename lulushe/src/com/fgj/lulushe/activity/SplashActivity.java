package com.fgj.lulushe.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.fgj.lulushe.R;
import com.fgj.lulushe.viewpager.DepthPageTransformer;
import com.fgj.lulushe.viewpager.ViewPagerCompat;
import com.fgj.lulushe.viewpager.ViewPagerCompat.OnPageChangeListener;

/**
 * http://blog.csdn.net/lmj623565791/article/details/40411921
 *
 */
public class SplashActivity extends Activity implements OnPageChangeListener
{
	private ViewPagerCompat mViewPager;
	private int[] mImgIds = new int[] { R.drawable.guide_image1,
			R.drawable.guide_image2, R.drawable.guide_image3, R.drawable.guide_image4 };//, R.drawable.guide_image3
	private List<ImageView> mImageViews = new ArrayList<ImageView>();
	private boolean misScrolled = true;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);

		initData();

		mViewPager = (ViewPagerCompat) findViewById(R.id.id_viewpager);

		mViewPager.setPageTransformer(true, new DepthPageTransformer());
//		mViewPager.setPageTransformer(true, new RotateDownPageTransformer());
		mViewPager.setAdapter(new PagerAdapter()
		{
			@Override
			public Object instantiateItem(ViewGroup container, int position)
			{

				container.addView(mImageViews.get(position));
				return mImageViews.get(position);
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object)
			{

				container.removeView(mImageViews.get(position));
			}

			@Override
			public boolean isViewFromObject(View view, Object object)
			{
				return view == object;
			}

			@Override
			public int getCount()
			{
				return mImgIds.length;
			}
		});
		mViewPager.setOnPageChangeListener(this);

	}

	private void initData()
	{
		for (int imgId : mImgIds)
		{
			ImageView imageView = new ImageView(getApplicationContext());
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageView.setImageResource(imgId);
			mImageViews.add(imageView);
		}
	}
	
	@Override
	public void onPageScrollStateChanged(int state) {
		switch (state) {
		case ViewPager.SCROLL_STATE_DRAGGING:
			misScrolled = false;
			break;
		case ViewPager.SCROLL_STATE_SETTLING:
			misScrolled = true;
			break;
		case ViewPager.SCROLL_STATE_IDLE:
			if (mViewPager.getCurrentItem() == mViewPager.getAdapter().getCount() - 1 && !misScrolled) {
				startActivity(new Intent(this, MainActivity.class));
				finish();
			}
			misScrolled = true;
			break;
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int position) {
	}

}
