package com.rizalfadiaalfikri.chatapp.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.rizalfadiaalfikri.chatapp.LoginRegister.SignInActivity;
import com.rizalfadiaalfikri.chatapp.LoginRegister.SignUpActivity;
import com.rizalfadiaalfikri.chatapp.R;

public class HomeActivity extends AppCompatActivity {

    Button btn_signUp, btn_signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_signUp = findViewById(R.id.btn_ToSignUp);
        btn_signIn = findViewById(R.id.btn_ToSignIn);

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SignUpActivity.class);
                Animatoo.animateSwipeLeft(HomeActivity.this);
                startActivity(intent);
            }
        });

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
                Animatoo.animateSwipeLeft(HomeActivity.this);
                startActivity(intent);
            }
        });

    }
}