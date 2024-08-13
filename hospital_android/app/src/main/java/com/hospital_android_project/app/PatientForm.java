package com.hospital_android_project.app;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.hospital_android_project.app.model.Patient;
import com.hospital_android_project.app.retrofit.PatientApi;
import com.hospital_android_project.app.retrofit.RetrofitService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientForm extends AppCompatActivity {

    private Patient patient;
    private TextInputEditText birthdateEditText;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_form);

        birthdateEditText = findViewById(R.id.form_textFieldBirthdate);
        birthdateEditText.setOnClickListener(view -> showDatePickerDialog());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("patient")) {
            patient = (Patient) intent.getSerializableExtra("patient");
            if (patient != null) {
                TextInputEditText nameEditText = findViewById(R.id.form_txtFieldPatientName);
                TextInputEditText lastnameEditText = findViewById(R.id.form_textFieldLastName);
                nameEditText.setText(patient.getName());
                lastnameEditText.setText(patient.getLastname());

                if (patient.getBirthdate() != null) {
                    calendar.setTime(patient.getBirthdate());
                    updateDateLabel();
                }
            }
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
        TextInputEditText inputEditName = findViewById(R.id.form_txtFieldPatientName);
        TextInputEditText inputEditLastName = findViewById(R.id.form_textFieldLastName);
        birthdateEditText = findViewById(R.id.form_textFieldBirthdate);
        birthdateEditText.setOnClickListener(view -> showDatePickerDialog());

        MaterialButton buttonAction = findViewById(R.id.form_actionButton);
        TextView patientFormLabel = findViewById(R.id.patient_form_label);

        if (patient != null) {
            buttonAction.setText("Update");
            patientFormLabel.setText("Edit Patient");

            buttonAction.setOnClickListener(view -> updatePatient(patient.getId(), inputEditName.getText().toString(), inputEditLastName.getText().toString()));
        } else {
            buttonAction.setText("Add");
            patientFormLabel.setText("New Patient");

            buttonAction.setOnClickListener(view -> newPatient(inputEditName.getText().toString(), inputEditLastName.getText().toString()));
        }
    }

    private void newPatient(String name, String lastname) {
        Patient patient = new Patient();
        patient.setName(name);
        patient.setLastname(lastname);
        patient.setBirthdate(calendar.getTime());

        updateDateLabel();

        RetrofitService retrofitService = new RetrofitService();
        PatientApi patientApi = retrofitService.getRetrofit().create(PatientApi.class);

        patientApi.createPatient(patient)
                .enqueue(new Callback<Patient>() {
                    @Override
                    public void onResponse(Call<Patient> call, Response<Patient> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(PatientForm.this, "New patient added", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PatientForm.this, PatientsList.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            try {
                                // Log error response
                                String errorBody = response.errorBody() != null ? response.errorBody().string() : "";
                                Log.e("PatientForm", "Failed to add new patient. Error Body: " + errorBody);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(PatientForm.this, "Failed to add new patient", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Patient> call, Throwable t) {
                        Log.e("PatientForm", "Failed to send request to create new patient", t);
                        Toast.makeText(PatientForm.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updatePatient(Long id, String name, String lastname) {
        Patient updatedPatient = new Patient();
        updatedPatient.setId(id);
        updatedPatient.setName(name);
        updatedPatient.setLastname(lastname);
        updatedPatient.setBirthdate(calendar.getTime());

        updateDateLabel();

        Log.d("PatientForm", "Updating patient: " + updatedPatient);

        RetrofitService retrofitService = new RetrofitService();
        PatientApi patientApi = retrofitService.getRetrofit().create(PatientApi.class);
        patientApi.updatePatient(id, updatedPatient)
                .enqueue(new Callback<Patient>() {
                    @Override
                    public void onResponse(Call<Patient> call, Response<Patient> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(PatientForm.this, "Patient updated successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PatientForm.this, PatientsList.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.e("PatientForm", "Failed to update patient: " + response.message());
                            Toast.makeText(PatientForm.this, "Failed to update department", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Patient> call, Throwable t) {
                        Log.e("PatientForm", "Network error: ", t);
                        Toast.makeText(PatientForm.this, "Network error", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(PatientForm.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });
    }

    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    calendar.set(Calendar.YEAR, selectedYear);
                    calendar.set(Calendar.MONTH, selectedMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                    updateDateLabel();
                }, year, month, day);

        datePickerDialog.show();
    }

    private void updateDateLabel() {
        String myFormat = "MMM d, yyyy h:mm:ss a";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        birthdateEditText.setText(sdf.format(calendar.getTime()));
    }

}
