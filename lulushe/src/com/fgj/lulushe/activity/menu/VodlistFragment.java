package com.fgj.lulushe.activity.menu;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fgj.lulushe.LulusheAppcation;
import com.fgj.lulushe.R;
import com.fgj.lulushe.activity.VodMediaActivity;
import com.fgj.lulushe.activity.util.UrlUtils;
import com.fgj.lulushe.entity.Vodlist;
import com.fgj.lulushe.imageloader.ImageLoader;
import com.fgj.lulushe.sidesliplistview.PullToRefreshListView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 */
@SuppressLint("ValidFragment")
public class VodlistFragment extends Fragment {
	private static final String TAG = "VodlistFragment";
	private static final boolean DEBUG = true;

	private static ArrayList<Vodlist> mVodlistList;
	private static PullToRefreshListView mListView;
	private static VodlistAdapter mAdapter;
	private static ImageLoader mImageLoader;
	private String href = "";

	public VodlistFragment() {
		super();
	}

	public VodlistFragment(String href) {
		super();
		this.href = href;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_vodlist, container, false);
		prepareView(view);
		return view;
	}

	private void prepareView(View view) {
		mVodlistList = new ArrayList<Vodlist>();
		// final String href = "http://www.xixilu.us/vodlist/1_1.html";
		mImageLoader = new ImageLoader(getActivity());
		mImageLoader.setRequiredSize(5 * (int) getResources().getDimension(R.dimen.litpic_width));
		mAdapter = new VodlistAdapter();
		mListView = (PullToRefreshListView) view.findViewById(R.id.listview);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				if (position > 0 && position <= mVodlistList.size()) {
					Intent intent = new Intent(getActivity(), VodMediaActivity.class);
					intent.putExtra("url", mVodlistList.get(position - 1).getUrl());
					intent.putExtra("pagetid", mVodlistList.get(position - 1).getTitle());
					startActivity(intent);
					getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
				}
			}
		});
		mListView.setAdapter(mAdapter);
		mListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			public void onRefresh() {
				loadNewsList(href, 1, true);
			}

			public void onLoadingMore() {
				int pageIndex = mVodlistList.size() / 10 + 1;
				Log.i("pageIndex", "pageIndex = " + pageIndex);
				loadNewsList(href, pageIndex, false);
			}
		});
		loadNewsList(href, 1, true);

	}

	private void loadNewsList(final String href, final int page, final boolean isRefresh) {
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					ArrayList<Vodlist> articleList = (ArrayList<Vodlist>) msg.obj;
					if (isRefresh) {
						mVodlistList.clear(); // 下拉刷新之前先将数据清空
						mListView.onRefreshComplete(new Date().toLocaleString());
					}
					for (Vodlist article : articleList) {
						mVodlistList.add(article);
					}
					mAdapter.notifyDataSetChanged();
					if (articleList.size() < 10) {
						mListView.onLoadingMoreComplete(true);
					} else if (articleList.size() == 10) {
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
			Element masthead = doc.select("ul.mlist").first();
			Elements articleElements = masthead.select("li");
			for (int i = 0; i < articleElements.size(); i++) {
				Vodlist article = new Vodlist();
				Element articleElement = articleElements.get(i);
				/**
				 * <div class="pagelist"> <!-- 筛选开始 --> <div class="filter">
				 * <div class="title"><b>当前位置：<a href='/'>首页</a>&nbsp;&nbsp;<a
				 * href="/">千百撸</a> > 亚洲情色</b></div> </div> <div
				 * class="movielist"> <div class="pagepre">
				 * <p>
				 * 共找到<span>6206</span>个视频
				 * </p>
				 * 
				 * </div>
				 * <ul class="mlist">
				 * <li>
				 * <a href="/vod/11076.html"
				 * title="最新HEYZO 1353 他人妻味~妖艶美女诱惑~[江波りゅう]" target="_blank"
				 * class="p"><img
				 * src="//i3.1100lu.xyz/vod/2016-12-23/585c0b00a8f3a.jpg"
				 * alt="最新HEYZO 1353 他人妻味~妖艶美女诱惑~[江波りゅう]" /></a> <div
				 * class="info">
				 * <h2><a href="/vod/11076.html"
				 * title="最新HEYZO 1353 他人妻味~妖艶美女诱惑~[江波りゅう]"
				 * target="_blank">最新HEYZO 1353 他</a><em></em></h2>
				 * <p>
				 * <i>更新：2016-12-23</i>
				 * </p>
				 * <p>
				 * <i>类型：亚洲情色</i>
				 * </p>
				 * <p>
				 * <i>撸量：0</i>
				 * </p>
				 * <span><a href="/vod/11076.html#kan" target="_blank">观看</a><a
				 * href="/vod/11076.html#down"
				 * target="_blank">下载</a></span></div></li>
				 * 
				 * <li>
				 * <a href="/vod/11075.html"
				 * title="最新东京热 Tokyo Hot n1209 鬼逝~[田辺美咲]" target="_blank"
				 * class="p"><img
				 * src="//i3.1100lu.xyz/vod/2016-12-23/585c0ae9d57ff.jpg"
				 * alt="最新东京热 Tokyo Hot n1209 鬼逝~[田辺美咲]" /></a> <div
				 * class="info">
				 * <h2><a href="/vod/11075.html"
				 * title="最新东京热 Tokyo Hot n1209 鬼逝~[田辺美咲]" target="_blank">最新东京热
				 * Tokyo </a><em></em></h2>
				 * <p>
				 * <i>更新：2016-12-23</i>
				 * </p>
				 * <p>
				 * <i>类型：亚洲情色</i>
				 * </p>
				 * <p>
				 * <i>撸量：0</i>
				 * </p>
				 * <span><a href="/vod/11075.html#kan" target="_blank">观看</a><a
				 * href="/vod/11075.html#down"
				 * target="_blank">下载</a></span></div></li>
				 */
				try {
					Element boxlElement = articleElement.select("li").first();
					Element aElement = boxlElement.select("a").first();
					String url = UrlUtils.LULUSHE_URL + aElement.attr("href");
					String title = aElement.attr("title");
					article.setTitle(title);
					article.setUrl(url);

					try {
						Element imgElement = null;
						if (boxlElement.select("img").size() != 0) {
							imgElement = boxlElement.select("img").first();
						}
						String imgsrc = "";
						if (imgElement != null) {
							imgsrc = imgElement.attr("src");
						}
						article.setImageUrl("http:" + imgsrc);
					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					Element summaryElement = articleElement.select("div.info").first();
					String summary = summaryElement.text();
					if (summary.length() > 2000)
						summary = summary.substring(0, 2000);
					article.setSummary(summary);
				} catch (Exception e) {
					e.printStackTrace();
				}
				articleList.add(article);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return articleList;
	}

	private static String _MakeURL(String p_url, int page) {
		// "http://www.xixilu.net/vodlist/1_1.html";
		String vod = p_url.replace(UrlUtils.LULUSHE_URL + "/list/", "").replace(".html", "");
		String params[] = vod.split("_");
		// String url =
		// UrlUtils.LULUSHE_URL+"/list/"+params[0]+"_"+page+".html?noscript";
		String url = UrlUtils.LULUSHE_URL + "/list/" + params[0] + ".html?noscript";
		return url;
	}

	private static class VodlistAdapter extends BaseAdapter {
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

			LayoutInflater layoutInflater = LayoutInflater.from(LulusheAppcation.getAppcationCxt());
			View view = layoutInflater.inflate(R.layout.item_vodlist_list, null);
			TextView title = (TextView) view.findViewById(R.id.title);
			TextView summary = (TextView) view.findViewById(R.id.summary);
			ImageView image = (ImageView) view.findViewById(R.id.img);
			TextView postTime = (TextView) view.findViewById(R.id.postTime);

			Vodlist article = mVodlistList.get(position);
			title.setText(article.getTitle());
			summary.setText(article.getSummary());
			postTime.setText(article.getPostTime());
			if (!article.getImageUrl().equals("")) {
				mImageLoader.DisplayImage(article.getImageUrl(), image);
			} else {
				image.setVisibility(View.GONE);
			}
			return view;
		}
	}

}
