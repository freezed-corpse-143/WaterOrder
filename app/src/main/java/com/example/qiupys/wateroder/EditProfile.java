package com.example.qiupys.wateroder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qiupys.wateroder.tool.Check;

import java.util.Arrays;

public class EditProfile extends AppCompatActivity {
    private EditText profilename;
    private EditText phone;
    private Spinner campus;
    private Spinner apartment;
    private Spinner number;
    private String account;
    private String IPv4;
    private Button submit;
    private ImageView back;
    private Check check = new Check();

    public String[] getInfoByAccount(String account){
        String[][] values= check.query.getInfo("Client","Account",account,
                new String[]{
                        "Client_name","Client_phone","campus","apartment","number"
                });
        return values[0];
    }

    public int getCampusPosition(String value){
        String[] values = getResources().getStringArray(R.array.campus);
        int i;
        log("---"+Arrays.toString(values));
        for(i=0;i<value.length();i++){
            if(value.equals(values[i])){
                break;
            }
        }
        return i-1;
    }

    public int getApartmentPosition(String value){
        String[] values = getResources().getStringArray(R.array.apartment);
        int i;
        for(i=0;i<value.length();i++){
            if(value.equals(values[i])){
                break;
            }
        }
        return i-1;
    }

    public int getNumberPosition(String value){
        String[] values = getResources().getStringArray(R.array.number);
        int i;
        for(i=0;i<value.length();i++){
            if(value.equals(values[i])){
                break;
            }
        }
        return i-1;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        Intent intent =getIntent();
        account = intent.getStringExtra("account");
        IPv4=intent.getStringExtra("IPv4");
        check.query.setIPv4(IPv4);


        submit=findViewById(R.id.submit);
        back=findViewById(R.id.back);
        profilename=findViewById(R.id.profile_name);
        phone=findViewById(R.id.profile_phone);
        campus=findViewById(R.id.spinner004);
        apartment=findViewById(R.id.spinner005);
        number=findViewById(R.id.spinner006);


        String[] values = getInfoByAccount(account);
        log("################ "+ Arrays.toString(values));
        profilename.setText(values[0]);
        phone.setText(values[1]);
        int c = getCampusPosition(values[2]);
        campus.setSelection(c);

        int a = getApartmentPosition(values[3]);
        apartment.setSelection(a);

        int n = getNumberPosition(values[4]);
        number.setSelection(n);


        submit.setOnClickListener(listener);
        back.setOnClickListener(listener);
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.submit:
                    infosubmit();
                    break;
                case R.id.back:
                    Intent intent=new Intent(EditProfile.this,User.class);
                    intent.putExtra("IPv4",IPv4);
                    intent.putExtra("username",account);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    public void update(String column, String value){
        check.query.update("Client","Account",account,column,value);
    }

    private void infosubmit() {
        if(inputeCheck()){
            update("Client_name",profilename.getText().toString());
            update("Client_phone",phone.getText().toString());
            toast("更新成功");
        }

    }



    private boolean inputeCheck() {
        if(check.checkName(profilename.getText().toString())){
            toast(check.answer);
            return false;
        }
        if(check.checkPhone(phone.getText().toString())){
            toast(check.answer);
            return false;
        }
        return true;
    }

    public void toast(String notice){
        if(notice==null){
            return;
        }
        if(notice.equals("")){
            return;
        }
        Toast.makeText(this, notice, Toast.LENGTH_SHORT).show();

    }

    public void log(String content){
        Log.i("TAG", "log: "+content);
    }
}
