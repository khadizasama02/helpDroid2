package com.example.helpdroid.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.helpdroid.R;
import com.example.helpdroid.constants.Constants;
import com.example.helpdroid.databinding.ActivityProfileBinding;
import com.example.helpdroid.model.User;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();

        User user = intent.getParcelableExtra("user");

        binding.username.setText(user.getUserName());
        binding.userEmail.setText(user.getUserEmail());
        binding.userNumber.setText(user.getUserPhoneNumber());
        binding.trustedContact1.setText(user.getTrustedPhoneNumber1());

        binding.editUsername.setOnClickListener(this);
        binding.editUserEmail.setOnClickListener(this);
        binding.editUserProfilePic.setOnClickListener(this);
        binding.editTrustedContact1.setOnClickListener(this);
        binding.logoutButton.setOnClickListener(this);
    }

    //click handle of buttons
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.edit_username:
                bundle.putInt(Constants.USER_EDIT_SELECTION, Constants.USER_NAME_VALUE);
                bundle.putString(Constants.USER_NAME, (String) binding.username.getText());
                Intent intent1 = new Intent(ProfileActivity.this,EditUserProfileActivity.class);
                intent1.putExtras(bundle);
                startActivity(intent1);
                break;
            case R.id.edit_user_email:
                bundle.putInt(Constants.USER_EDIT_SELECTION, Constants.USER_EMAIL_VALUE);
                bundle.putString(Constants.USER_EMAIL, (String) binding.userEmail.getText());
                Intent intent2 = new Intent(ProfileActivity.this,EditUserProfileActivity.class);
                intent2.putExtras(bundle);
                startActivity(intent2);
                break;
            case R.id.edit_trusted_contact_1:
                bundle.putInt(Constants.USER_EDIT_SELECTION, Constants.USER_TRUSTED_CONTACT_VALUE);
                bundle.putString(Constants.USER_TRUSTED_CONTACT, (String) binding.trustedContact1.getText());
                Intent intent3 = new Intent(ProfileActivity.this,EditUserProfileActivity.class);
                intent3.putExtras(bundle);
                startActivity(intent3);
                break;
            case R.id.edit_user_profile_pic:
                ImagePicker.with(this)
                        .galleryOnly()
                        .crop() //Crop image, Check Customization for more option
                        .compress(1024) //Final image size will be less than 1 MB
                        .maxResultSize(1080, 1080) //Final image resolution will be less than 1080 x 1080
                        .start();
            case R.id.logout_button:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
                finish();
        }
    }
        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            Uri uri = data.getData();
            binding.profileImage.setImageURI(uri);
        }


}