package com.example.qiupys.wateroder.tool;

//import android.util.Log;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

public class Query implements Runnable{
    private  String LOGIN_URL = "http://";

    public String getIPv4() {
        return IPv4;
    }

    public String IPv4="127.0.0.1";
    private String Port="8080";
    private  String WEB_NAME = "demo2_war_exploded/she";
    private final  String TAG = "HttpConnect";
    private  Thread t = new Thread(this);
    private String data;
    private boolean out;
    private String result;

    public void setIPv4(String IPv4) {
        this.IPv4 = IPv4;
    }

    public String connect(String data){
//        Log.i(TAG, "Connect: ");
        String msg = "";
        try{
            String u = LOGIN_URL+IPv4+":"+Port+"/"+WEB_NAME;
            Log.i(TAG, "connect: "+":"+u);
            URL url = new URL(u);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            //set the request way
            connection.setRequestMethod("POST");

            //set info of TimeOut
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);

            //set the property of whether can I input
            connection.setDoInput(true);
            // the premission the of output
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            //the data we request
//            String data = "password="+ URLEncoder.encode(password,"UTF_8")+
//                    "&username="+URLEncoder.encode(username,"UTF_8");

            //get outputstream
            Log.i(TAG, "connect: the ipv4 using is "+IPv4);
            OutputStream out = connection.getOutputStream();

            out.write(data.getBytes());
            out.flush();
            out.close();
            connection.connect();

            if(connection.getResponseCode()==200){
                //get the the relected input object
                InputStream in = connection.getInputStream();
                //create the outputstream
                ByteArrayOutputStream message = new ByteArrayOutputStream();
                //define the read length
                int len = 0;
                //define the buffer
                byte buffer[] = new byte[1024];
                //cycle read
                while((len=in.read(buffer))!=-1){
                    //write the info to os object
                    message.write(buffer,0,len);
                }
                //release the space
                in.close();
                message.close();
                // return String
                msg = new String(message.toByteArray());
                return msg;
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return msg;
    }

    public void excute(){
        t = new Thread(this);
        try{
            t.start();
            t.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public boolean ConnectCheck(){
        out = true;
        data = "task="+encode("connectCheck")+"&"+"content="+encode("connectCheck");
        excute();
        if(result==null||!result.equals("success")){
            out=false;
        }
        return out;
    }

    public String getWhereQuery(String[] columns, String[] values){
        String res = " where ";
        if(columns.length==1){
            res += columns[0]+"='"+values[0]+"'";
            return res;
        }
        int i;
        for(i = 0;i<columns.length-1;i++){
            res += columns[i] +"='"+values[i]+"' and ";
        }
        res += columns[i]+"='"+values[i]+"'";
        return res;
    }

    public String getColumnBarQuery(String[] columns){
        String res = " (";
        if(columns.length==1){
            res +=columns[0]+") ";
            return res;
        }
        int i;
        for(i = 0;i<columns.length-1;i++){
            res += columns[i]+",";
        }
        res += columns[i]+") ";
        return res;
    }

    public String getColumnQuery(String[] columns){
        String res = " ";
        if(columns.length==1){
            res+=columns[0]+" ";
            return res;
        }
        int i;
        for(i = 0;i<columns.length-1;i++){
            res += columns[i]+",";
        }
        res += columns[i]+" ";
        return res;
    }

    public String getValueBarQuery(String[] values){
        String res = " (";
        if(values.length==1){
            res+="'"+values[0]+"') ";
            return res;
        }
        int i;
        for(i = 0;i<values.length-1;i++){
            res += "'"+ values[i]+"',";
        }
        res += "'"+ values[i]+"') ";
        return res;
    }

    public String returnMethod(String task, String content){
        data="task="+encode(task)+"&content="+encode(content);
        excute();
        return result;
    }

    public String returnMethod(String task, String content, String targets){
        data="task="+encode(task)+"&content="+encode(content)+targets;
        excute();
        return result;
    }

    public String[] integrate(String[] one, String two){
        String[] out = new String[one.length+1];
        int i;
        for(i=0;i<one.length;i++){
            out[i]=one[i];
        }
        out[i]=two;
        return out;
    }

    public String  combine(String target, String[] columns){
        String out = "";
        for(String i:columns){
            out += "&"+target+i;
        }
        return out;
    }

    public String gathertargets(String[] columns){
        String out = "";
        for(String i : columns){
            out += "&target="+encode(i);
        }
        return out;
    }

    public boolean booleanMethod(String task,String content,String targets){
        data ="task="+encode(task)+"&content="+encode(content)+targets;
        excute();
        return result.equals("success");
    }

    public boolean booleanMethod(String task,String content){
        data ="task="+encode(task)+"&content="+encode(content);
        System.out.println(data);
        excute();
        return result.equals("success");
    }

    public boolean exists(String table,String column,String value){
        String[] columns = new String[]{column};
        String[] values = new String[]{value};
        return exists(table,columns,values);
    }

    public boolean exists(String table,String[] columns, String[] values){
        String content = "select count(*) as num from "+ table+ getWhereQuery(columns,values)+";";
        String targets = gathertargets(new String[]{"num"});
        return booleanMethod("exist",content,targets);
    }

    public boolean insert(String table,String[] columns,String[] values){
        boolean ou = false;
        String content = "insert into "+table+" "+getColumnBarQuery(columns)+"values"+ getValueBarQuery(values)+";";
        Log.i(TAG, "insert: --------"+content);
        if(!exists(table,columns,values)&&
                booleanMethod("insert",content)&&
                exists(table,columns,values)){
                out = true;
        }
        return out;
    }

    public boolean insert(String table,String column,String value){
        String[] columns = new String[]{column};
        String[] values = new String[]{value};
        return insert(table,columns,values);
    }

    public boolean update(String table,String column,String value,String targetColumn,String targetValue){
        String[] columns = new String[]{column};
        String[] values = new String[]{value};
        return update(table,columns,values,targetColumn,targetValue);
    }

    public boolean update(String table, String[] columns,String[] values,String targetColumn,String targetValue){
        boolean out = false;
        String content = "update "+table+" set "+targetColumn+"='"+targetValue+"' "+getWhereQuery(columns,values)+";";
        if(exists(table,columns,values)&&
                booleanMethod("update",content)&&
                exists(table,integrate(columns,targetColumn),integrate(values,targetValue))){
            out = true;
        }
        return out;
    }

    public boolean delete(String table, String column,String value){
        String[] columns = new String[]{column};
        String[] values = new String[]{value};
        return delete(table,columns,values);
    }

    public boolean delete(String table,String[] columns, String[] values){
        boolean out = false;
        String content = "delete from "+table+getWhereQuery(columns,values)+";";
        if(exists(table,columns,values)&&
                booleanMethod("delete",content)&&
                !exists(table,columns,values)){
                out = true;
        }
        return out;
    }

    public String[][] getTable(String content){
        if(content.equals("")){
            return null;
        }
        String[] one = content.split("&");
        int rows = one.length;
        int columns = one[0].split("#").length;
        String[][] out = new String[rows][columns];
//        Log.i(TAG, "getTable: "+content);
        for(int i=0;i<rows;i++){
            String[] two = one[i].split("#");
//            Log.i(TAG, "getTable: "+Arrays.toString(two));
            for(int j=0;j<columns;j++){
                out[i][j]=two[j];
            }
        }
        return out;
    }

    public String[][] getInfo(String table, String[] columns, String[] values, String[] targetColumns){
        String content = "select "+ getColumnQuery(targetColumns) +" from "+ table + getWhereQuery(columns,values);
        String targets = gathertargets(targetColumns);
        Log.i(TAG, "getInfo: "+content);
        String o = returnMethod("getInfo",content,targets);
        return getTable(o);
    }

    public String[][] getInfo(String table, String[] targetColumns){
        String content = "select "+ getColumnQuery(targetColumns) +" from "+ table;
        String targets = gathertargets(targetColumns);
        String o = returnMethod("getInfo",content,targets);
        return getTable(o);
    }

    public String[][] getInfo(String table, String column, String value, String[] targetColumns){
        String[] columns = new String[]{column};
        String[] values = new String[]{value};
        return getInfo(table,columns,values,targetColumns);
    }

    public String[][] getInfo(String table, String[] columns, String[] values, String targetColumn){
        String[] targetColumns = new String[]{targetColumn};
        return getInfo(table,columns,values,targetColumns);
    }

    public String[][] getInfo(String table, String column, String value, String targetColumn){
        String[] columns = new String[]{column};
        String[] values = new String[]{value};
        String[] targetColumns = new String[]{targetColumn};
        return getInfo(table,columns,values,targetColumns);
    }

    public String getCount(String table, String[] columns, String[] values){
        String content ="select count(*) as num from "+table+getWhereQuery(columns,values)+";";
        String targets = gathertargets(new String[]{"num"});
        return returnMethod("getCount",content,targets);
    }

    public String getCount(String table, String column, String value){
        String[] columns = new String[]{column};
        String[] values = new String[]{value};
        return getCount(table,columns,values);
    }

    @Override
    public void run() {
        result = connect(data);
    }

    public String encode(String content){
        String test = "";
        try {
            test = URLEncoder.encode(content,"UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return test;
    }

    public static void main(String[] args){
        Query t = new Query();
        t.setIPv4("10.63.45.67");
        String table = "Client";
        String column = "Account";
        String value = "123456";
        String targetColumn ="user";
        String targetValue ="lili";
        String[] targetColumns = new String[]{"user","kk"};
        String[] columns = new String[]{"Account","Password","Client_name","Client_address","Client_phone"};
        String[] c = new String[]{"柳林","梅园","304"};
//        String[][] r = t.getInfo("Client",new String[]{"campus","apartment","number"},c,"Account");
//        t.show(r);
        System.out.println(t.ConnectCheck());
    }

    public void show(String[][] con){
        for(int i = 0;i<con.length;i++){
            System.out.println();
            String[] one = con[i];
            for(String j:one){
                System.out.print(j+" ");
            }
        }
    }
    public String display(String[][] con){
        String res="";
        for(int i = 0;i<con.length;i++){
            res+="\n";
            String[] one = con[i];
            for(String j:one){
                res+=j+" ";
            }
        }
        return res;
    }

}
