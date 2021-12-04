package com.example.helpdroid.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.helpdroid.R;
import com.example.helpdroid.adapter.Adapter;
import com.example.helpdroid.model.Pojo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DetailsListHospitalActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    ImageView callButton;
    public static final int REQUEST_CALL = 1;
    List<Pojo> list = new ArrayList<>();
    String division;
    Pojo pojo;
    Adapter adapter;
    RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_list_hospital);

        //spinner
        Spinner spinner = findViewById(R.id.division_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.divisionName, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        //recyclerView
        recyclerView = findViewById(R.id.hospitalRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    //call intent
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String number = pojo.getNumber();
                if (number.trim().length() > 0) {
                    if (ContextCompat.checkSelfPermission(DetailsListHospitalActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(DetailsListHospitalActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                    } else {
                        String dial = "tel:" + number;
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                    }
                } else {
                    Toast.makeText(DetailsListHospitalActivity.this, "Invalid Number!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        division = parent.getItemAtPosition(position).toString();
        Log.d(TAG, "onItemSelected: "+division);
        list.clear();

        //database operations
        db.collection("Hospitals")
                .whereEqualTo("division",division)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            Pojo pojo = new Pojo(
                                    documentSnapshot.getData().get("division").toString(),
                                    documentSnapshot.getData().get("hospitalAddress").toString(),
                                    documentSnapshot.getData().get("hospitalName").toString(),
                                    documentSnapshot.getData().get("hospitalNumber").toString()
                            );
                            list.add(pojo);
                            Log.d(TAG, "onSuccess: " + documentSnapshot);
                        }
                        adapter = new Adapter(list);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        //for call intent
                        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
                            @Override
                            public void OnItemClick(Pojo pojo) {
                                String number = pojo.getNumber();
                                if (number.trim().length() > 0) {
                                    if (ContextCompat.checkSelfPermission(DetailsListHospitalActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(DetailsListHospitalActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                                    } else {
                                        String dial = "tel:" + number;
                                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                                    }
                                } else {
                                    Toast.makeText(DetailsListHospitalActivity.this, "Invalid Number!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Check Your Internet!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}