package com.manju_exports;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.manju_exports.Interface.APIInterface;
import com.manju_exports.Interface.Factory;
import com.manju_exports.Model.UserModel;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * In this activity user enter details.Here we maintain internet connectivity status and we integrate login api using retrofit.
 * If user register with application means  session value updated to "1" .It will reflect in splash activity
 *
 *
 * */
public class Registration_Screen extends Activity implements View.OnClickListener {
    SQLiteDatabase db;
    EditText et_username,
            et_password;
    Button btn_login,
            btn_show_hide_crnt_pwd;
    String str_user_name,
            str_password;
    ProgressDialog pd;
    //This is for Internet alert using snackbar status
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean internetConnected = true;
    Snackbar snackbar;
    Boolean show = false;
    String str_logintoken, str_username, str_admin_name, str_phn_num;

    private static final int SEND_CAMERA_PERMISSION_REQUEST_CODE = 1000;
    private static final String TAG = "TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_screen);
        db = (getApplicationContext().openOrCreateDatabase("manjuexports.db", Context.MODE_PRIVATE, null));
        pd = new ProgressDialog(Registration_Screen.this);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_show_hide_crnt_pwd = findViewById(R.id.btn_show_hide_crnt_pwd);
        btn_login.setOnClickListener(this);
        //This is password icon show and hide
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(et_password.getText().toString().length() == 0)) {
                    btn_show_hide_crnt_pwd.setVisibility(View.VISIBLE);
                    btn_show_hide_crnt_pwd.setBackgroundResource((R.drawable.eye_open));
                } else {
                    btn_show_hide_crnt_pwd.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //This is password icon show and hide onclick operations for visiblity of password
        btn_show_hide_crnt_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_show_hide_crnt_pwd.getText().toString().equals("Show")) {
                    btn_show_hide_crnt_pwd.setText("Hide");
                    et_password.setTransformationMethod(null);
                    et_password.setSelection(et_password.getText().toString().length());
                    btn_show_hide_crnt_pwd.setBackgroundResource((R.drawable.eye_hide));
                } else {
                    btn_show_hide_crnt_pwd.setText("Show");
                    et_password.setTransformationMethod(new PasswordTransformationMethod());
                    et_password.setSelection(et_password.getText().toString().length());
                    btn_show_hide_crnt_pwd.setBackgroundResource((R.drawable.eye_open));
                }
            }
        });
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        str_user_name = et_username.getText().toString();
        str_password = et_password.getText().toString();
        if (!isNetworkAvaliable()) {
            registerInternetCheckReceiver();
        } else {
            if (str_user_name.isEmpty()) {
                Toast.makeText(this, getResources().getString(R.string.pls_enter_username_txt), Toast.LENGTH_SHORT).show();
                et_username.requestFocus();
            } else if (str_password.isEmpty()) {
                Toast.makeText(this, getResources().getString(R.string.pls_enter_pwd_txt), Toast.LENGTH_SHORT).show();
                et_password.requestFocus();
            } else if (str_password.length() <= 4) {
                Toast.makeText(this, getResources().getString(R.string.pwd_length_min_5_txt), Toast.LENGTH_SHORT).show();
                et_password.requestFocus();
            } else {
                //Note: I have placed this code in onResume for demostration purpose. Be careful when you use it in
                // production code
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //You can show permission rationale if shouldShowRequestPermissionRationale() returns true.
                    //I will skip it for this demo
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (PermissionUtils.neverAskAgainSelected(this, Manifest.permission.CAMERA)) {
                            displayNeverAskAgainDialog();
                        } else {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                                    SEND_CAMERA_PERMISSION_REQUEST_CODE);
                        }
                    }
                } else {
                    softKeyboardVisibility(show);
                    Get_Login_Details();
                }
            }
        }
    }

    //In this method we send user details to api.If respose is successfull move to Home_activity else remains the same page and show the error message to the user.
    private void Get_Login_Details() {
        try {
            pd.setMessage("Loading...");
            pd.show();
            pd.setCancelable(false);
            ProgressBar progressbar = pd.findViewById(android.R.id.progress);
            progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#000000"), android.graphics.PorterDuff.Mode.SRC_IN);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("usname", str_user_name);
            jsonObject.put("pwd", str_password);
            APIInterface apiInterface = Factory.getClient();
            Call<UserModel> call = apiInterface.SIGNUP_RESPONSE_CALL("application/json", jsonObject.toString());
            call.enqueue(new Callback<UserModel>() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            String str_status = Objects.requireNonNull(response.body()).status;
                            String str_message = response.body().message;
                            if (str_status.equalsIgnoreCase("success")) {
                                pd.dismiss();
                                str_username = response.body().data.usname;
                                str_admin_name = response.body().data.admin_name;
                                str_logintoken = response.body().data.logintoken;
                                str_phn_num = response.body().data.ph_no;
                                SessionSave.SaveSession("Session_Logintoken", str_logintoken, Registration_Screen.this);
                                SessionSave.SaveSession("Session_UserName", str_username, Registration_Screen.this);
//                                Log.e("str_logintoken", str_logintoken);
//                                Get_Insert_Signup_Values();
                                SessionSave.SaveSession("Login_Session_valueee", "1", Registration_Screen.this);
                                Intent intent = new Intent(Registration_Screen.this, Home_Activity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                                Log.e("phone_num", String.valueOf(response.body().data));
                            } else {
                                pd.dismiss();
                                Toast.makeText(Registration_Screen.this, "" + str_message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (response.code() == 403) {
                        pd.dismiss();
                        Toast.makeText(Registration_Screen.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 500) {
                        pd.dismiss();
                        Toast.makeText(Registration_Screen.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    Toast.makeText(Registration_Screen.this, "Faliue" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Get_Insert_Signup_Values() {
        String select = "Select * FROM LOGINDETAILS";
        Cursor cursor = db.rawQuery(select, null);
        int n1 = cursor.getCount();
//        Log.e("n1_size", "" + n1);
        if (n1 > 0) {
            ContentValues contentValues1 = new ContentValues();
            contentValues1.put("STATUS", 1);
            db.update("LOGINDETAILS", contentValues1, null, null);
            Insert_Singup_Details();
        } else {
            Insert_Singup_Details();
        }
        cursor.close();
    }

    private void Insert_Singup_Details() {
        if (rowIDExistUsername(str_user_name)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("STATUS", 1);
            contentValues.put("SIGNUPSTATUS", 1);
            contentValues.put("USERNAME", str_user_name);
            contentValues.put("LOGINTOKEN", str_logintoken);
            db.insert("LOGINDETAILS", null, contentValues);
//            Log.e("contentValues_String", contentValues.toString());
//            DBEXPORT();
        }
    }

    private void DBEXPORT() {
        Toast.makeText(Registration_Screen.this, "exported", Toast.LENGTH_SHORT).show();
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source;
        FileChannel destination;
        String currentDBPath = "/data/" + "com.manju_export" + "/databases/" + "manjuexports.db";
        String backupDBPath = "manjuexports.db";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean rowIDExistUsername(String str_user_name) {
        String select = "select * from LOGINDETAILS ";
        Cursor cursor = db.rawQuery(select, null);
        List<String> labels = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(0);
                labels.add(var);
            } while (cursor.moveToNext());
        }
        cursor.close();
        boolean allMatch = true;
        for (String string : labels) {
            if (string.equalsIgnoreCase(str_user_name)) {
                allMatch = false;
                break;
            }
        }
        return allMatch;
    }

    /*This method is used for network connectivity*/
    private boolean isNetworkAvaliable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null;
    }

    /*This method is used for forced to close and open keyboard*/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void softKeyboardVisibility(boolean show) {
        InputMethodManager inputMethodManager = (InputMethodManager) Registration_Screen.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (show) {
            assert inputMethodManager != null;
            inputMethodManager.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
        } else {
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
        }
    }

    /**
     * Method to register runtime broadcast receiver to show snackbar alert for internet connection..
     */
    private void registerInternetCheckReceiver() {
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.wifi.STATE_CHANGE");
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadcastReceiver, internetFilter);
    }

    /**
     * Runtime Broadcast receiver inner class to capture internet connectivity events
     */
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = getConnectivityStatusString(context);
            setSnackbarMessage(status);
        }
    };

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = getConnectivityStatus(context);
        String status = null;
        if (conn == TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }

    private void setSnackbarMessage(String status) {
        String internetStatus;
        if (status.equalsIgnoreCase("Wifi enabled") || status.equalsIgnoreCase("Mobile data enabled")) {
            internetStatus = getResources().getString(R.string.back_online_txt);
            snackbar = Snackbar.make(findViewById(R.id.fab), internetStatus, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundResource(R.color.blue_color);
        } else {
            internetStatus = getResources().getString(R.string.check_internet_conn_txt);
            snackbar = Snackbar.make(findViewById(R.id.fab), internetStatus, Snackbar.LENGTH_INDEFINITE);
            snackbar.getView().setBackgroundResource(R.color.red_color_new);
        }
        // Changing message text color
        snackbar.setActionTextColor(Color.WHITE);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        if (internetStatus.equalsIgnoreCase(getResources().getString(R.string.check_internet_conn_txt))) {
            if (internetConnected) {
                snackbar.show();
                internetConnected = false;
            }
        } else {
            if (!internetConnected) {
                internetConnected = true;
                snackbar.show();
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerInternetCheckReceiver();
    }

    private void displayNeverAskAgainDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need to capture barcode value for performing necessary task. Please permit the permission through "
                + "Settings screen.\n\nSelect Permissions -> Enable permission");
        builder.setCancelable(false);
        builder.setPositiveButton("Permit Manually", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        if (SEND_CAMERA_PERMISSION_REQUEST_CODE == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted successfully");
                Toast.makeText(this, "Permission granted successfully", Toast.LENGTH_LONG).show();
            } else {
                PermissionUtils.setShouldShowStatus(this, Manifest.permission.CAMERA);
            }
        }
    }

    private static class PermissionUtils {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public static boolean neverAskAgainSelected(final Activity activity, final String permission) {
            final boolean prevShouldShowStatus = getRatinaleDisplayStatus(activity, permission);
            final boolean currShouldShowStatus = activity.shouldShowRequestPermissionRationale(permission);
            return prevShouldShowStatus != currShouldShowStatus;
        }

        public static void setShouldShowStatus(final Context context, final String permission) {
            SharedPreferences genPrefs = context.getSharedPreferences("GENERIC_PREFERENCES", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = genPrefs.edit();
            editor.putBoolean(permission, true);
            editor.commit();
        }

        public static boolean getRatinaleDisplayStatus(final Context context, final String permission) {
            SharedPreferences genPrefs = context.getSharedPreferences("GENERIC_PREFERENCES", Context.MODE_PRIVATE);
            return genPrefs.getBoolean(permission, false);
        }
    }
}
