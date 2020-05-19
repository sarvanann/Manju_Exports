package com.manju_exports;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class Toast_Message {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void showToastMessage(Context context, String message, int onclick_value) {
        LayoutInflater inflater = LayoutInflater.from(context.getApplicationContext());
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) ((Activity) context).findViewById(R.id.customToast));
        if (onclick_value == 1) {
            layout.setBackground(context.getApplicationContext().getDrawable(R.drawable.buy_toast_bg_for_sewing));
        } else if (onclick_value == 2) {
            layout.setBackground(context.getApplicationContext().getDrawable(R.drawable.buy_toast_bg_for_checking));
        } else if (onclick_value == 3) {
            layout.setBackground(context.getApplicationContext().getDrawable(R.drawable.buy_toast_bg_for_re_checking));
        } else if (onclick_value == 4) {
            layout.setBackground(context.getApplicationContext().getDrawable(R.drawable.buy_toast_bg_for_ironing));
        } else if (onclick_value == 5) {
            layout.setBackground(context.getApplicationContext().getDrawable(R.drawable.buy_toast_bg_for_packing));
        }else if (onclick_value == 6) {
            layout.setBackground(context.getApplicationContext().getDrawable(R.drawable.buy_toast_bg_for_error));
        }

        // set a message
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);
        // Toast...
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
