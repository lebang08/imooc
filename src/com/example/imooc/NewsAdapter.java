package com.example.imooc;

import java.net.URL;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsAdapter extends BaseAdapter{

	private List<NewsBean> mList;
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;
	
	public NewsAdapter(Context context, List<NewsBean> data) {
		mList = data;
		mInflater = LayoutInflater.from(context);
		mImageLoader = new ImageLoader();
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
//		new BitmapLoader().showImageByThread(viewHolder.ivIcon, url);
		mImageLoader.showImageByAsyncTask(viewHolder.ivIcon, url);
		viewHolder.tvTitle.setText(mList.get(position).newsTitle);
		viewHolder.tvContent.setText(mList.get(position).newsContent);
		return convertView;
	}

	class ViewHolder{
		public TextView tvTitle, tvContent;
		public ImageView ivIcon;
	}
}
