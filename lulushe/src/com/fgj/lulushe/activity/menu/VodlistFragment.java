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
	private String  href = "";
	
	private static class ViewHolder {
		public TextView title;
		public TextView summary;
		public ImageView image;
		public TextView postTime; 
	}
	
    public VodlistFragment() {
		super();
	}
    
    public VodlistFragment(String href){
    	super();
    	this.href = href;
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view  = inflater.inflate(R.layout.fragment_vodlist, container, false);
		prepareView(view);
        return view;
    }

	private void prepareView(View view) {
		mVodlistList = new ArrayList<Vodlist>();
//		final String  href = "http://www.xixilu.us/vodlist/1_1.html";
		mImageLoader = new ImageLoader(getActivity());
		mImageLoader.setRequiredSize(5 * (int)getResources().getDimension(R.dimen.litpic_width));
		mAdapter = new VodlistAdapter();
		mListView = (PullToRefreshListView) view.findViewById(R.id.listview);
		mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
               if(position > 0 && position <= mVodlistList.size()){
                   Intent intent  = new Intent(getActivity(), VodMediaActivity.class);
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
				Log.i("pageIndex","pageIndex = " + pageIndex);
				loadNewsList(href, pageIndex, false);
			}
		});
		loadNewsList(href, 1, true);
		
	}
	private void loadNewsList(final String href ,final int page, final boolean isRefresh) {
	    final  Handler handler = new Handler(){
			public void handleMessage(Message msg) {		
				if (msg.what == 1) {
					ArrayList<Vodlist> articleList = (ArrayList<Vodlist>)msg.obj;
					if (isRefresh) {
						mVodlistList.clear();	//下拉刷新之前先将数据清空
						mListView.onRefreshComplete (new Date().toLocaleString());
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
	
	public ArrayList<Vodlist>  parseVodlistList(String href, final int page){
		ArrayList<Vodlist> articleList = new ArrayList<Vodlist>();
		try {
			href = _MakeURL(href, page);
			Log.i("url","url = " + href);
		    Document doc = Jsoup.connect(href).timeout(10000).get(); 
		    Element masthead = doc.select("div.zy-box").first();
		    Elements articleElements =  masthead.select("div.list-pianyuan-box");		
		    for(int i = 0; i < articleElements.size(); i++) {
		    	Vodlist article = new Vodlist();
			    Element articleElement = articleElements.get(i);
			    
			    try {
			    	Element boxlElement = articleElement.select("div.list-pianyuan-box-l").first();
			    	Element aElement = boxlElement.select("a").first();
			    	String url = "http://www.xixilu.us" + aElement.attr("href"); 
				    String title = aElement.attr("title");
				    article.setTitle(title);
				    article.setUrl(url);
				    
				    try {
				    	Element imgElement = null;
					    if(boxlElement.select("img").size() != 0){
					       imgElement = boxlElement.select("img").first();
					    }
					    String imgsrc = "";
					    if(imgElement != null){
					    	imgsrc  = imgElement.attr("src");
					    }
					    article.setImageUrl(imgsrc);
					} catch (Exception e) {
						e.printStackTrace();
					}
				    
				} catch (Exception e) {
					e.printStackTrace();
				}
			    
			    try {
			    	Element summaryElement = articleElement.select("div.list-pianyuan-box-r").first();
			    	String summary = summaryElement.text();
				    if(summary.length() > 2000)
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
		//"http://www.xixilu.net/vodlist/1_1.html";
		String vod = p_url.replace("http://www.xixilu.us/vodlist/", "").replace(".html", "");
		String params[] = vod.split("_");
		String url = "http://www.xixilu.us/vodlist/"+params[0]+"_"+page+".html?noscript";
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
    		ViewHolder viewHolder = null;
    		if (convertView == null) {
    			LayoutInflater layoutInflater = LayoutInflater.from(LulusheAppcation.getAppcationCxt());
    			viewHolder = new ViewHolder();
				convertView = layoutInflater.inflate(R.layout.item_vodlist_list, null);
	 			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
    			viewHolder.summary = (TextView) convertView.findViewById(R.id.summary);
    			viewHolder.image = (ImageView) convertView.findViewById(R.id.img);
    			viewHolder.postTime = (TextView) convertView.findViewById(R.id.postTime);
    			convertView.setTag(viewHolder);
    		} else {
    			viewHolder = (ViewHolder) convertView.getTag();
    		}
    		Vodlist article = mVodlistList.get(position);
 			viewHolder.title.setText(article.getTitle());
 			viewHolder.summary.setText(article.getSummary());
 			viewHolder.postTime.setText(article.getPostTime());
 			if(!article.getImageUrl().equals("")){
 				mImageLoader.DisplayImage(article.getImageUrl(), viewHolder.image);
 			}else{
 				viewHolder.image.setVisibility(View.GONE);
 			}
    		return convertView;
    	}
    }	

}