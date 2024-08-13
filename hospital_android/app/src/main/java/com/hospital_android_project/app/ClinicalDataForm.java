package com.hospital_android_project.app;

import androidx.appcompat.app.AppCompatActivity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.hospital_android_project.app.model.Clinicalrecord;
import com.hospital_android_project.app.model.Department;
import com.hospital_android_project.app.retrofit.ClinicalrecordApi;
import com.hospital_android_project.app.retrofit.RetrofitService;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//
public class ClinicalDataForm extends AppCompatActivity {
//
    private Clinicalrecord clinicalrecord;
    private Long patientId;
    private String patientName;
    private String patientLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinical_data_form);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("patientId")) {
            patientId = intent.getLongExtra("patientId", -1);
            patientName = intent.getStringExtra("patientName");
            patientLastName = intent.getStringExtra("patientLastName");
        } else {
            Toast.makeText(this, "PatientId not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (intent != null && intent.hasExtra("clinicalrecord")) {
            clinicalrecord = (Clinicalrecord) intent.getSerializableExtra("clinicalrecord");
        } if (clinicalrecord != null) {
            TextInputEditText clinicalDataEditText = findViewById(R.id.form_textFieldClinicalData);
            clinicalDataEditText.setText(clinicalrecord.getClinicaldata());
        }


        MaterialButton cancelBtn = findViewById(R.id.form_cancelButton);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initializeComponents();
    }

    private void initializeComponents() {
        TextInputEditText inputEditClinicalData = findViewById(R.id.form_textFieldClinicalData);
        MaterialButton actionButton = findViewById(R.id.form_actionButton);
        TextView clinicalRecordsFormLabel = findViewById(R.id.clinicalRecords_form_label);

        if (clinicalrecord != null) {
            clinicalRecordsFormLabel.setText("Edit Record");

            actionButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String originalClinicalData = clinicalrecord.getClinicaldata();
                    String newClinicalData = inputEditClinicalData.getText().toString();
                    Long patientId = clinicalrecord.getPatientid();

                    updateClinicalRecord(originalClinicalData, newClinicalData, patientId);
                }
            });

        } else {
            clinicalRecordsFormLabel.setText("New Record");

            actionButton.setOnClickListener(view -> newClinicalRecord(inputEditClinicalData.getText().toString(), patientId));
        }
    }

    private void updateClinicalRecord(String originalClinicalData, String newClinicalData, Long patientId) {
        Clinicalrecord updatedClinicalRecord = new Clinicalrecord();
        updatedClinicalRecord.setClinicaldata(newClinicalData);
        updatedClinicalRecord.setPatientid(patientId);

        RetrofitService retrofitService = new RetrofitService();
        ClinicalrecordApi clinicalrecordApi = retrofitService.getRetrofit().create(ClinicalrecordApi.class);
        clinicalrecordApi.updateClinicalrecord(updatedClinicalRecord, originalClinicalData)
                .enqueue(new Callback<Clinicalrecord>() {
                    @Override
                    public void onResponse(Call<Clinicalrecord> call, Response<Clinicalrecord> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ClinicalDataForm.this, "Record updated successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ClinicalDataForm.this, PatientClinicalRecords.class);
                            intent.putExtra("patientId", patientId); // Pass patientId back
                            intent.putExtra("patientName", getIntent().getStringExtra("patientName")); // Pass patient name back
                            intent.putExtra("patientLastName", getIntent().getStringExtra("patientLastName")); // Pass patient last name back
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ClinicalDataForm.this, "Failed to update record", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Clinicalrecord> call, Throwable t) {
                        Toast.makeText(ClinicalDataForm.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void newClinicalRecord(String clinicalData, Long patientId) {
        Clinicalrecord clinicalrecord = new Clinicalrecord();
        clinicalrecord.setClinicaldata(clinicalData);
        clinicalrecord.setPatientid(patientId);

        RetrofitService retrofitService = new RetrofitService();
        ClinicalrecordApi clinicalrecordApi = retrofitService.getRetrofit().create(ClinicalrecordApi.class);
        clinicalrecordApi.createClinicalrecord(clinicalrecord)
                .enqueue(new Callback<Clinicalrecord>() {
                    @Override
                    public void onResponse(Call<Clinicalrecord> call, Response<Clinicalrecord> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ClinicalDataForm.this, "New clinical record added", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ClinicalDataForm.this, PatientClinicalRecords.class);
                            intent.putExtra("patientId", patientId); // Pass patientId back
                            intent.putExtra("patientName", getIntent().getStringExtra("patientName")); // Pass patient name back
                            intent.putExtra("patientLastName", getIntent().getStringExtra("patientLastName")); // Pass patient last name back
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.e(TAG, "Error updating record: " + response.message());
                            Toast.makeText(ClinicalDataForm.this, "Failed to add new clinical record", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Clinicalrecord> call, Throwable t) {
                        Log.e(TAG, "Error updating record: " + t.getMessage());
                        Toast.makeText(ClinicalDataForm.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}