package com.manju_exports.Fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.vision.CameraSource;
import com.manju_exports.Entries.Checking_Entry_Act;
import com.manju_exports.Entries.Re_Checking_Act;
import com.manju_exports.R;

import java.util.Objects;

public class Dashboard_Frag extends Fragment implements View.OnClickListener {
    private String str_hide_layout_value = "";
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dashboard, container, false);
        ConstraintLayout constraintLayout_sewing = view.findViewById(R.id.constraintLayout_sewing);
        ConstraintLayout constraintLayout_checking_entry = view.findViewById(R.id.constraintLayout_checking_entry);
        ConstraintLayout constraintLayout_re_checking_entry = view.findViewById(R.id.constraintLayout_re_checking_entry);
        ConstraintLayout constraintLayout_ironing_entry = view.findViewById(R.id.constraintLayout_ironing_entry);
        ConstraintLayout constraintLayout_packing_entry = view.findViewById(R.id.constraintLayout_packing_entry);

        constraintLayout_sewing.setOnClickListener(this);
        constraintLayout_checking_entry.setOnClickListener(this);
        constraintLayout_re_checking_entry.setOnClickListener(this);
        constraintLayout_ironing_entry.setOnClickListener(this);
        constraintLayout_packing_entry.setOnClickListener(this);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Log.e("camera_permission", "Granted");
        } else {
            ActivityCompat.requestPermissions(getActivity(), new
                    String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }

        return view;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.constraintLayout_sewing:
                Fragment fragment = new Sewing_Frag();
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, fragment).commit();
                break;
            case R.id.constraintLayout_checking_entry:
                Bundle bundle = Objects.requireNonNull(getActivity()).getIntent().getExtras();
                if (bundle == null) {
                    str_hide_layout_value = null;
                    fragment = new Checking_Entry_Frag();
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, fragment).commit();
                } else {
                    str_hide_layout_value = bundle.getString("hide_layout");
                    if (str_hide_layout_value != null && str_hide_layout_value.equals("1")) {
                        Intent intent = new Intent(getActivity(), Checking_Entry_Act.class);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.constraintLayout_re_checking_entry:
//                Intent intent1 = new Intent(getActivity(), Re_Checking_Act.class);
//                startActivity(intent1);
                fragment = new Re_Checking_Entry_Frag();
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, fragment).commit();

                Bundle bundle_recheck = Objects.requireNonNull(getActivity()).getIntent().getExtras();
                if (bundle_recheck == null) {
                    str_hide_layout_value = null;
                    fragment = new Re_Checking_Entry_Frag();
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, fragment).commit();
                } else {
                    str_hide_layout_value = bundle_recheck.getString("hide_layout");
                    if (str_hide_layout_value != null && str_hide_layout_value.equals("1")) {
                        Intent intent = new Intent(getActivity(), Re_Checking_Act.class);
                        startActivity(intent);
                    }
                }

                break;
            case R.id.constraintLayout_ironing_entry:
                fragment = new Ironing_Entry_Frag();
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, fragment).commit();
                break;
            case R.id.constraintLayout_packing_entry:
                fragment = new Packing_Entry_Frag();
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, fragment).commit();
                break;
        }
    }
}
