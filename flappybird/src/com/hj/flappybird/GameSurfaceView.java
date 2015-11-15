package com.hj.flappybird;

import java.util.Random;
import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;

public class GameSurfaceView extends SurfaceView implements Callback {

	private SurfaceHolder holder;
	private Paint paint;
	private Canvas canvas;
	//private Bitmap background;
	private Bitmap background_day;
	private Bitmap background_ground;
	private int dx,dx2 = -480;
	private Rect srcRect,destRect,destRect2;
	private int gsvHeight,gsvWidth;
	private Vector<Thread> barrierTds;
	private Vector<Barrier> barriers;
	private Bird bird;
	private Thread birdTd;
	private int a=0;
	public GameSurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		holder = this.getHolder();
		holder.addCallback(this);
		paint = new Paint();
		paint.setStrokeWidth(3);
		paint.setAntiAlias(true);
		paint.setColor(Color.RED);
		//background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
		background_day = BitmapFactory.decodeResource(getResources(), R.drawable.background_day);
		background_ground = BitmapFactory.decodeResource(getResources(), R.drawable.background_ground);
		srcRect = new Rect(0, 0, 480, 800);
		destRect = new Rect(dx, 0, 480, 800);
		destRect2 = new Rect(dx2, 0, 0, 800);
		barrierTds = new Vector<Thread>();
		barriers = new Vector<Barrier>();
	}
	
	public GameSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	private void draw() {
		canvas = holder.lockCanvas();
		clear();
		//背景
/*		dx+=3;
		dx2+=3;
		destRect.set(dx, 0, 480+dx, 800);
		destRect2.set(dx2, 0, 480+dx2, 800);
		
		canvas.drawBitmap(background, srcRect, destRect, paint);
		canvas.drawBitmap(background, srcRect, destRect2, paint);
		if(dx>=480){
			dx=-480;
		}
		if(dx2>=480){
			dx2=-480;
		}*/
		canvas.drawBitmap(resizeBitmap(background_day,gsvWidth,gsvHeight-100), 0, 0, null);
		canvas.drawBitmap(resizeBitmap(background_ground,gsvWidth,100), a, gsvHeight-100, null);
		canvas.drawBitmap(resizeBitmap(background_ground,gsvWidth,100), a-gsvWidth, gsvHeight-100, null);
		a+=3;
		if(a==gsvWidth){
			a=0;
		}
		//障碍
		for(Barrier b : barriers){
			drawBarried(b);
		}
		//物体
		drawBrid(bird);
		holder.unlockCanvasAndPost(canvas);
	}
	private void drawBrid(Bird bird){
		//canvas.drawCircle(bird.x, bird.y, bird.radius, paint);
		//canvas.drawRect(bird.x, bird.y, bird.x+bird.width, bird.y+bird.height, paint);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bird);
		canvas.drawBitmap(bitmap, bird.x, bird.y, null);
	}
	private void drawBarried(Barrier barrier){
		//画上面的障碍条
		canvas.drawRect(barrier.x, 0, barrier.x+barrier.width, barrier.y, paint);
		//画下面的障碍条
		canvas.drawRect(barrier.x, barrier.y+barrier.height, barrier.x+barrier.width, gsvHeight, paint);
	}
	public  Bitmap resizeBitmap(Bitmap bitmap, int w, int h) {
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int newWidth = w;
            int newHeight = h;
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
                    height, matrix, true);
            return resizedBitmap;
        } else {
            return null;
        }
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			bird.position=bird.y;
			bird.time=0;
			for (int i = 0; i < 7; i++) {
				bird.y-=20;
			}
		}
		return super.onTouchEvent(event);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		System.out.println("空格KEYCODE_SPACE");
		if(keyCode==KeyEvent.KEYCODE_SPACE){
			System.out.println("KEYCODE_SPACE");
			bird.position=bird.y;
			bird.time=0;
			for (int i = 0; i < 7; i++) {
				bird.y-=20;
			}
		}
		if(keyCode == KeyEvent.KEYCODE_BACK){
			bird.isLive = false;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		System.out.println("创建");
		//初始值必须放到这里才行, 因为SurfaceView创建成功后才能获取高宽 
		gsvHeight = getHeight();
		gsvWidth = getWidth();
		bird = new Bird();
		birdTd = new Thread(bird);
		Barrier barrier1 = new Barrier(540, 200, gsvWidth, gsvHeight);
		Thread barrierTd1 = new Thread(barrier1);
		Barrier barrier2 = new Barrier(810, 300, gsvWidth, gsvHeight);
		Thread barrierTd2 = new Thread(barrier2);
		barrierTds.add(0, barrierTd1);
		barrierTds.add(1, barrierTd2);
		barriers.add(0, barrier1);
		barriers.add(1, barrier2);
		
/*		this.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode==62&&event.getAction()==KeyEvent.ACTION_DOWN){
					System.out.println("点击");
					bird.position=bird.y;
					bird.time=0;
				}
				return true;
			}
		});*/
		
		//启动多个线程
		birdTd.start();
		for(Thread td : barrierTds){
			td.start();

		}
		new Thread(){
			public void run() {
				while(bird.isLive){
/*					for(int i=0; i<2; i++){
						Thread td = barrierTds.get(i);
						if(td.isAlive()){
							draw();
						}
					}*/
					if(isTouch()){
						bird.isLive = false;
						for (Barrier barrier : barriers) {
							barrier.isVisible = false;
						}
					}
					draw();
				}
			};
		}.start();

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
	//清除画布
	private void clear() {
		Canvas canvas = new Canvas();
		Paint paint = new Paint();
		paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		canvas.drawPaint(paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
		//invalidate();
	}
	//判断小鸟是否跟障碍物碰撞
	private boolean isTouch(){
		for (Barrier barrier : barriers) {
			if((bird.y<barrier.y||bird.y+bird.height>barrier.y+barrier.height)&&(bird.x+bird.width>barrier.x&&barrier.x+barrier.width>bird.x)){
				return true;
			}
		}
		return false;
	}
}
class Bird implements Runnable {
	//鸟的坐标，半径
	int x=150,y=150,radius=20,width=45,height=45;
	//是否存活
	boolean isLive=true;
	//time为按下键后的计时器，position为按下键后的位置
	int time,position=150;
	@Override
	public void run() {
		while(isLive){
			//this.y+=3;
			//System.out.println(x);
			//y=(int) (position-50*time+0.5*7*Math.pow(time, 2));
			y=(int) (position-30*time+0.5*2*Math.pow(time, 2));
			try { 
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//y +=3;
			time++;
			if(this.y>800){
				isLive=false;
				break;
			}
		}
	}
	
}
class Barrier implements Runnable {
	private int gsvWidth;
	private int gsvHeight;
	public Barrier(int x, int y,int gsvWidth,int gsvHeight) {
		this.x = x;
		this.y = y;
		this.gsvWidth = gsvWidth;
		this.gsvHeight = gsvHeight;
	}

	// 左上角的坐标,速度
	int x, y, speed = 3;
	// 左上角右上角的距离，左上角左下角的距离
	float width=60, height=450;
	boolean isVisible=true;

	@Override
	public void run() {
		while (isVisible) {
			
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
			}
			x -= speed;
			if (this.x < -30) {
				this.y = new Random().nextInt(400)+100;
				this.x = gsvWidth+30;
			}
		}
	}
}
