package com.example.funnel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {

    private ImageView uploadImageView;
    private Uri filepath;

    FirebaseStorage storage;
    StorageReference storageReference;
    private final int PICK_IMAGE_REQUEST = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Button returnButton = findViewById(R.id.backToHomeButton);
        Button selectButton = findViewById(R.id.selectImageButton);
        Button uploadButton = findViewById(R.id.uploadImageButton);
        uploadImageView = findViewById(R.id.uploadImageView);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnHomeIntent = new Intent(UploadActivity.this, HomeActivity.class);
                startActivity(returnHomeIntent);
            }
        });

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
    }

    private void selectImage() {
        Intent selectImageIntent = new Intent();
        selectImageIntent.setType("image/*");
        selectImageIntent.setAction(Intent.ACTION_GET_CONTENT);
        Intent.createChooser(selectImageIntent, "Select image from here...");
        startActivityForResult(
                Intent.createChooser(
                        selectImageIntent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Confirm image selection and save
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), filepath);
                uploadImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if (filepath != null) {

            // TODO: Define path for image storage in Cloud Storage.

            final String storagePath = "userID / groupID / image name";
            StorageReference imageRef = storageReference.child(
                    "images/" + filepath.getLastPathSegment());
            UploadTask uploadTask = imageRef.putFile(filepath);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(exception ->
                    Toast.makeText(UploadActivity.this,
                            "Failed " + exception.getMessage(),
                            Toast.LENGTH_SHORT).show())
                    .addOnSuccessListener(taskSnapshot ->
                            Toast.makeText(UploadActivity.this,
                            "Image Uploaded!!",
                            Toast.LENGTH_SHORT).show());
        }
    }
}