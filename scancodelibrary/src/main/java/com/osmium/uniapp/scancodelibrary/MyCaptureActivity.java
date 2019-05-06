package com.osmium.uniapp.scancodelibrary;

import android.content.pm.PackageManager;
import android.view.View;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.Collection;

public class MyCaptureActivity extends CaptureActivity {

    @Override
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.my_capture_activity);
        return (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scanner);
    }

    public void OnClick(View v){
        if(R.id.ivLeft == v.getId()){
            onBackPressed();
        }
//        switch (v.getId()){
//            case R.id.ivLeft:
//                onBackPressed();
//                break;
//        }
    }

    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }
}
