package com.example.admincafeposapp.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admincafeposapp.Adapters.MemberAdapter;
import com.example.admincafeposapp.Model.Members;
import com.example.admincafeposapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MembersFragment extends Fragment{

    private  FirebaseDatabase memberdb;
    private  DatabaseReference databaseReference;
    private String myMember;

    private MemberAdapter.recyclerListener listener = new MemberAdapter.recyclerListener() {
        @Override
        public void recyclerOnClick(int position) {
            layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.member_add,null);
            constraintLayout = container.findViewById(R.id.constraint);
            popupWindow = new PopupWindow(container, 400, WindowManager.LayoutParams.WRAP_CONTENT, true);
            popupWindow.showAtLocation(constraintLayout,Gravity.CENTER,0,0);
            btn_confirm = container.findViewById(R.id.button_confirm);
            memID = container.findViewById(R.id.et_id);
            title = container.findViewById(R.id.tv_title);
            memID.setText(membersList.get(position).getID());
            memID.setTextColor(Color.BLACK);
            memID.setEnabled(false);

            title.setText("Update member");
            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    memName = container.findViewById(R.id.et_name);
                    final String ID = memID.getText().toString();
                    final String Name = memName.getText().toString();

                    if(TextUtils.isEmpty(ID))
                    {
                        Toast.makeText(getContext(),"ID is empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    else if(TextUtils.isEmpty(Name))
                    {
                        Toast.makeText(getContext(),"Name is empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    final Query query = databaseReference.orderByChild("id").equalTo(ID);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                            {
                                Members test = dataSnapshot1.getValue(Members.class);

                                if(ID.equals(test.getID()))
                                {
                                    test.setName(Name);
                                    dataSnapshot1.getRef().setValue(test);
                                    Toast.makeText(getContext(),"Successfully update member" ,Toast.LENGTH_LONG).show();
                                    popupWindow.dismiss();
                                }

                                else
                                {
                                    Toast.makeText(getContext(),"Wrong info",Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        }
    };

    private RecyclerView member_recycler;
    private ArrayList<Members> membersList;
    private MemberAdapter memberAdapter;
    private Button btn_register, btn_remove, btn_confirm;

    //alert
    private LayoutInflater layoutInflater;
    private EditText memID, memName;

    //popup
    private PopupWindow popupWindow;
    private ConstraintLayout constraintLayout;
    private TextView title;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_members, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        memberdb = FirebaseDatabase.getInstance();
        databaseReference = memberdb.getReference().child("Member");

        membersList = new ArrayList<>();
        member_recycler = view.findViewById(R.id.member_recycler);
        member_recycler.setHasFixedSize(true);
        member_recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));

        readFromDatabase();

        btn_register = view.findViewById(R.id.button_register);
        btn_register.setOnClickListener(buttonListener);
        btn_remove = view.findViewById(R.id.button_remove);
        btn_remove.setOnClickListener(buttonListener);

    }

    private void readFromDatabase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                membersList.clear();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    Members members = dataSnapshot1.getValue(Members.class);
                    membersList.add(members);
                }
                memberAdapter = new MemberAdapter(getContext(),membersList, listener);
                member_recycler.setAdapter(memberAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    Button.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.member_add,null);
            constraintLayout = container.findViewById(R.id.constraint);
            popupWindow = new PopupWindow(container, 400, WindowManager.LayoutParams.WRAP_CONTENT, true);
            popupWindow.showAtLocation(constraintLayout,Gravity.CENTER,0,0);
            btn_confirm = container.findViewById(R.id.button_confirm);
            title = container.findViewById(R.id.tv_title);
            switch (view.getId())
            {
                case R.id.button_register:
                    btn_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            memID = container.findViewById(R.id.et_id);
                            memName = container.findViewById(R.id.et_name);

                            final String key = databaseReference.push().getKey();
                            final String ID = memID.getText().toString();
                            final String Name = memName.getText().toString();

                            if(TextUtils.isEmpty(ID))
                            {
                                Toast.makeText(getContext(),"Please enter an ID", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            else if(TextUtils.isEmpty((Name)))
                            {
                                Toast.makeText(getContext(),"Please enter name", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            else
                            {
                                final Query query = databaseReference.orderByChild("id").equalTo(ID);

                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                                        {
                                            Members members = dataSnapshot1.getValue(Members.class);

                                            myMember = members.getID();

                                        }

                                        if(ID.equals(myMember))
                                        {
                                            Toast.makeText(getContext(),"Member ID already existed", Toast.LENGTH_SHORT).show();
                                            popupWindow.dismiss();
                                        }

                                        else
                                        {
                                            Members member = new Members(ID, Name);

                                            databaseReference.child(key).setValue(member);

                                            popupWindow.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }
                        }
                    });
                    break;

                case R.id.button_remove:
                    title.setText("Remove member");
                    btn_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                            builder.setTitle("Remove Member")
                                    .setMessage("Are you sure you want remove this member?")
                                    .setNegativeButton("No", null)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            memID = container.findViewById(R.id.et_id);
                                            memName = container.findViewById(R.id.et_name);

                                            final String ID = memID.getText().toString();
                                            final String Name = memName.getText().toString();

                                            if(TextUtils.isEmpty(ID))
                                            {
                                                Toast.makeText(getContext(),"ID is empty", Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            else if(TextUtils.isEmpty(Name))
                                            {
                                                Toast.makeText(getContext(),"Name is empty", Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            final Query query = databaseReference.orderByChild("id").equalTo(ID);

                                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                                                    {
                                                        Members test = dataSnapshot1.getValue(Members.class);

                                                        if(Name.equals(test.getName()))
                                                        {
                                                            dataSnapshot1.getRef().removeValue();
                                                            Toast.makeText(getContext(),"Successfully delete member",Toast.LENGTH_LONG).show();
                                                            popupWindow.dismiss();
                                                        }

                                                        else
                                                        {
                                                            Toast.makeText(getContext(),"Wrong info",Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                }
                                            });

                                        }
                                    });
                            builder.show();


                        }
                    });
                    break;
            }


        }
    };

}
