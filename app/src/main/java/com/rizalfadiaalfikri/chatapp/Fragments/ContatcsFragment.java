package com.rizalfadiaalfikri.chatapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rizalfadiaalfikri.chatapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContatcsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContatcsFragment extends Fragment {

    public ContatcsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contatcs, container, false);
    }
}