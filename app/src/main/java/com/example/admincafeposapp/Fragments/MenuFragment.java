package com.example.admincafeposapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.admincafeposapp.Model.Beverages;
import com.example.admincafeposapp.Model.BeveragesDialog;
import com.example.admincafeposapp.Model.BeveragesListAdapter;
import com.example.admincafeposapp.Model.BeveragesRemoveDialog;
import com.example.admincafeposapp.Model.Food;
import com.example.admincafeposapp.Model.FoodDialog;
import com.example.admincafeposapp.Model.FoodListAdapter;
import com.example.admincafeposapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "FireLog";
    RecyclerView foodListView, beveragesListView;
    private FirebaseFirestore firestore;
    private List<Food> foodList;
    private List<Beverages> beveragesList;
    private FoodListAdapter foodListAdapter;
    private BeveragesListAdapter beveragesListAdapter;
    private Button foodAddBtn, foodDelBtn, beveragesAddBtn, beveragesDelBtn;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        foodList = new ArrayList<>();
        beveragesList = new ArrayList<>();

        foodListView = view.findViewById(R.id.foodList);
        foodListView.setHasFixedSize(true);
        foodListView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        foodListAdapter = new FoodListAdapter(this.getContext(), foodList);
        foodListView.setAdapter(foodListAdapter);

        beveragesListView = view.findViewById(R.id.beveragesList);
        beveragesListView.setHasFixedSize(true);
        beveragesListView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        beveragesListAdapter = new BeveragesListAdapter(this.getContext(), beveragesList);
        beveragesListView.setAdapter(beveragesListAdapter);

        foodAddBtn = view.findViewById(R.id.faddButton);
        foodAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFoodDialog();
            }
        });

        beveragesAddBtn = view.findViewById(R.id.baddButton);
        beveragesAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBeverageDialog();
            }
        });

        beveragesDelBtn = view.findViewById(R.id.bdeleteButton);
        beveragesDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRemoveBeverageDialog();
            }
        });

        swipeRefreshLayout = view.findViewById(R.id.List);
        swipeRefreshLayout.setOnRefreshListener(this);
        firestore = FirebaseFirestore.getInstance();
        getData();
    }

    public void openFoodDialog()
    {
        FoodDialog foodDialog = new FoodDialog();
        foodDialog.show(getActivity().getSupportFragmentManager(), "Food Dialog");
    }

    public void openBeverageDialog()
    {
        BeveragesDialog beveragesDialog = new BeveragesDialog();
        beveragesDialog.show(getActivity().getSupportFragmentManager(), "Beverage Dialog");
    }

    public void openRemoveBeverageDialog()
    {
        BeveragesRemoveDialog beveragesRemoveDialog = new BeveragesRemoveDialog();
        beveragesRemoveDialog.show(getActivity().getSupportFragmentManager(), "Beverage Remove Dialog");
    }

    public void getData()
    {
        firestore.collection("Food").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    foodList.clear();
                    for(DocumentSnapshot document: task.getResult()){
                        Food food = document.toObject(Food.class);
                        foodList.add(food);
                        foodListAdapter.notifyDataSetChanged();

                        Log.d("TAG", food.getItem_name());
                        Log.d("TAG", String.valueOf(food.getItem_price()));
                    }
                }
            }
        });

        firestore.collection("Drink").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    beveragesList.clear();
                    for(DocumentSnapshot document: task.getResult()){
                        Beverages beverages = document.toObject(Beverages.class);
                        beveragesList.add(beverages);
                        beveragesListAdapter.notifyDataSetChanged();

                        Log.d("TAG", beverages.getItem_name());
                        Log.d("TAG", String.valueOf(beverages.getItem_price()));
                    }
                }
            }
        });

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        getData();
    }
}
