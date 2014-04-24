package com.hj.mysinaweibo.oauth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;

import com.hj.mysinaweibo.model.UserInfo;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

public class OAuth {

	private CommonsHttpOAuthConsumer httpOAuthConsumer;
	private OAuthProvider httpOauthprovider;
	private String consumerKey;
	private String consumerSecret;

	public OAuth() {
		// 第一组：（App Key和App Secret）
		// 这组参数就是本系列文本第一篇提到的建一个新的应用获取App Key和App Secret。
		this("509245331", "3a3a45d91ce2dd016fd10befbe836166");
	}

	public OAuth(String consumerKey, String consumerSecret) {
		super();
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
	}

	public void requestAccessToken(final Activity activity,
			final String callBackUrl) {
		new Thread() {
			public void run() {
				try {

					httpOAuthConsumer = new CommonsHttpOAuthConsumer(
							consumerKey, consumerSecret);
					httpOauthprovider = new DefaultOAuthProvider(
							"http://api.t.sina.com.cn/oauth/request_token",
							"http://api.t.sina.com.cn/oauth/access_token",
							"http://api.t.sina.com.cn/oauth/authorize");
					String authUrl = httpOauthprovider.retrieveRequestToken(
							httpOAuthConsumer, callBackUrl);
					activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri
							.parse(authUrl)));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}

	public UserInfo getAccessToken(final Intent intent) {
		Uri uri = intent.getData();
		String verifier = uri
				.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
		try {
			httpOauthprovider.setOAuth10a(true);
			httpOauthprovider.retrieveAccessToken(httpOAuthConsumer, verifier);
		} catch (OAuthMessageSignerException e) {
			e.printStackTrace();
		} catch (OAuthNotAuthorizedException e) {
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			e.printStackTrace();
		}
		SortedSet<String> user_id = httpOauthprovider.getResponseParameters()
				.get("user_id");
		String userId = user_id.first();
		String userKey = httpOAuthConsumer.getToken();
		String userSecret = httpOAuthConsumer.getTokenSecret();
		System.out.println("userId" + userId);
		System.out.println("userKey" + userKey);
		System.out.println("userSecret" + userSecret);
		UserInfo user = new UserInfo();
		user.setUserId(userId);
		//user.setToken(userKey);
		//user.setTokenSecret(userSecret);
		return user;
	}

	public HttpResponse signRequest(String token, String tokenSecret,
			String url, List params) {
		HttpPost post = new HttpPost(url);
		try {
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 关闭Expect:100-Continue握手
		// 100-Continue握手需谨慎使用，因为遇到不支持HTTP/1.1协议的服务器或者代理时会引起问题
		post.getParams().setBooleanParameter(
				CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
		return signRequest(token, tokenSecret, post);
	}

	public HttpResponse signRequest(String token, String tokenSecret,
			HttpPost post) {
		httpOAuthConsumer = new CommonsHttpOAuthConsumer(consumerKey,
				consumerSecret);
		httpOAuthConsumer.setTokenWithSecret(token, tokenSecret);
		HttpResponse response = null;
		try {
			httpOAuthConsumer.sign(post);
		} catch (OAuthMessageSignerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 取得HTTP response
		try {
			response = new DefaultHttpClient().execute(post);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}
}
