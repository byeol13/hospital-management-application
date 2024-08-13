package com.hospital_android_project.app;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.hospital_android_project.app.adapter.ClinicalrecordAdapter;
import com.hospital_android_project.app.model.Clinicalrecord;
import com.hospital_android_project.app.retrofit.ClinicalrecordApi;
import com.hospital_android_project.app.retrofit.RetrofitService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientClinicalRecords extends AppCompatActivity {
    private Long patientId;
    String patientName;
    String patientLastName;
    private String patientFullName;
    private RecyclerView recyclerView;
    private ClinicalrecordAdapter adapter;
    private TextView patientNameTextView;

    private EditText searchBar;
    private Button searchButton;

    private List<Clinicalrecord> clinicalrecordList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_clinical_records);

        recyclerView = findViewById(R.id.clinicalList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ClinicalrecordAdapter(this);
        recyclerView.setAdapter(adapter);

        MaterialButton backBtn = findViewById(R.id.patient_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientClinicalRecords.this, PatientsList.class);
                startActivity(intent);
            }
        });

        Intent intent1 = getIntent();
        if (intent1 != null && intent1.hasExtra("patientId") && intent1.hasExtra("patientName")) {
            patientId = intent1.getLongExtra("patientId", -1);
            patientName = intent1.getStringExtra("patientName");
            patientLastName = intent1.getStringExtra("patientLastName");

            patientFullName = patientName + " " + patientLastName;
        }

        patientNameTextView = findViewById(R.id.patient_fullname_label);
        patientNameTextView.setText(patientFullName);


        searchBar = findViewById(R.id.record_search_input);
        searchButton = findViewById(R.id.record_search_btn);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clinicalData = searchBar.getText().toString();
                if (!clinicalData.isEmpty()) {
                    searchRecord(clinicalData);
                } else {
                    Toast.makeText(PatientClinicalRecords.this, "Please enter a record to search", Toast.LENGTH_SHORT).show();
                }
            }
        });

        MaterialButton addClinicalData = findViewById(R.id.clinicalRecordList_nav_btn);
        addClinicalData.setOnClickListener(view -> {
            Intent intent = new Intent(this, ClinicalDataForm.class);
            intent.putExtra("patientId", patientId);
            intent.putExtra("patientName", patientName);
            intent.putExtra("patientLastName", patientLastName);
            startActivity(intent);
        });

        loadClinicalRecords();

    }
    private void loadClinicalRecords() {

        RetrofitService retrofitService = new RetrofitService();
        ClinicalrecordApi clinicalrecordApi = retrofitService.getRetrofit().create(ClinicalrecordApi.class);
        clinicalrecordApi.getClinicalrecordByPatientid(patientId)
                .enqueue(new Callback<List<Clinicalrecord>>() {
                    @Override
                    public void onResponse(Call<List<Clinicalrecord>> call, Response<List<Clinicalrecord>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            adapter.setClinicalrecordsList(response.body());
                        } else {
                            Toast.makeText(PatientClinicalRecords.this, "No clinical records found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Clinicalrecord>> call, Throwable t) {
                        Log.e("PatientClinicalRecords", "Error fetching clinical records", t);
                        Toast.makeText(PatientClinicalRecords.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    
    private void searchRecord(String clinicalData) {
        RetrofitService retrofitService = new RetrofitService();
        ClinicalrecordApi clinicalrecordApi = retrofitService.getRetrofit().create(ClinicalrecordApi.class);
        clinicalrecordApi.getClinicalrecordByClinicaldata(clinicalData)
                .enqueue(new Callback<Clinicalrecord>() {
                    @Override
                    public void onResponse(Call<Clinicalrecord> call, Response<Clinicalrecord> response) {
                        if (response.isSuccessful()) {
                            Clinicalrecord clinicalrecord = response.body();
                            List<Clinicalrecord> searchResult = new ArrayList<>();
                            if (clinicalrecord != null) {
                                searchResult.add(clinicalrecord);
                            }
                            adapter.setClinicalrecordsList(searchResult);
                        } else {
                            Toast.makeText(PatientClinicalRecords.this, "No record found with clinical data: " + clinicalData, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Clinicalrecord> call, Throwable t) {
                        Toast.makeText(PatientClinicalRecords.this, "Failed to search clinical record", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}