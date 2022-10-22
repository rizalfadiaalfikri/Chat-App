package com.rizalfadiaalfikri.chatapp.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private DatabaseReference user_ref, chatRequestRef, ContactsRef;

    private String user_id, current_state, current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        user_ref = FirebaseDatabase.getInstance().getReference().child("Users");
        chatRequestRef = FirebaseDatabase.getInstance().getReference().child("Request");


        user_id = getIntent().getStringExtra("user_id");
        current_user_id = mAuth.getCurrentUser().getUid();
        current_state = "new";

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

                            manageChatRequest();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void manageChatRequest() {
        chatRequestRef.child(current_user_id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(user_id)) {
                            String request_type = snapshot.child(user_id).child("request_type").getValue().toString();
                            if (request_type.equals("sent")) {
                                current_state = "request_sent";
                                btn_profile_sendMessage.setText("Cancel Chat Request");
                            } else if (request_type.equals("received")) {
                                current_state = "request_received";
                                btn_profile_sendMessage.setText("Accept Chat Request");

                                btn_profile_cancelChatRequest.setVisibility(View.VISIBLE);
                                btn_profile_cancelChatRequest.setEnabled(true);
                                btn_profile_cancelChatRequest.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        removeChatRequest();
                                    }
                                });

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        if (!current_state.equals(user_id)) {
            btn_profile_sendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btn_profile_sendMessage.setEnabled(false);

                    if (current_state.equals("new")) {
                       sendChatRequest();
                    }
                    if (current_state.equals("request_sent")) {
                        removeChatRequest();
                    }
                    if (current_state.equals("request_received")) {
                        acceptRequest();
                    }
                }
            });
        }
    }

    private void acceptRequest() {

    }

    private void removeChatRequest() {
        chatRequestRef.child(current_user_id)
                .child(user_id)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            chatRequestRef.child(user_id)
                                    .child(current_user_id)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                btn_profile_sendMessage.setEnabled(true);
                                                current_state = "new";
                                                btn_profile_sendMessage.setText("Send Message");

//                                                btn_profile_cancelChatRequest.setVisibility(View.VISIBLE);
//                                                btn_profile_cancelChatRequest.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void sendChatRequest() {
        chatRequestRef.child(current_user_id).child(user_id)
                .child("request_type")
                .setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            chatRequestRef.child(user_id).child(current_user_id)
                                    .child("request_type")
                                    .setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                btn_profile_sendMessage.setEnabled(true);
                                                current_state = "request_sent";
                                                btn_profile_sendMessage.setVisibility(View.INVISIBLE);

                                                btn_profile_cancelChatRequest.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}