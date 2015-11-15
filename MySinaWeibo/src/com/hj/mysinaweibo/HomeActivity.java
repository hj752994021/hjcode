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
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hj.mysinaweibo.commonutil.AsyncImageLoader;
import com.hj.mysinaweibo.commonutil.AsyncImageLoader.ImageCallBack;
import com.hj.mysinaweibo.commonutil.AsynchTashImageLoader;
import com.hj.mysinaweibo.commonutil.ConfigHelper;
import com.hj.mysinaweibo.commonutil.HttpApiHelper;
import com.hj.mysinaweibo.model.UserInfo;
import com.hj.mysinaweibo.model.WeiBoInfo;

public class HomeActivity extends Activity implements OnClickListener, OnItemClickListener {

	private List<WeiBoInfo> weiBoInfos;
	private UserInfo userInfo;
	private TextView showName;
	private String weiboStr;
	private ImageButton ib_wite_weibo;
	private ImageButton ib_refresh;
	private WeiBoAdapter adapter;
	private AsynchTashImageLoader tashImageLoader;
	private LinearLayout ll_loading;
	private Button bt_foot;
	private ListView lv_message;
	private int pageSize=10;
	private int pageNow=1;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				System.out.println("首次进入");
				//首次进入
				weiBoInfos = stringToWeiBoInfos(weiboStr);
				if (weiBoInfos != null) {
					lv_message.addFooterView(bt_foot);
					lv_message.setAdapter(adapter);
					lv_message.removeFooterView(bt_foot);
					ll_loading.setVisibility(View.INVISIBLE);
				}
				break;
			case 2:
				System.out.println("刷新");
				//刷新
				weiBoInfos.addAll(stringToWeiBoInfos(weiboStr));
				ll_loading.setVisibility(View.INVISIBLE);
				lv_message.removeFooterView(bt_foot);
				adapter.notifyDataSetChanged();
				break;
			case 3:
				System.out.println("加载页");
				//加载页
				weiBoInfos.addAll(stringToWeiBoInfos(weiboStr));
				ll_loading.setVisibility(View.INVISIBLE);
				adapter.notifyDataSetChanged();
				lv_message.removeFooterView(bt_foot);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		ib_wite_weibo = (ImageButton) findViewById(R.id.ib_wite_weibo);
		ib_refresh = (ImageButton) findViewById(R.id.ib_refresh);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		bt_foot = (Button) getLayoutInflater().inflate(R.layout.list_foot, null);
		lv_message = (ListView) findViewById(R.id.lv_message);
		ib_refresh.setOnClickListener(this);
		ib_wite_weibo.setOnClickListener(this);
		tashImageLoader = new AsynchTashImageLoader();
		adapter = new WeiBoAdapter();
		getWeiboStrByHttp(pageNow,pageSize,1);
		lv_message.setOnItemClickListener(this);
		bt_foot.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_wite_weibo:
			Intent intent = new Intent(this, EditWeiboActivity.class);
			startActivity(intent);
			break;
		case R.id.ib_refresh:
			weiBoInfos.clear();
			pageNow=1;
			getWeiboStrByHttp(pageNow, pageSize,2);
			break;
		case R.id.bt_foot:
			pageNow++;
			getWeiboStrByHttp(pageNow, pageSize,3);
			break;
		default:
			break;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		System.out.println("点了一项");
		Intent intent = new Intent(HomeActivity.this, ViewActivity.class);
		intent.putExtra("weiboId", weiBoInfos.get(position)
				.getId());
		startActivity(intent);
	}
	/**
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @param flag 1首页，2刷新，3为加载其它页
	 */
	private void getWeiboStrByHttp(int pageNum,int pageSize,final int flag) {
		ll_loading.setVisibility(View.VISIBLE);
		if (ConfigHelper.currentUser != null) {
			userInfo = ConfigHelper.currentUser;
			// 显示当前用户名称
			showName = (TextView) findViewById(R.id.tv_showname);
			showName.setText(userInfo.getUserName());

			final ContentValues values = new ContentValues();
			values.put("access_token", userInfo.getAccessToken());
			values.put("uid", userInfo.getUserId());
			values.put("page", pageNum);
			values.put("count", pageSize);
			new Thread() {
				public void run() {
					weiboStr = HttpApiHelper
							.getApiData(
									"https://api.weibo.com/2/statuses/friends_timeline.json",
									values);
					handler.sendEmptyMessage(flag);
				};
			}.start();
		}

	}

	private List<WeiBoInfo> stringToWeiBoInfos(String str) {
		List<WeiBoInfo> weiBoInfos = new ArrayList<WeiBoInfo>();
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
		return weiBoInfos;
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
			//AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
			if (convertView == null) {
				System.out.println("convertView为空："+position);
				view = View.inflate(HomeActivity.this, R.layout.weibo, null);
			} else {
				view = convertView;
				System.out.println("convertView不为空："+position);
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
/*			asyncImageLoader.loadDrawable(weiBoInfo.getUserIcon(),
					iv_weibo_icon, new ImageCallBack() {
						@Override
						public void imageLoaded(Drawable imageDrawable,
								ImageView imageView) {
							imageView.setImageDrawable(imageDrawable);
						}
					});*/
			tashImageLoader.loadDrawable(weiBoInfo.getUserIcon(), iv_weibo_icon);
			if((position+1)%pageSize==0?(position+1)/pageSize==pageNow:false){
				System.out.println("页眉");
				lv_message.addFooterView(bt_foot);
			}
			return view;
		}

	}
}
