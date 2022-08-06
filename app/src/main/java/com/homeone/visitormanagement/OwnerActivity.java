package com.homeone.visitormanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.homeone.visitormanagement.databinding.ActivityOwnerBinding;
import com.homeone.visitormanagement.modal.RequestOwnerData;
import com.homeone.visitormanagement.modal.User;

import java.util.ArrayList;
import java.util.List;

public class OwnerActivity extends AppCompatActivity {

    ActivityOwnerBinding binding;
    List<RequestOwnerData> dataModelList = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth auth;
    Button approveBtn;
    Button denyBtn;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_owner);

        Log.i("MENU", "CLICKEDD");

        MaterialToolbar materialToolbar = findViewById(R.id.topAppBar);

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(OwnerActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
        });
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        DatabaseReference reference = database.getReference("uid");
        reference.child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                Log.i("STATUS","CHECKED");
                queryUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updateView() {
        if (dataModelList.isEmpty()) {
            binding.noReqText.setVisibility(View.VISIBLE);
        }
        OwnerAdapter myRecyclerViewAdapter = new OwnerAdapter(dataModelList, this);
        binding.requestList.setLayoutManager(new LinearLayoutManager(this));
        binding.requestList.setAdapter(myRecyclerViewAdapter);
    }


    private void queryUsers() {
        myRef = database.getReference("requests");

        myRef.orderByChild("owner").equalTo(user.name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataModelList.clear();
                for (DataSnapshot shot : snapshot.getChildren()) {
                    // Log.i("WRITEFIRE", shot.getValue().toString());
                    RequestOwnerData data;
                    data = shot.getValue(RequestOwnerData.class);
                    if(data.getStatus().equals("pending"))
                        dataModelList.add(data);
                }
                updateView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}