package com.example.finalproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproject.Domain.User;
import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class SignUpActivity extends BaseActivity {
    ActivitySignUpBinding binding;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        reference = FirebaseDatabase.getInstance().getReference("users");
        setVariable();
    }

    private void setVariable() {
        binding.btnSingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = binding.edtSignupUsername.getText().toString().trim();
                String email = binding.edtSignupEmail.getText().toString().trim();
                String password = binding.edtSignupPassword.getText().toString().trim();
                String repassword = binding.edtSignupRepassword.getText().toString().trim();
                if (!validateUsername(username) || !validatePassword(password) || !validateConfirmPassWord(repassword)) {
                    return;
                }
                if (!password.equals(repassword)) {
                    Toast.makeText(SignUpActivity.this, "ConfirmPassWord is wrong !!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String id = UUID.randomUUID().toString();
                User user = new User(id, username, email, password);
                checkEmailExist(user);
            }
        });
    }

    public boolean validateConfirmPassWord(String rePassword) {
        if (rePassword.isEmpty()) {
            binding.edtSignupRepassword.setError("ConfirmPassWord cannot be empty");
            return false;
        } else {
            binding.edtSignupRepassword.setError(null);
            return true;
        }
    }

    private boolean validateUsername(String username) {
        String regex = "^[a-zA-Z0-9_]*$";
        if (username.isEmpty()) {
            binding.edtSignupUsername.setError("Username cannot be empty");
            return false;
        } else if (!username.matches(regex)) {
            binding.edtSignupUsername.setError("Username cannot contain special characters");
            return false;
        } else {
            binding.edtSignupUsername.setError(null);
            return true;
        }
    }

    private boolean validatePassword(String password) {
        if (password.isEmpty()) {
            binding.edtSignupPassword.setError("Password cannot be empty");
            return false;
        } else {
            binding.edtSignupPassword.setError(null);
            return true;
        }
    }

    private void checkUsernameExist(User user) {
        String username = user.getUsername();
        Query query = reference.orderByChild("username").equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(SignUpActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                } else {
                    reference.child(user.getId()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                // Đăng ký thất bại, hiển thị thông báo lỗi
                                Toast.makeText(SignUpActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignUpActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkEmailExist(User user) {
        String email = user.getEmail();
        Query query = reference.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Email already exists
                    Toast.makeText(SignUpActivity.this, "The email already exists", Toast.LENGTH_SHORT).show();
                } else {
                    // Email is available
                    checkUsernameExist(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignUpActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}