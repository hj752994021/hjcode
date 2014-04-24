package com.hj.mysinaweibo.sql;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.hj.mysinaweibo.model.UserInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class DataHelper {
	// 数据库名称
	private static String DB_NAME = "MYSINAWEIBO.DB";
	// 数据库版本
	private static int DB_VERSION = 1;
	private SQLiteDatabase db;
	private SqliteHelper dbHelper;

	public DataHelper(Context context) {
		dbHelper = new SqliteHelper(context, DB_NAME, null, DB_VERSION);
		db = dbHelper.getWritableDatabase();
	}

	public void cloce() {
		db.close();
		dbHelper.close();
	}

	public List<UserInfo> getUserList(Boolean isSimple) {
		List<UserInfo> userInfos = new ArrayList<UserInfo>();
		Cursor cursor = db
				.rawQuery(
						"select _id,userId,accessToken,expiresTime,userName,userIcon from users",
						null);
		while (cursor.moveToNext()) {
			UserInfo userInfo = new UserInfo();
			userInfo.setId(cursor.getString(cursor.getColumnIndex("_id")));
			userInfo.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
			userInfo.setAccessToken(cursor.getString(cursor.getColumnIndex("accessToken")));
			userInfo.setExpiresTime(cursor.getString(cursor.getColumnIndex("expiresTime")));
			if (!isSimple) {
				userInfo.setUserName(cursor.getString(cursor
						.getColumnIndex("userName")));
				ByteArrayInputStream stream = new ByteArrayInputStream(
						cursor.getBlob(cursor.getColumnIndex("userIcon")));
				Drawable icon = Drawable.createFromStream(stream, "image");
				userInfo.setUserIcon(icon);
			}
			userInfos.add(userInfo);
			userInfo = null;
		}
		cursor.close();
		return userInfos;
	}

	// 判断users表中的是否包含某个UserID的记录
	public Boolean haveUserInfo(String userId) {
		Cursor cursor = db.rawQuery("select * from users where userId=?",
				new String[] { userId });
		if (cursor.moveToNext()) {
			return true;
		} else {
			return false;
		}
	}

	// 更新users表的记录，根据UserId更新用户昵称和用户图标
	public void updateUserInfo(String userName, Bitmap userIcon, String userId) {
		Object[] params = new Object[3];
		params[0] = userName;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		// 将Bitmap压缩成PNG编码，质量为100%存储
		userIcon.compress(Bitmap.CompressFormat.PNG, 100, os);
		params[1] = os.toByteArray();
		params[2] = userId;
		db.execSQL("update users set userName=? ,userIcon=? where userId=?",
				params);
	}
	//更新users表的记录
	public int updateUserInfo(UserInfo user) {
		ContentValues values = new ContentValues();
		values.put(UserInfo.USERID, user.getUserId());
		values.put(UserInfo.ACCESSTOKEN, user.getAccessToken());
		values.put(UserInfo.EXPIRESTIME, user.getExpiresTime());
		int id = db.update(SqliteHelper.TB_NAME, values, UserInfo.USERID + "="
				+ user.getUserId(), null);
		return id;
	}
    //添加users表的记录
    public Long SaveUserInfo(UserInfo user)
    {
        ContentValues values = new ContentValues();
        values.put(UserInfo.USERID, user.getUserId());
		values.put(UserInfo.ACCESSTOKEN, user.getAccessToken());
		values.put(UserInfo.EXPIRESTIME, user.getExpiresTime());
        Long uid = db.insert(SqliteHelper.TB_NAME, UserInfo.ID, values);
        return uid;
    }
    
    //删除users表的记录
    public int DelUserInfo(String UserId){
        int id=  db.delete(SqliteHelper.TB_NAME, UserInfo.USERID +"="+UserId, null);
        return id;
    }	
}
