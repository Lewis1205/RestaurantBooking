package com.itaem.administrator.restaurantbooking;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.itaem.administrator.restaurantbooking.utils.Utils;

public class WelcomeActivity extends AppCompatActivity {


    public final static int START_LOGIN = 1;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
            finish();

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        TextView textView = (TextView) findViewById(R.id.textView);
        Utils.beArtFront(this,textView);
        handler.sendEmptyMessageDelayed(START_LOGIN,5*1000);
    }
}
