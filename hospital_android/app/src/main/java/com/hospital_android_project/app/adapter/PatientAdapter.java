package com.hospital_android_project.app.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hospital_android_project.app.AdmissionDepList;
import com.hospital_android_project.app.DischargePatient;
import com.hospital_android_project.app.PatientClinicalRecords;
import com.hospital_android_project.app.PatientForm;
import com.hospital_android_project.app.R;
import com.hospital_android_project.app.model.AdmissionState;
import com.hospital_android_project.app.model.Department;
import com.hospital_android_project.app.model.Patient;
import com.hospital_android_project.app.retrofit.AdmissionStateApi;
import com.hospital_android_project.app.retrofit.PatientApi;
import com.hospital_android_project.app.retrofit.RetrofitService;

import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientHolder> {

    private Context context;
    private List<Patient> patientList;

    public PatientAdapter(Context context, List<Patient> patientList) {
        this.context = context;
        this.patientList = patientList;
    }

    @NonNull
    @Override
    public PatientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_patient_item, parent, false);
        return new PatientHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientAdapter.PatientHolder holder, int position) {
        Patient patient = patientList.get(position);
        holder.name.setText(patient.getName());
        holder.lastname.setText(patient.getLastname());
        holder.id_patient.setText(String.valueOf(patient.getId()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(patient.getBirthdate());
        holder.birthdate.setText(formattedDate);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PatientClinicalRecords.class);
            intent.putExtra("patientId", patient.getId());
            intent.putExtra("patientName", patient.getName());
            intent.putExtra("patientLastName", patient.getLastname());
            context.startActivity(intent);
        });

        holder.btn_delete.setOnClickListener(v -> showDeleteConfirmationDialog(holder.itemView.getContext(), patient, position));

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PatientForm.class);
                intent.putExtra("patient", patient);
                view.getContext().startActivity(intent);
            }
        });

        holder.btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.btn_more);
                popupMenu.inflate((R.menu.popup_menu));
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.menu_discharge) {
                            Intent intent = new Intent(context, DischargePatient.class);
                            intent.putExtra("patientId", patient.getId());
                            context.startActivity(intent);
                            return true;
                        } else if (menuItem.getItemId() == R.id.menu_next) {
                            Intent intent = new Intent(context, AdmissionDepList.class);
                            intent.putExtra("patientId", patient.getId());
                            context.startActivity(intent);
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });

//        holder.btn_more.setOnClickListener(view -> {
//            PopupMenu popupMenu = new PopupMenu(context, holder.btn_more);
//            popupMenu.inflate((R.menu.popup_menu));
//            popupMenu.setOnMenuItemClickListener(menuItem -> {
//                if (menuItem.getItemId() == R.id.menu_discharge) {
//                    checkDischargeStatusAndProceed(patient.getId());
//                    return true;
//                } else if (menuItem.getItemId() == R.id.menu_next) {
//                    Intent intent = new Intent(context, AdmissionDepList.class);
//                    intent.putExtra("patientId", patient.getId());
//                    context.startActivity(intent);
//                    return true;
//                } else {
//                    return false;
//                }
//            });
//            popupMenu.show();
//        });

    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

//    private void checkDischargeStatusAndProceed(Long patientId) {
//        RetrofitService retrofitService = new RetrofitService();
//        AdmissionStateApi admissionStateApi = retrofitService.getRetrofit().create(AdmissionStateApi.class);
//        admissionStateApi.findByPatientIdAndExitingDateIsNull(patientId)
//                .enqueue(new Callback<AdmissionState>() {
//                    @Override
//                    public void onResponse(Call<AdmissionState> call, Response<AdmissionState> response) {
//                        if (response.isSuccessful() && response.body() != null) {
//                            AdmissionState admissionState = response.body();
//                            if (admissionState.isDischarge()) {
//                                alertDischargeStatus();
//                            } else {
//                                Intent intent = new Intent(context, DischargePatient.class);
//                                intent.putExtra("patientId", patientId);
//                                context.startActivity(intent);
//                            }
//                        } else {
//                            Toast.makeText(context, "Failed to retrieve patient status", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<AdmissionState> call, Throwable t) {
//                        Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

    private void deletePatient(Context context, Patient patient, int position) {
        RetrofitService retrofitService = new RetrofitService();
        PatientApi patientApi = retrofitService.getRetrofit().create(PatientApi.class);
        patientApi.deletePatient(patient.getId())
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            patientList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, patientList.size());
                            Toast.makeText(context, "Patient deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showDeleteConfirmationDialog(Context context, Patient patient, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete patient?")
                .setMessage("This will delete the patient.")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    // User confirmed the deletion
                    deletePatient(context, patient, position);
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    // User canceled the deletion
                    dialog.dismiss();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public class PatientHolder extends RecyclerView.ViewHolder {

        TextView name, lastname, birthdate, id_patient;

        ImageView btn_delete, btn_edit, btn_more;

        public PatientHolder(@NonNull View itemView) {
            super(itemView);
            id_patient = itemView.findViewById(R.id.patientListItem_id);
            name = itemView.findViewById(R.id.patientListItem_name);
            lastname = itemView.findViewById(R.id.patientListItem_lastname);
            birthdate = itemView.findViewById(R.id.patientListItem_birthdate);
            btn_delete = itemView.findViewById(R.id.btn_deletePatient);
            btn_edit = itemView.findViewById(R.id.btn_editPatient);
            btn_more = itemView.findViewById(R.id.btn_more);
        }
    }
}
