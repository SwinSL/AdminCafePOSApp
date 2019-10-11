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

import java.util.ArrayList;

public class ReservationFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<Reservation> reservationArrayList;
    private ReservationRecyclerViewAdapter reservationRecyclerViewAdapter;

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
        reservationArrayList = new ArrayList<>();
        //setReservation();
        getReservations();

        recyclerView = view.findViewById(R.id.reservations_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        reservationRecyclerViewAdapter = new ReservationRecyclerViewAdapter(this.getContext(), reservationArrayList);
        recyclerView.setAdapter(reservationRecyclerViewAdapter);
    }

    private void getReservations(){
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document: task.getResult()){
                        Reservation reservation = document.toObject(Reservation.class);
                        reservationArrayList.add(reservation);
                        reservationRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
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
