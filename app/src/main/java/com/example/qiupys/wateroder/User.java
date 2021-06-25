package com.example.qiupys.wateroder;


import android.os.Build;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.qiupys.wateroder.fragments.FavoritesFragment;
import com.example.qiupys.wateroder.fragments.GoodsFragment;
import com.example.qiupys.wateroder.fragments.ProfileFragment;
import com.example.qiupys.wateroder.fragments.ShapFragment;
import com.example.qiupys.wateroder.supplement.OrderItem;
import com.example.qiupys.wateroder.supplement.RateItem;
import com.example.qiupys.wateroder.supplement.ShapItem;
import com.example.qiupys.wateroder.tool.Check;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@RequiresApi(api = Build.VERSION_CODES.N)
public class User extends AppCompatActivity implements ShapFragment.Cat, GoodsFragment.Car {
    public String username="123456";
    private GoodsFragment goodsFragment;
    private FavoritesFragment favoritesFragment;
    private ProfileFragment profileFragment;
    private ShapFragment shapFragment;
    private String TAG = "user";
    public String cost;
    public String IPv4="10.63.199.230";
    private Check check;
    private List<Fragment> fragmentList;
    private String tag;
    public List<OrderItem> li=new ArrayList<>();

    @Override
    public void toshap() {
        hide(getFragmentByTag(tag));
        show(getFragmentByTag("shap"),"shap");
    }

    @Override
    public void send() {
        goodsFragment.send(li);
    }

    @Override
    public void backData(List<OrderItem> li) {
        this.li=li;
        log("接收完成");
        show(this.li);
    }

    @Override
    public void setChange(String name, int count, ShapItem shapItem) {
        if(count==0){
            return;
        }
        if(count<0){
            for(int i=0;i>count;i--){
                log("展示之前");
                show(li);
                log("展示之后");
                log("寻找前");
                int d = findOrderItemByName(name,li);
                log("name找到的第"+d+"号");
                li.remove(findOrderItemByName(name,li));
                log("修改之后");
                show(li);
            }
        }
        if(count>0){
            for(int i=0;i<count;i++){
                RateItem item = new RateItem();
                item.setPrice(shapItem.getPrice());
                item.setName(shapItem.getName());
                goodsFragment.addOrderItem(item);
            }
        }
    }

    public int findOrderItemByName(String name,List<OrderItem> li){
        int out =-1;
        int i;
        log("开始寻找之前");
        show(li);
        for(i=0;i<li.size();i++){
            if(li.get(i).getName().equals(name)){
                break;
            }
        }
        log("寻找结果："+i);
        return i;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user);

        username = getIntent().getStringExtra("username");
        IPv4 = getIntent().getStringExtra("IPv4");
//        username="45877";
//        IPv4="10.63.110.233";

        profileFragment=ProfileFragment.newInstance(IPv4,username);

        add(profileFragment,"profile");
        show(profileFragment,"profile");
        goodsFragment=GoodsFragment.newInstance(IPv4,username);
        add(goodsFragment,"goods");
        hide(goodsFragment);

        favoritesFragment=FavoritesFragment.newInstance(IPv4,username);
        add(favoritesFragment,"favorite");
        hide(favoritesFragment);

        shapFragment=ShapFragment.newInstance(IPv4,username);
        add(shapFragment,"shap");
        hide(shapFragment);
        fragmentList=getSupportFragmentManager().getFragments();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(bottomNav.getMenu().getItem(3).getItemId());
    }

    public void add(Fragment fragment,String tag){
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragment,tag).commit();
    }

    public void hide(Fragment fragment){
        if(fragment==null){
            return;
        }
        getSupportFragmentManager().beginTransaction().hide(fragment).commit();
    }

    public void show(Fragment fragment,String tag){
        if(fragment==null){
            return;
        }
        this.tag=tag;
        getSupportFragmentManager().beginTransaction().show(fragment).commit();
    }

    public Fragment getFragmentByTag(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.nav_goods:
                            if(goodsFragment.isVisible()){
                                break;
                            }
                            hide(getFragmentByTag(tag));
                            show(getFragmentByTag("goods"),"goods");
                            break;
                        case R.id.nav_aks:
                            if(shapFragment.isVisible()){
                                break;
                            }
                            hide(getFragmentByTag(tag));
                            show(getFragmentByTag("shap"),"shap");
                            break;
                        case R.id.nav_favorites:
                            if(favoritesFragment.isVisible()){
                                break;
                            }
                            hide(getFragmentByTag(tag));
                            show(getFragmentByTag("favorite"),"favorite");
                            break;
                        case R.id.nav_profile:
                            if(profileFragment.isVisible()){
                                break;
                            }
                            hide(getFragmentByTag(tag));
                            show(getFragmentByTag("profile"),"profile");
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            };

    public void log(String content){
        Log.i(TAG, "log: "+content);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    public List<OrderItem> getList() {
        return this.li;
    }


    @Override
    public List<OrderItem> getOrderItemList() {
        return this.li;
    }

    public void show(List<OrderItem> li){
        Iterator<OrderItem> iterator = li.iterator();
        while(iterator.hasNext()){
            iterator.next().display();
        }
    }
}