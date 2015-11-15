package com.hj.mysinaweibo.sql;

import com.hj.mysinaweibo.model.UserInfo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {

	public static final String TB_NAME = "users";

	public SqliteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	// 创建表
	@Override
	public void onCreate(SQLiteDatabase db) {
		//integer primary key设置成这样，可以自增
		db.execSQL("create table " + TB_NAME + "(" + UserInfo.ID
				+ " integer primary key," + UserInfo.EXPIRESTIME + "  varchar,"
				+ UserInfo.USERID + " varchar," + UserInfo.ACCESSTOKEN
				+ " varchar," + UserInfo.USERNAME + " varchar,"
				+ UserInfo.USERICON + " blob" + ")");
	}

	// 更新表
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
}
