package com.hospital_android_project.app.retrofit;

import com.hospital_android_project.app.model.Clinicalrecord;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ClinicalrecordApi {
    @POST("/api/clinicalrecord")
    Call<Clinicalrecord> createClinicalrecord(@Body Clinicalrecord clinicalrecord);

    @GET("api/clinicalrecord/{clinicaldata}")
    Call<Clinicalrecord> getClinicalrecordByClinicaldata(@Path("clinicaldata") String clinicaldata);

    @GET("/api/clinicalrecordById/{patientid}")
    Call<List<Clinicalrecord>> getClinicalrecordByPatientid(@Path("patientid") Long patientid);

    @PUT("/api/clinicalrecord/{clinicaldata}")
    Call<Clinicalrecord> updateClinicalrecord(@Body Clinicalrecord updatedClinicalRecord, @Path("clinicaldata") String clinicaldata);

    @GET("/api/clinicalrecords")
    Call<List<Clinicalrecord>> getAllClinicalrecords();

    @DELETE("/api/clinicalrecordDl/{clinicaldata}")
    Call<Void> deleteClinicalrecord(@Path("clinicaldata") String clinicaldata);
}
