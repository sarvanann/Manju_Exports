package com.manju_exports;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.manju_exports.Fragments.Checking_Entry_Frag;
import com.manju_exports.Fragments.Dashboard_Frag;
import com.manju_exports.Fragments.Ironing_Entry_Frag;
import com.manju_exports.Fragments.Packing_Entry_Frag;
import com.manju_exports.Fragments.Re_Checking_Entry_Frag;
import com.manju_exports.Fragments.Sewing_Frag;
import com.manju_exports.Interface.APIInterface;
import com.manju_exports.Interface.Factory;
import com.manju_exports.Model.UserModel;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/*
 * This activity is used for navigation drawer and act as dashboard.
 * tv_title_txt act as static so all activity can access this component named their own.
 * here we maintain on back press ,all other activity controlled by this activity.
 * And Finlay here we add ShowDashboard method this will display home activity displaying details.
 */

public class Home_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    boolean doubleBackToExitPressedOnce = false;
    Fragment fragment;
    FragmentTransaction ft;
    NavigationView navigationView;
    TextView menuRight_icon;
    public static DrawerLayout drawer;
    @SuppressLint("StaticFieldLeak")
    public static TextView tv_title_txt;
    @SuppressLint("StaticFieldLeak")
    public static Toolbar toolbar;
    private String str_session_username, str_session_logintoken;
    ActionBarDrawerToggle toggle;
    @SuppressLint("StaticFieldLeak")
    public static ImageView imageView_in_nav_header;

    SQLiteDatabase db;
    Dialog logout_dialog;

    //This is for Internet alert using snackbar status
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private boolean internetConnected = true;
    Snackbar snackbar;
    Boolean show = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = Objects.requireNonNull(getApplicationContext()).openOrCreateDatabase("manjuexports.db", Context.MODE_PRIVATE, null);
        db.execSQL("create table if not exists LOGINDETAILS(USERNAME varchar,LOGINTOKEN int,STATUS int);");

        /*String select = "select USERNAME from LOGINDETAILS where STATUS ='" + 1 + "'";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                String str_username = "";
                String str_username1 = "";
                str_username = cursor.getString(0);
                Log.e("str_usernamewww", str_username);
                Log.e("str_username1", str_username1);
            } while (cursor.moveToNext());
        }
        cursor.close();
//        DBEXPORT();*/

        toolbar = findViewById(R.id.toolbar);
        tv_title_txt = findViewById(R.id.tv_title_txt);
        imageView_in_nav_header = findViewById(R.id.imageView_in_nav_header);
        menuRight_icon = findViewById(R.id.menuRight);
        toolbar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        Home_Activity.tv_title_txt.setText(getResources().getString(R.string.home_txt));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //This line is used for hiding menu_icon
        toggle.setDrawerIndicatorEnabled(false);
        menuRight_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        str_session_username = SessionSave.getSession("Session_UserName", Home_Activity.this);
        str_session_logintoken = SessionSave.getSession("Session_Logintoken", Home_Activity.this);
        //This method this will display home activity displaying details.
        ShowDashboard();
    }

    private void ShowDashboard() {
        fragment = new Dashboard_Frag();
        if (fragment != null) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }

    //here we maintain on back press ,all other activity controlled by this activity.
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Fragment ff = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (ff instanceof Sewing_Frag) {
                ((Sewing_Frag) ff).onBackPressed();
            }
            if (ff instanceof Checking_Entry_Frag) {
                ((Checking_Entry_Frag) ff).onBackPressed();
            }

            if (ff instanceof Re_Checking_Entry_Frag) {
                ((Re_Checking_Entry_Frag) ff).onBackPressed();
            }

            if (ff instanceof Ironing_Entry_Frag) {
                ((Ironing_Entry_Frag) ff).onBackPressed();
            }
            if (ff instanceof Packing_Entry_Frag) {
                ((Packing_Entry_Frag) ff).onBackPressed();
            }


            /*if (ff instanceof QR_Code_Scan_Frag) {
                ((QR_Code_Scan_Frag) ff).onBackPressed();
            }*/
        } else {
            if (!doubleBackToExitPressedOnce) {
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            } else {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                a.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(a);
                System.exit(0);
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

       /* //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_sewing) {
            Toast.makeText(Home_Activity.this, "sewing", Toast.LENGTH_SHORT).show();
            fragment = new Sewing_Frag();
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, fragment).commit();
        } else if (id == R.id.nav_checking) {
            fragment = new Checking_Entry_Frag();
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, fragment).commit();
        } else if (id == R.id.nav_rechecking) {
            fragment = new Re_Checking_Entry_Frag();
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, fragment).commit();
        } else if (id == R.id.nav_ironing) {
            fragment = new Ironing_Entry_Frag();
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, fragment).commit();
        } else if (id == R.id.nav_packing) {
            fragment = new Packing_Entry_Frag();
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, fragment).commit();
        } else if (id == R.id.nav_logout) {
            logout_dialog = new Dialog(Home_Activity.this);
            logout_dialog.setContentView(R.layout.exit_logout_alert);
            TextView tv_yes, tv_no;
            Objects.requireNonNull(logout_dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            logout_dialog.show();

            tv_yes = logout_dialog.findViewById(R.id.tv_yes);
            tv_no = logout_dialog.findViewById(R.id.tv_cancel);
            tv_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isNetworkAvaliable()) {
                        registerInternetCheckReceiver();
                    } else {
                        Get_Logout_Details();
                    }
                    /*ContentValues cv = new ContentValues();
                    cv.put("STATUS", 0);
                    cv.put("SIGNUPSTATUS", 0);
                    db.update("LOGINDETAILS", cv, null, null);
                    DBEXPORT();*/

                    /*String str_login_value = SessionSave.getSession("Login_Session_valueee", Home_Activity.this);
//                    Log.e("str_login_value", str_login_value);
                    if (str_login_value.equals("1")) {
                        SessionSave.SaveSession("Login_Session_valueee", "0", Home_Activity.this);
                        Intent intent = new Intent(Home_Activity.this, Registration_Screen.class);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        logout_dialog.dismiss();
//                        Log.e("str_login_value_After_logout", SessionSave.getSession("Login_Session_valueee", Home_Activity.this));
                    }*/
                }
            });
            tv_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout_dialog.dismiss();
                }
            });
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void DBEXPORT() {
//        Toast.makeText(Home_Activity.this, "exported", Toast.LENGTH_SHORT).show();
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

    private void Get_Logout_Details() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("usname", str_session_username);
            jsonObject.put("token", str_session_logintoken);
            Log.e("json_logout", jsonObject.toString());
            APIInterface apiInterface = Factory.getClient();
            Call<UserModel> call = apiInterface.GET_LOGOUT_DETAILS("application/json", jsonObject.toString());
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.code() == 200) {
                        String str_status = "";
                        String str_message = "";
                        str_message = response.message();
                        str_status = response.body().status;
                        Log.e("str_status_logout", str_status);
                        Log.e("str_message_logout", str_message);
                        if (response.isSuccessful()) {
                            if (str_status.equalsIgnoreCase("error")) {
                                Toast.makeText(Home_Activity.this, "" + str_message, Toast.LENGTH_SHORT).show();
                            } else {
                                String str_login_value = SessionSave.getSession("Login_Session_valueee", Home_Activity.this);
                                Log.e("str_login_value_logout", str_login_value);
                                if (str_login_value.equals("1")) {
                                    SessionSave.SaveSession("Login_Session_valueee", "0", Home_Activity.this);
                                    Intent intent = new Intent(Home_Activity.this, Registration_Screen.class);
                                    intent.addCategory(Intent.CATEGORY_HOME);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                    logout_dialog.dismiss();
                                    Toast.makeText(Home_Activity.this, "Logged out successfully !", Toast.LENGTH_SHORT).show();
//                        Log.e("str_login_value_After_logout", SessionSave.getSession("Login_Session_valueee", Home_Activity.this));
                                }
                            }
                        } else {
                            Toast.makeText(Home_Activity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.code() == 401) {
                        Toast.makeText(Home_Activity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 500) {
                        Toast.makeText(Home_Activity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*This method is used for network connectivity*/
    private boolean isNetworkAvaliable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null;
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
}
