package com.example.admincafeposapp.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class MembersFragment extends Fragment {

    private  FirebaseDatabase memberdb;
    private  DatabaseReference databaseReference;

    private RecyclerView member_recycler;
    private ArrayList<Members> membersList;
    private MemberAdapter memberAdapter;
    private Button btn_register, btn_remove;

    //popup
    private LayoutInflater layoutInflater;
    private EditText memID, memName;

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
                memberAdapter = new MemberAdapter(getContext(),membersList);
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

            AlertDialog.Builder mybuilder = new AlertDialog.Builder(getContext());

            layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.member_add,null);

            switch (view.getId())
            {
                case R.id.button_remove:
                    mybuilder.setView(container)
                            .setTitle("Remove member")
                            .setNegativeButton("Cancel", null)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    memID = container.findViewById(R.id.editText_member_ID);
                                    memName = container.findViewById(R.id.editText_member_name);

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
                                                //dataSnapshot1.getRef().removeValue();
                                                //Toast.makeText(getContext(),"Value is : " + test.getName(),Toast.LENGTH_LONG).show();

                                                if(Name.equals(test.getName()))
                                                {
                                                    dataSnapshot1.getRef().removeValue();
                                                    Toast.makeText(getContext(),"Successfully delete member",Toast.LENGTH_LONG).show();
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

                    AlertDialog alertDialog = mybuilder.create();
                    alertDialog.show();
                    break;

                case R.id.button_register:
                    mybuilder.setView(container)
                            .setTitle("Add member")
                            .setNegativeButton("Cancel",null)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    memID = container.findViewById(R.id.editText_member_ID);
                                    memName = container.findViewById(R.id.editText_member_name);

                                    String key = databaseReference.push().getKey();
                                    String ID = memID.getText().toString();
                                    String Name = memName.getText().toString();

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
                                        Members member = new Members(ID, Name);

                                        databaseReference.child(key).setValue(member);

                                        Toast.makeText(getContext(),"Successfully register member", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    AlertDialog alertDialog1 = mybuilder.create();
                    alertDialog1.show();

                /*layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.member_add,null);

                popupWindow = new PopupWindow(container,400,300,true);
                popupWindow.showAtLocation(constraintLayout, Gravity.CENTER, 0 , 0);
                popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                memID = container.findViewById(R.id.editText_member_ID);
                memName = container.findViewById(R.id.editText_member_name);
                cancel = container.findViewById(R.id.button_cancel);
                ok = container.findViewById(R.id.button_confirm);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                        return;
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String key = databaseReference.push().getKey();
                        String ID = memID.getText().toString();
                        String Name = memName.getText().toString();

                        Members member = new Members(ID, Name);

                        databaseReference.child(key).setValue(member);

                        popupWindow.dismiss();
                        return;
                    }
                });*/

                /*container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                        return;
                    }
                });*/
                    break;
            }
        }
    };
}
