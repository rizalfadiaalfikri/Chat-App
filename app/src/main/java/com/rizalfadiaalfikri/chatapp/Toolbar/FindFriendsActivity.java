package com.rizalfadiaalfikri.chatapp.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rizalfadiaalfikri.chatapp.Models.Contacs;
import com.rizalfadiaalfikri.chatapp.Profile.ProfileActivity;
import com.rizalfadiaalfikri.chatapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriendsActivity extends AppCompatActivity {

    private RecyclerView recyclerView_findFriends;
    private Toolbar mToolbar;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        recyclerView_findFriends = findViewById(R.id.recyclerView_findFriends);
        recyclerView_findFriends.setLayoutManager(new LinearLayoutManager(this));

        mToolbar = findViewById(R.id.find_fiends_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Find Friends");

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Contacs>()
                .setQuery(usersRef, Contacs.class)
                .build();

        FirebaseRecyclerAdapter<Contacs, FindFriendsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contacs, FindFriendsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FindFriendsViewHolder holder, int position, @NonNull Contacs model) {
                        final String usersId = getRef(position).getKey();
                        usersRef.child(usersId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String user_id = snapshot.getKey().toString();
                                String username = snapshot.child("name").getValue().toString();
                                String userStatus = snapshot.child("status").getValue().toString();

                                holder.txt_username.setText(username);
                                holder.txt_status.setText(userStatus);

                                holder.cardView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(FindFriendsActivity.this, user_id, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                        intent.putExtra("user_id", user_id);
                                        startActivity(intent);
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public FindFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_display_layout, parent, false);
                        return new FindFriendsViewHolder(view);
                    }
                };

        recyclerView_findFriends.setAdapter(adapter);
        adapter.startListening();
    }

    public class FindFriendsViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_username, txt_status;
        private CircleImageView circleImageView;
        private CardView cardView;

        public FindFriendsViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_username = itemView.findViewById(R.id.txt_users_profile_name);
            txt_status = itemView.findViewById(R.id.txt_users_profile_status);
            circleImageView = itemView.findViewById(R.id.users_profile_image);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }
}