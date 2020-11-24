package com.example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //分别获取开关按钮、亮灯图片、灭灯图片、退出按钮组件
        ToggleButton toggle_bt = (ToggleButton) findViewById(R.id.light_on_off);
        ImageView light_on = (ImageView)findViewById(R.id.light_on);
        ImageView light_off = (ImageView)findViewById(R.id.light_off);
        Button exit_bt = (Button)findViewById(R.id.exit_bt);

        //实现对开关按钮状态改变事件的监听
        toggle_bt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                /*当开关按钮处于选中状态，显示亮灯图片，不显示灭灯图片，
                  否则显示灭灯图片，不显示亮灯图片，实现按钮对灯泡亮灭的控制
                 **/
                if(isChecked){
                    light_on.setVisibility(View.VISIBLE);
                    light_off.setVisibility(View.INVISIBLE);
                }
                else{
                    light_off.setVisibility(View.VISIBLE);
                    light_on.setVisibility(View.INVISIBLE);
                }
            }
        });

        //实现对退出按钮单击事件的监听，点击退出按钮则退出主界面
        exit_bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}