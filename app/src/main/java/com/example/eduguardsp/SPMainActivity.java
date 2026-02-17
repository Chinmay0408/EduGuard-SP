package com.example.eduguardsp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SPMainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spmain);

        bottomNav = findViewById(R.id.sp_bottom_navigation);

        // ✅ Default Screen
        loadFragment(new SPHomeFragment());

        bottomNav.setOnItemSelectedListener(item -> {

            Fragment fragment = null;

            if(item.getItemId() == R.id.nav_home){
                fragment = new SPHomeFragment();
            }
            else if(item.getItemId() == R.id.nav_admins){
                fragment = new SPAdminsFragment();
            }
            else if(item.getItemId() == R.id.nav_setting){
                fragment = new SPSettingsFragment();
            }

            return loadFragment(fragment);
        });
    }

    private boolean loadFragment(Fragment fragment){

        if(fragment != null){

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.sp_fragment_container, fragment)
                    .commit();

            return true;
        }

        return false;
    }
}
