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

	public Drawable loadDrawable(final String imageUrl, final ImageView imageView,
			final ImageCallBack imageCallBack) {
		System.out.println("imageCache大小："+imageCache.size());
		SoftReference<Drawable> drawableRef = imageCache.get(imageUrl);
		if(drawableRef!=null){
			//从缓存中获取
			System.out.println(imageUrl+"从缓存中获取");
			//SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = drawableRef.get();
			if(drawable!=null){
				return drawable;
			}
			 // Reference has expired so remove the key from drawableMap 
			imageCache.remove(imageUrl);
		}
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				imageCallBack.imageLoaded((Drawable) msg.obj, imageView);
			}
		};
		new Thread(){
			public void run() {
				Drawable drawable = loadImageFromUrl(imageUrl);
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				System.out.println("imageCache放后大小："+imageCache.size());
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			};
		}.start();
		return null;
	}
	public static Drawable loadImageFromUrl(String urlStr){
		try {
			URL url = new URL(urlStr);
			InputStream is = url.openConnection().getInputStream();
			Drawable drawable = Drawable.createFromStream(is, "src");
			is.close();
			is=null;
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
	public static Drawable getDrawable(InputStream is, int reqWidth, int reqHeight){
		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(is, null, options);
		int inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;
		options.inSampleSize = inSampleSize;
		Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
		Drawable drawable = new BitmapDrawable(bitmap);
		return drawable;
	}
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
