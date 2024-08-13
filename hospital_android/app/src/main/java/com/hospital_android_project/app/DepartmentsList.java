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
import com.hospital_android_project.app.adapter.DepartmentAdapter;
import com.hospital_android_project.app.model.Department;
import com.hospital_android_project.app.retrofit.DepartmentApi;
import com.hospital_android_project.app.retrofit.RetrofitService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepartmentsList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText searchBar;
    private Button searchButton;
    private List<Department> departmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departments_list);

        recyclerView = findViewById(R.id.depList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MaterialButton homeBtn = findViewById(R.id.dep_home_btn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DepartmentsList.this, MainPageActivity.class);
                startActivity(intent);
            }
        });

        searchBar = findViewById(R.id.search_input);
        searchButton = findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = searchBar.getText().toString();
                if (!code.isEmpty()) {
                    searchDepartment(code);
                } else {
                    Toast.makeText(DepartmentsList.this, "Please enter a code to search", Toast.LENGTH_SHORT).show();
                }
            }
        });

        MaterialButton addDepBtn = findViewById(R.id.depList_nav_btn);
        addDepBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, DepartmentForm.class);
            startActivity(intent);
        });

        loadDepartments();
    }

    private void loadDepartments() {
        RetrofitService retrofitService = new RetrofitService();
        // create an instance of ...Api interface using retrofit
        DepartmentApi departmentApi = retrofitService.getRetrofit().create(DepartmentApi.class);
        departmentApi.getAllDepartments()
                // make asynch network call
                .enqueue(new Callback<List<Department>>() {
                    @Override
                    public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                        populateListView(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Department>> call, Throwable t) {
                        Toast.makeText(DepartmentsList.this, "Failed to load departments", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void searchDepartment(String code) {
        RetrofitService retrofitService = new RetrofitService();
        DepartmentApi departmentApi = retrofitService.getRetrofit().create(DepartmentApi.class);
        departmentApi.getDepartmentByCode(code)
                .enqueue(new Callback<Department>() {
                    @Override
                    public void onResponse(Call<Department> call, Response<Department> response) {
                        if (response.isSuccessful()) {
                            Department department = response.body();
                            List<Department> searchResult = new ArrayList<>();
                            if (department != null) {
                                searchResult.add(department);
                            }
                            populateListView(searchResult);
                        } else {
                            Toast.makeText(DepartmentsList.this, "No department found with code: " + code, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Department> call, Throwable t) {
                        Toast.makeText(DepartmentsList.this, "Failed to search department", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateListView(List<Department> departmentList) {
        if (departmentList != null && !departmentList.isEmpty()) {
            DepartmentAdapter adapter = new DepartmentAdapter(departmentList);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No departments found", Toast.LENGTH_SHORT).show();
        }
    }
}