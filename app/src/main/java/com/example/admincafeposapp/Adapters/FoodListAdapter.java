package com.example.admincafeposapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.admincafeposapp.Model.Food;
import com.example.admincafeposapp.R;

import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {
    private Context context;
    private List<Food> foodList;

    public FoodListAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @Override
    public FoodListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FoodListAdapter.ViewHolder holder, int position) {

        holder.nameText.setText(foodList.get(position).getItem_name());
        holder.priceText.setText(String.valueOf(foodList.get(position).getItem_price()));
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View fView;

        public TextView nameText, priceText;

        public ViewHolder(View itemView) {
            super(itemView);
            fView = itemView;

            nameText = fView.findViewById(R.id.f_name_text);
            priceText = fView.findViewById(R.id.f_price_text);
        }
    }
}
