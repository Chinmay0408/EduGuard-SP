package com.example.eduguardsp;

import android.view.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder>{

    ArrayList<Admin> list;

    public interface OnFacultyClick {
        void onClick(Admin admin);
    }

    OnFacultyClick listener;

    public AdminAdapter(ArrayList<Admin> list,
                          OnFacultyClick listener){
        this.list = list;
        this.listener = listener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, collegeName;

        public ViewHolder(View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.tvAdminName);
            collegeName = itemView.findViewById(R.id.tvColleges);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_admin,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,
                                 int position) {

        Admin admin= list.get(position);

        holder.name.setText(admin.name);
        holder.collegeName.setText("College: " + admin.collegeName);

        holder.itemView.setOnClickListener(v -> {
            listener.onClick(admin);
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
