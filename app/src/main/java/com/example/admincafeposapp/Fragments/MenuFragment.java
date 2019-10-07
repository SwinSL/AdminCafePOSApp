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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

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
        foodListView.setAdapter(foodListAdapter);
        foodListAdapter = new FoodListAdapter(foodList);

        beveragesListView = view.findViewById(R.id.beverageList);

        firestore = FirebaseFirestore.getInstance();

        firestore.collection("Food").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot documentSnapshots, FirebaseFirestoreException
                    e) {
                if (e != null) {
                    Log.d(TAG, "Error : " + e.getMessage());
                }

                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        Food food = doc.getDocument().toObject(Food.class);
                        foodList.add(food);
                        foodListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
