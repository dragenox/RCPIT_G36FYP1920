package com.ariatekstudios.dragenox.g36fyp1920;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText recoveryEditText;
    Button resetButton;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Forgot Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recoveryEditText = findViewById(R.id.recovery_email);
        resetButton = findViewById(R.id.reset_button);

        firebaseAuth = FirebaseAuth.getInstance();

        resetButton.setOnClickListener(view -> {
            String email = recoveryEditText.getText().toString();

            if (email.equals("")){
                Toast.makeText(ForgotPasswordActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
            } else {
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(ForgotPasswordActivity.this, "Incorrect Email Address.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgotPasswordActivity.this, SplashActivity.class));
                    } else {
                        String error = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(ForgotPasswordActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
