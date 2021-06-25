package com.example.qiupys.wateroder.supplement;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShapItem {
    private String name;
    private String price;
    private int count;

    public static List<ShapItem> getShapList(List<OrderItem> values){
        List<ShapItem> li = new ArrayList<ShapItem>();
        Iterator<OrderItem> iterator = values.iterator();
        while(iterator.hasNext()){
            OrderItem i = iterator.next();
            ShapItem w = findByNameInShapList(li,i.getName());
            if(w==null){
                w = new ShapItem(i.getName(),i.getPrice(),1);
                li.add(w);
            }else{
                w.setCount(w.getCount()+1);
            }

        }
        return li;
    }

    public static ShapItem findByNameInShapList(List<ShapItem> li, String name){
        ShapItem out = null;
        Iterator<ShapItem> iterator = li.iterator();
        while(iterator.hasNext()){
            ShapItem i = iterator.next();
            if(i.getName().equals(name)){
                out=i;
                break;
            }
        }
        return out;
    }



    public ShapItem(String name, String price,int count){
        this.name=name;
        this.price=price;
        this.count=count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ShapItem{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", count=" + count +
                '}';
    }

    public void display(){
        Log.i("TAG", "display: "+toString());
    }
}
