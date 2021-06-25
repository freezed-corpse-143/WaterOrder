package com.example.qiupys.wateroder.supplement;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qiupys.wateroder.R;
import com.example.qiupys.wateroder.tool.AdderView;

import java.util.List;

public class ShapAdapter extends ArrayAdapter {
    private List<ShapItem> list;
    public ShapAdapter(@NonNull Context context, int resource, @NonNull List<ShapItem> objects) {
        super(context, resource, objects);
        this.list=objects;
    }

    public void update(View itemView,ShapItem shapItem){
        int count = shapItem.getCount();
        double pri = Double.parseDouble(shapItem.getPrice());
        double fee = count*pri;
        AdderView adderView = itemView.findViewById(R.id.addview44);
        adderView.setValue(count);

        TextView total = itemView.findViewById(R.id.textView21);
        total.setText("共计"+count+"件商品");

        TextView xiaoji = itemView.findViewById(R.id.textView22);
        xiaoji.setText("小计：¥"+fee);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.order_list_item,parent,false);
        }
        Log.i("TAG", "getView: 进入了适配器内部方法");
        ShapItem shapItem = (ShapItem)getItem(position);

        TextView name = itemView.findViewById(R.id.textView20_name);
        name.setText(shapItem.getName());

        TextView price = itemView.findViewById(R.id.textView3_price);
        price.setText(shapItem.getPrice());

        ImageView img = itemView.findViewById(R.id.imageView2);
        img.setImageResource(getImageByName(shapItem.getName()));

        update(itemView,shapItem);

        AdderView adderView = itemView.findViewById(R.id.addview44);
        adderView.setOnValueChangeListener(new AdderView.OnValueChangeListener() {
            @Override
            public void onValueChange(int value, boolean added) {

            }
        });


        return itemView;
    }


    public int getImageByName(String name){
        ApplicationInfo appInfo = getContext().getApplicationInfo();
        int resID = getContext().getResources().getIdentifier(name, "drawable", appInfo.packageName);
        return resID;
    }

    public void refresh(List<ShapItem> data){
        list.clear();
        for(int i=0;i<data.size();i++){
            list.add(data.get(i));
        }
        Log.i("TAG", "refresh: "+"进入了适配器内部");
        notifyDataSetChanged();
    }


}
