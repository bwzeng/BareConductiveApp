package com.example.zbw.myapplication;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class register extends AppCompatActivity {

    //Timing
    int _second = 0;
    boolean isScanning = false;
    boolean screenLock = false;  //螢幕按下，禁止其他感應
    private Handler mHandlerTime = new Handler();
    //private Button button;
    //private TextView hint_text;
    //private TextView message;
    private TextView nameText ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //初始化
        super.onCreate(savedInstanceState);

        //設置畫面
        setContentView(R.layout.activity_register);

        //綁定元件
        findViews();

        //設定文字
        setText();

    }

    private void findViews() {
        nameText = (TextView) findViewById(R.id.editText);

    }

    private void setText() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerNum = 0;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_UP:
                // 最後一個點離開

                Log.i("ACTION_UP", "ACTION_UP");
                if (isScanning) return true;    //如果前一次的讀卡還在執行中，則阻止感應
                mHandlerTime.postDelayed(timerRun, 1000);
                break;

            case MotionEvent.ACTION_DOWN:
                //第一個點按下

                screenLock = true;
                Log.i("ACTION_DOWN", "ACTION_DOWN");
                //time_message.setText(getDateTime());
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                //當螢幕上已有一個點，此時再按下其他點
                Log.i("ACTION_POINTER_DOWN", "ACTION_POINTER_DOWN");
                _second = 0;
                pointerNum = event.getPointerCount();   //觸控點數量
                Log.i("pointerNum", Integer.toString(pointerNum));
                //String userName;

                //userName = executeQuery("SELECT * FROM `user` WHERE `Nodes` = "+pointerNum);
                //INSERT INTO `user` (`UserId`, `User_Name`, `Register_Time`, `Nodes`, `Distance`) VALUES (NULL, 'JzKUO', CURRENT_TIMESTAMP, '1', '');
                //message.setText("welcome "+userName);
                break;
        }
        if(pointerNum != 0) {
            executeQuery("INSERT INTO `user` (`UserId`, `User_Name`, `Register_Time`, `Nodes`, `Distance`) VALUES (NULL, '" + nameText.getText() + "', CURRENT_TIMESTAMP, '" + pointerNum + "', '');");
        }
        return true;
    }

    public String executeQuery(String query) {
        try {
            Log.i("query",query);
             //= DBconnector.executeQuery(query);
            /*String jresult = DBconnector.executeQuery(query);
            JSONArray jsonArray = new JSONArray(jresult);
            String firstResult;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                firstResult = jsonData.getString("User_Name");
                return firstResult;
            }*/

        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }
        return null;
    }

    private final Runnable timerRun = new Runnable() {
        public void run() {
            isScanning = true;

            ++_second; // 經過的秒數 + 1
            if (_second > 5) {
                //message.setText("");
                _second = 0;
                isScanning = false;
            } else {
                mHandlerTime.postDelayed(this, 1000);
            }
        }
    };
}