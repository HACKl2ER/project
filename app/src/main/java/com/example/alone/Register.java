package com.example.alone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Register extends AppCompatActivity {
    private EditText emailId, password, userName,lastName,sexName;
    private Button btnSignUp;
    private TextView tvSignIn;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage firebaseStorage;
    String email,pwd,sex,name,ltName;
    private StorageReference storageReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Register");
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.etEmailUpdate);
        password = findViewById(R.id.etPassword);
        btnSignUp = findViewById(R.id.btnSave);
        tvSignIn = findViewById(R.id.textView1);
        userName = findViewById(R.id.etNameUpdate);
        lastName = findViewById(R.id.etLastNameUpdate);
        sexName = findViewById(R.id.etSexUpdate);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               email = emailId.getText().toString();
               pwd = password.getText().toString();
               sex = sexName.getText().toString();
               name = userName.getText().toString();
               ltName = lastName.getText().toString();
                if(email.isEmpty()) {
                    emailId.setError("Please enter Email id");
                    emailId.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else if(email.isEmpty() && pwd.isEmpty() && sex.isEmpty() && name.isEmpty() && ltName.isEmpty()) {
                    Toast.makeText(Register.this,"Fields Are Empty", Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && pwd.isEmpty() && sex.isEmpty() && name.isEmpty() && ltName.isEmpty())) {
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(Register.this,"SignUp unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                sendUserData();
                                startActivity(new Intent(Register.this,Second.class));
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(Register.this,"Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register.this,Login.class);
                startActivity(i);
            }
        });
    }
    private void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(mFirebaseAuth.getUid());
        UserProfile userProfile = new UserProfile(email, name,sex,ltName);
        myRef.setValue(userProfile);
    }
}
