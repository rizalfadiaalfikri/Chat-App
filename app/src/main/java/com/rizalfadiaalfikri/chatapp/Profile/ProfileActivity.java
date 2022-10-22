package com.rizalfadiaalfikri.chatapp.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rizalfadiaalfikri.chatapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView imageView;
    private TextView txt_profile_name, txt_profile_status;
    private Button btn_profile_sendMessage, btn_profile_cancelChatRequest;

    private FirebaseAuth mAuth;
    private DatabaseReference user_ref;

    private String user_id, current_state, current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        user_ref = FirebaseDatabase.getInstance().getReference().child("Users");

        user_id = getIntent().getStringExtra("user_id");
        current_user_id = mAuth.getCurrentUser().getUid();

        initializefields();
        retrieveUserInfo();

    }

    private void initializefields() {
        txt_profile_name = findViewById(R.id.txt_profile_username);
        txt_profile_status = findViewById(R.id.txt_profile_status);
        imageView = findViewById(R.id.img_profile);
        btn_profile_sendMessage = findViewById(R.id.btn_profile_sendMessage);
        btn_profile_cancelChatRequest = findViewById(R.id.btn_profile_cancelChatRequest);

    }

    private void retrieveUserInfo() {
        user_ref.child(user_id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            String username = snapshot.child("name").getValue().toString();
                            String status = snapshot.child("status").getValue().toString();

                            txt_profile_name.setText(username);
                            txt_profile_status.setText(status);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}