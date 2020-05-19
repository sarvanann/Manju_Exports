package com.manju_exports;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QRCode_EDM_Youtube extends AppCompatActivity {
    Button btn_scan_qr_code;
    public static TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_code__e_d_m__youtube);
        btn_scan_qr_code = findViewById(R.id.btn_scan_qr_code);
        textView = findViewById(R.id.tv_scannser_camera_View_txt);

        btn_scan_qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QRCode_EDM_Youtube.this, Next_Qr_code.class);
                startActivity(intent);
            }
        });
    }
}
       /* Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        zXingScannerView.setResultHandler(QRCode_EDM_Youtube.this);
                        zXingScannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(QRCode_EDM_Youtube.this, "You must accept this permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }

    @Override
    protected void onDestroy() {
        zXingScannerView.stopCamera();
        super.onDestroy();
    }

    @Override
    public void handleResult(Result rawResult) {
        textView.setText(rawResult.getText());
        zXingScannerView.startCamera();
    }*/
