package com.example.myapplication.salarytracker.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.salarytracker.Payment.PaymentHistoryActivity;
import com.example.myapplication.salarytracker.R;
import com.example.myapplication.salarytracker.UserDetails.Employee;
import com.example.myapplication.salarytracker.UserDetails.History;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import static com.example.myapplication.salarytracker.Dashboard.AddEmployee.EMPLOYEE_DATA;

public class    IndividualEmployeeData extends AppCompatActivity {

    private EditText phoneno, post, leaves, base_salary, unpaid_leaves;
    private TextView name, emailid, edu_qual;
    private Button update;
    private FirebaseFirestore db;
    private Employee emp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_employee_data);

        phoneno = findViewById(R.id.ind_phone);
        post = findViewById(R.id.ind_post);
        leaves = findViewById(R.id.ind_leaves);
        name = findViewById(R.id.ind_name);
        emailid = findViewById(R.id.ind_emailid);
        edu_qual = findViewById(R.id.ind_edu_qual);
        update = findViewById(R.id.ind_update);
        base_salary = findViewById(R.id.ind_base_salary);
        unpaid_leaves = findViewById(R.id.ind_unpaid_leaves);

        db = FirebaseFirestore.getInstance();

        Intent in = getIntent();
        emp = in.getParcelableExtra("Current Employee");


        name.setText(emp.getName());
        emailid.setText(emp.getEmailid());
        post.setHint(emp.getPost());
        leaves.setHint(emp.getNum_leaves());
        edu_qual.setText(emp.getEdu_qual());
        phoneno.setHint(emp.getPhoneno());
        base_salary.setHint(emp.getBase_salary());
        unpaid_leaves.setHint(emp.getUnpaid_leaves_amount());



        // Storing the old values, to compare with new ones while updating
        String old_phoneno = emp.getPhoneno();
        String old_post = emp.getPost();
        String old_leaves = emp.getNum_leaves();
        String old_base_salary = emp.getBase_salary();
        String old_unpaid_leaves_amount = emp.getUnpaid_leaves_amount();


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * Updating the current employee data
                 */

                String new_phoneno = phoneno.getText().toString();
                String new_post = post.getText().toString();
                String new_leaves = leaves.getText().toString();
                String new_unpaid_leaves_amount = unpaid_leaves.getText().toString();
                String new_base_salary = base_salary.getText().toString();

                if(new_phoneno.isEmpty()) new_phoneno = old_phoneno;
                if(new_post.isEmpty()) new_post = old_post;
                if(new_leaves.isEmpty()) new_leaves = old_leaves;
                if(new_base_salary.isEmpty()) new_base_salary = old_base_salary;
                if(new_unpaid_leaves_amount.isEmpty()) new_unpaid_leaves_amount = old_unpaid_leaves_amount;
//                Log.e("Updated unpaid amount", new_unpaid_leaves_amount);
                int due_date = emp.getDue_date();
                int due_month = emp.getDue_month();
                int due_year = emp.getDue_year();
                Log.e("VALUES: ", String.valueOf(due_date) + " " + String.valueOf(due_month) +  " " + String.valueOf(due_year));
                final Employee newEmp = new Employee(name.getText().toString(),
                                                    emailid.getText().toString(),
                                                    new_phoneno,
                                                    new_post,
                                                    edu_qual.getText().toString(),
                                                    new_base_salary,
                                                    new_leaves,
                                                    due_date,
                                                    due_month,
                                                    due_year,
                                                    new_unpaid_leaves_amount
                );

                db.collection(EMPLOYEE_DATA)
                        .whereEqualTo("emailid", emailid.getText().toString())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                String id = queryDocumentSnapshots.getDocuments().get(0).getId();
                                DocumentReference ob = db.collection(EMPLOYEE_DATA).document(id);

                                ob.set(newEmp);

                                Toast.makeText(IndividualEmployeeData.this, "Data updated successfully...", Toast.LENGTH_SHORT).show();

                                Intent in = new Intent(IndividualEmployeeData.this, EmployeeDataDisplayActivity.class);
                                startActivity(in);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {

                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(IndividualEmployeeData.this, "Cannot update data, try again", Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(IndividualEmployeeData.this, EmployeeDataDisplayActivity.class);
                                startActivity(in);
                            }
                        });

            }
        });

    }

    public void onPaymentsHistory(View view) {
        Intent in = new Intent(this, PaymentHistoryActivity.class);
        in.putExtra("emailid", emp.getEmailid());
        in.putExtra("name", emp.getName());
        in.putExtra("base_salary", emp.getBase_salary());

        startActivity(in);


    }

}