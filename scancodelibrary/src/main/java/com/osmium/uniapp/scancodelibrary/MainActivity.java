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
//            integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);  // 扫码类型
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
            Map<String, String> map = new HashMap<>();
            map.put("success", "true");
            map.put("scanType", intentResult.getFormatName());
            map.put("result", contents);
            backResult = contents;
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
