package com.homeone.visitormanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.homeone.visitormanagement.adapters.ViewPagerAdapter;
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
    ArrayList<String> tabList = new ArrayList<>();
    Button addVisitorBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("requests");

        /*myRef.addValueEventListener(new ValueEventListener() {
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
*/
        //addVisitorBtn = findViewById(R.id.visitorBtn);

       /* addVisitorBtn.setOnClickListener( view -> {
            //sendPhoto();
            Intent i = new Intent(this, DashboardActivity.class);
            startActivity(i);
        });*/

        MaterialToolbar materialToolbar = findViewById(R.id.gatekeeper_bar);

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.more:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.panabell:
                        String packageName = "cn.ubia.pana";
                        Intent launchIntent = null;
                        try{
                            launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
                        } catch (Exception ignored) {}

                        if(launchIntent == null){
                            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                        } else {
                            startActivity(launchIntent);
                        }
                        return true;
                    case R.id.new_visitor:
                        Intent i = new Intent(HomeActivity.this, DashboardActivity.class);
                        startActivity(i);
                        return true;
                }
                return false;

            }
        });

        tabList.add("Pending Requests");
        tabList.add("Raised Alerts");

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),getLifecycle());
        binding.viewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout,binding.viewPager,(tab, position) -> {
            tab.setText(tabList.get(position));
        }).attach();

    }

}