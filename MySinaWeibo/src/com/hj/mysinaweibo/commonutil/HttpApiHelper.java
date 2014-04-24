package com.hj.mysinaweibo.commonutil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.ContentValues;

public class HttpApiHelper {
	public static String postApiData(String path,ContentValues values){
		try {
			// 1.创建一个浏览器
			HttpClient httpClient = new DefaultHttpClient();
			// 2.准备一个连接
			HttpPost httpPost = new HttpPost(path);
			Iterator iterator = values.valueSet().iterator();
			// 要向服务器提交的数据实体
			List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				parameters.add(new BasicNameValuePair((String)entry.getKey(), (String)entry.getValue()));
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "UTF-8");
			httpPost.setEntity(entity);
			// 3.敲回车
			HttpResponse response = httpClient.execute(httpPost);
			int code = response.getStatusLine().getStatusCode();
			System.out.println("http返回码"+code);
			if(code==200){
				InputStream is = response.getEntity().getContent();
				Reader reader = new BufferedReader(new InputStreamReader(is));
				char[] buf = new char[1024];
				int len = 0;
				StringBuilder builder = new StringBuilder();
				while((len=reader.read(buf))!=-1){
					builder.append(buf, 0, len);
				}
				return builder.toString();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getApiData(String path,ContentValues values){
		try {
			HttpClient httpClient = new DefaultHttpClient();
			Iterator iterator = values.valueSet().iterator();
			StringBuilder urlsb = new StringBuilder(path);
			int count=0;
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				if(count==0){
					urlsb.append("?");
				}else{
					urlsb.append("&");
				}
				urlsb.append(entry.getKey()+"=");
				urlsb.append(entry.getValue());
				count++;
			}
			String url = urlsb.toString();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = httpClient.execute(httpGet);
			int code = response.getStatusLine().getStatusCode();
			System.out.println("http返回码"+code);
			if(code==200){
				InputStream is = response.getEntity().getContent();
				Reader reader = new BufferedReader(new InputStreamReader(is));
				char[] buf = new char[1024];
				int len = 0;
				StringBuilder builder = new StringBuilder();
				while((len=reader.read(buf))!=-1){
					builder.append(buf, 0, len);
				}
				return builder.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
