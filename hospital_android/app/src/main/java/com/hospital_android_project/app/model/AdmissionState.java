package com.hospital_android_project.app.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class AdmissionState {
    private Long id;

    private String enteringdate;
    private LocalDateTime exitingdate;
    private String cause;
    private String reason;
    private boolean discharge;
    private Long patientid;
    private Long departmentid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnteringdate() {
        return enteringdate;
    }

    public void setEnteringdate(String enteringdate) {
        this.enteringdate = enteringdate;
    }

    public LocalDateTime getExitingdate() {
        return exitingdate;
    }

    public void setExitingdate(LocalDateTime exitingdate) {
        this.exitingdate = exitingdate;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isDischarge() {
        return discharge;
    }

    public void setDischarge(boolean discharge) {
        this.discharge = discharge;
    }

    public Long getPatientid() {
        return patientid;
    }

    public void setPatientid(Long patientid) {
        this.patientid = patientid;
    }

    public Long getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(Long departmentid) {
        this.departmentid = departmentid;
    }

    @Override
    public String toString() {
        return "AdmissionState{" +
                "id=" + id +
                ", enteringdate=" + enteringdate +
                ", exitingdate=" + exitingdate +
                ", cause='" + cause + '\'' +
                ", reason='" + reason + '\'' +
                ", discharge=" + discharge +
                ", patientid=" + patientid +
                ", departmentid=" + departmentid +
                '}';
    }
}
