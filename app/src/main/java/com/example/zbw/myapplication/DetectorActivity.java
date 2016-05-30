package com.example.zbw.myapplication;

import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetectorActivity extends AppCompatActivity {

    //Timing
    int _second = 0;
    boolean isScanning = false;
    boolean screenLock = false;  //螢幕按下，禁止其他感應
    private Handler mHandlerTime = new Handler();
    //private Button button;
    private TextView hint_text;
    private TextView message;
    double startTime;
    double endTime;

    private Handler timerHandler = new Handler();

    private final Runnable timerRun = new Runnable() {
        public void run() {
            isScanning = true;

            ++_second; // 經過的秒數 + 1
            if (_second > 3) {
                message.setText("");
                _second = 0;
                isScanning = false;
            } else {
                mHandlerTime.postDelayed(this, 1000);
            }
        }
    };

    private TextView time_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //初始化
        super.onCreate(savedInstanceState);

        //設置畫面
        setContentView(R.layout.activity_detector);

        //綁定元件
        findViews();

        //設定文字
        setText();

        //設定定時要執行的方法
        timerHandler.removeCallbacks(updateTimer);
        //設定Delay的時間
        timerHandler.postDelayed(updateTimer, 500);

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
    public void onDestroy() {
        mHandlerTime.removeCallbacks(timerRun);
        super.onDestroy();
    }

    //設置畫面
    private void findViews() {
        hint_text = (TextView) findViewById(R.id.textView);
        message = (TextView) findViewById(R.id.textView2);
        time_message = (TextView) findViewById(R.id.textView3);
    }

    //設定文字
    private void setText() {
        hint_text.setText("Scan card here");
        message.setText("");
        time_message.setText(getDateTime());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startTime = event.getDownTime(); //按下開始時間
        endTime = event.getEventTime(); // 事件結束時間
        Log.i("startTime", Double.toString(startTime));
        Log.i("endTime", Double.toString(endTime));

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_UP:
                // 最後一個點離開

                Log.i("ACTION_UP", "ACTION_UP");
                if (isScanning) return true;    //如果前一次的讀卡還在執行中，則阻止感應
                mHandlerTime.postDelayed(timerRun, 1000);
                break;

            case MotionEvent.ACTION_DOWN:
                //第一個點按下


                Log.i("ACTION_DOWN", "ACTION_DOWN");
                //time_message.setText(getDateTime());
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                //當螢幕上已有一個點，此時再按下其他點

                Log.i("ACTION_POINTER_DOWN", "ACTION_POINTER_DOWN");
                if (!isScanning) {
                    _second = 0;
                    int pointerNum = event.getPointerCount();   //觸控點數量
                    Log.i("pointerNum", Integer.toString(pointerNum));
                    String userName;
                    userName = executeQuery("SELECT * FROM `user` WHERE `Nodes` = " + pointerNum);
                    message.setText("welcome " + userName);
                }

                break;
        }

        return true;
    }

    //固定要執行的方法
    private Runnable updateTimer = new Runnable() {
        public void run() {
            timerHandler.postDelayed(this,500);
            time_message.setText(getDateTime());
        }
    };

    //取得現在時間
    public String getDateTime() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        Date date = new Date();
        String strDate = sdFormat.format(date);
        return strDate;
    }

    //執行SQL
    public String executeQuery(String query) {
        try {
            Log.i("query", query);
            String jresult = DBconnector.executeQuery(query);
            JSONArray jsonArray = new JSONArray(jresult);
            String firstResult;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                firstResult = jsonData.getString("User_Name");
                return firstResult;
            }

        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }
        return null;
    }
}