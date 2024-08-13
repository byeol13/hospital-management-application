package com.hospital_android_project.app.retrofit;

import com.hospital_android_project.app.model.Patient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PatientApi {

    @POST("/api/patient")
    Call<Patient> createPatient(@Body Patient patient);

    @GET("/api/patient/{id}")
    Call<Patient> getPatientByName(@Path("id") Long id);

    @PUT("/api/patient/{id}")
    Call<Patient> updatePatient(@Path("id") Long id, @Body Patient patient);

    @GET("/api/patients")
    Call<List<Patient>> getAllPatients();

    @DELETE("/api/patientDlt/{id}")
    Call<Void> deletePatient(@Path("id") Long id);
}
