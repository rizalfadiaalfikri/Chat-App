package com.rizalfadiaalfikri.chatapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import com.rizalfadiaalfikri.chatapp.R;
import com.rizalfadiaalfikri.chatapp.Models.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GroupsFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference groupRef;

    private View groupFragemntView;
    private RecyclerView recyclerView_groupName;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups = new ArrayList<>();

    public GroupsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mAuth = FirebaseAuth.getInstance();
        groupRef = FirebaseDatabase.getInstance().getReference().child("Groups");

        View view =  inflater.inflate(R.layout.fragment_groups, container, false);

//        recyclerView_groupName = view.findViewById(R.id.recyclerView_groupName);
//        recyclerView_groupName.setLayoutManager(new LinearLayoutManager(getContext()));

        listView = view.findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list_of_groups);
        listView.setAdapter(arrayAdapter);

        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> set  = new HashSet<>();
                Iterator iterator = snapshot.getChildren().iterator();

                while (iterator.hasNext()) {
                    set.add(((DataSnapshot) iterator.next()).getKey());
                }
                list_of_groups.clear();
                list_of_groups.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        return view;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
////        FirebaseRecyclerOptions options =
////                new FirebaseRecyclerOptions.Builder<GroupsModel>()
////                .setQuery(groupRef, GroupsModel.class)
////                .build();
////
////        FirebaseRecyclerAdapter<GroupsModel, GroupViewHolder> adapter =
////                new FirebaseRecyclerAdapter<GroupsModel, GroupViewHolder>(options) {
////                    @Override
////                    protected void onBindViewHolder(@NonNull GroupViewHolder holder, int position, @NonNull GroupsModel model) {
////                        final String userId = getRef(position).getKey();
////
////                        groupRef.child(userId).addValueEventListener(new ValueEventListener() {
////                            @Override
////                            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                                if (snapshot.exists()) {
////                                    String name = snapshot.getValue(GroupsModel.class).toString();
////
////                                    holder.nameGroup.setText(name);
////                                }
////                            }
////
////                            @Override
////                            public void onCancelled(@NonNull DatabaseError error) {
////
////                            }
////                        });
////                    }
////
////                    @NonNull
////                    @Override
////                    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
////                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_display_layout, parent, false);
////                        return new GroupViewHolder(view);
////                    }
////                };
////
////        recyclerView_groupName.setAdapter(adapter);
////        adapter.startListening();
//
//    }
//
//    public class GroupViewHolder extends RecyclerView.ViewHolder {
//
//        TextView nameGroup;
//
//        public GroupViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            nameGroup = itemView.findViewById(R.id.groupName);
//
//        }
//    }
}