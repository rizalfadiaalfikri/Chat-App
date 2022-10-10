package com.rizalfadiaalfikri.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rizalfadiaalfikri.chatapp.Fragments.ChatsFragment;
import com.rizalfadiaalfikri.chatapp.Fragments.ContatcsFragment;
import com.rizalfadiaalfikri.chatapp.Fragments.GroupsFragment;
import com.rizalfadiaalfikri.chatapp.Fragments.RequestsFragment;
import com.rizalfadiaalfikri.chatapp.Home.HomeActivity;
import com.rizalfadiaalfikri.chatapp.LoginRegister.SignInActivity;
import com.rizalfadiaalfikri.chatapp.Toolbar.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    Toolbar mToolbar;

    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        frameLayout = findViewById(R.id.frameLayout);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

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

    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser == null) {
            sendToSignInActivity();
        } else {
            verifyUserExistence();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_find_friends) {
            Toast.makeText(this, "Find Friends", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.menu_create_group) {
            Toast.makeText(this, "Create Groups", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.menu_setting) {
            sendToSettingsActivity();
        }
        if (item.getItemId() == R.id.menu_signout) {
            mAuth.signOut();
            Toast.makeText(this, "Sign Out Successful", Toast.LENGTH_SHORT).show();
            sendToHomeActivity();
        }


        return super.onOptionsItemSelected(item);
    }

    private void sendToSettingsActivity() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void sendToHomeActivity() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    private void verifyUserExistence() {
        String currentId = mAuth.getCurrentUser().getUid();
        rootRef.child("Users").child(currentId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void sendToSignInActivity() {
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(intent);
    }
}