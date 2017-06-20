package com.itaem.administrator.restaurantbooking;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.Toast;


import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;



public class LoginActivity extends AppCompatActivity {

    private EditText et_phone;
    private EditText et_verificationCode;
    private ImageView iv_delete;
    private Button bt_getVC;

    private Context context;

    //倒计时设置器
    CountDownTimer timer = new CountDownTimer(60000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            bt_getVC.setText(millisUntilFinished/1000+"秒");
        }

        @Override
        public void onFinish(){
            if (et_phone.getText().toString().length()==11){
                bt_getVC.setEnabled(true);
            }else {
                bt_getVC.setEnabled(false);
            }
            bt_getVC.setText("获取验证码");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SMSSDK.initSDK(this,"1d31cb6a08ff4","30c959fd71165c773295875d949dafc4");
        setContentView(R.layout.activity_login);

        context = this;


        init();//初始化控件

        //设置监听
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>=1){//

                    iv_delete.setVisibility(View.VISIBLE);
                    if (s.length()==11){
                        bt_getVC.setEnabled(true);
                    }else {
                        bt_getVC.setEnabled(false);
                    }
                }else {
                    iv_delete.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_phone.setText("");
                bt_getVC.setEnabled(false);
            }
        });

        initSMS();//初始化SMS配置
    }


    private void initSMS() {

        EventHandler eventHandler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                super.afterEvent(event, result, data);
                    try {
                        Throwable throwable = (Throwable) data;
                        throwable.printStackTrace();
                        final JSONObject object = new JSONObject(throwable.getMessage());
                        final String des = object.optString("detail");//错误描述
                        final int status = object.optInt("status");//错误代码

                        if (status > 0 && !TextUtils.isEmpty(des)) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, des, Toast.LENGTH_LONG).show();
                                }
                            });
                            return;
                        }

                        //后台校验手机
                        String phone = et_phone.getText().toString();
                        isPhoneOk(phone);

                        }catch(Exception e){
                            e.printStackTrace();
                        }

            }
        };
        SMSSDK.registerEventHandler(eventHandler);

    }

    private void init() {
        //初始化控件
        bt_getVC = (Button) findViewById(R.id.bt_getVC);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_verificationCode = (EditText) findViewById(R.id.et_VerificationCode);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);

        iv_delete.setVisibility(View.INVISIBLE);
    }


    public void login(View view){//登陆
        String phone = et_phone.getText().toString();
        String verificationCode = et_verificationCode.getText().toString();
        SMSSDK.submitVerificationCode("86",phone,verificationCode);

    }

    public void getVerificationCode(View view){

        String phone = et_phone.getText().toString();
        SMSSDK.getVerificationCode("86",phone); //获得验证码
        //按钮变成不可按并且倒计时
        bt_getVC.setEnabled(false);
        timer.start();

    }


    /*检验手机是否已经注册*/
    public void isPhoneOk(String phone){

        OkHttpUtils.post()
                .url("http://192.168.2.159/booking_system/login.action")
                .addParams("phone_num",phone)
                .build()
                .connTimeOut(5*1000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(context,"网络异常",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        String result;
                        try{
                            JSONObject object = new JSONObject(s);
                            result = object.getString("tip");
                            if (result.equals("success")){
                                startActivity(new Intent(context,MainActivity.class));
                                finish();
                            }else {
                                Toast.makeText(context,"",Toast.LENGTH_SHORT).show();
                            }

                        }catch (Exception e){
                            Toast.makeText(context,"系统异常",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }



}
