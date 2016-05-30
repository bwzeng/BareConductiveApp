package com.example.zbw.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main extends AppCompatActivity {

    private Button ButtonGoDetector;
    private Button ButtonGoRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //綁定元件
        findViews();
        //設定文字
        setText();

        //事件監聽
        setListener();
    }

    private void setListener() {
        ButtonGoDetector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Main.this,DetectorActivity.class);
                startActivity(intent);
            }
        });
    }


    private void findViews() {
        ButtonGoDetector = (Button) findViewById(R.id.button_go_detector);
        ButtonGoRegister= (Button) findViewById(R.id.button_go_register);
    }

    private  void setText(){
        ButtonGoDetector.setText("ID辨識");
        ButtonGoRegister.setText("ID註冊");
    }
}
