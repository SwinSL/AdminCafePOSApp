package com.example.admincafeposapp.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Objects;

public class TablesFragment extends Fragment {

    //RecyclerView and Adapter
    private RecyclerView table_recyclerView;
    private TableRecyclerViewAdapter table_Adapter;
    private ArrayList<Tables> tablesArrayList;
    private ArrayList<String> myTable = new ArrayList<>();

    //FireStore Database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference tableCollectionReference = db.collection("Tables");

    //Popup Add Table Info
    private EditText editText_tableNumber, editText_numberOfSeat;
    private Button button_tableAddConfirm, button_addTable;
    private Spinner spinner_addTableStautus;
    private String addTableStatus;

    //Popup Remove Table
    private Spinner spinner_TableNumber;
    private Button button_tableRemoveConfirm, button_removeTable;
    private String remove_tableNumber;

    //Popup Update Table
    private Spinner spinner_tableStatus_update;
    private TextView textView_tableNo_update;
    private EditText editText_tableNoOfSeat_update;
    private Button button_updateTableConfirm;
    private String tableNumberUpdate;
    private String tableStatusUpdate;
    private int updateSeat;

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

        table_Adapter.setOnItemClickListener(new TableRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.update_table_popup, null);
                final PopupWindow popupWindow = new PopupWindow(view, 400, WindowManager.LayoutParams.WRAP_CONTENT);

                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                textView_tableNo_update = view.findViewById(R.id.textView_updateTableNumber);
                editText_tableNoOfSeat_update = view.findViewById(R.id.editText_tableNumberOfSeatUpdate);
                spinner_tableStatus_update = view.findViewById(R.id.spinner_updateTableStatus);
                button_updateTableConfirm = view.findViewById(R.id.button_confirmUpdateTable);

                textView_tableNo_update.setText(tablesArrayList.get(position).getTableNo());
                tableNumberUpdate = tablesArrayList.get(position).getTableNo();

                ArrayAdapter<CharSequence>  adapter = ArrayAdapter.createFromResource(getContext(), R.array.table_status, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_tableStatus_update.setAdapter(adapter);

                spinner_tableStatus_update.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                        tableStatusUpdate = adapterView.getItemAtPosition(pos).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        //Do nothing
                    }
                });

                button_updateTableConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(editText_tableNoOfSeat_update.getText().toString().isEmpty())
                        {
                            Toast.makeText(getContext(),"Please Enter Seat Number!", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            updateSeat = Integer.parseInt(editText_tableNoOfSeat_update.getText().toString());
                            tableCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful())
                                    {
                                        for(DocumentSnapshot document : task.getResult())
                                        {
                                            Tables tables = document.toObject(Tables.class);
                                            if(tableNumberUpdate.equals(tables.getTableNo()))
                                            {
                                                DocumentReference table_details = tableCollectionReference.document(tableNumberUpdate);
                                                table_details.update("tableNoOfSeat", updateSeat);
                                                table_details.update("tableStatus", tableStatusUpdate)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(getContext(),"Update Successfully", Toast.LENGTH_LONG).show();

                                                            }
                                                        });
                                            }
                                        }
                                        readTablesFromDatabase();
                                    }
                                }
                            });
                        }
                        popupWindow.dismiss();
                    }
                });



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
        spinner_addTableStautus = view.findViewById(R.id.spinner_popupAddTableStatus);
        button_tableAddConfirm = view.findViewById(R.id.button_confirmAddTable);

        ArrayAdapter<CharSequence>  adapter = ArrayAdapter.createFromResource(getContext(), R.array.table_status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_addTableStautus.setAdapter(adapter);

        spinner_addTableStautus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                addTableStatus = adapterView.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        button_tableAddConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validateAddFields())
                {
                    final String tableNumber = editText_tableNumber.getText().toString();
                    final int numberOfSeat = Integer.parseInt(editText_numberOfSeat.getText().toString());

                    tableCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                for(DocumentSnapshot document : task.getResult())
                                {
                                    Tables tables = document.toObject(Tables.class);
                                    myTable.add(tableCollectionReference.document(tables.getTableNo()).getId());
                                    System.out.println(myTable);
                                }

                                for(int i = 0; i < myTable.size(); i++){
                                    if(myTable.contains(tableNumber))
                                    {
                                        Toast.makeText(getContext(),"This table already Exist", Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        Tables tables1 = new Tables(tableNumber, numberOfSeat, addTableStatus);
                                        tableCollectionReference.document(tableNumber).set(tables1);

                                        Toast.makeText(getContext(),"New Tables is added Successfully!", Toast.LENGTH_LONG).show();
                                        popupWindow.dismiss();
                                    }
                                }

                                readTablesFromDatabase();
                            }
                        }
                    });

                }
                else
                {
                    Toast.makeText(getContext(),"Please complete the tables information!.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean validateAddFields() {
        return !(editText_tableNumber.getText().toString().isEmpty() || editText_numberOfSeat.getText().toString().isEmpty());
    }

    private void PopupRemoveTable(){

        View view = LayoutInflater.from(getContext()).inflate(R.layout.remove_table_popup, null);
        final PopupWindow popupWindow = new PopupWindow(view, 400, 200);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        spinner_TableNumber = view.findViewById(R.id.spinner_removeTable);
        button_tableRemoveConfirm = view.findViewById(R.id.button_confirmRemoveTable);
        ArrayList<String> tableNumber = new ArrayList<>();

        for(Tables tables: tablesArrayList)
        {
            tableNumber.add(tables.getTableNo());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, tableNumber);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_TableNumber.setAdapter(spinnerAdapter);

        spinner_TableNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                remove_tableNumber = adapterView.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        button_tableRemoveConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Remove Table")
                        .setMessage("Are you sure you want remove this table?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                tableCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful())
                                            {
                                                for(DocumentSnapshot document: task.getResult())
                                                {
                                                    Tables tables = document.toObject(Tables.class);
                                                    if(tables.getTableNo().equals(remove_tableNumber))
                                                    {
                                                        tableCollectionReference.document(document.getId()).delete();
                                                        Toast.makeText(getContext(),"Tables Number: " + remove_tableNumber + " is removed!", Toast.LENGTH_LONG).show();
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
                        });

                builder.show();
            }
        });
    }



}
