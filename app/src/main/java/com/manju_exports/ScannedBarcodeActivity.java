package com.manju_exports;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.manju_exports.Entries.Checking_Entry_Act;
import com.manju_exports.Entries.Ironing_Entry_Act;
import com.manju_exports.Entries.Packing_Entry_Act;
import com.manju_exports.Entries.Re_Checking_Act;
import com.manju_exports.Entries.Sewing_Entry_Act;
import com.manju_exports.Fragments.Checking_Entry_Frag;
import com.manju_exports.Fragments.Ironing_Entry_Frag;
import com.manju_exports.Fragments.Packing_Entry_Frag;
import com.manju_exports.Fragments.Re_Checking_Entry_Frag;
import com.manju_exports.Fragments.Sewing_Frag;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ScannedBarcodeActivity extends AppCompatActivity {
    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;
    String intentData = "", str_entry_point_onclick_value = "", str_entry_point_onclick_value_others = "";
    boolean isEmail = false;

    RelativeLayout scanner_layout;
    Fragment fragment;
    FragmentTransaction ft;

    Vibrator vibrator;
    private boolean mAllowShake = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);
        getSupportActionBar().hide();
        scanner_layout = findViewById(R.id.scanner_layout);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            str_entry_point_onclick_value = null;
            str_entry_point_onclick_value_others = null;
        } else {
            str_entry_point_onclick_value = bundle.getString("Entry_Point_Onclick_Value");
            str_entry_point_onclick_value_others = bundle.getString("Entry_Point_Onclick_Value_Others");

            if (str_entry_point_onclick_value != null) {
                Log.e("kokoko", str_entry_point_onclick_value);
            }
            if (str_entry_point_onclick_value_others != null) {
                Log.e("entry_point_others", str_entry_point_onclick_value_others);
            }
        }

        initViews();
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction);


        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(ScannedBarcodeActivity.this, EmailActivity.class));
            }
        });
    }

    private void initialiseDetectorsAndSources() {
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScannedBarcodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @SuppressLint("LongLogTag")
            @Override
            public void release() {
//                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
                //This is for Sewing Fragment
                if (str_entry_point_onclick_value != null) {
                    Log.e("str_entry_point_onclick_value", str_entry_point_onclick_value);
                    if (str_entry_point_onclick_value.equals("1")) {
                        Sewing_Frag.constraintLayout_recyclerview.setVisibility(View.GONE);
                        Sewing_Frag.constraintLayout_qr_code_scan.setVisibility(View.GONE);
                        Sewing_Frag.fab.setVisibility(View.GONE);
                    }
                }
                if (str_entry_point_onclick_value_others != null) {
                    if (str_entry_point_onclick_value_others.equals("1")) {
                        Sewing_Frag.constraintLayout_recyclerview.setVisibility(View.GONE);
                        Sewing_Frag.fab.setVisibility(View.GONE);
                        Sewing_Frag.constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                        Sewing_Frag.btn_save_new.setVisibility(View.VISIBLE);
                    }
                }


                //This is for Checking_Entry_Fragment
                if (str_entry_point_onclick_value != null) {
                    if (str_entry_point_onclick_value.equals("2")) {
                        if (Checking_Entry_Frag.constraintLayout_checking_rv_layout.getVisibility() == View.GONE) {
                            Checking_Entry_Frag.constraintLayout_checking_rv_layout.setVisibility(View.VISIBLE);
                            Checking_Entry_Frag.fab_for_checking.setVisibility(View.VISIBLE);
                        }
                    }
                }
                if (str_entry_point_onclick_value_others != null) {
                    if (str_entry_point_onclick_value_others.equals("2")) {
                        Checking_Entry_Frag.constraintLayout_checking_rv_layout.setVisibility(View.GONE);
                        Checking_Entry_Frag.fab_for_checking.setVisibility(View.GONE);
                        Checking_Entry_Frag.constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                        Checking_Entry_Frag.btn_save_checking.setVisibility(View.VISIBLE);
                    }
                }


                //This is for Re_Checking_Entry_Fragment
                if (str_entry_point_onclick_value != null) {
                    if (str_entry_point_onclick_value.equals("3")) {
                        if (Re_Checking_Entry_Frag.constraintLayout_rechecking_rv_layout.getVisibility() == View.GONE) {
                            Re_Checking_Entry_Frag.constraintLayout_rechecking_rv_layout.setVisibility(View.VISIBLE);
                            Re_Checking_Entry_Frag.fab_for_rechecking.setVisibility(View.VISIBLE);
                        }
                    }
                }
                if (str_entry_point_onclick_value_others != null) {
                    if (str_entry_point_onclick_value_others.equals("3")) {
                        Re_Checking_Entry_Frag.constraintLayout_rechecking_rv_layout.setVisibility(View.GONE);
                        Re_Checking_Entry_Frag.fab_for_rechecking.setVisibility(View.GONE);
                        Re_Checking_Entry_Frag.constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                        Re_Checking_Entry_Frag.btn_save_rechecking.setVisibility(View.VISIBLE);
                    }
                }


                //This is for Ironing_Entry_Fragment
                if (str_entry_point_onclick_value != null) {
                    if (str_entry_point_onclick_value.equals("4")) {
                        if (Ironing_Entry_Frag.constraintLayout_recyclerview.getVisibility() == View.GONE) {
                            Ironing_Entry_Frag.constraintLayout_recyclerview.setVisibility(View.VISIBLE);
                            Ironing_Entry_Frag.fab_for_ironing.setVisibility(View.VISIBLE);
                        }
                    }
                }
                if (str_entry_point_onclick_value_others != null) {
                    if (str_entry_point_onclick_value_others.equals("4")) {
                        Ironing_Entry_Frag.constraintLayout_recyclerview.setVisibility(View.GONE);
                        Ironing_Entry_Frag.fab_for_ironing.setVisibility(View.GONE);
                        Ironing_Entry_Frag.constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                        Ironing_Entry_Frag.btn_save_ironing.setVisibility(View.VISIBLE);
                    }
                }


                //This is for Ironing_Entry_Fragment
                if (str_entry_point_onclick_value != null) {
                    if (str_entry_point_onclick_value.equals("5")) {
                        if (Packing_Entry_Frag.constraintLayout_packing_rv_layout.getVisibility() == View.GONE) {
                            Packing_Entry_Frag.constraintLayout_packing_rv_layout.setVisibility(View.VISIBLE);
                            Packing_Entry_Frag.fab_for_packing.setVisibility(View.VISIBLE);
                        }
                    }
                }
                if (str_entry_point_onclick_value_others != null) {
                    if (str_entry_point_onclick_value_others.equals("5")) {
                        Packing_Entry_Frag.constraintLayout_packing_rv_layout.setVisibility(View.GONE);
                        Packing_Entry_Frag.fab_for_packing.setVisibility(View.GONE);
                        Packing_Entry_Frag.constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                        Packing_Entry_Frag.btn_save_packing.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    txtBarcodeValue.post(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.Q)
                        @Override
                        public void run() {
                            intentData = barcodes.valueAt(0).displayValue;
                            txtBarcodeValue.setText(intentData);
                            Log.e("intentData", intentData);
                            Set<String> stringSet = new HashSet<>();
                            stringSet.add(txtBarcodeValue.getText().toString());
                            if (!(txtBarcodeValue.getText().toString().isEmpty())) {
                                if (str_entry_point_onclick_value != null) {
                                    if (str_entry_point_onclick_value.equals("1")) {
                                        Intent intent = new Intent(ScannedBarcodeActivity.this, Sewing_Entry_Act.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("Barcode_value", txtBarcodeValue.getText().toString());
                                        startActivity(intent);
                                        if (mAllowShake) {
                                            if (Build.VERSION.SDK_INT >= 26) {
                                                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_TICK));
                                            } else {
                                                vibrator.vibrate(50);
                                            }
                                        }
                                    } else if (str_entry_point_onclick_value.equals("2")) {
                                        Intent intent = new Intent(ScannedBarcodeActivity.this, Checking_Entry_Act.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("Barcode_value", txtBarcodeValue.getText().toString());
                                        SessionSave.SaveSession("Barcode_value_if_null", txtBarcodeValue.getText().toString(), ScannedBarcodeActivity.this);
                                        startActivity(intent);
                                        if (mAllowShake) {
                                            if (Build.VERSION.SDK_INT >= 26) {
                                                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_TICK));
                                            } else {
                                                vibrator.vibrate(50);
                                            }
                                        }
                                    } else if (str_entry_point_onclick_value.equals("3")) {
                                        Intent intent = new Intent(ScannedBarcodeActivity.this, Re_Checking_Act.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("Barcode_value", txtBarcodeValue.getText().toString());
                                        startActivity(intent);
                                        if (mAllowShake) {
                                            if (Build.VERSION.SDK_INT >= 26) {
                                                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_TICK));
                                            } else {
                                                vibrator.vibrate(50);
                                            }
                                        }
                                    } else if (str_entry_point_onclick_value.equals("4")) {
                                        Intent intent = new Intent(ScannedBarcodeActivity.this, Ironing_Entry_Act.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("Barcode_value", txtBarcodeValue.getText().toString());
                                        startActivity(intent);
                                        if (mAllowShake) {
                                            if (Build.VERSION.SDK_INT >= 26) {
                                                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_TICK));
                                            } else {
                                                vibrator.vibrate(50);
                                            }
                                        }
                                    } else if (str_entry_point_onclick_value.equals("5")) {
                                        Intent intent = new Intent(ScannedBarcodeActivity.this, Packing_Entry_Act.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("Barcode_value", txtBarcodeValue.getText().toString());
                                        startActivity(intent);

                                        if (mAllowShake) {
                                            if (Build.VERSION.SDK_INT >= 26) {
                                                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_TICK));
                                            } else {
                                                vibrator.vibrate(50);
                                            }
                                        }
                                    }
                                }
                                if (str_entry_point_onclick_value_others != null) {
                                    if (str_entry_point_onclick_value_others.equals("1")) {
                                        Intent intent = new Intent(ScannedBarcodeActivity.this, Sewing_Entry_Act.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("Barcode_value", txtBarcodeValue.getText().toString());
                                        intent.putExtra("Barcode_value_Others", "1");
                                        startActivity(intent);
                                        if (mAllowShake) {
                                            if (Build.VERSION.SDK_INT >= 26) {
                                                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_TICK));
                                            } else {
                                                vibrator.vibrate(50);
                                            }
                                        }
                                    } else if (str_entry_point_onclick_value_others.equals("2")) {
                                        Intent intent = new Intent(ScannedBarcodeActivity.this, Checking_Entry_Act.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("Barcode_value", txtBarcodeValue.getText().toString());
                                        intent.putExtra("Barcode_value_Others", "2");
                                        startActivity(intent);
                                        if (mAllowShake) {
                                            if (Build.VERSION.SDK_INT >= 26) {
                                                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_TICK));
                                            } else {
                                                vibrator.vibrate(50);
                                            }
                                        }
                                    } else if (str_entry_point_onclick_value_others.equals("3")) {
                                        Intent intent = new Intent(ScannedBarcodeActivity.this, Re_Checking_Act.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("Barcode_value", txtBarcodeValue.getText().toString());
                                        intent.putExtra("Barcode_value_Others", "3");
                                        startActivity(intent);
                                        if (mAllowShake) {
                                            if (Build.VERSION.SDK_INT >= 26) {
                                                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_TICK));
                                            } else {
                                                vibrator.vibrate(50);
                                            }
                                        }
                                    } else if (str_entry_point_onclick_value_others.equals("4")) {
                                        Intent intent = new Intent(ScannedBarcodeActivity.this, Ironing_Entry_Act.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("Barcode_value", txtBarcodeValue.getText().toString());
                                        intent.putExtra("Barcode_value_Others", "4");
                                        startActivity(intent);
                                        if (mAllowShake) {
                                            if (Build.VERSION.SDK_INT >= 26) {
                                                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_TICK));
                                            } else {
                                                vibrator.vibrate(50);
                                            }
                                        }
                                    } else if (str_entry_point_onclick_value_others.equals("5")) {
                                        Intent intent = new Intent(ScannedBarcodeActivity.this, Packing_Entry_Act.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("Barcode_value", txtBarcodeValue.getText().toString());
                                        intent.putExtra("Barcode_value_Others", "5");
                                        startActivity(intent);

                                        if (mAllowShake) {
                                            if (Build.VERSION.SDK_INT >= 26) {
                                                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_TICK));
                                            } else {
                                                vibrator.vibrate(50);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
        mAllowShake = false;
        vibrator.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
        mAllowShake = true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ScannedBarcodeActivity.this, Home_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

