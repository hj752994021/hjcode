package com.hj.flappybird;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class MainActivity extends Activity {

	private EditText et_text;
	private GameSurfaceView gameSurfaceView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//View view = View.inflate(this, R.layout.activity_main, null);
		setContentView(R.layout.activity_main);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//gameSurfaceView.setFocusable(true);
/*		gameSurfaceView = (GameSurfaceView) findViewById(R.id.sv_fb);
		SurfaceHolder holder = gameSurfaceView.getHolder();
		Canvas canvas = holder.lockCanvas();
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		canvas.drawCircle(200, 200, 60, paint);
		holder.unlockCanvasAndPost(canvas);*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void startGame(View view){
		Intent intent = new Intent(MainActivity.this, GameViewActivity.class);
		startActivity(intent);
		
	}
}
