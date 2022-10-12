package com.rizalfadiaalfikri.chatapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rizalfadiaalfikri.chatapp.Models.GroupMessageModel;
import com.rizalfadiaalfikri.chatapp.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesGroupAdapter extends RecyclerView.Adapter<MessagesGroupAdapter.MyViewHolder> {
    private List<GroupMessageModel> messageList;
    private Context context;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    public MessagesGroupAdapter(List<GroupMessageModel> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_message_layout, parent, false);
        mAuth = FirebaseAuth.getInstance();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String messageSenderId = mAuth.getCurrentUser().getUid();
        GroupMessageModel messages = messageList.get(position);

        String fromUserID = messages.getFrom();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserID);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.receiver_messageList.setVisibility(View.INVISIBLE);
        holder.profileImage.setVisibility(View.INVISIBLE);
        holder.sender_messageList.setVisibility(View.INVISIBLE);

        if (fromUserID.equals(messageSenderId)) {
            holder.sender_messageList.setVisibility(View.VISIBLE);

            holder.sender_messageList.setBackgroundResource(R.drawable.sender_message_layout);
            holder.sender_messageList.setTextColor(Color.BLACK);
            holder.sender_messageList.setText(messages.getMessage());
        } else {

            holder.profileImage.setVisibility(View.VISIBLE);
            holder.receiver_messageList.setVisibility(View.VISIBLE);

            holder.receiver_messageList.setBackgroundResource(R.drawable.receiver_message_layout);
            holder.receiver_messageList.setTextColor(Color.BLACK);
            holder.receiver_messageList.setText(messages.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView receiver_messageList, sender_messageList;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.message_profile_image);
            receiver_messageList = itemView.findViewById(R.id.txt_receiver_message);
            sender_messageList = itemView.findViewById(R.id.txt_sender_message);

        }
    }
}
