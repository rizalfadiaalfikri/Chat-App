package com.rizalfadiaalfikri.chatapp.Chats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.rizalfadiaalfikri.chatapp.R;

public class GroupChatsActivity extends AppCompatActivity {

    private String currentGroupName;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chats);

        currentGroupName = getIntent().getStringExtra("groupName");

        mToolbar = findViewById(R.id.groupChat_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(currentGroupName);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}