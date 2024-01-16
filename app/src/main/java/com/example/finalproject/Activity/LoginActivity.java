package com.example.finalproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.finalproject.Domain.User;
import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends BaseActivity {
    ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVariable();

    }

    private void setVariable() {
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.edtLoginUsername.getText().toString().trim();
                String password = binding.edtLoginPassword.getText().toString().trim();

                if (username.isEmpty() || !validatePassword(password)) {
                    return;
                } else {
                    checkUser(username, password);
                }
            }
        });

        binding.btnSignupOnLoginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }


    public void checkUser(String userUsername, String userPassword) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query query = reference.orderByChild("username").equalTo(userUsername);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        if (user != null) {
                            String password = user.getPassword();
                            if (userPassword.equals(password)) {
                                //save users
                                SharedPreferences preferences = getSharedPreferences("account", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("userId", user.getId());
                                editor.putString("username", userUsername);
                                editor.putString("password", userPassword);
                                editor.apply();

                                //chuyen den mainactivity
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.d(TAG, "Password does not match");
                                Toast.makeText(LoginActivity.this, "Username or password not correct", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    Log.d(TAG, "User does not exist");
                    Toast.makeText(LoginActivity.this, "Username or Password not correct", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(LoginActivity.this, "Error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validatePassword(String password) {
        if (password.isEmpty()) {
            binding.edtLoginPassword.setError("Password cannot be empty");
            return false;
        } else {
            binding.edtLoginPassword.setError(null);
            return true;
        }
    }
}