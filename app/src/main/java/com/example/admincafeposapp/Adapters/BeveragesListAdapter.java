package com.example.admincafeposapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.admincafeposapp.Model.Beverages;
import com.example.admincafeposapp.R;

import java.util.List;

public class BeveragesListAdapter extends RecyclerView.Adapter<BeveragesListAdapter.ViewHolder> {
    private Context context;
    private List<Beverages> beveragesList;

    public BeveragesListAdapter(Context context, List<Beverages> beveragesList) {
        this.context = context;
        this.beveragesList = beveragesList;
    }

    @Override
    public BeveragesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.beverages_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BeveragesListAdapter.ViewHolder holder, int position) {
        holder.nameText.setText(beveragesList.get(position).getItem_name());
        holder.priceText.setText(String.format("%.2f", beveragesList.get(position).getItem_price()));
    }

    @Override
    public int getItemCount() {
        return beveragesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        View bView;

        TextView nameText, priceText;

        ViewHolder(View itemView) {
            super(itemView);
            bView = itemView;

            nameText = bView.findViewById(R.id.b_name_text);
            priceText = bView.findViewById(R.id.b_price_text);
        }
    }
}
