package com.example.funnel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class EditSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_summary);

        String summaryPath = getIntent().getStringExtra("summaryPath");
        Uri imageUri = Uri.parse(getIntent().getStringExtra("imageUri"));
        String currentSummary = getIntent().getStringExtra("currentSummary");

        ImageView snippetImage = findViewById(R.id.snippetImage);
        EditText newSummary = findViewById(R.id.editNewSummary);
        Button backButton = findViewById(R.id.editBackButton);
        Button submitButton = findViewById(R.id.submitSummaryButton);

        Picasso.with(snippetImage.getContext())
                .load(imageUri)
                .noPlaceholder()
                .fit().centerInside()
                .into(snippetImage);

        newSummary.setText(currentSummary);

        backButton.setOnClickListener(view -> {
            Intent returnIntent = new Intent(EditSummaryActivity.this, HomeActivity.class);
            startActivity(returnIntent);
        });

    }
}