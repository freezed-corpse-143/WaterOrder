package com.example.qiupys.wateroder.fragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.qiupys.wateroder.EditProfile;
import com.example.qiupys.wateroder.R;
import com.example.qiupys.wateroder.tool.Query;

import java.util.Arrays;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ProfileFragment extends Fragment {
    private TextView username;
    private TextView phone;
    private TextView address;
    private TextView card;
    private TextView totalAKS;
    private TextView balance;
    private Query query = new Query();
    private ImageView settings;
    private String account;
    private String IPv4;

     public static ProfileFragment newInstance(String IPv4,String username) {
//         Log.i("TAG", "ProfileFragment newInstance: "+IPv4+"　"+username);
        Bundle args = new Bundle();
        args.putString("IPv4",IPv4);
        args.putString("username",username);
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Bundle info = getArguments();
        account=info.getString("username");
        IPv4=info.getString("IPv4");
        query.setIPv4(IPv4);

        username=view.findViewById(R.id.username);
        phone=view.findViewById(R.id.phone);
        address=view.findViewById(R.id.address);

        balance=view.findViewById(R.id.balance);
        totalAKS=view.findViewById(R.id.total_AKS);

        fill();
        settings=view.findViewById(R.id.setting);
        settings.setOnClickListener(listener);

        return view;
    }

    private void fill() {
        username.setText(this.account);
        Log.i("TAG", "fill: -----"+(query.ConnectCheck()));
        String[][] values = query.getInfo("Client","Account",account,
                new String[]{"Client_phone","campus","apartment","number"});
//        Log.i("TAG", "fill: IPv4"+query.IPv4);
        Log.i("TAG", "fill: "+ Arrays.toString(values[0]));
        phone.setText(values[0][0]);
        address.setText(values[0][1]+values[0][2]+values[0][3]);
        totalAKS.setText(getEmptyBarrel(values[0][1],values[0][2],values[0][3])+"");
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("TAG", "--------------------------"+"chenggong");
            switch(view.getId()){

                case R.id.setting:
                    Intent intent=new Intent(getActivity(), EditProfile.class);
                    intent.putExtra("account",account);
                    intent.putExtra("IPv4",IPv4);

                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

     private double getEmptyBarrel(String campus, String apartment,String number){
         double out =0;
         String[][] values = query.getInfo("Client",new String[]{"campus","apartment","number"}
                    ,new String[]{campus,apartment,number},
                    "Account");
         Log.i("TAG", "getEmptyBarrel: 查询结果"+query.display(values));
        for(int i =0;i<values.length;i++){
            out += getEmptyBarrelByAccount(values[i][0]);
        }
        return out;
     }

     private double getEmptyBarrelByAccount(String account){
         double i = 0;
         String value = query.getCount("[WaterOrder].[dbo].[Order]","Account",account);
         return Double.parseDouble(value);
     }

}
