package com.hospital_android_project.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hospital_android_project.app.model.Department;
import com.hospital_android_project.app.retrofit.DepartmentApi;
import com.hospital_android_project.app.retrofit.RetrofitService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepartmentForm extends AppCompatActivity {

    private Department department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_form);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("department")) {
            department = (Department) intent.getSerializableExtra("department");
            if (department != null) {
                TextInputEditText codeEditText = findViewById(R.id.form_textFieldCode);
                TextInputEditText nameEditText = findViewById(R.id.form_textFieldName);
                codeEditText.setText(department.getCode());
                nameEditText.setText(department.getName());
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
        TextInputEditText inputEditCode = findViewById(R.id.form_textFieldCode);
        TextInputEditText inputEditName = findViewById(R.id.form_textFieldName);
        MaterialButton buttonAction = findViewById(R.id.form_actionButton);
        TextView depFormLabel = findViewById(R.id.dep_form_label);

        if (department != null) {
            buttonAction.setText("Update");
            depFormLabel.setText("Edit Department");

            buttonAction.setOnClickListener(view -> {
                String originalCode = department.getCode();
                String newCode = inputEditCode.getText().toString();
                String newName = inputEditName.getText().toString();

                updateDepartment(originalCode, newCode, newName);
            });
        } else {
            buttonAction.setText("Add");
            depFormLabel.setText("New Department");

            buttonAction.setOnClickListener(view -> newDepartment(inputEditCode.getText().toString(), inputEditName.getText().toString()));
        }
    }

    private void newDepartment(String code, String name) {
        Department department = new Department();
        department.setCode(code);
        department.setName(name);

        RetrofitService retrofitService = new RetrofitService();
        DepartmentApi departmentApi = retrofitService.getRetrofit().create(DepartmentApi.class);
        departmentApi.createDepartment(department)
                .enqueue(new Callback<Department>() {
                    @Override
                    public void onResponse(Call<Department> call, Response<Department> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(DepartmentForm.this, "New department added", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DepartmentForm.this, DepartmentsList.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(DepartmentForm.this, "Failed to add new department", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Department> call, Throwable t) {
                        Toast.makeText(DepartmentForm.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void updateDepartment(String originalCode, String newCode, String name) {
        Department updatedDepartment = new Department();
        updatedDepartment.setCode(newCode);
        updatedDepartment.setName(name);

        RetrofitService retrofitService = new RetrofitService();
        DepartmentApi departmentApi = retrofitService.getRetrofit().create(DepartmentApi.class);
        departmentApi.updateDepartment(originalCode, updatedDepartment)
                .enqueue(new Callback<Department>() {
                    @Override
                    public void onResponse(Call<Department> call, Response<Department> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(DepartmentForm.this, "Department updated successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DepartmentForm.this, DepartmentsList.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(DepartmentForm.this, "Failed to update department", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Department> call, Throwable t) {
                        Toast.makeText(DepartmentForm.this, "Network error", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(DepartmentForm.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });
    }

}