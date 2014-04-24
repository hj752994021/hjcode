package com.hj.mysinaweibo.model;

import java.util.Date;

import android.graphics.drawable.Drawable;

public class UserInfo {
	public static final String ID = "_id";
	public static final String USERID = "userId";
	public static final String ACCESSTOKEN = "accessToken";
	public static final String USERNAME = "userName";
	public static final String USERICON = "userIcon";
	public static final String EXPIRESTIME="expiresTime";
	
	private String id;
	private String userId;
	private String accessToken;
	private String userName;
	private Drawable userIcon;
	private String expiresTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Drawable getUserIcon() {
		return userIcon;
	}
	public void setUserIcon(Drawable userIcon) {
		this.userIcon = userIcon;
	}
	public String getExpiresTime() {
		return expiresTime;
	}
	public void setExpiresTime(String expiresTime) {
		this.expiresTime = expiresTime;
	}
}
