package com.xy.http;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            request();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void request() throws Exception {
        OkHttpWrapper okHttpWrapper=new OkHttpWrapper();
        String url="";
        Map<String,Object> params=new HashMap<>();
        params.put("userId",1);
        params.put("name","zhangsan");
        okHttpWrapper.postRequest(url,params);
    }

    public void asynRequest(){
        OkHttpWrapper okHttpWrapper=new OkHttpWrapper();
        String url="";
        Map<String,Object> params=new HashMap<>();
        params.put("userId",1);
        params.put("name","zhangsan");
        okHttpWrapper.postAsynsRequest(url, params, new CallbackSuccess() {
            @Override
            public void callback(JSONObject responseObject) {

            }
        }, new CallbackFail() {
            @Override
            public void callback(Exception e) {

            }
        });
    }

}
