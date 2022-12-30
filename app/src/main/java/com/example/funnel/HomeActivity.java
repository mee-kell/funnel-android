package com.example.funnel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class HomeActivity extends AppCompatActivity {

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
    }
}