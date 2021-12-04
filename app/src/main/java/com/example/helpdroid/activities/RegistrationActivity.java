package com.example.helpdroid.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.helpdroid.R;
import com.example.helpdroid.databinding.ActivityRegistrationBinding;
import com.example.helpdroid.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";
    private ActivityRegistrationBinding binding;
    FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        //click handle
        binding.textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        binding.textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToLogin = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(goToLogin);
                finish();
            }
        });

    }

    private void register() {
        String email = binding.useremailInput.getText().toString().trim();
        String password = binding.userPass.getText().toString().trim();
        String confirmPassword = binding.userConfirmPass.getText().toString().trim();
        String phone = binding.userphoneInput.getText().toString().trim();
        String trustedPhone = binding.trustedphoneInput.getText().toString().trim();
        String name = binding.usernameInput.getText().toString().trim();


        //checking all the condition of text field
        if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty() ||trustedPhone.isEmpty() || name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Field must not be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length()<6 && confirmPassword.length()<6) {
            Toast.makeText(getApplicationContext(), "Password should be 6 character or greater!", Toast.LENGTH_SHORT).show();
        }

        if(phone.length()<11 && phone.length()>11) {
            Toast.makeText(getApplicationContext(), "Phone number must be 11 digit", Toast.LENGTH_SHORT).show();
            return;
        }

        if(trustedPhone.length()<11 && trustedPhone.length()>11) {
            Toast.makeText(getApplicationContext(), "Phone number must be 11 digit", Toast.LENGTH_SHORT).show();
            return;
        }


        if(!password.equals(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Password and Confirm Password do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        //database operations for authenticate a user
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            binding.usernameInput.setText("");
                            binding.userPass.setText("");
                            binding.userConfirmPass.setText("");
                            binding.userphoneInput.setText("");
                            binding.trustedphoneInput.setText("");
                            binding.useremailInput.setText("");

                            Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();

                            User user = new User(name,email,phone,trustedPhone);

                            db.collection("User").document(email).set(user);

                            Intent goToMain = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(goToMain);
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
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