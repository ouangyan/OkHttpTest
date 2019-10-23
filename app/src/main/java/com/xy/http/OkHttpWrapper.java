package com.xy.http;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Ouyang on 2019/10/23.
 */

public class OkHttpWrapper {
    private OkHttpClient okHttpClient=new OkHttpClient();
    private Callback emptyCallback=new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {

        }
    };

    /**
     * 同步请求
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public String postRequest(String url,Map<String,Object> params) throws Exception {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            for (String keyTemp : params.keySet()) {
                String valueTemp = String.valueOf(params.get(keyTemp));
                builder.add(keyTemp, valueTemp);
            }
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).method("POST", requestBody).build();
        Response response=null;
        try {
            response = this.okHttpClient.newCall(request).execute();
            String responseString = response.body().string();
            return responseString;
        }catch (IOException e){
            throw new Exception(e);
        }finally {
            if(response!=null)
                response.close();
        }
    }

    /**
     * 异步请求
     * @param url
     * @param params
     * @param callbackSuccess
     * @param callbackFail
     */
    public void postAsynsRequest(String url, Map<String,Object> params, CallbackSuccess callbackSuccess,CallbackFail callbackFail){
        Callback callback=new InternalCallback(callbackSuccess,callbackFail);

        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            for (String keyTemp : params.keySet()) {
                String valueTemp = String.valueOf(params.get(keyTemp));
                builder.add(keyTemp, valueTemp);
            }
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).method("POST", requestBody).build();
        Call call = this.okHttpClient.newCall(request);
        if(callback==null){
            call.enqueue(emptyCallback);
        }else{
            call.enqueue(callback);
        }
    }

    static class InternalCallback implements Callback{
        private CallbackSuccess callbackSuccess;
        private CallbackFail callbackFail;
        public InternalCallback(CallbackSuccess callbackSuccess,CallbackFail callbackFail){
            this.callbackSuccess=callbackSuccess;
            this.callbackFail=callbackFail;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            callbackFail.callback(e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            JSONObject responseObject=null;
            try {
                responseObject=new JSONObject(response.body().string());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            callbackSuccess.callback(responseObject);
        }
    }

}
