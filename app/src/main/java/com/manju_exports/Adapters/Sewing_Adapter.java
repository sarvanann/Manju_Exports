package com.manju_exports.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.manju_exports.Model.Sewing_Model;
import com.manju_exports.R;

import java.util.ArrayList;

public class Sewing_Adapter extends RecyclerView.Adapter<Sewing_Adapter.ViewHolder> {
    ArrayList<Sewing_Model.Data> dataArrayList;
    Context mContext;

    public Sewing_Adapter(FragmentActivity activity, ArrayList<Sewing_Model.Data> data) {
        this.dataArrayList = data;
        this.mContext = activity;
    }

    @NonNull
    @Override
    public Sewing_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;
        view = inflater.inflate(R.layout.sewing_list_details_layout, parent, false);
        return new Sewing_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Sewing_Adapter.ViewHolder holder, int position) {
        holder.tv_io_num.setText(dataArrayList.get(position).io_no);
        holder.tv_operator_name.setText(dataArrayList.get(position).operator_name);
        holder.tv_operation_name.setText(dataArrayList.get(position).operation_name);
        holder.tv_shift.setText(dataArrayList.get(position).shift_name);
//        holder.tv_time.setText(dataArrayList.get(position).operator_id);
        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "In Progress", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_io_num, tv_operator_name, tv_operation_name, tv_shift, tv_time, tv_edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_io_num = itemView.findViewById(R.id.tv_io_num);
            tv_operator_name = itemView.findViewById(R.id.tv_operator_name);
            tv_operation_name = itemView.findViewById(R.id.tv_operation_name);
            tv_shift = itemView.findViewById(R.id.tv_shift);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_edit = itemView.findViewById(R.id.tv_edit);
        }
    }
}
