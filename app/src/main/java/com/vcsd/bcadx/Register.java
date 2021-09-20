package com.vcsd.bcadx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    EditText Name, Phone, Email, Password, ConfirmPass;
    Button btnRegister;
    TextView LinkReg;
    FirebaseAuth FAuth;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Name = findViewById(R.id.txtName);
        Phone = findViewById(R.id.txtPhone);
        Email = findViewById(R.id.txtEmail);
        Password = findViewById(R.id.txtPassword);
        ConfirmPass = findViewById(R.id.txtConfirmPass);
        btnRegister = findViewById(R.id.btnRegister);
        LinkReg = findViewById(R.id.txtLinkReg);

        FAuth = FirebaseAuth.getInstance();

        /*if(FAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }*/
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Name.getText().toString().trim();
                String phone = String.valueOf(Phone.getText());
                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();
                String confirmpass = ConfirmPass.getText().toString().trim();




                if (!password.equals(confirmpass)) {
                    ConfirmPass.setError("Passwords do not match");
                } else {
                    FAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                try {


                                    User user = new User(name, phone, email);
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    mDatabase = database.getReference().push();


                                    mDatabase.setValue(user);


                                    Toast.makeText(Register.this, "User has been created", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                } catch (Error e) {
                                    Toast.makeText(Register.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(Register.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

                if (TextUtils.isEmpty(email)) {
                    Email.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Password.setError("Password  is required");
                    return;
                }
                if (password.length() < 6) {
                    Password.setError("Password must be more than 6 characters");
                    return;
                }//creating new user with email and password


            }


        });

    }

    public void linkreg(View view) {
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}