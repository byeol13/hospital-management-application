package com.hospital_android_project.app.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hospital_android_project.app.DepartmentForm;
import com.hospital_android_project.app.R;
import com.hospital_android_project.app.model.Clinicalrecord;
import com.hospital_android_project.app.model.Department;
import com.hospital_android_project.app.retrofit.DepartmentApi;
import com.hospital_android_project.app.retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.DepartmentHolder> {

    private List<Department> departmentList;
    public DepartmentAdapter(List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    @NonNull
    @Override
    public DepartmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_dep_item, parent, false);
        return new DepartmentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentHolder holder, int position) {
        Department department = departmentList.get(position);
        holder.code.setText(department.getCode());
        holder.name.setText(department.getName());

        holder.btn_delete.setOnClickListener(v -> showDeleteConfirmationDialog(holder.itemView.getContext(), department, position));

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DepartmentForm.class);
                intent.putExtra("department", department);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return departmentList.size();
    }

    private void delDepartment(Context context, Department department, int position) {
        RetrofitService retrofitService = new RetrofitService();
        DepartmentApi departmentApi = retrofitService.getRetrofit().create(DepartmentApi.class);
        departmentApi.deleteDepartment(department.getCode())
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            departmentList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, departmentList.size());
                            Toast.makeText(context, "Department deleted", Toast.LENGTH_SHORT).show();
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

    private void showDeleteConfirmationDialog(Context context, Department department, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete department?")
                .setMessage("This will delete the department.")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    // User confirmed the deletion
                    delDepartment(context, department, position);
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    // User canceled the deletion
                    dialog.dismiss();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    public class DepartmentHolder extends RecyclerView.ViewHolder {

        TextView code, name;
        ImageView btn_delete, btn_edit;

        public DepartmentHolder(@NonNull View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.depListItem_code);
            name = itemView.findViewById(R.id.depListItem_name);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            btn_edit = itemView.findViewById(R.id.btn_edit);
        }
    }
}
