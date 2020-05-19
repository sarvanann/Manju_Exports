package com.manju_exports;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.manju_exports.Entries.Checking_Entry_Act;
import com.manju_exports.Interface.APIInterface;
import com.manju_exports.Interface.Factory;
import com.manju_exports.Model.Model;
import com.manju_exports.Model.Operator_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Show_Layout extends AppCompatActivity {
    Fragment fragment;
    ConstraintLayout constraintLayout_defect_pieces;
    FragmentTransaction ft;
    TextView tv_defect_pieces_top_layout,
            tv_onclick_select_piece;

    EditText et_no_of_defect_pieces_in_bottom_layout,
            et_total_Number,
            et_ok_pieces;

    Button btn_save_checking,
            btn_plus_icon;

    ListView listView;

    private Dialog
            dialog_for_pieces;

    RecyclerView rv_entry_pieces;

    ArrayList<String> defectsArrayList = new ArrayList<>();
    ArrayList<Integer> onclick_add_integer_arrayList = new ArrayList<>();
    ArrayList<String> onclick_mistakes_arrayList = new ArrayList<>();
    ArrayList<Model> ItemModelList;

    String str_session_value_for_integer,
            str_session_value_for_mistake,
            str_session_defect_pieces,
            str_session_total_pieces,
            str_session_ok_pieces,
            str_session_username,
            str_session_logintoken;
    HashMap<ArrayList<Integer>, ArrayList<String>> arrayListArrayListHashMap = new HashMap<>();

    String str_number_of_defect_pieces_in_bottom_layout,
            str_tv_defect_pieces_in_top_layout,
            str_mistake_pieces,
            str_total_pieces,
            str_ok_pieces;

    int int_number_of_defect_pieces_in_bottom_layout,
            int_top_tv_defect_Pieces_in_top_layout,
            int_total_pieces,
            int_ok_pieces,
            int_defect_pieces;

    Select_Pieces_Adapter select_pieces_adapter;
    CustomAdapter customAdapter;
    String str_keyname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_layout);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            str_keyname = b.getString("keyname");
        }
        listView = findViewById(R.id.listview);
        btn_plus_icon = findViewById(R.id.btn_plus_icon);
        et_no_of_defect_pieces_in_bottom_layout = findViewById(R.id.et_no_of_defect_pieces_in_bottom_layout);
        getSupportActionBar().hide();

        et_total_Number = findViewById(R.id.et_total_Number);
        et_ok_pieces = findViewById(R.id.et_ok_pieces);
        tv_defect_pieces_top_layout = findViewById(R.id.tv_defect_pieces_top_layout);
        tv_onclick_select_piece = findViewById(R.id.tv_onclick_select_piece);
        btn_save_checking = findViewById(R.id.btn_save_checking);
        constraintLayout_defect_pieces = findViewById(R.id.constraintLayout_defect_pieces);
        et_no_of_defect_pieces_in_bottom_layout = findViewById(R.id.et_no_of_defect_pieces_in_bottom_layout);
        tv_onclick_select_piece = findViewById(R.id.tv_onclick_select_piece);
        btn_plus_icon = findViewById(R.id.btn_plus_icon);

        str_session_value_for_integer = SessionSave.getSession("session_jsonKeyvalues", Show_Layout.this);
        str_session_total_pieces = SessionSave.getSession("Session_Total_Pieces", Show_Layout.this);
        str_session_ok_pieces = SessionSave.getSession("Session_Ok_Pieces", Show_Layout.this);
        str_session_defect_pieces = SessionSave.getSession("Session_Defect_Pieces", Show_Layout.this);
        str_session_username = SessionSave.getSession("Session_UserName", Show_Layout.this);
        str_session_logintoken = SessionSave.getSession("Session_Logintoken", Show_Layout.this);

        btn_save_checking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onclick_mistake_aryLst", "" + onclick_mistakes_arrayList.toString());
//                SessionSave.SaveSession("Hashmap_Session_Values_For_Integer", "" + onclick_add_integer_arrayList.toString().replace("[", "").replace("]", ""), Show_Layout.this);
//                SessionSave.SaveSession("Hashmap_Session_Values_For_Mistake", "" + onclick_mistakes_arrayList.toString().replace("[", "").replace("]", ""), Show_Layout.this);
//                Log.e("save_total_show_act", et_total_Number.getText().toString());
//                Log.e("save_ok_show_act", et_ok_pieces.getText().toString());
//                Log.e("save_defect_show_act", tv_defect_pieces_top_layout.getText().toString());

                SessionSave.SaveSession("Session_Total_Pieces", et_total_Number.getText().toString(), Show_Layout.this);
                SessionSave.SaveSession("Session_Ok_Pieces", et_ok_pieces.getText().toString(), Show_Layout.this);
                SessionSave.SaveSession("Session_Defect_Pieces", tv_defect_pieces_top_layout.getText().toString(), Show_Layout.this);
                if (str_session_value_for_integer.equalsIgnoreCase("No data")) {
                    JSONArray jsonArray = new JSONArray();
                    JSONArray jsonArray1 = new JSONArray();
                    for (int i = 0; i < onclick_add_integer_arrayList.size(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        int a = onclick_add_integer_arrayList.get(i);
                        try {
                            jsonObject.put("count_value", a);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        jsonArray.put(jsonObject);
                        String b = onclick_mistakes_arrayList.get(i);
//                        Log.e("B_Value_loop", b);
                        try {
                            jsonObject.put("defect_name", b);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jsonArray1.put(jsonObject);
//                        jsonArray.put(jsonObject);
//                        Log.e("jsonObject1_inside", jsonObject.toString());
                    }
                    Log.e("jsonArray1_outside", jsonArray1.toString());
//                    Log.e("jsonArrayyyyy_outside", jsonArray.toString());

                    SessionSave.SaveSession("session_jsonKeyvalues", jsonArray1.toString(), getApplicationContext());
//                    Log.e("---->", jsonArray.toString());
//                    Log.e("Iff_Session_Value", SessionSave.getSession("session_jsonKeyvalues", getApplicationContext()));
                    Intent intent = new Intent(Show_Layout.this, Checking_Entry_Act.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("hide_layout", "1");
                    startActivity(intent);
                } else {
                    JSONArray jsonArray = new JSONArray();
                    for (int i = 0; i < ItemModelList.size(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        String a = ItemModelList.get(i).getName();
                        try {
                            jsonObject.put("count_value", a);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String b = ItemModelList.get(i).getMistake_piece();
                        try {
                            jsonObject.put("defect_name", b);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jsonArray.put(jsonObject);
                    }
                    SessionSave.SaveSession("session_jsonKeyvalues", jsonArray.toString(), getApplicationContext());
                    Log.e("---->_ELSE", jsonArray.toString());
                    Log.e("Else_Session_Value", SessionSave.getSession("session_jsonKeyvalues", getApplicationContext()));
                    Intent intent = new Intent(Show_Layout.this, Checking_Entry_Act.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("hide_layout", "1");
                    startActivity(intent);
                }
            }
        });

        /*Here we just enter input values ,if input gets empty et_ok_pieces set as empty.*/
        et_total_Number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_total_Number.getText().toString().trim().equals("")) {
                    et_ok_pieces.setText("");
                } else {
                    str_total_pieces = et_total_Number.getText().toString();
//                    Log.e("str_total_pieces", str_total_pieces);
                    int_total_pieces = Integer.parseInt(str_total_pieces);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /*Here we perform functonalites like if user enter ok piece value 50 and total value 100 here we just calculating remaining value and showed in defect pieces*/
        et_ok_pieces.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_ok_pieces.getText().toString().trim().equals("")) {
                    tv_defect_pieces_top_layout.setText("");
                } else {
                    int_total_pieces = Integer.parseInt(str_total_pieces);
                    str_ok_pieces = et_ok_pieces.getText().toString();
                    int_ok_pieces = Integer.parseInt(str_ok_pieces);
                    if (int_total_pieces > int_ok_pieces) {
                        int_defect_pieces = int_total_pieces - int_ok_pieces;
                        tv_defect_pieces_top_layout.setText(String.valueOf(int_defect_pieces));
                    } else {
                        Toast.makeText(Show_Layout.this, "Ok piece is greater than total piece value", Toast.LENGTH_SHORT).show();
                        tv_defect_pieces_top_layout.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        /*initialzing arraylist*/
        ItemModelList = new ArrayList<Model>();

        /*Getting session values for future use like when user enter edit it'll get these values from session*/
//        str_session_value_for_integer = SessionSave.getSession("Hashmap_Session_Values_For_Integer", Show_Layout.this);
//        str_session_value_for_mistake = SessionSave.getSession("Hashmap_Session_Values_For_Mistake", Show_Layout.this);


//        Log.e("sess_vlu_fr_inte", str_session_value_for_integer);
//        Log.e("sess_vlu_fr_mstk", str_session_value_for_mistake);

        if (!(str_session_value_for_integer.equalsIgnoreCase("No data"))) {
            addValue_With_Data();
        }
        btn_plus_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addValue();
            }
        });

        constraintLayout_defect_pieces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_for_pieces = new Dialog(Objects.requireNonNull(Show_Layout.this));
                dialog_for_pieces.setContentView(R.layout.check_pieces_alert);
                rv_entry_pieces = dialog_for_pieces.findViewById(R.id.rv_entry_pieces);
                rv_entry_pieces.setLayoutManager(new LinearLayoutManager(Show_Layout.this, LinearLayoutManager.VERTICAL, false));
                rv_entry_pieces.setHasFixedSize(true);
                dialog_for_pieces.setCancelable(true);
                Get_Mistakes_Details();
                dialog_for_pieces.show();
            }
        });
    }

    private void Get_Mistakes_Details() {
        /*defectsArrayList.add("Sam");
        defectsArrayList.add("Sam");
        defectsArrayList.add("Sam");
        defectsArrayList.add("Sam");
        defectsArrayList.add("Sam");
        defectsArrayList.add("Sam");
        defectsArrayList.add("Sam");
        defectsArrayList.add("Sam");
        defectsArrayList.add("Sam");
        defectsArrayList.add("Sam");
        rv_entry_pieces.setVisibility(View.VISIBLE);
        select_pieces_adapter = new Select_Pieces_Adapter(Show_Layout.this, defectsArrayList);
        rv_entry_pieces.setAdapter(select_pieces_adapter);*/

        try {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("usname", str_session_username);
            jsonObject1.put("token", str_session_logintoken);
            Log.e("Json_Mistake", jsonObject1.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<Operator_Model> call = apiInterface.MISTAKE_RESPONSE_CALL("application/json", jsonObject1.toString());
            call.enqueue(new Callback<Operator_Model>() {
                @Override
                public void onResponse(Call<Operator_Model> call, Response<Operator_Model> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            String str_status = "";
                            str_status = response.body().status;
                            if (str_status.equalsIgnoreCase("error")) {
                                dialog_for_pieces.dismiss();
                                Toast.makeText(Show_Layout.this, "This user doesn't have permission to access the mistake data.", Toast.LENGTH_SHORT).show();
                            } else {
                                rv_entry_pieces.setVisibility(View.VISIBLE);
                                select_pieces_adapter = new Select_Pieces_Adapter(Show_Layout.this, response.body().data);
                                rv_entry_pieces.setAdapter(select_pieces_adapter);
                            }
                        }
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

    /*In this method if session value is not empty means this method will call and getting session value and showed to respective fields*/
    private void addValue_With_Data() {
//        Log.e("sksldk", "dksldfl");
        et_total_Number.setText(str_session_total_pieces);
        et_ok_pieces.setText(str_session_ok_pieces);
        tv_defect_pieces_top_layout.setText(str_session_defect_pieces);


        et_total_Number.setSelection(str_session_total_pieces.length());
        ArrayList<String> stringsarrylist_for_integer = new ArrayList<>();
        ArrayList<String> stringsarrylist_for_mistake = new ArrayList<>();

//        stringsarrylist_for_integer.add(str_session_value_for_integer);
//        stringsarrylist_for_mistake.add(str_session_value_for_mistake);
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(str_session_value_for_integer);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                int int_Defectvalue = jsonObject.getInt("count_value");
                String str_defect_name = jsonObject.getString("defect_name");

                Model md = new Model(String.valueOf(int_Defectvalue), str_defect_name);
                ItemModelList.add(md);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            customAdapter = new CustomAdapter(getApplicationContext(), ItemModelList);
            listView.setAdapter(customAdapter);
            customAdapter.notifyDataSetChanged();
            et_no_of_defect_pieces_in_bottom_layout.setText("");
        }


//        if (str_session_value_for_mistake != null && (!(str_session_value_for_mistake.equalsIgnoreCase("No data")))) {
//            String s2 = stringsarrylist_for_integer.toString().replace("[", "").replace("]", "");
//            String s3[] = s2.split(",");
//            Log.e("s333", Arrays.toString(s3));
//            for (int i = 0; i < s3.length; i++) {
//                Log.e("s3_loop", "" + s3[i]);
//            }
//
//            String s2_mistake = stringsarrylist_for_mistake.toString().replace("[", "").replace("]", "");
//            String s3_mistake[] = s2_mistake.split(",");
//            Log.e("s333_mistake", Arrays.toString(s3_mistake));
//            for (int i = 0; i < s3_mistake.length; i++) {
//                Log.e("s3_mistake_loop", "" + s3_mistake[i]);
//            }
//
//            Log.e("s111", str_session_value_for_integer.replace("[", "").replace("]", "").replace(",", ""));
//            Log.e("str_session_length", String.valueOf(str_session_value_for_integer.length()));
//            for (int i = 0; i < s3.length; i++) {
//                Model md = new Model(String.valueOf(s3[i]), String.valueOf(s3_mistake[i]));
//                ItemModelList.add(md);
//                customAdapter = new CustomAdapter(getApplicationContext(), ItemModelList);
//                listView.setAdapter(customAdapter);
//                customAdapter.notifyDataSetChanged();
//                et_no_of_defect_pieces_in_bottom_layout.setText("");
//            }
//        } else {
//            try {
//                JSONArray jsonArray = new JSONArray(str_session_value_for_integer);
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//                    jsonObject.get("count_value");
//                    jsonObject.get("defect_name");
//
//                    Log.e("qqweesrfgf", ((JSONObject) jsonArray.get(i)).toString());
//                    Model md = new Model(jsonObject.getString("count_value"), jsonObject.getString("defect_name"));
//                    ItemModelList.add(md);
//                }
//                customAdapter = new CustomAdapter(getApplicationContext(), ItemModelList);
//                listView.setAdapter(customAdapter);
//                customAdapter.notifyDataSetChanged();
//                et_no_of_defect_pieces_in_bottom_layout.setText("");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
    }

    /*This method is used for inital setup like when app launch.
     *1st step it'll validate fields and then add those values to arraylist ,and add arraylist values to listview.
     * Initially it'll check sessionvalue is empty this method will call else addValue_With_Data() method will call.
     */
    @SuppressLint({"NewApi", "LongLogTag"})
    public void addValue() {
        if (str_session_value_for_integer.equalsIgnoreCase("No data")) {
            if (et_total_Number.getText().toString().isEmpty()) {
                Toast.makeText(Show_Layout.this, "Please enter total no of defects", Toast.LENGTH_SHORT).show();
                et_total_Number.requestFocus();
            } else if (et_ok_pieces.getText().toString().isEmpty()) {
                Toast.makeText(Show_Layout.this, "Please enter ok Pieces", Toast.LENGTH_SHORT).show();
                et_ok_pieces.requestFocus();
            } else if (et_no_of_defect_pieces_in_bottom_layout.getText().toString().isEmpty()) {
                Toast.makeText(Show_Layout.this, "Please enter no of defects pieces", Toast.LENGTH_SHORT).show();
                et_no_of_defect_pieces_in_bottom_layout.requestFocus();
            } else if (tv_onclick_select_piece.getText().toString().isEmpty()) {
                Toast.makeText(Show_Layout.this, "Please enter mistake", Toast.LENGTH_SHORT).show();
            } else {
                str_number_of_defect_pieces_in_bottom_layout = et_no_of_defect_pieces_in_bottom_layout.getText().toString();
                str_tv_defect_pieces_in_top_layout = tv_defect_pieces_top_layout.getText().toString();
                int_number_of_defect_pieces_in_bottom_layout = Integer.parseInt(str_number_of_defect_pieces_in_bottom_layout);
                int_top_tv_defect_Pieces_in_top_layout = Integer.parseInt(str_tv_defect_pieces_in_top_layout);
                onclick_add_integer_arrayList.add(int_number_of_defect_pieces_in_bottom_layout);
                onclick_mistakes_arrayList.add(str_mistake_pieces);
                arrayListArrayListHashMap.put(onclick_add_integer_arrayList, onclick_mistakes_arrayList);
//                SessionSave.SaveSession("Session_Total_Pieces", et_total_Number.getText().toString(), Show_Layout.this);
//                SessionSave.SaveSession("Session_Ok_Pieces", et_ok_pieces.getText().toString(), Show_Layout.this);
//                SessionSave.SaveSession("Session_Defect_Pieces", tv_defect_pieces_top_layout.getText().toString(), Show_Layout.this);
                int int_temp1 = 0;
                for (int i = 0; i < onclick_add_integer_arrayList.size(); i++) {
                    int_temp1 += onclick_add_integer_arrayList.get(i);
                }
                Log.e("after_calc", String.valueOf(onclick_add_integer_arrayList.toString()));
                Log.e("onclick_mistakes_arrayList_value", onclick_mistakes_arrayList.toString());


                final Iterator<Map.Entry<ArrayList<Integer>, ArrayList<String>>> iter = arrayListArrayListHashMap.entrySet().iterator();
                final HashSet<String> valueSet = new HashSet<>();
                while (iter.hasNext()) {
                    final Map.Entry<ArrayList<Integer>, ArrayList<String>> next = iter.next();
                    if (!valueSet.add(String.valueOf(next.getValue()))) {
                        iter.remove();
                    }
                }
//                Log.e("aryLstAryLstHash", String.valueOf(arrayListArrayListHashMap.toString()));
//                Log.e("iter", String.valueOf(iter));
//                Log.e("int_temp1", String.valueOf(int_temp1));
                /*Here we enable save button if user entered value is equal to total defect value*/
                if (int_temp1 == int_top_tv_defect_Pieces_in_top_layout) {
                    btn_save_checking.setEnabled(true);
                    btn_save_checking.setBackground(getResources().getDrawable(R.drawable.checking_entry_button_bg));
                }
                /*Here we check userd enterd value is less to total defect value.
                 * If true means add defect count and defect name to listview,else it will remove last entered element from arraylist and empty editext ,show toast message*/
                if (int_temp1 <= int_top_tv_defect_Pieces_in_top_layout) {
                    Model md = new Model(String.valueOf(str_number_of_defect_pieces_in_bottom_layout), String.valueOf(str_mistake_pieces));
                    ItemModelList.add(md);
                    customAdapter = new CustomAdapter(getApplicationContext(), ItemModelList);
                    listView.setAdapter(customAdapter);
                    customAdapter.notifyDataSetChanged();
                    et_no_of_defect_pieces_in_bottom_layout.setText("");
                    tv_onclick_select_piece.setText("");
                } else {
                    arrayListArrayListHashMap.remove(arrayListArrayListHashMap.size() - 1);
                    onclick_add_integer_arrayList.remove(onclick_add_integer_arrayList.size() - 1);
                    onclick_mistakes_arrayList.remove(onclick_mistakes_arrayList.size() - 1);
                    Log.e("aftr_remove", onclick_add_integer_arrayList.toString());
                    Log.e("aftr_remove_mistake", onclick_mistakes_arrayList.toString());

                    et_no_of_defect_pieces_in_bottom_layout.setText("");
                    tv_onclick_select_piece.setText("");
                    Toast.makeText(Show_Layout.this, "Please enter below value", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            try {
                Log.e("kkk_checking", "kokokoo");
//                addValue_With_Data();
                int int_total_count = 0;
//                if (!(str_session_value_for_integer.equals(""))) {
                if (et_total_Number.getText().toString().isEmpty()) {
                    Toast.makeText(Show_Layout.this, "Please enter total no of defects", Toast.LENGTH_SHORT).show();
                    et_total_Number.requestFocus();
                } else if (et_ok_pieces.getText().toString().isEmpty()) {
                    Toast.makeText(Show_Layout.this, "Please enter ok Pieces", Toast.LENGTH_SHORT).show();
                    et_ok_pieces.requestFocus();
                } else if (et_no_of_defect_pieces_in_bottom_layout.getText().toString().isEmpty()) {
                    Toast.makeText(Show_Layout.this, "Please enter no of defects pieces", Toast.LENGTH_SHORT).show();
                    et_no_of_defect_pieces_in_bottom_layout.requestFocus();
                } else if (tv_onclick_select_piece.getText().toString().isEmpty()) {
                    Toast.makeText(Show_Layout.this, "Please enter mistake", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < ItemModelList.size(); i++) {
                        String s1 = ItemModelList.get(i).getName().replaceAll("\\s", "");
                        int nn = Integer.parseInt(s1);
                        int_total_count += nn;
                    }
//                    Log.e("int_total_count", "" + int_total_count);


                    str_number_of_defect_pieces_in_bottom_layout = et_no_of_defect_pieces_in_bottom_layout.getText().toString();
                    str_tv_defect_pieces_in_top_layout = tv_defect_pieces_top_layout.getText().toString();
                    int_number_of_defect_pieces_in_bottom_layout = Integer.parseInt(str_number_of_defect_pieces_in_bottom_layout);
                    int_top_tv_defect_Pieces_in_top_layout = Integer.parseInt(str_tv_defect_pieces_in_top_layout);


                    int_number_of_defect_pieces_in_bottom_layout = Integer.parseInt(str_number_of_defect_pieces_in_bottom_layout);
                    int_top_tv_defect_Pieces_in_top_layout = Integer.parseInt(str_tv_defect_pieces_in_top_layout);
                    onclick_add_integer_arrayList.add(int_number_of_defect_pieces_in_bottom_layout);
                    onclick_mistakes_arrayList.add(str_mistake_pieces);

//                    SessionSave.SaveSession("Session_Total_Pieces", et_total_Number.getText().toString(), Show_Layout.this);
//                    SessionSave.SaveSession("Session_Ok_Pieces", et_ok_pieces.getText().toString(), Show_Layout.this);
//                    SessionSave.SaveSession("Session_Defect_Pieces", tv_defect_pieces_top_layout.getText().toString(), Show_Layout.this);

                    //*This is for addding total loop value and user enter defect value*//*
                    int int_nnn = int_total_count + int_number_of_defect_pieces_in_bottom_layout;
//                    Log.e("int_nnn", "" + int_number_of_defect_pieces_in_bottom_layout);
                    if (int_nnn == int_top_tv_defect_Pieces_in_top_layout) {
                        btn_save_checking.setEnabled(true);
                        btn_save_checking.setBackground(getResources().getDrawable(R.drawable.checking_entry_button_bg));
                    }
//                    Log.e("int_nnn", "" + int_nnn);
                    if (int_nnn <= int_top_tv_defect_Pieces_in_top_layout) {
                        Model md = new Model(String.valueOf(str_number_of_defect_pieces_in_bottom_layout), String.valueOf(str_mistake_pieces));
                        ItemModelList.add(md);
                        customAdapter = new CustomAdapter(getApplicationContext(), ItemModelList);
                        listView.setAdapter(customAdapter);
                        customAdapter.notifyDataSetChanged();
                        et_no_of_defect_pieces_in_bottom_layout.setText("");
                        tv_onclick_select_piece.setText("");

                        JSONArray jsonArray = new JSONArray();

                        for (int i = 0; i < ItemModelList.size(); i++) {
                            JSONObject jsonObject = new JSONObject();
                            String a = ItemModelList.get(i).getName();
                            try {
                                jsonObject.put("count_value", a);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            for (int j = 0; j < ItemModelList.size(); j++) {
                            String b = ItemModelList.get(i).getMistake_piece();
                            try {
                                jsonObject.put("defect_name", b);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            jsonArray.put(jsonObject);
                        }
                        SessionSave.SaveSession("session_jsonKeyvalues", jsonArray.toString(), getApplicationContext());
                        Log.e("save_json_checking", jsonArray.toString());
                            /*if (int_temp1 == int_top_tv_defect_Pieces_in_top_layout) {
                                btn_save_checking.setEnabled(true);
                                btn_save_checking.setBackground(getResources().getDrawable(R.drawable.checking_entry_button_bg));
                            }*/
//                            SessionSave.SaveSession("Hashmap_Session_Values_For_Integer", "" + ItemModelList.toString().replace("[", "").replace("]", ""), Show_Layout.this);
//                            SessionSave.SaveSession("Hashmap_Session_Values_For_Mistake", "" + ItemModelList.toString().replace("[", "").replace("]", ""), Show_Layout.this);
                    } else {
                        et_no_of_defect_pieces_in_bottom_layout.setText("");
                        tv_onclick_select_piece.setText("");
                        Toast.makeText(Show_Layout.this, "Please enter below value", Toast.LENGTH_SHORT).show();
                    }

                }
//                else {
//                    JSONArray jsonArray = new JSONArray(str_session_value_for_integer);
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//                        String s1 = jsonObject.getString("count_value").trim();
//                        int int_nnn = Integer.parseInt(s1);
//                        int_total_count += int_nnn;
//                        Log.e("int_total_count", String.valueOf(int_total_count));
//                    }
//                    int_top_tv_defect_Pieces_in_top_layout = Integer.parseInt(tv_defect_pieces_top_layout.getText().toString());
//                    Log.e("int_top_tv_defect_Pieces_in_top_layout_kkk", "" + int_top_tv_defect_Pieces_in_top_layout);
//                    int_number_of_defect_pieces_in_bottom_layout = Integer.parseInt(et_no_of_defect_pieces_in_bottom_layout.getText().toString());
//
//                    str_number_of_defect_pieces_in_bottom_layout = et_no_of_defect_pieces_in_bottom_layout.getText().toString();
//                    str_tv_defect_pieces_in_top_layout = tv_defect_pieces_top_layout.getText().toString();
//                    int_number_of_defect_pieces_in_bottom_layout = Integer.parseInt(str_number_of_defect_pieces_in_bottom_layout);
//                    int_top_tv_defect_Pieces_in_top_layout = Integer.parseInt(str_tv_defect_pieces_in_top_layout);
//                    int int_nnn = int_total_count + int_number_of_defect_pieces_in_bottom_layout;
//             //      *//*Here we check userd enterd value is less to total defect value.
////                     * If true means add defect count and defect name to listview,else it will remove last entered element from arraylist and empty editext ,show toast message*/
//                    if (int_nnn < int_top_tv_defect_Pieces_in_top_layout) {
////                        String str_total_count = String.valueOf(int_number_of_defect_pieces_in_bottom_layout);
//                        Model md = new Model(String.valueOf(int_nnn), String.valueOf(str_mistake_pieces));
//                        ItemModelList.add(md);
//                        customAdapter = new CustomAdapter(getApplicationContext(), ItemModelList);
//                        listView.setAdapter(customAdapter);
//                        customAdapter.notifyDataSetChanged();
//                        et_no_of_defect_pieces_in_bottom_layout.setText("");
//                        tv_onclick_select_piece.setText("");
//                    } else {
//                        arrayListArrayListHashMap.remove(arrayListArrayListHashMap.size() - 1);
//                        onclick_add_integer_arrayList.remove(onclick_add_integer_arrayList.size() - 1);
//                        onclick_mistakes_arrayList.remove(onclick_mistakes_arrayList.size() - 1);
//                        Log.e("aftr_remove", onclick_add_integer_arrayList.toString());
//                        Log.e("aftr_remove_mistake", onclick_mistakes_arrayList.toString());
//
//                        et_no_of_defect_pieces_in_bottom_layout.setText("");
//                        tv_onclick_select_piece.setText("");
//                        Toast.makeText(Show_Layout.this, "Please enter below value", Toast.LENGTH_SHORT).show();
//                    }
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public class CustomAdapter extends BaseAdapter {
        Context context;
        ArrayList<Model> itemModelList;

        public CustomAdapter(Context context, ArrayList<Model> modelList) {
            this.context = context;
            this.itemModelList = modelList;
        }

        @Override
        public int getCount() {
            return itemModelList.size();
        }

        @Override
        public Object getItem(int position) {
            return itemModelList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.items, null);
            TextView tvName = convertView.findViewById(R.id.tvName);
            TextView tv_onclick_select_misktake_piece = convertView.findViewById(R.id.tv_onclick_select_misktake_piece);
            Button imgRemove = convertView.findViewById(R.id.imgRemove);
            Model m = itemModelList.get(position);
            tvName.setText(m.getName());
            tv_onclick_select_misktake_piece.setText(m.getMistake_piece());
            // click listiner for remove button
            imgRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemModelList.remove(position);
                    try {
                        if (onclick_add_integer_arrayList.size() > 0) {
                            onclick_add_integer_arrayList.remove(position);
                        }
                    } catch (IndexOutOfBoundsException iob) {
                        iob.printStackTrace();
                    }

                    String str_n1 = "", str_n2 = "";
//                    Log.e("removing_position", "" + itemModelList.size());
                    customAdapter.notifyDataSetChanged();
                    JSONArray jsonArray = new JSONArray();

                    for (int i = 0; i < itemModelList.size(); i++) {
//                        Log.e("before", itemModelList.get(i).getName());
                        str_n1 = itemModelList.get(i).getName().trim();
                        str_n2 = itemModelList.get(i).getMistake_piece().trim();
//                        Log.e("before-->", itemModelList.get(i).getMistake_piece());
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("count_value", str_n1);
                            jsonObject.put("defect_name", str_n2);
                            jsonArray.put(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    int int_total_count = 0;
                    for (int i = 0; i < ItemModelList.size(); i++) {
                        String s1 = ItemModelList.get(i).getName().replaceAll("\\s", "");
                        int nn = Integer.parseInt(s1);
                        int_total_count += nn;
                    }


                    if (int_total_count < int_top_tv_defect_Pieces_in_top_layout) {
                        btn_save_checking.setEnabled(false);
                        btn_save_checking.setBackground(getResources().getDrawable(R.drawable.disable_button_bg));
                    }
//                    Log.e("str_n1", jsonArray.toString());
                    SessionSave.SaveSession("Hashmap_Session_Values_For_Integer", "" + jsonArray.toString(), Show_Layout.this);
                    SessionSave.clearSession("Hashmap_Session_Values_For_Mistake", Show_Layout.this);
                    if (itemModelList.size() == 0) {
                        SessionSave.clearSession("Hashmap_Session_Values_For_Integer", Show_Layout.this);
                        SessionSave.clearSession("Hashmap_Session_Values_For_Mistake", Show_Layout.this);
                        onclick_add_integer_arrayList.clear();
                    }
                }
            });
            return convertView;
        }
    }

    class Select_Pieces_Adapter extends RecyclerView.Adapter<Select_Pieces_Adapter.ViewHolder> {
        ArrayList<Operator_Model.Data> stringArrayList;
        Context mContext;


        /*public Select_Pieces_Adapter(Show_Layout testing_act, ArrayList<String> defectsArrayList) {
            this.mContext = testing_act;
            this.stringArrayList = defectsArrayList;
        }*/

        public Select_Pieces_Adapter(Show_Layout testing_act, ArrayList<Operator_Model.Data> data) {
            this.mContext = testing_act;
            this.stringArrayList = data;
        }

        @NonNull
        @Override
        public Select_Pieces_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view;
            view = inflater.inflate(R.layout.select_pieces_details_layout, parent, false);
            return new Select_Pieces_Adapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Select_Pieces_Adapter.ViewHolder holder, int position) {
            String str_defect_name = stringArrayList.get(position).name;
            holder.tv_pieces_adapter.setText(str_defect_name);
            holder.tv_pieces_adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_onclick_select_piece.setText("");
                    tv_onclick_select_piece.setText(holder.tv_pieces_adapter.getText().toString());
                    str_mistake_pieces = tv_onclick_select_piece.getText().toString();
                    dialog_for_pieces.dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return stringArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_pieces_adapter;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_pieces_adapter = itemView.findViewById(R.id.tv_pieces_adapter);
            }
        }
    }
}
