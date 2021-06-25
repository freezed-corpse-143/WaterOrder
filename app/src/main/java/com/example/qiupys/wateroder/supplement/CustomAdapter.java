package com.example.qiupys.wateroder.supplement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qiupys.wateroder.R;
import com.example.qiupys.wateroder.supplement.RateItem;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private ArrayList<RateItem> list;
    private LayoutInflater inflater;

    public CustomAdapter(ArrayList<RateItem> list){
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.custom_layout, null);


        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        TextView textView_name = (TextView)view.findViewById(R.id.textView_name);
        TextView textView_price = (TextView)view.findViewById(R.id.textView_price);
        RateItem item = (RateItem) getItem(i);
        imageView.setImageResource(item.getImage());
        textView_name.setText(item.getName());
        textView_price.setText(item.getPrice());
        return view;

    }

    public void setInflater(LayoutInflater inflater) {
        this.inflater = inflater;
    }
}
