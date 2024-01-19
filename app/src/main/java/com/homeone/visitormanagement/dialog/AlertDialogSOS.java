package com.homeone.visitormanagement.dialog;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.homeone.visitormanagement.R;
import com.homeone.visitormanagement.utility.Config;

import java.util.Objects;

public class AlertDialogSOS extends DialogFragment implements View.OnClickListener {
    private FloatingActionButton fireFab;
    private FloatingActionButton otherFab;
    private FloatingActionButton callFab;
    private DatabaseReference myRef;
    private Boolean fire = false;
    private Boolean other = false;
    private String emergencyNo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sos_dialog, container, false);
        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_back);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        fireFab = view.findViewById(R.id.fire_fab);
        otherFab = view.findViewById(R.id.other_fab);
        callFab = view.findViewById(R.id.call_fab);

        myRef = FirebaseDatabase.getInstance().getReference("soscall");

        fireFab.setOnClickListener(this);
        otherFab.setOnClickListener(this);
        callFab.setOnClickListener(this);

        myRef.child("alerts").child(Config.user.name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (Objects.equals(dataSnapshot.getKey(), "fire")) {
                            fire = dataSnapshot.getValue(Boolean.class);
                        } else if (dataSnapshot.getKey().equals("other")) {
                            other = dataSnapshot.getValue(Boolean.class);
                        }
                    }
                    updateUI();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void updateUI() {
        if (!fire && !other) {
            myRef.child("alerts").child(Config.user.name).setValue(null);
        }

        if (fire || other) {
            myRef.child("alerts").child(Config.user.name).child("name").setValue(Config.user.name);
        }

        if (fire) {
            fireFab.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            myRef.child("alerts").child(Config.user.name).child("fire").setValue(fire);
        } else {
            fireFab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D6D5D5")));
            myRef.child("alerts").child(Config.user.name).child("fire").setValue(null);
        }
        fireFab.setImageResource(R.drawable.ic_baseline_local_fire_department_24);

        if (other) {
            otherFab.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            myRef.child("alerts").child(Config.user.name).child("other").setValue(other);
        } else {
            otherFab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D6D5D5")));
            myRef.child("alerts").child(Config.user.name).child("other").setValue(null);
        }
        otherFab.setImageResource(R.drawable.ic_baseline_help_outline_24);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fire_fab:
                fire = !fire;
                updateUI();
                return;
            case R.id.other_fab:
                other = !other;
                updateUI();
                return;
            case R.id.call_fab:
                callFab.setImageResource(R.drawable.ic_baseline_call_24);
                dialNumber();
        }
    }

    private void dialNumber() {
        myRef.child("dialer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    emergencyNo = snapshot.getValue(String.class);
                    if (emergencyNo != null) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + emergencyNo));
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
