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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText editText_email, editText_pass;
    private Button btn_login;
    private TextView error;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        error = findViewById(R.id.textView_error);
        editText_email = findViewById(R.id.editText_username);
        editText_pass = findViewById(R.id.editText_pass);
        btn_login = findViewById(R.id.button_login);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = editText_email.getText().toString();
                String pass = editText_pass.getText().toString();

                if(TextUtils.isEmpty(user))
                {
                    Toast.makeText(getApplicationContext(),"Please enter your email",Toast.LENGTH_SHORT).show();
                    return;
                }

                else if(TextUtils.isEmpty(pass))
                {
                    Toast.makeText(getApplicationContext(),"Please enter your password",Toast.LENGTH_SHORT).show();
                    return;
                }

                else
                {
                    progressDialog.setMessage("Logging in");
                    progressDialog.show();

                    firebaseAuth.signInWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                error.setText(null);
                                Toast.makeText(getApplicationContext(),"Login success",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                progressDialog.dismiss();
                            }

                            else
                            {
                                error.setText("No user existed");
                                Toast.makeText(getApplicationContext(),"Login failed",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
                }
            }
        });
    }
}
