package com.fgj.lulushe.activity;

import java.util.ArrayList;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fgj.lulushe.LulusheAppcation;
import com.fgj.lulushe.R;
import com.fgj.lulushe.entity.Vodlist;
import com.fgj.lulushe.imageloader.ImageLoader;
import com.fgj.lulushe.sidesliplistview.PullToRefreshListView;
import com.fgj.lulushe.swipefinish.SildingFinishLayout;
import com.fgj.lulushe.swipefinish.SildingFinishLayout.OnSildingFinishListener;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class VodMediaActivity extends BaseActivity {
	private static final String TAG = "VodMediaActivity";
	private ArrayList<Vodlist> mVodlistList;
	private PullToRefreshListView mListView;
	private VodlistAdapter mAdapter;
	private ImageLoader mImageLoader;

//	private static class ViewHolder {
//		public TextView title;
//		public TextView summary;
//		public ImageView image;
//		public TextView postTime;
//		// public LinearLayout layout;
////		public ImageView img;
//	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getTitleBar().setTitle(getIntent().getStringExtra("pagetid"));
		setContentLayout(R.layout.activity_vod_media);
		LulusheAppcation.getAppcationCxt().addActivity(this);
		getTitleBar().leftBackListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		getTitleBar().setBackTextVisible(false);
		getTitleBar().scrollTopListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				long secondTime = System.currentTimeMillis();
				if (secondTime - scrollTopTime > BACK_DISTANCE) {
					scrollTopTime = secondTime;
				} else { // 两次按键小于2秒时,listview默认回到顶部
					mListView.smoothScrollToPosition(0);
				}
			}
		});

		preparedView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LulusheAppcation.getAppcationCxt().removeActivity(this);
	}

	@Override
	protected void preparedView() {
		super.preparedView();
		href = getIntent().getStringExtra("url");

		mListView = (PullToRefreshListView) findViewById(R.id.listview);
		SildingFinishLayout sfl = (SildingFinishLayout) findViewById(R.id.slide_parent_layout);
		sfl.setTouchView(mListView);
		sfl.setOnSildingFinishListener(new OnSildingFinishListener() {
			@Override
			public void onSildingFinish() {
				finish();
			}
		});

		mVodlistList = new ArrayList<Vodlist>();
		// final String href = "http://www.xixilu.us/vodlist/1_1.html";
		mImageLoader = new ImageLoader(this);
		mImageLoader.setRequiredSize(5 * (int) getResources().getDimension(
				R.dimen.litpic_width));
		mAdapter = new VodlistAdapter();
		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				// if(position > 0 && position <= mVodlistList.size()){
				// Intent intent = new Intent(getActivity(),
				// VodMediaActivity.class);
				// intent.putExtra("url", mVodlistList.get(position -
				// 1).getUrl());
				// intent.putExtra("pagetid", mVodlistList.get(position -
				// 1).getTitle());
				// startActivity(intent);
				// getActivity().overridePendingTransition(R.anim.base_slide_right_in,
				// R.anim.base_slide_remain);
				// }
			}
		});
		mListView.setAdapter(mAdapter);
		mListView
				.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
					public void onRefresh() {
						loadNewsList(href, 1, true);
					}

					public void onLoadingMore() {
						int pageIndex = mVodlistList.size() / 10 + 1;
						Log.i("pageIndex", "pageIndex = " + pageIndex);
						loadNewsList(href, 1, false);
					}
				});
		loadNewsList(href, 1, true);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.base_slide_right_out);
	}

	private void loadNewsList(final String href, final int page,
			final boolean isRefresh) {
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					ArrayList<Vodlist> articleList = (ArrayList<Vodlist>) msg.obj;
					if (isRefresh) {
						mVodlistList.clear(); // 下拉刷新之前先将数据清空
						mListView
								.onRefreshComplete(new Date().toLocaleString());
					}
					for (Vodlist article : articleList) {
						mVodlistList.add(article);
					}
					mAdapter.notifyDataSetChanged();
					if (articleList.size() < 100) {
						mListView.onLoadingMoreComplete(true);
					} else if (articleList.size() == 100) {
						mListView.onLoadingMoreComplete(false);
					}
				}
			}
		};

		new Thread() {
			public void run() {
				Message msg = new Message();
				ArrayList<Vodlist> articleList = new ArrayList<Vodlist>();
				try {
					articleList = parseVodlistList(href, page);
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				msg.what = 1;
				msg.obj = articleList;
				handler.sendMessage(msg);
			}
		}.start();
	}

	public ArrayList<Vodlist> parseVodlistList(String href, final int page) {
		ArrayList<Vodlist> articleList = new ArrayList<Vodlist>();
		try {
			href = _MakeURL(href, page);
			Log.i("url", "url = " + href);
			Document doc = Jsoup.connect(href).timeout(10000).get();
			Element masthead = doc.select("div.play-nr-l").first();
			Elements articleElements = masthead.select("div.nr-box");
			for (int i = 0; i < articleElements.size(); i++) {
				Vodlist article = new Vodlist();
				Element articleElement = articleElements.get(i);
				// ArrayList<String> imgUrl = new ArrayList<String>();
				try {
					Element boxlElement = articleElement
							.select("div.nr-box-dh").first();
					String title = boxlElement.text();
					article.setTitle(title);
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					Element summaryElement = articleElement.select("div.vmain")
							.first();
					try {
						Elements logoElement = summaryElement
								.select("div.vpic");
						// article.setImageUrl(logoElement.attr("src"));
						// imgUrl.add(logoElement.attr("src"));
						article.setImageUrl(logoElement.attr("src"));
					} catch (Exception e) {
						e.printStackTrace();
					}
					String summary = summaryElement.text();
					if (summary.length() > 2000)
						summary = summary.substring(0, 2000);
					article.setSummary(summary);
				} catch (Exception e) {
					e.printStackTrace();
				}

				articleList.add(article);
			}

			// 图片信息
			try {
				// ArrayList<String> imgUrl = new ArrayList<String>();
				Element picElement = doc.select("div.jianjie").first();
				// 图片
				Element imgElement = null;
				String imgsrc = "";

				int size = picElement.select("img").size();
				for (int j = 0; j < size; j++) {
					Vodlist article = new Vodlist();
					imgElement = picElement.select("img").get(j);
					if (imgElement != null) {
						imgsrc = imgElement.attr("src");
					}
					article.setImageUrl(imgsrc);
					articleList.add(article);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return articleList;
	}

	private static String _MakeURL(String p_url, int page) {
		// "http://www.xixilu.net/vodlist/1_1.html";
		// String vod = p_url.replace("http://www.xixilu.us/vodlist/",
		// "").replace(".html", "");
		// String params[] = vod.split("_");
		// String url =
		// "http://www.xixilu.us/vodlist/"+params[0]+"_"+page+".html?noscript";
		return p_url;
	}

	private class VodlistAdapter extends BaseAdapter {
		private int mLastAnimatedPosition;

		public int getCount() {
			return mVodlistList.size();
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
//			 ViewHolder viewHolder = null;
//			 if (convertView == null) {
			LayoutInflater layoutInflater = LayoutInflater
					.from(LulusheAppcation.getAppcationCxt());
//			 viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.item_vod_media_list,
					null);
			TextView title = (TextView) convertView.findViewById(R.id.title);
			TextView summary = (TextView) convertView
					.findViewById(R.id.summary);
			ImageView image = (ImageView) convertView
					.findViewById(R.id.image);
//			LinearLayout layout = (LinearLayout) convertView
//					.findViewById(R.id.layout);
//			 convertView.setTag(viewHolder);
//			 } else {
//			 viewHolder = (ViewHolder) convertView.getTag();
//			 }

			Vodlist article = mVodlistList.get(position);
			title.setText(article.getTitle());
			summary.setText(article.getSummary());
			final String url = article.getImageUrl();
			// for (final String url : article.getAllImage()) {
			if (url != null && !url.equals("")) {
//				ImageView image = new ImageView(VodMediaActivity.this);
//				image.setLayoutParams(new LayoutParams(
//						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//				image.setAdjustViewBounds(true);
				image.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent("android.intent.action.VIEW");
						intent.addCategory("android.intent.category.DEFAULT");
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						Uri uri = Uri.fromFile(mImageLoader.getCache(url));
						intent.setDataAndType(uri, "image/*");
						VodMediaActivity.this.startActivity(intent);
					}
				});
//				layout.addView(image);
				image.setVisibility(View.VISIBLE);
				mImageLoader.DisplayImage(url, image);
			}else{
				image.setVisibility(View.GONE);
			}
			// }
			return convertView;
		}
	}

}
