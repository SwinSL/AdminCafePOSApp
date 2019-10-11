package com.example.admincafeposapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admincafeposapp.Model.Reservation;
import com.example.admincafeposapp.R;

import java.util.List;

public class ReservationRecyclerViewAdapter extends RecyclerView.Adapter<ReservationRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Reservation> reservationList;

    public ReservationRecyclerViewAdapter(Context context, List<Reservation> reservationList) {
        this.context = context;
        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.reservation_row, parent, false);
        ViewHolder vHolder = new ViewHolder(view);
        vHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);

        holder.reservation_id.setText(reservation.getId());
        holder.reservation_surname.setText(reservation.getCustomer_surname());
        holder.reservation_date.setText(reservation.getDate());
        holder.reservation_time.setText(reservation.getTime());
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView reservation_id, reservation_surname, reservation_date, reservation_time;
        private View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            reservation_id = itemView.findViewById(R.id.textview_reservationId_data);
            reservation_surname = itemView.findViewById(R.id.textview_reservationSurname_data);
            reservation_date = itemView.findViewById(R.id.textview_reservationDate_data);
            reservation_time = itemView.findViewById(R.id.textview_reservationTime_data);
        }
    }
}
