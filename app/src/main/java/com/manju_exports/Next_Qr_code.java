package com.manju_exports;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;
import com.manju_exports.Entries.Sewing_Entry_Act;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Next_Qr_code extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView zXingScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zXingScannerView = new ZXingScannerView(this);
        setContentView(zXingScannerView);

    }

    @Override
    public void handleResult(Result rawResult) {
//        QRCode_EDM_Youtube.textView.setText(rawResult.getText());
        Intent intent = new Intent(this, Sewing_Entry_Act.class);
        intent.putExtra("Barcode_value", rawResult.getText());
        startActivity(intent);
//        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }
}
