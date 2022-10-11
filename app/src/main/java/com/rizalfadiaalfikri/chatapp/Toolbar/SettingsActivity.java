package com.rizalfadiaalfikri.chatapp.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rizalfadiaalfikri.chatapp.MainActivity;
import com.rizalfadiaalfikri.chatapp.R;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private EditText ed_username, ed_status;
    private Button btn_update;
    private CircleImageView imageView;

    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private String currentId;

    private Toolbar settingsToolbar;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        currentId = mAuth.getCurrentUser().getUid();

        loadingBar = new ProgressDialog(this);

        initializefields();

    }

    private void initializefields() {
        ed_username = findViewById(R.id.ed_settings_username);
        ed_status = findViewById(R.id.ed_settings_status);
        imageView = findViewById(R.id.settings_image);
        btn_update = findViewById(R.id.btn_settings_update);

        settingsToolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(settingsToolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Account Settings");

        retrieveUserInfo();

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
    }

    private void retrieveUserInfo() {
        rootRef.child("Users")
                .child(currentId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if ((snapshot.exists()) && (snapshot.hasChild("name"))) {
                            String retrieveUsername = snapshot.child("name").getValue().toString();
                            String retrieveStatus = snapshot.child("status").getValue().toString();

                            ed_username.setText(retrieveUsername);
                            ed_status.setText(retrieveStatus);

                        } else {
                            Toast.makeText(SettingsActivity.this, "Please set & update your profile information", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void updateProfile() {
        String username = ed_username.getText().toString();
        String status = ed_status.getText().toString();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(status)) {
            Toast.makeText(this, "Please enter status", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> profileMap = new HashMap<>();

            profileMap.put("uid", currentId);
            profileMap.put("name", username);
            profileMap.put("status", status);

            rootRef.child("Users")
                    .child(currentId)
                    .updateChildren(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                sendToMainActivity();
                                Toast.makeText(SettingsActivity.this, "Update Profile Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                String error = task.getException().toString();
                                Toast.makeText(SettingsActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void sendToMainActivity() {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
    }
}