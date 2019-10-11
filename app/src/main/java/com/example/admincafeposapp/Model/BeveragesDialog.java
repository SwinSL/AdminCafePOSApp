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

public class BeveragesDialog extends AppCompatDialogFragment {

    private EditText editTextBeveragesName, editTextBeveragesPrice;
    private BeveragesDialogListener beveragesDialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder beveragesDialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater beveragesDialogInflater = getActivity().getLayoutInflater();

        View view = beveragesDialogInflater.inflate(R.layout.add_beverages_dialog, null);

        beveragesDialogBuilder.setView(view).setTitle("Add Beverages").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String beveragesName = editTextBeveragesName.getText().toString();
                String beveragesPrice = editTextBeveragesPrice.getText().toString();
                beveragesDialogListener.addBeveragesText(beveragesName, beveragesPrice);
            }
        });

        editTextBeveragesName = view.findViewById(R.id.bNameEditText);
        editTextBeveragesPrice = view.findViewById(R.id.bPriceEditText);

        return beveragesDialogBuilder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            beveragesDialogListener = (BeveragesDialog.BeveragesDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement BeveragesDialogListener");
        }
    }

    public interface BeveragesDialogListener
    {
        void addBeveragesText(String name, String price);
    }


}
