package com.example.qiupys.wateroder.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qiupys.wateroder.R;
import com.example.qiupys.wateroder.supplement.OrderItem;
import com.example.qiupys.wateroder.supplement.ShapAdapter;
import com.example.qiupys.wateroder.supplement.ShapItem;
import com.example.qiupys.wateroder.tool.AdderView;

import java.util.Iterator;
import java.util.List;

public class ShapFragment extends Fragment {
    private String IPv4;
    private String account;
    private Cat cat;
    private ShapAdapter shapAdapter;
    private TextView cost;
    private ListView listView;
    private Button button;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        Bundle info=getArguments();
        IPv4=info.getString("IPv4");
        account=info.getString("account");

        button=view.findViewById(R.id.button111111);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat.send();
            }
        });


        listView = view.findViewById(R.id.list_order_1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                log("开始");
                ShapItem shapItem=(ShapItem) listView.getItemAtPosition(position);
                shapItem.display();
                AdderView adderView = view.findViewById(R.id.addview44);

                int count = adderView.getValue();
                double pri = Double.parseDouble(shapItem.getPrice());
                double fee = count*pri;
                cat.setChange(shapItem.getName(),count-shapItem.getCount(),shapItem);
                shapItem.setCount(count);
                TextView total = view.findViewById(R.id.textView21);
                total.setText("共计"+count+"件商品");

                TextView xiaoji = view.findViewById(R.id.textView22);
                xiaoji.setText("小计：¥"+fee);
                updateCost();
            }
        });
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        cost=view.findViewById(R.id.textView20);

        List<OrderItem> lis = cat.getOrderItemList();
        log(""+(lis==null));
        log(lis.size()+"");
        if(lis!=null){
            Iterator<OrderItem> iterator = lis.iterator();
            while(iterator.hasNext()){
                iterator.next().display();
            }
        }
        log("创建购物车成功");
        if(lis!=null){
            List<ShapItem> li = ShapItem.getShapList(lis);
            shapAdapter = new ShapAdapter(getActivity(),R.id.list_order_1,li);
            listView.setAdapter(shapAdapter);
        }
        updateCost();
        return view;
    }


    public void updateCost(){
        double out=0;
        int count = shapAdapter.getCount();
        if(count==0){
            return;
        }
        for(int i=0;i<count;i++){
            ShapItem s = (ShapItem)shapAdapter.getItem(i);
            out+=Double.parseDouble(s.getPrice())*s.getCount();
        }
        cost.setText("¥："+out);
    }





    public static ShapFragment newInstance(String IPv4,String account) {
        
        Bundle args = new Bundle();
        args.putString("IPv4",IPv4);
        args.putString("account",account);
        ShapFragment fragment = new ShapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            refresh();
            log("刷新了购物车");
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        cat = (Cat) activity;
    }

    public interface Cat{
        public List<OrderItem> getOrderItemList();
        public void setChange(String name, int count, ShapItem shapItem);
        public void send();
    }

    @Override
    public String toString() {
        return "ShapFragment{" +
                "IPv4='" + IPv4 + '\'' +
                ", account='" + account + '\'' +
                ", cat=" + cat +
                ", shapAdapter=" + shapAdapter +
                '}';
    }

    public void log(String cont){
        Log.i("TAG", "log: "+cont);
    }

    public void refresh(){
        List<OrderItem> li = cat.getOrderItemList();
        show(li);
        List<ShapItem> data = ShapItem.getShapList(cat.getOrderItemList());
        showShap(data);
        shapAdapter.refresh(data);
        updateCost();
    }

    public void show(List<OrderItem> li){
        Iterator<OrderItem> iterator = li.iterator();
        while(iterator.hasNext()){
            iterator.next().display();
        }
    }

    public void showShap(List<ShapItem> li){
        Iterator<ShapItem> iterator = li.iterator();
        while(iterator.hasNext()){
            iterator.next().display();
        }
    }


}
