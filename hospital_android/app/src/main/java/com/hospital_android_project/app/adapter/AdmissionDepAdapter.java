package com.hospital_android_project.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.hospital_android_project.app.Admit_Form;
import com.hospital_android_project.app.MoveTo_Form;
import com.hospital_android_project.app.R;
import com.hospital_android_project.app.model.Department;

import java.util.List;

public class AdmissionDepAdapter extends RecyclerView.Adapter<AdmissionDepAdapter.AdmissionDepViewHolder> {

    private List<Department> admitDepList;
    private Context context;
    private Long patientId;


    public AdmissionDepAdapter(Context context, List<Department> admitDepList, Long patientId) {
        this.context = context;
        this.admitDepList = admitDepList;
        this.patientId = patientId;

    }

    @NonNull
    @Override
    public AdmissionDepAdapter.AdmissionDepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admit_dep_list_item, parent, false);
        return new AdmissionDepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdmissionDepAdapter.AdmissionDepViewHolder holder, int position) {
        Department department = admitDepList.get(position);
        holder.departmentName.setText(department.getName());
        holder.departmentCode.setText(department.getCode());

        holder.admitButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, Admit_Form.class);
            intent.putExtra("departmentId", department.getId());
            intent.putExtra("patientId", patientId);
            context.startActivity(intent);
        });

        holder.moveToButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, MoveTo_Form.class);
            intent.putExtra("departmentId", department.getId());
            intent.putExtra("patientId", patientId);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return admitDepList.size();
    }

    public class AdmissionDepViewHolder extends RecyclerView.ViewHolder {

        TextView departmentName, departmentCode;
        MaterialButton admitButton, moveToButton;

        public AdmissionDepViewHolder(@NonNull View itemView) {
            super(itemView);
            departmentCode = itemView.findViewById(R.id.depListItem_code);
            departmentName = itemView.findViewById(R.id.depListItem_name);
            admitButton = itemView.findViewById(R.id.admit_btn);
            moveToButton = itemView.findViewById(R.id.moveTo_btn);
        }
    }
}
