package com.example.admincafeposapp.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admincafeposapp.Model.Beverages;
import com.example.admincafeposapp.Adapters.BeveragesListAdapter;
import com.example.admincafeposapp.Model.Food;
import com.example.admincafeposapp.Adapters.FoodListAdapter;
import com.example.admincafeposapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MenuFragment extends Fragment{

    private RecyclerView foodListView, beveragesListView;
    private FoodListAdapter foodListAdapter;
    private BeveragesListAdapter beveragesListAdapter;
    private ArrayList<Food> foodList;
    private ArrayList<Beverages> beveragesList;

    private FirebaseFirestore firestore;

    private EditText editAddFoodName, editAddFoodPrice, editAddBeveragesName, editAddBeveragesPrice, editDeleteFoodName, editDeleteBeveragesName;
    private Button addFoodBtn, deleteFoodBtn, addBeveragesBtn, deleteBeveragesBtn, addConfirmFoodBtn, addConfirmBeveragesBtn, deleteConfirmFoodBtn, deleteConfirmBeveragesBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firestore = FirebaseFirestore.getInstance();

        addFoodBtn = view.findViewById(R.id.faddButton);
        deleteFoodBtn = view.findViewById(R.id.fdeleteButton);
        addBeveragesBtn = view.findViewById(R.id.baddButton);
        deleteBeveragesBtn = view.findViewById(R.id.bdeleteButton);

        foodList = new ArrayList<>();
        beveragesList = new ArrayList<>();
        readFoodBeveragesFromDatabase();

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

        addFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupAddFood();
            }
        });

        addBeveragesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupAddBeverages();
            }
        });

        deleteBeveragesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupRemoveBeverages();
            }
        });

        deleteFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupRemoveFood();
            }
        });


    }

    private void readFoodBeveragesFromDatabase()
    {
        foodList.clear();
        firestore.collection("Food").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    foodList.clear();
                    for (DocumentSnapshot document : task.getResult()) {
                        Food food = document.toObject(Food.class);
                        foodList.add(food);
                    }
                    foodListAdapter.notifyDataSetChanged();
                }
            }
        });

        beveragesList.clear();
        firestore.collection("Drink").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    beveragesList.clear();
                    for(DocumentSnapshot document: task.getResult()){
                        Beverages beverages = document.toObject(Beverages.class);
                        beveragesList.add(beverages);
                    }
                    beveragesListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void PopupAddFood()
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.add_food_popup, null);
        final PopupWindow popupWindow = new PopupWindow(view, 400, WindowManager.LayoutParams.WRAP_CONTENT);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        editAddFoodName = view.findViewById(R.id.addFoodNameFill);
        editAddFoodPrice = view.findViewById(R.id.addFoodPriceFill);
        addConfirmFoodBtn = view.findViewById(R.id.button_confirmAddFood);

        addConfirmFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validateAddFoodFields())
                {
                    String addedFoodName = editAddFoodName.getText().toString();
                    Double addedFoodPrice = Double.parseDouble(editAddFoodPrice.getText().toString());

                    Food food = new Food(addedFoodName, addedFoodPrice);
                    firestore.collection("Food").document(addedFoodName).set(food);
                    readFoodBeveragesFromDatabase();

                    Toast.makeText(getContext(),addedFoodName + " is added", Toast.LENGTH_LONG).show();
                    popupWindow.dismiss();
                }
                else
                {
                    Toast.makeText(getContext(),"PLEASE DO NOT LEAVE ANY FIELD BLANK", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateAddFoodFields() {
        return !(editAddFoodName.getText().toString().isEmpty() || editAddFoodPrice.getText().toString().isEmpty());
    }

    private void PopupAddBeverages()
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.add_beverages_popup, null);
        final PopupWindow popupWindow = new PopupWindow(view, 400, WindowManager.LayoutParams.WRAP_CONTENT);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        editAddBeveragesName = view.findViewById(R.id.addBeveragesNameFill);
        editAddBeveragesPrice = view.findViewById(R.id.addBeveragesPriceFill);
        addConfirmBeveragesBtn = view.findViewById(R.id.button_confirmAddBeverages);

        addConfirmBeveragesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validateAddBeveragesFields())
                {
                    String addedBeveragesName = editAddBeveragesName.getText().toString();
                    Double addedBeveragesPrice = Double.parseDouble(editAddBeveragesPrice.getText().toString());

                    Beverages beverages = new Beverages(addedBeveragesName, addedBeveragesPrice);
                    firestore.collection("Drink").document(addedBeveragesName).set(beverages);
                    readFoodBeveragesFromDatabase();

                    Toast.makeText(getContext(),addedBeveragesName + " is added", Toast.LENGTH_LONG).show();
                    popupWindow.dismiss();
                }
                else
                {
                    Toast.makeText(getContext(),"PLEASE DO NOT LEAVE ANY FIELD BLANK", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateAddBeveragesFields() {
        return !(editAddBeveragesName.getText().toString().isEmpty() || editAddBeveragesPrice.getText().toString().isEmpty());
    }

    private void PopupRemoveFood(){

        View view = LayoutInflater.from(getContext()).inflate(R.layout.delete_food_popup, null);
        final PopupWindow popupWindow = new PopupWindow(view, 400, WindowManager.LayoutParams.WRAP_CONTENT);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        editDeleteFoodName = view.findViewById(R.id.deleteFoodNameFill);
        deleteFoodBtn = view.findViewById(R.id.button_confirmDeleteFood);

        deleteFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Remove Food")
                        .setMessage("Are you sure you want remove this food?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(!editDeleteFoodName.getText().toString().isEmpty())
                                {
                                    final String foodRemove = editDeleteFoodName.getText().toString();

                                    firestore.collection("Food").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful())
                                            {
                                                for(DocumentSnapshot document: task.getResult())
                                                {
                                                    Food food = document.toObject(Food.class);
                                                    if(food.getItem_name().equals(foodRemove))
                                                    {
                                                        firestore.collection("Food").document(document.getId()).delete();
                                                        Toast.makeText(getContext(),foodRemove + " deleted", Toast.LENGTH_LONG).show();
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(getContext(),"PLEASE ENTER A EXISTED FOOD NAME", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                readFoodBeveragesFromDatabase();
                                            }
                                        }
                                    });


                                    popupWindow.dismiss();
                                }
                                else
                                {
                                    Toast.makeText(getContext(),"PLEASE ENTER A FOOD NAME", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                builder.show();

            }
        });
    }

    private void PopupRemoveBeverages(){

        View view = LayoutInflater.from(getContext()).inflate(R.layout.delete_beverages_popup, null);
        final PopupWindow popupWindow = new PopupWindow(view, 400, WindowManager.LayoutParams.WRAP_CONTENT);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        editDeleteBeveragesName = view.findViewById(R.id.deleteBeveragesNameFill);
        deleteBeveragesBtn = view.findViewById(R.id.button_confirmDeleteBeverages);

        deleteBeveragesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Remove Beverages")
                        .setMessage("Are you sure you want remove this beverages?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(!editDeleteBeveragesName.getText().toString().isEmpty())
                                {


                                    final String beveragesRemove = editDeleteBeveragesName.getText().toString();

                                    firestore.collection("Drink").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful())
                                            {
                                                for(DocumentSnapshot document: task.getResult())
                                                {
                                                    Beverages beverages = document.toObject(Beverages.class);
                                                    if(beverages.getItem_name().equals(beveragesRemove))
                                                    {
                                                        firestore.collection("Drink").document(document.getId()).delete();
                                                        Toast.makeText(getContext(),beveragesRemove + " deleted", Toast.LENGTH_LONG).show();
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(getContext(),"PLEASE ENTER A EXISTED BEVERAGE NAME", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                readFoodBeveragesFromDatabase();
                                            }
                                        }
                                    });


                                    popupWindow.dismiss();
                                }
                                else
                                {
                                    Toast.makeText(getContext(),"PLEASE ENTER A BEVERAGE NAME", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                builder.show();


            }
        });
    }
}
