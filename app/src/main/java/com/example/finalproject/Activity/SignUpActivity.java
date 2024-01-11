package com.example.finalproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityIntroBinding;
import com.example.finalproject.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class SignUpActivity extends BaseActivity {
    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setVariable();
    }

    private void setVariable() {
        binding.btnSingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.edtSignupEmail.getText().toString();
                String password = binding.edtSignupPassword.getText().toString();
                if (password.length() < 6) {
                    Toast.makeText(SignUpActivity.this, "Your password must be more than 6 character", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete()) {
                            Log.i(TAG, "onComplete: " + task.getException());
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                        } else {
                            Log.i(TAG, "Failure: " + task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}