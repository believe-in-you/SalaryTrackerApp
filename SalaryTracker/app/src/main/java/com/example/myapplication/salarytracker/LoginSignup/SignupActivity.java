package com.example.myapplication.salarytracker.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.salarytracker.R;
import com.example.myapplication.salarytracker.UserDetails.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class SignupActivity extends AppCompatActivity {

    private EditText name, username, password, confirm_password, phoneno;
    private FirebaseFirestore db;
    public static final String ADMIN_LOGIN_COLLECTION = "Admin Login Details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.signup_name);
        username = findViewById(R.id.signup_username);
        password = findViewById(R.id.signup_password);
        confirm_password = findViewById(R.id.signup_confirm_password);
        phoneno = findViewById(R.id.signup_phoneno);
        
        db = FirebaseFirestore.getInstance();


    }

    public void onClickSignup(View view) {

        String name_val, username_val, password_val, confirm_password_val, phoneno_val;

        name_val = name.getText().toString();
        username_val = username.getText().toString();
        password_val = password.getText().toString();
        confirm_password_val = confirm_password.getText().toString();
        phoneno_val = phoneno.getText().toString();

        if(name_val.isEmpty() || username_val.isEmpty() ||
                password_val.isEmpty() || confirm_password_val.isEmpty() || phoneno_val.isEmpty()) {

            // Display toast that ID cannot be created
            Toast.makeText(this, "No field can be left empty!!", Toast.LENGTH_LONG).show();
            return;
        }
        else if(!password_val.equals(confirm_password_val)) {
            // Both Password and confirm password must be same
            Toast.makeText(this, "Passwords in both fields do not match", Toast.LENGTH_LONG).show();
            return;
        }

        // Valid Details, can be inserted in Database

        final Admin admin = new Admin(name_val, username_val, password_val, phoneno_val);

        // Checking if admin details already exist in database
        db.collection(ADMIN_LOGIN_COLLECTION)
                .whereEqualTo("username", username_val)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult().size() > 0) {
                            Toast.makeText(SignupActivity.this, "Admin already registered with same username", Toast.LENGTH_LONG).show();
                            Log.e("ADMIN PRESENT: ", "TRUE");
                        }
                        else {
                            // Username is unique, we can create new ID safely
                            db.collection(ADMIN_LOGIN_COLLECTION)
                                    .add(admin)
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            // Admin registered successfully
                                            Toast.makeText(SignupActivity.this, "Admin Registered Successfully", Toast.LENGTH_SHORT).show();
                                            finish(); // Returning to login page
                                        }


                                    })
                                    .addOnFailureListener(new OnFailureListener() {

                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignupActivity.this, "Could not register admin, try again", Toast.LENGTH_SHORT).show();
                                            Log.e("REGISTRATION FAILED: ", "TRUE");
                                        }
                                    });

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupActivity.this, "Some error occurred, try again...", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}