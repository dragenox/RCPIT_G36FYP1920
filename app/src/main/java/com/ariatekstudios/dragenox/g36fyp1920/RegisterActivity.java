package com.ariatekstudios.dragenox.g36fyp1920;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText username, email, password;
    Button register_button;
    TextView sign_in_button;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register_button = findViewById(R.id.register_button);
        sign_in_button = findViewById(R.id.sign_in_button);

        firebaseAuth = FirebaseAuth.getInstance();

        register_button.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View v) {
                String text_username;
                String text_email;
                String text_password;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    text_username = Objects.requireNonNull(username.getText()).toString();
                    text_email = Objects.requireNonNull(email.getText()).toString();
                    text_password = Objects.requireNonNull(password.getText()).toString();
                }
                else {
                    text_username = username.getText().toString();
                    text_email = email.getText().toString();
                    text_password = password.getText().toString();
                }
                if(TextUtils.isEmpty(text_username) || TextUtils.isEmpty(text_email) || TextUtils.isEmpty(text_password)){
                    Toast.makeText(RegisterActivity.this, "Fields missing" , Toast.LENGTH_SHORT).show();
                } else {
                    if (text_password.length() < 6){
                        Toast.makeText(RegisterActivity.this, "Weak password", Toast.LENGTH_SHORT).show();
                    } else {
                        register (text_username, text_email, text_password);
                    }
                }
            }
        });

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void register(final String username, String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        String userId = Objects.requireNonNull(firebaseUser).getUid();
                        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", userId);
                        hashMap.put("username", username);
                        hashMap.put("imageURL", "default");
                        databaseReference.setValue(hashMap).addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()){
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                finish();
                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "Unable to register with this email or password", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
