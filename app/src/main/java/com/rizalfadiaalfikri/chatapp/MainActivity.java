package com.rizalfadiaalfikri.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.rizalfadiaalfikri.chatapp.Toolbar.FindFriendsActivity;
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
            sendToHomeActivity();
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
            sendToFindFriendsActivity();
        }
        if (item.getItemId() == R.id.menu_create_group) {
            requestCreateNewGroup();
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

    private void requestCreateNewGroup() {

        View alertCustomDialog = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_dlalog, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        alertDialog.setView(alertCustomDialog).show();
        ImageButton btn_cancel = (ImageButton) alertCustomDialog.findViewById(R.id.btn_cancelId);
        EditText ed_create_new_group = (EditText) alertCustomDialog.findViewById(R.id.ed_create_new_group);
        Button btn_create_new_group = (Button) alertCustomDialog.findViewById(R.id.btn_create_new_group);

        AlertDialog dialog = alertDialog.create();
        // getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        btn_create_new_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_group = ed_create_new_group.getText().toString();

                if (TextUtils.isEmpty(new_group)) {
                    Toast.makeText(MainActivity.this, "Please write your group name", Toast.LENGTH_SHORT).show();
                } else {
                    createNewGroup(new_group);
                    dialog.cancel();
                }

            }
        });
    }

    private void createNewGroup(String new_group) {
        rootRef.child("Groups").child(new_group)
                .setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Create new group is successful", Toast.LENGTH_SHORT).show();
                        } else {
                            String error = task.getException().toString();
                            Toast.makeText(MainActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendToFindFriendsActivity() {
        Intent intent = new Intent(MainActivity.this, FindFriendsActivity.class);
        startActivity(intent);
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
                        if (snapshot.child("name").exists()) {
                            Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                            startActivity(intent);
                        }
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