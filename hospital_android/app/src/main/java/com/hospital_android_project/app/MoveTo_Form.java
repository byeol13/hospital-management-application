package com.hospital_android_project.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

public class MoveTo_Form extends AppCompatActivity {

    private TextInputEditText causeEditText;
    private MaterialButton confirmButton, cancelButton;
    private Long departmentId;
    private Long patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_to_form);

        causeEditText = findViewById(R.id.field_cause);
        confirmButton = findViewById(R.id.form_actionButton);
        cancelButton = findViewById(R.id.form_cancelButton);

        departmentId = getIntent().getLongExtra("departmentId", -1);
        patientId = getIntent().getLongExtra("patientId", -1);

        confirmButton.setOnClickListener(v -> {
            String cause = causeEditText.getText().toString();
            if (!cause.isEmpty()) {
                moveToDepartment(cause);
            } else {
                showEmptyCause();
            }
        });

        cancelButton.setOnClickListener(view -> finish());

        checkPatientAdmittedStatus();
    }

    private void checkPatientAdmittedStatus() {
        RetrofitService retrofitService = new RetrofitService();
        AdmissionStateApi admissionStateApi = retrofitService.getRetrofit().create(AdmissionStateApi.class);

        admissionStateApi.findByPatientIdAndExitingDateIsNull(patientId)
                .enqueue(new Callback<AdmissionState>() {
                    @Override
                    public void onResponse(Call<AdmissionState> call, Response<AdmissionState> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            AdmissionState admissionState = response.body();
                            if (!admissionState.isDischarge()) {
                                confirmButton.setEnabled(true); // Patient is admitted, enable move button
                            } else {
                                confirmButton.setEnabled(false); // Patient is discharged, disable move button
                            }
                        } else {
                            confirmButton.setEnabled(false); // Error or patient not found, disable move button
                            showConfirmationDialog("Move unavailable", "Admit patient to proceed");
                        }
                    }

                    @Override
                    public void onFailure(Call<AdmissionState> call, Throwable t) {
                        confirmButton.setEnabled(false); // Error, disable move button
                        Toast.makeText(MoveTo_Form.this, "Error checking patient status", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void moveToDepartment(String cause) {
        RetrofitService retrofitService = new RetrofitService();
        AdmissionStateApi admissionStateApi = retrofitService.getRetrofit().create(AdmissionStateApi.class);

        AdmissionState updatedAdmissionState = new AdmissionState();
        updatedAdmissionState.setReason("changing department");

        // First, update the reason for the current admission state
        admissionStateApi.updateReason(patientId, updatedAdmissionState)
                .enqueue(new Callback<AdmissionState>() {
                    @Override
                    public void onResponse(Call<AdmissionState> call, Response<AdmissionState> response) {
                        if (response.isSuccessful()) {
                            // If reason update is successful, proceed to admit the patient to the new department
                            AdmissionState newAdmissionState = new AdmissionState();
                            newAdmissionState.setCause(cause);
                            newAdmissionState.setPatientid(patientId);
                            newAdmissionState.setDepartmentid(departmentId);
                            newAdmissionState.setEnteringdate(formatDateTime(new Date()));
                            newAdmissionState.setDischarge(false);

                            admissionStateApi.admitPatient(patientId, newAdmissionState)
                                    .enqueue(new Callback<AdmissionState>() {
                                        @Override
                                        public void onResponse(Call<AdmissionState> call, Response<AdmissionState> response) {
                                            if (response.isSuccessful()) {
                                                showConfirmationDialog("Move Successful", "Patient moved successfully!");
                                            } else {
                                                handleMoveError(response);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<AdmissionState> call, Throwable t) {
                                            Toast.makeText(MoveTo_Form.this, "Error admitting patient", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            handleMoveError(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<AdmissionState> call, Throwable t) {
                        Toast.makeText(MoveTo_Form.this, "Error updating reason", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        return sdf.format(date);
    }


    private void showConfirmationDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    dialog.dismiss();
                    Intent intent = new Intent(MoveTo_Form.this, PatientsList.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .show();
    }

    private void handleMoveError(Response<AdmissionState> response) {
        try {
            String errorBody = response.errorBody().string();
            Log.e("MoveToDepartment", "Error moving patient. Error body: " + errorBody);
            Toast.makeText(MoveTo_Form.this, "Error moving patient: " + errorBody, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("MoveToDepartment", "IOException: " + e.getMessage());
            Toast.makeText(MoveTo_Form.this, "Error moving patient", Toast.LENGTH_SHORT).show();
        }
    }

    private void showEmptyCause() {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Please enter a cause")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void alertPatientNotAdmitted() {
        new AlertDialog.Builder(this)
                .setTitle("Patient Not Admitted")
                .setMessage("Patient must be admitted before they can be moved to another department.")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                .show();
    }
}