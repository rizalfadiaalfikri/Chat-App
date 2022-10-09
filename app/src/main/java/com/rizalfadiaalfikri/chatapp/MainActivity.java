package com.rizalfadiaalfikri.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.rizalfadiaalfikri.chatapp.Fragments.ChatsFragment;
import com.rizalfadiaalfikri.chatapp.Fragments.ContatcsFragment;
import com.rizalfadiaalfikri.chatapp.Fragments.GroupsFragment;
import com.rizalfadiaalfikri.chatapp.Fragments.RequestsFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        frameLayout = findViewById(R.id.frameLayout);

        replaceFragment(new ChatsFragment());

        BottomNavigation();

    }

    private void BottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_chat:
                        replaceFragment(new ChatsFragment());
                        break;
                    case R.id.menu_group:
                        replaceFragment(new GroupsFragment());
                        break;
                    case R.id.menu_contact:
                        replaceFragment(new ContatcsFragment());
                        break;
                    case R.id.menu_request:
                        replaceFragment(new RequestsFragment());
                        break;
                }
                return false;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}