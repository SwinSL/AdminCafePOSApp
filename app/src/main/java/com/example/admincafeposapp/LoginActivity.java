package com.example.admincafeposapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admincafeposapp.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private TextView textView_error;
    private EditText editText_username, editText_pass;
    private Button btn_login;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textView_error = findViewById(R.id.textView_error);
        editText_username = findViewById(R.id.editText_username);
        editText_pass = findViewById(R.id.editText_pass);
        btn_login = findViewById(R.id.button_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = editText_username.getText().toString();
                final String pass = editText_pass.getText().toString();

                mReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Admin");
                ValueEventListener listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        if(TextUtils.isEmpty(username)){
                            textView_error.setText("Username is empty");
                            Toast.makeText(getApplicationContext(),"Please enter username",Toast.LENGTH_SHORT).show();

                        }

                        else if(TextUtils.isEmpty(pass)){
                            textView_error.setText("Password is empty");
                            Toast.makeText(getApplicationContext(),"Please enter password",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if(user.getUsername().equals(username))
                            {
                                if(user.getPassword().equals(pass))
                                {
                                    textView_error.setText(null);
                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                    Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_SHORT).show();
                                }

                                else
                                {
                                    textView_error.setText("Wrong password");
                                    Toast.makeText(getApplicationContext(),"Wrong password",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                textView_error.setText("User do not exist");
                                Toast.makeText(getApplicationContext(),"User do not exist",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                mReference.addValueEventListener(listener);
            }

        });
    }
}
