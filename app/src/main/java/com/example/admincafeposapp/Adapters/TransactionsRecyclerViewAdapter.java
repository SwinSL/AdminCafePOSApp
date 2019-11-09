package com.example.admincafeposapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admincafeposapp.Model.Order;
import com.example.admincafeposapp.R;
import java.util.List;

public class TransactionsRecyclerViewAdapter extends RecyclerView.Adapter<TransactionsRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Order> orderList;

    public TransactionsRecyclerViewAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.order_id.setText(orderList.get(position).getOrder_id());
        holder.table_no.setText(orderList.get(position).getTable_no());
        holder.order_status.setText(orderList.get(position).getOrder_status());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView order_id, table_no, order_status;
        public View view;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            view = itemView;
            order_id = itemView.findViewById(R.id.textview_transactions_transactionid_data);
            table_no = itemView.findViewById(R.id.textview_transactions_tableno_data);
            order_status = itemView.findViewById(R.id.textview_transactions_status_data);
        }
    }
}


