package com.example.funnel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.snippets_content_fragment, RecycleSnippetsFragment.class, null)
                    .add(R.id.select_group_fragment, SelectGroupFragment.class, null)
                    .commit();
        }

        Button logoutButton = findViewById(R.id.logout_button);
        Button uploadButton = findViewById(R.id.uploadButton);

        logoutButton.setOnClickListener(view -> AuthUI.getInstance()
                .signOut(HomeActivity.this)
                .addOnCompleteListener(task -> {
                    Intent loginIntent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(loginIntent);
                }));

        uploadButton.setOnClickListener(view -> {
            Intent uploadIntent = new Intent(HomeActivity.this, UploadActivity.class);
            startActivity(uploadIntent);
        });
    }
}