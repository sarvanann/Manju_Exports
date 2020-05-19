package com.manju_exports.Adapters;/*
package com.manju_export.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.manju_export.Fragments.Sewing_Frag;
import com.manju_export.QR_Code_Scan_Act;
import com.manju_export.R;

import java.util.ArrayList;

import static com.manju_export.Fragments.Sewing_Frag.btn_save_new;
import static com.manju_export.Fragments.Sewing_Frag.tv_onclick_operation;
import static com.manju_export.Fragments.Sewing_Frag.tv_onclick_shift;
import static com.manju_export.Fragments.Sewing_Frag.tv_onclick_tailor_txt;
import static com.manju_export.Fragments.Sewing_Frag.tv_select_shift;

public class Shift_Timing_Adapter extends RecyclerView.Adapter<Shift_Timing_Adapter.ViewHolder> {
    ArrayList<String> stringArrayList;
    Context mContext;

    public Shift_Timing_Adapter(QR_Code_Scan_Act qr_code_scan_act, ArrayList<String> shift_timing_list) {
        this.mContext = qr_code_scan_act;
        this.stringArrayList = shift_timing_list;
    }

    public Shift_Timing_Adapter(FragmentActivity activity, ArrayList<String> shift_timing_list) {
        this.mContext = activity;
        this.stringArrayList = shift_timing_list;
    }

    @NonNull
    @Override
    public Shift_Timing_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;
        view = inflater.inflate(R.layout.select_shift_details_layout, parent, false);
        return new Shift_Timing_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Shift_Timing_Adapter.ViewHolder holder, int position) {
        holder.tv_shift_timing.setText(stringArrayList.get(position));

        holder.tv_shift_timing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tv_shift_timing.getText().toString();
                tv_select_shift.setText("");
                Sewing_Frag.tv_onclick_shift.setText(holder.tv_shift_timing.getText().toString());
                Sewing_Frag.dialog_for_shift_timing.dismiss();

                if (!(tv_onclick_tailor_txt.getText().toString().isEmpty()) && (!(tv_onclick_operation.getText().toString().isEmpty())) && (!(tv_onclick_shift.getText().toString().isEmpty()))) {
                    btn_save_new.setEnabled(true);
                    btn_save_new.setBackground(mContext.getResources().getDrawable(R.drawable.login_button_bg));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_shift_timing;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_shift_timing = itemView.findViewById(R.id.tv_shift_timing);
        }
    }
}
*/
