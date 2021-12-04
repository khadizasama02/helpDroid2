package com.example.helpdroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.helpdroid.R;
import com.example.helpdroid.constants.Constants;
import com.example.helpdroid.databinding.ActivityEditUserProfileBinding;
import com.example.helpdroid.databinding.ActivityProfileBinding;
import com.google.android.material.textfield.TextInputEditText;

public class EditUserProfileActivity extends AppCompatActivity {
    private ActivityEditUserProfileBinding binding;
    private int selection;
    private String name,email,trustedContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selection = extras.getInt(Constants.USER_EDIT_SELECTION);
            name = extras.getString(Constants.USER_NAME);
            email = extras.getString(Constants.USER_EMAIL);
            trustedContact = extras.getString(Constants.USER_TRUSTED_CONTACT);
        }

        switch (selection) {
            case 0:
                //splitting the fullName to first and last name
                String fullName = name;
                int idx = fullName.lastIndexOf(' ');
                if (idx == -1)
                    throw new IllegalArgumentException("Only a single name: " + fullName);
                String firstName = fullName.substring(0, idx);
                String lastName = fullName.substring(idx + 1);

                binding.firstTextInputLayout.setHint("First Name");
                binding.secondTextInputLayout.setHint("Last Name");
                binding.firstEdittext.setText(firstName);
                binding.secondEdittext.setText(lastName);
                binding.secondTextInputLayout.setVisibility(View.VISIBLE);
                break;

            case 1:
                binding.firstTextInputLayout.setHint("Email");
                binding.firstEdittext.setText(email);
                binding.secondTextInputLayout.setVisibility(View.GONE);
                break;
            case 2:
                binding.firstTextInputLayout.setHint("Trusted Contact");
                binding.firstEdittext.setText(trustedContact);
                binding.secondTextInputLayout.setVisibility(View.GONE);
        }

    }
}