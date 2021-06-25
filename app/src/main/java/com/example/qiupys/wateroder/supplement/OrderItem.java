package com.example.qiupys.wateroder.supplement;

import android.util.Log;
import android.util.Printer;

public class OrderItem {
    private String orderId;
    private String OrderStatus;
    private String StartTime;
    private String Account;

    public String getWaterid() {
        return waterid;
    }

    public void setWaterid(String waterid) {
        this.waterid = waterid;
    }

    private String waterid;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    private String Name;

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    private String Price;

    public OrderItem() {
    }

    private String DeliveryManId;

    public String getOrderId() {
        return orderId;
    }

    public OrderItem(String orderId, String orderStatus, String startTime, String account, String deliveryManId) {
        this.orderId = orderId;
        OrderStatus = orderStatus;
        StartTime = startTime;
        Account = account;
        DeliveryManId = deliveryManId;
    }
    public OrderItem(String[] values){
        this.orderId=values[0];
        this.OrderStatus=values[1];
        this.StartTime=timesub(values[2]);
        this.Account=values[3];
        this.DeliveryManId=values[4];
    }

    public String timesub(String time){
        return time.substring(0,10);
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getDeliveryManId() {
        return DeliveryManId;
    }

    public void setDeliveryManId(String deliveryManId) {
        DeliveryManId = deliveryManId;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderId='" + orderId + '\'' +
                ", OrderStatus='" + OrderStatus + '\'' +
                ", StartTime='" + StartTime + '\'' +
                ", Account='" + Account + '\'' +
                ", Name='" + Name + '\'' +
                ", Price='" + Price + '\'' +
                ", DeliveryManId='" + DeliveryManId + '\'' +
                '}';
    }

    public void display(){
        Log.i("TAG", "display: "+toString());
    }
}
