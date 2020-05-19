package com.manju_exports;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

/*
 * This is the first activity to display the user for 3600 sec.After that here we maintain session values for which screen display to the user.
 *For example user not register the application, session value show No data so,it'll show splash and then move to login screen.
 *Or else if session is 1 means it'll automatically move to Home_Activity.
 */
public class Splash_Act extends Activity {
    static long SLEEP_TIME = 20;
    SQLiteDatabase db;
    int int_normal_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        db = getApplicationContext().openOrCreateDatabase("manjuexports.db", Context.MODE_PRIVATE, null);
        db.execSQL("create table if not exists LOGINDETAILS(USERNAME varchar,LOGINTOKEN int,STATUS int,SIGNUPSTATUS int)");
        /*This is for thread to  visible splash screen 3600 sec to user */
        IntentLauncher launcher = new IntentLauncher();
        launcher.start();

    }

    private class IntentLauncher extends Thread {
        public void run() {
            try {
                Thread.sleep(SLEEP_TIME * 180);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String select = "Select SIGNUPSTATUS from LOGINDETAILS where STATUS ='" + 1 + "'";
            Cursor cursor = db.rawQuery(select, null);
            int n1 = cursor.getCount();
            if (n1 > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        int_normal_status = cursor.getInt(0);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            } else {
                int_normal_status = 0;
            }

           /* Log.e("int_normal_status", String.valueOf(int_normal_status));
            if (int_normal_status == 0) {
                Intent intent = new Intent(Splash_Act.this, Registration_Screen.class);
                startActivity(intent);
            } else if (int_normal_status == 1) {
                Intent intent = new Intent(Splash_Act.this, Home_Activity.class);
                startActivity(intent);
            }*/


            String str_login_value = SessionSave.getSession("Login_Session_valueee", Splash_Act.this);
//            Log.e("str_login_value", str_login_value);
            if (str_login_value.equalsIgnoreCase("No data") || str_login_value.equalsIgnoreCase("0")) {
                Intent intent = new Intent(Splash_Act.this, Registration_Screen.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(Splash_Act.this, Home_Activity.class);
                startActivity(intent);
            }

        }
    }
}
