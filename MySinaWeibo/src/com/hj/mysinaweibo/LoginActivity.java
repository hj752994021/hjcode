package com.hj.mysinaweibo;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hj.mysinaweibo.commonutil.AndroidHelper;
import com.hj.mysinaweibo.commonutil.ConfigHelper;
import com.hj.mysinaweibo.model.UserInfo;
import com.hj.mysinaweibo.sql.DataHelper;

public class LoginActivity extends Activity implements OnClickListener {

	private Dialog dialog;
	private DataHelper dbHelper;
	private List<UserInfo> userInfos;
	private SharedPreferences sp;
	private EditText iconSelect;
	private ImageView icon;
	private ImageButton login;
	private ListView list;

	private LinearLayout ll_add_account;
	private LinearLayout ll_exit_soft;
	private LinearLayout ll_del_account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
		iconSelect = (EditText) findViewById(R.id.iconSelect);
		icon = (ImageView) findViewById(R.id.icon);
		login = (ImageButton) findViewById(R.id.login);
		ll_add_account = (LinearLayout) findViewById(R.id.ll_add_account);
		ll_exit_soft = (LinearLayout) findViewById(R.id.ll_exit_soft);
		ll_del_account = (LinearLayout) findViewById(R.id.ll_del_account);
		sp = getSharedPreferences("name", MODE_PRIVATE);

		AndroidHelper.autoBackground(this, layout, R.drawable.bg_w,
				R.drawable.bg_h);
		ImageButton iconSelectBtn = (ImageButton) findViewById(R.id.iconSelectBtn);
		iconSelectBtn.setOnClickListener(this);
		login.setOnClickListener(this);
		ll_add_account.setOnClickListener(this);
		ll_exit_soft.setOnClickListener(this);
		ll_del_account.setOnClickListener(this);
		initUser();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iconSelectBtn:

			dialog = new Dialog(LoginActivity.this, R.style.dialog2);
			View diaView = View.inflate(LoginActivity.this, R.layout.dialog2,
					null);
			dialog.setContentView(diaView);
			dialog.show();

			UserAdapter adapter = new UserAdapter();
			list = (ListView) diaView.findViewById(R.id.list);
			list.setVerticalScrollBarEnabled(true);// ListView去掉下拉条
			list.setAdapter(adapter);
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					TextView tv_username = (TextView) view
							.findViewById(R.id.tv_username);
					ImageView iv_usericon = (ImageView) view
							.findViewById(R.id.iv_usericon);
					iconSelect.setText(tv_username.getText());
					icon.setImageDrawable(iv_usericon.getDrawable());
					dialog.dismiss();
				}
			});
			break;
		case R.id.login:
			goHome();
			break;
		case R.id.ll_add_account:
			Intent intent = new Intent(this, AuthorizeActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_exit_soft:
			finish();
			break;
		case R.id.ll_del_account:
			String name = iconSelect.getText().toString();
			if("".equals(name)){
				Toast.makeText(this, "请选择用户", 0).show();
				break;
			}
			UserInfo userInfo = getUserByName(name);
			dbHelper.DelUserInfo(userInfo.getUserId());
			initUser();
			break;
		default:
			break;
		}
	}

	// 进入用户首页
	private void goHome() {
		if (userInfos != null) {
			String name = iconSelect.getText().toString();
			UserInfo userInfo = getUserByName(name);
			if (Long.parseLong(userInfo.getExpiresTime()) < System
					.currentTimeMillis()) {
				Toast.makeText(LoginActivity.this, "授权日期已过，请重新授权", 0).show();
				Intent intent = new Intent(this, AuthorizeActivity.class);
				startActivity(intent);
				finish();
				return;
			}
			if (userInfo != null) {
				System.out.println(9999);
				ConfigHelper.currentUser = userInfo;
			}
		}
		if (ConfigHelper.currentUser != null) {
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
		}
	}

	@Override
	protected void onStop() {
		Editor editor = sp.edit();
		editor.putString("name", iconSelect.getText().toString());
		editor.commit();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		if (dialog != null) {
			dialog.dismiss();
		}
		super.onDestroy();
	}

	private void initUser() {
		dbHelper = new DataHelper(this);
		userInfos = dbHelper.getUserList(false);
		if (userInfos == null) {
			Intent intent = new Intent(this, AuthorizeActivity.class);
			startActivity(intent);
		} else {
			String name = sp.getString("name", "");
			UserInfo userInfo;
			if (!name.equals("")) {
				userInfo = getUserByName(name);
			} else {
				userInfo = userInfos.get(0);
			}
			icon.setImageDrawable(userInfo.getUserIcon());
			iconSelect.setText(userInfo.getUserName());
		}
	}

	private UserInfo getUserByName(String name) {
		for (UserInfo userInfo : userInfos) {
			if (userInfo.getUserName().equals(name)) {
				return userInfo;
			}
		}
		return null;
	}

	private class UserAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return userInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return userInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			UserInfo userInfo = userInfos.get(position);
			for (UserInfo u : userInfos) {
				System.out.println(u.getUserName());
				System.out.println(u.getUserId());
			}
			if (convertView == null) {
				view = View.inflate(LoginActivity.this, R.layout.user_item,
						null);
			} else {
				view = convertView;
			}
			ImageView iv_usericon = (ImageView) view
					.findViewById(R.id.iv_usericon);
			TextView tv_username = (TextView) view
					.findViewById(R.id.tv_username);
			iv_usericon.setImageDrawable(userInfo.getUserIcon());
			tv_username.setText(userInfo.getUserName());
			return view;
		}

	}
}
