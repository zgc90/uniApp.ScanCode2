package com.osmium.uniapp.scancodelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.taobao.weex.bridge.JSCallback;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Callback {

    private boolean hasSurface;
    public static JSCallback myCallback;
    private String backResult = "isFirst";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onStart(){
        super.onStart();

        if(backResult.equals("isFirst")){
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setCaptureActivity(MyCaptureActivity.class);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.PDF_417);  // 扫码类型
            integrator.setPrompt("将二维码/条形码放入框内，即可自动扫描");
            integrator.setOrientationLocked(true); //方向锁定，不可旋转
            integrator.setBeepEnabled(true); //扫码完成的提示音
            integrator.setBarcodeImageEnabled(false); //扫码成功时是否保存二维码图片
            integrator.initiateScan();
        }
    }

    /**
     * 从返回的信息里面找到扫描到的条码
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    if(ISO_8859_1)
                        contents = new String(contents.getBytes("ISO-8859-1"),"UTF-8");
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
            backResult = "Second";
            myCallback.invoke(map);
            finish();
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }
}
