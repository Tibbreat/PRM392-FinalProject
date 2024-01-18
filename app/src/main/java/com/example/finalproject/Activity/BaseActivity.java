package com.example.finalproject.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.finalproject.R;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
/*
* BaseActivity is base activity contains firebase connection. All activities extend BaseActivity
* */
public class BaseActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    public String TAG = "TagCheck";

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
    }
}