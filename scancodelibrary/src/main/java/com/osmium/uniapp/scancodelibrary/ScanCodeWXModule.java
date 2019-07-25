package com.osmium.uniapp.scancodelibrary;

import android.app.Activity;
import android.content.Intent;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ScanCodeWXModule extends WXSDKEngine.DestroyableModule {
    private JSCallback myCallback;

    @JSMethod(uiThread = true)
    public void scanCode(JSONObject options, JSCallback jsCallback){
        myCallback = jsCallback;
        scanUseZxing(options);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        //通过IntentIntegrator.parseActivityResult 来获取结果
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(intentResult!=null){
            //获取扫描到的数据
            String contents = intentResult.getContents();
            String scanType = intentResult.getFormatName();
            Map<String, String> map = new HashMap<>();
            if(contents != null && scanType != null){
                try{
                    boolean ISO_8859_1 = Charset.forName("ISO-8859-1").newEncoder().canEncode(contents);
                    if(ISO_8859_1){
//                        contents = new String(contents.getBytes("ISO-8859-1"),"UTF-8");
                        contents = getChinese(contents);
                    }
                }catch (Exception e) {

                }
                map.put("success", "true");
                map.put("scanType", scanType);
                map.put("result", contents);
            }else{
                map.put("success", "false");
                map.put("scanType", "");
                map.put("result", "用户取消");
            }
            myCallback.invoke(map);
        }
    }

    @Override
    public void destroy() {

    }

    private void scanUseZxing(JSONObject options){
        if(mWXSDKInstance.getContext() instanceof Activity){
            Map<String, String> map = new HashMap<>();
            try{
                String[] scanType = {};//扫码类型
                Object prompt = "将二维码/条形码放入框内，即可自动扫描";//扫码提示语
                Object locked = true;//方向锁定，不可旋转
                Object beepEnabled = true;//扫码完成的提示音
                Object imageEnabled = false;//扫码成功时是否保存二维码图片
                Object cameraId = 0; //使用指定的相机ID
                if(options.size() > 0){
                    Object scanTypeObj = options.get("scanType");
                    if(scanTypeObj != null){
                        JSONArray objects = (JSONArray)scanTypeObj;
                        scanType = new String[objects.size()];
                        if(objects.size() > 0){
                            for(int i=0; i<objects.size(); i++){
                                scanType[i] = objects.get(i).toString();
                            }
                        }
                    }
                    prompt = options.get("prompt") == null ? prompt : options.get("prompt");
                    locked = options.get("locked") == null ? locked : options.get("locked");
                    beepEnabled = options.get("beepEnabled") == null ? beepEnabled : options.get("beepEnabled");
                    imageEnabled = options.get("imageEnabled") == null ? imageEnabled : options.get("imageEnabled");
                    cameraId = options.get("cameraId") == null ? cameraId : options.get("cameraId");
                }

                IntentIntegrator integrator = new IntentIntegrator((Activity)mWXSDKInstance.getContext());
                integrator.setCaptureActivity(MyCaptureActivity.class); //扫码界面及其他处理
                if(scanType.length > 0){
                    integrator.setDesiredBarcodeFormats(scanType);  // 扫码类型
                }
                integrator.setPrompt(prompt.toString()); //屏幕提示语
                integrator.setOrientationLocked((boolean)locked); //方向锁定，不可旋转
                integrator.setBeepEnabled((boolean)beepEnabled); //扫码完成的提示音
                integrator.setBarcodeImageEnabled((boolean)imageEnabled); //扫码成功时是否保存二维码图片
                integrator.setCameraId((int)cameraId); //使用指定的相机ID
                integrator.initiateScan(); //启动扫描
            }catch (Exception e){
                map.put("success", "false");
                map.put("scanType", "");
                map.put("result", e.getMessage());
                myCallback.invoke(map);
            }
        }
    }

    //编码转换 UTF-8/GBK
    private static String getChinese(String contents){
        String result = "";
        if(!contents.equals("")){
            try{//先尝试转换为UTF-8
                result = new String(contents.getBytes("ISO-8859-1"),"UTF-8");
            }catch (Exception e){

            }

            if(!isChineseByREG(result)){
                try{//尝试转换为GBK
                    result = new String(contents.getBytes("ISO-8859-1"),"GBK");
                }catch (Exception e){

                }
            }
        }
        return result;
    }

    //正则判断是否有中文
    private static boolean isChineseByREG(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
        return pattern.matcher(str.trim()).find();
    }

}
