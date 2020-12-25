package com.example.myapplication.salarytracker.Payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.myapplication.salarytracker.R;

public class DueDatesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_dates);
        Toolbar toolbar = findViewById(R.id.due_dates_toolbar);
        setSupportActionBar(toolbar);



    }
}