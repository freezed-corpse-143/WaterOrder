package com.example.qiupys.wateroder.fragments;


import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.qiupys.wateroder.supplement.CustomAdapter;
import com.example.qiupys.wateroder.supplement.OrderItem;
import com.example.qiupys.wateroder.R;
import com.example.qiupys.wateroder.supplement.RateItem;
import com.example.qiupys.wateroder.tool.Query;
import com.example.qiupys.wateroder.tool.Van;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;



@RequiresApi(api = Build.VERSION_CODES.N)
public class GoodsFragment extends Fragment{
    int [] IMAGES = {R.drawable.water1, R.drawable.water2, R.drawable.water3, R.drawable.water4};
    private String IPv4;
    private String account;
    private Query  query = new Query();

    private BottomSheetDialog bottomSheet;
    ListView listView;
    TextView textView8;
    Button button;
    double fee=0;
    ArrayList<RateItem> list;
    private String TAG = "---------";
    private Car car;

    private List<OrderItem> orderItems = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        car = (Car) activity;
    }

    public static GoodsFragment newInstance(String IPv4, String account) {
        Bundle args = new Bundle();
        args.putString("IPv4",IPv4);
        args.putString("account",account);
        GoodsFragment fragment = new GoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ArrayList<RateItem> getGoodInfo(){
        ArrayList<RateItem> li = new ArrayList<>();
        String[][] values = query.getInfo("Water",new String[]{"Water_image",
            "Water_name","Price","Water_surplus","Supplier_id","WareHouse_id","Water_id"});
//        show(values);
        for(int i = 0;i<values.length;i++){
            RateItem item = new RateItem(values[i]);
            li.add(item);
        }
        return li;
    }

    public int getImageByName(String name){
        ApplicationInfo appInfo = getActivity().getApplicationInfo();
        int resID = getResources().getIdentifier(name, "drawable", appInfo.packageName);
        return resID;
    }

    public void send(List<OrderItem> orderItems1){
        Iterator<OrderItem> iterator = orderItems1.iterator();
        while(iterator.hasNext()){
            OrderItem item = iterator.next();
            item.display();

            boolean r = query.insert("[WaterOrder].[dbo].[Order]",new String[]{
                    "Order_id","Order_status","Start_time","Account","Water_id"
            },new String[]{
                    item.getOrderId(),item.getOrderStatus(),item.getStartTime(),item.getAccount(),item.getWaterid()
            });
            log(Arrays.toString(new String[]{
                    item.getOrderId(),item.getOrderStatus(),item.getStartTime(),item.getAccount(),item.getWaterid()
            }));
        }
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods, container, false);
        Bundle info = getArguments();
        account=info.getString("account");
        IPv4=info.getString("IPv4");
        query.setIPv4(IPv4);

        listView = view.findViewById(R.id.list_goods);
        textView8= view.findViewById(R.id.textView8);
        button = view.findViewById(R.id.button2);




        list=getGoodInfo();
        for(int i = 0;i<4;i++){
            int img = getImageByName(list.get(i).getName());
            list.get(i).setImage(img);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                send(orderItems);
                car.toshap();
            }
        });
        CustomAdapter customAdapter = new CustomAdapter(list);
        customAdapter.setInflater(getLayoutInflater());

        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object itemAtPosition = listView.getItemAtPosition(i);
                RateItem item = (RateItem) itemAtPosition;
                String lastfee = textView8.getText().toString();
                fee = Double.parseDouble((lastfee.equals("")?"0":lastfee));
                fee += Double.parseDouble(item.getPrice());
                log("-"+fee+"-");
                textView8.setText(fee+"");
                addOrderItem(item);
                log("送出去了");
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object itemAtPosition = listView.getItemAtPosition(i);
                RateItem item = (RateItem) itemAtPosition;
                showBottomSheetDialog(item);
                return true;
            }
        });

        return view;
    }

    public void addOrderItem(RateItem item){
        OrderItem orderItem = new OrderItem();
        orderItem.setAccount(account);
        orderItem.setOrderStatus("待派送");
        String datetime = getOrderTime();
        orderItem.setStartTime(datetime);
        orderItem.setDeliveryManId("");
        orderItem.setOrderId(""+datetime.hashCode());
        orderItem.setName(item.getName());
        orderItem.setPrice(item.getPrice());
        orderItem.setWaterid(standarString(orderItem.getOrderId().hashCode()+""));
        orderItems.add(orderItem);
        car.backData(orderItems);
    }


    public String standarString(String in){
        if(in.length()>10){
            return in.substring(0,10);
        }
        if(in.length()<10){
            for(int i=0;i<10-in.length();i++){
                in+="X";
            }
        }
        return in;
    }


    public String getOrderTime(){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");// HH:mm:ss//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        String datetime = simpleDateFormat.format(date);
        return datetime;
    }

    //使用BottomSheetDialog方式实现底部弹窗
    void showBottomSheetDialog(RateItem item){
        bottomSheet = new BottomSheetDialog(getActivity());
        bottomSheet.setCancelable(true);//设置点击外部是否可以取消
        bottomSheet.setContentView(R.layout.bottom_test);//设置对框框中的布局
        TextView name = bottomSheet.findViewById(R.id.goodname);
        TextView price = bottomSheet.findViewById(R.id.goodprice);
        TextView surplus = bottomSheet.findViewById(R.id.goodsurplus);
        TextView spplier = bottomSheet.findViewById(R.id.goodsupplier);
        name.setText("名称："+item.getName());
        price.setText("价格："+item.getPrice());
        surplus.setText("容量："+item.getSurplus());
        spplier.setText("供应商："+item.getSupplier());
        Button cancle = bottomSheet.findViewById(R.id.cancel);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheet.cancel();
            }
        });
        bottomSheet.show();//显示弹窗
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    public void log(String content){
        Log.i(TAG, "log: "+content);
    }

    public void show(String[][] con){
        String res = "";
        for(int i = 0;i<con.length;i++){
            res+="\n";
            String[] one = con[i];
            for(String j:one){
                res+=j+" ";
            }
        }
        Log.i(TAG, "show: "+res);
    }

    public interface Car{
        public void backData(List<OrderItem> li);
        public void toshap();
    }
}
