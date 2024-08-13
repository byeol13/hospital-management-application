package com.hospital_android_project.app.retrofit;

import com.hospital_android_project.app.model.AdmissionState;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AdmissionStateApi {
    @GET("/api/admissionstate/{patientid}")
    Call<AdmissionState> findByPatientIdAndExitingDateIsNull(@Path("patientid") Long patientid);

    @PUT("/api/admissionstateUpd/{patientid}")
    Call<AdmissionState> updateReason(@Path("patientid") Long patientid, @Body AdmissionState admissionState);

    @PUT("/api/admissionstateAdmit/{patientid}")
    Call<AdmissionState> admitPatient(@Path("patientid") Long patientid, @Body AdmissionState admissionState);

    @GET("/api/admissionstateByPId/{patientid}")
    Call<List<AdmissionState>> getAAdmissionnByPatientid(@Path("patientid") Long patientid);
}
