package com.example.admincafeposapp.Model;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.admincafeposapp.R;

public class FoodDialog extends AppCompatDialogFragment {

    private EditText editTextFoodName;
    private EditText editTextFoodPrice;
    private FoodDialogListener foodDialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder foodDialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater foodDialogInflater = getActivity().getLayoutInflater();

        View view = foodDialogInflater.inflate(R.layout.add_food_dialog, null);

        foodDialogBuilder.setView(view).setTitle("Add Food").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String foodName = editTextFoodName.getText().toString();
                String foodPrice = editTextFoodPrice.getText().toString();
                foodDialogListener.addFoodText(foodName, foodPrice);
            }
        });

        editTextFoodName = view.findViewById(R.id.fNameEditText);
        editTextFoodPrice = view.findViewById(R.id.fPriceEditText);

        return foodDialogBuilder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            foodDialogListener = (FoodDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement FoodDialogListener");
        }
    }

    public interface FoodDialogListener
    {
        void addFoodText(String name, String price);
    }
}
