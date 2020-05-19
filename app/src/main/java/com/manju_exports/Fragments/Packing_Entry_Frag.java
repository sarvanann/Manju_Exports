package com.manju_exports.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.Result;
import com.manju_exports.Entries.Packing_Entry_Act;
import com.manju_exports.Home_Activity;
import com.manju_exports.Interface.APIInterface;
import com.manju_exports.Interface.Factory;
import com.manju_exports.Model.Full_App_Model_Class;
import com.manju_exports.Model.Operator_Model;
import com.manju_exports.R;
import com.manju_exports.ScannedBarcodeActivity;
import com.manju_exports.SessionSave;
import com.manju_exports.Toast_Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import io.supercharge.shimmerlayout.ShimmerLayout;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;

public class Packing_Entry_Frag extends Fragment implements View.OnClickListener, ZXingScannerView.ResultHandler {
    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;

    public static ConstraintLayout constraintLayout_packing_rv_layout,
            constraintLayout_qr_code_scan;
    private TextView textViewName,
            textViewAddress;
    private TextView tv_io_number_normal_txt, tv_onclick_io_number_txt;
    private TextView tv_color_normal_txt, tv_onclick_color_txt;
    private TextView tv_normal_checking_person_txt, tv_onclick_checking_person_txt;
    private TextView tv_normal_select_piece, tv_onclick_select_piece;
    private TextView tv_select_shift, tv_onclick_shift;

    public static Button btn_save_packing;
    private Dialog dialog_for_io_number,
            dialog_for_colors,
            dialog_for_check_person,
            dialog_for_pieces,
            dialog_for_shift_timing,
            dialog_for_operation;
    private Handler handler;
    private String str_session_username,
            str_session_logintoken,
            str_session_total_pieces,
            str_session_io_number,
            str_session_color_pick,
            str_session_checking_person,
            str_session_shift_timing,
            str_session_operation;
    RecyclerView rv_select_operation;
    private RecyclerView rv_io_number;
    private RecyclerView rv_colors;
    private RecyclerView rv_checking_person;
    RecyclerView rv_entry_pieces;
    private RecyclerView rv_shift_timing;
    public static FloatingActionButton fab_for_packing;

    private ConstraintLayout
            constraintLayout_io_number_layout,
            constraintLayout_color_layout,
            constraintLayout_checking_person_layout,
            constraintLayout_pieces_layout,
            constraintLayout_select_operation,
            constraintLayout_select_shift;

    private ArrayList<String> ionumber_ArrayList = new ArrayList<>();
    private ArrayList<String> color_ArrayList = new ArrayList<>();
    private ArrayList<String> checking_person_ArrayList = new ArrayList<>();
    private ArrayList<String> selectshift_ArrayList = new ArrayList<>();

    private Shift_Timing_Adapter_For_Packing shift_timing_adapter_for_packing;
    private Io_Number_Adapter_For_Packing io_number_adapter_for_packing;
    private Color_Adapter_For_Packing color_adapter_for_packing;
    private Checking_Person_Adapter_For_Packing checking_person_adapter_for_packing;
    private Operation_Select_Adapter operation_select_adapter;
    TextView tv_onclick_operation_for_packing, tv_select_operation_for_packing;

    EditText et_num_from_pieces_dialog;
    TextView tv_ok_from_pieces_dialog;
    String str_num_of_pieces;

    private String str_qrcode_value = "";
    private String str_qrcode_value_others = "";
    String str_address, str_name;


    RelativeLayout scanner_layout;
    SurfaceView surfaceView;
    TextView textViewBarCodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    String intentData = "";

    RecyclerView rv_packing;
    ScrollView scroll_view_layout;
    private String
            str_io_num,
            str_art_num,
            str_date,
            str_color_code,
            str_bundle_num,
            str_size,
            str_lot_num,
            str_total_pieces;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("LongLogTag")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        scannerView = new ZXingScannerView(getActivity());
        final View view = inflater.inflate(R.layout.packing_entry_frag_lay, container, false);
        Home_Activity.tv_title_txt.setText(getResources().getString(R.string.packing_txt));
        //This is used for Full screen
//        Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Home_Activity.toolbar.setBackgroundColor(getResources().getColor(R.color.light_red_color));
        tv_io_number_normal_txt = view.findViewById(R.id.tv_io_number_normal_txt);
        tv_color_normal_txt = view.findViewById(R.id.tv_color_normal_txt);
        tv_normal_checking_person_txt = view.findViewById(R.id.tv_normal_checking_person_txt);
        tv_normal_select_piece = view.findViewById(R.id.tv_normal_select_piece);
        tv_select_shift = view.findViewById(R.id.tv_select_shift);
        rv_packing = view.findViewById(R.id.rv_packing);
        scroll_view_layout = view.findViewById(R.id.scroll_view_layout);
        rv_packing.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_packing.setHasFixedSize(true);

        scanner_layout = view.findViewById(R.id.scanner_layout);
        textViewBarCodeValue = view.findViewById(R.id.txtBarcodeValue);
        surfaceView = view.findViewById(R.id.surfaceView);


        ShimmerLayout shimmer_for_packing = view.findViewById(R.id.shimmer_for_packing);
        shimmer_for_packing.setVisibility(View.VISIBLE);
        shimmer_for_packing.startShimmerAnimation();

        tv_onclick_io_number_txt = view.findViewById(R.id.tv_onclick_io_number_txt);
        tv_onclick_color_txt = view.findViewById(R.id.tv_onclick_color_txt);
        tv_onclick_checking_person_txt = view.findViewById(R.id.tv_onclick_checking_person_txt);
        tv_onclick_select_piece = view.findViewById(R.id.tv_onclick_select_piece);
        tv_select_shift = view.findViewById(R.id.tv_select_shift);
        tv_onclick_shift = view.findViewById(R.id.tv_onclick_shift);

        fab_for_packing = view.findViewById(R.id.fab_for_packing);
        btn_save_packing = view.findViewById(R.id.btn_save_packing);
        constraintLayout_qr_code_scan = view.findViewById(R.id.constraintLayout_qr_code_scan);
        constraintLayout_packing_rv_layout = view.findViewById(R.id.constraintLayout_packing_rv_layout);
        tv_select_operation_for_packing = view.findViewById(R.id.tv_select_operation_for_packing);
        tv_onclick_operation_for_packing = view.findViewById(R.id.tv_onclick_operation_for_packing);
        constraintLayout_qr_code_scan = view.findViewById(R.id.constraintLayout_qr_code_scan);

        constraintLayout_io_number_layout = view.findViewById(R.id.constraintLayout_io_number_layout);
        constraintLayout_color_layout = view.findViewById(R.id.constraintLayout_color_layout);
        constraintLayout_checking_person_layout = view.findViewById(R.id.constraintLayout_checking_person_layout);
        constraintLayout_pieces_layout = view.findViewById(R.id.constraintLayout_pieces_layout);
        constraintLayout_select_operation = view.findViewById(R.id.constraintLayout_select_operation);
        constraintLayout_select_shift = view.findViewById(R.id.constraintLayout_select_shift);

        constraintLayout_io_number_layout.setOnClickListener(this);
        constraintLayout_color_layout.setOnClickListener(this);
        constraintLayout_checking_person_layout.setOnClickListener(this);
        constraintLayout_pieces_layout.setOnClickListener(this);
        constraintLayout_select_operation.setOnClickListener(this);
        constraintLayout_select_shift.setOnClickListener(this);
        fab_for_packing.setOnClickListener(this);
        btn_save_packing.setOnClickListener(this);

        str_session_username = SessionSave.getSession("Session_UserName", getActivity());
        str_session_logintoken = SessionSave.getSession("Session_Logintoken", getActivity());
        str_session_io_number = SessionSave.getSession("Session_IO_Number_Packing", getActivity());
        str_session_color_pick = SessionSave.getSession("Session_Color_Pick_Packing", getActivity());
        str_session_checking_person = SessionSave.getSession("Session_Checking_Person_Packing", getActivity());
        str_session_total_pieces = SessionSave.getSession("Session_Total_Pieces_Packing", getActivity());
        str_session_shift_timing = SessionSave.getSession("Session_Shift_Timing_Packing", getActivity());
        str_session_operation = SessionSave.getSession("Session_Operation_Packing", getActivity());

        Bundle bundle = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        if (bundle == null) {
            str_qrcode_value = null;
        } else {
            str_qrcode_value = bundle.getString("Barcode_value");
            Log.e("main_bundle_value", str_qrcode_value);
            Set<String> stringSet = new HashSet<>();
            stringSet.add(str_qrcode_value);
            JSONObject jsonObject = null;
            Log.e("main_stringSet", stringSet.toString());
            try {
                jsonObject = new JSONObject(str_qrcode_value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < Objects.requireNonNull(jsonObject).length(); i++) {
                try {
                    str_address = jsonObject.getString("qr_no");
                    str_name = jsonObject.getString("Name");
                    Log.e("str_address", str_address);
                    Log.e("str_name", str_name);
                    constraintLayout_packing_rv_layout.setVisibility(View.GONE);
                    constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                    fab_for_packing.setVisibility(View.GONE);
                    btn_save_packing.setVisibility(View.VISIBLE);
                    tv_normal_checking_person_txt.setText("");
                    tv_onclick_checking_person_txt.setText(str_name);
                    SessionSave.SaveSession("Session_Checking_Person_Packing", tv_onclick_checking_person_txt.getText().toString(), getActivity());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if ((tv_onclick_io_number_txt.getText().toString().equals(""))
                || (tv_onclick_color_txt.getText().toString().equals(""))
                || (tv_onclick_checking_person_txt.getText().toString().equals(""))
                || (tv_onclick_select_piece.getText().toString().equals(""))
                || tv_onclick_operation_for_packing.getText().toString().equals("")) {

//            Log.e("tv_onclick_io_number_txt", tv_onclick_io_number_txt.getText().toString());
//            Log.e("tv_onclick_color_txt", tv_onclick_color_txt.getText().toString());
//            Log.e("tv_onclick_rechecking_person_txt", tv_onclick_checking_person_txt.getText().toString());
//            Log.e("tv_total_Pieces", tv_onclick_select_piece.getText().toString());
//            Log.e("tv_onclick_operation_for_packing", tv_onclick_operation_for_packing.getText().toString());
//            Log.e("tv_onclick_shift", tv_onclick_shift.getText().toString());


            tv_select_shift.setText(R.string.select_operation_txt);
            tv_onclick_shift.setText("");
            btn_save_packing.setEnabled(false);
            btn_save_packing.setBackground(getResources().getDrawable(R.drawable.disable_button_bg));
        }
        if (!(str_session_io_number.equalsIgnoreCase("No data"))
                && !(str_session_color_pick.equalsIgnoreCase("No data"))
                && !(str_session_checking_person.equalsIgnoreCase("No data"))
                && !(str_session_total_pieces.equalsIgnoreCase("No data"))
                && !(str_session_shift_timing.equalsIgnoreCase("No data"))) {

            Log.e("str_session_io_number", str_session_io_number);
            Log.e("str_session_color_pick", str_session_color_pick);
            Log.e("str_session_checking_person", str_session_checking_person);
            Log.e("str_session_total_pieces", str_session_total_pieces);
            Log.e("str_session_shift_timing", str_session_shift_timing);


           /* if (!(tv_onclick_io_number_txt.getText().toString().equals(""))
                    && !(tv_onclick_color_txt.getText().toString().equals(""))
                    && !(tv_onclick_checking_person_txt.getText().toString().equals(""))
                    && !(tv_onclick_select_piece.getText().toString().equals(""))
                    && !(tv_onclick_operation_for_packing.getText().toString().equals(""))
                    && !(tv_onclick_shift.getText().toString().equals(""))) {*/

            Log.e("tv_onclick_io_number_txt", tv_onclick_io_number_txt.getText().toString());
            Log.e("tv_onclick_color_txt", tv_onclick_color_txt.getText().toString());
            Log.e("tv_onclick_rechecking_person_txt", tv_onclick_checking_person_txt.getText().toString());
            Log.e("tv_total_Pieces", tv_onclick_select_piece.getText().toString());
            Log.e("tv_onclick_shift", tv_onclick_shift.getText().toString());

            btn_save_packing.setEnabled(true);
            btn_save_packing.setBackground(getResources().getDrawable(R.drawable.packing_entry_button_bg));

        }

        if (!(str_session_io_number.equalsIgnoreCase("No data"))) {
            tv_onclick_io_number_txt.setText(str_session_io_number);
            tv_io_number_normal_txt.setText("");
        }

        if (!(str_session_color_pick.equalsIgnoreCase("No data"))) {
            tv_onclick_color_txt.setText(str_session_color_pick);
            tv_color_normal_txt.setText("");
        }

        if (!(str_session_checking_person.equalsIgnoreCase("No data"))) {
            if (str_qrcode_value_others != null && str_qrcode_value != null) {
                tv_onclick_checking_person_txt.setText(str_name);
                tv_normal_checking_person_txt.setText("");
                SessionSave.SaveSession("Session_Checking_Person_Packing", tv_onclick_checking_person_txt.getText().toString(), getActivity());
            } else if (str_qrcode_value != null) {
                tv_onclick_checking_person_txt.setText(str_session_checking_person);
                tv_normal_checking_person_txt.setText("");
                SessionSave.SaveSession("Session_Checking_Person_Packing", tv_onclick_checking_person_txt.getText().toString(), getActivity());
            }
        }

        if (!(str_session_operation.equalsIgnoreCase("No data"))) {
            tv_onclick_operation_for_packing.setText(str_session_operation);
            tv_select_operation_for_packing.setText("");
        }

        if (!(str_session_shift_timing.equalsIgnoreCase("No data"))) {
            tv_onclick_shift.setText(str_session_shift_timing);
            tv_select_shift.setText("");
        }

        if (!(str_session_total_pieces.equalsIgnoreCase("No data"))) {
            tv_onclick_select_piece.setText(str_session_total_pieces);
            tv_normal_select_piece.setText("");
        }

        if (constraintLayout_packing_rv_layout.getVisibility() == View.GONE) {
            Packing_Entry_Act.tv_exit_txt.setVisibility(View.VISIBLE);
        }

        /*Newly added on 16-05-2020-Aftn*/
        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                Toast.makeText(getActivity(), "Permission already granted!", Toast.LENGTH_LONG).show();
            } else {
                requestPermission();
            }
        }
        return view;
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{CAMERA}, REQUEST_CAMERA);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void onBackPressed() {
        int backStackEntryCount = Objects.requireNonNull(getActivity()).getSupportFragmentManager().getBackStackEntryCount();
        Log.e("backStackCnt_wallet", "" + backStackEntryCount);
        if (backStackEntryCount == 1) {
            Intent intent = new Intent(getContext(), Home_Activity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();// write your code to switch between fragments.
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_packing:
                String str_io_number, str_color, str_checking_person, str_total_pieces, str_ok_pieces, str_defect_pieces, str_shifttime;
                str_io_number = tv_onclick_io_number_txt.getText().toString();
                str_color = tv_onclick_color_txt.getText().toString();
                str_checking_person = tv_onclick_checking_person_txt.getText().toString();
                str_shifttime = tv_onclick_shift.getText().toString();
//                str_total_pieces = tv_onclick_shift.getText().toString();
//                str_ok_pieces = tv_onclick_shift.getText().toString();
//                str_defect_pieces = tv_onclick_shift.getText().toString();

                Log.e("str_io_number", str_io_number);
                Log.e("str_color", str_color);
                Log.e("str_checking_person", str_checking_person);
                Log.e("str_shifttime", str_shifttime);

                constraintLayout_qr_code_scan.setVisibility(View.GONE);
                constraintLayout_packing_rv_layout.setVisibility(View.VISIBLE);
                btn_save_packing.setVisibility(View.GONE);
                fab_for_packing.setVisibility(View.VISIBLE);
                if (constraintLayout_packing_rv_layout.getVisibility() == View.VISIBLE) {
                    Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), getResources().getString(R.string.successfully_submitted_txt), 5);
                    Packing_Entry_Act.tv_exit_txt.setVisibility(View.GONE);
                }

                /*try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("", str_operator_name);
                    jsonObject.put("", str_operation_name);
                    jsonObject.put("", str_shifttime);
                    APIInterface apiInterface = Factory.getClient();
                    Call<Operator_Model> call = apiInterface.OPERATOR_RESPONSE_CALL("application/json", jsonObject.toString());
                    call.enqueue(new Callback<Operator_Model>() {
                        @Override
                        public void onResponse(Call<Operator_Model> call, Response<Operator_Model> response) {
                            if (response.code() == 200) {
                                if (response.isSuccessful()) {

                                }
                            } else if (response.code() == 401) {

                            } else if (response.code() == 500) {

                            }
                        }

                        @Override
                        public void onFailure(Call<Operator_Model> call, Throwable t) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                break;
            case R.id.fab_for_packing:
                final Dialog dialog_fab = new Dialog(Objects.requireNonNull(getActivity()));
                dialog_fab.setContentView(R.layout.bar_code_alert_for_packing);
                TextView tv_scan_fab, tv_list_fab, tv_cancel_fab;
                tv_scan_fab = dialog_fab.findViewById(R.id.tv_scan);
                tv_list_fab = dialog_fab.findViewById(R.id.tv_list);
                tv_cancel_fab = dialog_fab.findViewById(R.id.tv_cancel);

                tv_scan_fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_fab.dismiss();
                        constraintLayout_packing_rv_layout.setVisibility(View.GONE);
                        constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                        btn_save_packing.setVisibility(View.VISIBLE);
                        fab_for_packing.setVisibility(View.GONE);
                        /*Intent intent = new Intent(getActivity(), ScannedBarcodeActivity.class);
                        intent.putExtra("Entry_Point_Onclick_Value", "5");
                        startActivity(intent);*/

                        scanner_layout.setVisibility(View.VISIBLE);
                        constraintLayout_qr_code_scan.setVisibility(View.GONE);
                        btn_save_packing.setVisibility(View.GONE);

                        /*Newly added on 16-05-2020-Aftn*/
                        //                        initialiseDetectorsAndSources();
                        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
                            if (checkPermission()) {
                                if (scannerView == null) {
                                    scannerView = new ZXingScannerView(getActivity());
                                    getActivity().setContentView(scannerView);
                                }
                                scannerView.setResultHandler((ZXingScannerView.ResultHandler) getContext());
                                scannerView.startCamera();
                            } else {
                                requestPermission();
                            }
                        }
                    }
                });
                tv_cancel_fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_fab.dismiss();
                    }
                });
                dialog_fab.setCancelable(false);
                dialog_fab.show();
                break;
            case R.id.constraintLayout_io_number_layout:
                dialog_for_io_number = new Dialog(Objects.requireNonNull(getActivity()));
                dialog_for_io_number.setContentView(R.layout.io_number_alert_for_packing);
                rv_io_number = dialog_for_io_number.findViewById(R.id.rv_io_number);
                rv_io_number.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                rv_io_number.setHasFixedSize(true);
                dialog_for_io_number.setCancelable(true);
                Get_Io_Number_Details();
                dialog_for_io_number.show();
                break;
            case R.id.constraintLayout_color_layout:
                dialog_for_colors = new Dialog(Objects.requireNonNull(getActivity()));
                dialog_for_colors.setContentView(R.layout.color_select_alert_for_packing);
                rv_colors = dialog_for_colors.findViewById(R.id.rv_colors);
                rv_colors.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                rv_colors.setHasFixedSize(true);
                dialog_for_colors.setCancelable(true);
                Get_Color_Details();
                dialog_for_colors.show();
                break;
            case R.id.constraintLayout_checking_person_layout:
                /*dialog_for_check_person = new Dialog(Objects.requireNonNull(getActivity()));
                dialog_for_check_person.setContentView(R.layout.check_person_alert);
                rv_checking_person = dialog_for_check_person.findViewById(R.id.rv_checking_person);
                rv_checking_person.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                rv_checking_person.setHasFixedSize(true);
                dialog_for_check_person.setCancelable(true);
                dialog_for_check_person.show();*/

                final Dialog dialog_01 = new Dialog(Objects.requireNonNull(getActivity()));
                dialog_01.setContentView(R.layout.bar_code_alert_for_packing);
                TextView tv_scan_01, tv_list_01, tv_cancel_01;
                tv_scan_01 = dialog_01.findViewById(R.id.tv_scan);
                tv_list_01 = dialog_01.findViewById(R.id.tv_list);
                tv_cancel_01 = dialog_01.findViewById(R.id.tv_cancel);
                tv_list_01.setText("List");

                tv_list_01.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_01.dismiss();
                        dialog_for_check_person = new Dialog(Objects.requireNonNull(getActivity()));
                        dialog_for_check_person.setContentView(R.layout.check_person_alert_for_packing);
                        rv_checking_person = dialog_for_check_person.findViewById(R.id.rv_checking_person);
                        rv_checking_person.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        rv_checking_person.setHasFixedSize(true);
                        Get_Checking_Person_Details();
                        dialog_for_check_person.setCancelable(true);
                        dialog_for_check_person.show();
                    }
                });
                tv_scan_01.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ScannedBarcodeActivity.class);
//                        intent.putExtra("Entry_Point_Onclick_Value", "5");
                        intent.putExtra("Entry_Point_Onclick_Value_Others", "5");
                        startActivity(intent);
                        constraintLayout_qr_code_scan.setVisibility(View.GONE);
                        btn_save_packing.setVisibility(View.GONE);
                    }
                });
                tv_cancel_01.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_01.dismiss();
                    }
                });
                dialog_01.setCancelable(false);
                dialog_01.show();
                break;
            case R.id.constraintLayout_pieces_layout:
                dialog_for_pieces = new Dialog(Objects.requireNonNull(getActivity()));
                dialog_for_pieces.setContentView(R.layout.select_pieces_alert_for_packing);
                et_num_from_pieces_dialog = dialog_for_pieces.findViewById(R.id.et_num_from_pieces_dialog);
                tv_ok_from_pieces_dialog = dialog_for_pieces.findViewById(R.id.tv_ok_from_pieces_dialog);
                tv_ok_from_pieces_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        str_num_of_pieces = et_num_from_pieces_dialog.getText().toString();
                        if (str_num_of_pieces.isEmpty()) {
                            Toast.makeText(getActivity(), "Please enter piece value", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("inside", str_num_of_pieces);
                            dialog_for_pieces.dismiss();
                            tv_normal_select_piece.setText("");
                            SessionSave.SaveSession("Session_Total_Pieces_Packing", str_num_of_pieces, getActivity());
                            tv_onclick_select_piece.setText(str_num_of_pieces);
                        }
                    }
                });
                dialog_for_pieces.setCancelable(true);
                dialog_for_pieces.show();
                break;

            case R.id.constraintLayout_select_operation:
                dialog_for_operation = new Dialog(Objects.requireNonNull(getActivity()));
                dialog_for_operation.setContentView(R.layout.operation_select_alert_for_packing);
                rv_select_operation = dialog_for_operation.findViewById(R.id.rv_select_operation);
                rv_select_operation.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                rv_select_operation.setHasFixedSize(true);
                Get_Operation_Call();
                dialog_for_operation.setCancelable(true);
                dialog_for_operation.show();
                break;
            case R.id.constraintLayout_select_shift:
                if (tv_onclick_io_number_txt.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Select IO Number", Toast.LENGTH_SHORT).show();
                } else if (tv_onclick_color_txt.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Select Color", Toast.LENGTH_SHORT).show();
                } else if (tv_onclick_checking_person_txt.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Select Checking Person", Toast.LENGTH_SHORT).show();
                } else if (tv_onclick_select_piece.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Select Pieces", Toast.LENGTH_SHORT).show();
                } else if (tv_onclick_operation_for_packing.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Select Operation", Toast.LENGTH_SHORT).show();
                } else {
                    dialog_for_shift_timing = new Dialog(Objects.requireNonNull(getActivity()));
                    dialog_for_shift_timing.setContentView(R.layout.select_shift_alert_for_packing);
                    rv_shift_timing = dialog_for_shift_timing.findViewById(R.id.rv_shift_timing);
                    rv_shift_timing.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    rv_shift_timing.setHasFixedSize(true);
                    Get_Shift_Timing();
                    dialog_for_shift_timing.setCancelable(true);
                    dialog_for_shift_timing.show();
                }
                break;
        }

    }

    private void initialiseDetectorsAndSources() {
        Toast.makeText(getActivity(), "Barcode scanner started", Toast.LENGTH_SHORT).show();
        barcodeDetector = new BarcodeDetector.Builder(getActivity())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(getActivity(), barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                openCamera();
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
            @Override
            public void release() {
                Toast.makeText(getActivity(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barCode = detections.getDetectedItems();
                if (barCode.size() > 0) {
                    setBarCode(barCode);
                }
            }
        });
    }

    private void setBarCode(final SparseArray<Barcode> barCode) {
        textViewBarCodeValue.post(new Runnable() {
            @Override
            public void run() {
                intentData = barCode.valueAt(0).displayValue;
                textViewBarCodeValue.setText(intentData);
                copyToClipBoard(intentData);
                if (!(textViewBarCodeValue.getText().toString().isEmpty())) {
                    scanner_layout.setVisibility(View.GONE);
                    Log.e("textViewBarCodeValue_loggg", "" + textViewBarCodeValue.getText().toString());

                    Get_Scan_Details();
                    rv_packing.setVisibility(View.GONE);
                    constraintLayout_packing_rv_layout.setVisibility(View.GONE);
                    constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                    scroll_view_layout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void Get_Scan_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("usname", str_session_username);
            jsonObject.put("token", str_session_logintoken);
            jsonObject.put("qr_no", str_qrcode_value);
//            jsonObject.put("qr_no", str_bar_code_scan_value);
            Log.e("Scan_Details_Json", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Full_App_Model_Class> call = apiInterface.GET_BUNDLE_DATA_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Full_App_Model_Class>() {
                @Override
                public void onResponse(Call<Full_App_Model_Class> call, Response<Full_App_Model_Class> response) {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        if (response.body().data != null) {
                            if (response.isSuccessful()) {

                                scroll_view_layout.setVisibility(View.VISIBLE);
                                constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                                btn_save_packing.setVisibility(View.VISIBLE);

                                rv_packing.setVisibility(View.GONE);
                                constraintLayout_packing_rv_layout.setVisibility(View.GONE);
                                constraintLayout_packing_rv_layout.setVisibility(View.GONE);
                                fab_for_packing.setVisibility(View.GONE);

                                str_io_num = response.body().data.io_no;
                                str_date = response.body().data.date;
                                str_color_code = response.body().data.color_code;
                                str_bundle_num = response.body().data.bundle_no;
                                str_size = response.body().data.size;
                                str_lot_num = response.body().data.lot_no;
                                str_total_pieces = response.body().data.tot_pcs;
                                Log.e("str_io_num_log", str_io_num);
                                Log.e("str_date_log", str_date);
                                Log.e("str_color_code_log", str_color_code);
                                Log.e("str_bundle_num_log", str_bundle_num);
                                Log.e("str_size_log", str_size);
                                Log.e("str_lot_num_log", str_lot_num);
                                Log.e("str_total_pieces_log", str_total_pieces);

                            }
                        } else {
//                            Toast.makeText(getActivity(), "Please enter valid user", Toast.LENGTH_LONG).show();
                            /*Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), "Please enter valid user", 6);
                            Intent intent = new Intent(getActivity(), Home_Activity.class);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
                        }
                    } else if (response.code() == 401) {
                        Toast.makeText(getActivity(), "" + response.message(), Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 500) {
                        Toast.makeText(getActivity(), "" + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Full_App_Model_Class> call, Throwable t) {

                }
            });
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    private void openCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                cameraSource.start(surfaceView.getHolder());
            } else {
                ActivityCompat.requestPermissions(getActivity(), new
                        String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyToClipBoard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("QR code Scanner", text);
        assert clipboard != null;
        clipboard.setPrimaryClip(clip);
    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                getActivity().finish();
            else
                openCamera();
        } else
            getActivity().finish();
    }
*/

    private void Get_Operation_Call() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("usname", str_session_username);
            jsonObject.put("token", str_session_logintoken);
            Log.e("json_operation", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Operator_Model> call = apiInterface.OPERATION_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Operator_Model>() {
                @Override
                public void onResponse(Call<Operator_Model> call, Response<Operator_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            rv_select_operation.setVisibility(View.VISIBLE);
                            operation_select_adapter = new Operation_Select_Adapter(getActivity(), response.body().data);
                            rv_select_operation.setAdapter(operation_select_adapter);
                        }
                    } else if (response.code() == 401) {

                    } else if (response.code() == 500) {

                    }
                }

                @Override
                public void onFailure(Call<Operator_Model> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Get_Checking_Person_Details() {
        checking_person_ArrayList.add("Sam");
        checking_person_ArrayList.add("Sam");
        checking_person_ArrayList.add("Sam");
        checking_person_ArrayList.add("Sam");
        checking_person_ArrayList.add("Sam");
        checking_person_ArrayList.add("Sam");
        checking_person_ArrayList.add("Sam");
        checking_person_ArrayList.add("Sam");
        checking_person_ArrayList.add("Sam");
        checking_person_ArrayList.add("Sam");
        rv_checking_person.setVisibility(View.VISIBLE);
        checking_person_adapter_for_packing = new Checking_Person_Adapter_For_Packing(getActivity(), checking_person_ArrayList);
        rv_checking_person.setAdapter(checking_person_adapter_for_packing);
    }

    private void Get_Color_Details() {
        color_ArrayList.add("#09877");
        color_ArrayList.add("#09877");
        color_ArrayList.add("#09877");
        color_ArrayList.add("#09877");
        color_ArrayList.add("#09877");
        color_ArrayList.add("#09877");
        color_ArrayList.add("#09877");
        color_ArrayList.add("#09877");
        color_ArrayList.add("#09877");
        color_ArrayList.add("#09877");
        rv_colors.setVisibility(View.VISIBLE);
        color_adapter_for_packing = new Color_Adapter_For_Packing(getActivity(), color_ArrayList);
        rv_colors.setAdapter(color_adapter_for_packing);
    }

    private void Get_Io_Number_Details() {
        ionumber_ArrayList.add("1234");
        ionumber_ArrayList.add("1234");
        ionumber_ArrayList.add("1234");
        ionumber_ArrayList.add("1234");
        ionumber_ArrayList.add("1234");
        ionumber_ArrayList.add("1234");
        ionumber_ArrayList.add("1234");
        ionumber_ArrayList.add("1234");
        ionumber_ArrayList.add("1234");
        ionumber_ArrayList.add("1234");
        rv_io_number.setVisibility(View.VISIBLE);
        io_number_adapter_for_packing = new Io_Number_Adapter_For_Packing(getActivity(), ionumber_ArrayList);
        rv_io_number.setAdapter(io_number_adapter_for_packing);
    }

    private void Get_Shift_Timing() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("usname", str_session_username);
            jsonObject.put("token", str_session_logintoken);
            APIInterface apiInterface = Factory.getClient();
            Call<Operator_Model> call = apiInterface.SHIFT_TIMING_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Operator_Model>() {
                @Override
                public void onResponse(Call<Operator_Model> call, Response<Operator_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            rv_shift_timing.setVisibility(View.VISIBLE);
                            shift_timing_adapter_for_packing = new Shift_Timing_Adapter_For_Packing(getActivity(), response.body().data);
                            rv_shift_timing.setAdapter(shift_timing_adapter_for_packing);
                        }
                    } else if (response.code() == 401) {

                    } else if (response.code() == 500) {

                    }
                }

                @Override
                public void onFailure(Call<Operator_Model> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        Toast.makeText(getActivity(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(Result result) {
        final String myResult = result.getText();
        Log.d("QRCodeScanner", result.getText());
        Log.d("QRCodeScanner", result.getBarcodeFormat().toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Scan Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scannerView.resumeCameraPreview((ZXingScannerView.ResultHandler) getActivity());
            }
        });
        builder.setNeutralButton("Visit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myResult));
                startActivity(browserIntent);
            }
        });
        builder.setMessage(result.getText());
        AlertDialog alert1 = builder.create();
        alert1.show();
    }

    private class Io_Number_Adapter_For_Packing extends RecyclerView.Adapter<Io_Number_Adapter_For_Packing.ViewHolder> {
        ArrayList<String> stringArrayList;
        Context mContext;


        public Io_Number_Adapter_For_Packing(FragmentActivity activity, ArrayList<String> ionumber_arrayList) {
            this.mContext = activity;
            this.stringArrayList = ionumber_arrayList;
        }

        @NonNull
        @Override
        public Io_Number_Adapter_For_Packing.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view;
            view = inflater.inflate(R.layout.io_number_adapter_details_layout, parent, false);
            return new Io_Number_Adapter_For_Packing.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Io_Number_Adapter_For_Packing.ViewHolder holder, int position) {
            String str_from_time = stringArrayList.get(position);
            holder.tv_io_number_pick.setText(str_from_time);
            holder.tv_io_number_pick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_io_number_normal_txt.setText("");
                    SessionSave.SaveSession("Session_IO_Number_Packing", holder.tv_io_number_pick.getText().toString(), getActivity());
                    tv_onclick_io_number_txt.setText(holder.tv_io_number_pick.getText().toString());
                    dialog_for_io_number.dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return stringArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_io_number_pick;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_io_number_pick = itemView.findViewById(R.id.tv_io_number_pick);
            }
        }
    }

    private class Color_Adapter_For_Packing extends RecyclerView.Adapter<Color_Adapter_For_Packing.ViewHolder> {
        ArrayList<String> stringArrayList;
        Context mContext;


        public Color_Adapter_For_Packing(FragmentActivity activity, ArrayList<String> ionumber_arrayList) {
            this.mContext = activity;
            this.stringArrayList = ionumber_arrayList;
        }

        @NonNull
        @Override
        public Color_Adapter_For_Packing.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view;
            view = inflater.inflate(R.layout.color_adapter_details_layout, parent, false);
            return new Color_Adapter_For_Packing.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Color_Adapter_For_Packing.ViewHolder holder, int position) {
            String str_from_time = stringArrayList.get(position);
            holder.tv_color_pick.setText(str_from_time);
            holder.tv_color_pick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_color_normal_txt.setText("");
                    SessionSave.SaveSession("Session_Color_Pick_Packing", holder.tv_color_pick.getText().toString(), getActivity());
                    tv_onclick_color_txt.setText(holder.tv_color_pick.getText().toString());
                    dialog_for_colors.dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return stringArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_color_pick;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_color_pick = itemView.findViewById(R.id.tv_color_pick);
            }
        }
    }

    private class Checking_Person_Adapter_For_Packing extends RecyclerView.Adapter<Checking_Person_Adapter_For_Packing.ViewHolder> {
        ArrayList<String> stringArrayList;
        Context mContext;


        public Checking_Person_Adapter_For_Packing(FragmentActivity activity, ArrayList<String> ionumber_arrayList) {
            this.mContext = activity;
            this.stringArrayList = ionumber_arrayList;
        }

        @NonNull
        @Override
        public Checking_Person_Adapter_For_Packing.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view;
            view = inflater.inflate(R.layout.checking_person_adapter_details_layout, parent, false);
            return new Checking_Person_Adapter_For_Packing.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Checking_Person_Adapter_For_Packing.ViewHolder holder, int position) {
            String str_from_time = stringArrayList.get(position);
            holder.tv_checking_person_pick.setText(str_from_time);
            holder.tv_checking_person_pick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_normal_checking_person_txt.setText("");
                    SessionSave.SaveSession("Session_Checking_Person_Packing", holder.tv_checking_person_pick.getText().toString(), getActivity());
                    tv_onclick_checking_person_txt.setText(holder.tv_checking_person_pick.getText().toString());
                    dialog_for_check_person.dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return stringArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_checking_person_pick;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_checking_person_pick = itemView.findViewById(R.id.tv_checking_person_pick);
            }
        }
    }

    private class Shift_Timing_Adapter_For_Packing extends RecyclerView.Adapter<Shift_Timing_Adapter_For_Packing.ViewHolder> {
        ArrayList<Operator_Model.Data> stringArrayList;
        Context mContext;

        public Shift_Timing_Adapter_For_Packing(FragmentActivity activity, ArrayList<Operator_Model.Data> data) {
            this.mContext = activity;
            this.stringArrayList = data;
        }

        @NonNull
        @Override
        public Shift_Timing_Adapter_For_Packing.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view;
            view = inflater.inflate(R.layout.select_shift_details_layout, parent, false);
            return new Shift_Timing_Adapter_For_Packing.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Shift_Timing_Adapter_For_Packing.ViewHolder holder, int position) {
            String str_from_time = stringArrayList.get(position).from_time;
            String str_to_time = stringArrayList.get(position).to_time;
            final String str_merge_timings = str_from_time + "  to  " + str_to_time;

            holder.tv_from_shift_timing.setText(str_merge_timings);
            holder.tv_from_shift_timing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_select_shift.setText("");
                    Log.e("str_merge_timings", str_merge_timings);
                    SessionSave.SaveSession("Session_Shift_Timing_Packing", holder.tv_from_shift_timing.getText().toString(), getActivity());
                    tv_onclick_shift.setText(holder.tv_from_shift_timing.getText().toString());
                    dialog_for_shift_timing.dismiss();

                    if (!(tv_onclick_io_number_txt.getText().toString().isEmpty())
                            && (!(tv_onclick_color_txt.getText().toString().isEmpty()))
                            && (!(tv_onclick_checking_person_txt.getText().toString().isEmpty())
                            && (!(tv_onclick_operation_for_packing.getText().toString().isEmpty())
                            && (!(tv_onclick_shift.getText().toString().isEmpty()))))) {
                        btn_save_packing.setEnabled(true);
                        btn_save_packing.setBackground(mContext.getResources().getDrawable(R.drawable.packing_entry_button_bg));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return stringArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_from_shift_timing;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_from_shift_timing = itemView.findViewById(R.id.tv_from_shift_timing);
            }
        }
    }

    private class Operation_Select_Adapter extends RecyclerView.Adapter<Operation_Select_Adapter.ViewHolder> {
        ArrayList<Operator_Model.Data> stringArrayList;
        Context mContext;

        public Operation_Select_Adapter(FragmentActivity activity, ArrayList<Operator_Model.Data> data) {
            this.mContext = activity;
            this.stringArrayList = data;
        }

        @NonNull
        @Override
        public Operation_Select_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view;
            view = inflater.inflate(R.layout.operation_select_details_layout, parent, false);
            return new Operation_Select_Adapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Operation_Select_Adapter.ViewHolder holder, int position) {
            holder.tv_operation_select.setText(stringArrayList.get(position).name);

            holder.tv_operation_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.tv_operation_select.getText().toString();
                    tv_select_operation_for_packing.setText("");
                    SessionSave.SaveSession("Session_Operation_Packing", holder.tv_operation_select.getText().toString(), getActivity());
                    tv_onclick_operation_for_packing.setText(holder.tv_operation_select.getText().toString());
                    dialog_for_operation.dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return stringArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_operation_select;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_operation_select = itemView.findViewById(R.id.tv_operation_select);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    public void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

}