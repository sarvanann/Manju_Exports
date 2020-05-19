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
import com.manju_export.Model.Operator_Model;
import com.manju_export.R;

import java.util.ArrayList;

public class Tailors_Select_Adapter extends RecyclerView.Adapter<Tailors_Select_Adapter.ViewHolder> {
    ArrayList<Operator_Model.Data> stringArrayList;
    Context mContext;


    public Tailors_Select_Adapter(FragmentActivity activity, ArrayList<Operator_Model.Data> data) {
        this.mContext = activity;
        this.stringArrayList = data;
    }

    @NonNull
    @Override
    public Tailors_Select_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;
        view = inflater.inflate(R.layout.tailors_select_details_layout, parent, false);
        return new Tailors_Select_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Tailors_Select_Adapter.ViewHolder holder, int position) {
        holder.tv_tailors_select.setText(stringArrayList.get(position).name);

        holder.tv_tailors_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tv_tailors_select.getText().toString();
                Sewing_Frag.tv_normal_tailor_txt.setText("");
                Sewing_Frag.tv_onclick_tailor_txt.setText(holder.tv_tailors_select.getText().toString());
                Sewing_Frag.dialog_for_tailor.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_tailors_select;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tailors_select = itemView.findViewById(R.id.tv_tailors_select);
        }
    }
}
*/
