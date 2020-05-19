package com.manju_exports;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Testing_Act extends AppCompatActivity {
    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 1000;
    private static final String TAG = "TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Note: I have placed this code in onResume for demostration purpose. Be careful when you use it in
        // production code
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager
                .PERMISSION_GRANTED) {
            //You can show permission rationale if shouldShowRequestPermissionRationale() returns true.
            //I will skip it for this demo
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (PermissionUtils.neverAskAgainSelected(this, Manifest.permission.CAMERA)) {
                    displayNeverAskAgainDialog();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                            SEND_SMS_PERMISSION_REQUEST_CODE);
                }
            }

        }
    }

    private void displayNeverAskAgainDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need to send SMS for performing necessary task. Please permit the permission through "
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
        if (SEND_SMS_PERMISSION_REQUEST_CODE == requestCode) {
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
            final boolean prevShouldShowStatus = getRatinaleDisplayStatus(activity,permission);
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
            SharedPreferences genPrefs =     context.getSharedPreferences("GENERIC_PREFERENCES", Context.MODE_PRIVATE);
            return genPrefs.getBoolean(permission, false);
        }
    }
}
