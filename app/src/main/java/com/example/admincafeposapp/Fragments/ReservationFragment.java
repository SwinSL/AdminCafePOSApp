package com.example.admincafeposapp.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ReservationFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<Reservation> reservationArrayList;
    private ReservationRecyclerViewAdapter reservationRecyclerViewAdapter;
    private Button add_button, remove_button, confirm_button;
    private EditText surname_editText, noOfPeople_editText, date_editText;
    private Spinner time_spinner, tableNo_spinner;

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
    }

    private void showAddReservationPopup() {
        @SuppressLint("InflateParams") final View popupView = LayoutInflater.from(getContext()).inflate(R.layout.add_reservation_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, 400, WindowManager.LayoutParams.WRAP_CONTENT);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        surname_editText = popupView.findViewById(R.id.add_reservation_surname_edittext);
        noOfPeople_editText = popupView.findViewById(R.id.add_reservation_noOfPeople_edittext);
        date_editText = popupView.findViewById(R.id.add_reservation_date_editText);
        time_spinner = popupView.findViewById(R.id.add_reservation_time_spinner);
        tableNo_spinner = popupView.findViewById(R.id.add_reservation_table_spinner);
        confirm_button = popupView.findViewById(R.id.confirm_reservationBtn);

        date_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!date_editText.getText().toString().isEmpty()){
                    getTimeOptions(date_editText.getText().toString());
                }
            }
        });

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateFields()){
                    String id = surname_editText.getText().toString().concat(date_editText.getText().toString()).concat(time_spinner.getSelectedItem().toString()).replaceAll("\\W", "");
                    String surname = surname_editText.getText().toString();
                    String tableNo = tableNo_spinner.getSelectedItem().toString();
                    String date = date_editText.getText().toString();
                    String time = time_spinner.getSelectedItem().toString();
                    int noOfPeople = Integer.parseInt(noOfPeople_editText.getText().toString());

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

    private boolean validateFields() {
        return !(surname_editText.getText().toString().isEmpty() || noOfPeople_editText.getText().toString().isEmpty() || date_editText.getText().toString().isEmpty() || time_spinner.getSelectedItem().toString().isEmpty() || tableNo_spinner.getSelectedItem().toString().isEmpty());
    }

    private void getReservations(){
        reservationArrayList.clear();

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document: task.getResult()){
                        Reservation reservation = document.toObject(Reservation.class);
                        reservationArrayList.add(reservation);
                    }
                    reservationRecyclerViewAdapter.notifyDataSetChanged();
                }
            }
        });
    }



    private void getTimeOptions(String date_selected) {
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

        for(Reservation reservation: reservationArrayList){
            if(reservation.getDate().equals(date_selected)){
                time_options.remove(reservation.getTime());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.reservation_spinner_item, time_options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time_spinner.setAdapter(adapter);

        getTables();
    }

    private void getTables() {
        ArrayList<String> table_options = new ArrayList<>();
        table_options.add("1A");
        table_options.add("2A");
        table_options.add("3A");
        table_options.add("1B");
        table_options.add("2B");
        table_options.add("3B");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.reservation_spinner_item, table_options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tableNo_spinner.setAdapter(adapter);
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
