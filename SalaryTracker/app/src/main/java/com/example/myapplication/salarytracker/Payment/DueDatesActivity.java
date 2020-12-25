package com.example.myapplication.salarytracker.Payment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.salarytracker.Adapters.DueDatesAdapter;
import com.example.myapplication.salarytracker.Adapters.EmployeeDataAdapter;
import com.example.myapplication.salarytracker.Dashboard.EmployeeDataDisplayActivity;
import com.example.myapplication.salarytracker.R;
import com.example.myapplication.salarytracker.UserDetails.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.myapplication.salarytracker.Dashboard.AddEmployee.EMPLOYEE_DATA;

public class DueDatesActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_dates);

        db = FirebaseFirestore.getInstance();

        // Getting all employee data and populating the due_date recycler view
        db.collection(EMPLOYEE_DATA)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(!task.isSuccessful() || task.getResult().isEmpty()) {
                            Toast.makeText(DueDatesActivity.this, "No employee data found...", Toast.LENGTH_SHORT).show();
                            finish(); // TAKE CARE OF THIS LATER
                        }
                        else {
                            // Employee data found, we can add now
                            List<Employee> emp = task.getResult().toObjects(Employee.class);

                            Collections.sort(emp, new Comparator<Employee>() {

                                        @Override
                                        public int compare(Employee o1, Employee o2) {
                                            if(o1.getDue_month() > o2.getDue_month() && o1.getDue_year() <= o2.getDue_month()) return 1;
                                            else if(o1.getDue_month() < o2.getDue_month() && o1.getDue_year() <= o2.getDue_month()) return 1;

                                            return o1.getName().compareTo(o2.getName());
                                        }


                            });
                            mRecyclerView = findViewById(R.id.due_dates_recycler_view);
                            DueDatesAdapter mAdapter = new DueDatesAdapter(DueDatesActivity.this, emp);
                            mRecyclerView.setAdapter(mAdapter);
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(DueDatesActivity.this));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DueDatesActivity.this, "Cannot fetch data at moment, try again...", Toast.LENGTH_LONG).show();
                    }
                });

    }

    public void recreateActivity() {
        this.recreate();
    }

}