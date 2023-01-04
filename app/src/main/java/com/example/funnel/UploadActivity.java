package com.example.funnel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {

    private ImageView uploadImageView;
    private Uri filepath;
    private FirebaseUser user;
    private final int PICK_IMAGE_REQUEST = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        user = FirebaseAuth.getInstance().getCurrentUser();

        Button returnButton = findViewById(R.id.backToHomeButton);
        Button selectButton = findViewById(R.id.selectImageButton);
        Button uploadButton = findViewById(R.id.uploadImageButton);
        EditText uploadGroupName = findViewById(R.id.uploadGroupName);
        uploadImageView = findViewById(R.id.uploadImageView);

        returnButton.setOnClickListener(view -> {
            returnHome();
        });

        selectButton.setOnClickListener(view -> selectImage());

        uploadButton.setOnClickListener(view -> uploadImage(uploadGroupName.getText().toString()));
    }

    private void returnHome() {
        Intent returnHomeIntent = new Intent(UploadActivity.this, HomeActivity.class);
        startActivity(returnHomeIntent);
    }

    private void selectImage() {
        Intent selectImageIntent = new Intent();
        selectImageIntent.setType("image/*");
        selectImageIntent.setAction(Intent.ACTION_GET_CONTENT);
        Intent.createChooser(selectImageIntent, "Select image from here...");
        startActivityForResult(
                Intent.createChooser(
                        selectImageIntent,
                        "Select image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Confirm image selection and display
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filepath = data.getData();
            Picasso.with(UploadActivity.this)
                    .load(filepath)
                    .noPlaceholder()
                    .into(uploadImageView);
        }
    }

    private void uploadImage(String groupName) {
        if (filepath != null && user != null) {
            // Upload image to Cloud Storage.
            String userId = user.getUid();
            String imageName = filepath.getLastPathSegment();
            String storagePath = String.format("%s/%s/%s", userId, groupName, imageName);

            Log.d("UPLOAD", storagePath);

            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageReference.child(storagePath);
            UploadTask uploadTask = imageRef.putFile(filepath);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(exception ->
                    Toast.makeText(UploadActivity.this,
                            "Failed " + exception.getMessage(),
                            Toast.LENGTH_SHORT).show())
                    .addOnSuccessListener(taskSnapshot -> {
                        // Display success message
                        Toast.makeText(UploadActivity.this,
                                "Image Uploaded!",
                                Toast.LENGTH_SHORT).show();
                        // Save metadata to realtime database
                        saveToDatabase(userId, groupName, imageName);
                        returnHome();
                    });
        }
    }

    private void saveToDatabase(String userId, String groupName, String imageName) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference snippetNode = database.child(userId).child(groupName).child(imageName);
        snippetNode.child("imgPath").setValue(imageName);
        snippetNode.child("summary").setValue("No summary");
    }
}