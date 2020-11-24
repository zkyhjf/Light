package com.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private DBOpenHelper dbOpenHelper = null;                    //声明DBOpenHelper对象，并将其初始化为空
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        dbOpenHelper = new DBOpenHelper(SignUpActivity.this);    //实例化DBOpenHelper对象

        //分别获取账号编辑框、邮箱编辑框、两个密码编辑框、保存按钮、取消按钮组件
        EditText sign_up_id_text = findViewById(R.id.sign_up_id_text);
        EditText email_text = findViewById(R.id.email_text);
        EditText password_text1 = findViewById(R.id.password_text1);
        EditText password_text2 = findViewById(R.id.password_text2);
        Button save_bt = (Button)findViewById(R.id.save_bt);
        Button cancel_bt = (Button) findViewById(R.id.cancel_bt);

        //用数组保存获取到的编辑框组件
        EditText []editTexts = new EditText[4];
        editTexts[0]=sign_up_id_text;
        editTexts[1]=password_text1;
        editTexts[2]=password_text2;
        editTexts[3]=email_text;

        //实现对多个编辑框的输入监听，当都有输入时保存按钮变为可用状态，否则为不可用状态
        save_bt.setEnabled(false);
        for(EditText editText:editTexts) {
            editText.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (sign_up_id_text.length() > 0 && email_text.length() > 0 && password_text1.length() > 0 && password_text2.length()>0)
                        save_bt.setEnabled(true);
                    else save_bt.setEnabled(false);
                }

                public void afterTextChanged(Editable s) {
                }
            });
        }

        //实现对保存按钮单击事件的监听
        save_bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //获取用户输入的账号、邮箱以及两个密码
                String id = sign_up_id_text.getText().toString();
                String email = email_text.getText().toString();
                String password1 = password_text1.getText().toString();
                String password2 = password_text2.getText().toString();

                //执行数据库的查询操作， 并将查询到的数据保存在动态数组中
                Cursor cursor = dbOpenHelper.getReadableDatabase().query("account", null, "id=?", new String[]{id}, null, null, null);
                ArrayList<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
                Map<String, String> map = new HashMap<String, String>();
                while (cursor.moveToNext()) {
                    map.put("id", cursor.getString(cursor.getColumnIndex("id")));
                    resultList.add(map);
                }

                if(resultList.size() != 0) {          //查询到用户输入的账号，证明账号已存在
                    Toast.makeText(SignUpActivity.this, "账号已存在",Toast.LENGTH_SHORT).show();
                }
                else if(!password1.equals(password2)){
                    Toast.makeText(SignUpActivity.this, "密码两次输入不一致",Toast.LENGTH_SHORT).show();
                }
                else{
                    insertData(dbOpenHelper.getReadableDatabase(), id, email, password1);    //向数据库中插入账号等数据
                    Toast.makeText(SignUpActivity.this, "保存成功",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        //实现对取消按钮单击事件的监听，点击取消按钮则返回登录界面
        cancel_bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    //向数据库中插入数据
    private void insertData(SQLiteDatabase sqLiteDatabase, String id, String email, String password){
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("email", email);
        values.put("password", password);
        sqLiteDatabase.insert("account", null, values);
    }

    //关闭数据库连接
    protected void onDestroy(){
        super.onDestroy();
        if(dbOpenHelper != null){
            dbOpenHelper.close();
        }
    }
}