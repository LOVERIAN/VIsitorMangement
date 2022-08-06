package com.homeone.visitormanagement.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.homeone.visitormanagement.databinding.FragmentOwnerLoginBinding;

public class OwnerLogin extends Fragment {

    FragmentOwnerLoginBinding binding;
    private FirebaseAuth mAuth;

    String TAG = "OwnerLoginFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentOwnerLoginBinding.inflate(inflater,container,false);
        // Initialize Firebase Auth

        mAuth = FirebaseAuth.getInstance();

        String email = binding.email.getText().toString();
        String password = binding.password.getText().toString();

        binding.loginButton.setOnClickListener( view -> {

            if (email.isEmpty() || password.isEmpty()) {
                Log.i(TAG, "empty");
            }else
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.i(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.i(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(getContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        });

        return binding.getRoot();
    }

    private void updateUI(Object o) {
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //reload();
        }
    }
}