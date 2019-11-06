package com.example.admincafeposapp.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admincafeposapp.Adapters.ReservationRecyclerViewAdapter;
import com.example.admincafeposapp.Model.Reservation;
import com.example.admincafeposapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class ReservationFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<Reservation> reservationArrayList;
    private ReservationRecyclerViewAdapter reservationRecyclerViewAdapter;
    private Button add_button, remove_button, confirm_addReservationBtn, confirm_removeReservationBtn;
    private EditText addReservation_surname_editText, addReservation_noOfPeople_editText, addReservation_date_editText;
    private Spinner addReservation_time_spinner, addReservation_tableNo_spinner, removeReservation_surname_spinner, removeReservation_date_spinner, removeReservation_time_spinner, removeReservation_tableNo_spinner;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Reservations");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reservation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        add_button = view.findViewById(R.id.createReservationBtn);
        remove_button = view.findViewById(R.id.removeReservationBtn);

        reservationArrayList = new ArrayList<>();
        getReservations();

        recyclerView = view.findViewById(R.id.reservations_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        reservationRecyclerViewAdapter = new ReservationRecyclerViewAdapter(this.getContext(), reservationArrayList);
        recyclerView.setAdapter(reservationRecyclerViewAdapter);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddReservationPopup();
            }
        });
        remove_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRemoveReservationPopup();
            }
        });
    }

    private void showRemoveReservationPopup() {
        @SuppressLint("InflateParams") final View popupView = LayoutInflater.from(getContext()).inflate(R.layout.remove_reservation_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, 400, WindowManager.LayoutParams.WRAP_CONTENT);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        removeReservation_surname_spinner = popupView.findViewById(R.id.remove_reservation_surname_spinner);
        removeReservation_date_spinner = popupView.findViewById(R.id.remove_reservation_date_spinner);
        removeReservation_time_spinner = popupView.findViewById(R.id.remove_reservation_time_spinner);
        removeReservation_tableNo_spinner = popupView.findViewById(R.id.remove_reservation_table_spinner);
        confirm_removeReservationBtn = popupView.findViewById(R.id.confirm_removeReservationBtn);

        ArrayList<String> surname_options = new ArrayList<>();

        for(Reservation reservation: reservationArrayList){
            surname_options.add(reservation.getCustomer_surname());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.reservation_spinner_item, surname_options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        removeReservation_surname_spinner.setAdapter(adapter);

        removeReservation_surname_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getRemoveReservationDateOptions(removeReservation_surname_spinner.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        confirm_removeReservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateRemoveFields()){
                    final String surname = removeReservation_surname_spinner.getSelectedItem().toString();
                    final String tableNo = removeReservation_tableNo_spinner.getSelectedItem().toString();
                    final String date = removeReservation_date_spinner.getSelectedItem().toString();
                    final String time = removeReservation_time_spinner.getSelectedItem().toString();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    builder.setTitle("Remove Reservation")
                            .setMessage("Are you sure you want remove this reservation?")
                            .setNegativeButton("No", null)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        for(DocumentSnapshot document: Objects.requireNonNull(task.getResult())){
                                                            Reservation reservation = document.toObject(Reservation.class);
                                                            assert reservation != null;
                                                            if(reservation.getCustomer_surname().equals(surname) && reservation.getTable_no().equals(tableNo) && reservation.getDate().equals(date) && reservation.getTime().equals(time)){
                                                                collectionReference.document(document.getId()).delete();
                                                            }
                                                        }
                                                        getReservations();
                                                    }
                                                }
                                            });

                                            Toast.makeText(getContext(),"Reservation removed successfully!", Toast.LENGTH_LONG).show();
                                            popupWindow.dismiss();
                                        }
                                    });
                            builder.show();

                }else{
                    Toast.makeText(getContext(),"Please complete the fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateRemoveFields() {
        return (!removeReservation_surname_spinner.getSelectedItem().toString().isEmpty() || removeReservation_date_spinner.getSelectedItem().toString().isEmpty() || removeReservation_time_spinner.getSelectedItem().toString().isEmpty() || removeReservation_tableNo_spinner.getSelectedItem().toString().isEmpty());
    }

    private void getRemoveReservationDateOptions(final String surname){
        ArrayList<String> date_options = new ArrayList<>();

        for(Reservation reservation: reservationArrayList){
            if(reservation.getCustomer_surname().equals(surname))
                date_options.add(reservation.getDate());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.reservation_spinner_item, date_options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        removeReservation_date_spinner.setAdapter(adapter);

        removeReservation_date_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getRemoveReservationTimeOptions(surname, removeReservation_date_spinner.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void getRemoveReservationTimeOptions(final String surname, final String date) {
        ArrayList<String> time_options = new ArrayList<>();

        for(Reservation reservation: reservationArrayList){
            if(reservation.getCustomer_surname().equals(surname) && reservation.getDate().equals(date))
                time_options.add(reservation.getTime());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.reservation_spinner_item, time_options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        removeReservation_time_spinner.setAdapter(adapter);

        removeReservation_time_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getRemoveReservationTableOptions(surname, date, removeReservation_time_spinner.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void getRemoveReservationTableOptions(String surname, String date, String time) {
        ArrayList<String> table_options = new ArrayList<>();

        for(Reservation reservation: reservationArrayList){
            if(reservation.getCustomer_surname().equals(surname) && reservation.getDate().equals(date) && reservation.getTime().equals(time))
                table_options.add(reservation.getTable_no());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.reservation_spinner_item, table_options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        removeReservation_tableNo_spinner.setAdapter(adapter);
    }

    private void showAddReservationPopup() {
        @SuppressLint("InflateParams") final View popupView = LayoutInflater.from(getContext()).inflate(R.layout.add_reservation_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, 400, WindowManager.LayoutParams.WRAP_CONTENT);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        addReservation_surname_editText = popupView.findViewById(R.id.add_reservation_surname_edittext);
        addReservation_noOfPeople_editText = popupView.findViewById(R.id.add_reservation_noOfPeople_edittext);
        addReservation_date_editText = popupView.findViewById(R.id.add_reservation_date_editText);
        addReservation_time_spinner = popupView.findViewById(R.id.add_reservation_time_spinner);
        addReservation_tableNo_spinner = popupView.findViewById(R.id.add_reservation_table_spinner);
        confirm_addReservationBtn = popupView.findViewById(R.id.confirm_addReservationBtn);

        addReservation_date_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDatePicker();
                if(!addReservation_date_editText.getText().toString().isEmpty()){
                    getAddReservationTimeOptions(addReservation_date_editText.getText().toString());
                }
            }
        });

        addReservation_time_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getTables(addReservation_date_editText.getText().toString(), addReservation_time_spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        confirm_addReservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateAddFields()){
                    String id = addReservation_surname_editText.getText().toString().concat(addReservation_date_editText.getText().toString()).concat(addReservation_time_spinner.getSelectedItem().toString()).concat(addReservation_tableNo_spinner.getSelectedItem().toString()).replaceAll("\\W", "").toLowerCase();
                    String surname = addReservation_surname_editText.getText().toString();
                    String tableNo = addReservation_tableNo_spinner.getSelectedItem().toString();
                    String date = addReservation_date_editText.getText().toString();
                    String time = addReservation_time_spinner.getSelectedItem().toString();
                    int noOfPeople = Integer.parseInt(addReservation_noOfPeople_editText.getText().toString());

                    Reservation reservation = new Reservation(id, surname, tableNo, date, time, noOfPeople);
                    collectionReference.document().set(reservation);
                    getReservations();
                    Toast.makeText(getContext(),"Reservation Successful!", Toast.LENGTH_LONG).show();
                    popupWindow.dismiss();
                }else{
                    Toast.makeText(getContext(),"Please complete the fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void displayDatePicker(){
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "d MMM Y";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                addReservation_date_editText.setText(sdf.format(myCalendar.getTime()));
                getAddReservationTimeOptions(addReservation_date_editText.getText().toString());
            }

        };

        DatePickerDialog datepickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        datepickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
        datepickerDialog.getDatePicker().setMaxDate((myCalendar.getTimeInMillis() + 1000L*60*60*24*30));
        datepickerDialog.show();
    }

    private boolean validateAddFields() {
        return !(addReservation_surname_editText.getText().toString().isEmpty() || addReservation_noOfPeople_editText.getText().toString().isEmpty() || addReservation_date_editText.getText().toString().isEmpty() || addReservation_time_spinner.getSelectedItem().toString().isEmpty() || addReservation_tableNo_spinner.getSelectedItem().toString().isEmpty());
    }

    private void getReservations(){
        reservationArrayList.clear();

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document: Objects.requireNonNull(task.getResult())){
                        Reservation reservation = document.toObject(Reservation.class);
                        reservationArrayList.add(reservation);
                    }
                    reservationRecyclerViewAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    private void getAddReservationTimeOptions(String date_selected) {
        ArrayList<String> time_options = new ArrayList<>();
        time_options.add("5:00PM");
        time_options.add("5:30PM");
        time_options.add("6:00PM");
        time_options.add("6:30PM");
        time_options.add("7:00PM");
        time_options.add("7:30PM");
        time_options.add("8:00PM");
        time_options.add("8:30PM");
        time_options.add("9:00PM");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.reservation_spinner_item, time_options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addReservation_time_spinner.setAdapter(adapter);
    }

    private void getTables(String date_selected, String time_selected) {
        ArrayList<String> table_options = new ArrayList<>();
        table_options.add("1A");
        table_options.add("2A");
        table_options.add("3A");
        table_options.add("1B");
        table_options.add("2B");
        table_options.add("3B");

        for(Reservation reservation: reservationArrayList){
            if(reservation.getDate().equals(date_selected) && reservation.getTime().equals(time_selected)){
                table_options.remove(reservation.getTable_no());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.reservation_spinner_item, table_options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addReservation_tableNo_spinner.setAdapter(adapter);
    }

/*    private void setReservation(){
        Reservation reservation = new Reservation("Reservation01", "Tan", "5", "24 October 2019", "12:00 AM", 1000);

        collectionReference.document().set(reservation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "DocumentSnapshot successfully written!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("TAG", "Error writing document", e);
            }
        });
    }*/
}
