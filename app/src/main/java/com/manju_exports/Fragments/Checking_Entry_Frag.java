package com.manju_exports.Fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.manju_exports.Adapters.Checking_RecyclerView_Adapter;
import com.manju_exports.Entries.Checking_Entry_Act;
import com.manju_exports.Home_Activity;
import com.manju_exports.Interface.APIInterface;
import com.manju_exports.Interface.Factory;
import com.manju_exports.Model.Full_App_Model_Class;
import com.manju_exports.Model.Operator_Model;
import com.manju_exports.Model.Sewing_Model;
import com.manju_exports.R;
import com.manju_exports.ScannedBarcodeActivity;
import com.manju_exports.SessionSave;
import com.manju_exports.Show_Layout;
import com.manju_exports.Toast_Message;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import io.supercharge.shimmerlayout.ShimmerLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Checking_Entry_Frag extends Fragment implements View.OnClickListener {
    public static ConstraintLayout constraintLayout_checking_rv_layout,
            constraintLayout_recyclerview,
            constraintLayout_qr_code_scan;
    private TextView textViewName,
            textViewAddress;
    private TextView tv_io_number_normal_txt,
            tv_onclick_io_number_txt,
            tv_color_normal_txt,
            tv_onclick_color_txt,
            tv_normal_checking_person_txt,
            tv_onclick_checking_person_txt,
            tv_select_shift,
            tv_onclick_shift;

    public static Button btn_save_checking;
    private Dialog dialog_for_io_number,
            dialog_for_colors,
            dialog_for_check_person,
            dialog_for_pieces,
            dialog_for_shift_timing;
    private String str_session_username,
            str_session_logintoken;
    private RecyclerView rv_io_number,
            rv_colors,
            rv_checking_person,
            rv_entry_pieces,
            rv_shift_timing,
            rv_checking;
    public static FloatingActionButton fab_for_checking;

    private ConstraintLayout constraintLayout_io_number_layout,
            constraintLayout_color_layout,
            constraintLayout_checking_person_layout,
            constraintLayout_pieces_layout,
            constraintLayout_edit_pieces_layout,
            constraintLayout_select_shift;

    private ArrayList<String> ionumber_ArrayList = new ArrayList<>();
    private ArrayList<String> color_ArrayList = new ArrayList<>();
    private ArrayList<String> checking_person_ArrayList = new ArrayList<>();

    private Shift_Timing_Adapter_For_Checking shift_timing_adapter_for_checking;
    private Io_Number_Adapter_For_Checking io_number_adapter_for_checking;
    private Color_Adapter_For_Checking color_adapter_for_checking;
    private Checking_Person_Adapter_For_Checking checking_person_adapter_for_checking;


    private TextView tv_total_Pieces,
            tv_ok_Pieces,
            tv_defect_Pieces,
            tv_edit_txt,
            tv_pieces_or_total_pieces_txt,
            tv_no_data_available;


    private String str_session_defect_pieces,
            str_session_total_pieces,
            str_session_ok_pieces,
            str_session_io_number,
            str_session_color_pick,
            str_session_io_number_manual_entry,
            str_session_color_pick_manual_entry,
            str_session_checking_person,
            str_session_shift_timing,
            str_session_value_for_integer,
            str_session_game_entry_point_value;
    private String str_hide_layout_value = "",
            str_qrcode_value = "";
    private String str_qrcode_value_others = "";
    ScrollView scroll_view_layout;
    ShimmerLayout shimmer_for_checking;
    String str_io_num = "",
            str_color_code = "",
            str_bar_code_scan_value = "",
            str_bundle_number = "";

    private Checking_RecyclerView_Adapter checking_recyclerView_adapter;
    int int_fab_onlick_value = 0;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("LongLogTag")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.checking_entry_frag_lay, container, false);
//        Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        shimmer_for_checking = view.findViewById(R.id.shimmer_for_checking);
        tv_io_number_normal_txt = view.findViewById(R.id.tv_io_number_normal_txt);
        tv_color_normal_txt = view.findViewById(R.id.tv_color_normal_txt);
        tv_normal_checking_person_txt = view.findViewById(R.id.tv_normal_checking_person_txt);
        tv_select_shift = view.findViewById(R.id.tv_select_shift);
        tv_onclick_io_number_txt = view.findViewById(R.id.tv_onclick_io_number_txt);
        tv_onclick_color_txt = view.findViewById(R.id.tv_onclick_color_txt);
        tv_onclick_checking_person_txt = view.findViewById(R.id.tv_onclick_checking_person_txt);
        tv_onclick_shift = view.findViewById(R.id.tv_onclick_shift);
        rv_checking_person = view.findViewById(R.id.rv_checking_person);
        rv_checking = view.findViewById(R.id.rv_checking);

        rv_checking.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_checking.setHasFixedSize(true);

        fab_for_checking = view.findViewById(R.id.fab_for_checking);
        btn_save_checking = view.findViewById(R.id.btn_save_checking);
        constraintLayout_qr_code_scan = view.findViewById(R.id.constraintLayout_qr_code_scan);
        constraintLayout_checking_rv_layout = view.findViewById(R.id.constraintLayout_checking_rv_layout);
        constraintLayout_recyclerview = view.findViewById(R.id.constraintLayout_recyclerview);

        constraintLayout_io_number_layout = view.findViewById(R.id.constraintLayout_io_number_layout);
        constraintLayout_color_layout = view.findViewById(R.id.constraintLayout_color_layout);
        constraintLayout_checking_person_layout = view.findViewById(R.id.constraintLayout_checking_person_layout);
        constraintLayout_pieces_layout = view.findViewById(R.id.constraintLayout_pieces_layout);
        constraintLayout_select_shift = view.findViewById(R.id.constraintLayout_select_shift);
        scroll_view_layout = view.findViewById(R.id.scroll_view_layout);


        /*This components are from edit pieces*/
        constraintLayout_edit_pieces_layout = view.findViewById(R.id.constraintLayout_edit_pieces_layout);
        tv_total_Pieces = view.findViewById(R.id.tv_total_Pieces);
        tv_ok_Pieces = view.findViewById(R.id.tv_ok_Pieces);
        tv_defect_Pieces = view.findViewById(R.id.tv_defect_Pieces);
        tv_edit_txt = view.findViewById(R.id.tv_edit_txt);
        tv_pieces_or_total_pieces_txt = view.findViewById(R.id.tv_pieces_or_total_pieces_txt);

        tv_no_data_available = view.findViewById(R.id.tv_no_data_available);
        tv_no_data_available.setVisibility(View.GONE);

        constraintLayout_io_number_layout.setOnClickListener(this);
        constraintLayout_color_layout.setOnClickListener(this);
        constraintLayout_checking_person_layout.setOnClickListener(this);
        constraintLayout_pieces_layout.setOnClickListener(this);
        constraintLayout_select_shift.setOnClickListener(this);
        fab_for_checking.setOnClickListener(this);
        btn_save_checking.setOnClickListener(this);
        tv_edit_txt.setOnClickListener(this);


        shimmer_for_checking.setVisibility(View.VISIBLE);
        shimmer_for_checking.startShimmerAnimation();

        str_session_username = SessionSave.getSession("Session_UserName", getActivity());
        str_session_logintoken = SessionSave.getSession("Session_Logintoken", getActivity());
        str_session_io_number = SessionSave.getSession("Session_IO_Number", getActivity());
        str_session_color_pick = SessionSave.getSession("Session_Color_Pick", getActivity());

        str_session_io_number_manual_entry = SessionSave.getSession("Session_IO_Number_Manual_Entry", getActivity());
        str_session_color_pick_manual_entry = SessionSave.getSession("Session_Color_Pick_Manual_Entry", getActivity());

        str_session_checking_person = SessionSave.getSession("Session_Checking_Person", getActivity());
        str_session_total_pieces = SessionSave.getSession("Session_Total_Pieces", getActivity());
        str_session_ok_pieces = SessionSave.getSession("Session_Ok_Pieces", getActivity());
        str_session_defect_pieces = SessionSave.getSession("Session_Defect_Pieces", getActivity());
        str_session_shift_timing = SessionSave.getSession("Session_Shift_Timing", getActivity());
        str_session_value_for_integer = SessionSave.getSession("session_jsonKeyvalues", getActivity());
        str_session_game_entry_point_value = SessionSave.getSession("Game_Entry_Point_Value_For_Checking", getActivity());
        Get_Sewing_Details();

//        Log.e("str_session_username", str_session_username);
//        Log.e("str_session_logintoken", str_session_logintoken);
//        Log.e("str_session_io_number", str_session_io_number);
//        Log.e("str_session_color_pick", str_session_color_pick);
//
//        Log.e("str_session_checking_person", str_session_checking_person);
//        Log.e("str_session_total_pieces", str_session_total_pieces);
//        Log.e("str_session_ok_pieces", str_session_ok_pieces);
//        Log.e("str_session_defect_pieces", str_session_defect_pieces);
//        Log.e("str_session_shift_timing", str_session_shift_timing);
//        Log.e("str_session_value_for_integer", str_session_value_for_integer);
        Bundle bundle = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        if (bundle == null) {
            str_hide_layout_value = null;
            str_qrcode_value = null;
            Home_Activity.toolbar.setBackgroundColor(getResources().getColor(R.color.orange_color));
            Home_Activity.tv_title_txt.setText(getResources().getString(R.string.checking_txt));
        } else {
            str_hide_layout_value = bundle.getString("hide_layout");
            str_qrcode_value = bundle.getString("Barcode_value");
            str_qrcode_value_others = bundle.getString("Barcode_value_Others");
            Log.e("str_hide_layout_value_log", str_hide_layout_value);
           /* Set<String> stringSet = new HashSet<>();
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
//                        Log.e("str_bar_code_scan_value", str_bar_code_scan_value);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            */
            if (str_qrcode_value != null) {
                Get_Scan_Details();
                rv_checking.setVisibility(View.GONE);
                shimmer_for_checking.stopShimmerAnimation();
                shimmer_for_checking.setVisibility(View.GONE);
                constraintLayout_recyclerview.setVisibility(View.GONE);
                constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                fab_for_checking.setVisibility(View.GONE);
                btn_save_checking.setVisibility(View.VISIBLE);
                scroll_view_layout.setVisibility(View.VISIBLE);
                Checking_Entry_Act.tv_exit_txt.setVisibility(View.VISIBLE);
            }
        }
        if (!(str_session_checking_person.equalsIgnoreCase("No data"))) {
            if (str_qrcode_value_others != null && str_qrcode_value != null) {
                tv_onclick_checking_person_txt.setText(str_session_checking_person);
                tv_normal_checking_person_txt.setText("");
                SessionSave.SaveSession("Session_Checking_Person", tv_onclick_checking_person_txt.getText().toString(), getActivity());
            } else if (str_qrcode_value != null) {
                tv_onclick_checking_person_txt.setText(str_session_checking_person);
                tv_normal_checking_person_txt.setText("");
                SessionSave.SaveSession("Session_Checking_Person", tv_onclick_checking_person_txt.getText().toString(), getActivity());
            } else {
                tv_onclick_checking_person_txt.setText(str_session_checking_person);
                tv_normal_checking_person_txt.setText("");
                SessionSave.SaveSession("Session_Checking_Person", tv_onclick_checking_person_txt.getText().toString(), getActivity());
            }
        } else {
            tv_normal_checking_person_txt.setText(R.string.select_checking_person_txt);
        }


        /*
         * If Session Value is non_empty means
         * */
        if (!(str_session_io_number_manual_entry.equalsIgnoreCase("No data"))) {
            tv_onclick_io_number_txt.setText(str_session_io_number_manual_entry);
            tv_io_number_normal_txt.setText("");
        } else {
            tv_io_number_normal_txt.setText(R.string.io_number_txt);
        }

        if (!(str_session_color_pick_manual_entry.equalsIgnoreCase("No data"))) {
            tv_onclick_color_txt.setText(str_session_color_pick_manual_entry);
            tv_color_normal_txt.setText("");
        } else {
            tv_color_normal_txt.setText(R.string.color_txt);
        }

        if (!(str_session_shift_timing.equalsIgnoreCase("No data"))) {
            tv_onclick_shift.setText(str_session_shift_timing);
            tv_select_shift.setText("");
        }
        if (!(str_session_checking_person.equalsIgnoreCase("No data"))) {
            tv_onclick_checking_person_txt.setText(str_session_checking_person);
            tv_normal_checking_person_txt.setText("");
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

        if (!(str_session_shift_timing.equalsIgnoreCase("No data"))) {
            tv_onclick_shift.setText(str_session_shift_timing);
            tv_select_shift.setText("");
        } else {
            tv_select_shift.setText(R.string.select_shift_txt);
        }

        /*
         *Created on  09-05-2020
         * If any of one input value is empty means button is disable this is  for entry point value 1 or 2
         * */
        if (str_session_game_entry_point_value.equals("1")) {
            if ((tv_onclick_io_number_txt.getText().toString().equals(""))
                    || (tv_onclick_color_txt.getText().toString().equals(""))
                    || (tv_onclick_checking_person_txt.getText().toString().equals(""))
                    || (tv_total_Pieces.getText().toString().equals(""))
                    || (tv_onclick_shift.getText().toString().equals(""))) {
                btn_save_checking.setEnabled(false);
                btn_save_checking.setBackground(getResources().getDrawable(R.drawable.disable_button_bg));
            }
        } else if (str_session_game_entry_point_value.equals("2")) {
            if ((tv_onclick_io_number_txt.getText().toString().equals(""))
                    || (tv_onclick_color_txt.getText().toString().equals(""))
                    || (tv_onclick_checking_person_txt.getText().toString().equals(""))
                    || (tv_total_Pieces.getText().toString().equals(""))
                    || (tv_onclick_shift.getText().toString().equals(""))) {
                btn_save_checking.setEnabled(false);
                btn_save_checking.setBackground(getResources().getDrawable(R.drawable.disable_button_bg));
            }
        }



        /*
         *
         *If total pieces session value is empty means use if statement or else use ele statement.
         * */
        if (str_session_total_pieces.equalsIgnoreCase("No data")) {
            constraintLayout_pieces_layout.setVisibility(View.VISIBLE);
            constraintLayout_edit_pieces_layout.setVisibility(View.INVISIBLE);
            tv_total_Pieces.setVisibility(View.INVISIBLE);
            tv_ok_Pieces.setVisibility(View.INVISIBLE);
            tv_defect_Pieces.setVisibility(View.INVISIBLE);
            tv_edit_txt.setVisibility(View.INVISIBLE);
        } else {
            constraintLayout_pieces_layout.setVisibility(View.GONE);
            constraintLayout_edit_pieces_layout.setVisibility(View.VISIBLE);
            tv_pieces_or_total_pieces_txt.setVisibility(View.INVISIBLE);
            tv_total_Pieces.setText(str_session_total_pieces);
            tv_ok_Pieces.setText(str_session_ok_pieces);
            tv_defect_Pieces.setText(str_session_defect_pieces);
            if (str_session_game_entry_point_value.equals("1")) {
                if (!(tv_onclick_io_number_txt.getText().toString().equals(""))
                        && !(tv_onclick_color_txt.getText().toString().equals(""))
                        && !(tv_onclick_checking_person_txt.getText().toString().equals(""))
                        && !(tv_total_Pieces.getText().toString().equals(""))
                        && !(tv_onclick_shift.getText().toString().equals(""))) {
                    btn_save_checking.setEnabled(true);
                    btn_save_checking.setBackground(getResources().getDrawable(R.drawable.checking_entry_button_bg));
                }
            } else if (str_session_game_entry_point_value.equals("2")) {
                if (!(tv_onclick_io_number_txt.getText().toString().equals(""))
                        && !(tv_onclick_color_txt.getText().toString().equals(""))
                        && !(tv_onclick_checking_person_txt.getText().toString().equals(""))
                        && !(tv_total_Pieces.getText().toString().equals(""))
                        && !(tv_onclick_shift.getText().toString().equals(""))) {
                    btn_save_checking.setEnabled(true);
                    btn_save_checking.setBackground(getResources().getDrawable(R.drawable.checking_entry_button_bg));
                }
            }
        }

        /*
         *
         *This funtion is used for if defect value is non_empty means show entry page
         * */
        if (str_hide_layout_value != null && str_hide_layout_value.equals("1")) {
            constraintLayout_checking_rv_layout.setVisibility(View.GONE);
            constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
            scroll_view_layout.setVisibility(View.VISIBLE);
            tv_no_data_available.setVisibility(View.GONE);
        }

        if (str_hide_layout_value != null) {
            if (str_hide_layout_value.equals("1")) {
                constraintLayout_checking_rv_layout.setVisibility(View.GONE);
                constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                fab_for_checking.setVisibility(View.GONE);
                btn_save_checking.setVisibility(View.VISIBLE);
                tv_no_data_available.setVisibility(View.GONE);
            } else {
                constraintLayout_checking_rv_layout.setVisibility(View.VISIBLE);
                constraintLayout_qr_code_scan.setVisibility(View.GONE);
                fab_for_checking.setVisibility(View.VISIBLE);
            }
        }
        return view;
    }

    private void Get_Scan_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("usname", str_session_username);
            jsonObject.put("token", str_session_logintoken);
            jsonObject.put("qr_no", str_qrcode_value);
//            jsonObject.put("qr_no", str_bar_code_scan_value);
            APIInterface apiInterface = Factory.getClient();
            Log.e("Scan_Details_Json", jsonObject.toString());
            Call<Full_App_Model_Class> call = apiInterface.GET_BUNDLE_DATA_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Full_App_Model_Class>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<Full_App_Model_Class> call, Response<Full_App_Model_Class> response) {
                    if (response.code() == 200) {
                        if (response.body().data != null) {
                            if (response.isSuccessful()) {
                                scroll_view_layout.setVisibility(View.VISIBLE);
                                constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                                btn_save_checking.setVisibility(View.VISIBLE);
                                tv_no_data_available.setVisibility(View.GONE);
                                rv_checking.setVisibility(View.GONE);
                                constraintLayout_recyclerview.setVisibility(View.GONE);
                                constraintLayout_checking_rv_layout.setVisibility(View.GONE);
                                fab_for_checking.setVisibility(View.GONE);

                                str_io_num = response.body().data.io_no;
                                str_color_code = response.body().data.color_code;
                                tv_io_number_normal_txt.setText("");
                                tv_color_normal_txt.setText("");
                                tv_onclick_io_number_txt.setText(str_io_num);
                                tv_onclick_color_txt.setText(str_color_code);
                                str_bundle_number = response.body().data.bundle_no;

                                SessionSave.SaveSession("Session_IO_Number", tv_onclick_io_number_txt.getText().toString(), getActivity());
                                SessionSave.SaveSession("Session_Color_Pick", tv_onclick_color_txt.getText().toString(), getActivity());
                                SessionSave.SaveSession("Session_Bundle_Number", str_bundle_number, getActivity());
//                            Log.e("str_session_checking_person_scan", str_session_checking_person);
//                            Log.e("str_bundle_number_scan", str_bundle_number);

                            /*if (str_session_checking_person.equalsIgnoreCase("No data")
                                    || str_session_checking_person.equals("")) {
                                tv_onclick_checking_person_txt.setText("");
                                tv_normal_checking_person_txt.setText(R.string.select_checking_person_txt);
                            } else {
                                tv_onclick_checking_person_txt.setText(str_session_checking_person);
//                                SessionSave.SaveSession("Session_Checking_Person", tv_onclick_checking_person_txt.getText().toString(), getActivity());
                            }*/
                            }
                        } else {
//                            Toast.makeText(getActivity(), "Please enter valid user", Toast.LENGTH_LONG).show();
                       /*     Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()),"Please enter valid user",6);
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

    @SuppressLint("LongLogTag")
    private void Get_Sewing_Details() {
        try {
            /*This for to avoiding error show_layout_Act getting null pointer exception in 1st edittext*/
//            SessionSave.clearSession("Session_Total_Pieces", Objects.requireNonNull(getActivity()));
//            SessionSave.clearSession("session_jsonKeyvalues", getActivity());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("usname", str_session_username);
            jsonObject.put("token", str_session_logintoken);
            Log.e("json_obje", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Sewing_Model> call = apiInterface.CHECKING_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Sewing_Model>() {
                @Override
                public void onResponse(Call<Sewing_Model> call, Response<Sewing_Model> response) {
                    if (response.body().data != null) {
                        String str_status = "";
                        str_status = response.body().status;
                        if (response.code() == 200) {
                            if (response.isSuccessful() && response.body().data.size() != 0) {
                                if (str_status.equals("error")) {
//                                    Log.e("str_status", str_status);
                                    constraintLayout_checking_rv_layout.setVisibility(View.VISIBLE);
                                    shimmer_for_checking.setVisibility(View.VISIBLE);
                                    constraintLayout_recyclerview.setVisibility(View.GONE);
                                } else {
//                                    Log.e("Else_str_success_status", str_status);
                                    if (constraintLayout_qr_code_scan.getVisibility() == View.VISIBLE) {
                                        rv_checking.setVisibility(View.GONE);
                                        constraintLayout_recyclerview.setVisibility(View.GONE);
                                    } else {
                                        rv_checking.setVisibility(View.VISIBLE);
                                        constraintLayout_recyclerview.setVisibility(View.VISIBLE);
                                    }
                                    tv_no_data_available.setVisibility(View.GONE);
                                    shimmer_for_checking.setVisibility(View.GONE);
                                    checking_recyclerView_adapter = new Checking_RecyclerView_Adapter(getActivity(), response.body().data);
                                    rv_checking.setAdapter(checking_recyclerView_adapter);
                                }
                            } else {
                                Log.e("res_data_size_else", String.valueOf(response.body().data.size()));
                                constraintLayout_recyclerview.setVisibility(View.VISIBLE);
                                constraintLayout_checking_rv_layout.setVisibility(View.GONE);
                                tv_no_data_available.setText(response.body().message);

                                if (str_hide_layout_value != null) {
                                    Log.e("str_hide_layout_value_scan_method", str_hide_layout_value);
                                    if (str_hide_layout_value.equals("1")) {
                                        tv_no_data_available.setVisibility(View.GONE);
                                    } else {
                                        tv_no_data_available.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    Log.e("str_hide_layout_value_scan_methodesllsssesss", "esleesss");
                                    if (scroll_view_layout.getVisibility() == View.VISIBLE) {
                                        tv_no_data_available.setVisibility(View.GONE);
                                    } else {
                                        tv_no_data_available.setVisibility(View.VISIBLE);
                                    }
                                }

                                shimmer_for_checking.setVisibility(View.GONE);
                                rv_checking.setVisibility(View.GONE);
                            }
                        } else if (response.code() == 401) {
                            Toast.makeText(getActivity(), "" + response.message(), Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 500) {
                            Toast.makeText(getActivity(), "" + response.message(), Toast.LENGTH_SHORT).show();
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
            case R.id.tv_edit_txt:
                Intent intent = new Intent(getActivity(), Show_Layout.class);
                startActivity(intent);
                break;
            /*
             * created on 09-05-2020
             * Initially getting values from session differentiate entry point value by '1' and '2'
             *
             * */
            case R.id.btn_save_checking:
                String str_temp_entry_point = SessionSave.getSession("Game_Entry_Point_Value_For_Checking", getActivity());
                if (str_temp_entry_point.equals("1")) {
                    if (tv_onclick_io_number_txt.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Io Number", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_color_txt.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Color", Toast.LENGTH_SHORT).show();
                    } else if (tv_total_Pieces.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Pieces", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_shift.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Shift Time", Toast.LENGTH_SHORT).show();
                    } else {
                        Get_Save_Sewing_Details(str_temp_entry_point);
                    }
                } else if (str_temp_entry_point.equals("2")) {
                    if (tv_onclick_io_number_txt.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Io Number", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_color_txt.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Color", Toast.LENGTH_SHORT).show();
                    } else if (tv_total_Pieces.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Pieces", Toast.LENGTH_SHORT).show();
                    } else if (tv_onclick_shift.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Select Shift Time", Toast.LENGTH_SHORT).show();
                    } else {
                        Get_Save_Sewing_Details(str_temp_entry_point);
                    }
                }
//                Log.e("int_fab_onlick_value", "" + int_fab_onlick_value);
               /* if (int_fab_onlick_value == 0) {
                    if (constraintLayout_recyclerview.getVisibility() == View.VISIBLE) {
                        Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), getResources().getString(R.string.successfully_submitted_txt), 2);
                        Checking_Entry_Act.tv_exit_txt.setVisibility(View.GONE);
                    }
                }
                if (int_fab_onlick_value == 2) {
                    Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()), getResources().getString(R.string.successfully_submitted_txt), 2);
                }*/
                break;
            case R.id.fab_for_checking:
                final Dialog dialog_fab = new Dialog(Objects.requireNonNull(getActivity()));
                dialog_fab.setContentView(R.layout.bar_code_alert_for_checking);
                TextView tv_scan_fab, tv_manual, tv_cancel_fab;
                tv_scan_fab = dialog_fab.findViewById(R.id.tv_scan);
                tv_manual = dialog_fab.findViewById(R.id.tv_manual);
                tv_cancel_fab = dialog_fab.findViewById(R.id.tv_cancel);
                SessionSave.clearSession("Game_Entry_Point_Value_For_Checking", Objects.requireNonNull(getActivity()));
                tv_scan_fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int_fab_onlick_value = 0;
                        SessionSave.SaveSession("Game_Entry_Point_Value_For_Checking", "1", getActivity());

                        SessionSave.clearSession("Session_IO_Number_Rechecking_Manual_Entry", getActivity());
                        SessionSave.clearSession("Session_Color_Pick_Rechecking_Manual_Entry", getActivity());

                        tv_onclick_io_number_txt.setText("");
                        tv_onclick_color_txt.setText("");

                        dialog_fab.dismiss();
                        shimmer_for_checking.setVisibility(View.GONE);
                        constraintLayout_qr_code_scan.setVisibility(View.GONE);
                        constraintLayout_checking_rv_layout.setVisibility(View.GONE);
                        scroll_view_layout.setVisibility(View.VISIBLE);
                        btn_save_checking.setVisibility(View.GONE);
                        fab_for_checking.setVisibility(View.GONE);

                        Intent intent = new Intent(getActivity(), ScannedBarcodeActivity.class);
                        intent.putExtra("Entry_Point_Onclick_Value", "2");
                        startActivity(intent);

                    }
                });
                tv_manual.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onClick(View v) {
                        int_fab_onlick_value = 2;
                        SessionSave.SaveSession("Game_Entry_Point_Value_For_Checking", "2", getActivity());
                        dialog_fab.dismiss();

                        shimmer_for_checking.setVisibility(View.GONE);
                        constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                        constraintLayout_checking_rv_layout.setVisibility(View.GONE);
                        constraintLayout_recyclerview.setVisibility(View.GONE);
                        rv_checking.setVisibility(View.GONE);
                        scroll_view_layout.setVisibility(View.VISIBLE);
                        btn_save_checking.setVisibility(View.VISIBLE);
                        fab_for_checking.setVisibility(View.GONE);

//                        Log.e("Game_Entry_Point_Value_for_checking", SessionSave.getSession("Game_Entry_Point_Value_For_Checking", getActivity()));
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
                dialog_for_io_number.setContentView(R.layout.io_number_alert);
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
                    Toast.makeText(getActivity(), "Please Select Io Number", Toast.LENGTH_SHORT).show();
                } else {
                    dialog_for_colors = new Dialog(Objects.requireNonNull(getActivity()));
                    dialog_for_colors.setContentView(R.layout.color_select_alert);
                    rv_colors = dialog_for_colors.findViewById(R.id.rv_colors);
                    rv_colors.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    rv_colors.setHasFixedSize(true);
                    dialog_for_colors.setCancelable(true);
                    Get_Color_Details();
                    dialog_for_colors.show();
                }
                break;
            case R.id.constraintLayout_checking_person_layout:
                final Dialog dialog_01 = new Dialog(Objects.requireNonNull(getActivity()));
                dialog_01.setContentView(R.layout.bar_code_alert_for_checking);
                TextView tv_scan_01, tv_manual1, tv_cancel_01;
                tv_scan_01 = dialog_01.findViewById(R.id.tv_scan);
                tv_manual1 = dialog_01.findViewById(R.id.tv_manual);
                tv_cancel_01 = dialog_01.findViewById(R.id.tv_cancel);
                tv_manual1.setText("List");

                tv_manual1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_01.dismiss();
                        dialog_for_check_person = new Dialog(Objects.requireNonNull(getActivity()));
                        dialog_for_check_person.setContentView(R.layout.check_person_alert);
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
//                        intent.putExtra("Entry_Point_Onclick_Value", "2");
                        intent.putExtra("Entry_Point_Onclick_Value_Others", "2");
                        startActivity(intent);
                        constraintLayout_qr_code_scan.setVisibility(View.GONE);
                        btn_save_checking.setVisibility(View.GONE);
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
                Intent intent1 = new Intent(getActivity(), Show_Layout.class);
                startActivity(intent1);
                /*dialog_for_pieces = new Dialog(Objects.requireNonNull(getActivity()));
                dialog_for_pieces.setContentView(R.layout.select_pieces_alert);
                *//*rv_shift_timing = dialog_for_pieces.findViewById(R.id.rv_shift_timing);
                rv_shift_timing.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                rv_shift_timing.setHasFixedSize(true);*//*
                dialog_for_pieces.setCancelable(true);
                dialog_for_pieces.show();*/
                break;
            case R.id.constraintLayout_select_shift:
                if (tv_onclick_io_number_txt.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Select IO Number", Toast.LENGTH_SHORT).show();
                } else if (tv_onclick_color_txt.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Select Color", Toast.LENGTH_SHORT).show();
                } else if (tv_onclick_checking_person_txt.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Select Checking Person", Toast.LENGTH_SHORT).show();
                } else if (tv_total_Pieces.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Select Pieces", Toast.LENGTH_SHORT).show();
                } else {
                    dialog_for_shift_timing = new Dialog(Objects.requireNonNull(getActivity()));
                    dialog_for_shift_timing.setContentView(R.layout.select_shift_alert_for_checking);
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
    private void Get_Save_Sewing_Details(final String str_temp_entry_point) {
//        Log.e("str_temp_entry_point_btn", str_temp_entry_point);
        try {
            String str_io_num = SessionSave.getSession("Session_IO_Number", getActivity());
            String str_io_num_for_manual_entry = SessionSave.getSession("Session_IO_Number_Manual_Entry", getActivity());

            String str_color_pick = SessionSave.getSession("Session_Color_Pick", getActivity());
            String str_color_pick_for_manual_entry = SessionSave.getSession("Session_Color_Pick_Manual_Entry", getActivity());

            String str_checking_person_id = SessionSave.getSession("Session_Onclick_id_value_For_Checking_Person_Checking", getActivity());
            String str_checking_person = SessionSave.getSession("Session_Checking_Person", getActivity());

            String str_total_pieces = SessionSave.getSession("Session_Total_Pieces", getActivity());
            String str_ok_pieces = SessionSave.getSession("Session_Ok_Pieces", getActivity());
            String str_defect_pieces = SessionSave.getSession("Session_Defect_Pieces", getActivity());

            String str_shift_id = SessionSave.getSession("Session_Onclick_id_value_For_Shift_Timing_Checking", getActivity());
            String str_shift_name = SessionSave.getSession("Session_Shift_Timing", getActivity());

            String str_bundle_num_save = SessionSave.getSession("Session_Bundle_Number", getActivity());
//            Log.e("str_bundle_num_save", str_bundle_num_save);

            JSONObject jsonObject = new JSONObject();
            if (str_temp_entry_point.equals("1")) {
                jsonObject.put("usname", str_session_username);
                jsonObject.put("token", str_session_logintoken);
                jsonObject.put("bundle_no", str_bundle_num_save);
                jsonObject.put("io_no", str_io_num);

                jsonObject.put("operator_id", str_checking_person_id);
                jsonObject.put("operator_name", str_checking_person);

                jsonObject.put("operation_id", "1");
                jsonObject.put("operation_name", "test");

                jsonObject.put("shift_id", str_shift_id);
                jsonObject.put("shift_name", str_shift_name);

                /*Newly added params -created 09-05-2020*/
                jsonObject.put("entry_type", "system");
                jsonObject.put("color_code", str_color_pick);
                jsonObject.put("tot_pcs", str_total_pieces);
                jsonObject.put("finish_pcs", str_ok_pieces);
//                jsonObject.put("defect_pcs", str_defect_pieces);
                jsonObject.put("defect_pcs", str_session_value_for_integer);
                Log.e("Get_assign_checking_json_01", jsonObject.toString());
            } else if (str_temp_entry_point.equals("2")) {
                jsonObject.put("usname", str_session_username);
                jsonObject.put("token", str_session_logintoken);
                jsonObject.put("bundle_no", str_bundle_num_save);
                jsonObject.put("io_no", str_io_num_for_manual_entry);

                jsonObject.put("operator_id", str_checking_person_id);
                jsonObject.put("operator_name", str_checking_person);

                jsonObject.put("operation_id", "1");
                jsonObject.put("operation_name", "test");

                jsonObject.put("shift_id", str_shift_id);
                jsonObject.put("shift_name", str_shift_name);

                /*Newly added params -created 09-05-2020*/
                jsonObject.put("entry_type", "manual");
                jsonObject.put("color_code", str_color_pick_for_manual_entry);
                jsonObject.put("tot_pcs", str_total_pieces);
                jsonObject.put("finish_pcs", str_ok_pieces);
//                jsonObject.put("defect_pcs", str_defect_pieces);
                jsonObject.put("defect_pcs", str_session_value_for_integer);
                Log.e("Get_assign_checking_json_02", jsonObject.toString());
            }
            APIInterface apiInterface = Factory.getClient();
            Call<Full_App_Model_Class> call = apiInterface.GET_ASSIGN_CHECKING_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Full_App_Model_Class>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResponse(Call<Full_App_Model_Class> call, Response<Full_App_Model_Class> response) {
                    if (response.isSuccessful()) {
//                        Toast.makeText(getActivity(), "" + response.body().message, Toast.LENGTH_SHORT).show();
//                        Log.e("Responsee", String.valueOf(Objects.requireNonNull(response.body()).message));
                        String str_status = "";
                        String str_message = "";
                        assert response.body() != null;
                        str_status = response.body().status;
                        str_message = response.body().message;
                        if (str_status.equalsIgnoreCase("success")) {
                            if (str_temp_entry_point.equals("1")) {
                                SessionSave.clearSession("Session_IO_Number", getActivity());
                                SessionSave.clearSession("Session_Color_Pick", getActivity());
                                SessionSave.clearSession("Session_Checking_Person", getActivity());
                                SessionSave.clearSession("Session_Shift_Timing", getActivity());
                                SessionSave.clearSession("Session_Total_Pieces", getActivity());
                                SessionSave.clearSession("session_jsonKeyvalues", getActivity());

                                constraintLayout_pieces_layout.setVisibility(View.VISIBLE);
                                constraintLayout_edit_pieces_layout.setVisibility(View.INVISIBLE);
                                tv_onclick_io_number_txt.setText("");
                                tv_onclick_color_txt.setText("");
                                tv_onclick_checking_person_txt.setText("");
                                tv_total_Pieces.setText("");
                                tv_ok_Pieces.setText("");
                                tv_defect_Pieces.setText("");
                                tv_onclick_shift.setText("");

                                tv_io_number_normal_txt.setText(R.string.io_number_txt);
                                tv_color_normal_txt.setText(R.string.color_txt);
                                tv_normal_checking_person_txt.setText(R.string.select_checking_person_txt);
                                tv_pieces_or_total_pieces_txt.setText(R.string.enter_pieces_txt);
                                tv_select_shift.setText(R.string.select_shift_txt);

                                Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()),
                                        getResources().getString(R.string.successfully_submitted_txt), 2);
                                Checking_Entry_Act.tv_exit_txt.setVisibility(View.GONE);
                                Get_Sewing_Details();

                                scroll_view_layout.setVisibility(View.GONE);
                                constraintLayout_qr_code_scan.setVisibility(View.GONE);
                                btn_save_checking.setVisibility(View.GONE);
                                constraintLayout_checking_rv_layout.setVisibility(View.VISIBLE);
                                constraintLayout_recyclerview.setVisibility(View.VISIBLE);
                                fab_for_checking.setVisibility(View.VISIBLE);
                                rv_checking.setVisibility(View.VISIBLE);
                            } else if (str_temp_entry_point.equals("2")) {
                                SessionSave.clearSession("Session_IO_Number_Manual_Entry", getActivity());
                                SessionSave.clearSession("Session_Color_Pick_Manual_Entry", getActivity());
                                SessionSave.clearSession("Session_Checking_Person", getActivity());
                                SessionSave.clearSession("Session_Shift_Timing", getActivity());
                                SessionSave.clearSession("Session_Total_Pieces", getActivity());
                                SessionSave.clearSession("session_jsonKeyvalues", getActivity());

                                tv_onclick_io_number_txt.setText("");
                                tv_onclick_color_txt.setText("");
                                tv_onclick_checking_person_txt.setText("");
                                tv_total_Pieces.setText("");
                                tv_ok_Pieces.setText("");
                                tv_defect_Pieces.setText("");
                                tv_onclick_shift.setText("");

                                constraintLayout_pieces_layout.setVisibility(View.VISIBLE);
                                constraintLayout_edit_pieces_layout.setVisibility(View.INVISIBLE);

                                tv_io_number_normal_txt.setText(R.string.io_number_txt);
                                tv_color_normal_txt.setText(R.string.color_txt);
                                tv_normal_checking_person_txt.setText(R.string.select_checking_person_txt);
                                tv_pieces_or_total_pieces_txt.setText(R.string.enter_pieces_txt);
                                tv_select_shift.setText(R.string.select_shift_txt);

                                Toast_Message.showToastMessage(Objects.requireNonNull(getActivity()),
                                        getResources().getString(R.string.successfully_submitted_txt), 2);
                                Get_Sewing_Details();

                                scroll_view_layout.setVisibility(View.GONE);
                                constraintLayout_qr_code_scan.setVisibility(View.GONE);
                                btn_save_checking.setVisibility(View.GONE);
                                constraintLayout_checking_rv_layout.setVisibility(View.VISIBLE);
                                constraintLayout_recyclerview.setVisibility(View.VISIBLE);
                                fab_for_checking.setVisibility(View.VISIBLE);
                                rv_checking.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(getActivity(), "" + str_message, Toast.LENGTH_SHORT).show();
                            if (str_temp_entry_point.equals("1")) {
                                constraintLayout_recyclerview.setVisibility(View.GONE);
                                constraintLayout_checking_rv_layout.setVisibility(View.GONE);
                                fab_for_checking.setVisibility(View.GONE);
                                rv_checking.setVisibility(View.GONE);
                                constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                                scroll_view_layout.setVisibility(View.VISIBLE);
                                btn_save_checking.setVisibility(View.VISIBLE);
                            } else if (str_temp_entry_point.equals("2")) {
                                constraintLayout_checking_rv_layout.setVisibility(View.GONE);
                                constraintLayout_recyclerview.setVisibility(View.GONE);
                                constraintLayout_recyclerview.setVisibility(View.GONE);
                                fab_for_checking.setVisibility(View.GONE);
                                rv_checking.setVisibility(View.GONE);
                                constraintLayout_qr_code_scan.setVisibility(View.VISIBLE);
                                scroll_view_layout.setVisibility(View.VISIBLE);
                                btn_save_checking.setVisibility(View.VISIBLE);
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

    private void Get_Checking_Person_Details() {
        /*checking_person_ArrayList.add("Sam");
        checking_person_ArrayList.add("Sam1");
        checking_person_ArrayList.add("Sam2");
        checking_person_ArrayList.add("Sam3");
        checking_person_ArrayList.add("Sam4");
        checking_person_ArrayList.add("Sam5");
        checking_person_ArrayList.add("Sam6");
        checking_person_ArrayList.add("Sam7");
        checking_person_ArrayList.add("Sam8");
        checking_person_ArrayList.add("Sam9");
        rv_checking_person.setVisibility(View.VISIBLE);
        checking_person_adapter_for_checking = new Checking_Person_Adapter_For_Checking(getActivity(), checking_person_ArrayList);
        rv_checking_person.setAdapter(checking_person_adapter_for_checking);*/


        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("usname", str_session_username);
            jsonObject.put("token", str_session_logintoken);
            jsonObject.put("role", "checking");
            APIInterface apiInterface = Factory.getClient();
            Call<Operator_Model> call = apiInterface.OPERATOR_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Operator_Model>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(Call<Operator_Model> call, Response<Operator_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            String str_status = "";
                            str_status = response.body() != null ? Objects.requireNonNull(response.body()).status : null;
                            if (str_status.equals("error")) {
                                dialog_for_check_person.dismiss();
                                Toast.makeText(getActivity(), "" + response.body().message, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getActivity(), "This user doesn't have permission to access the Checking Person data", Toast.LENGTH_SHORT).show();
                            } else {
                                rv_checking_person.setVisibility(View.VISIBLE);
                                checking_person_adapter_for_checking = new Checking_Person_Adapter_For_Checking(getActivity(), response.body().data);
                                rv_checking_person.setAdapter(checking_person_adapter_for_checking);
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

    private void Get_Color_Details() {
        /*color_ArrayList.add("#09877");
        color_ArrayList.add("#09878");
        color_ArrayList.add("#09879");
        color_ArrayList.add("#09870");
        color_ArrayList.add("#09871");
        color_ArrayList.add("#09872");
        color_ArrayList.add("#09873");
        color_ArrayList.add("#09874");
        color_ArrayList.add("#09875");
        color_ArrayList.add("#09876");
        rv_colors.setVisibility(View.VISIBLE);
        color_adapter_for_checking = new Color_Adapter_For_Checking(getActivity(), color_ArrayList);
        rv_colors.setAdapter(color_adapter_for_checking);
*/


        try {
            String str_io_number_local = "";
            if (str_session_game_entry_point_value.equals("1")) {
                str_io_number_local = SessionSave.getSession("Session_IO_Number", Objects.requireNonNull(getActivity()));
            } else if (str_session_game_entry_point_value.equals("2")) {
                str_io_number_local = SessionSave.getSession("Session_IO_Number_Manual_Entry", Objects.requireNonNull(getActivity()));
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("usname", str_session_username);
            jsonObject.put("token", str_session_logintoken);
            jsonObject.put("io_no", str_io_number_local);
//            Log.e("json_operation", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Operator_Model> call = apiInterface.COLOR_CODE_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Operator_Model>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
                                color_adapter_for_checking = new Color_Adapter_For_Checking(getActivity(), response.body().data);
                                rv_colors.setAdapter(color_adapter_for_checking);
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

    private void Get_Io_Number_Details() {
        /*ionumber_ArrayList.add("1234");
        ionumber_ArrayList.add("1235");
        ionumber_ArrayList.add("1236");
        ionumber_ArrayList.add("1237");
        ionumber_ArrayList.add("1238");
        ionumber_ArrayList.add("1239");
        ionumber_ArrayList.add("1230");
        ionumber_ArrayList.add("1231");
        ionumber_ArrayList.add("1232");
        ionumber_ArrayList.add("1232");
        rv_io_number.setVisibility(View.VISIBLE);
        io_number_adapter_for_checking = new Io_Number_Adapter_For_Checking(getActivity(), ionumber_ArrayList);
        rv_io_number.setAdapter(io_number_adapter_for_checking);*/


        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("usname", str_session_username);
            jsonObject.put("token", str_session_logintoken);
//            Log.e("json_operation", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Operator_Model> call = apiInterface.IO_NUMBER_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Operator_Model>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
                                io_number_adapter_for_checking = new Io_Number_Adapter_For_Checking(getActivity(), response.body().data);
                                rv_io_number.setAdapter(io_number_adapter_for_checking);
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

            APIInterface apiInterface = Factory.getClient();
            Call<Operator_Model> call = apiInterface.SHIFT_TIMING_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<Operator_Model>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
                                shift_timing_adapter_for_checking = new Shift_Timing_Adapter_For_Checking(getActivity(), response.body().data);
                                rv_shift_timing.setAdapter(shift_timing_adapter_for_checking);
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

    private class Io_Number_Adapter_For_Checking extends RecyclerView.Adapter<Io_Number_Adapter_For_Checking.ViewHolder> {
        //        ArrayList<String> stringArrayList;
        ArrayList<Operator_Model.Data> stringArrayList;
        Context mContext;


        /*public Io_Number_Adapter_For_Checking(FragmentActivity activity, ArrayList<String> ionumber_arrayList) {
            this.mContext = activity;
            this.stringArrayList = ionumber_arrayList;
        }*/

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
                    tv_io_number_normal_txt.setText("");
                    tv_onclick_io_number_txt.setText("");
                    SessionSave.SaveSession("Session_IO_Number_Manual_Entry", holder.tv_io_number_pick.getText().toString(), getActivity());
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

    private class Color_Adapter_For_Checking extends RecyclerView.Adapter<Color_Adapter_For_Checking.ViewHolder> {
        ArrayList<Operator_Model.Data> stringArrayList;
        Context mContext;


        /*public Color_Adapter_For_Checking(FragmentActivity activity, ArrayList<String> ionumber_arrayList) {
            this.mContext = activity;
            this.stringArrayList = ionumber_arrayList;
        }*/

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
                    tv_color_normal_txt.setText("");
                    SessionSave.SaveSession("Session_Color_Pick_Manual_Entry", holder.tv_color_pick.getText().toString(), getActivity());
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

    private class Checking_Person_Adapter_For_Checking extends RecyclerView.Adapter<Checking_Person_Adapter_For_Checking.ViewHolder> {
        //        ArrayList<String> stringArrayList;
        ArrayList<Operator_Model.Data> stringArrayList;
        Context mContext;


        /*public Checking_Person_Adapter_For_Checking(FragmentActivity activity, ArrayList<String> ionumber_arrayList) {
            this.mContext = activity;
            this.stringArrayList = ionumber_arrayList;
        }*/

        public Checking_Person_Adapter_For_Checking(FragmentActivity activity, ArrayList<Operator_Model.Data> data) {
            this.mContext = activity;
            this.stringArrayList = data;
        }

        @NonNull
        @Override
        public Checking_Person_Adapter_For_Checking.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view;
            view = inflater.inflate(R.layout.checking_person_adapter_details_layout, parent, false);
            return new Checking_Person_Adapter_For_Checking.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Checking_Person_Adapter_For_Checking.ViewHolder holder, int position) {
            holder.tv_checking_person_pick.setText(stringArrayList.get(position).name);

            holder.tv_checking_person_pick.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("LongLogTag")
                @Override
                public void onClick(View v) {
                    holder.tv_checking_person_pick.getText().toString();
                    tv_normal_checking_person_txt.setText("");
                    String str_checking_person_id = stringArrayList.get(position).id;
                    SessionSave.SaveSession("Session_Onclick_id_value_For_Checking_Person_Checking", str_checking_person_id, getActivity());
                    SessionSave.SaveSession("Session_Checking_Person", holder.tv_checking_person_pick.getText().toString(), getActivity());
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

    private class Shift_Timing_Adapter_For_Checking extends RecyclerView.Adapter<Shift_Timing_Adapter_For_Checking.ViewHolder> {
        ArrayList<Operator_Model.Data> stringArrayList;
        Context mContext;

        public Shift_Timing_Adapter_For_Checking(FragmentActivity activity, ArrayList<Operator_Model.Data> data) {
            this.mContext = activity;
            this.stringArrayList = data;
        }

        @NonNull
        @Override
        public Shift_Timing_Adapter_For_Checking.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view;
            view = inflater.inflate(R.layout.select_shift_details_layout, parent, false);
            return new Shift_Timing_Adapter_For_Checking.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Shift_Timing_Adapter_For_Checking.ViewHolder holder, int position) {
            String str_from_time = stringArrayList.get(position).from_time;
            String str_to_time = stringArrayList.get(position).to_time;
            String str_merge_timings = str_from_time + "  to  " + str_to_time;

            holder.tv_from_shift_timing.setText(str_merge_timings);
            holder.tv_from_shift_timing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_select_shift.setText("");
                    String str_checking_person_id = stringArrayList.get(position).id;
                    SessionSave.SaveSession("Session_Onclick_id_value_For_Shift_Timing_Checking", str_checking_person_id, getActivity());
                    SessionSave.SaveSession("Session_Shift_Timing", holder.tv_from_shift_timing.getText().toString(), getActivity());
                    tv_onclick_shift.setText(holder.tv_from_shift_timing.getText().toString());
                    dialog_for_shift_timing.dismiss();

                    if (!(tv_onclick_io_number_txt.getText().toString().isEmpty())
                            && (!(tv_onclick_color_txt.getText().toString().isEmpty()))
                            && (!(tv_onclick_checking_person_txt.getText().toString().isEmpty())
                            && (!(tv_onclick_shift.getText().toString().isEmpty())))) {
                        btn_save_checking.setEnabled(true);
                        btn_save_checking.setBackground(mContext.getResources().getDrawable(R.drawable.checking_entry_button_bg));
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
