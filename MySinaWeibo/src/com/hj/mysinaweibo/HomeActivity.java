package com.hj.mysinaweibo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hj.mysinaweibo.commonutil.AsyncImageLoader;
import com.hj.mysinaweibo.commonutil.AsyncImageLoader.ImageCallBack;
import com.hj.mysinaweibo.commonutil.ConfigHelper;
import com.hj.mysinaweibo.commonutil.HttpApiHelper;
import com.hj.mysinaweibo.model.UserInfo;
import com.hj.mysinaweibo.model.WeiBoInfo;

public class HomeActivity extends Activity implements OnClickListener {

	private List<WeiBoInfo> weiBoInfos;
	private UserInfo userInfo;
	private TextView showName;
	private String weiboStr;
	private ImageButton ib_wite_weibo;
	private ImageButton ib_refresh;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			getWeiBoInfos(weiboStr);
			if (weiBoInfos != null) {
				WeiBoAdapter adapter = new WeiBoAdapter();
				ListView lv_message = (ListView) findViewById(R.id.lv_message);

				lv_message.setAdapter(adapter);
				lv_message.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						System.out.println("点了一项");
						Intent intent = new Intent(HomeActivity.this,
								ViewActivity.class);
						intent.putExtra("weiboId", weiBoInfos.get(position)
								.getId());
						startActivity(intent);
					}
				});
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		ib_wite_weibo = (ImageButton) findViewById(R.id.ib_wite_weibo);
		ib_refresh = (ImageButton) findViewById(R.id.ib_refresh);
		ib_refresh.setOnClickListener(this);
		ib_wite_weibo.setOnClickListener(this);
		loadList();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_wite_weibo:
			Intent intent = new Intent(this, EditWeiboActivity.class);
			startActivity(intent);
			break;
		case R.id.ib_refresh:

			break;

		default:
			break;
		}
	}

	private void loadList() {
		if (ConfigHelper.currentUser != null) {
			userInfo = ConfigHelper.currentUser;
			// 显示当前用户名称
			showName = (TextView) findViewById(R.id.tv_showname);
			showName.setText(userInfo.getUserName());

			final ContentValues values = new ContentValues();
			values.put("access_token", userInfo.getAccessToken());
			values.put("uid", userInfo.getUserId());
			new Thread() {
				public void run() {
					weiboStr = HttpApiHelper
							.getApiData(
									"https://api.weibo.com/2/statuses/friends_timeline.json",
									values);
					handler.sendEmptyMessage(1);
				};
			}.start();
		}

	}

	private void getWeiBoInfos(String str) {
		if (!"".equals(str)) {
			try {
				JSONObject obj = new JSONObject(str);
				JSONArray data = obj.getJSONArray("statuses");
				for (int i = 0; i < data.length(); i++) {
					JSONObject d = data.getJSONObject(i);
					if (d != null) {
						JSONObject u = d.getJSONObject("user");
						if (d.has("retweeted_status")) {
							JSONObject r = d.getJSONObject("retweeted_status");
						}
						// 微博
						String id = d.getString("id");
						String userId = u.getString("id");
						String userName = u.getString("screen_name");
						String userIcon = u.getString("profile_image_url");
						String time = d.getString("created_at");
						String text = d.getString("text");
						Boolean haveImg = false;
						if (d.has("thumbnail_pic")) {
							haveImg = true;
						}
						Date date = new Date(time);
						time = ConvertTime(date);
						if (weiBoInfos == null) {
							weiBoInfos = new ArrayList<WeiBoInfo>();
						}
						WeiBoInfo weiBoInfo = new WeiBoInfo();
						weiBoInfo.setId(id);
						weiBoInfo.setUserId(userId);
						weiBoInfo.setUserName(userName);
						weiBoInfo.setTime(time);
						weiBoInfo.setText(text);

						weiBoInfo.setHaveImage(haveImg);
						weiBoInfo.setUserIcon(userIcon);

						weiBoInfos.add(weiBoInfo);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private String ConvertTime(Date oldDate) {
		Date nowDate = new Date();
		Long timeSub = (nowDate.getTime() - oldDate.getTime()) / 1000;
		int day = (int) (timeSub / (3600 * 24));
		if (day > 1) {
			return day + "天前";
		} else {
			int hours = (int) (timeSub % (3600 * 24));
			int hour = hours / (3600);
			if (hour > 1)
				return (hour + "小时前");
			int mins = (int) (hours % 3600);
			int min = mins / (60);
			if (min > 1) {
				return (min + "分种前");
			} else {
				int sec = (int) (mins % 60);
				return (sec + "秒前");
			}
		}
	}

	public class WeiBoAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return weiBoInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return weiBoInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			WeiBoInfo weiBoInfo = weiBoInfos.get(position);
			AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
			if (convertView == null) {
				view = View.inflate(HomeActivity.this, R.layout.weibo, null);
			} else {
				view = convertView;
			}
			ImageView iv_weibo_icon = (ImageView) view
					.findViewById(R.id.iv_weibo_icon);
			TextView tv_user_name = (TextView) view
					.findViewById(R.id.tv_user_name);
			ImageView iv_have_photo = (ImageView) view
					.findViewById(R.id.iv_have_photo);
			TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
			TextView tv_text = (TextView) view.findViewById(R.id.tv_text);

			tv_user_name.setText(weiBoInfo.getUserName());
			tv_time.setText(weiBoInfo.getTime());
			tv_text.setText(weiBoInfo.getText());
			if (weiBoInfo.getHaveImage()) {
				iv_have_photo.setVisibility(View.VISIBLE);
				iv_have_photo.setImageResource(R.drawable.images);
			} else {
				iv_have_photo.setVisibility(View.INVISIBLE);
			}
			Drawable cachedImage = asyncImageLoader.loadDrawable(
					weiBoInfo.getUserIcon(), iv_weibo_icon,
					new ImageCallBack() {
						@Override
						public void imageLoaded(Drawable imageDrawable,
								ImageView imageView) {
							imageView.setImageDrawable(imageDrawable);
						}
					});
			if (cachedImage != null) {
				iv_weibo_icon.setImageDrawable(cachedImage);
			}
			return view;
		}

	}

}
