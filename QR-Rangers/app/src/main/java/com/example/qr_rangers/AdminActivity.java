package com.example.qr_rangers;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

/**
 * This is an activity that provides administrator functionality.
 * @author Alexander Salm
 * @version 1.0
 */
public class AdminActivity extends AppCompatActivity {

    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        user = (User) getIntent().getSerializableExtra("user");

        TabLayout tabs = findViewById(R.id.admin_tab_switcher);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                Fragment fragment = null;
                switch(tab.getPosition()){
                    case 0:
                        fragment = new ViewAdminUsersFragment();
                        break;
                    case 1:
                        fragment = new ViewAdminCodesFragment();
                        break;
                }

                switchFragment(fragment);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab){

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab){

            }
        });

        switchFragment(new ViewAdminUsersFragment());
    }

    public void switchFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.admin_frame_layout, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }
}
