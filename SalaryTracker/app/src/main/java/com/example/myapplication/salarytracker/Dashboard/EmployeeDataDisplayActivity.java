package com.example.myapplication.salarytracker.Dashboard;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.salarytracker.Adapters.EmployeeDataAdapter;
import com.example.myapplication.salarytracker.SharedPreferences.SessionManager;
import com.example.myapplication.salarytracker.UserDetails.Admin;
import com.example.myapplication.salarytracker.UserDetails.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.salarytracker.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.myapplication.salarytracker.Dashboard.AddEmployee.EMPLOYEE_DATA;

public class EmployeeDataDisplayActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_data_display);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent in = new Intent(EmployeeDataDisplayActivity.this, AddEmployee.class);
            startActivity(in);
            finish();
        });

        // Now we have to fetch all the data from employee

        db.collection(EMPLOYEE_DATA)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(!task.isSuccessful() || task.getResult().isEmpty()) {
                            Toast.makeText(EmployeeDataDisplayActivity.this, "No employee data present", Toast.LENGTH_LONG).show();
                        }
                        else {
                            // Num of Employee > 0, hence we need to display details
                            List<Employee> emp_data = task.getResult().toObjects(Employee.class);

                            Collections.sort(emp_data, new Comparator<Employee>() {

                                @Override
                                public int compare(Employee o1, Employee o2) {
                                    String s1 = o1.getName().toLowerCase();
                                    String s2 = o2.getName().toLowerCase();

                                    return s1.compareTo(s2);
                                }
                            });

                            // We have data to populate the recycler view, so we create one
                            mRecyclerView = findViewById(R.id.employee_recycler_view);
                            EmployeeDataAdapter mAdapter = new EmployeeDataAdapter(EmployeeDataDisplayActivity.this, emp_data);
                            mRecyclerView.setAdapter(mAdapter);
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(EmployeeDataDisplayActivity.this));

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EmployeeDataDisplayActivity.this, "Error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Intent in = new Intent(EmployeeDataDisplayActivity.this, HomeActivity.class);
                        startActivity(in);
                    }
                });


    }

    /**
     * Have to add CreateOptionsMenu to toggle between screens
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_emp_data_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch(menuItem.getItemId()) {

            case R.id.to_home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}