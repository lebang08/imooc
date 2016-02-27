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
//		获取最大可用内存
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory/4;
		mCaches = new LruCache<String, Bitmap>(cacheSize){
			@SuppressLint("NewApi")
			@Override
			protected int sizeOf(String key, Bitmap value) {
//				每次存入缓存的时候调用，确认大小
				return value.getByteCount();
			}
		};
	}
//	增加到缓存，set方法
	public void setBitmapToCache(String url,Bitmap bitmap){
		if(getBitmapFromCache(url) == null){
			mCaches.put(url, bitmap);
		}
	}
//	从缓存中获取数组，get方法
	public Bitmap getBitmapFromCache(String url){
		return mCaches.get(url);
	}

//	第二种方法供NewsAdapter 调用
	public void showImageByAsyncTask(ImageView imageView, String url){
//		从缓存中取出相应的图片
		Bitmap bitmap = getBitmapFromCache(url);
//		如果缓存中没有，就去网络中下载
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
//			从网络获取图片
			Bitmap bitmap = getBitmapFromURL(url);
			if(bitmap != null){
//				将不在缓存的图片加入到缓存中
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
