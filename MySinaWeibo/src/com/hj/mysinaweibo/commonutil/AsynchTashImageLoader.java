package com.hj.mysinaweibo.commonutil;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

public class AsynchTashImageLoader {

	private HashMap<String, SoftReference<Drawable>> imageCache;
	
	public AsynchTashImageLoader(){
		imageCache = new HashMap<String, SoftReference<Drawable>>();
	}
	public void loadDrawable(String imageUrl, ImageView imageView){
		SoftReference<Drawable> drawableRef = imageCache.get(imageUrl);
		//System.out.println("imageCache大小："+imageCache.size());
		if(drawableRef!=null){
			Drawable drawable = drawableRef.get();
			if(drawable!=null){
				System.out.println(imageUrl+"从Tash缓存中获取");
				imageView.setImageDrawable(drawable);
			}else{
				 // Reference has expired so remove the key from drawableMap 
				imageCache.remove(imageUrl);
				//System.out.println("从缓存回收");
			}
		}else{
			//System.out.println(imageUrl+"非Tash缓存获取");
			AsynchLoaderTask task = new AsynchLoaderTask(imageView,imageUrl);
			task.execute(imageUrl);
		}
	}
	private class AsynchLoaderTask extends AsyncTask<String, Integer, Drawable>{
		private ImageView imageView;
		private String imageUrl;
		public AsynchLoaderTask(ImageView imageView, String imageUrl){
			this.imageView = imageView;
			this.imageUrl = imageUrl;
		}
		@Override
		protected Drawable doInBackground(String... params) {
			try {
				URL url = new URL(params[0]);
				InputStream is = url.openConnection().getInputStream();
				Options options = new Options();
				//不解析图片，只获取头文件信息
				options.inJustDecodeBounds = true;
				Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
				//解析图片
				options.inJustDecodeBounds = false;
				//获取解码的图，而不是原图
				options.inSampleSize = calculateInSampleSize(options, 250, 400);;
				is = url.openConnection().getInputStream();
				bitmap = BitmapFactory.decodeStream(is, null, options);
				Drawable drawable = new BitmapDrawable(bitmap);
				return drawable;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Drawable result) {
			super.onPostExecute(result);
			if(result!=null){
				imageView.setImageDrawable(result);
				SoftReference<Drawable> reference = new SoftReference<Drawable>(result);
				imageCache.put(imageUrl, reference);
				//System.out.println("放进去了："+imageCache.size());
			}
		}
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
