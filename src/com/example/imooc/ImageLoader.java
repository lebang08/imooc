package com.example.imooc;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

public class ImageLoader {
	
	private LruCache<String, Bitmap> mCaches;
	
	public ImageLoader(){
//		��ȡ�������ڴ�
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory/4;
		mCaches = new LruCache<String, Bitmap>(cacheSize){
			@SuppressLint("NewApi")
			@Override
			protected int sizeOf(String key, Bitmap value) {
//				ÿ�δ��뻺���ʱ����ã�ȷ�ϴ�С
				return value.getByteCount();
			}
		};
	}
//	���ӵ����棬set����
	public void setBitmapToCache(String url,Bitmap bitmap){
		if(getBitmapFromCache(url) == null){
			mCaches.put(url, bitmap);
		}
	}
//	�ӻ����л�ȡ���飬get����
	public Bitmap getBitmapFromCache(String url){
		return mCaches.get(url);
	}

//	�ڶ��ַ�����NewsAdapter ����
	public void showImageByAsyncTask(ImageView imageView, String url){
//		�ӻ�����ȡ����Ӧ��ͼƬ
		Bitmap bitmap = getBitmapFromCache(url);
//		���������û�У���ȥ����������
		if(bitmap == null){
			new NewsAsyncTask(imageView,url).execute(url);
		}else {
			imageView.setImageBitmap(bitmap);
		}
	}
	
	private class NewsAsyncTask extends AsyncTask<String,Void,Bitmap>{
		
		private ImageView mImageView;
		private String mUrl;
		
		public NewsAsyncTask(ImageView imageView,String url) {
			mImageView = imageView;
			mUrl = url;
		}
		
		@Override
		protected Bitmap doInBackground(String... params) {
			String url = params[0];
//			�������ȡͼƬ
			Bitmap bitmap = getBitmapFromURL(url);
			if(bitmap != null){
//				�����ڻ����ͼƬ���뵽������
				setBitmapToCache(url, bitmap);
			}
			return bitmap;
			
		}
		
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);
			if(mImageView.getTag().equals(mUrl)){
				mImageView.setImageBitmap(bitmap);
			}
	}

	public Bitmap getBitmapFromURL(String urlString){
		Bitmap bitmap;
		InputStream is = null;
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			is = new BufferedInputStream(connection.getInputStream());
			bitmap = BitmapFactory.decodeStream(is);
			connection.disconnect();
			return bitmap;
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
}
