package com.hj.mysinaweibo;

import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.hj.mysinaweibo.commonutil.ConfigHelper;
import com.hj.mysinaweibo.commonutil.HttpApiHelper;
import com.hj.mysinaweibo.model.UserInfo;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditWeiboActivity extends Activity implements OnClickListener {
	private Button bt_publish;
	private EditText et_weibo_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_weibo);
		bt_publish = (Button) findViewById(R.id.bt_publish);
		et_weibo_text = (EditText) findViewById(R.id.et_weibo_text);
		bt_publish.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		new Thread(){
			public void run() {
				String path = "https://api.weibo.com/2/statuses/update.json";
				UserInfo userInfo = ConfigHelper.currentUser;
				try {
					ContentValues values = new ContentValues();
					values.put("access_token", userInfo.getAccessToken());
					values.put("status", et_weibo_text.getText()
							.toString());
					values.put("visible", "0");
					String resultStr = HttpApiHelper.postApiData(path, values);
					JSONObject jsonObject = new JSONObject(resultStr);
					String createTime = jsonObject.getString("created_at");
					System.out.println("创建时间：" + createTime);
				} catch (Exception e) {
					e.printStackTrace();
				}			
			};
		}.start();

	}
}
