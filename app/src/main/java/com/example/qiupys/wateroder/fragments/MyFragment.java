package com.example.qiupys.wateroder.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qiupys.wateroder.supplement.OrderAdapter;
import com.example.qiupys.wateroder.supplement.OrderItem;
import com.example.qiupys.wateroder.R;

import java.util.List;

public class MyFragment extends Fragment {
    View view;
    String TAG= "myfragement";
    String text;
    private Context context;
    private ListView listView;
    private OrderAdapter orderAdapter;

    public MyFragment(Context context, List<OrderItem> orderItems){
        this.context = context;
        this.orderItems = orderItems;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    List<OrderItem> orderItems;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.orderinfoview, null);
//        Log.i(TAG, "onCreateView: "+(view==null));
        listView= view.findViewById(R.id.orderlist3);
        orderAdapter=new OrderAdapter(context,R.layout.order_for_each_one,orderItems);
        listView.setAdapter(orderAdapter);

        return view;
    }

    public void setText(String t){
        this.text = t;
    }

    public void update(List<OrderItem> orderItems){
        Log.i(TAG, "update: listView == null?"+(listView==null));
    }

}
