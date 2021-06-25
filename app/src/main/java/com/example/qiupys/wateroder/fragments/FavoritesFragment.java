package com.example.qiupys.wateroder.fragments;


import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.example.qiupys.wateroder.supplement.MyAdapter;
import com.example.qiupys.wateroder.supplement.OrderItem;
import com.example.qiupys.wateroder.R;
import com.example.qiupys.wateroder.User;
import com.example.qiupys.wateroder.tool.Query;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@RequiresApi(api = Build.VERSION_CODES.N)
public class FavoritesFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private TabLayoutMediator mediator;
    private final String[] tabs = new String[]{"全部订单", "待派送", "派送中"};
    private User user;
    private String TAG="订单";
    private Query query = new Query();
    private String account;
    private String IPv4;
    MyAdapter adapter;
    MyFragment all;
    MyFragment wait;
    MyFragment doing;


    public static FavoritesFragment newInstance(String IPv4,String account) {

        Bundle args = new Bundle();
        args.putString("IPv4",IPv4);
        args.putString("account",account);
        FavoritesFragment fragment = new FavoritesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ViewPager2.OnPageChangeCallback changeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
        }
    };
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorites, container, false);
        Bundle info = getArguments();
        account=info.getString("account");
        IPv4=info.getString("IPv4");
        query.setIPv4(IPv4);

        initDBview();
        return  view;
    }

    @Override
    public void onDestroy() {
        mediator.detach();
        viewPager2.unregisterOnPageChangeCallback(changeCallback);
        log("毁灭");
        super.onDestroy();
    }

    @Override
    public void onPause() {
        log("停止");
        super.onPause();
    }

    @Override
    public void onResume() {
        log("恢复");
        super.onResume();
    }

    public List<OrderItem> getOrderInfo(String account){
        List<OrderItem> li = new ArrayList<>();
        String[][] values = query.getInfo("[WaterOrder].[dbo].[Order]","Account",account,
                new String[]{
                        "Order_id","Order_status","Start_time","Account","Deliveryman_id"
                });
        if(values!=null){
            for(int i =0;i<values.length;i++){
                OrderItem item = new OrderItem(values[i]);
                li.add(item);
            }
        }
        return li;
    }


    public void initDBview(){
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager2 = view.findViewById(R.id.view_pager);

        //禁用预加载
        viewPager2.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        //Adapter
        List<OrderItem> orderItems = getOrderInfo(account);

        fragments.clear();

        all = new MyFragment(getActivity(),orderItems);
        fragments.add(all);
        wait = new MyFragment(getActivity(),getSpecialOrder(orderItems,"待派送"));
        fragments.add(wait);
        doing = new MyFragment(getActivity(),getSpecialOrder(orderItems,"派送中"));
        fragments.add(doing);
        adapter = new MyAdapter(getActivity().getSupportFragmentManager(),getLifecycle(),fragments);

        viewPager2.setAdapter(adapter);



        //viewPager 页面切换监听
        viewPager2.registerOnPageChangeCallback(changeCallback);

        mediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                //这里可以自定义TabView
                tab.setText(tabs[position]);
            }
        });
        //要执行这一句才是真正将两者绑定起来
        mediator.attach();
    }


    public List<OrderItem> getSpecialOrder(List<OrderItem> list,String status){
        List<OrderItem> li = new ArrayList<>();
        Iterator iterator = list.iterator();
        while(iterator.hasNext()){
            OrderItem item = (OrderItem) iterator.next();
            if(item.getOrderStatus().equals(status)){
                li.add(item);
            }
        }
        return li;
    }
    public void log(String content){
        Log.i(TAG, "log: "+content);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            log("刷新了");
            List<OrderItem> orderItems = getOrderInfo(account);

            log(""+orderItems.size());
            all.update(orderItems);
            wait.update(orderItems);
            doing.update(orderItems);
        }
    }
}
