package com.example.myapplication.salarytracker.Dashboard;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.salarytracker.Payment.DueDatesActivity;
import com.example.myapplication.salarytracker.SharedPreferences.SessionManager;
import com.example.myapplication.salarytracker.UserDetails.Admin;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.salarytracker.R;

public class HomeActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mTextView = findViewById(R.id.admin_details);
        Intent in = getIntent();
        Admin data = in.getParcelableExtra("ADMIN INFO");

        if(data!=null) {
            String disp = "Name: " + String.valueOf(data.getName()) + "\nUsername: " + String.valueOf(data.getUsername());

            mTextView.setText(disp);
        }

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Intent in;
            switch(menuItem.getItemId()) {
                case R.id.logout:
                    SessionManager mManager = new SessionManager(HomeActivity.this);
                    mManager.removeSession();
                    finish();
                    break;

                case R.id.emp_details:
                    in = new Intent(this, EmployeeDataDisplayActivity.class);
                    startActivity(in);
                    break;

                case R.id.due_details:
                    in = new Intent(this, DueDatesActivity.class);
                    startActivity(in);
                    break;
            }
            return super.onOptionsItemSelected(menuItem);
    }


}