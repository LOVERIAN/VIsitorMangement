package com.homeone.visitormanagement;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private static final int requestImageCapture = 1;
    Button captureImage;
    Button uploadBtn;
    String imageEncoded;
    String id;
    String imageFilePath;
    ProgressDialog progressDialog;
    StorageReference ref;
    DatabaseReference myRef;
    Uri uri = null;

    public BottomSheetDialog() {
    }

    public BottomSheetDialog(String id) {
        this.id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_dialog,
                container, false);

        // Defining the child of storageReference
        ref = FirebaseStorage
                .getInstance()
                .getReference()
                .child(
                        "GestImage/"
                                + UUID.randomUUID().toString());
        myRef = FirebaseDatabase.getInstance().getReference("requests");

        captureImage = v.findViewById(R.id.captureImage);
        uploadBtn = v.findViewById(R.id.upload);

        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uri != null) {
                    progressDialog
                            = new ProgressDialog(getContext());
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();
                    uploadPhoto();
                }
            }
        });
        return v;
    }

    private void uploadPhoto() {
        // adding listeners on upload
        ref = FirebaseStorage
                .getInstance()
                .getReference()
                .child(
                        "GestImage/"
                                + id.toString());
        myRef = FirebaseDatabase.getInstance().getReference("requests");
        // or failure of image
        ref.putFile(uri)
                .addOnSuccessListener(
                        new OnSuccessListener<UploadTask.TaskSnapshot>() {

                            @Override
                            public void onSuccess(
                                    UploadTask.TaskSnapshot taskSnapshot) {

                                // Image uploaded successfully
                                // Dismiss dialog
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        myRef.child(id).child("ProfileUrl").setValue(uri.toString()).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                e.printStackTrace();
                                            }
                                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast
                                                        .makeText(getContext(),
                                                                "Image Uploaded!!",
                                                                Toast.LENGTH_SHORT)
                                                        .show();
                                                progressDialog.dismiss();
                                                dismiss();
                                            }
                                        });


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast
                                                .makeText(getContext(),
                                                        "Something went wrong",
                                                        Toast.LENGTH_SHORT)
                                                .show();
                                        progressDialog.dismiss();
                                        dismiss();
                                    }
                                });

                            }
                        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        // Error, Image not uploaded
                        progressDialog.dismiss();
                        Toast
                                .makeText(getContext(),
                                        "Failed " + e.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .addOnProgressListener(
                        new OnProgressListener<UploadTask.TaskSnapshot>() {

                            // Progress Listener for loading
                            // percentage on the dialog box
                            @Override
                            public void onProgress(
                                    UploadTask.TaskSnapshot taskSnapshot) {
                                double progress
                                        = (100.0
                                        * taskSnapshot.getBytesTransferred()
                                        / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage(
                                        "Uploaded "
                                                + (int) progress + "%");
                            }
                        });
    }

    private File createImageFile() {
        try {
            String timeStamp =
                    new SimpleDateFormat("yyyyMMdd_HHmmss",
                            Locale.getDefault()).format(new Date());
            String imageFileName = "IMG_" + timeStamp + "_";
            File storageDir =
                    getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            imageFilePath = image.getAbsolutePath();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
                File photo = null;
                photo = createImageFile();

                if (photo != null) {
                    Uri photoURI = FileProvider.getUriForFile(getContext(),
                            "com.homeone.visitormanagement.provider",
                            photo);

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                    startActivityForResult(takePictureIntent, requestImageCapture);
                }
            }
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestImageCapture && resultCode == RESULT_OK) {
            try {
                //our imageFilePath that contains the absolute path to the created file
                File file = new File(imageFilePath);
                captureImage.setBackgroundColor(Color.GREEN);
                uri = Uri.fromFile(file);

            } catch (Exception e) {
                e.printStackTrace();
            }
            //uploadToDB(imageUri);
        }
    }
}