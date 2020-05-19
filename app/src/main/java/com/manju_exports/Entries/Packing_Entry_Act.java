package com.manju_exports.Entries;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.manju_exports.Fragments.Packing_Entry_Frag;
import com.manju_exports.Home_Activity;
import com.manju_exports.R;

import java.util.Objects;

public class Packing_Entry_Act extends FragmentActivity {
    Fragment fragment;
    FragmentTransaction ft;
    Toolbar toolbar;
    TextView tv_title_txt;
    NavigationView navigationView;
    TextView menuRight_icon;
    public static TextView tv_exit_txt;
    public static DrawerLayout drawer;
    @SuppressLint("StaticFieldLeak")
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.packing_entry_act);
        toolbar = findViewById(R.id.toolbar);
        tv_title_txt = findViewById(R.id.tv_title_txt);
        tv_exit_txt = findViewById(R.id.tv_exit_txt);
        menuRight_icon = findViewById(R.id.menuRight);
        tv_exit_txt.setVisibility(View.GONE);
        toolbar.setBackgroundColor(getResources().getColor(R.color.light_red_color));
        tv_title_txt.setText(getResources().getString(R.string.packing_txt));

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        /*
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

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
*/
        tv_exit_txt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Packing_Entry_Act.this);
                dialog.setContentView(R.layout.exit_alert);
                TextView tv_yes, tv_no;
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                tv_yes = dialog.findViewById(R.id.tv_yes);
                tv_no = dialog.findViewById(R.id.tv_cancel);
                tv_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(Packing_Entry_Act.this, Home_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
                tv_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        Show_Checking_Fragment();
    }

    private void Show_Checking_Fragment() {
        fragment = new Packing_Entry_Frag();
        if (fragment != null) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Packing_Entry_Act.this, Home_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
