package com.example.qiupys.wateroder.supplement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qiupys.wateroder.R;
import com.example.qiupys.wateroder.supplement.OrderItem;

import java.util.List;

public class OrderAdapter extends ArrayAdapter {
    List<OrderItem> list;
    public OrderAdapter(@NonNull Context context, int resource, @NonNull List<OrderItem> list) {
        super(context, resource, list);
        this.list=list;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.order_for_each_one,parent,false);
        }
        OrderItem orderItem = (OrderItem)getItem(position);
        TextView account = itemView.findViewById(R.id.textView12);
        TextView orderid = itemView.findViewById(R.id.textView9);
        TextView orderstatus = itemView.findViewById(R.id.textView10);
        TextView starttime = itemView.findViewById(R.id.textView11);
        orderid.setText(orderItem.getOrderId());
        orderstatus.setText(orderItem.getOrderStatus());
        starttime.setText(orderItem.getStartTime());
        account.setText(orderItem.getAccount());
        return itemView;
    }

    public void update(List<OrderItem> list){
        this.list=list;
        notifyDataSetChanged();
    }
}
