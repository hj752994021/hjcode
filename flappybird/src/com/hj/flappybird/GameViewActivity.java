package com.hj.flappybird;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class GameViewActivity extends Activity {
	GameSurfaceView gameSurfaceView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		gameSurfaceView = new GameSurfaceView(this);
		setContentView(gameSurfaceView);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		gameSurfaceView.setFocusable(true);
	}
	//http://tech.it168.com/a2011/0406/1174/000001174856_all.shtml
}
