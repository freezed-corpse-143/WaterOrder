package com.example.qiupys.wateroder.tool;

import com.example.qiupys.wateroder.supplement.OrderItem;

import java.util.List;

public interface Van {
    public void deliverCost(String value);
//    public void deliverAccount(String value);
    public void trans(List<OrderItem> li);
    public List<OrderItem> getList();
}
