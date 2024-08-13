package com.hospital_android_project.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.hospital_android_project.app.adapter.PatientAdapter;
import com.hospital_android_project.app.model.Patient;
import com.hospital_android_project.app.retrofit.PatientApi;
import com.hospital_android_project.app.retrofit.RetrofitService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientsList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText searchBar;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_list);

        recyclerView = findViewById(R.id.patientsList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MaterialButton homeBtn = findViewById(R.id.patient_home_btn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientsList.this, MainPageActivity.class);
                startActivity(intent);
            }
        });

        searchBar = findViewById(R.id.patient_search_input);
        searchButton = findViewById(R.id.patient_search_btn);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idString = searchBar.getText().toString();
                if (!idString.isEmpty()) {
                    try {
                        Long id = Long.parseLong(idString);
                        searchPatient(id);
                    } catch (NumberFormatException e) {
                        Toast.makeText(PatientsList.this, "Invalid ID format", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PatientsList.this, "Please enter patient ID to search", Toast.LENGTH_SHORT).show();
                }
            }
        });

        MaterialButton addDepBtn = findViewById(R.id.patientList_nav_btn);
        addDepBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, PatientForm.class);
            startActivity(intent);
        });

        loadPatients();
    }
    
    private void loadPatients() {
        RetrofitService retrofitService = new RetrofitService();
        PatientApi patientApi = retrofitService.getRetrofit().create(PatientApi.class);
        patientApi.getAllPatients()
                .enqueue(new Callback<List<Patient>>() {
                    @Override
                    public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                        populatePatientList(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Patient>> call, Throwable t) {
                        Toast.makeText(PatientsList.this, "Failed to load patients", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    
    
    private void searchPatient(Long id) {
        RetrofitService retrofitService = new RetrofitService();
        PatientApi patientApi = retrofitService.getRetrofit().create(PatientApi.class);
        patientApi.getPatientByName(id)
                .enqueue(new Callback<Patient>() {
                    @Override
                    public void onResponse(Call<Patient> call, Response<Patient> response) {
                        if (response.isSuccessful()) {
                            Patient patient = response.body();
                            List<Patient> searchResult = new ArrayList<>();
                            if (patient != null) {
                                searchResult.add(patient);
                            }
                            populatePatientList(searchResult);
                        } else {
                            Toast.makeText(PatientsList.this, "No patients found with name: " + id, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Patient> call, Throwable t) {
                        Toast.makeText(PatientsList.this, "Failed to search patient", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populatePatientList(List<Patient> patientList) {
        if (patientList != null && !patientList.isEmpty()) {
            PatientAdapter adapter = new PatientAdapter(this, patientList);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No patients found", Toast.LENGTH_SHORT).show();
        }
    }
}