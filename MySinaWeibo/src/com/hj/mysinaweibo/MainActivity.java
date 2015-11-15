package com.hj.mysinaweibo;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

import com.hj.mysinaweibo.commonutil.AndroidHelper;
import com.hj.mysinaweibo.commonutil.HttpApiHelper;
import com.hj.mysinaweibo.model.UserInfo;
import com.hj.mysinaweibo.sql.DataHelper;

public class MainActivity extends Activity {

	private DataHelper dbHelper;
	private List<UserInfo> userInfos;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		LinearLayout ll_main = (LinearLayout) findViewById(R.id.ll_main);
		AndroidHelper.autoBackground(this, ll_main, R.drawable.bg_w,
				R.drawable.bg_h);

		// 获取帐号列表
		dbHelper = new DataHelper(this);
		userInfos = dbHelper.getUserList(true);
		if (userInfos.isEmpty()) {// 如果为空说明第一次使用 跳到AuthorizeActivity页面进行OAuth认证
			Intent intent = new Intent(this, AuthorizeActivity.class);
			startActivity(intent);
		} else {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		}
		finish();
	}

	/**
	 * 根据授权时获得的用户id和授权码，完善用户名和头像等信息
	 * 
	 * @param mainActivity
	 * @param userInfos
	 */
	private void updateUserInfo(MainActivity mainActivity,
			List<UserInfo> userInfos) {
		DataHelper dbHelper = new DataHelper(this);
		String url = "https://api.weibo.com/2/users/show.json";
		for (UserInfo userInfo : userInfos) {
			if (Long.parseLong(userInfo.getExpiresTime()) > System
					.currentTimeMillis()) {
				ContentValues values = new ContentValues();
				values.put("access_token", userInfo.getAccessToken());
				values.put("uid", userInfo.getUserId());
				String str = HttpApiHelper.getApiData(url, values);
				Bitmap userIcon;
				String userName;
				try {
					JSONObject data = new JSONObject(str);
					String imgPath = data.getString("profile_image_url");
					userIcon = downloadImg(imgPath);
					userName = data.getString("screen_name");
					dbHelper.updateUserInfo(userName, userIcon,
							userInfo.getUserId());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}
		dbHelper.cloce();
	}

	private Bitmap downloadImg(String urlStr) {
		Bitmap bitmap = null;
		try {
			URL url = new URL(urlStr);
			InputStream is = url.openConnection().getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}

}
