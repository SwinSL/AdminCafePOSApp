package com.example.admincafeposapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admincafeposapp.Model.Members;
import com.example.admincafeposapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private Context ctx;
    private ArrayList<Members> members;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Member");

    public MemberAdapter(Context ctx, ArrayList<Members> members) {
        this.ctx= ctx;
        this.members = members;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.ctx).inflate(R.layout.member_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.member_id.setText(members.get(position).getID());
        holder.member_name.setText(members.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView member_id, member_name;
        Button btn_register;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            member_id = itemView.findViewById(R.id.textView_memberIdData);
            member_name = itemView.findViewById(R.id.textView_memberNameData);
            btn_register = itemView.findViewById(R.id.button_register);
        }
    }
}
