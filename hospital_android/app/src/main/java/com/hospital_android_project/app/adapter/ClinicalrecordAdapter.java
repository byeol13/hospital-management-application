package com.hospital_android_project.app.adapter;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.hospital_android_project.app.ClinicalDataForm;
import com.hospital_android_project.app.R;
import com.hospital_android_project.app.model.Clinicalrecord;
import com.hospital_android_project.app.retrofit.ClinicalrecordApi;
import com.hospital_android_project.app.retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClinicalrecordAdapter extends RecyclerView.Adapter<ClinicalrecordAdapter.ClinicalrecordHolder>{

    private List<Clinicalrecord> clinicalrecordsList;
    private Context context;

    public ClinicalrecordAdapter(Context context) {
        this.context = context;
    }

    public void setClinicalrecordsList(List<Clinicalrecord> clinicalrecordsList) {
        this.clinicalrecordsList = clinicalrecordsList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ClinicalrecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_clinical_record_item, parent, false);
        return new ClinicalrecordHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClinicalrecordHolder holder, int position) {
        Clinicalrecord clinicalrecord = clinicalrecordsList.get(position);
        holder.clinicalDataTextView.setText(clinicalrecord.getClinicaldata());

        holder.deleteBtn.setOnClickListener(v -> showDeleteConfirmationDialog(holder.itemView.getContext(), clinicalrecord, position));

        holder.editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, ClinicalDataForm.class);
            intent.putExtra("clinicalrecord", clinicalrecord);
            intent.putExtra("patientId", clinicalrecord.getPatientid());
            intent.putExtra("patientName", "");  // Pass the patient name if available
            intent.putExtra("patientLastName", "");  // Pass the patient last name if available
            context.startActivity(intent);
        });
    }

    private void showDeleteConfirmationDialog(Context context, Clinicalrecord clinicalrecord, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Clinical Record")
                .setMessage("Do you confirm deletion?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    // User confirmed the deletion
                    deleteRecord(context, clinicalrecord, position);
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    // User canceled the deletion
                    dialog.dismiss();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteRecord(Context context, Clinicalrecord clinicalrecord, int position) {
        RetrofitService retrofitService = new RetrofitService();
        ClinicalrecordApi clinicalrecordApi = retrofitService.getRetrofit().create(ClinicalrecordApi.class);
        clinicalrecordApi.deleteClinicalrecord(clinicalrecord.getClinicaldata())
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            clinicalrecordsList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, clinicalrecordsList.size());
                            Toast.makeText(context, "Record deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to delete record", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return clinicalrecordsList != null ? clinicalrecordsList.size() : 0;
    }

    public class ClinicalrecordHolder extends RecyclerView.ViewHolder {

        TextView clinicalDataTextView;

        ImageView editBtn, deleteBtn;

        public ClinicalrecordHolder(@NonNull View itemView) {
            super(itemView);
            clinicalDataTextView = itemView.findViewById(R.id.clinicalList_data);
            deleteBtn = itemView.findViewById(R.id.btn_deleteRecord);
            editBtn = itemView.findViewById(R.id.btn_editRecord);
        }
    }
}
