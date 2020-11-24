package com.example;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBOpenHelper extends SQLiteOpenHelper {
    final String CREATE_TABLE_SQL = "create table account (id text primary key, email text, password text)";    //定义创建数据表的SQL语句
    public DBOpenHelper(Context context) {
        super(context, "user_db", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);    //创建个人账户的数据表
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
