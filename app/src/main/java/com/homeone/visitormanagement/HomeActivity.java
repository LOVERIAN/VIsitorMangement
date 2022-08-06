package com.homeone.visitormanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.homeone.visitormanagement.databinding.ActivityHomeBinding;
import com.homeone.visitormanagement.databinding.RequestLayoutBinding;
import com.homeone.visitormanagement.modal.RequestData;
import com.homeone.visitormanagement.modal.User;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    List<RequestData> dataModelList = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference reference;
    User userdata;

    Button addVisitorBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("requests");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataModelList.clear();
                for (DataSnapshot shot : snapshot.getChildren()) {
                   // Log.i("WRITEFIRE", shot.getValue().toString());
                    RequestData data;
                    data = shot.getValue(RequestData.class);
                    dataModelList.add(data);
                }
                updateView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addVisitorBtn = findViewById(R.id.visitorBtn);

        addVisitorBtn.setOnClickListener( view -> {
            //sendPhoto();
            Intent i = new Intent(this, DashboardActivity.class);
            startActivity(i);
        });
        MaterialToolbar materialToolbar = findViewById(R.id.topAppBar);

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
        });

    }

    public void updateView() {
        GateKeeperAdapter myRecyclerViewAdapter = new GateKeeperAdapter(dataModelList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        binding.requestList.setLayoutManager(linearLayoutManager);
        binding.requestList.setAdapter(myRecyclerViewAdapter);
    }
    private void sendPhoto() {

        BottomSheetDialog bottomSheet = new BottomSheetDialog();
        bottomSheet.show(getSupportFragmentManager(),
                "ModalBottomSheet");
    }
}