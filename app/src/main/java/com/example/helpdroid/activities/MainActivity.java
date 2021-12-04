package com.example.helpdroid.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import io.grpc.okhttp.internal.Util;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.helpdroid.R;
import com.example.helpdroid.model.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.CarouselViewPager;
import com.synnapps.carouselview.ImageListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CardView police, hospital, fireService, help;
    ShapeableImageView profile;
    TextView currentLocation,userNameText;
    String locationMessage;
    CarouselView carouselView;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final String TAG = "MainActivity";
    User user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    //image links of slider
    private String[] urls = {"https://www.orissapost.com/wp-content/uploads/2020/01/WOMEN-SAFETY.jpeg",
            "https://vodakm.zeenews.com/vod/How-to-Protect-Yourself-IN-WEB.mp4/screenshot/00000027.jpg",
            "https://www.vpnmentor.com/wp-content/uploads/2018/07/wg18-768x403.jpg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //finding all the views
        police = findViewById(R.id.police);
        hospital = findViewById(R.id.hospital);
        fireService = findViewById(R.id.fireService);
        help = findViewById(R.id.help);
        currentLocation = findViewById(R.id.currentLocation);
        profile = findViewById(R.id.profile);
        userNameText = findViewById(R.id.user_name);
        carouselView = findViewById(R.id.imageSlider);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();

        //database operation
        String email = auth.getCurrentUser().getEmail();
        Log.d(TAG, "onCreate: "+email);

        db.collection("User").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    user = documentSnapshot.toObject(User.class);

                    String userName = user.getUserName();
                    userNameText.setText(userName);
                }
            }
        });



        police.setOnClickListener(this);
        hospital.setOnClickListener(this);
        fireService.setOnClickListener(this);
        help.setOnClickListener(this);
        profile.setOnClickListener(this);


        //image slider in home page
        carouselView.setPageCount(3);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Glide.with(imageView).load(urls[position]).into(imageView);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        });

    }


    //getting location of the device
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
        else {
            //when permission granted
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {

                @Override
                public void onComplete(@NonNull @NotNull Task<Location> task) {

                    //initialize location
                    Location location = task.getResult();
                    if (location != null) {

                        //initialize geocoder
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                        //initialize address list
                        try {
                            List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                            currentLocation.setText(address.get(0).getAddressLine(0));
                            locationMessage = address.get(0).getAddressLine(0);
                            Log.d(TAG, "onComplete: "+locationMessage);
                            Log.d(TAG, "onComplete: "+address);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        }

    }

    //handle all the clicks
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.police:
                Intent police = new Intent(MainActivity.this, DetailsListPoliceActivity.class);
                startActivity(police);
                break;
            case R.id.hospital:
                Intent hospital = new Intent(MainActivity.this, DetailsListHospitalActivity.class);
                startActivity(hospital);
                break;
            case R.id.fireService:
                Intent fire = new Intent(MainActivity.this, DetailsListFireActivity.class);
                startActivity(fire);
                break;
            case R.id.profile:
                Intent profileButton = new Intent(MainActivity.this, ProfileActivity.class);
                profileButton.putExtra("user",user);
                startActivity(profileButton);
                break;
            case R.id.help:
                //check conditions
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    sendMessage();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 100);
                }
        }
    }

    //sending message to trusted number
    private void sendMessage() {
        String message = "Help me please! \nI am stack in " + locationMessage + ".\nHelpdroid";
        String trustedContact = user.getTrustedPhoneNumber1();
        Log.d(TAG, "sendMessage: "+trustedContact);
        if (!message.equals("")) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(trustedContact, null, message, null, null);
            Toast.makeText(MainActivity.this, "Message sent successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "You do not have balance!", Toast.LENGTH_SHORT).show();
        }
    }

}