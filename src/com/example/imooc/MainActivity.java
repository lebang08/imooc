package com.example.imooc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URL;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

public class MainActivity extends Activity {

	private ListView mListView;
	private static String URL = "http://www.imooc.com/api/teacher?type=4&num=30";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mListView = (ListView) findViewById(R.id.lv_main);
		new NewsAsyncTask().execute(URL);
	}
	
	private List<NewsBean> getJsonData(String url) {
		List<NewsBean> newsBeanList = new ArrayList<>();  // 不同
		try{
			String jsonString = readStream(new URL(url).openStream());//不同
			JSONObject jsonobject;
			NewsBean newsBean;
			try {
				jsonobject = new JSONObject(jsonString);
				JSONArray jsonArray = jsonobject.getJSONArray("data");
				for(int i=0; i < jsonArray.length(); i++){
					jsonobject = jsonArray.getJSONObject(i);
					newsBean = new NewsBean();
					newsBean.newsIconUrl = jsonobject.getString("picSmall");
					newsBean.newsTitle = jsonobject.getString("name");
					newsBean.newsContent = jsonobject.getString("description");
					newsBeanList.add(newsBean);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return newsBeanList;
		}
	
	private String readStream(InputStream is){
		InputStreamReader isr;
		String result = "";
		try {
			String line = "";
			isr = new InputStreamReader(is,"utf-8");
			BufferedReader br = new BufferedReader(isr);
			while((line = br.readLine()) != null){
				result += line;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	class NewsAsyncTask extends AsyncTask<String,Void,List<NewsBean>>{

		@Override
		protected List<NewsBean> doInBackground(String... params) {
			return getJsonData(params[0]);
		}
		
		@Override
		protected void onPostExecute(List<NewsBean> newsBean) {
			super.onPostExecute(newsBean);
			NewsAdapter adapter = new NewsAdapter(MainActivity.this, newsBean);
			mListView.setAdapter(adapter);
		}
	}
}
