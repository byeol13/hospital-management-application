package com.hospital_android_project.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.hospital_android_project.app.model.AdmissionState;
import com.hospital_android_project.app.retrofit.AdmissionStateApi;
import com.hospital_android_project.app.retrofit.RetrofitService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admit_Form extends AppCompatActivity {

    private TextInputEditText causeEditText;
    private MaterialButton confirmButton, cancelButton;
    private Long departmentId;
    private Long patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admit_form);

        causeEditText = findViewById(R.id.field_cause);
        confirmButton = findViewById(R.id.form_actionButton);
        cancelButton = findViewById(R.id.form_cancelButton);

        departmentId = getIntent().getLongExtra("departmentId", -1);
        patientId = getIntent().getLongExtra("patientId", -1);


        checkPatientStatus();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cause = causeEditText.getText().toString();
                if (!cause.isEmpty()) {
                    admitPatient(cause);
                } else {
                    showEmptyCause();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void checkPatientStatus() {
        RetrofitService retrofitService = new RetrofitService();
        AdmissionStateApi admissionStateApi = retrofitService.getRetrofit().create(AdmissionStateApi.class);

        admissionStateApi.findByPatientIdAndExitingDateIsNull(patientId)
                .enqueue(new Callback<AdmissionState>() {
                    @Override
                    public void onResponse(Call<AdmissionState> call, Response<AdmissionState> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            AdmissionState admissionState = response.body();
                            if (!admissionState.isDischarge()) {
                                confirmButton.setEnabled(false);
                                alertAdmissionStatus();
                            }
                        } else {
                            confirmButton.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<AdmissionState> call, Throwable t) {
                        Toast.makeText(Admit_Form.this, "Error checking patient status", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void admitPatient(String cause) {
        AdmissionState admissionState = new AdmissionState();
        admissionState.setCause(cause);
        admissionState.setPatientid(patientId);
        admissionState.setDepartmentid(departmentId);

        admissionState.setEnteringdate(formatDateTime(new Date()));

        RetrofitService retrofitService = new RetrofitService();
        AdmissionStateApi admissionStateApi = retrofitService.getRetrofit().create(AdmissionStateApi.class);


        admissionStateApi.admitPatient(patientId, admissionState)
                .enqueue(new Callback<AdmissionState>() {
                    @Override
                    public void onResponse(Call<AdmissionState> call, Response<AdmissionState> response) {
                        if (response.isSuccessful()) {
                            showConfirmationDialog();
                        } else {
                            try {
                                String errorBody = response.errorBody().string();
                                Log.e("AdmitPatient", "Error admitting patient. Error body: " + errorBody);
                               Toast.makeText(Admit_Form.this, errorBody, Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                Log.e("AdmitPatient", "IOException: " + e.getMessage());
                                Toast.makeText(Admit_Form.this, "Error admitting patient", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AdmissionState> call, Throwable t) {
                        Toast.makeText(Admit_Form.this, "Error admitting patient", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        return sdf.format(date);
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Admission Successful")
                .setMessage("Patient admitted successfully!")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    dialog.dismiss();
                    Intent intent = new Intent(Admit_Form.this, PatientsList.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .show();
    }

    private void alertAdmissionStatus() {
        new AlertDialog.Builder(this)
                .setTitle("Cannot Admit Patient")
                .setMessage("Patient has not been discharged yet")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    dialog.dismiss();
                    Intent intent = new Intent(Admit_Form.this, PatientsList.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .show();
    }

    private void showEmptyCause() {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Please enter a cause")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                .show();
    }
}