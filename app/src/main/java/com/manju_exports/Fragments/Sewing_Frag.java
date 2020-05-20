package com.manju_exports.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.manju_exports.Adapters.Sewing_Adapter;
import com.manju_exports.Entries.Sewing_Entry_Act;
import com.manju_exports.Home_Activity;
import com.manju_exports.Interface.APIInterface;
import com.manju_exports.Interface.Factory;
import com.manju_exports.Model.Full_App_Model_Class;
import com.manju_exports.Model.Operator_Model;
import com.manju_exports.Model.Sewing_Model;
import com.manju_exports.Next_Qr_code;
import com.manju_exports.R;
import com.manju_exports.ScannedBarcodeActivity;
import com.manju_exports.SessionSave;
import com.manju_exports.Toast_Message;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import io.supercharge.shimmerlayout.ShimmerLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.VIBRATOR_SERVICE;

public class Sewing_Frag extends Fragment implements View.OnClickListener {
    public static ConstraintLayout
            constraintLayout_sewing_rv_layout,
            constraintLayout_recyclerview,
            constraintLayout_qr_code_scan;
    private ConstraintLayout
            constraintLayout_select_no_of_operation,
            constraintLayout_scanning_result_values,
            constraintLayout_inside_io_number_color,
            constraintLayout_io_number_layout_inside,
            constraintLayout_color_layout_inside,
            constraintLayout_inside_api_values,
            constraintLayout_operation_layout,
            constraintLayout_select_shift,
            constraintLayout_tailor_layout;

    ScrollView scroll_view_layout;
    public static Button btn_save_new;

    private Dialog
            dialog_for_tailor,
            dialog_for_shift_timing,
            dialog_for_operation,
            dialog_for_pieces,
            dialog_for_io_number,
            dialog_for_colors;

    private Operation_Select_Adapter operation_select_adapter;
    private Select_No_Pieces_Adapter select_no_pieces_adapter;
    private Tailors_Select_Adapter tailors_select_adapter;
    private Shift_Timing_Adapter shift_timing_adapter;
    private Sewing_Adapter sewing_adapter;

    public static FloatingActionButton fab;
    private RecyclerView
            rv_select_operation,
            rv_tailors_select,
            rv_shift_timing,
            rv_no_of_pieces,
            rv_io_number,
            rv_sewing,
            rv_colors;


    ShimmerLayout shimmer_for_sewing;
    private TextView
            tv_io_number_normal_manual_entry_txt,
            tv_onclick_io_number_manual_entry_txt,
            tv_normal_select_piece_for_sewing,
            tv_onclick_select_piece_for_sewing,
            tv_onclick_color_manual_entry_txt,
            tv_color_normal_manual_entry_txt,
            tv_normal_tailor_txt,
            tv_onclick_tailor_txt,
            tv_select_operation,
            tv_onclick_operation,
            tv_bundle_number,
            tv_total_pieces,
            tv_color_code,
            tv_art_number,
            tv_io_number,
            tv_lot_number,
            tv_date,
            tv_size,
            textViewName,
            textViewAddress,
            tv_scan_again,
            tv_select_shift,
            tv_onclick_shift,
            tv_no_data_available;

    private String
            str_io_num,
            str_art_num,
            str_date,
            str_color_code,
            str_bundle_num,
            str_size,
            str_lot_num,
            str_total_pieces,
            str_session_game_entry_point_value;

    private String
            str_session_username,
            str_session_logintoken,
            str_session_checking_person,
            str_session_io_number,
            str_session_color_pick,
            str_session_io_number_manual_entry,
            str_session_color_pick_manual_entry,
            str_session_checking_person_dummy,
            str_session_operation,
            str_session_shift_timing,
            str_bar_code_scan_value;

    private String str_qrcode_value = "";
    private String str_qrcode_value_others = "";
    int int_fab_onlick_value = 0;


    private EditText et_num_from_pieces_dialog;
    private TextView tv_ok_from_pieces_dialog;
    private String str_num_of_pieces, str_session_num_of_pieces;


    private Io_Number_Adapter_For_Checking io_number_adapter_for_checking;
    private Color_Adapter_For_Checking color_adapter_for_checking;


    RelativeLayout scanner_layout;
    SurfaceView surfaceView;
    TextView textViewBarCodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    String intentData = "";
    Vibrator vibrator;
    private boolean mAllowShake = false;

    @SuppressLint("LongLogTag")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sewing_frag_lay, container, false);
        //This is used for Full screen
//        Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Home_Activity.toolbar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        Home_Activity.tv_title_txt.setText(getResources().getString(R.string.sewing_txt));
        fab = view.findViewById(R.id.fab);
        shimmer_for_sewing = view.findViewById(R.id.shimmer_for_sewing);

        constraintLayout_io_number_layout_inside = view.findViewById(R.id.constraintLayout_io_number_layout_inside);
        constraintLayout_select_no_of_operation = view.findViewById(R.id.constraintLayout_select_no_of_operation);
        constraintLayout_scanning_result_values = view.findViewById(R.id.constraintLayout_scanning_result_values);
        constraintLayout_inside_io_number_color = view.findViewById(R.id.constraintLayout_inside_io_number_color);
        constraintLayout_color_layout_inside = view.findViewById(R.id.constraintLayout_color_layout_inside);
        constraintLayout_inside_api_values = view.findViewById(R.id.constraintLayout_inside_api_values);
        constraintLayout_operation_layout = view.findViewById(R.id.constraintLayout_operation_layout);
        constraintLayout_tailor_layout = view.findViewById(R.id.constraintLayout_tailor_layout);
        constraintLayout_select_shift = view.findViewById(R.id.constraintLayout_select_shift);

        constraintLayout_sewing_rv_layout = view.findViewById(R.id.constraintLayout_sewing_rv_layout);
        constraintLayout_recyclerview = view.findViewById(R.id.constraintLayout_recyclerview);
        constraintLayout_qr_code_scan = view.findViewById(R.id.constraintLayout_qr_code_scan);

        scroll_view_layout = view.findViewById(R.id.scroll_view_layout);
        btn_save_new = view.findViewById(R.id.btn_save_new);
        rv_sewing = view.findViewById(R.id.rv_sewing);

        btn_save_new.setVisibility(View.GONE);
        fab.setVisibility(View.VISIBLE);

        scanner_layout = view.findViewById(R.id.scanner_layout);
        textViewBarCodeValue = view.findViewById(R.id.txtBarcodeValue);
        surfaceView = view.findViewById(R.id.surfaceView);
        vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);


        tv_io_number_normal_manual_entry_txt = view.findViewById(R.id.tv_io_number_normal_manual_entry_txt);
        tv_onclick_io_number_manual_entry_txt = view.findViewById(R.id.tv_onclick_io_number_manual_entry_txt);
        tv_onclick_select_piece_for_sewing = view.findViewById(R.id.tv_onclick_select_piece_for_sewing);
        tv_normal_select_piece_for_sewing = view.findViewById(R.id.tv_normal_select_piece_for_sewing);
        tv_onclick_color_manual_entry_txt = view.findViewById(R.id.tv_onclick_color_manual_entry_txt);
        tv_color_normal_manual_entry_txt = view.findViewById(R.id.tv_color_normal_manual_entry_txt);
        tv_normal_tailor_txt = view.findViewById(R.id.tv_normal_tailor_txt);
        tv_onclick_tailor_txt = view.findViewById(R.id.tv_onclick_tailor_txt);
        tv_onclick_operation = view.findViewById(R.id.tv_onclick_operation);
        tv_select_operation = view.findViewById(R.id.tv_select_operation);
        tv_onclick_shift = view.findViewById(R.id.tv_onclick_shift);
        tv_select_shift = view.findViewById(R.id.tv_select_shift);
        tv_no_data_available = view.findViewById(R.id.tv_no_data_available);
        tv_no_data_available.setVisibility(View.GONE);
        textViewAddress = view.findViewById(R.id.textViewAddress);
        tv_scan_again = view.findViewById(R.id.tv_scan_again);
        textViewName = view.findViewById(R.id.textViewName);
        tv_bundle_number = view.findViewById(R.id.tv_bundle_number);
        tv_total_pieces = view.findViewById(R.id.tv_total_pieces);
        tv_art_number = view.findViewById(R.id.tv_art_number);
        tv_lot_number = view.findViewById(R.id.tv_lot_number);
        tv_color_code = view.findViewById(R.id.tv_color_code);
        tv_io_number = view.findViewById(R.id.tv_io_number);
        tv_date = view.findViewById(R.id.tv_date);
        tv_size = view.findViewById(R.id.tv_size);

        rv_sewing.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_sewing.setHasFixedSize(true);

        shimmer_for_sewing.setVisibility(View.VISIBLE);
        shimmer_for_sewing.startShimmerAnimation();
        Home_Activity.tv_title_txt.setText(getResources().getString(R.string.sewing_txt));

        //attaching onclick listener
        constraintLayout_tailor_layout.setOnClickListener(this);
        constraintLayout_select_no_of_operation.setOnClickListener(this);
        constraintLayout_operation_layout.setOnClickListener(this);
        constraintLayout_select_shift.setOnClickListener(this);
        tv_scan_again.setOnClickListener(this);
        btn_save_new.setOnClickListener(this);
        fab.setOnClickListener(this);
        constraintLayout_io_number_layout_inside.setOnClickListener(this);
        constraintLayout_color_layout_inside.setOnClickListener(this);

        str_session_username = SessionSave.getSession("Session_UserName", getActivity());
        str_session_logintoken = SessionSave.getSession("Session_Logintoken", getActivity());
        str_session_io_number = SessionSave.getSession("Session_IO_Number", getActivity());

        str_session_io_number_manual_entry = SessionSave.getSession("Session_IO_Number_Manual_Entry", getActivity());
        str_session_color_pick_manual_entry = SessionSave.getSession("Session_Color_Pick_Manual_Entry", getActivity());

        str_session_color_pick = SessionSave.getSession("Session_Color_Pick", getActivity());
//        str_session_checking_person = SessionSave.getSession("Session_Checking_Person_Sewing", getActivity());
        str_session_checking_person_dummy = SessionSave.getSession("Session_Checking_Person_Sewing_dummy", getActivity());
        str_session_operation = SessionSave.getSession("Session_Operation_Sewing", getActivity());
        str_session_shift_timing = SessionSave.getSession("Session_Shift_Timing_Sewing", getActivity());
        str_session_num_of_pieces = SessionSave.getSession("Session_Total_Pieces_Sewing", getActivity());
        str_session_game_entry_point_value = SessionSave.getSession("Game_Entry_Point_Value", getActivity());

//        Log.e("str_session_username", str_session_username);
//        Log.e("str_session_logintoken", str_session_logintoken);
//        Log.e("str_session_operation", str_session_operation);
//        Log.e("str_session_shift_timing", str_session_shift_timing);
//        Log.e("str_session_checking_person_dummy", str_session_checking_person_dummy);
//        Log.e("str_session_num_of_pieces", str_session_num_of_pieces);
//        Log.e("str_session_game_entry_point_value", str_session_game_entry_point_value);
//        Log.e("str_session_io_number", str_session_io_number);
//        Log.e("str_session_color_pick", str_session_color_pick);
//        Log.e("str_session_io_number_manual_entry", str_session_io_number_manual_entry);
//        Log.e("str_session_color_pick_manual_entry", str_session_color_pick_manual_entry);


        Get_Sewing_Entry_Details();
        Bundle bundle = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        if (bundle == null) {
            str_qrcode_value = null;
        } else {
            str_qrcode_value = bundle.getString("Barcode_value");
            str_qrcode_value_others = bundle.getString("Barcode_value_Others");
            Log.e("str_qrcode_value_log", str_qrcode_value);
            /*Set<String> stringSet = new HashSet<>();
            stringSet.add(str_qrcode_value);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(str_qrcode_value);
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            /*for (int i = 0; i < Objects.requireNonNull(jsonObject).length(); i++) {
                try {
                    str_bar_code_scan_value = jsonObject.getString("qr_no");
                    if (str_bar_code_scan_value!=null) {
                        Log.e("str_bar_code_scan_value", str_bar_code_scan_value);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }*/
            if (str_qrcode_value != null) {
                Get_Scan_Details(str_qrcode_value);
                rv_sewing.setVisibility(View.GONE);
                constraintLayout_recyclerview.setVisibility(View.GONE);
                constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                constraintLayout_sewing_rv_layout.setVisibility(View.GONE);
                Sewing_Entry_Act.tv_exit_txt.setVisibility(View.VISIBLE);
                scroll_view_layout.setVisibility(View.VISIBLE);
            }
        }


        /*
         *
         * This is for setting operator name from shared preferences if session is non empty and session qrcode non empty means
         * else smiply set select tailor string
         *
         * */
        if (!(str_session_checking_person_dummy.equalsIgnoreCase("No data"))) {
            if (str_qrcode_value_others != null && str_qrcode_value != null) {
                tv_onclick_tailor_txt.setText(str_session_checking_person_dummy);
                tv_normal_tailor_txt.setText("");
                SessionSave.SaveSession("Session_Checking_Person_Sewing_dummy", tv_onclick_tailor_txt.getText().toString(), getActivity());
            } else if (str_qrcode_value != null) {
                tv_onclick_tailor_txt.setText(str_session_checking_person_dummy);
                tv_normal_tailor_txt.setText("");
                SessionSave.SaveSession("Session_Checking_Person_Sewing_dummy", tv_onclick_tailor_txt.getText().toString(), getActivity());
            } else {
                tv_onclick_tailor_txt.setText(str_session_checking_person_dummy);
                tv_normal_tailor_txt.setText("");
                SessionSave.SaveSession("Session_Checking_Person_Sewing_dummy", tv_onclick_tailor_txt.getText().toString(), getActivity());
            }
        } else {
            tv_normal_tailor_txt.setText(R.string.select_tailor_txt);
        }

        /*
         * If Session Value is non_empty means set io number
         * else set string value
         * */

        if (!(str_session_io_number_manual_entry.equalsIgnoreCase("No data"))) {
            tv_onclick_io_number_manual_entry_txt.setText(str_session_io_number_manual_entry);
            tv_io_number_normal_manual_entry_txt.setText("");
        } else {
            tv_io_number_normal_manual_entry_txt.setText(R.string.io_number_txt);
        }

        /*
         * If Session Value is non_empty means set color
         * else set string value
         * */
        if (!(str_session_color_pick_manual_entry.equalsIgnoreCase("No data"))) {
            tv_onclick_color_manual_entry_txt.setText(str_session_color_pick_manual_entry);
            tv_color_normal_manual_entry_txt.setText("");
        } else {
            tv_color_normal_manual_entry_txt.setText(R.string.color_txt);
        }

        /*
         * If Session Value is non_empty means set operation
         * else set string value
         * */
        if (!(str_session_operation.equalsIgnoreCase("No data"))) {
            tv_onclick_operation.setText(str_session_operation);
            tv_select_operation.setText("");
        } else {
            tv_onclick_operation.setText("");
            tv_select_operation.setText(R.string.select_operation_txt);
        }

        /*
         * If Session Value is non_empty means set shift
         * else set string value
         * */
        if (!(str_session_shift_timing.equalsIgnoreCase("No data"))) {
            tv_onclick_shift.setText(str_session_shift_timing);
            tv_select_shift.setText("");
        } else {
            tv_select_shift.setText(R.string.select_shift_txt);
        }

        /*
         * If Session Value is non_empty means set pieces
         * else set string value
         * */
        if (!(str_session_num_of_pieces.equalsIgnoreCase("No data"))) {
            tv_onclick_select_piece_for_sewing.setText(str_session_num_of_pieces);
            tv_normal_select_piece_for_sewing.setText("");
        } else {
            tv_onclick_select_piece_for_sewing.setText("");
        }


        /*
         * If any of one input value is empty means button is disable this is  for entry point value 1 or 2
         * */
        if (str_session_game_entry_point_value.equals("1")) {
            if ((tv_onclick_tailor_txt.getText().toString().equals(""))
                    || (tv_onclick_operation.getText().toString().equals(""))
                    || (tv_onclick_shift.getText().toString().equals(""))
                    || (tv_onclick_select_piece_for_sewing.getText().toString().equals(""))) {
                btn_save_new.setEnabled(false);
                btn_save_new.setBackground(getResources().getDrawable(R.drawable.disable_button_bg));
            }
        } else if (str_session_game_entry_point_value.equals("2")) {
            if ((tv_onclick_io_number_manual_entry_txt.getText().toString().equals(""))
                    || (tv_onclick_color_manual_entry_txt.getText().toString().equals(""))
                    || (tv_onclick_tailor_txt.getText().toString().equals(""))
                    || (tv_onclick_operation.getText().toString().equals(""))
                    || (tv_onclick_shift.getText().toString().equals(""))
                    || (tv_onclick_select_piece_for_sewing.getText().toString().equals(""))) {
                btn_save_new.setEnabled(false);
                btn_save_new.setBackground(getResources().getDrawable(R.drawable.disable_button_bg));
            }
        }

        /*
         * If all input value is full means button is enabled this is  for entry point value 1 or 2
         * */
        if (str_session_game_entry_point_value.equals("1")) {
            if (!(tv_onclick_tailor_txt.getText().toString().equals(""))
                    && !(tv_onclick_operation.getText().toString().equals(""))
                    && !(tv_onclick_shift.getText().toString().equals(""))
                    && !(tv_onclick_select_piece_for_sewing.getText().toString().equals(""))) {
                btn_save_new.setEnabled(true);
                btn_save_new.setBackground(getResources().getDrawable(R.drawable.sewing_entry_button_bg));
            }
        } else if (str_session_game_entry_point_value.equals("2")) {
            if (!(tv_onclick_io_number_manual_entry_txt.getText().toString().equals(""))
                    && !(tv_onclick_color_manual_entry_txt.getText().toString().equals(""))
                    && !(tv_onclick_tailor_txt.getText().toString().equals(""))
                    && !(tv_onclick_operation.getText().toString().equals(""))
                    && !(tv_onclick_shift.getText().toString().equals(""))
                    && !(tv_onclick_select_piece_for_sewing.getText().toString().equals(""))) {
                btn_save_new.setEnabled(true);
                btn_save_new.setBackground(getResources().getDrawable(R.drawable.sewing_entry_button_bg));
            }
        }

        return view;
    }

    /*
     * This method is called when scan button is opened hides recyclerview layout open scan activity and getting values and show scrollview layout for displaying values
     * */
    private void Get_Scan_Details(String intentData) {
        constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
        scroll_view_layout.setVisibility(View.VISIBLE);
        vibrator.cancel();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("usname", str_session_username);
            jsonObject.put("token", str_session_logintoken);
            jsonObject.put("qr_no", intentData);
//            jsonObject.put("qr_no", str_bar_code_scan_value);
            Log.e("Scan_Details_Json", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Full_App_Model_Class> call = apiInterface.GET_BUNDLE_DATA_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Full_App_Model_Class>() {
                @Override
                public void onResponse(Call<Full_App_Model_Class> call, Response<Full_App_Model_Class> response) {
//                    String str_status = response.body().status;
//                    String str_message = response.body().message;
//                    Log.e("str_status_log", str_status);
//                    Log.e("str_message_log", str_message);
                    if (response.code() == 200) {
                        assert response.body() != null;
                        if (response.body().data != null) {
                            if (response.isSuccessful()) {
                                tv_no_data_available.setVisibility(View.GONE);

                                constraintLayout_inside_io_number_color.setVisibility(View.GONE);
                                constraintLayout_inside_api_values.setVisibility(View.VISIBLE);
                                scroll_view_layout.setVisibility(View.VISIBLE);
                                constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                                btn_save_new.setVisibility(View.VISIBLE);

                                rv_sewing.setVisibility(View.GONE);
                                constraintLayout_recyclerview.setVisibility(View.GONE);
                                constraintLayout_sewing_rv_layout.setVisibility(View.GONE);
                                fab.setVisibility(View.GONE);

                                str_io_num = response.body().data.io_no;
//                            str_art_num = response.body().data.art_no;
                                str_date = response.body().data.date;
                                str_color_code = response.body().data.color_code;
                                str_bundle_num = response.body().data.bundle_no;
                                str_size = response.body().data.size;
                                str_lot_num = response.body().data.lot_no;
                                str_total_pieces = response.body().data.tot_pcs;

                                tv_io_number.setText(str_io_num);
//                            tv_art_number.setText(str_art_num);
                                tv_date.setText(str_date);
                                tv_color_code.setText(str_color_code);
                                tv_bundle_number.setText(str_bundle_num);
                                tv_size.setText(str_size);
                                tv_lot_number.setText(str_lot_num);
                                tv_total_pieces.setText(str_total_pieces);
                                SessionSave.SaveSession("Session_IO_Number", tv_io_number.getText().toString(), getActivity());
                            } else {
                                Toast.makeText(getActivity(), "" + response.message(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "QR code not exists.", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(getActivity(), "Please enter valid user", Toast.LENGTH_LONG).show();
                            /*Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), "Please enter valid user", 6);
                            Intent intent = new Intent(getActivity(), Home_Activity.class);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
                        }
                    } else if (response.code() == 401) {
                        assert response.body() != null;
                        Toast.makeText(getActivity(), "" + Objects.requireNonNull(response.body()).message, Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 500) {
                        assert response.body() != null;
                        Toast.makeText(getActivity(), "" + Objects.requireNonNull(response.body()).message, Toast.LENGTH_SHORT).show();
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

    /*
     * This method is called when sewing layout is opened shows values to recyclerview layout  and getting values and show recyclerview layout for displaying values
     * and hide shimmer layout.
     * */
    private void Get_Sewing_Entry_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("usname", str_session_username);
            jsonObject.put("token", str_session_logintoken);
//            Log.e("json_obje", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Sewing_Model> call = apiInterface.SEWING_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Sewing_Model>() {
                @Override
                public void onResponse(Call<Sewing_Model> call, Response<Sewing_Model> response) {
                    if (response.body().data != null) {
                        if (response.code() == 200) {
                            Log.e("res_data_size", String.valueOf(response.body().data.size()));
                            if (response.isSuccessful() && response.body().data.size() != 0) {
                                if (constraintLayout_qr_code_scan.getVisibility() == View.VISIBLE) {
                                    rv_sewing.setVisibility(View.GONE);
                                    constraintLayout_recyclerview.setVisibility(View.GONE);
                                } else {
                                    rv_sewing.setVisibility(View.VISIBLE);
                                    constraintLayout_recyclerview.setVisibility(View.VISIBLE);
                                }
                                tv_no_data_available.setVisibility(View.GONE);
                                shimmer_for_sewing.setVisibility(View.GONE);
                                sewing_adapter = new Sewing_Adapter(getActivity(), response.body().data);
                                rv_sewing.setAdapter(sewing_adapter);
                            } else {
                                Log.e("res_data_size_else", String.valueOf(response.body().data.size()));
                                constraintLayout_recyclerview.setVisibility(View.VISIBLE);
                                constraintLayout_sewing_rv_layout.setVisibility(View.GONE);
                                tv_no_data_available.setText(response.body().message);
                                if (scroll_view_layout.getVisibility() == View.VISIBLE) {
                                    tv_no_data_available.setVisibility(View.GONE);
                                } else {
                                    tv_no_data_available.setVisibility(View.VISIBLE);
                                }
                                shimmer_for_sewing.setVisibility(View.GONE);
                                rv_sewing.setVisibility(View.GONE);
                            }
                        } else if (response.code() == 401) {
                            Toast.makeText(getActivity(), "" + response.message(), Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 500) {
                            Toast.makeText(getActivity(), "" + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please enter valid user", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Sewing_Model> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void onBackPressed() {
        int backStackEntryCount = Objects.requireNonNull(getActivity()).getSupportFragmentManager().getBackStackEntryCount();
//        Log.e("backStackCnt_wallet", "" + backStackEntryCount);
        if (scroll_view_layout.getVisibility() == View.VISIBLE || constraintLayout_inside_io_number_color.getVisibility() == View.VISIBLE) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.exit_alert);
            TextView tv_yes, tv_no;
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            tv_yes = dialog.findViewById(R.id.tv_yes);
            tv_no = dialog.findViewById(R.id.tv_cancel);
            tv_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cameraSource != null) {
                        try {
                            cameraSource.stop();
                        } catch (NullPointerException npe) {
                            npe.printStackTrace();
                        }
                    }

                    dialog.dismiss();
                    Intent intent = new Intent(getActivity(), Sewing_Entry_Act.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });
            tv_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } else if (backStackEntryCount == 1) {
            Intent intent = new Intent(getActivity(), Sewing_Entry_Act.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();// write your code to switch between fragments.
        }
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.constraintLayout_io_number_layout_inside:
                dialog_for_io_number = new Dialog(Objects.requireNonNull(getActivity()));
                dialog_for_io_number.setContentView(R.layout.io_number_alert_sewing);
                rv_io_number = dialog_for_io_number.findViewById(R.id.rv_io_number);
                rv_io_number.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                rv_io_number.setHasFixedSize(true);
                dialog_for_io_number.setCancelable(true);
                Get_Io_Number_Details();
                dialog_for_io_number.show();
                tv_onclick_color_manual_entry_txt.setText("");
                tv_color_normal_manual_entry_txt.setText(R.string.color_txt);
                break;
            case R.id.constraintLayout_color_layout_inside:
                if (tv_onclick_io_number_manual_entry_txt.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Select Io Number", Toast.LENGTH_SHORT).show();
                } else {
                    dialog_for_colors = new Dialog(Objects.requireNonNull(getActivity()));
                    dialog_for_colors.setContentView(R.layout.color_select_alert_for_sewing);
                    rv_colors = dialog_for_colors.findViewById(R.id.rv_colors);
                    rv_colors.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    rv_colors.setHasFixedSize(true);
                    dialog_for_colors.setCancelable(true);
                    Get_Color_Details();
                    dialog_for_colors.show();
                }
                break;
            case R.id.tv_scan_again:
                Toast.makeText(getActivity(), "In Progress", Toast.LENGTH_SHORT).show();
                /*final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
                dialog.setContentView(R.layout.bar_code_alert);
                TextView tv_scan, tv_manual_in_scan_again, tv_cancel;
                tv_scan = dialog.findViewById(R.id.tv_scan);
                tv_manual_in_scan_again = dialog.findViewById(R.id.tv_list);
                tv_cancel = dialog.findViewById(R.id.tv_cancel);

                tv_scan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SessionSave.SaveSession("Game_Entry_Point_Value", "1", getActivity());
                        dialog.dismiss();
                        scroll_view_layout.setVisibility(View.VISIBLE);
                        constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                        btn_save_new.setVisibility(View.VISIBLE);
                        constraintLayout_qr_code_scan.setVisibility(View.GONE);
                        btn_save_new.setVisibility(View.GONE);
                        fab.setVisibility(View.GONE);

                        *//*scroll_view_layout.setVisibility(View.GONE);
                        constraintLayout_recyclerview.setVisibility(View.GONE);
                        constraintLayout_qr_code_scan.setVisibility(View.GONE);
                        btn_save_new.setVisibility(View.GONE);*//*
                        Intent intent = new Intent(getActivity(), ScannedBarcodeActivity.class);
                        intent.putExtra("Entry_Point_Onclick_Value", "1");
                        startActivity(intent);
                    }
                });
                tv_manual_in_scan_again.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onClick(View v) {
                        int_fab_onlick_value = 2;
                        SessionSave.SaveSession("Game_Entry_Point_Value", "2", getActivity());
                        dialog.dismiss();
                        constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                        scroll_view_layout.setVisibility(View.VISIBLE);
                        btn_save_new.setVisibility(View.VISIBLE);
                        constraintLayout_sewing_rv_layout.setVisibility(View.GONE);
                        constraintLayout_recyclerview.setVisibility(View.GONE);
                        fab.setVisibility(View.GONE);
                        rv_sewing.setVisibility(View.GONE);
                        constraintLayout_inside_io_number_color.setVisibility(View.VISIBLE);
                        constraintLayout_inside_api_values.setVisibility(View.GONE);
                        constraintLayout_scanning_result_values.setVisibility(View.VISIBLE);
                        tv_scan_again.setVisibility(View.GONE);
//                        Log.e("Game_Entry_Point_Value_logggg_Elseee", SessionSave.getSession("Game_Entry_Point_Value", getActivity()));
                        if (!(str_session_checking_person_dummy.equals("No data"))) {
                            tv_normal_tailor_txt.setText("");
                            tv_onclick_tailor_txt.setText(str_session_checking_person_dummy);
                        } else if (!(str_session_operation.equals("No data"))) {
                            tv_onclick_operation.setText(str_session_operation);
                        } else if (!(str_session_shift_timing.equals("No data"))) {
                            tv_onclick_shift.setText(str_session_shift_timing);
                        } else if (!(str_session_num_of_pieces.equals("No data"))) {
                            tv_onclick_select_piece_for_sewing.setText(str_session_num_of_pieces);
                        }
                        String s1 = SessionSave.getSession("Game_Entry_Point_Value", getActivity());
                        if (s1.equals("2")) {
                            if (!(tv_onclick_io_number_manual_entry_txt.getText().toString().isEmpty())
                                    && !(tv_onclick_color_manual_entry_txt.getText().toString().isEmpty())
                                    && !(tv_onclick_tailor_txt.getText().toString().isEmpty())
                                    && (!(tv_onclick_operation.getText().toString().isEmpty()))
                                    && (!(tv_onclick_shift.getText().toString().isEmpty()))
                                    && (!(tv_onclick_select_piece_for_sewing.getText().toString().isEmpty()))) {
                                btn_save_new.setEnabled(true);
                                btn_save_new.setBackground(getResources().getDrawable(R.drawable.sewing_entry_button_bg));
                            }
                        }
                    }
                });
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();*/
                break;
            case R.id.constraintLayout_tailor_layout:
                final Dialog dialog_01 = new Dialog(Objects.requireNonNull(getActivity()));
                dialog_01.setContentView(R.layout.bar_code_alert);
                TextView tv_scan_01, tv_list_01, tv_cancel_01;
                tv_scan_01 = dialog_01.findViewById(R.id.tv_scan);
                tv_list_01 = dialog_01.findViewById(R.id.tv_list);
                tv_cancel_01 = dialog_01.findViewById(R.id.tv_cancel);
                tv_list_01.setText("List");

                tv_list_01.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_01.dismiss();
                        dialog_for_tailor = new Dialog(Objects.requireNonNull(getActivity()));
                        dialog_for_tailor.setContentView(R.layout.select_tailors_alert);
                        rv_tailors_select = dialog_for_tailor.findViewById(R.id.rv_tailors_select);
                        rv_tailors_select.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        rv_tailors_select.setHasFixedSize(true);
                        Get_Operator_Call();
                        dialog_for_tailor.setCancelable(true);
                        dialog_for_tailor.show();
                    }
                });
                tv_scan_01.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ScannedBarcodeActivity.class);
                        intent.putExtra("Entry_Point_Onclick_Value_Others", "1");
                        startActivity(intent);
                        scroll_view_layout.setVisibility(View.GONE);
                        constraintLayout_qr_code_scan.setVisibility(View.GONE);
                        btn_save_new.setVisibility(View.GONE);
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

            case R.id.constraintLayout_operation_layout:
                Log.e("tv_io_number_logg", tv_io_number.getText().toString());
                Log.e("tv_onclick_io_number_manual_entry_txt_logg", tv_onclick_io_number_manual_entry_txt.getText().toString());
                String s1 = SessionSave.getSession("Game_Entry_Point_Value", Objects.requireNonNull(getActivity()));
                Log.e("s1_s1_s1", s1);
                if (s1.equals("1")) {
                    if (tv_io_number.getText().toString().isEmpty()) {
                        Toast.makeText(getContext(), "Please Enter Io Number", Toast.LENGTH_SHORT).show();
                    } else {
                        Get_Operation_Call();
                        dialog_for_operation = new Dialog(Objects.requireNonNull(getActivity()));
                        dialog_for_operation.setContentView(R.layout.operation_select_alert);
                        rv_select_operation = dialog_for_operation.findViewById(R.id.rv_select_operation);
                        rv_select_operation.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        rv_select_operation.setHasFixedSize(true);
                        dialog_for_operation.setCancelable(true);
                        dialog_for_operation.show();
                    }
                }
                if (s1.equals("2")) {
                    if (tv_onclick_io_number_manual_entry_txt.getText().toString().isEmpty()) {
                        Toast.makeText(getContext(), "Please Enter Io Number", Toast.LENGTH_SHORT).show();
                    } else {
                        Get_Operation_Call();
                        dialog_for_operation = new Dialog(Objects.requireNonNull(getActivity()));
                        dialog_for_operation.setContentView(R.layout.operation_select_alert);
                        rv_select_operation = dialog_for_operation.findViewById(R.id.rv_select_operation);
                        rv_select_operation.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        rv_select_operation.setHasFixedSize(true);
                        dialog_for_operation.setCancelable(true);
                        dialog_for_operation.show();
                    }
                }
                break;
            case R.id.constraintLayout_select_shift:
                dialog_for_shift_timing = new Dialog(Objects.requireNonNull(getActivity()));
                dialog_for_shift_timing.setContentView(R.layout.select_shift_alert);
                rv_shift_timing = dialog_for_shift_timing.findViewById(R.id.rv_shift_timing);
                rv_shift_timing.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                rv_shift_timing.setHasFixedSize(true);
                Get_Shift_Timing();
                dialog_for_shift_timing.setCancelable(true);
                dialog_for_shift_timing.show();

                break;
            case R.id.constraintLayout_select_no_of_operation:
                if (str_session_game_entry_point_value.equals("1") || str_session_game_entry_point_value.equalsIgnoreCase("No data")) {
                    if (tv_onclick_tailor_txt.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Tailor", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_operation.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Operation", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_shift.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Shift Timings", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog_for_pieces = new Dialog(Objects.requireNonNull(getActivity()));
                        dialog_for_pieces.setContentView(R.layout.select_pieces_alert_for_sewing);
                        et_num_from_pieces_dialog = dialog_for_pieces.findViewById(R.id.et_num_from_pieces_dialog);
                        tv_ok_from_pieces_dialog = dialog_for_pieces.findViewById(R.id.tv_ok_from_pieces_dialog);
                        tv_ok_from_pieces_dialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                str_num_of_pieces = et_num_from_pieces_dialog.getText().toString();
                                if (str_num_of_pieces.isEmpty()) {
                                    Toast.makeText(getActivity(), "Please enter piece value", Toast.LENGTH_SHORT).show();
                                } else {
                                    dialog_for_pieces.dismiss();
                                    tv_normal_select_piece_for_sewing.setText("");
                                    SessionSave.SaveSession("Session_Total_Pieces_Sewing", str_num_of_pieces, getActivity());
                                    tv_onclick_select_piece_for_sewing.setText(str_num_of_pieces);
                                    if (str_session_game_entry_point_value.equals("1")) {
                                        if (!(tv_onclick_tailor_txt.getText().toString().isEmpty())
                                                && (!(tv_onclick_operation.getText().toString().isEmpty()))
                                                && (!(tv_onclick_shift.getText().toString().isEmpty()))
                                                && (!(tv_onclick_select_piece_for_sewing.getText().toString().isEmpty()))) {
                                            btn_save_new.setEnabled(true);
                                            btn_save_new.setBackground(getResources().getDrawable(R.drawable.sewing_entry_button_bg));
                                        }
                                    }
                                }
                            }
                        });
                        dialog_for_pieces.setCancelable(true);
                        dialog_for_pieces.show();
                    }
                } else if (str_session_game_entry_point_value.equals("2") || str_session_game_entry_point_value.equalsIgnoreCase("No data")) {
                    if (tv_onclick_io_number_manual_entry_txt.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select IO Number", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_color_manual_entry_txt.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Color", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_tailor_txt.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Tailor", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_operation.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Operation", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_shift.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Shift Timings", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog_for_pieces = new Dialog(Objects.requireNonNull(getActivity()));
                        dialog_for_pieces.setContentView(R.layout.select_pieces_alert_for_sewing);
                        et_num_from_pieces_dialog = dialog_for_pieces.findViewById(R.id.et_num_from_pieces_dialog);
                        tv_ok_from_pieces_dialog = dialog_for_pieces.findViewById(R.id.tv_ok_from_pieces_dialog);
                        tv_ok_from_pieces_dialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                str_num_of_pieces = et_num_from_pieces_dialog.getText().toString();
                                if (str_num_of_pieces.isEmpty()) {
                                    Toast.makeText(getActivity(), "Please enter piece value", Toast.LENGTH_SHORT).show();
                                } else {
                                    dialog_for_pieces.dismiss();
                                    tv_normal_select_piece_for_sewing.setText("");
                                    SessionSave.SaveSession("Session_Total_Pieces_Sewing", str_num_of_pieces, getActivity());
                                    tv_onclick_select_piece_for_sewing.setText(str_num_of_pieces);
                                    if (str_session_game_entry_point_value.equals("2")) {
                                        if (!(tv_onclick_io_number_manual_entry_txt.getText().toString().isEmpty())
                                                && !(tv_onclick_color_manual_entry_txt.getText().toString().isEmpty())
                                                && !(tv_onclick_tailor_txt.getText().toString().isEmpty())
                                                && (!(tv_onclick_operation.getText().toString().isEmpty()))
                                                && (!(tv_onclick_shift.getText().toString().isEmpty()))
                                                && (!(tv_onclick_select_piece_for_sewing.getText().toString().isEmpty()))) {
                                            btn_save_new.setEnabled(true);
                                            btn_save_new.setBackground(getResources().getDrawable(R.drawable.sewing_entry_button_bg));
                                        }
                                    }
                                }
                            }
                        });
                        dialog_for_pieces.setCancelable(true);
                        dialog_for_pieces.show();
                    }
                }
                break;
            case R.id.btn_save_new:
                String str_session_game_entry_point_value_temp = "";
                if (constraintLayout_inside_io_number_color.getVisibility() == View.VISIBLE) {
                    SessionSave.SaveSession("Game_Entry_Point_Value", "2", getActivity());
                    str_session_game_entry_point_value_temp = SessionSave.getSession("Game_Entry_Point_Value", getActivity());
                } else {
                    SessionSave.SaveSession("Game_Entry_Point_Value", "1", getActivity());
                    str_session_game_entry_point_value_temp = SessionSave.getSession("Game_Entry_Point_Value", getActivity());
                }
                if (str_session_game_entry_point_value_temp.equals("1")) {
                    if (tv_onclick_tailor_txt.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Tailor", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_operation.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Operation", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_shift.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Shift Timings", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_select_piece_for_sewing.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select No of Pieces", Toast.LENGTH_SHORT).show();
                    } else {
                        Get_Save_Sewing_Details(str_session_game_entry_point_value_temp);
                        /*if (constraintLayout_recyclerview.getVisibility() == View.VISIBLE) {
                            Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), getResources().getString(R.string.successfully_submitted_txt), 1);
                            Sewing_Entry_Act.tv_exit_txt.setVisibility(View.GONE);
                        }*/
//                        scroll_view_layout.setVisibility(View.GONE);
//                        constraintLayout_qr_code_scan.setVisibility(View.GONE);
//                        btn_save_new.setVisibility(View.GONE);
//                        constraintLayout_sewing_rv_layout.setVisibility(View.VISIBLE);
//                        constraintLayout_recyclerview.setVisibility(View.VISIBLE);
//                        rv_sewing.setVisibility(View.VISIBLE);
//                        fab.setVisibility(View.VISIBLE);

/*
                        if (int_fab_onlick_value == 0) {
                            if (constraintLayout_recyclerview.getVisibility() == View.VISIBLE) {
                                Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), getResources().getString(R.string.successfully_submitted_txt), 1);
                                Sewing_Entry_Act.tv_exit_txt.setVisibility(View.GONE);
                            }
                        }
                        if (int_fab_onlick_value == 2) {
                            Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), getResources().getString(R.string.successfully_submitted_txt), 1);
                        }
*/
                    }
                } else if (str_session_game_entry_point_value_temp.equals("2")) {
//                    Log.e("tv_onclick_io_number_manual_entry_txt_02", tv_onclick_io_number_manual_entry_txt.getText().toString());
//                    Log.e("tv_onclick_color_manual_entry_txt_02", tv_onclick_color_manual_entry_txt.getText().toString());
//                    Log.e("tv_onclick_tailor_txt_02", tv_onclick_tailor_txt.getText().toString());
//                    Log.e("tv_onclick_operation_02", tv_onclick_operation.getText().toString());
//                    Log.e("tv_onclick_shift_02", tv_onclick_shift.getText().toString());
//                    Log.e("tv_onclick_select_piece_for_sewing_02", tv_onclick_select_piece_for_sewing.getText().toString());
                    if (tv_onclick_io_number_manual_entry_txt.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Io Number", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_color_manual_entry_txt.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Color", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_tailor_txt.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Tailor", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_operation.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Operation", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_shift.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Shift Timings", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_select_piece_for_sewing.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select No of Pieces", Toast.LENGTH_SHORT).show();
                    } else {
                        Get_Save_Sewing_Details(str_session_game_entry_point_value_temp);
                        /*if (constraintLayout_recyclerview.getVisibility() == View.VISIBLE) {
                            Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), getResources().getString(R.string.successfully_submitted_txt), 1);
                            Sewing_Entry_Act.tv_exit_txt.setVisibility(View.GONE);
                        }*/
//                        scroll_view_layout.setVisibility(View.GONE);
//                        constraintLayout_qr_code_scan.setVisibility(View.GONE);
//                        btn_save_new.setVisibility(View.GONE);

//                        constraintLayout_sewing_rv_layout.setVisibility(View.VISIBLE);
//                        constraintLayout_recyclerview.setVisibility(View.VISIBLE);
//                        rv_sewing.setVisibility(View.VISIBLE);
//                        fab.setVisibility(View.VISIBLE);

                        /*if (int_fab_onlick_value == 0) {
                            if (constraintLayout_recyclerview.getVisibility() == View.VISIBLE) {
                                Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), getResources().getString(R.string.successfully_submitted_txt), 1);
                                Sewing_Entry_Act.tv_exit_txt.setVisibility(View.GONE);
                            }
                        }
                        if (int_fab_onlick_value == 2) {
                            Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), getResources().getString(R.string.successfully_submitted_txt), 1);
                        }*/
                    }
                }


                break;
            case R.id.fab:
                final Dialog dialog_fab = new Dialog(Objects.requireNonNull(getActivity()));
                dialog_fab.setContentView(R.layout.bar_code_alert);
                TextView tv_scan_fab, tv_manual, tv_cancel_fab;
                tv_scan_fab = dialog_fab.findViewById(R.id.tv_scan);
                tv_manual = dialog_fab.findViewById(R.id.tv_list);
                tv_cancel_fab = dialog_fab.findViewById(R.id.tv_cancel);
                SessionSave.clearSession("Game_Entry_Point_Value", Objects.requireNonNull(getActivity()));
                tv_scan_fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int_fab_onlick_value = 0;
                        SessionSave.SaveSession("Game_Entry_Point_Value", "1", getActivity());
                        dialog_fab.dismiss();
                        scroll_view_layout.setVisibility(View.VISIBLE);
                        btn_save_new.setVisibility(View.GONE);
                        constraintLayout_qr_code_scan.setVisibility(View.GONE);
                        fab.setVisibility(View.GONE);

                        Intent intent = new Intent(getActivity(), Next_Qr_code.class);
                        intent.putExtra("Entry_Point_Onclick_Value", "1");
                        startActivity(intent);

//                        scanner_layout.setVisibility(View.VISIBLE);
//                        initialiseDetectorsAndSources();
                    }
                });
                tv_manual.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onClick(View v) {
                        int_fab_onlick_value = 2;
                        SessionSave.SaveSession("Game_Entry_Point_Value", "2", getActivity());
                        dialog_fab.dismiss();
                        constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                        scroll_view_layout.setVisibility(View.VISIBLE);
                        btn_save_new.setVisibility(View.VISIBLE);
                        constraintLayout_sewing_rv_layout.setVisibility(View.GONE);
                        constraintLayout_recyclerview.setVisibility(View.GONE);
                        fab.setVisibility(View.GONE);
                        rv_sewing.setVisibility(View.GONE);
                        constraintLayout_inside_io_number_color.setVisibility(View.VISIBLE);
                        constraintLayout_inside_api_values.setVisibility(View.GONE);
                        constraintLayout_scanning_result_values.setVisibility(View.VISIBLE);
                        tv_scan_again.setVisibility(View.GONE);
//                        Log.e("Game_Entry_Point_Value_logggg_Elseee", SessionSave.getSession("Game_Entry_Point_Value", getActivity()));
                        if (!(str_session_checking_person_dummy.equals("No data"))) {
                            tv_normal_tailor_txt.setText("");
                            tv_onclick_tailor_txt.setText(str_session_checking_person_dummy);
                        } else if (!(str_session_operation.equals("No data"))) {
                            tv_onclick_operation.setText(str_session_operation);
                        } else if (!(str_session_shift_timing.equals("No data"))) {
                            tv_onclick_shift.setText(str_session_shift_timing);
                        } else if (!(str_session_num_of_pieces.equals("No data"))) {
                            tv_onclick_select_piece_for_sewing.setText(str_session_num_of_pieces);
                        }

//                        SessionSave.SaveSession("Session_IO_Number_Manual_Entry", tv_onclick_io_number_manual_entry_txt.getText().toString(), getActivity());
//                        SessionSave.SaveSession("Session_Color_Pick_Manual_Entry", tv_onclick_color_manual_entry_txt.getText().toString(), getActivity());

//                        Log.e("tv_onclick_io_number_manual_entry_txt", tv_onclick_io_number_manual_entry_txt.getText().toString());
//                        Log.e("tv_onclick_color_manual_entry_txt", tv_onclick_color_manual_entry_txt.getText().toString());
//                        Log.e("tv_onclick_tailor_txt", tv_onclick_tailor_txt.getText().toString());
//                        Log.e("tv_onclick_operation", tv_onclick_operation.getText().toString());
//                        Log.e("tv_onclick_shift", tv_onclick_shift.getText().toString());

                        String s1 = SessionSave.getSession("Game_Entry_Point_Value", getActivity());
                        if (s1.equals("2")) {
                            if (!(tv_onclick_io_number_manual_entry_txt.getText().toString().isEmpty())
                                    && !(tv_onclick_color_manual_entry_txt.getText().toString().isEmpty())
                                    && !(tv_onclick_tailor_txt.getText().toString().isEmpty())
                                    && (!(tv_onclick_operation.getText().toString().isEmpty()))
                                    && (!(tv_onclick_shift.getText().toString().isEmpty()))
                                    && (!(tv_onclick_select_piece_for_sewing.getText().toString().isEmpty()))) {
                                btn_save_new.setEnabled(true);
                                btn_save_new.setBackground(getResources().getDrawable(R.drawable.sewing_entry_button_bg));

//                                Log.e("inside_tv_onclick_io_number_txt", tv_onclick_io_number_manual_entry_txt.getText().toString());
//                                Log.e("inside_tv_onclick_color_txt", tv_onclick_color_manual_entry_txt.getText().toString());
//                                Log.e("inside_tv_onclick_tailor_txt", tv_onclick_tailor_txt.getText().toString());
//                                Log.e("inside_tv_onclick_operation", tv_onclick_operation.getText().toString());
//                                Log.e("inside_tv_onclick_shift", tv_onclick_shift.getText().toString());
//                                Log.e("inside_tv_onclick_no_of_operation", tv_onclick_select_piece_for_sewing.getText().toString());
//                                Log.e("inside_Game_Entry_Point_Value_dialog", s1);
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
        }
    }

    private void initialiseDetectorsAndSources() {
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
//                Toast.makeText(getActivity(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barCode = detections.getDetectedItems();
                if (barCode.size() > 0) {
                    setBarCode(barCode);
                }
            }
        });
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

    private void setBarCode(final SparseArray<Barcode> barCode) {
        textViewBarCodeValue.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                intentData = barCode.valueAt(0).displayValue;
                textViewBarCodeValue.setText(intentData);


                copyToClipBoard(intentData);
                if (!(textViewBarCodeValue.getText().toString().isEmpty())) {
                    if (mAllowShake) {
                        if (Build.VERSION.SDK_INT >= 26) {
                            Log.e("vibrator_log_if", "true");
                            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.EFFECT_TICK));
                        } else {
                            Log.e("vibrator_log_else", "true");
                            vibrator.vibrate(100);
                        }
                    }
                    scanner_layout.setVisibility(View.GONE);
//                    Log.e("textViewBarCodeValue_loggg", "" + textViewBarCodeValue.getText().toString());
//                    Log.e("intentData_loggg", "" + intentData);

                    rv_sewing.setVisibility(View.GONE);
                    constraintLayout_sewing_rv_layout.setVisibility(View.GONE);
//                    Get_Scan_Details(intentData);
                }
            }
        });
    }

    private void copyToClipBoard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("QR code Scanner", text);
        assert clipboard != null;
        clipboard.setPrimaryClip(clip);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                getActivity().finish();
            else
                openCamera();
        } else
            getActivity().finish();
    }

    private void Get_Io_Number_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("usname", str_session_username);
            jsonObject.put("token", str_session_logintoken);
            Log.e("json_io_number", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Operator_Model> call = apiInterface.IO_NUMBER_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Operator_Model>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(Call<Operator_Model> call, Response<Operator_Model> response) {
                    if (response.code() == 200) {
                        String str_status = "";
                        assert response.body() != null;
                        str_status = Objects.requireNonNull(response.body()).status;
                        if (response.isSuccessful()) {
                            if (str_status.equalsIgnoreCase("error")) {
                                dialog_for_io_number.dismiss();
                                Toast.makeText(getActivity(), "" + response.body().message, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getActivity(), "This user doesn't have permission to access the Io Number data", Toast.LENGTH_SHORT).show();
                            } else {
                                rv_io_number.setVisibility(View.VISIBLE);
                                io_number_adapter_for_checking = new Io_Number_Adapter_For_Checking(getActivity(), response.body().data);
                                rv_io_number.setAdapter(io_number_adapter_for_checking);
                            }
                        }
                    } else if (response.code() == 401) {
                        dialog_for_io_number.dismiss();
                        Toast.makeText(getActivity(), "" + response.message(), Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 500) {
                        dialog_for_io_number.dismiss();
                        Toast.makeText(getActivity(), "" + response.message(), Toast.LENGTH_SHORT).show();
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

    private void Get_Color_Details() {
        try {
            String str_io_number_local = SessionSave.getSession("Session_IO_Number_Manual_Entry", getActivity());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("usname", str_session_username);
            jsonObject.put("token", str_session_logintoken);
            jsonObject.put("io_no", str_io_number_local);
            Log.e("json_color", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Operator_Model> call = apiInterface.COLOR_CODE_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Operator_Model>() {
                @Override
                public void onResponse(Call<Operator_Model> call, Response<Operator_Model> response) {
                    if (response.code() == 200) {
                        String str_status = "";
                        assert response.body() != null;
                        str_status = response.body().status;
                        if (response.isSuccessful()) {
                            if (str_status.equalsIgnoreCase("error")) {
                                dialog_for_colors.dismiss();
                                Toast.makeText(getActivity(), "" + response.body().message, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getActivity(), "This user doesn't have permission to access the Color data", Toast.LENGTH_SHORT).show();
                            } else {
                                rv_colors.setVisibility(View.VISIBLE);
                                color_adapter_for_checking = new Color_Adapter_For_Checking(getActivity(), response.body().data);
                                rv_colors.setAdapter(color_adapter_for_checking);
                            }
                        }
                    } else if (response.code() == 401) {
                        dialog_for_io_number.dismiss();
                        Toast.makeText(getActivity(), "" + response.message(), Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 500) {
                        dialog_for_io_number.dismiss();
                        Toast.makeText(getActivity(), "" + response.message(), Toast.LENGTH_SHORT).show();
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

    private void Get_No_pieces_Call() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("usname", str_session_username);
            jsonObject.put("token", str_session_logintoken);
//            Log.e("json_operation", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Operator_Model> call = apiInterface.OPERATION_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Operator_Model>() {
                @Override
                public void onResponse(Call<Operator_Model> call, Response<Operator_Model> response) {
                    if (response.code() == 200) {
                        String str_status = "";
                        str_status = Objects.requireNonNull(response.body()).status;
                        if (response.isSuccessful()) {
                            if (str_status.equalsIgnoreCase("error")) {
                                dialog_for_pieces.dismiss();
                                Toast.makeText(getActivity(), "" + response.body().message, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getActivity(), "This user doesn't have permission to access the Pieces data", Toast.LENGTH_SHORT).show();
                            } else {
                                rv_no_of_pieces.setVisibility(View.VISIBLE);
                                select_no_pieces_adapter = new Select_No_Pieces_Adapter(getActivity(), response.body().data);
                                rv_no_of_pieces.setAdapter(select_no_pieces_adapter);
                            }
                        }
                    } else if (response.code() == 401) {
                        dialog_for_io_number.dismiss();
                        Toast.makeText(getActivity(), "" + response.message(), Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 500) {
                        dialog_for_io_number.dismiss();
                        Toast.makeText(getActivity(), "" + response.message(), Toast.LENGTH_SHORT).show();
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

    private void Get_Save_Sewing_Details(final String str_session_game_entry_point_value_temp) {
        String str_total_pics_02 = SessionSave.getSession("Session_Total_Pieces_Sewing", getActivity());
        String str_total_pics_01 = tv_total_pieces.getText().toString();
//        Log.e("str_total_pics_01_logg", str_total_pics_01);
//        Log.e("str_total_pics_02_logg", str_total_pics_02);
//        Log.e("str_session_game_entry_point_value_temp_btnsddsd", str_session_game_entry_point_value_temp);

        try {
            String str_io_num = SessionSave.getSession("Session_IO_Number", getActivity());
            String str_io_num_for_manual_entry = SessionSave.getSession("Session_IO_Number_Manual_Entry", getActivity());

            String str_color_pick = SessionSave.getSession("Session_Color_Pick", getActivity());
            String str_color_pick_for_manual_entry = SessionSave.getSession("Session_Color_Pick_Manual_Entry", getActivity());

            String str_operator_id = SessionSave.getSession("Session_Onclick_id_value_For_Checking_Person_Sewing_dummy", getActivity());
            String str_opeartor_name = SessionSave.getSession("Session_Checking_Person_Sewing_dummy", getActivity());

            String str_operation_id = SessionSave.getSession("Session_Onclick_id_value_For_Operation_Sewing", getActivity());
            String str_operation_name = SessionSave.getSession("Session_Operation_Sewing", getActivity());

            String str_shift_id = SessionSave.getSession("Session_Onclick_id_value_For_Shift_Timing_Sewing", getActivity());
            String str_shift_name = SessionSave.getSession("Session_Shift_Timing_Sewing", getActivity());


            JSONObject jsonObject = new JSONObject();
            if (str_session_game_entry_point_value_temp.equals("1")) {
                jsonObject.put("usname", str_session_username);
                jsonObject.put("token", str_session_logintoken);
                jsonObject.put("bundle_no", str_bundle_num);
                jsonObject.put("io_no", str_io_num);

                jsonObject.put("operator_id", str_operator_id);
                jsonObject.put("operator_name", str_opeartor_name);

                jsonObject.put("operation_id", str_operation_id);
                jsonObject.put("operation_name", str_operation_name);

                jsonObject.put("shift_id", str_shift_id);
                jsonObject.put("shift_name", str_shift_name);

                jsonObject.put("entry_type", "system");
                jsonObject.put("color_code", str_color_pick);
                jsonObject.put("tot_pcs", str_total_pics_01);
                Log.e("Get_assign_json_for_01", jsonObject.toString());
            }
            if (str_session_game_entry_point_value_temp.equals("2")) {
                jsonObject.put("usname", str_session_username);
                jsonObject.put("token", str_session_logintoken);
                jsonObject.put("bundle_no", str_bundle_num);
                jsonObject.put("io_no", str_io_num_for_manual_entry);

                jsonObject.put("operator_id", str_operator_id);
                jsonObject.put("operator_name", str_opeartor_name);

                jsonObject.put("operation_id", str_operation_id);
                jsonObject.put("operation_name", str_operation_name);

                jsonObject.put("shift_id", str_shift_id);
                jsonObject.put("shift_name", str_shift_name);

                jsonObject.put("entry_type", "manual");
                jsonObject.put("color_code", str_color_pick_for_manual_entry);
                jsonObject.put("tot_pcs", str_total_pics_02);
                Log.e("Get_assign_json_for_02", jsonObject.toString());
            }
            APIInterface apiInterface = Factory.getClient();
            Call<Full_App_Model_Class> call = apiInterface.GET_ASSIGN_SEWING_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Full_App_Model_Class>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResponse(Call<Full_App_Model_Class> call, Response<Full_App_Model_Class> response) {
                    if (response.isSuccessful()) {
                        String str_status = "";
                        String str_message = "";
                        assert response.body() != null;
                        str_status = response.body().status;
                        str_message = response.body().message;
//                        Log.e("Responsee", String.valueOf(Objects.requireNonNull(response.body()).message));
//                        Log.e("inside_str_session_game_entry_point_value_temp",str_session_game_entry_point_value_temp);
                        if (str_status.equalsIgnoreCase("success")) {
                            if (str_session_game_entry_point_value_temp.equals("1")) {
                                if (constraintLayout_recyclerview.getVisibility() == View.VISIBLE) {

                                    SessionSave.clearSession("Session_IO_Number", getActivity());
                                    SessionSave.clearSession("Session_Color_Pick", getActivity());
                                    SessionSave.clearSession("Session_Checking_Person_Sewing_dummy", getActivity());
                                    SessionSave.clearSession("Session_Operation_Sewing", getActivity());
                                    SessionSave.clearSession("Session_Shift_Timing_Sewing", getActivity());
                                    SessionSave.clearSession("Session_Total_Pieces_Sewing", getActivity());

                                    tv_onclick_io_number_manual_entry_txt.setText("");
                                    tv_onclick_color_manual_entry_txt.setText("");
                                    tv_onclick_tailor_txt.setText("");
                                    tv_onclick_operation.setText("");
                                    tv_onclick_shift.setText("");
                                    tv_onclick_select_piece_for_sewing.setText("");

                                    tv_io_number_normal_manual_entry_txt.setText(R.string.io_number_txt);
                                    tv_color_normal_manual_entry_txt.setText(R.string.color_txt);
                                    tv_normal_tailor_txt.setText(R.string.select_tailor_txt);
                                    tv_select_operation.setText(R.string.select_operation_txt);
                                    tv_select_shift.setText(R.string.select_shift_txt);
                                    tv_normal_select_piece_for_sewing.setText(R.string.select_pieces_txt);

                                    Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), getResources().getString(R.string.successfully_submitted_txt), 1);
                                    Sewing_Entry_Act.tv_exit_txt.setVisibility(View.GONE);
                                    Get_Sewing_Entry_Details();
                                }
                            } else if (str_session_game_entry_point_value_temp.equals("2")) {

                                SessionSave.clearSession("Session_IO_Number_Manual_Entry", getActivity());
                                SessionSave.clearSession("Session_Color_Pick_Manual_Entry", getActivity());
                                SessionSave.clearSession("Session_Checking_Person_Sewing_dummy", getActivity());
                                SessionSave.clearSession("Session_Operation_Sewing", getActivity());
                                SessionSave.clearSession("Session_Shift_Timing_Sewing", getActivity());
                                SessionSave.clearSession("Session_Total_Pieces_Sewing", getActivity());


                                tv_onclick_io_number_manual_entry_txt.setText("");
                                tv_onclick_color_manual_entry_txt.setText("");
                                tv_onclick_tailor_txt.setText("");
                                tv_onclick_operation.setText("");
                                tv_onclick_shift.setText("");
                                tv_onclick_select_piece_for_sewing.setText("");

                                tv_io_number_normal_manual_entry_txt.setText(R.string.io_number_txt);
                                tv_color_normal_manual_entry_txt.setText(R.string.color_txt);
                                tv_normal_tailor_txt.setText(R.string.select_tailor_txt);
                                tv_select_operation.setText(R.string.select_operation_txt);
                                tv_select_shift.setText(R.string.select_shift_txt);
                                tv_normal_select_piece_for_sewing.setText(R.string.select_pieces_txt);

                                Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), getResources().getString(R.string.successfully_submitted_txt), 1);
                                Get_Sewing_Entry_Details();

                                constraintLayout_inside_io_number_color.setVisibility(View.GONE);
                                constraintLayout_inside_api_values.setVisibility(View.GONE);
                                constraintLayout_qr_code_scan.setVisibility(View.GONE);
                                constraintLayout_inside_api_values.setVisibility(View.GONE);
                                constraintLayout_recyclerview.setVisibility(View.VISIBLE);
                                scroll_view_layout.setVisibility(View.GONE);
                                btn_save_new.setVisibility(View.GONE);
                                constraintLayout_sewing_rv_layout.setVisibility(View.VISIBLE);
                                constraintLayout_recyclerview.setVisibility(View.VISIBLE);
                                fab.setVisibility(View.VISIBLE);
                                rv_sewing.setVisibility(View.VISIBLE);

/*                                str_session_io_number = SessionSave.getSession("Session_IO_Number", getActivity());
                                str_session_io_number_manual_entry = SessionSave.getSession("Session_IO_Number_Manual_Entry", getActivity());
                                str_session_color_pick_manual_entry = SessionSave.getSession("Session_Color_Pick_Manual_Entry", getActivity());
                                str_session_color_pick = SessionSave.getSession("Session_Color_Pick", getActivity());
                                //        str_session_checking_person = SessionSave.getSession("Session_Checking_Person_Sewing", getActivity());
                                str_session_checking_person_dummy = SessionSave.getSession("Session_Checking_Person_Sewing_dummy", getActivity());
                                str_session_operation = SessionSave.getSession("Session_Operation_Sewing", getActivity());
                                str_session_shift_timing = SessionSave.getSession("Session_Shift_Timing_Sewing", getActivity());
                                str_session_num_of_pieces = SessionSave.getSession("Session_Total_Pieces_Sewing", getActivity());
                                sstr_session_game_entry_point_value = SessionSave.getSession("Game_Entry_Point_Value", getActivity());*/
                            }
                        } else {
                            Toast.makeText(getActivity(), "" + str_message, Toast.LENGTH_SHORT).show();
                            if (str_session_game_entry_point_value.equals("1")) {
                                constraintLayout_recyclerview.setVisibility(View.GONE);
                                constraintLayout_sewing_rv_layout.setVisibility(View.GONE);
                                constraintLayout_inside_api_values.setVisibility(View.VISIBLE);
                                constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                                constraintLayout_inside_io_number_color.setVisibility(View.GONE);
                                scroll_view_layout.setVisibility(View.VISIBLE);
                                btn_save_new.setVisibility(View.VISIBLE);
                                fab.setVisibility(View.GONE);
                                rv_sewing.setVisibility(View.GONE);
                            } else if (str_session_game_entry_point_value.equals("2")) {
                                constraintLayout_inside_io_number_color.setVisibility(View.VISIBLE);
                                constraintLayout_inside_api_values.setVisibility(View.VISIBLE);
                                constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                                constraintLayout_inside_api_values.setVisibility(View.GONE);
                                constraintLayout_recyclerview.setVisibility(View.GONE);
                                scroll_view_layout.setVisibility(View.VISIBLE);
                                btn_save_new.setVisibility(View.VISIBLE);
                                constraintLayout_sewing_rv_layout.setVisibility(View.GONE);
                                constraintLayout_recyclerview.setVisibility(View.GONE);
                                fab.setVisibility(View.GONE);
                                rv_sewing.setVisibility(View.GONE);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<Full_App_Model_Class> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                        String str_status = "";
                        str_status = Objects.requireNonNull(response.body()).status;
                        if (response.isSuccessful()) {
                            if (str_status.equalsIgnoreCase("error")) {
                                dialog_for_shift_timing.dismiss();
                                Toast.makeText(getActivity(), "" + response.body().message, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getActivity(), "This user doesn't have permission to access the Shift Timing data", Toast.LENGTH_SHORT).show();
                            } else {
                                rv_shift_timing.setVisibility(View.VISIBLE);
                                shift_timing_adapter = new Shift_Timing_Adapter(getActivity(), response.body().data);
                                rv_shift_timing.setAdapter(shift_timing_adapter);
                            }
                        }
                    } else if (response.code() == 401) {
                        dialog_for_io_number.dismiss();
                        Toast.makeText(getActivity(), "" + response.message(), Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 500) {
                        dialog_for_io_number.dismiss();
                        Toast.makeText(getActivity(), "" + response.message(), Toast.LENGTH_SHORT).show();
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

    private void Get_Operator_Call() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("usname", str_session_username);
            jsonObject.put("token", str_session_logintoken);
            jsonObject.put("role", "sewing");
            APIInterface apiInterface = Factory.getClient();
            Call<Operator_Model> call = apiInterface.OPERATOR_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Operator_Model>() {
                @Override
                public void onResponse(Call<Operator_Model> call, Response<Operator_Model> response) {
                    if (response.code() == 200) {
                        String str_status = "";
                        assert response.body() != null;
                        str_status = Objects.requireNonNull(response.body()).status;
                        if (response.isSuccessful()) {
                            if (str_status.equalsIgnoreCase("error")) {
                                dialog_for_tailor.dismiss();
                                Toast.makeText(getActivity(), "" + response.body().message, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getActivity(), "This user doesn't have permission to access the Tailor data", Toast.LENGTH_SHORT).show();
                            } else {
                                rv_tailors_select.setVisibility(View.VISIBLE);
                                tailors_select_adapter = new Tailors_Select_Adapter(getActivity(), response.body().data);
                                rv_tailors_select.setAdapter(tailors_select_adapter);
                            }
                        }
                    } else if (response.code() == 401) {
                        dialog_for_io_number.dismiss();
                        Toast.makeText(getActivity(), "" + response.message(), Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 500) {
                        dialog_for_io_number.dismiss();
                        Toast.makeText(getActivity(), "" + response.message(), Toast.LENGTH_SHORT).show();
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

    @SuppressLint("LongLogTag")
    private void Get_Operation_Call() {
        String s1 = "";
        String sss = tv_onclick_io_number_manual_entry_txt.getText().toString().replaceAll("\\/", "/");
//        Log.e("tv_onclick_io_number_manual_entry_txt_samkdal", sss);
        str_session_game_entry_point_value = SessionSave.getSession("Game_Entry_Point_Value", Objects.requireNonNull(getActivity()));
//        Log.e("ssssstr_session_game_entry_point_value", str_session_game_entry_point_value);
        if (str_session_game_entry_point_value.equals("1")) {
            str_session_io_number = SessionSave.getSession("Session_IO_Number", getActivity());
        } else if (str_session_game_entry_point_value.equals("2")) {
            str_session_io_number_manual_entry = SessionSave.getSession("Session_IO_Number_Manual_Entry", getActivity());
        }
//        Log.e("str_session_io_number_operator", str_session_io_number);
//        Log.e("str_session_io_number_manual_entry_op", str_session_io_number_manual_entry);
        try {
            JSONObject jsonObject = new JSONObject();
            if (str_session_game_entry_point_value.equals("1")) {
                if (str_session_io_number.equalsIgnoreCase("No data")) {
                    jsonObject.put("usname", str_session_username);
                    jsonObject.put("token", str_session_logintoken);
                    jsonObject.put("io_no", tv_io_number.getText().toString());
                    jsonObject.put("type", "sewing");
                    Log.e("json_operation_if_01", jsonObject.toString());
                } else {
                    jsonObject.put("usname", str_session_username);
                    jsonObject.put("token", str_session_logintoken);
                    jsonObject.put("io_no", str_session_io_number);
                    jsonObject.put("type", "sewing");
                    Log.e("json_operation_else_01", jsonObject.toString());
                }

            } else if (str_session_game_entry_point_value.equals("2")) {
                if (str_session_io_number_manual_entry.equalsIgnoreCase("No data")) {
                    jsonObject.put("usname", str_session_username);
                    jsonObject.put("token", str_session_logintoken);
                    jsonObject.put("io_no", sss);
                    jsonObject.put("type", "sewing");
                    Log.e("json_operation_if_02", jsonObject.toString());
                } else {
                    s1 = str_session_io_number_manual_entry.replaceAll("\\/", "/");
//                    Log.e("s11111_02", s1);
                    jsonObject.put("usname", str_session_username);
                    jsonObject.put("token", str_session_logintoken);
                    jsonObject.put("io_no", s1);
                    jsonObject.put("type", "sewing");
//                    Log.e("json_operation_else_02", jsonObject.toString());
                }
            }
            APIInterface apiInterface = Factory.getClient();
            Call<Operator_Model> call = apiInterface.OPERATION_RESPONSE_CALL("application/json", jsonObject.toString());
            String finalS = s1;
            call.enqueue(new Callback<Operator_Model>() {
                @Override
                public void onResponse(Call<Operator_Model> call, Response<Operator_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            String str_message = response.body().message;
                            assert response.body() != null;
                            if (response.body().data.isEmpty()) {
                                dialog_for_operation.dismiss();
                                Toast.makeText(getActivity(), "" + str_message, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getActivity(), "No records found for operation.", Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getActivity(), "This Io Number doesn't have operation", Toast.LENGTH_SHORT).show();
                            } else {
                                rv_select_operation.setVisibility(View.VISIBLE);
                                operation_select_adapter = new Operation_Select_Adapter(getActivity(), response.body().data);
                                rv_select_operation.setAdapter(operation_select_adapter);
                            }
                        }
                    } else if (response.code() == 401) {
                        dialog_for_operation.dismiss();
                        Toast.makeText(getActivity(), "" + response.message(), Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 500) {
                        dialog_for_operation.dismiss();
                        Toast.makeText(getActivity(), "" + response.message(), Toast.LENGTH_SHORT).show();
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


    /*This adapter class used for Operators*/
    private class Tailors_Select_Adapter extends RecyclerView.Adapter<Tailors_Select_Adapter.ViewHolder> {
        ArrayList<Operator_Model.Data> stringArrayList;
        Context mContext;


        public Tailors_Select_Adapter(FragmentActivity activity, ArrayList<Operator_Model.Data> data) {
            this.mContext = activity;
            this.stringArrayList = data;
        }

        @NonNull
        @Override
        public Tailors_Select_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view;
            view = inflater.inflate(R.layout.tailors_select_details_layout, parent, false);
            return new Tailors_Select_Adapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Tailors_Select_Adapter.ViewHolder holder, int position) {
            holder.tv_tailors_select.setText(stringArrayList.get(position).name);

            holder.tv_tailors_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.tv_tailors_select.getText().toString();
                    tv_normal_tailor_txt.setText("");
                    String s1 = stringArrayList.get(position).id;
//                    Log.e("s1111q21", s1);
                    SessionSave.SaveSession("Session_Onclick_id_value_For_Checking_Person_Sewing_dummy", s1, getActivity());
                    SessionSave.SaveSession("Session_Checking_Person_Sewing_dummy", holder.tv_tailors_select.getText().toString(), getActivity());
                    tv_onclick_tailor_txt.setText(holder.tv_tailors_select.getText().toString());
                    dialog_for_tailor.dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return stringArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_tailors_select;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_tailors_select = itemView.findViewById(R.id.tv_tailors_select);

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
                    tv_select_operation.setText("");
                    String s1 = stringArrayList.get(position).id;
                    SessionSave.SaveSession("Session_Onclick_id_value_For_Operation_Sewing", s1, getActivity());
                    SessionSave.SaveSession("Session_Operation_Sewing", holder.tv_operation_select.getText().toString(), getActivity());
                    tv_onclick_operation.setText(holder.tv_operation_select.getText().toString());
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

    private class Shift_Timing_Adapter extends RecyclerView.Adapter<Shift_Timing_Adapter.ViewHolder> {
        ArrayList<Operator_Model.Data> stringArrayList;
        Context mContext;

        public Shift_Timing_Adapter(FragmentActivity activity, ArrayList<Operator_Model.Data> data) {
            this.mContext = activity;
            this.stringArrayList = data;
        }

        @NonNull
        @Override
        public Shift_Timing_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view;
            view = inflater.inflate(R.layout.select_shift_details_layout, parent, false);
            return new Shift_Timing_Adapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Shift_Timing_Adapter.ViewHolder holder, int position) {
            String str_from_time = stringArrayList.get(position).from_time;
            String str_to_time = stringArrayList.get(position).to_time;
            String str_merge_timings = str_from_time + "  to  " + str_to_time;

            holder.tv_from_shift_timing.setText(str_merge_timings);
            holder.tv_from_shift_timing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_select_shift.setText("");
//                    Log.e("str_merge_timings", str_merge_timings);
                    tv_onclick_shift.setText(holder.tv_from_shift_timing.getText().toString());
                    String s1 = stringArrayList.get(position).id;
                    SessionSave.SaveSession("Session_Onclick_id_value_For_Shift_Timing_Sewing", s1, getActivity());
                    SessionSave.SaveSession("Session_Shift_Timing_Sewing", holder.tv_from_shift_timing.getText().toString(), getActivity());
                    dialog_for_shift_timing.dismiss();
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


    private class Select_No_Pieces_Adapter extends RecyclerView.Adapter<Select_No_Pieces_Adapter.ViewHolder> {
        ArrayList<Operator_Model.Data> stringArrayList;
        Context mContext;

        public Select_No_Pieces_Adapter(FragmentActivity activity, ArrayList<Operator_Model.Data> data) {
            this.mContext = activity;
            this.stringArrayList = data;
        }

        @NonNull
        @Override
        public Select_No_Pieces_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view;
            view = inflater.inflate(R.layout.select_no_pieces_details_layout, parent, false);
            return new Select_No_Pieces_Adapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Select_No_Pieces_Adapter.ViewHolder holder, int position) {
            holder.tv_select_no_pieces.setText(stringArrayList.get(position).name);

            holder.tv_select_no_pieces.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.tv_select_no_pieces.getText().toString();
                    tv_normal_select_piece_for_sewing.setText("");
                    String s1 = stringArrayList.get(position).id;
                    SessionSave.SaveSession("Session_Onclick_id_value", s1, getActivity());
                    SessionSave.SaveSession("Session_No_Of_Pieces_Sewing", holder.tv_select_no_pieces.getText().toString(), getActivity());
                    tv_onclick_select_piece_for_sewing.setText(holder.tv_select_no_pieces.getText().toString());
                    dialog_for_operation.dismiss();

                    if (str_session_game_entry_point_value.equals("1")) {
                        if (!(tv_onclick_tailor_txt.getText().toString().isEmpty())
                                && (!(tv_onclick_operation.getText().toString().isEmpty()))
                                && (!(tv_onclick_shift.getText().toString().isEmpty()))
                                && (!(tv_onclick_select_piece_for_sewing.getText().toString().isEmpty()))) {
                            btn_save_new.setEnabled(true);
                            btn_save_new.setBackground(mContext.getResources().getDrawable(R.drawable.sewing_entry_button_bg));
                        }
                    } else if (str_session_game_entry_point_value.equals("2")) {
                        if (!(tv_onclick_io_number_manual_entry_txt.getText().toString().isEmpty())
                                && !(tv_onclick_color_manual_entry_txt.getText().toString().isEmpty())
                                && !(tv_onclick_tailor_txt.getText().toString().isEmpty())
                                && (!(tv_onclick_operation.getText().toString().isEmpty()))
                                && (!(tv_onclick_shift.getText().toString().isEmpty()))
                                && (!(tv_onclick_select_piece_for_sewing.getText().toString().isEmpty()))) {
                            btn_save_new.setEnabled(true);
                            btn_save_new.setBackground(mContext.getResources().getDrawable(R.drawable.sewing_entry_button_bg));
                        }
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return stringArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_select_no_pieces;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_select_no_pieces = itemView.findViewById(R.id.tv_select_no_pieces);
            }
        }
    }


    private class Io_Number_Adapter_For_Checking extends RecyclerView.Adapter<Io_Number_Adapter_For_Checking.ViewHolder> {
        ArrayList<Operator_Model.Data> stringArrayList;
        Context mContext;


        public Io_Number_Adapter_For_Checking(FragmentActivity activity, ArrayList<Operator_Model.Data> data) {
            this.mContext = activity;
            this.stringArrayList = data;
        }

        @NonNull
        @Override
        public Io_Number_Adapter_For_Checking.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view;
            view = inflater.inflate(R.layout.io_number_adapter_details_layout, parent, false);
            return new Io_Number_Adapter_For_Checking.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Io_Number_Adapter_For_Checking.ViewHolder holder, int position) {
            String str_from_time = stringArrayList.get(position).io_no;
            holder.tv_io_number_pick.setText(str_from_time);
            holder.tv_io_number_pick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_io_number_normal_manual_entry_txt.setText("");
                    tv_onclick_io_number_manual_entry_txt.setText("");
                    SessionSave.SaveSession("Session_IO_Number_Manual_Entry", holder.tv_io_number_pick.getText().toString(), getActivity());
                    tv_onclick_io_number_manual_entry_txt.setText(holder.tv_io_number_pick.getText().toString());
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

    private class Color_Adapter_For_Checking extends RecyclerView.Adapter<Color_Adapter_For_Checking.ViewHolder> {
        ArrayList<Operator_Model.Data> stringArrayList;
        Context mContext;

        public Color_Adapter_For_Checking(FragmentActivity activity, ArrayList<Operator_Model.Data> data) {
            this.mContext = activity;
            this.stringArrayList = data;
        }

        @NonNull
        @Override
        public Color_Adapter_For_Checking.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view;
            view = inflater.inflate(R.layout.color_adapter_details_layout, parent, false);
            return new Color_Adapter_For_Checking.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Color_Adapter_For_Checking.ViewHolder holder, int position) {
            String str_from_time = stringArrayList.get(position).color_code;
            holder.tv_color_pick.setText(str_from_time);
            holder.tv_color_pick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_color_normal_manual_entry_txt.setText("");
                    SessionSave.SaveSession("Session_Color_Pick_Manual_Entry", holder.tv_color_pick.getText().toString(), getActivity());
                    tv_onclick_color_manual_entry_txt.setText(holder.tv_color_pick.getText().toString());
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

    @Override
    public void onResume() {
        super.onResume();
        mAllowShake = true;
        if (!(tv_onclick_io_number_manual_entry_txt.getText().toString().isEmpty())
                && !(tv_onclick_color_manual_entry_txt.getText().toString().isEmpty())
                && !(tv_onclick_tailor_txt.getText().toString().isEmpty())
                && (!(tv_onclick_operation.getText().toString().isEmpty()))
                && (!(tv_onclick_shift.getText().toString().isEmpty()))
                && (!(tv_onclick_select_piece_for_sewing.getText().toString().isEmpty()))) {
//            Toast.makeText(getActivity(), "onresume_toast", Toast.LENGTH_SHORT).show();
            btn_save_new.setEnabled(true);
            btn_save_new.setBackground(getResources().getDrawable(R.drawable.sewing_entry_button_bg));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (cameraSource != null) {
            try {
                cameraSource.release();
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        }

        mAllowShake = false;
        vibrator.cancel();
    }
}

