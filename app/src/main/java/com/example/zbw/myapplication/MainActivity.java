package com.example.zbw.myapplication;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //Timing
    int _second=0;
    private Handler mHandlerTime = new Handler();
    boolean isScanning =false;
    //private Button button;
    private TextView hint_text;
    private TextView message;
    private TextView time_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //初始化
        super.onCreate(savedInstanceState);

        //設置畫面
        setContentView(R.layout.activity_main);

        //綁定元件
        findViews();

        //設定文字
        setText();

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }

    @Override
    public void onDestroy()
    {
        mHandlerTime.removeCallbacks(timerRun);
        super.onDestroy();
    }

    private void findViews() {
        hint_text = (TextView) findViewById(R.id.textView);
        message = (TextView) findViewById(R.id.textView2);
        time_message = (TextView) findViewById(R.id.textView3);
    }

    private void setText() {
        hint_text.setText("Scan card here");
        message.setText("");
    }



        @Override
        public boolean onTouchEvent(MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_UP  ) {

                if(isScanning) return true;
                mHandlerTime.postDelayed(timerRun, 1000);
            } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
                int pointerNum = event.getPointerCount();
                _second=0;
                //String user_name =
               // message.setText("get " + pointerNum + " point(s)");
                time_message.setText(getDateTime());
                try {
                    String result = DBconnector.executeQuery("Select * from USER");
                    JSONArray jsonArray = new JSONArray(result);
                    String User_Name="";
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        User_Name = jsonData.getString("User_Name");
                    }
                    message.setText("Welcome "+User_Name);

                } catch(Exception e) {
                     Log.e("log_tag", e.toString());
                }
            }
            return true;

        }

        public String getDateTime() {
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            Date date = new Date();
            String strDate = sdFormat.format(date);
            return strDate;
        }

    private final Runnable timerRun = new Runnable()
    {
        public void run()
        {
            isScanning=true;

            ++_second; // 經過的秒數 + 1
            if(_second>5){
                message.setText("");
                _second=0;
                isScanning=false;
            }else {
                mHandlerTime.postDelayed(this, 1000);
            }
        }
    };
}