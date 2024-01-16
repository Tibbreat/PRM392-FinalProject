package com.example.finalproject.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.finalproject.Adapter.CartAdapter;
import com.example.finalproject.Domain.Order;
import com.example.finalproject.Helper.ChangeNumberItemsListener;
import com.example.finalproject.Helper.ManagmentCart;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.databinding.ActivityCartBinding;

import com.example.finalproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CartActivity extends BaseActivity {

    private ActivityCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;
    private double tax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        managmentCart = new ManagmentCart(this);
        setVariable();
        caculateCart();
        initList();
    }

    private void initList() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.cardView.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(managmentCart.getListCart(), this, new ChangeNumberItemsListener() {
            @Override
            public void change() {
                caculateCart();
            }
        });
        binding.cardView.setAdapter(adapter);
    }

    private void caculateCart() {
        double percent = 0.02;
        double delivery = 10;
        tax = (double) Math.round(managmentCart.getTotalFee() * percent * 100.0) / 100;
        double total = (double) Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100;
        double itemTotal = (double) Math.round(managmentCart.getTotalFee() * 100) / 100;

        binding.totalFeeTxt.setText("$" + itemTotal);
        binding.taxTxt.setText("$" + tax);
        binding.deliveryTxt.setText("$" + delivery);
        binding.totalTxt.setText(new StringBuilder().append("$").append(total).toString());

    }

    private void setVariable() {
        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Order");

                cartRef.child("counter").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long currentCount = dataSnapshot.getValue(Long.class);

                        if (currentCount == null) {
                            currentCount = 0L;
                        }

                        Long newCount = currentCount + 1;

                        cartRef.child("counter").setValue(newCount);

                        String orderId = String.valueOf(currentCount);
                        SharedPreferences preferences = getSharedPreferences("account", MODE_PRIVATE);
                        String userId = preferences.getString("userId", "");

                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                        String orderDate = dateFormat.format(new Date());
                        Order order = new Order(orderId, userId, binding.totalTxt.getText().toString(), orderDate);

                        cartRef.child(orderId).setValue(order);

//                        Toast.makeText(LoginActivity.this, "Username or Password not correct", Toast.LENGTH_SHORT).show();
                        managmentCart.clearCart();
                        adapter.notifyDataSetChanged();


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


    }


}