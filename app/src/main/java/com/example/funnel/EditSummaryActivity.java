package com.example.funnel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class EditSummaryActivity extends AppCompatActivity {

    private String summaryPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_summary);
        summaryPath = getIntent().getStringExtra("summaryPath");
    }
}