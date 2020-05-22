package com.manju_exports;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;
import com.manju_exports.Entries.Sewing_Entry_Act;
import com.manju_exports.Fragments.Sewing_Frag;

import java.util.Objects;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Next_Qr_code extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView zXingScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zXingScannerView = new ZXingScannerView(this);
        setContentView(zXingScannerView);
        Objects.requireNonNull(getSupportActionBar()).hide();
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

    @Override
    public void onBackPressed() {
//        Toast.makeText(this, "sdfgvbnnasdfgvhg", Toast.LENGTH_SHORT).show();
        Sewing_Frag.constraintLayout_recyclerview.setVisibility(View.VISIBLE);
        Sewing_Frag.constraintLayout_sewing_rv_layout.setVisibility(View.VISIBLE);
        Sewing_Frag.scroll_view_layout.setVisibility(View.GONE);
        Sewing_Frag.constraintLayout_inside_io_number_color.setVisibility(View.GONE);
        Sewing_Frag.constraintLayout_qr_code_scan.setVisibility(View.GONE);
        Sewing_Frag.constraintLayout_opertions.setVisibility(View.GONE);
        Sewing_Frag.constraintLayout_inside_api_values.setVisibility(View.GONE);
        Sewing_Frag.fab.setVisibility(View.VISIBLE);
        super.onBackPressed();
    }
}
