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
            new IntentIntegrator(this)
                    .initiateScan();
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
            backResult = contents;
            myCallback.invoke(backResult);
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
