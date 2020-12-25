package com.example.myapplication.salarytracker.Payment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.salarytracker.Adapters.DueDatesAdapter;
import com.example.myapplication.salarytracker.Adapters.PaymentHistoryDataAdapter;
import com.example.myapplication.salarytracker.R;
import com.example.myapplication.salarytracker.UserDetails.History;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.myapplication.salarytracker.Dashboard.AddEmployee.EMPLOYEE_DATA;

public class PaymentHistoryActivity extends AppCompatActivity {

    private String idForCurrentEmployee;
    private FirebaseFirestore db;
    public static final String HISTORY = "History";
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);

        db = FirebaseFirestore.getInstance();

        Intent in = getIntent();
        idForCurrentEmployee = in.getStringExtra("emailid");
        String name = in.getStringExtra("name");
        String base_salary = in.getStringExtra("base_salary");

        TextView emp_details = findViewById(R.id.payment_history_employee_info);

        emp_details.setText(name + "\nBase Salary: Rs " + base_salary + " / month");

        if(idForCurrentEmployee == null) {
            Toast.makeText(this, "Error getting data, try again...", Toast.LENGTH_LONG);
            finish();
        }

        db.collection(EMPLOYEE_DATA).document(idForCurrentEmployee)
                .collection(HISTORY)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<History> history = queryDocumentSnapshots.toObjects(History.class);

                        if(history.isEmpty()) {
                            Toast.makeText(PaymentHistoryActivity.this, "No Payments history found", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        Collections.sort(history, new Comparator<History>() {

                            @Override
                            public int compare(History o1, History o2) {

                                int o1_yr = Integer.parseInt(o1.getYear());
                                int o2_yr = Integer.parseInt(o2.getYear());
                                int o1_month = Integer.parseInt(o1.getMonth());
                                int o2_month = Integer.parseInt(o2.getMonth());

                                // Either year is big, or month
                                if(o1_yr != o2_yr) return o2_yr - o1_yr;
                                return o2_month - o1_month;

                            }
                        });

                        mRecyclerView = findViewById(R.id.payment_history_recycler_view);
                        PaymentHistoryDataAdapter mAdapter = new PaymentHistoryDataAdapter(PaymentHistoryActivity.this, history);
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(PaymentHistoryActivity.this));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }
}