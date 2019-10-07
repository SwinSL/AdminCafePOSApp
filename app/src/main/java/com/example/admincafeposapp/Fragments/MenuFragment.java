package com.example.admincafeposapp.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admincafeposapp.Model.Food;
import com.example.admincafeposapp.Model.FoodListAdapter;
import com.example.admincafeposapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {

    private  static final String TAG = "FireLog";
    RecyclerView foodListView, beveragesListView;
    private FirebaseFirestore firestore;
    private List<Food> foodList;
    private FoodListAdapter foodListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        foodList = new ArrayList<>();

        foodListView = view.findViewById(R.id.foodList);
        foodListView.setHasFixedSize(true);
        foodListView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        foodListAdapter = new FoodListAdapter(this.getContext(), foodList);
        foodListView.setAdapter(foodListAdapter);

        beveragesListView = view.findViewById(R.id.beverageList);

        firestore = FirebaseFirestore.getInstance();

        firestore.collection("Food").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document: task.getResult()){
                        Food food = document.toObject(Food.class);
                        foodList.add(food);
                        foodListAdapter.notifyDataSetChanged();
                        Log.d("TAG", food.getItem_name());
                    }
                }
            }
        });
    }
}
