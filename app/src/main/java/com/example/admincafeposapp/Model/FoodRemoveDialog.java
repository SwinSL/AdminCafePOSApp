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

public class FoodRemoveDialog extends AppCompatDialogFragment {

    private EditText editTextFoodName;
    private FoodRemoveDialogListener foodRemoveDialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder foodRemoveDialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater foodRemoveDialogInflater = getActivity().getLayoutInflater();

        View viewRemove = foodRemoveDialogInflater.inflate((R.layout.delete_food_dialog), null);

        foodRemoveDialogBuilder.setView(viewRemove).setTitle("Delete Food").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String foodName = editTextFoodName.getText().toString();
                foodRemoveDialogListener.deleteFoodText(foodName);
            }
        });

        editTextFoodName = viewRemove.findViewById(R.id.fRNameEditText);

        return foodRemoveDialogBuilder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            foodRemoveDialogListener = (FoodRemoveDialog.FoodRemoveDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement FoodRemoveDialogListener");
        }
    }

    public interface FoodRemoveDialogListener
    {
        void deleteFoodText(String name);
    }


}
