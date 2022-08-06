package com.homeone.visitormanagement;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.homeone.visitormanagement.fragments.GatekeeperLogin;
import com.homeone.visitormanagement.fragments.OwnerLogin;

public class LoginAdapter extends FragmentStateAdapter {


    public LoginAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new OwnerLogin();
        }else {
            return new GatekeeperLogin();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
