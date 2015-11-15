package com.hj.mysinaweibo.commonutil;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class AsyncImageLoader {
	// SoftReference是软引用，是为了更好的为了系统回收变量
	private HashMap<String, SoftReference<Drawable>> imageCache;

	public AsyncImageLoader() {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
	}

	public void loadDrawable(final String imageUrl, final ImageView imageView,
			final ImageCallBack imageCallBack) {
		//System.out.println("imageCache大小："+imageCache.size());
		SoftReference<Drawable> drawableRef = imageCache.get(imageUrl);
		if(drawableRef!=null){
			//从缓存中获取
			System.out.println(imageUrl+"从缓存中获取");
			//SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = drawableRef.get();
			if(drawable!=null){
				imageView.setImageDrawable(drawable);
			}else{
				 // Reference has expired so remove the key from drawableMap 
				imageCache.remove(imageUrl);	
			}
		}
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				imageCallBack.imageLoaded((Drawable) msg.obj, imageView);
				System.out.println(imageUrl+"非缓存获取");
			}
		};
		new Thread(){
			public void run() {
				Drawable drawable = loadImageFromUrl(imageUrl);
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				//System.out.println("imageCache放后大小："+imageCache.size());
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			};
		}.start();
	}
	public static Drawable loadImageFromUrl(String urlStr){
		try {
			URL url = new URL(urlStr);
			//使用HttpURLConnection产生的流只能使用一次如果你想连续调用这个流 必须重新初始化 
			//InputStream is = url.openConnection().getInputStream();
			//Drawable drawable = Drawable.createFromStream(is, "src");
			Drawable drawable = getDrawable(250, 400, url);
			//is.close();
			//is=null;
			return drawable;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	//回调接口
	public interface ImageCallBack {
		public void imageLoaded(Drawable imageDrawable, ImageView imageView);
	}
	//防止图片内存溢出
	public static Drawable getDrawable(int reqWidth, int reqHeight, URL url) throws IOException{
		Options options = new Options();
		//不解析图片，只获取头文件信息
		options.inJustDecodeBounds = true;
		InputStream is = url.openConnection().getInputStream();
		Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
		int inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		//解析图片
		options.inJustDecodeBounds = false;
		//获取解码的图，而不是原图
		options.inSampleSize = inSampleSize;
		is = url.openConnection().getInputStream();
		bitmap = BitmapFactory.decodeStream(is, null, options);
		Drawable drawable = new BitmapDrawable(bitmap);
		return drawable;
	}
	//计算缩放比例
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}	
}
