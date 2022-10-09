package com.rizalfadiaalfikri.chatapp.LoginRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rizalfadiaalfikri.chatapp.MainActivity;
import com.rizalfadiaalfikri.chatapp.R;

public class SignUpActivity extends AppCompatActivity {

    private Button btn_signup;
    private EditText ed_email, ed_password;

    FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();

        initializefields();

    }

    private void initializefields() {
        ed_email = findViewById(R.id.ed_signUp_email);
        ed_password = findViewById(R.id.ed_signUp_password);
        btn_signup = findViewById(R.id.btn_signUp);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAccount();
            }
        });

    }

    private void createNewAccount() {

        String email = ed_email.getText().toString().toLowerCase().toString();
        String password = ed_password.getText().toString().toString();
        loadingBar = new ProgressDialog(this);

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Create new account");
            loadingBar.setMessage("Please wait .....");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String currentUserID = mAuth.getCurrentUser().getUid();
                                rootRef.child("Users").child(currentUserID).setValue("");

                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                Animatoo.animateSlideUp(SignUpActivity.this);
                                startActivity(intent);

                                loadingBar.dismiss();
                            } else {
                                String error = task.getException().toString();
                                Toast.makeText(SignUpActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();

                                loadingBar.dismiss();
                            }

                        }
                    });
        }

    }
}