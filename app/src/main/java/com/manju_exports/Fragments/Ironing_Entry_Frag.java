package com.manju_exports.Fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.manju_exports.Adapters.Ironing_Adapter;
import com.manju_exports.Entries.Ironing_Entry_Act;
import com.manju_exports.Home_Activity;
import com.manju_exports.Interface.APIInterface;
import com.manju_exports.Interface.Factory;
import com.manju_exports.Model.Full_App_Model_Class;
import com.manju_exports.Model.Operator_Model;
import com.manju_exports.Model.Sewing_Model;
import com.manju_exports.R;
import com.manju_exports.ScannedBarcodeActivity;
import com.manju_exports.SessionSave;
import com.manju_exports.Toast_Message;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import io.supercharge.shimmerlayout.ShimmerLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ironing_Entry_Frag extends Fragment implements View.OnClickListener {

    public static ConstraintLayout constraintLayout_recyclerview,
            constraintLayout_qr_code_scan;
    private TextView textViewName,
            textViewAddress;
    private TextView tv_io_number_normal_txt, tv_onclick_io_number_txt;
    private TextView tv_color_normal_txt, tv_onclick_color_txt;
    private TextView tv_normal_checking_person_txt, tv_onclick_checking_person_txt;
    private TextView tv_normal_select_piece, tv_onclick_select_piece;
    private TextView tv_select_shift, tv_onclick_shift;
    ConstraintLayout constraintLayout_tailor_layout;

    public static Button btn_save_ironing;
    private Dialog dialog_for_io_number,
            dialog_for_colors,
            dialog_for_check_person,
            dialog_for_pieces,
            dialog_for_shift_timing;
    private Handler handler;
    private String str_session_username,
            str_session_logintoken,
            str_session_io_number,
            str_session_color_pick,
            str_session_checking_person,
            str_session_shift_timing,
            str_session_total_pieces,
            str_session_game_entry_point_value,
            str_session_io_number_manual_entry,
            str_session_color_pick_manual_entry;

    private RecyclerView rv_io_number;
    private RecyclerView rv_colors;
    private RecyclerView rv_checking_person;
    RecyclerView rv_entry_pieces;
    private RecyclerView rv_shift_timing;
    private RecyclerView rv_ironing;


    private ArrayList<String> ionumber_ArrayList = new ArrayList<>();
    private ArrayList<String> color_ArrayList = new ArrayList<>();
    private ArrayList<String> checking_person_ArrayList = new ArrayList<>();
    private ArrayList<String> selectshift_ArrayList = new ArrayList<>();

    private ConstraintLayout constraintLayout_io_number_layout;
    private ConstraintLayout constraintLayout_color_layout;
    private ConstraintLayout constraintLayout_check_person_layout;
    private ConstraintLayout constraintLayout_check_pieces_layout;
    private ConstraintLayout constraintLayout_select_shift;
    public static FloatingActionButton fab_for_ironing;


    private Shift_Timing_Adapter_For_Ironing Cheshift_timing_adapter_for_ironing;
    private Io_Number_Adapter_For_Ironing io_number_adapter_for_ironing;
    private Color_Adapter_For_Ironing color_adapter_for_ironing;
    private Checking_Person_Adapter_For_Ironing checking_person_adapter_for_ironing;

    private EditText et_num_from_pieces_dialog;
    private TextView tv_ok_from_pieces_dialog;
    private String str_num_of_pieces;

    private String str_qrcode_value = "";
    String str_bar_code_scan_value;
    private String str_qrcode_value_others = "";
    ShimmerLayout shimmer_for_ironing;
    Ironing_Adapter ironing_adapter;

    ScrollView scroll_view_layout;
    ConstraintLayout constraintLayout_ironing_rv_layout;


    private String str_io_num,
            str_art_num,
            str_date,
            str_color_code,
            str_size,
            str_lot_num,
            str_total_pieces;
    String str_bundle_number = "";

    TextView tv_no_data_available;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("LongLogTag")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.ironing_entry_frag_lay, container, false);
        Home_Activity.tv_title_txt.setText(getResources().getString(R.string.ironing_txt));
        Home_Activity.toolbar.setBackgroundColor(getResources().getColor(R.color.purple_color));
        tv_io_number_normal_txt = view.findViewById(R.id.tv_io_number_normal_txt);
        tv_color_normal_txt = view.findViewById(R.id.tv_color_normal_txt);
        tv_normal_checking_person_txt = view.findViewById(R.id.tv_normal_checking_person_txt);
        tv_normal_select_piece = view.findViewById(R.id.tv_normal_select_piece);
        tv_select_shift = view.findViewById(R.id.tv_select_shift);
        shimmer_for_ironing = view.findViewById(R.id.shimmer_for_ironing);
        tv_onclick_io_number_txt = view.findViewById(R.id.tv_onclick_io_number_txt);
        tv_onclick_color_txt = view.findViewById(R.id.tv_onclick_color_txt);
        tv_onclick_checking_person_txt = view.findViewById(R.id.tv_onclick_checking_person_txt);
        tv_onclick_select_piece = view.findViewById(R.id.tv_onclick_select_piece);
        tv_onclick_shift = view.findViewById(R.id.tv_onclick_shift);
        rv_ironing = view.findViewById(R.id.rv_ironing);
        scroll_view_layout = view.findViewById(R.id.scroll_view_layout);
        constraintLayout_ironing_rv_layout = view.findViewById(R.id.constraintLayout_ironing_rv_layout);

        tv_no_data_available = view.findViewById(R.id.tv_no_data_available);
        tv_no_data_available.setVisibility(View.GONE);

        rv_ironing.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_ironing.setHasFixedSize(true);

        fab_for_ironing = view.findViewById(R.id.fab_for_ironing);
        btn_save_ironing = view.findViewById(R.id.btn_save_ironing);
        constraintLayout_qr_code_scan = view.findViewById(R.id.constraintLayout_qr_code_scan);
        constraintLayout_recyclerview = view.findViewById(R.id.constraintLayout_recyclerview);

        constraintLayout_io_number_layout = view.findViewById(R.id.constraintLayout_io_number_layout);
        constraintLayout_color_layout = view.findViewById(R.id.constraintLayout_color_layout);
        constraintLayout_check_person_layout = view.findViewById(R.id.constraintLayout_check_person_layout);
        constraintLayout_check_pieces_layout = view.findViewById(R.id.constraintLayout_check_pieces_layout);
        constraintLayout_select_shift = view.findViewById(R.id.constraintLayout_select_shift);

        constraintLayout_io_number_layout.setOnClickListener(this);
        constraintLayout_color_layout.setOnClickListener(this);
        constraintLayout_check_person_layout.setOnClickListener(this);
        constraintLayout_check_pieces_layout.setOnClickListener(this);
        constraintLayout_select_shift.setOnClickListener(this);
        fab_for_ironing.setOnClickListener(this);
        btn_save_ironing.setOnClickListener(this);

        shimmer_for_ironing.setVisibility(View.VISIBLE);
        constraintLayout_ironing_rv_layout.setVisibility(View.VISIBLE);
        shimmer_for_ironing.startShimmerAnimation();

        str_session_username = SessionSave.getSession("Session_UserName", getActivity());
        str_session_logintoken = SessionSave.getSession("Session_Logintoken", getActivity());
        str_session_io_number = SessionSave.getSession("Session_IO_Number_Ironing", getActivity());
        str_session_color_pick = SessionSave.getSession("Session_Color_Pick_Ironing", getActivity());

        str_session_io_number_manual_entry = SessionSave.getSession("Session_IO_Number_Manual_Entry_For_Ironing", getActivity());
        str_session_color_pick_manual_entry = SessionSave.getSession("Session_Color_Pick_Manual_Entry_For_Ironing", getActivity());

        str_session_checking_person = SessionSave.getSession("Session_Checking_Person_Ironing", getActivity());
        str_session_shift_timing = SessionSave.getSession("Session_Shift_Timing_Ironing", getActivity());
        str_session_total_pieces = SessionSave.getSession("Session_Total_Pieces_Ironing", getActivity());
        str_session_game_entry_point_value = SessionSave.getSession("Game_Entry_Point_Value_For_Ironing", getActivity());

        Get_Iorning_Details();

        Bundle bundle = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        if (bundle == null) {
            str_qrcode_value = null;
        } else {
            str_qrcode_value = bundle.getString("Barcode_value");
            str_qrcode_value_others = bundle.getString("Barcode_value_Others");
            /*Set<String> stringSet = new HashSet<>();
            stringSet.add(str_qrcode_value);
            JSONObject jsonObject = null;
//            Log.e("main_stringSet", stringSet.toString());
            try {
                jsonObject = new JSONObject(str_qrcode_value);
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int i = 0; i < Objects.requireNonNull(jsonObject).length(); i++) {
                try {
                    str_bar_code_scan_value = jsonObject.getString("qr_no");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }*/

            if (str_qrcode_value != null) {
                Send_Scan_Details();
                rv_ironing.setVisibility(View.GONE);
                constraintLayout_ironing_rv_layout.setVisibility(View.GONE);
                constraintLayout_recyclerview.setVisibility(View.GONE);
                constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                Ironing_Entry_Act.tv_exit_txt.setVisibility(View.VISIBLE);
                scroll_view_layout.setVisibility(View.VISIBLE);
                fab_for_ironing.setVisibility(View.GONE);
                btn_save_ironing.setVisibility(View.VISIBLE);
                tv_normal_checking_person_txt.setText("");
                SessionSave.SaveSession("Session_Checking_Person_Ironing", tv_onclick_checking_person_txt.getText().toString(), getActivity());
            }
        }

        if (!(str_session_checking_person.equalsIgnoreCase("No data"))) {
            if (str_qrcode_value_others != null && str_qrcode_value != null) {
                tv_normal_checking_person_txt.setText("");
                SessionSave.SaveSession("Session_Checking_Person_Ironing", tv_onclick_checking_person_txt.getText().toString(), getActivity());
            } else if (str_qrcode_value != null) {
                tv_onclick_checking_person_txt.setText(str_session_checking_person);
                tv_normal_checking_person_txt.setText("");
                SessionSave.SaveSession("Session_Checking_Person_Ironing", tv_onclick_checking_person_txt.getText().toString(), getActivity());
            } else {
                tv_onclick_checking_person_txt.setText(str_session_checking_person);
                tv_normal_checking_person_txt.setText("");
                SessionSave.SaveSession("Session_Checking_Person_Ironing", tv_onclick_checking_person_txt.getText().toString(), getActivity());
            }
        } else {
            tv_normal_checking_person_txt.setText(R.string.select_checking_person_txt);
        }

        /*
         * If Session Value is non_empty means
         * */
//        Log.e("str_session_io_number_manual_entry", str_session_io_number_manual_entry);
        if (!(str_session_io_number_manual_entry.equalsIgnoreCase("No data"))) {
            tv_onclick_io_number_txt.setText(str_session_io_number_manual_entry);
            tv_io_number_normal_txt.setText("");
        } else {
            tv_io_number_normal_txt.setText(R.string.io_number_txt);
        }
//        Log.e("str_session_color_pick_manual_entry", str_session_color_pick_manual_entry);
        if (!(str_session_color_pick_manual_entry.equalsIgnoreCase("No data"))) {
            tv_onclick_color_txt.setText(str_session_color_pick_manual_entry);
            tv_color_normal_txt.setText("");
        } else {
            tv_color_normal_txt.setText(R.string.color_txt);
        }


        /*This is for input value 1*/
        if (!(str_session_io_number.equalsIgnoreCase("No data"))) {
            tv_onclick_io_number_txt.setText(str_session_io_number);
            tv_io_number_normal_txt.setText("");
        } else {
            tv_io_number_normal_txt.setText(R.string.io_number_txt);
        }

        if (!(str_session_color_pick.equalsIgnoreCase("No data"))) {
            tv_onclick_color_txt.setText(str_session_color_pick);
            tv_color_normal_txt.setText("");
        } else {
            tv_color_normal_txt.setText(R.string.color_txt);
        }

        if (!(str_session_shift_timing.equalsIgnoreCase("No data"))) {
            tv_onclick_shift.setText(str_session_shift_timing);
            tv_select_shift.setText("");
        } else {
            tv_select_shift.setText(R.string.select_shift_txt);
        }

        if (!(str_session_total_pieces.equalsIgnoreCase("No data"))) {
            tv_onclick_select_piece.setText(str_session_total_pieces);
            tv_normal_select_piece.setText("");
        } else {
            tv_normal_select_piece.setText(R.string.enter_pieces_txt);
        }


        /*
         * If Session Value is non_empty means
         * */
        if (!(str_session_checking_person.equalsIgnoreCase("No data"))) {
            tv_onclick_checking_person_txt.setText(str_session_checking_person);
            tv_normal_checking_person_txt.setText("");
        } else {
            tv_normal_checking_person_txt.setText(R.string.select_checking_person_txt);
        }

        /*
         * If Session Value is Empty means
         *
         * */
        if (str_session_checking_person.equalsIgnoreCase("No data")) {
            tv_normal_checking_person_txt.setText(R.string.select_checking_person_txt);
            tv_onclick_checking_person_txt.setText("");
        } else {
            tv_onclick_checking_person_txt.setText(str_session_checking_person);
        }

        if (str_session_shift_timing.equalsIgnoreCase("No data")) {
            tv_select_shift.setText(R.string.select_shift_txt);
            tv_onclick_shift.setText("");
        } else {
            tv_onclick_shift.setText(str_session_shift_timing);
        }

        if (str_session_checking_person.equalsIgnoreCase("No data") || str_session_checking_person.equals("")) {
            tv_normal_checking_person_txt.setText(R.string.select_checking_person_txt);
            tv_onclick_checking_person_txt.setText("");
        } else {
            tv_onclick_checking_person_txt.setText(str_session_checking_person);
            tv_normal_checking_person_txt.setText("");
        }



        /*
         *Created on  09-05-2020
         * If any of one input value is empty means button is disable this is  for entry point value 1 or 2
         * */
        if (str_session_game_entry_point_value.equals("1")) {
            if ((tv_onclick_io_number_txt.getText().toString().equals(""))
                    || (tv_onclick_color_txt.getText().toString().equals(""))
                    || (tv_onclick_checking_person_txt.getText().toString().equals(""))
                    || (tv_onclick_select_piece.getText().toString().equals(""))
                    || (tv_onclick_shift.getText().toString().equals(""))) {
                btn_save_ironing.setEnabled(false);
                btn_save_ironing.setBackground(getResources().getDrawable(R.drawable.disable_button_bg));
            }
        } else if (str_session_game_entry_point_value.equals("2")) {
            if ((tv_onclick_io_number_txt.getText().toString().equals(""))
                    || (tv_onclick_color_txt.getText().toString().equals(""))
                    || (tv_onclick_checking_person_txt.getText().toString().equals(""))
                    || (tv_onclick_select_piece.getText().toString().equals(""))
                    || (tv_onclick_shift.getText().toString().equals(""))) {
                btn_save_ironing.setEnabled(false);
                btn_save_ironing.setBackground(getResources().getDrawable(R.drawable.disable_button_bg));
            }
        }

        if (str_session_game_entry_point_value.equals("1")) {
        /*if (!(str_session_io_number.equalsIgnoreCase("No data"))
                && !(str_session_color_pick.equalsIgnoreCase("No data"))
                && !(str_session_checking_person.equalsIgnoreCase("No data"))
                && !(str_session_total_pieces.equalsIgnoreCase("No data"))
                && !(str_session_shift_timing.equalsIgnoreCase("No data"))) {*/
            if (!(tv_onclick_io_number_txt.getText().toString().equals(""))
                    && !(tv_onclick_color_txt.getText().toString().equals(""))
                    && !(tv_onclick_checking_person_txt.getText().toString().equals(""))
                    && !(tv_onclick_select_piece.getText().toString().equals(""))
                    && !(tv_onclick_shift.getText().toString().equals(""))) {

//                Log.e("tv_onclick_io_number_txt", tv_onclick_io_number_txt.getText().toString());
//                Log.e("tv_onclick_color_txt", tv_onclick_color_txt.getText().toString());
//                Log.e("tv_onclick_rechecking_person_txt", tv_onclick_checking_person_txt.getText().toString());
//                Log.e("tv_total_Pieces", tv_onclick_select_piece.getText().toString());
//                Log.e("tv_onclick_shift", tv_onclick_shift.getText().toString());

                btn_save_ironing.setEnabled(true);
                btn_save_ironing.setBackground(getResources().getDrawable(R.drawable.iron_entry_button_bg));
            }
        } else if (str_session_game_entry_point_value.equals("2")) {
            if (!(tv_onclick_io_number_txt.getText().toString().equals(""))
                    && !(tv_onclick_color_txt.getText().toString().equals(""))
                    && !(tv_onclick_checking_person_txt.getText().toString().equals(""))
                    && !(tv_onclick_select_piece.getText().toString().equals(""))
                    && !(tv_onclick_shift.getText().toString().equals(""))) {

//                Log.e("tv_onclick_io_number_txt", tv_onclick_io_number_txt.getText().toString());
//                Log.e("tv_onclick_color_txt", tv_onclick_color_txt.getText().toString());
//                Log.e("tv_onclick_rechecking_person_txt", tv_onclick_checking_person_txt.getText().toString());
//                Log.e("tv_total_Pieces", tv_onclick_select_piece.getText().toString());
//                Log.e("tv_onclick_shift", tv_onclick_shift.getText().toString());

                btn_save_ironing.setEnabled(true);
                btn_save_ironing.setBackground(getResources().getDrawable(R.drawable.iron_entry_button_bg));
            }
        }
        return view;
    }

    private void Get_Iorning_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("usname", str_session_username);
            jsonObject.put("token", str_session_logintoken);
            Log.e("json_obje_ironing", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Sewing_Model> call = apiInterface.GET_IRONING_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Sewing_Model>() {
                @Override
                public void onResponse(Call<Sewing_Model> call, Response<Sewing_Model> response) {
                    if (response.body().data != null) {
                        if (response.code() == 200) {
                            if (response.isSuccessful() && response.body().data.size() != 0) {
                                if (constraintLayout_qr_code_scan.getVisibility() == View.VISIBLE) {
                                    rv_ironing.setVisibility(View.GONE);
                                    constraintLayout_recyclerview.setVisibility(View.GONE);
                                } else {
                                    rv_ironing.setVisibility(View.VISIBLE);
                                    constraintLayout_recyclerview.setVisibility(View.VISIBLE);
                                }
                                shimmer_for_ironing.setVisibility(View.GONE);
                                tv_no_data_available.setVisibility(View.GONE);
                                ironing_adapter = new Ironing_Adapter(getActivity(), response.body().data);
                                rv_ironing.setAdapter(ironing_adapter);
                            } else {
                                Log.e("res_data_size_else", String.valueOf(response.body().data.size()));
                                constraintLayout_ironing_rv_layout.setVisibility(View.GONE);
                                constraintLayout_recyclerview.setVisibility(View.VISIBLE);
                                tv_no_data_available.setText(response.body().message);
                                if (scroll_view_layout.getVisibility() == View.VISIBLE) {
                                    tv_no_data_available.setVisibility(View.GONE);
                                } else {
                                    tv_no_data_available.setVisibility(View.VISIBLE);
                                }
                                shimmer_for_ironing.setVisibility(View.GONE);
                                rv_ironing.setVisibility(View.GONE);
                            }
                        } else if (response.code() == 401) {

                        } else if (response.code() == 500) {

                        }
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
        if (backStackEntryCount == 1) {
            Intent intent = new Intent(getActivity(), Home_Activity.class);
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
            case R.id.btn_save_ironing:
                String str_temp_entry_point = SessionSave.getSession("Game_Entry_Point_Value_For_Ironing", getActivity());
//                Log.e("str_temp_entry_point_onclick", str_temp_entry_point);
                if (str_temp_entry_point.equals("1")) {
                    if (tv_onclick_io_number_txt.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Io Number", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_color_txt.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Color", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_checking_person_txt.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Checking Person", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_select_piece.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Enter Pieces", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_shift.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Shift Time", Toast.LENGTH_SHORT).show();
                    } else {
                        Get_Save_Iorning_Details(str_temp_entry_point);
                    }
                } else if (str_temp_entry_point.equals("2")) {
                    if (tv_onclick_io_number_txt.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Io Number", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_color_txt.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Color", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_checking_person_txt.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Checking Person", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_select_piece.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Enter Pieces", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_shift.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Shift Time", Toast.LENGTH_SHORT).show();
                    } else {
                        Get_Save_Iorning_Details(str_temp_entry_point);
                    }
                }


                /*constraintLayout_qr_code_scan.setVisibility(View.GONE);
                constraintLayout_recyclerview.setVisibility(View.VISIBLE);
                btn_save_ironing.setVisibility(View.GONE);
                fab_for_ironing.setVisibility(View.VISIBLE);
                if (constraintLayout_recyclerview.getVisibility() == View.VISIBLE) {
                    Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), getResources().getString(R.string.successfully_submitted_txt), 4);
                    Ironing_Entry_Act.tv_exit_txt.setVisibility(View.GONE);
                }*/

                /*String str_io_number, str_color, str_checking_person, str_total_pieces, str_ok_pieces, str_defect_pieces, str_shifttime;
                str_io_number = tv_onclick_io_number_txt.getText().toString();
                str_color = tv_onclick_color_txt.getText().toString();
                str_checking_person = tv_onclick_checking_person_txt.getText().toString();
                str_shifttime = tv_onclick_shift.getText().toString();
//                str_total_pieces = tv_onclick_shift.getText().toString();
//                str_ok_pieces = tv_onclick_shift.getText().toString();
//                str_defect_pieces = tv_onclick_shift.getText().toString();

//                Log.e("str_io_number", str_io_number);
//                Log.e("str_color", str_color);
//                Log.e("str_checking_person", str_checking_person);
//                Log.e("str_shifttime", str_shifttime);*/

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
            case R.id.fab_for_ironing:
                final Dialog dialog_fab = new Dialog(Objects.requireNonNull(getActivity()));
                dialog_fab.setContentView(R.layout.bar_code_alert_for_ironing);
                TextView tv_scan_fab, tv_manual, tv_cancel_fab;
                tv_scan_fab = dialog_fab.findViewById(R.id.tv_scan);
                tv_manual = dialog_fab.findViewById(R.id.tv_list);
                tv_cancel_fab = dialog_fab.findViewById(R.id.tv_cancel);

                SessionSave.clearSession("Game_Entry_Point_Value_For_Ironing", Objects.requireNonNull(getActivity()));
                tv_scan_fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_fab.dismiss();

                        SessionSave.SaveSession("Game_Entry_Point_Value_For_Ironing", "1", getActivity());

                        constraintLayout_recyclerview.setVisibility(View.GONE);
                        constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                        btn_save_ironing.setVisibility(View.VISIBLE);
                        fab_for_ironing.setVisibility(View.GONE);
                        Intent intent = new Intent(getActivity(), ScannedBarcodeActivity.class);
                        intent.putExtra("Entry_Point_Onclick_Value", "4");
                        startActivity(intent);
                        constraintLayout_qr_code_scan.setVisibility(View.GONE);
                        btn_save_ironing.setVisibility(View.GONE);
                    }
                });
                tv_manual.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onClick(View v) {
                        dialog_fab.dismiss();
                        SessionSave.SaveSession("Game_Entry_Point_Value_For_Ironing", "2", getActivity());


                        shimmer_for_ironing.setVisibility(View.GONE);
                        constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                        constraintLayout_recyclerview.setVisibility(View.GONE);
                        rv_ironing.setVisibility(View.GONE);
                        scroll_view_layout.setVisibility(View.VISIBLE);
                        btn_save_ironing.setVisibility(View.VISIBLE);
                        fab_for_ironing.setVisibility(View.GONE);
//                        Log.e("Game_Entry_Point_Value_for_Ironing", SessionSave.getSession("Game_Entry_Point_Value_For_Ironing", getActivity()));
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
                dialog_for_io_number.setContentView(R.layout.io_number_alert_for_ironing);
                rv_io_number = dialog_for_io_number.findViewById(R.id.rv_io_number);
                rv_io_number.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                rv_io_number.setHasFixedSize(true);
                dialog_for_io_number.setCancelable(true);
                Get_Io_Number_Details();
                dialog_for_io_number.show();
                tv_onclick_color_txt.setText("");
                tv_color_normal_txt.setText(R.string.color_txt);
                break;
            case R.id.constraintLayout_color_layout:
                if (tv_onclick_io_number_txt.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Select IO Number", Toast.LENGTH_SHORT).show();
                } else {
                    dialog_for_colors = new Dialog(Objects.requireNonNull(getActivity()));
                    dialog_for_colors.setContentView(R.layout.color_select_alert_for_ironing);
                    rv_colors = dialog_for_colors.findViewById(R.id.rv_colors);
                    rv_colors.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    rv_colors.setHasFixedSize(true);
                    dialog_for_colors.setCancelable(true);
                    Get_Color_Details();
                    dialog_for_colors.show();
                }
                break;
            case R.id.constraintLayout_check_person_layout:
                /*dialog_for_check_person = new Dialog(Objects.requireNonNull(getActivity()));
                dialog_for_check_person.setContentView(R.layout.check_person_alert);
                rv_checking_person = dialog_for_check_person.findViewById(R.id.rv_checking_person);
                rv_checking_person.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                rv_checking_person.setHasFixedSize(true);
                dialog_for_check_person.setCancelable(true);
                dialog_for_check_person.show();*/

                final Dialog dialog_01 = new Dialog(Objects.requireNonNull(getActivity()));
                dialog_01.setContentView(R.layout.bar_code_alert_for_ironing);
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
                        dialog_for_check_person.setContentView(R.layout.check_person_alert_for_ironing);
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
//                        intent.putExtra("Entry_Point_Onclick_Value", "4");
                        intent.putExtra("Entry_Point_Onclick_Value_Others", "4");
                        startActivity(intent);
                        constraintLayout_qr_code_scan.setVisibility(View.GONE);
                        btn_save_ironing.setVisibility(View.GONE);
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
            case R.id.constraintLayout_check_pieces_layout:
                dialog_for_pieces = new Dialog(Objects.requireNonNull(getActivity()));
                dialog_for_pieces.setContentView(R.layout.select_pieces_alert_for_ironing);
                et_num_from_pieces_dialog = dialog_for_pieces.findViewById(R.id.et_num_from_pieces_dialog);
                tv_ok_from_pieces_dialog = dialog_for_pieces.findViewById(R.id.tv_ok_from_pieces_dialog);
                tv_ok_from_pieces_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        str_num_of_pieces = et_num_from_pieces_dialog.getText().toString();
                        if (str_num_of_pieces.isEmpty()) {
                            Toast.makeText(getActivity(), "Please enter piece value", Toast.LENGTH_SHORT).show();
                        } else {
//                            Log.e("inside", str_num_of_pieces);
                            dialog_for_pieces.dismiss();
                            tv_normal_select_piece.setText("");
                            SessionSave.SaveSession("Session_Total_Pieces_Ironing", str_num_of_pieces, getActivity());
                            tv_onclick_select_piece.setText(str_num_of_pieces);
                        }
                    }
                });
                dialog_for_pieces.setCancelable(true);
                dialog_for_pieces.show();
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
                } else {
                    dialog_for_shift_timing = new Dialog(Objects.requireNonNull(getActivity()));
                    dialog_for_shift_timing.setContentView(R.layout.select_shift_alert_for_ironing);
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

    @SuppressLint("LongLogTag")
    private void Get_Save_Iorning_Details(String str_temp_entry_point) {
//        Log.e("str_temp_entry_point_btn_ironing", str_temp_entry_point);
        try {
            String str_io_num = SessionSave.getSession("Session_IO_Number_Ironing", getActivity());
            String str_io_num_for_manual_entry = SessionSave.getSession("Session_IO_Number_Manual_Entry_For_Ironing", getActivity());

            String str_color_pick = SessionSave.getSession("Session_Color_Pick_Ironing", getActivity());
            String str_color_pick_for_manual_entry = SessionSave.getSession("Session_Color_Pick_Manual_Entry_For_Ironing", getActivity());

            String str_operator_id = SessionSave.getSession("Session_Onclick_id_value_For_Checking_Person_Ironing", getActivity());
            String str_opeartor_name = SessionSave.getSession("Session_Checking_Person_Ironing", getActivity());

            String str_shift_id = SessionSave.getSession("Session_Onclick_id_value_For_Shift_Timing_Ironing", getActivity());
            String str_shift_name = SessionSave.getSession("Session_Shift_Timing_Ironing", getActivity());

            String str_total_pieces = SessionSave.getSession("Session_Total_Pieces_Ironing", getActivity());
            String str_bundle_num_save = SessionSave.getSession("Session_Bundle_Number_Ironing", getActivity());
            JSONObject jsonObject = new JSONObject();
            if (str_temp_entry_point.equals("1")) {
                jsonObject.put("usname", str_session_username);
                jsonObject.put("token", str_session_logintoken);
                jsonObject.put("bundle_no", str_bundle_num_save);
                jsonObject.put("io_no", str_io_num);

                jsonObject.put("operator_id", str_operator_id);
                jsonObject.put("operator_name", str_opeartor_name);

                jsonObject.put("operation_id", "1");
                jsonObject.put("operation_name", "test");

                jsonObject.put("shift_id", str_shift_id);
                jsonObject.put("shift_name", str_shift_name);

                jsonObject.put("entry_type", "system");
                jsonObject.put("color_code", str_color_pick);
                jsonObject.put("tot_pcs", str_total_pieces);
                Log.e("Get_assign_json_iron_01", jsonObject.toString());
            } else if (str_temp_entry_point.equals("2")) {
                jsonObject.put("usname", str_session_username);
                jsonObject.put("token", str_session_logintoken);
                jsonObject.put("bundle_no", str_bundle_num_save);
                jsonObject.put("io_no", str_io_num_for_manual_entry);

                jsonObject.put("operator_id", str_operator_id);
                jsonObject.put("operator_name", str_opeartor_name);

                jsonObject.put("operation_id", "1");
                jsonObject.put("operation_name", "test");

                jsonObject.put("shift_id", str_shift_id);
                jsonObject.put("shift_name", str_shift_name);

                jsonObject.put("entry_type", "manual");
                jsonObject.put("color_code", str_color_pick_for_manual_entry);
                jsonObject.put("tot_pcs", str_total_pieces);
                Log.e("Get_assign_json_iron_02", jsonObject.toString());
            }
            APIInterface apiInterface = Factory.getClient();
            Call<Full_App_Model_Class> call = apiInterface.GET_ASSIGN_IRONING_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Full_App_Model_Class>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResponse(Call<Full_App_Model_Class> call, Response<Full_App_Model_Class> response) {
                    String str_status = "";
                    String str_message = "";
                    assert response.body() != null;
                    str_status = response.body().status;
                    str_message = response.body().message;
                    if (str_status.equalsIgnoreCase("success")) {
                        if (str_temp_entry_point.equals("1")) {
                            SessionSave.clearSession("Session_IO_Number_Ironing", getActivity());
                            SessionSave.clearSession("Session_Color_Pick_Ironing", getActivity());
                            SessionSave.clearSession("Session_Checking_Person_Ironing", getActivity());
                            SessionSave.clearSession("Session_Shift_Timing_Ironing", getActivity());
                            SessionSave.clearSession("Session_Total_Pieces_Ironing", getActivity());

                            tv_onclick_io_number_txt.setText("");
                            tv_onclick_color_txt.setText("");
                            tv_onclick_checking_person_txt.setText("");
                            tv_onclick_select_piece.setText("");
                            tv_onclick_shift.setText("");

                            tv_io_number_normal_txt.setText(R.string.io_number_txt);
                            tv_color_normal_txt.setText(R.string.color_txt);
                            tv_normal_checking_person_txt.setText(R.string.select_checking_person_txt);
                            tv_normal_select_piece.setText(R.string.enter_pieces_txt);
                            tv_select_shift.setText(R.string.select_shift_txt);

                            Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()),
                                    getResources().getString(R.string.successfully_submitted_txt), 4);
                            Ironing_Entry_Act.tv_exit_txt.setVisibility(View.GONE);

                            Get_Iorning_Details();

                            scroll_view_layout.setVisibility(View.GONE);
                            constraintLayout_qr_code_scan.setVisibility(View.GONE);
                            btn_save_ironing.setVisibility(View.GONE);
                            constraintLayout_recyclerview.setVisibility(View.VISIBLE);

                            fab_for_ironing.setVisibility(View.VISIBLE);
                            rv_ironing.setVisibility(View.VISIBLE);
                        } else if (str_temp_entry_point.equals("2")) {
                            SessionSave.clearSession("Session_IO_Number_Manual_Entry_For_Ironing", getActivity());
                            SessionSave.clearSession("Session_Color_Pick_Manual_Entry_For_Ironing", getActivity());
                            SessionSave.clearSession("Session_Checking_Person_Ironing", getActivity());
                            SessionSave.clearSession("Session_Shift_Timing_Ironing", getActivity());
                            SessionSave.clearSession("Session_Total_Pieces_Ironing", getActivity());

                            tv_onclick_io_number_txt.setText("");
                            tv_onclick_color_txt.setText("");
                            tv_onclick_checking_person_txt.setText("");
                            tv_onclick_select_piece.setText("");
                            tv_onclick_shift.setText("");

                            tv_io_number_normal_txt.setText(R.string.io_number_txt);
                            tv_color_normal_txt.setText(R.string.color_txt);
                            tv_normal_checking_person_txt.setText(R.string.select_checking_person_txt);
                            tv_normal_select_piece.setText(R.string.enter_pieces_txt);
                            tv_select_shift.setText(R.string.select_shift_txt);

                            Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()),
                                    getResources().getString(R.string.successfully_submitted_txt), 4);

                            Get_Iorning_Details();

                            scroll_view_layout.setVisibility(View.GONE);
                            constraintLayout_qr_code_scan.setVisibility(View.GONE);
                            btn_save_ironing.setVisibility(View.GONE);
                            constraintLayout_recyclerview.setVisibility(View.VISIBLE);
                            fab_for_ironing.setVisibility(View.VISIBLE);
                            rv_ironing.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(getActivity(), "" + str_message, Toast.LENGTH_SHORT).show();
                        if (str_temp_entry_point.equals("1")) {
                            constraintLayout_recyclerview.setVisibility(View.GONE);
                            fab_for_ironing.setVisibility(View.GONE);
                            rv_ironing.setVisibility(View.GONE);
                            constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                            scroll_view_layout.setVisibility(View.VISIBLE);
                            btn_save_ironing.setVisibility(View.VISIBLE);
                        } else if (str_temp_entry_point.equals("2")) {
                            constraintLayout_recyclerview.setVisibility(View.GONE);
                            fab_for_ironing.setVisibility(View.GONE);
                            rv_ironing.setVisibility(View.GONE);
                            constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                            scroll_view_layout.setVisibility(View.VISIBLE);
                            btn_save_ironing.setVisibility(View.VISIBLE);
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

    @SuppressLint("LongLogTag")
    private void Get_Checking_Person_Details() {
       /* checking_person_ArrayList.add("Sam");
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
        checking_person_adapter_for_ironing = new Checking_Person_Adapter_For_Ironing(getActivity(), checking_person_ArrayList);
        rv_checking_person.setAdapter(checking_person_adapter_for_ironing);*/

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("usname", str_session_username);
            jsonObject.put("token", str_session_logintoken);
            jsonObject.put("role", "ironing");
            Log.e("json_obj_checking_person_for_ironing", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Operator_Model> call = apiInterface.OPERATOR_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Operator_Model>() {
                @Override
                public void onResponse(Call<Operator_Model> call, Response<Operator_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            String str_status = "";
                            str_status = response.body() != null ? Objects.requireNonNull(response.body()).status : null;
                            assert str_status != null;
                            if (str_status.equals("error")) {
                                dialog_for_check_person.dismiss();
                                Toast.makeText(getActivity(), "" + response.body().message, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getActivity(), "This user doesn't have permission to access the Checking Person data", Toast.LENGTH_SHORT).show();
                            } else {
                                rv_checking_person.setVisibility(View.VISIBLE);
                                assert response.body() != null;
                                checking_person_adapter_for_ironing = new Checking_Person_Adapter_For_Ironing(getActivity(), response.body().data);
                                rv_checking_person.setAdapter(checking_person_adapter_for_ironing);
                            }
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

    @SuppressLint("LongLogTag")
    private void Get_Color_Details() {
        /*color_ArrayList.add("#09877");
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
        color_adapter_for_ironing = new Color_Adapter_For_Ironing(getActivity(), color_ArrayList);
        rv_colors.setAdapter(color_adapter_for_ironing);*/

//        Log.e("str_session_game_entry_point_value_for_color", str_session_game_entry_point_value);
        try {
            String str_io_number_local = "";
            if (str_session_game_entry_point_value.equals("1")) {
                str_io_number_local = SessionSave.getSession("Session_IO_Number_Ironing", getActivity());
            } else if (str_session_game_entry_point_value.equals("2")) {
                str_io_number_local = SessionSave.getSession("Session_IO_Number_Manual_Entry_For_Ironing", getActivity());
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("usname", str_session_username);
            jsonObject.put("token", str_session_logintoken);
            jsonObject.put("io_no", str_io_number_local);
            Log.e("json_OBJ_COLOR_for_ironing", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Operator_Model> call = apiInterface.COLOR_CODE_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Operator_Model>() {
                @Override
                public void onResponse(Call<Operator_Model> call, Response<Operator_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            String str_status = "";
                            str_status = response.body() != null ? Objects.requireNonNull(response.body()).status : null;
                            if (str_status.equals("error")) {
                                dialog_for_colors.dismiss();
                                Toast.makeText(getActivity(), "" + response.body().message, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getActivity(), "This user doesn't have permission to access the Color data", Toast.LENGTH_SHORT).show();
                            } else {
                                rv_colors.setVisibility(View.VISIBLE);
                                color_adapter_for_ironing = new Color_Adapter_For_Ironing(getActivity(), response.body().data);
                                rv_colors.setAdapter(color_adapter_for_ironing);
                            }
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

    @SuppressLint("LongLogTag")
    private void Get_Io_Number_Details() {
        /*ionumber_ArrayList.add("1234");
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
        io_number_adapter_for_ironing = new Io_Number_Adapter_For_Ironing(getActivity(), ionumber_ArrayList);
        rv_io_number.setAdapter(io_number_adapter_for_ironing);*/

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("usname", str_session_username);
            jsonObject.put("token", str_session_logintoken);
            Log.e("json_obj_io_number_for_ironing", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Operator_Model> call = apiInterface.IO_NUMBER_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Operator_Model>() {
                @Override
                public void onResponse(Call<Operator_Model> call, Response<Operator_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            String str_status = "";
                            str_status = response.body() != null ? Objects.requireNonNull(response.body()).status : null;
                            if (str_status.equals("error")) {
                                dialog_for_io_number.dismiss();
                                Toast.makeText(getActivity(), "" + response.body().message, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getActivity(), "This user doesn't have permission to access the Io Number data", Toast.LENGTH_SHORT).show();
                            } else {
                                rv_io_number.setVisibility(View.VISIBLE);
                                io_number_adapter_for_ironing = new Io_Number_Adapter_For_Ironing(getActivity(), response.body().data);
                                rv_io_number.setAdapter(io_number_adapter_for_ironing);
                            }
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

    private void Get_Shift_Timing() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("usname", str_session_username);
            jsonObject.put("token", str_session_logintoken);
            Log.e("shift_json_ironing", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Operator_Model> call = apiInterface.SHIFT_TIMING_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Operator_Model>() {
                @Override
                public void onResponse(Call<Operator_Model> call, Response<Operator_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            String str_status = "";
                            str_status = response.body() != null ? Objects.requireNonNull(response.body()).status : null;
                            if (str_status.equals("error")) {
                                dialog_for_shift_timing.dismiss();
                                Toast.makeText(getActivity(), "" + response.body().message, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getActivity(), "This user doesn't have permission to access the Shift Timing data", Toast.LENGTH_SHORT).show();
                            } else {
                                rv_shift_timing.setVisibility(View.VISIBLE);
                                Cheshift_timing_adapter_for_ironing = new Shift_Timing_Adapter_For_Ironing(getActivity(), response.body().data);
                                rv_shift_timing.setAdapter(Cheshift_timing_adapter_for_ironing);
                            }
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

    private void Send_Scan_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("usname", str_session_username);
            jsonObject.put("token", str_session_logintoken);
            jsonObject.put("qr_no", str_qrcode_value);
//            jsonObject.put("qr_no", str_bar_code_scan_value);
            Log.e("json_obje", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Full_App_Model_Class> call = apiInterface.GET_BUNDLE_DATA_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Full_App_Model_Class>() {
                @Override
                public void onResponse(Call<Full_App_Model_Class> call, Response<Full_App_Model_Class> response) {
                    if (response.code() == 200) {
                        if (response.body().data != null) {
                            if (response.isSuccessful()) {
                                scroll_view_layout.setVisibility(View.VISIBLE);
                                constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                                btn_save_ironing.setVisibility(View.VISIBLE);

                                tv_no_data_available.setVisibility(View.GONE);
                                rv_ironing.setVisibility(View.GONE);
                                constraintLayout_ironing_rv_layout.setVisibility(View.GONE);
                                constraintLayout_recyclerview.setVisibility(View.GONE);
                                fab_for_ironing.setVisibility(View.GONE);

                                str_io_num = response.body().data.io_no;
                                str_date = response.body().data.date;
                                str_color_code = response.body().data.color_code;
                                str_size = response.body().data.size;
                                str_lot_num = response.body().data.lot_no;
                                str_total_pieces = response.body().data.tot_pcs;
                                str_bundle_number = response.body().data.bundle_no;

                                tv_io_number_normal_txt.setText("");
                                tv_color_normal_txt.setText("");
                                tv_onclick_io_number_txt.setText(str_io_num);
                                tv_onclick_color_txt.setText(str_color_code);

//                            Log.e("str_io_num", str_io_num);
//                            Log.e("str_bundle_number", str_bundle_number);

                                SessionSave.SaveSession("Session_IO_Number_Ironing", tv_onclick_io_number_txt.getText().toString(), getActivity());
                                SessionSave.SaveSession("Session_Color_Pick_Ironing", tv_onclick_color_txt.getText().toString(), getActivity());
                                SessionSave.SaveSession("Session_Bundle_Number_Ironing", str_bundle_number, getActivity());

                            }
                        } else {
//                            Toast.makeText(getActivity(), R.string.pls_enter_valid_user, Toast.LENGTH_LONG).show();
                            /*Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()),"Please enter valid user",6);
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class Io_Number_Adapter_For_Ironing extends RecyclerView.Adapter<Io_Number_Adapter_For_Ironing.ViewHolder> {
//        ArrayList<String> stringArrayList;
//        Context mContext;

        ArrayList<Operator_Model.Data> stringArrayList;
        Context mContext;


/*
        public Io_Number_Adapter_For_Ironing(FragmentActivity activity, ArrayList<String> ionumber_arrayList) {
            this.mContext = activity;
            this.stringArrayList = ionumber_arrayList;
        }
*/

        public Io_Number_Adapter_For_Ironing(FragmentActivity activity, ArrayList<Operator_Model.Data> data) {
            this.mContext = activity;
            this.stringArrayList = data;
        }

        @NonNull
        @Override
        public Io_Number_Adapter_For_Ironing.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view;
            view = inflater.inflate(R.layout.io_number_adapter_details_layout, parent, false);
            return new Io_Number_Adapter_For_Ironing.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Io_Number_Adapter_For_Ironing.ViewHolder holder, int position) {
            String str_from_time = stringArrayList.get(position).io_no;
            holder.tv_io_number_pick.setText(str_from_time);
            holder.tv_io_number_pick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_io_number_normal_txt.setText("");
                    tv_onclick_io_number_txt.setText("");
//                    Log.e("str_session_game_entry_point_value", str_session_game_entry_point_value);
                    SessionSave.SaveSession("Session_IO_Number_Manual_Entry_For_Ironing", holder.tv_io_number_pick.getText().toString(), getActivity());
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

    private class Color_Adapter_For_Ironing extends RecyclerView.Adapter<Color_Adapter_For_Ironing.ViewHolder> {
        //        ArrayList<String> stringArrayList;
        ArrayList<Operator_Model.Data> stringArrayList;
        Context mContext;


        /*public Color_Adapter_For_Ironing(FragmentActivity activity, ArrayList<String> ionumber_arrayList) {
            this.mContext = activity;
            this.stringArrayList = ionumber_arrayList;
        }*/

        public Color_Adapter_For_Ironing(FragmentActivity activity, ArrayList<Operator_Model.Data> data) {
            this.mContext = activity;
            this.stringArrayList = data;
        }

        @NonNull
        @Override
        public Color_Adapter_For_Ironing.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view;
            view = inflater.inflate(R.layout.color_adapter_details_layout, parent, false);
            return new Color_Adapter_For_Ironing.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Color_Adapter_For_Ironing.ViewHolder holder, int position) {
            String str_from_time = stringArrayList.get(position).color_code;
            holder.tv_color_pick.setText(str_from_time);
            holder.tv_color_pick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_color_normal_txt.setText("");
                    SessionSave.SaveSession("Session_Color_Pick_Manual_Entry_For_Ironing", holder.tv_color_pick.getText().toString(), getActivity());
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

    private class Checking_Person_Adapter_For_Ironing extends RecyclerView.Adapter<Checking_Person_Adapter_For_Ironing.ViewHolder> {
        //        ArrayList<String> stringArrayList;
        ArrayList<Operator_Model.Data> stringArrayList;
        Context mContext;


        /*public Checking_Person_Adapter_For_Ironing(FragmentActivity activity, ArrayList<String> ionumber_arrayList) {
            this.mContext = activity;
            this.stringArrayList = ionumber_arrayList;
        }*/

        public Checking_Person_Adapter_For_Ironing(FragmentActivity activity, ArrayList<Operator_Model.Data> data) {
            this.mContext = activity;
            this.stringArrayList = data;
        }

        @NonNull
        @Override
        public Checking_Person_Adapter_For_Ironing.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view;
            view = inflater.inflate(R.layout.checking_person_adapter_details_layout, parent, false);
            return new Checking_Person_Adapter_For_Ironing.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Checking_Person_Adapter_For_Ironing.ViewHolder holder, int position) {
            String str_from_time = stringArrayList.get(position).name;
            holder.tv_checking_person_pick.setText(str_from_time);
            holder.tv_checking_person_pick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_normal_checking_person_txt.setText("");
                    String str_checking_person_id = stringArrayList.get(position).id;
                    SessionSave.SaveSession("Session_Onclick_id_value_For_Checking_Person_Ironing", str_checking_person_id, getActivity());
                    SessionSave.SaveSession("Session_Checking_Person_Ironing", holder.tv_checking_person_pick.getText().toString(), getActivity());
                    tv_onclick_checking_person_txt.setText(holder.tv_checking_person_pick.getText().toString());
//                    Log.e("Onclick_id_value_For_Checking_Person_Ironing", SessionSave.getSession("Session_Onclick_id_value_For_Checking_Person_Ironing", getActivity()));

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

    private class Shift_Timing_Adapter_For_Ironing extends RecyclerView.Adapter<Shift_Timing_Adapter_For_Ironing.ViewHolder> {
        ArrayList<Operator_Model.Data> stringArrayList;
        Context mContext;

        public Shift_Timing_Adapter_For_Ironing(FragmentActivity activity, ArrayList<Operator_Model.Data> data) {
            this.mContext = activity;
            this.stringArrayList = data;
        }

        @NonNull
        @Override
        public Shift_Timing_Adapter_For_Ironing.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view;
            view = inflater.inflate(R.layout.select_shift_details_layout, parent, false);
            return new Shift_Timing_Adapter_For_Ironing.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Shift_Timing_Adapter_For_Ironing.ViewHolder holder, int position) {
            String str_from_time = stringArrayList.get(position).from_time;
            String str_to_time = stringArrayList.get(position).to_time;
            String str_merge_timings = str_from_time + "  to  " + str_to_time;

            holder.tv_from_shift_timing.setText(str_merge_timings);
            holder.tv_from_shift_timing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_select_shift.setText("");
//                    Log.e("str_merge_timings", str_merge_timings);
                    String str_shift_timing_id = stringArrayList.get(position).id;
                    SessionSave.SaveSession("Session_Onclick_id_value_For_Shift_Timing_Ironing", str_shift_timing_id, getActivity());
                    SessionSave.SaveSession("Session_Shift_Timing_Ironing", holder.tv_from_shift_timing.getText().toString(), getActivity());
//                    Log.e("Onclick_id_value_For_Shift_Timing_Ironing", SessionSave.getSession("Session_Onclick_id_value_For_Shift_Timing_Ironing", getActivity()));
                    tv_onclick_shift.setText(holder.tv_from_shift_timing.getText().toString());
                    dialog_for_shift_timing.dismiss();

                    if (!(tv_onclick_io_number_txt.getText().toString().isEmpty())
                            && (!(tv_onclick_color_txt.getText().toString().isEmpty()))
                            && (!(tv_onclick_checking_person_txt.getText().toString().isEmpty())
                            && (!(tv_onclick_select_piece.getText().toString().isEmpty())
                            && (!(tv_onclick_shift.getText().toString().isEmpty()))))) {
                        btn_save_ironing.setEnabled(true);
                        btn_save_ironing.setBackground(mContext.getResources().getDrawable(R.drawable.ironing_entry_button_bg));
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
}

