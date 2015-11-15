package com.hj.mysinaweibo.commonutil;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class AndroidHelper {
	//获取屏幕方向
	public static int getScreenOrient(Activity activity){
		//获取 到清单文件中activity的属性android:screenOrientation="landscape"
		int orient = activity.getRequestedOrientation();
		if(orient != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE && orient != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
			//宽>高为横屏,反之为竖屏
			WindowManager windowManager = activity.getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			int screenWidth = display.getWidth();
			int screenHeight = display.getHeight();
			orient = screenWidth > screenHeight ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		}
		return orient;
	}
	
	public static void autoBackground(Activity activity, View view, int Background_v, int Background_h){
		int orient = getScreenOrient(activity);
		if(orient == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){//模
			view.setBackgroundResource(Background_h);
		}else{
			view.setBackgroundResource(Background_v);
		}
	}
}
