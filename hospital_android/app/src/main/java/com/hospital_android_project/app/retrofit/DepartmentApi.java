package com.hospital_android_project.app.retrofit;


import com.hospital_android_project.app.model.Department;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DepartmentApi {
    @GET("/api/departments")
    Call<List<Department>> getAllDepartments();

    @POST("/api/department")
    Call<Department> createDepartment(@Body Department department);

    @PUT("/api/department/{code}")
    Call<Department> updateDepartment(@Path("code") String code, @Body Department updatedDepartment);

    @GET("/api/department/{code}")
    Call<Department> getDepartmentByCode(@Path("code") String code);

    @DELETE("/api/{code}")
    Call<Void> deleteDepartment(@Path("code") String code);
}
