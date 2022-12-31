package com.example.funnel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;

public class HomeActivity extends AppCompatActivity {
    private static final int DATASET_COUNT = 60;
    protected RecyclerView recyclerView;
    protected RecycleSnippetAdapter adapter;
    protected RecyclerView.LayoutManager layoutManager;
    protected String[] dataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(view -> AuthUI.getInstance()
                .signOut(HomeActivity.this)
                .addOnCompleteListener(task -> {
                    Intent loginIntent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(loginIntent);
                }));

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        initDataset();
        recyclerView = findViewById(R.id.recycleSnippetsView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecycleSnippetAdapter(dataset);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {
        dataset = new String[DATASET_COUNT];
        for (int i = 0; i < DATASET_COUNT; i++) {
            dataset[i] = "This is element #" + i;
        }
    }
}