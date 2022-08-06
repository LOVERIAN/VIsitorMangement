package com.homeone.visitormanagement;

import static android.app.Activity.RESULT_OK;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.homeone.visitormanagement.modal.RequestOwnerData;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private static final int requestImageCapture = 1;
    Button captureImage;
    Button uploadBtn;
    String imageEncoded;
    String id;
    EditText usernameEditText;
    EditText visitorNameEditText;
    EditText purposeEditText;

    public BottomSheetDialog() {}
    public BottomSheetDialog(String id) {
        this.id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.bottom_sheet_dialog,
                container, false);

        captureImage = v.findViewById(R.id.captureImage);
        uploadBtn = v.findViewById(R.id.upload);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("requests");

        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dispatchTakePictureIntent();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (!TextUtils.isEmpty(imageEncoded))
                myRef.child(id).child("image").setValue(imageEncoded);
                dismiss();
            }
        });
        return v;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, requestImageCapture);
            }
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestImageCapture && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            encodeBitmap(imageBitmap);
            captureImage.setBackgroundColor(Color.GREEN);
            //uploadToDB(imageUri);
        }
    }

    private void encodeBitmap(Bitmap imageBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

        //clickImage.setImageBitmap(decodeFromFirebaseBase64(imageEncoded));
    }

    public static Bitmap decodeFromFirebaseBase64(String image) {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }
}