package com.hj.flappybird;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;

public class GameSurfaceView extends SurfaceView implements Callback, Runnable {

	private SurfaceHolder holder;
	private Paint paint;
	private int top;
	private boolean bExit;
	private int position;
	private float time;
	private Thread thread;
	private Canvas canvas;
	private Bitmap background;
	private int dx,dx2 = -480;
	private Rect srcRect,destRect,destRect2;
	private Rect barrier1,barrier2,barrier3,barrier4;
	private int gsvHeight,gsvWidth;
	public GameSurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		holder = this.getHolder();
		holder.addCallback(this);
		top=350;
		position=350;
		bExit=true;
		paint = new Paint();
		paint.setStrokeWidth(3);
		paint.setAntiAlias(true);
		paint.setColor(Color.RED);
		background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
		srcRect = new Rect(0, 0, 480, 800);
		destRect = new Rect(dx, 0, 480, 800);
		destRect2 = new Rect(dx2, 0, 0, 800);
		barrier1 = new Rect(50, 0, 100, 200);
		barrier2 = new Rect(200, 0, 250, 200);
		barrier3 = new Rect(50, 600, 100, 800);
		barrier4 = new Rect(200, 600, 250, 800);
	}
	
	public GameSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	private synchronized void draw(float time) {
		top=(int) (position-50*time+0.5*7*Math.pow(time, 2));
		System.out.println("top="+top+" time="+time);
		canvas = holder.lockCanvas();
		dx+=3;
		dx2+=3;
		//canvas.drawColor(Color.GRAY);
		//paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		destRect.set(dx, 0, 480+dx, 800);
		destRect2.set(dx2, 0, 480+dx2, 800);
		canvas.drawBitmap(background, srcRect, destRect, paint);
		canvas.drawBitmap(background, srcRect, destRect2, paint);
		canvas.drawCircle(150, top, 20, paint);
		canvas.drawRect(barrier1, paint);
		canvas.drawRect(barrier2, paint);
		canvas.drawRect(barrier3, paint);
		canvas.drawRect(barrier4, paint);
		if(dx>=480){
			dx=-480;
		}
		if(dx2>=480){
			dx2=-480;
		}
		holder.unlockCanvasAndPost(canvas);
		if(top>getHeight()){
			bExit=false;
		}
	}

	@Override
	public void run() {
		time=0;
		while(time<150&&bExit==true){
			draw(time);
			time=(float) (time+1);
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		//初始值必须放到这里才行, 因为SurfaceView创建成功后才能获取高宽 
		gsvHeight = getHeight();
		gsvWidth = getWidth();
		thread = new Thread(this);
		thread.start();
		this.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode==62&&event.getAction()==KeyEvent.ACTION_DOWN){

					if(!thread.isAlive()){
						thread = new Thread(GameSurfaceView.this);
						thread.start();
					}
					System.out.println("top="+top+" time="+time);
					time=0;
					bExit = true;
					if(top>=getHeight()){
						top=350;
					}
					position=top;
				}
				return false;
			}
		});
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
 
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

}
