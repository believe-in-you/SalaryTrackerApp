package com.example.myapplication.salarytracker.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.salarytracker.R;
import com.example.myapplication.salarytracker.UserDetails.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import static com.example.myapplication.salarytracker.Dashboard.AddEmployee.EMPLOYEE_DATA;

public class IndividualEmployeeData extends AppCompatActivity {

    private EditText phoneno, post, leaves, base_salary;
    private TextView name, emailid, edu_qual;
    private Button update;
    private FirebaseFirestore db;

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

        db = FirebaseFirestore.getInstance();

        Intent in = getIntent();
        Employee emp = in.getParcelableExtra("Current Employee");


        name.setText(emp.getName());
        emailid.setText(emp.getEmailid());
        post.setText(emp.getPost());
        leaves.setText(emp.getNum_leaves());
        edu_qual.setText(emp.getEdu_qual());
        phoneno.setText(emp.getPhoneno());
        base_salary.setText(emp.getBase_salary());


        // Storing the old values, to compare with new ones while updating
        String old_phoneno = phoneno.getText().toString();
        String old_post = post.getText().toString();
        String old_leaves = leaves.getText().toString();
        String old_base_salary = base_salary.getText().toString();


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * Updating the current employee data
                 */

                String new_phoneno = phoneno.getText().toString();
                String new_post = post.getText().toString();
                String new_leaves = leaves.getText().toString();
                String new_base_salary = base_salary.getText().toString();
                if(new_phoneno.isEmpty()) new_phoneno = old_phoneno;
                if(new_post.isEmpty()) new_post = old_post;
                if(new_leaves.isEmpty()) new_leaves = old_leaves;
                if(new_base_salary.isEmpty()) new_base_salary = old_base_salary;
                final Employee newEmp = new Employee(name.getText().toString(),
                                                    emailid.getText().toString(),
                                                    new_phoneno,
                                                    new_post,
                                                    edu_qual.getText().toString(),
                                                    new_base_salary,
                                                    new_leaves,
                                                    emp.getDue_date(),
                                                    emp.getDue_month(),
                                                    emp.getDue_year());

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
}