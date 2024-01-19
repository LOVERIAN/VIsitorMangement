package com.homeone.visitormanagement;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.homeone.visitormanagement.adapters.PromotionAdapter;
import com.homeone.visitormanagement.databinding.ActivityOwnerBinding;
import com.homeone.visitormanagement.dialog.AlertDialogSOS;
import com.homeone.visitormanagement.modal.PromotionData;
import com.homeone.visitormanagement.modal.RequestOwnerData;
import com.homeone.visitormanagement.modal.User;
import com.homeone.visitormanagement.utility.Config;

import java.util.ArrayList;
import java.util.List;

public class OwnerActivity extends AppCompatActivity {

    ActivityOwnerBinding binding;
    List<RequestOwnerData> dataModelList = new ArrayList<>();
    List<PromotionData> promotionDataList = new ArrayList<>();
    PromotionAdapter promotionAdapter;
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
        updatePromotionView();

        myRef = FirebaseDatabase.getInstance().getReference("links").child("temp");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        PromotionData promotionData = dataSnapshot.getValue(PromotionData.class);
                        promotionDataList.add(promotionData);
                    }
                    promotionAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        MaterialToolbar materialToolbar = findViewById(R.id.topAppBar);

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.more:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(OwnerActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.dashboard:
                        String url = "https://apartment.homeonetechnologies.in/";
                        Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        // Note the Chooser below. If no applications match,
                        // Android displays a system message.So here there is no need for try-catch.
                        startActivity(Intent.createChooser(intent2, "Open With"));
                        return true;
                    case R.id.alert:
                        showSOSDialog();
                        return true;
                }
                return false;
            }
        });
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        DatabaseReference reference = database.getReference("uid");
        reference.child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                Config.user = user;
                String msg = "Welcome " + user.name;
                binding.welcomeMsg.setText(msg);
                Log.i("STATUS","CHECKED");
                queryUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showSOSDialog() {
        AlertDialogSOS dialogFragment = new AlertDialogSOS();
        dialogFragment.show(getSupportFragmentManager(),"SOS Fragment");

    }

    private void updatePromotionView() {
        promotionAdapter = new PromotionAdapter(promotionDataList,this);
        binding.promotionRecyclerview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        binding.promotionRecyclerview.setAdapter(promotionAdapter);
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