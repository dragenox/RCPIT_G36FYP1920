package com.ariatekstudios.dragenox.g36fyp1920;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rcpit.g36fyp1920.micronlp.microNLP;

public class SplashActivity extends AppCompatActivity {

    Button register, sign_in;
    FirebaseUser user;

    @Override
    protected void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        register = findViewById(R.id.register_button);
        sign_in = findViewById(R.id.sign_in_button);

        register.setOnClickListener(v -> startActivity(new Intent(SplashActivity.this, RegisterActivity.class)));

        sign_in.setOnClickListener(v -> startActivity(new Intent(SplashActivity.this, LoginActivity.class)));

        try {
            microNLP.test(SplashActivity.this);
        }catch (Exception e){
            Log.e("microNLP", "Error with library!");
        }
    }
}
