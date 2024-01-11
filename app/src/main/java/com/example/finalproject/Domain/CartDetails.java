package com.example.finalproject.Domain;

public class CartDetails {
    private int CartDetailId;
    private int CartId;
    private int FoodId;
    private int Quantity;
    private double Total;

    public int getCartDetailId() {
        return CartDetailId;
    }

    public void setCartDetailId(int cartDetailId) {
        CartDetailId = cartDetailId;
    }

    public int getCartId() {
        return CartId;
    }

    public void setCartId(int cartId) {
        CartId = cartId;
    }

    public int getFoodId() {
        return FoodId;
    }

    public void setFoodId(int foodId) {
        FoodId = foodId;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }

    public CartDetails() {
    }
}
