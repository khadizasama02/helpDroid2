package com.example.helpdroid.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.helpdroid.R;
import com.example.helpdroid.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        //click handle
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        binding.textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToRegistration = new Intent(getApplicationContext(),RegistrationActivity.class);
                startActivity(goToRegistration);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    private void login() {
        String email = binding.useremailInput.getText().toString().trim();
        String pass = binding.userPass.getText().toString().trim();

        //database operations for authenticate a user
        firebaseAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            binding.useremailInput.setText("");
                            binding.userPass.setText("");
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();

                            Intent goToMain = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(goToMain);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Register yourself first", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}