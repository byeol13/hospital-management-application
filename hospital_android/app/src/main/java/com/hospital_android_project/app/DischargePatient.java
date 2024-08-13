package com.hospital_android_project.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hospital_android_project.app.model.AdmissionState;
import com.hospital_android_project.app.retrofit.AdmissionStateApi;
import com.hospital_android_project.app.retrofit.PatientApi;
import com.hospital_android_project.app.retrofit.RetrofitService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DischargePatient extends AppCompatActivity {

    private RadioGroup radioGroup;
    private Button btnConfirm, btnCancel;

    private Long patientId;
    private AdmissionStateApi admissionStateApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discharge_patient);

        RetrofitService retrofitService = new RetrofitService();
        admissionStateApi = retrofitService.getRetrofit().create(AdmissionStateApi.class);

        patientId = getIntent().getLongExtra("patientId", -1);
        if (patientId == -1) {
            Toast.makeText(this, "Invalid patient ID", Toast.LENGTH_SHORT).show();
            finish();
        }

        radioGroup = findViewById(R.id.radio_group);
        btnConfirm = findViewById(R.id.btn_confirm);
        btnCancel = findViewById(R.id.btn_cancel);


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    showEmptyReason();
                    Toast.makeText(DischargePatient.this, "Please select a reason", Toast.LENGTH_SHORT).show();
                } else {
                    RadioButton radioButton = findViewById(selectedId);
                    String reason = radioButton.getText().toString();
                    dischargePatient(patientId, reason);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
    private void dischargePatient(Long patientId, String reason) {
        AdmissionState dischargeData = new AdmissionState();
        dischargeData.setReason(reason);

        admissionStateApi.updateReason(patientId, dischargeData)
                .enqueue(new Callback<AdmissionState>() {
                    @Override
                    public void onResponse(Call<AdmissionState> call, Response<AdmissionState> response) {
                        if (response.isSuccessful()) {
//                            Toast.makeText(DischargePatient.this, "Patient discharged successfully", Toast.LENGTH_SHORT).show();
                            showConfirmationDialog("Patient Discharged", "Patient discharged successfully");
                        } else {
                            showConfirmationDialog("Cannot Discharge", "Patient is not admitted to discharge");
//                            Toast.makeText(DischargePatient.this, "Failed to discharge patient", Toast.LENGTH_SHORT).show();
                            Log.e("DischargePatient", "Failed to discharge patient. Error body: " + response.errorBody());
                        }
                    }

                    @Override
                    public void onFailure(Call<AdmissionState> call, Throwable t) {
                        Toast.makeText(DischargePatient.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showConfirmationDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    dialog.dismiss();
                    Intent intent = new Intent(DischargePatient.this, PatientsList.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .show();
    }

    private void showEmptyReason() {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Please select a reason")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                .show();
    }

}