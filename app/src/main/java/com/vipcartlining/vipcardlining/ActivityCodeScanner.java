package com.vipcartlining.vipcardlining;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.zxing.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Codex on 17.06.2015.
 */
public class ActivityCodeScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static Logger log = LoggerFactory.getLogger(ActivityCodeScanner.class);

    private ZXingScannerView mScannerView;
    public final static String RESULT_BARCODE = "RESULT_BARCODE";
    public String TAG = "BARCODEScn";

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view

        /*
        Test
        <!----------------
 */
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (getIntent().getBooleanExtra(LAUNCHED_FOR_RESULT, false)) {
//                    quit("6913657077940");
//                }
//                else{
//                    startActivityDetails("6913657077940");
//                }
//            }
//        },1000);

        //--->>>>///////////
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        /*Toast.makeText(this, "Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();
                */
        mScannerView.startCamera();
        Log.v(TAG, rawResult.getText()); // Prints scan results
        Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        quit(rawResult.getText());

    }

    private void quit(String barcode) {
        Intent intent = new Intent();
        intent.putExtra(RESULT_BARCODE, barcode);
        setResult(RESULT_OK, intent);
        finish();
    }

}
