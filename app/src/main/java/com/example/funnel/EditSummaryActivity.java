package com.example.funnel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class EditSummaryActivity extends AppCompatActivity {

    private String currentSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_summary);

        Uri imageUri = Uri.parse(getIntent().getStringExtra("imageUri"));
        currentSummary = getIntent().getStringExtra("currentSummary");

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
            returnHome();
        });

        submitButton.setOnClickListener(view -> {
            String userId = getIntent().getStringExtra("userId");
            String groupName = getIntent().getStringExtra("groupName");
            String imageName = getIntent().getStringExtra("imageName");
            submitSummary(newSummary.getText().toString(), userId, groupName, imageName);
        });

    }

    private void returnHome() {
        Intent returnIntent = new Intent(EditSummaryActivity.this, HomeActivity.class);
        startActivity(returnIntent);
    }

    private void submitSummary(String newSummary, String userId, String groupName, String imageName) {
        if (newSummary.equals(currentSummary)) {
            return;
        }
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child(userId)
                .child(groupName)
                .child(imageName)
                .child("summary")
                .setValue(newSummary);
        returnHome();
    }
}