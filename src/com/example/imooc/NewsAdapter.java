package com.example.imooc;

import java.net.URL;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsAdapter extends BaseAdapter implements OnScrollListener{

	private List<NewsBean> mList;
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;
	private int mStart,mEnd;
	public static String[] URLS;
	
	public NewsAdapter(Context context, List<NewsBean> data) {
		mList = data;
		mInflater = LayoutInflater.from(context);
		mImageLoader = new ImageLoader(null);
		URLS = new String[data.size()];
		for(int i = 0 ; i < data.size(); i++){
			URLS[i] = data.get(i).newsIconUrl;
		}
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_layout, null);
			viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
			viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.ivIcon.setImageResource(R.drawable.ic_launcher);
		String url = mList.get(position).newsIconUrl;
		viewHolder.ivIcon.setTag(url);
//		new ImageLoader().showImageByThread(viewHolder.ivIcon, url);
		mImageLoader.showImageByAsyncTask(viewHolder.ivIcon, url);
		viewHolder.tvTitle.setText(mList.get(position).newsTitle);
		viewHolder.tvContent.setText(mList.get(position).newsContent);
		return convertView;
	}

	class ViewHolder{
		public TextView tvTitle, tvContent;
		public ImageView ivIcon;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == SCROLL_STATE_IDLE){
//			加载可见项
			mImageLoader.loadImage(mStart, mEnd);
		}else{
//			停止任务
			mImageLoader.cancelAllTasks();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		mStart = firstVisibleItem;
		mEnd = firstVisibleItem + visibleItemCount;
	}
	
}
