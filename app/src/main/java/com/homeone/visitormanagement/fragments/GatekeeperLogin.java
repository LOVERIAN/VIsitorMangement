package com.homeone.visitormanagement.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.homeone.visitormanagement.PortalActivity;
import com.homeone.visitormanagement.databinding.FragmentGatekeeperLoginBinding;

public class GatekeeperLogin extends Fragment {

    private FragmentGatekeeperLoginBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentGatekeeperLoginBinding.inflate(getLayoutInflater(), container, false);

        binding.pprtalButton.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), PortalActivity.class);
            startActivity(i);
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}