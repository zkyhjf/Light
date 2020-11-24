package com.example;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.com.newland.nle_sdk.requestEntity.SignIn;
import cn.com.newland.nle_sdk.responseEntity.User;
import cn.com.newland.nle_sdk.responseEntity.base.BaseResponseEntity;
import cn.com.newland.nle_sdk.util.NCallBack;
import cn.com.newland.nle_sdk.util.NetWorkBusiness;

public class LoginActivity extends AppCompatActivity {
    private DBOpenHelper dbOpenHelper = null;        //声明DBOpenHelper对象，并将其初始化为空
    private String id = null;                        //声明保存账号的字符串，并将其初始化为空
    private String password = null;                  //声明保存密码的字符串，并将其初始化为空
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        dbOpenHelper = new DBOpenHelper(LoginActivity.this);       //实例化DBOpenHelper对象

        //分别获取登录按钮、账号编辑框、密码编辑框、注册文本框组件
        Button login_bt = (Button)findViewById(R.id.login_bt);
        EditText login_id_text = (EditText)findViewById(R.id.login_id_text);
        EditText login_password_text = (EditText)findViewById(R.id.login_password_text);
        TextView sign_up_bt = (TextView) findViewById(R.id.sign_up_bt);

        //用数组保存获取到的编辑框组件
        EditText []editTexts = new EditText[2];
        editTexts[0]=login_id_text;
        editTexts[1]=login_password_text;

        //实现对多个编辑框的输入监听，当都有输入时登录按钮变为可用状态，否则为不可用状态
        login_bt.setEnabled(false);
        for(EditText editText:editTexts) {
            editText.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (login_id_text.length() > 0 && login_password_text.length() > 0)
                        login_bt.setEnabled(true);
                    else login_bt.setEnabled(false);
                }

                public void afterTextChanged(Editable s) {
                }
            });
        }

        //实现对登录按钮单击事件的监听，执行后续的登录操作
        login_bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                id = login_id_text.getText().toString();
                password = login_password_text.getText().toString();
                Login();
            }
        });

        //实现对注册文本框单击事件的监听，点击注册文本框则跳转到注册界面
        sign_up_bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    //登录云平台，登录成功获取AccessToken并弹出，失败则进行数据库登录
    private void Login() {
        String address = "http://api.nlecloud.com:80/";
        NetWorkBusiness netWorkBusiness = new NetWorkBusiness("", address);
        netWorkBusiness.signIn(new SignIn(id, password), new NCallBack<BaseResponseEntity<User>>(LoginActivity.this) {
            protected void onResponse(BaseResponseEntity<User> response) {
                if(response.getStatus() == 0){
                    String accessToken = response.getResultObj().getAccessToken();
                    Toast.makeText(LoginActivity.this, "AccessToken："+accessToken, Toast.LENGTH_LONG).show();
                }
                else db_Login();
            }
        });
    }

    //用数据库账号登录，登录成功则进入主界面
    private void db_Login(){

        //执行数据库的查询操作， 并将查询到的数据保存在动态数组中
        Cursor cursor = dbOpenHelper.getReadableDatabase().query("account", null, "id=?", new String[]{id}, null, null, null);
        ArrayList<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        while (cursor.moveToNext()) {
            map.put("id", cursor.getString(cursor.getColumnIndex("id")));
            map.put("password", cursor.getString(cursor.getColumnIndex("password")));
            resultList.add(map);
        }

        if (resultList.size() == 0) {            //查询不到用户输入的账号，证明用户不存在
            Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
        } else if (!password.equals((String) map.get("password"))) {
            Toast.makeText(LoginActivity.this, "账号密码错误", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    //关闭数据库连接
    protected void onDestroy(){
        super.onDestroy();
        if(dbOpenHelper != null){
            dbOpenHelper.close();
        }
    }
}