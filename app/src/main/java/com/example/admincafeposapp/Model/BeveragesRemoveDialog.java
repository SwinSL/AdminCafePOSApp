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

public class BeveragesRemoveDialog extends AppCompatDialogFragment {

    private EditText editTextBeveragesName;
    private BeveragesRemoveDialogListener beveragesRemoveDialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder beveragesRemoveDialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater beveragesRemoveDialogInflater = getActivity().getLayoutInflater();

        View viewRemove = beveragesRemoveDialogInflater.inflate((R.layout.delete_beverages_dialog), null);

        beveragesRemoveDialogBuilder.setView(viewRemove).setTitle("Delete Beverages").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String beveragesName = editTextBeveragesName.getText().toString();
                beveragesRemoveDialogListener.deleteBeveragesText(beveragesName);
            }
        });

        editTextBeveragesName = viewRemove.findViewById(R.id.bRNameEditText);

        return beveragesRemoveDialogBuilder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            beveragesRemoveDialogListener = (BeveragesRemoveDialog.BeveragesRemoveDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement BeveragesRemoveDialogListener");
        }
    }

    public interface BeveragesRemoveDialogListener
    {
        void deleteBeveragesText(String name);
    }


}
