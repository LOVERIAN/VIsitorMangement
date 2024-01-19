package com.homeone.visitormanagement.utility;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.homeone.visitormanagement.modal.User;

public class Config {
    public static User user;
    public static String serverUrl = "http://34.93.115.120:8081/";

    public static void initUrl() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("apis").child("serverUrl");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String url = snapshot.getValue(String.class);
                serverUrl = url;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Error", error.getMessage());
            }
        });
    }
}
