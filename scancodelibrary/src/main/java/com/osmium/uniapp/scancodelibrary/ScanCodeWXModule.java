package com.osmium.uniapp.scancodelibrary;

import android.app.Activity;
import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

import java.util.HashMap;
import java.util.Map;

public class ScanCodeWXModule extends WXSDKEngine.DestroyableModule {
    @JSMethod(uiThread = true)
    public void scanCode(JSONObject options, JSCallback jsCallback){
        try{
//            String scanType = options.getString("scanType");

            MainActivity.myCallback = jsCallback;
            Intent intent = new Intent(mWXSDKInstance.getContext(), MainActivity.class);
            ((Activity)mWXSDKInstance.getContext()).startActivityForResult(intent, 9);
        }catch (Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("success", "false");
            map.put("result", e.getMessage());
            jsCallback.invoke(map);
        }
    }

    @JSMethod(uiThread = true)
    public void scanCode(JSCallback jsCallback){
        try{
            MainActivity.myCallback = jsCallback;
            Intent intent = new Intent(mWXSDKInstance.getContext(), MainActivity.class);
            ((Activity)mWXSDKInstance.getContext()).startActivityForResult(intent, 9);
        }catch (Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("success", "false");
            map.put("result", e.getMessage());
            jsCallback.invoke(map);
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//        super.onActivityResult(requestCode, resultCode, data);
//        String stringBuffer = data.getStringExtra("scanResult");;
//        Map<String,Object> params = new HashMap<>();
//        params.put("result",stringBuffer);
//        mWXSDKInstance.fireGlobalEventCallback("scanResult",params);
//        System.out.print(requestCode);
//    }

    @Override
    public void destroy() {

    }

}
