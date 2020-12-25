package com.example.myapplication.salarytracker.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.salarytracker.LoginSignup.SignupActivity;
import com.example.myapplication.salarytracker.R;
import com.example.myapplication.salarytracker.UserDetails.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class AddEmployee extends AppCompatActivity {

    private EditText mName, mEmailid, mPhoneno, mPost, mEduQual, mBaseSalary;
    private FirebaseFirestore db;
    public static final String EMPLOYEE_DATA = "Employee Details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        mName = findViewById(R.id.emp_name);
        mEmailid = findViewById(R.id.emp_emailid);
        mPhoneno = findViewById(R.id.emp_phoneno);
        mPost = findViewById(R.id.emp_post);
        mEduQual = findViewById(R.id.emp_edu_qual);
        mBaseSalary = findViewById(R.id.emp_base_salary);

        db = FirebaseFirestore.getInstance();

    }

    public void onClickRegister(View view) {

        String name = mName.getText().toString();
        String emailid = mEmailid.getText().toString();
        String phoneno = mPhoneno.getText().toString();
        String post = mPost.getText().toString();
        String edu_qual = mEduQual.getText().toString();
        String base_salary = mBaseSalary.getText().toString();

        if (name.isEmpty() || emailid.isEmpty() || phoneno.isEmpty() ||
                post.isEmpty() || edu_qual.isEmpty() || base_salary.isEmpty()) {

            // None of positions should be empty...
            Toast.makeText(this, "None of the fields should be left empty", Toast.LENGTH_SHORT).show();
        }

        /**
         * INSERT DATA INTO EMPLOYEE COLLECTION NOW
         */
        Calendar calendar = Calendar.getInstance();
        int curr_date = calendar.get(Calendar.DAY_OF_MONTH);
        int max_date = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int curr_month = calendar.get(Calendar.MONTH);
        int curr_year = calendar.get(Calendar.YEAR);
        final Employee employee = new Employee(name, emailid, phoneno, post, edu_qual, base_salary, "0", max_date, curr_month, curr_year);
        Log.e("VALUES DATES INSERT: ", max_date + " " + curr_month + " " + curr_year);

        // Checking if this employee exists in database, checking by email ID
        db.collection(EMPLOYEE_DATA)
                .whereEqualTo("emailid", emailid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            Toast.makeText(AddEmployee.this, "Employee with this emailID exists, " +
                                    "try with another one, or check for duplicates", Toast.LENGTH_LONG).show();
                        } else {
                            // We can add this employee

                            db.collection(EMPLOYEE_DATA)
                                    .document(emailid)
                                    .set(employee)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {

                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddEmployee.this, "Employee Data appended successfully", Toast.LENGTH_SHORT).show();
                                            Intent in = new Intent(AddEmployee.this, EmployeeDataDisplayActivity.class);
                                            startActivity(in);
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {

                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AddEmployee.this, "Could not register admin, try again", Toast.LENGTH_SHORT).show();
                                            Log.e("REGISTRATION FAILED: ", "TRUE");
                                        }
                                    });



                        }
                    }
                });

    }
}