package com.example.myapplication.salarytracker.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.salarytracker.Dashboard.EmployeeDataDisplayActivity;
import com.example.myapplication.salarytracker.Dashboard.IndividualEmployeeData;
import com.example.myapplication.salarytracker.R;
import com.example.myapplication.salarytracker.UserDetails.Employee;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class EmployeeDataAdapter extends RecyclerView.Adapter<EmployeeDataAdapter.EmployeeDataViewHolder> {

    Context context;
    FirebaseFirestore db;
    List<Employee> emp_data;

    public EmployeeDataAdapter(Context context, List<Employee> emp_data) {
        db = FirebaseFirestore.getInstance();
        this.context = context;
        this.emp_data = emp_data;

    }

    @NonNull
    @Override
    public EmployeeDataAdapter.EmployeeDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflator = LayoutInflater.from(context);
        View view = inflator.inflate(R.layout.emp_data_cardview, parent, false);
        return new EmployeeDataViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull EmployeeDataAdapter.EmployeeDataViewHolder holder, int position) {
        holder.name.setText(emp_data.get(position).getName());
        holder.post.setText(emp_data.get(position).getPost());
        holder.base_salary.setText("Rs " + emp_data.get(position).getBase_salary() + " / month");

        holder.leaves.setText("Leaves this month: " + emp_data.get(position).getNum_leaves());

        // Giving clicking behaviour to layout to view/edit data of an employee
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, IndividualEmployeeData.class);
                in.putExtra("Current Employee", emp_data.get(position));
                Log.e("Updated unpaid amount", emp_data.get(position).getUnpaid_leaves_amount());
                context.startActivity(in);
            }
        });

    }


    @Override
    public int getItemCount() {
        return emp_data.size();
    }

    public static class EmployeeDataViewHolder extends RecyclerView.ViewHolder {

        public TextView name, post, base_salary, leaves;
        public LinearLayout layout;

        public EmployeeDataViewHolder(@NonNull View itemView) {
            super(itemView);
            try {

                name = itemView.findViewById(R.id.emp_cardview_name);
                post = itemView.findViewById(R.id.emp_cardview_post);
                base_salary = itemView.findViewById(R.id.emp_cardview_basesalary);
                leaves = itemView.findViewById(R.id.emp_cardview_leave);
                layout = itemView.findViewById(R.id.emp_cardview_layout);

            }
            catch (Exception e) {
                Log.e("itemViewERROR", e.getMessage());
            }

        }
    }


}
