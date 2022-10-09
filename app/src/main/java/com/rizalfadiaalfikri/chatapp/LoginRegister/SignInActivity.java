package com.rizalfadiaalfikri.chatapp.LoginRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.rizalfadiaalfikri.chatapp.MainActivity;
import com.rizalfadiaalfikri.chatapp.R;

public class SignInActivity extends AppCompatActivity {

    Button btn_signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btn_signIn = findViewById(R.id.btn_signIn);

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                Animatoo.animateSlideUp(SignInActivity.this);
                startActivity(intent);
            }
        });

    }
}