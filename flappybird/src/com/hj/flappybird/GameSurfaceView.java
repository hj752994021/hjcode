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
	private int dy,dy2 = -480;
	private Rect srcRect,destRect,destRect2;
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
		srcRect = new Rect(0, 0, 400, 800);
		destRect = new Rect(0, dy, 480, 800);
		destRect2 = new Rect(0, dy2, 480, 0);
	}
	
	public GameSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	private synchronized void draw(float time) {
		top=(int) (position-50*time+0.5*7*Math.pow(time, 2));
		System.out.println("top="+top+" time="+time);
		canvas = holder.lockCanvas();
		dy+=3;
		dy2+=3;
		//canvas.drawColor(Color.GRAY);
		//paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		destRect.set(0, dy, 480, 800+dy);
		destRect2.set(0, dy2, 480, 800+dy2);
		canvas.drawBitmap(background, srcRect, destRect, paint);
		canvas.drawBitmap(background, srcRect, destRect2, paint);
		canvas.drawCircle(150, top, 20, paint);
		if(dy>=480){
			dy=-480;
		}
		if(dy2>=480){
			dy2=-480;
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
/*			try {
				Thread.sleep(33);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
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
