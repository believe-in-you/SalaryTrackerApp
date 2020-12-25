package com.example.myapplication.salarytracker.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.se.omapi.Session;
import android.security.identity.SessionTranscriptMismatchException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.salarytracker.Dashboard.HomeActivity;
import com.example.myapplication.salarytracker.R;
import com.example.myapplication.salarytracker.SharedPreferences.SessionManager;
import com.example.myapplication.salarytracker.UserDetails.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import static com.example.myapplication.salarytracker.LoginSignup.SignupActivity.ADMIN_LOGIN_COLLECTION;

public class LoginActivity extends AppCompatActivity {

    private EditText loginUsername, loginPassword;
    private Button loginButton;
    private TextView loginSignup;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        loginSignup = findViewById(R.id.login_signup);

        checkIfLoggedIn();

        db = FirebaseFirestore.getInstance();

    }

    private void checkIfLoggedIn() {
        // We check if user is logged in, if so, directly we move to Dashboard
        SessionManager mManager = new SessionManager(this);
        Admin loggedInUser = mManager.getSession();

        // If returned value is not NA, means already user is logged in, so we directly move to dashboard.
        if(loggedInUser != null) {
            Intent in = new Intent(this, HomeActivity.class);
            in.putExtra("ADMIN INFO", loggedInUser);

            startActivity(in);
        }
    }

    public void onClickSignup(View view) {

        Intent in = new Intent(this, SignupActivity.class);
        startActivity(in);

    }


    public void onClickLogin(View view) {

        final String username = loginUsername.getText().toString();
        final String password = loginPassword.getText().toString();

        db.collection(ADMIN_LOGIN_COLLECTION)
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().isEmpty()) {
                            Toast.makeText(LoginActivity.this, "Invalid username", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            // Checking for validity of password
                            List<Admin> data = task.getResult().toObjects(Admin.class);

                            // Since only one admin for one username
                            Admin curr_admin = data.get(0);

                            if(curr_admin.getPassword().equals(password)) {
                                // Correct password

                                // Saving to Shared Preferences
                                SessionManager mManager =new SessionManager(LoginActivity.this);
                                mManager.saveSession(curr_admin);

                                //Proceed to next screen
                                Intent in = new Intent(LoginActivity.this, HomeActivity.class);
                                in.putExtra("ADMIN INFO", curr_admin);

                                startActivity(in);

                            }

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Some error has occurred, try again...", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}