package com.example.finalproject.Domain;

public class Order {
    private String Id;
    private String UserId;
    private String Total;
    private String OrderDate;

    public Order() {
    }

    public Order(String id, String userId, String total, String orderDate) {
        Id = id;
        UserId = userId;
        Total = total;
        OrderDate = orderDate;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }
}
