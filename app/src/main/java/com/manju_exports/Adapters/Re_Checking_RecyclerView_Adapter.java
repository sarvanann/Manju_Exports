package com.manju_exports.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.manju_exports.Model.Sewing_Model;
import com.manju_exports.R;

import java.util.ArrayList;

public class Re_Checking_RecyclerView_Adapter extends RecyclerView.Adapter<Re_Checking_RecyclerView_Adapter.ViewHolder> {
    ArrayList<Sewing_Model.Data> dataArrayList;
    Context mContext;

    public Re_Checking_RecyclerView_Adapter(FragmentActivity activity, ArrayList<Sewing_Model.Data> data) {
        this.dataArrayList = data;
        this.mContext = activity;
    }

    @NonNull
    @Override
    public Re_Checking_RecyclerView_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;
        view = inflater.inflate(R.layout.re_checking_recyclerview_list_details_layout, parent, false);
        return new Re_Checking_RecyclerView_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Re_Checking_RecyclerView_Adapter.ViewHolder holder, int position) {
        holder.tv_operator_name.setText(dataArrayList.get(position).operator_name);
        holder.tv_operator_id.setText(dataArrayList.get(position).operator_id);
        holder.tv_io_num.setText(dataArrayList.get(position).io_no);
        holder.tv_operation_name.setText(dataArrayList.get(position).operation_id);
        holder.tv_operation_id.setText(dataArrayList.get(position).operation_name);
        holder.tv_shift.setText(dataArrayList.get(position).shift_name);

    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_operator_name, tv_operator_id, tv_io_num, tv_operation_id, tv_operation_name, tv_shift;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_operator_name = itemView.findViewById(R.id.tv_operator_name);
            tv_operator_id = itemView.findViewById(R.id.tv_operator_id);
            tv_io_num = itemView.findViewById(R.id.tv_io_num);
            tv_operation_id = itemView.findViewById(R.id.tv_operation_id);
            tv_operation_name = itemView.findViewById(R.id.tv_operation_name);
            tv_shift = itemView.findViewById(R.id.tv_shift);
        }
    }
}


