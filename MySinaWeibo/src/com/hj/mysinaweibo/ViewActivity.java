package com.hj.mysinaweibo;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.hj.mysinaweibo.commonutil.AsyncImageLoader;
import com.hj.mysinaweibo.commonutil.AsyncImageLoader.ImageCallBack;
import com.hj.mysinaweibo.commonutil.ConfigHelper;
import com.hj.mysinaweibo.commonutil.HttpApiHelper;
import com.hj.mysinaweibo.model.UserInfo;

public class ViewActivity extends Activity {

	private String weiboId;
	private ImageView iv_user_icon;
	private TextView tv_user_name;
	private TextView tv_text;
	private ImageView iv_pic;
	private UserInfo userInfo;
	private String weiboStr;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
			try {
				JSONObject root = new JSONObject(weiboStr);
				String text = root.getString("text");
				JSONObject userObject = root.getJSONObject("user");
				String screenName = userObject.getString("screen_name");
				tv_user_name.setText(screenName);
				tv_text.setText(text);
				String profile_image_url = userObject
						.getString("profile_image_url");
				Drawable cacheImage1 = asyncImageLoader.loadDrawable(
						profile_image_url, iv_user_icon, new ImageCallBack() {

							@Override
							public void imageLoaded(Drawable imageDrawable,
									ImageView imageView) {
								imageView.setImageDrawable(imageDrawable);
							}
						});
				if (cacheImage1 != null) {
					iv_user_icon.setImageDrawable(userInfo.getUserIcon());
				}

				String bmiddle_pic = root.getString("bmiddle_pic");
				String original_pic = root.getString("original_pic");

				if (!bmiddle_pic.isEmpty()) {
					Drawable cacheImage2 = asyncImageLoader.loadDrawable(
							original_pic, iv_pic, new ImageCallBack() {

								@Override
								public void imageLoaded(Drawable imageDrawable,
										ImageView imageView) {
									imageView.setImageDrawable(imageDrawable);
								}
							});
					if (cacheImage2 != null) {
						iv_pic.setImageDrawable(cacheImage2);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view);

		iv_user_icon = (ImageView) findViewById(R.id.iv_user_icon);
		iv_pic = (ImageView) findViewById(R.id.iv_pic);
		tv_user_name = (TextView) findViewById(R.id.tv_user_name);
		tv_text = (TextView) findViewById(R.id.tv_text);
		System.out.println(iv_pic.getWidth() + " 长度 " + iv_pic.getHeight());
		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				if (bundle.containsKey("weiboId")) {
					weiboId = bundle.getString("weiboId");
					System.out.println(weiboId);
					view(weiboId);
				}
			}
		}
	}

	private void view(String id) {
		userInfo = ConfigHelper.currentUser;
		final ContentValues values = new ContentValues();
		values.put("access_token", userInfo.getAccessToken());
		values.put("id", weiboId);
		new Thread() {
			public void run() {
				weiboStr = HttpApiHelper.getApiData(
						"https://api.weibo.com/2/statuses/show.json", values);
				handler.sendEmptyMessage(0);
			};
		}.start();

	}
}
