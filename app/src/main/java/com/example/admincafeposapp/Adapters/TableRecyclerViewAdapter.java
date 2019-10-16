package com.example.admincafeposapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admincafeposapp.Model.Tables;
import com.example.admincafeposapp.R;

import java.util.ArrayList;

public class TableRecyclerViewAdapter extends RecyclerView.Adapter<TableRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Tables> tablesArrayList;

    public TableRecyclerViewAdapter(Context context, ArrayList<Tables> tablesArrayList) {
        this.context = context;
        this.tablesArrayList = tablesArrayList;
    }

    @NonNull
    @Override
    public TableRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.table_list_row, parent,false);
        ViewHolder holder =new ViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TableRecyclerViewAdapter.ViewHolder holder, int position) {

        Tables table = tablesArrayList.get(position);
        holder.textView_tableNo.setText(String.valueOf(table.getTableNo()));
        holder.textView_tableNoOfSeat.setText(String.valueOf(table.getTableNoOfSeat()));
        holder.textView_tableStatus.setText(String.valueOf(table.getTableStatus()));
    }

    @Override
    public int getItemCount() {
        return tablesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_tableNo, textView_tableNoOfSeat, textView_tableStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_tableNo = itemView.findViewById(R.id.textView_tableNoData);
            textView_tableNoOfSeat = itemView.findViewById(R.id.textView_tableNoSeatData);
            textView_tableStatus = itemView.findViewById(R.id.textView_tableStatusData);

        }
    }
}
