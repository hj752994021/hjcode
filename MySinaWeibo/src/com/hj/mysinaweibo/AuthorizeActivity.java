package com.hj.mysinaweibo;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hj.mysinaweibo.commonutil.AndroidHelper;
import com.hj.mysinaweibo.commonutil.HttpApiHelper;
import com.hj.mysinaweibo.model.UserInfo;
import com.hj.mysinaweibo.oauth.Constants;
import com.hj.mysinaweibo.sql.DataHelper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.utils.LogUtil;

public class AuthorizeActivity extends Activity {

	/**
	 * WeiboSDKDemo 程序的 APP_SECRET。 请注意：请务必妥善保管好自己的
	 * APP_SECRET，不要直接暴露在程序中，此处仅作为一个DEMO来演示。
	 */
	private static final String WEIBO_DEMO_APP_SECRET = "3a3a45d91ce2dd016fd10befbe836166";

	/** 通过 code 获取 Token 的 URL */
	private static final String OAUTH2_ACCESS_TOKEN_URL = "https://open.weibo.cn/oauth2/access_token";

	protected static final String TAG = "AuthorizeActivity";
	/** 微博 Web 授权接口类，提供登陆等功能 */
	private WeiboAuth weiboAuth;
	/** 获取到的 Code */
	private String auth_code;
	/** 获取到的 Token */
	private Oauth2AccessToken accessToken;

	private Dialog dialog;
	private DataHelper dataHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authorize);
		LinearLayout ll_authorize = (LinearLayout) findViewById(R.id.ll_authorize);
		AndroidHelper.autoBackground(AuthorizeActivity.this, ll_authorize,
				R.drawable.bg_w, R.drawable.bg_h);
		dataHelper = new DataHelper(this);
		weiboAuth = new WeiboAuth(this, Constants.APP_KEY,
				Constants.REDIRECT_URL, Constants.SCOPE);
		View diaView = View.inflate(this, R.layout.dialog, null);
		dialog = new Dialog(this, R.style.dialog);
		dialog.setContentView(diaView);
		dialog.show();
		ImageButton btn_start = (ImageButton) diaView
				.findViewById(R.id.btn_start);
		ImageButton btn_cancel = (ImageButton) diaView
				.findViewById(R.id.btn_cancel);
		btn_start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//点击开始，进入授权页面
				weiboAuth.authorize(new AuthListener(),
						WeiboAuth.OBTAIN_AUTH_CODE);
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		//按下返回键时，，把activity也结束掉
		dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				finish();
			}
		});
	}

	private class AuthListener implements WeiboAuthListener {

		@Override
		public void onCancel() {
			Toast.makeText(AuthorizeActivity.this, "取消授权", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onComplete(Bundle values) {
			if (values == null) {
				Toast.makeText(AuthorizeActivity.this, "oauthcode获取失败", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			String code = values.getString("code");
			if (TextUtils.isEmpty(code)) {
				Toast.makeText(AuthorizeActivity.this, "oauthcode获取失败", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			auth_code = code;
			Toast.makeText(AuthorizeActivity.this, "oauthcode获取成功", Toast.LENGTH_SHORT).show();
			fetchTokenAsync(auth_code, WEIBO_DEMO_APP_SECRET);
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(AuthorizeActivity.this, "oauthcode异常", Toast.LENGTH_SHORT).show();

		}

	}

	/**
	 * 异步获取 Token。
	 * 
	 * @param authCode
	 *            授权 Code，该 Code 是一次性的，只能被获取一次 Token
	 * @param appSecret
	 *            应用程序的 APP_SECRET，请务必妥善保管好自己的 APP_SECRET，
	 *            不要直接暴露在程序中，此处仅作为一个DEMO来演示。
	 */
	protected void fetchTokenAsync(String authCode, String appSecret) {
		WeiboParameters requestParams = new WeiboParameters();
		requestParams.put(WBConstants.AUTH_PARAMS_CLIENT_ID, Constants.APP_KEY);
		requestParams.put(WBConstants.AUTH_PARAMS_CLIENT_SECRET, appSecret);
		requestParams.put(WBConstants.AUTH_PARAMS_GRANT_TYPE,
				"authorization_code");
		requestParams.put(WBConstants.AUTH_PARAMS_CODE, authCode);
		requestParams.put(WBConstants.AUTH_PARAMS_REDIRECT_URL,
				Constants.REDIRECT_URL);

		// 异步请求，获取 Token
		AsyncWeiboRunner.requestAsync(OAUTH2_ACCESS_TOKEN_URL, requestParams,
				"POST", new RequestListener() {

					@Override
					public void onWeiboException(WeiboException e) {
						LogUtil.e(TAG, "onWeiboException�� " + e.getMessage());
						Toast.makeText(AuthorizeActivity.this,
								"accessToken获取成功", 0).show();
					}

					@Override
					public void onComplete(String response) {
						LogUtil.d(TAG, "Response: " + response);
						// 获取 Token 成功
						accessToken = Oauth2AccessToken
								.parseAccessToken(response);
						if (accessToken != null && accessToken.isSessionValid()) {
							String data = new SimpleDateFormat(
									"yyyy/MM/dd HH:mm:ss").format(new Date(
									accessToken.getExpiresTime()));
							String uid = accessToken.getUid();
							final UserInfo user = new UserInfo();
							user.setUserId(uid);
							user.setAccessToken(accessToken.getToken());
							user.setExpiresTime(String.valueOf(accessToken
									.getExpiresTime()));
							if (dataHelper.haveUserInfo(uid)) {
								dataHelper.updateUserInfo(user);
							} else {
								dataHelper.SaveUserInfo(user);
							}
							final Handler handler = new Handler() {
								@Override
								public void handleMessage(Message msg) {
									super.handleMessage(msg);
									AuthorizeActivity.this.finish();
									Intent intent = new Intent(
											AuthorizeActivity.this,
											LoginActivity.class);
									startActivity(intent);
								}
							};
							new Thread() {
								public void run() {
									fillUserInfo(dataHelper, user);
									handler.sendEmptyMessage(1);
								};
							}.start();
							System.out.println("auth_code=" + auth_code
									+ "  accessToken=" + accessToken.getToken()
									+ "  过期时间" + data + "  uid"
									+ accessToken.getUid());
						}

					}
				});
	}

	@Override
	protected void onDestroy() {
		dialog.dismiss();
		dataHelper.cloce();
		super.onDestroy();
	}

	/**
	 * 根据授权时获得的用户id和授权码，完善用户名和头像等信息
	 * 
	 * @param userInfo
	 */
	public void fillUserInfo(DataHelper dataHelper, UserInfo userInfo) {
		String url = "https://api.weibo.com/2/users/show.json";
		ContentValues values = new ContentValues();
		values.put("access_token", userInfo.getAccessToken());
		values.put("uid", userInfo.getUserId());
		String userStr = HttpApiHelper.getApiData(url, values);
		try {
			JSONObject data = new JSONObject(userStr);
			String imgPath = data.getString("profile_image_url");
			String userName = data.getString("screen_name");
			Bitmap userIcon = downloadImg(imgPath);
			dataHelper.updateUserInfo(userName, userIcon, userInfo.getUserId());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private Bitmap downloadImg(String urlStr) {
		Bitmap bitmap = null;
		try {
			URL url = new URL(urlStr);
			InputStream is = url.openConnection().getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
