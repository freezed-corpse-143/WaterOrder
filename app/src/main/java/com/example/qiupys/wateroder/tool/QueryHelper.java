package com.example.qiupys.wateroder.tool;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.qiupys.wateroder.supplement.OrderItem;
import com.example.qiupys.wateroder.supplement.RateItem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class QueryHelper implements Runnable{
    private static final String TAG = "连接数据库测试";
    private String IPv4;
    private static final String IPv6 = "fe80::31c4:2d48:191b:f41f%10";
    private static final String DBNAME = "WaterOrder";
    private static final String USER = "sa";
    private static final String PASSWORD = "123";
    private Statement ps=null;
    private Connection ct=null;
    private ResultSet rs=null;
    private String SQLquery;
    private String res=null;
    private int task;
    private String name;
    private String password;
    boolean out;
    private ArrayList<RateItem> list = new ArrayList<>();
    private RateItem item;
    private String target;
    private int x;
    private boolean success;
    private List<OrderItem> orderinfo= new ArrayList<>();
    private String head = "jdbc:jtds:sqlserver://";
    private String end = ";useunicode=true;characterEncoding=UTF-8";
    private String info;
    private String filter;

    public QueryHelper(){
        this("10.63.89.148");
    }

    public QueryHelper(String IPv4){
        this.IPv4 =IPv4;
        Excute(-1);
        Log.i(TAG, "QueryHelper: 连接成功");
    }




    public void generate(){
        try {
            Log.i(TAG, "generate: "+"11111");
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String url = head + IPv4 + ":1433/" + DBNAME + end;
            ct = DriverManager.getConnection(url,USER,PASSWORD);
            Log.i(TAG, "ct是否为空 "+(ct==null));
            ps = ct.createStatement();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean accountExists(String Account){
        out = false;
        SQLquery = "select * from Client where Account = "+Account;
        res = null;
        Excute(0);
        if(rs!=null){
            out = true;
        }
        return out;
    }
    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
            }
        }
    };
    private Message msg = handler.obtainMessage();

    @Override
    public void run() {
    }

    public boolean loginCheck(String account,String password){
        out = false;
        SQLquery = "select * from Client where Account = "+account + " And Password = "+password;
        res = null;
        target = "Account";
        Excute(0);
        if(res!=null){
            out = true;
        }
        return out;
    }

    public void queryExists(){
        try {
            rs = ps.executeQuery(SQLquery);
            if(rs==null){
                return;
            }
            while(rs.next()){
                res += rs.getString(target)+"\n";
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void queryExcute(){
        try {
            rs = ps.executeQuery(SQLquery);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void queryGetClientInfo(){
        try {
            rs = ps.executeQuery(SQLquery);
            if(rs==null){
                return;
            }
            while(rs.next()){
                info = rs.getString(filter);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ExcuteQuery(){
        try {
            success=ps.execute(SQLquery);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void queryGetGoodInfo(){
        try {
            rs = ps.executeQuery(SQLquery);
            if(rs==null){
                return;
            }
            while(rs.next()){
                item = new RateItem(rs.getInt("Water_image"),
                        rs.getString("Water_name"),rs.getString("Price"),
                        rs.getString("Water_surplus"),rs.getString("Supplier_id"),
                        rs.getString("WareHouse_id"));
                list.add(item);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void queryGetOrderInfo(){
        try {
            rs = ps.executeQuery(SQLquery);
            if(rs==null){
                return;
            }
            while(rs.next()){
                OrderItem orderItem = new OrderItem(rs.getString("Order_id"),
                        rs.getString("Order_status"),rs.getString("Start_time"),
                        rs.getString("Account"),rs.getString("Deliveryman_id"));
                orderinfo.add(orderItem);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void Excute(int n){
        task = n;
        Thread t = new Thread(){
            @Override
            public void run() {
                switch (task){
                    case -1:
                        generate();
                        break;
                    case 0:
                        queryExists();
                        break;
                    case 1:
                        queryExcute();
                        break;
                    case 2:
                        queryGetGoodInfo();
                        break;
                    case 3:
                        ExcuteQuery();
                        break;
                    case 4:
                        queryGetOrderInfo();
                        break;
                    case 5:
                        queryGetClientInfo();
                        break;
                    default:
                        break;
                };
            }
        };
        try{
            t.start();
            t.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public String getRes() {
        return res;
    }


    public boolean findUserByName(String name){
        out = false;
        SQLquery = "select * from Client where Account = "+"'"+name+"'";
        res = null;
        target = "Account";
        Excute(0);
        if(res!=null){
            out = true;
        }
        return out;
    }


    public boolean findOrderInfoByOrderId(String orderId){
        out = false;
        SQLquery = "select * from [WaterOrder].[dbo].[Order] where Order_id = "+"'"+orderId+"'";
        res = null;
        target = "Order_id";
        Excute(3);
        if(res!=null){
            out = true;
        }
        return out;
    }



    public byte insertNewAccount(String name,String pwd,String phone,String address){
        name="213255";
        pwd="155";
        phone="55555555";
        address="aadd";
        Log.i(TAG, "insertNewAccount: 找没找到重名"+(findUserByName(name)));
        if(findUserByName(name)){
            return 0;
        }
        SQLquery = "insert into Client (Account,Password,Client_phone,Client_address,Client_name,Dormitory_id) values "+
                "("+"'"+name+"'"+","+"'"+pwd+"'"+","+"'"+phone+"'"+","+"'"+address+"'"+",'','');";
        Excute(1);
        if(findUserByName(name)){
            return 1;
        }
//        Log.i(TAG, "insertNewAccount: "+"插入失败");
        return 0;
    }


    public ArrayList<RateItem> getRateItem(){
        SQLquery = "select * from Water";
//        Log.i(TAG, "getRateItem: "+(rs==null));
        list.clear();
        Excute(2);
//        Log.i(TAG, "getRateItem: "+(rs==null));
        Log.i(TAG, "getRateItem: 获得链表长度"+list.size());
        return list;
    }

    public byte insertOrderInfo(OrderItem item){
        String orderId = item.getOrderId();
        String orderStatus = item.getOrderStatus();
        String startTime = item.getStartTime();
        String account = item.getAccount();
        String deliveryManId = item.getDeliveryManId();
        if(findOrderInfoByOrderId(orderId)){
            return 0;
        }
        SQLquery = "insert [WaterOrder].[dbo].[Order] values('"+orderId+"','"+
                orderStatus+"','"+startTime+"','"+account+"','"+deliveryManId+"','')";
        Excute(3);
        if(findOrderInfoByOrderId(orderId)){
            return 1;
        }
        return 0;
    }

    public List<OrderItem> getOrderInfoByAccount(String account){
        orderinfo.clear();
        SQLquery = "select * from [WaterOrder].[dbo].[Order] where Account = '"+account+"'";
        Log.i(TAG, "getOrderInfoByAccount: "+SQLquery);
        Excute(4);
        Log.i(TAG, "getOrderInfoByAccount: "+SQLquery);
        return orderinfo;
    }

    public String getClientInfoByAccount(String username,String fileter){
        info = "";
        this.filter = fileter;
        SQLquery = "select * from [WaterOrder].[dbo].[Client] where Account = '"+username+"'";
        Excute(5);
        return info;
    }

    public String getBarrelsInfoByAccount(String username){
        info = "";
        this.filter="num";
        SQLquery="select COUNT(*) as num from [WaterOrder].[dbo].[Order] A,(select Account from Client where Dormitory_id = (select Dormitory_id from Client where Account = '"+username
                +"')) B where A.Account=B.Account and A.Order_status='已完成' and A.Recycle = '否';";
        Excute(5);
        return info;
    }

    public boolean updateClientInfoByAccount(String account, String columns,String value){
        SQLquery = "update Client set "+columns+" = '"+value+"' where Account = '"+account+"';";
        Excute(3);
        return success;
    }

    public boolean isConnected(){
        return ct != null;
    }
    
}
