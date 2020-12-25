package com.example.myapplication.salarytracker.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.salarytracker.Payment.DueDatesActivity;
import com.example.myapplication.salarytracker.R;
import com.example.myapplication.salarytracker.UserDetails.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.myapplication.salarytracker.Dashboard.AddEmployee.EMPLOYEE_DATA;
import static java.util.Map.entry;

public class DueDatesAdapter extends RecyclerView.Adapter<DueDatesAdapter.DueDatesViewHolder>{

    Context context;
    FirebaseFirestore db;
    List<Employee> emp;

    final public static Map<Integer, String> MONTH_MAPPING = Map.ofEntries(
            entry(0, "Jan"),
            entry(1, "Feb"),
            entry(2, "Mar"),
            entry(3, "Apr"),
            entry(4, "May"),
            entry(5, "Jun"),
            entry(6, "Jul"),
            entry(7, "Aug"),
            entry(8, "Sep"),
            entry(9, "Oct"),
            entry(10, "Nov"),
            entry(11, "Dec")
    );

    public DueDatesAdapter(Context context, List<Employee> emp) {
        this.context = context;
        this.emp = emp;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public DueDatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.due_dates_card_view, parent, false);

        return new DueDatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DueDatesViewHolder holder, int position) {

        Employee curr_emp = emp.get(position);
        holder.name.setText(curr_emp.getName());
        holder.post.setText(curr_emp.getPost());

        int due_date = curr_emp.getDue_date();
        int due_month = curr_emp.getDue_month();
        int due_year = curr_emp.getDue_year();

        String due = "Due: " + due_date + " " + MONTH_MAPPING.get(due_month) + " " + due_year;
        holder.due.setText(due);

        Calendar calendar = Calendar.getInstance();
        int curr_date = calendar.get(Calendar.DAY_OF_MONTH);
        int curr_month = calendar.get(Calendar.MONTH);
        int curr_year = calendar.get(Calendar.YEAR);

        if((curr_month > due_month && curr_year >= due_year) || (curr_month < due_month && curr_year > due_year)) {
            holder.pending_tag.setVisibility(View.VISIBLE);
        }
        else if(curr_date == due_date) {
            int curr_hour = calendar.get(Calendar.HOUR_OF_DAY); // Hour in 24hr format

            // We consider 09:00 AM as our deadline time
            if(curr_hour >= 9) {
                holder.pending_tag.setVisibility(View.VISIBLE);
            }
        }

        //(new DueDatesActivity()).recreateActivity();

        holder.layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(context);
                mAlertDialog.setTitle(holder.name.getText().toString());
                int amnt_to_pay = Integer.parseInt(curr_emp.getBase_salary()) - Integer.parseInt(curr_emp.getNum_leaves()) * 300;
                String amount = String.valueOf(amnt_to_pay);
                mAlertDialog.setMessage("Amount to be paid is Rs " + amount);

                mAlertDialog.setPositiveButton("Paid", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String emailid = curr_emp.getEmailid();

                        int next_due_month = (due_month+1) % 12;
                        int next_due_year = next_due_month==0? due_year+1: due_year;
                        Calendar newCal = Calendar.getInstance();
                        newCal.set(next_due_year, next_due_month, 1);
                        int next_due_date = newCal.getActualMaximum(Calendar.DAY_OF_MONTH);

                        final Employee modified_emp = curr_emp;
                        modified_emp.setDue_date(next_due_date);     // Setting the new due date for payment
                        modified_emp.setDue_month(next_due_month);
                        modified_emp.setDue_year(next_due_year);
                        modified_emp.setNum_leaves("0");             // New number of leaves for this month = 0

                        db.collection(EMPLOYEE_DATA)
                                .whereEqualTo("emailid", curr_emp.getEmailid())
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        String id = queryDocumentSnapshots.getDocuments().get(0).getId();
                                        DocumentReference ob = db.collection(EMPLOYEE_DATA).document(id);

                                        ob.set(modified_emp);
                                        holder.pending_tag.setVisibility(View.INVISIBLE);
                                        Toast.makeText(context, "Updated!!", Toast.LENGTH_LONG).show();
                                        // Refreshing the Screen to reflect the changes
                                        Intent in = new Intent(context, DueDatesActivity.class);
                                        context.startActivity(in);

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {

                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Cannot update data now, try later...", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                });

                mAlertDialog.setNegativeButton("Not yet paid", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Pay salary before due date", Toast.LENGTH_SHORT).show();
                    }
                });

                mAlertDialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return emp.size();
    }

    public static class DueDatesViewHolder extends RecyclerView.ViewHolder {

        public TextView name, post, due, pending_tag;
        public LinearLayout layout;

        public DueDatesViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.due_cardview_name);
            post = itemView.findViewById(R.id.due_cardview_post);
            due = itemView.findViewById(R.id.due_cardview_duedate);
            pending_tag = itemView.findViewById(R.id.due_cardview_pending_tag);
            layout = itemView.findViewById(R.id.due_cardview_layout);
        }
    }
}
