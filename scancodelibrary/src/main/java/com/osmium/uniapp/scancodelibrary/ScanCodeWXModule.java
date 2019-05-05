package com.osmium.uniapp.scancodelibrary;

import android.app.Activity;
import android.content.Intent;

import com.google.zxing.integration.android.IntentIntegrator;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

public class ScanCodeWXModule extends WXSDKEngine.DestroyableModule {
    @JSMethod(uiThread = true)
    public void scanCode(String title, JSCallback jsCallback){
        MainActivity.myCallback = jsCallback;
        Intent intent = new Intent(mWXSDKInstance.getContext(), MainActivity.class);
        ((Activity)mWXSDKInstance.getContext()).startActivityForResult(intent, 9);

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
