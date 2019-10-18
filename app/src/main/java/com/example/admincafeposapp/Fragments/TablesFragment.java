package com.example.admincafeposapp.Fragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admincafeposapp.Adapters.TableRecyclerViewAdapter;
import com.example.admincafeposapp.Model.Tables;
import com.example.admincafeposapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TablesFragment extends Fragment {

    //RecyclerView and Adapter
    private RecyclerView table_recyclerView;
    private TableRecyclerViewAdapter table_Adapter;
    private ArrayList<Tables> tablesArrayList;

    //FireStore Database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference tableCollectionReference = db.collection("Tables");

    //Popup Add Table Info
    private EditText editText_tableNumber, editText_numberOfSeat, editText_tableStatus;
    private Button button_tableAddConfirm, button_addTable;

    //Popup Remove Table
    private EditText editText_removeTableNumber;
    private Button button_tableRemoveConfirm, button_removeTable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tables, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        button_addTable = view.findViewById(R.id.button_addTable);
        button_removeTable = view.findViewById(R.id.button_removeTable);

        tablesArrayList = new ArrayList<>();
        readTablesFromDatabase();

        table_recyclerView = view.findViewById(R.id.recyclerView_table);
        table_recyclerView.setHasFixedSize(true);
        table_recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        table_Adapter = new TableRecyclerViewAdapter(this.getContext(), tablesArrayList);
        table_recyclerView.setAdapter(table_Adapter);
        table_recyclerView.addItemDecoration(new DividerItemDecoration(table_recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        button_addTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupAddTable();
            }
        });

        button_removeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupRemoveTable();
            }
        });

    }

    private void readTablesFromDatabase()
    {
        tablesArrayList.clear();
        tableCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(DocumentSnapshot document: task.getResult())
                    {
                        Tables tables = document.toObject(Tables.class);
                        tablesArrayList.add(tables);
                    }
                    table_Adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void PopupAddTable()
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.add_table_popup, null);
        final PopupWindow popupWindow = new PopupWindow(view, 400, WindowManager.LayoutParams.WRAP_CONTENT);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        editText_tableNumber = view.findViewById(R.id.editText_tableNo);
        editText_numberOfSeat = view.findViewById(R.id.editText_tableNumberOfSeat);
        editText_tableStatus = view.findViewById(R.id.editText_tableStatus);
        button_tableAddConfirm = view.findViewById(R.id.button_confirmAddTable);

        button_tableAddConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validateAddFields())
                {
                    String tableNumber = editText_tableNumber.getText().toString();
                    int numberOfSeat = Integer.parseInt(editText_numberOfSeat.getText().toString());
                    String tableStatus = editText_tableStatus.getText().toString();

                    Tables tables = new Tables(tableNumber, numberOfSeat, tableStatus);
                    tableCollectionReference.document().set(tables);
                    readTablesFromDatabase();

                    Toast.makeText(getContext(),"New Tables is added Successfully!", Toast.LENGTH_LONG).show();
                    popupWindow.dismiss();
                }
                else
                {
                    Toast.makeText(getContext(),"Please complete the tables information!.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean validateAddFields() {
        return !(editText_tableNumber.getText().toString().isEmpty() || editText_numberOfSeat.getText().toString().isEmpty() || editText_tableStatus.getText().toString().isEmpty());
    }

    private void PopupRemoveTable(){

        View view = LayoutInflater.from(getContext()).inflate(R.layout.remove_table_popup, null);
        final PopupWindow popupWindow = new PopupWindow(view, 400, WindowManager.LayoutParams.WRAP_CONTENT);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        editText_removeTableNumber = view.findViewById(R.id.editText_tableNoRemove);
        button_tableRemoveConfirm = view.findViewById(R.id.button_confirmRemoveTable);

        button_tableRemoveConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText_removeTableNumber.getText().toString().isEmpty())
                {
                    final String tableNoRemove = editText_removeTableNumber.getText().toString();

                    tableCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                for(DocumentSnapshot document: task.getResult())
                                {
                                    Tables tables = document.toObject(Tables.class);
                                    if(tables.getTableNo().equals(tableNoRemove))
                                    {
                                        tableCollectionReference.document(document.getId()).delete();
                                        Toast.makeText(getContext(),"Tables Number: " + tableNoRemove + "is removed!", Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(getContext(),"Please enter valid Table Number", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                readTablesFromDatabase();
                            }
                        }
                    });


                    popupWindow.dismiss();
                }
                else
                {
                    Toast.makeText(getContext(),"Please enter Table Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
