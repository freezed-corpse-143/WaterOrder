package com.example.qiupys.wateroder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qiupys.wateroder.tool.Check;
import com.example.qiupys.wateroder.tool.Query;

public class Register extends AppCompatActivity {
    private EditText userName;
    private EditText userPwd;
    private EditText recPwd;
    private EditText userPhone;
    private Spinner campus;
    private Spinner apartments;
    private Spinner number;
    private Button signUp;
    private TextView login;
    private Check check = new Check();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        check.query.setIPv4(getIntent().getStringExtra("IPv4"));

        userName=findViewById(R.id.input_user_r);
        userPwd=findViewById(R.id.input_password_r);
        recPwd=findViewById(R.id.input_password_r2);
        userPhone=findViewById(R.id.input_phone_r);
        campus =findViewById(R.id.spinner001);
        apartments=findViewById(R.id.spinner002);
        number=findViewById(R.id.spinner003);

        signUp=findViewById(R.id.sign_up);
        signUp.setOnClickListener(listener);

        login=findViewById(R.id.login);
        String text="已有账号？登录";
        SpannableString ss=new SpannableString(text);
        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                back();
            }
        },0,text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        login.setText(ss);
        login.setMovementMethod(LinkMovementMethod.getInstance());
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.sign_up:
                    register();
                    break;

                default:
                    break;
            }
        }
    };

    private void register() {
        if (inputeCheck()){
            String[] columns = new String[]{
                    "Account",
                    "Password",
                    "Client_phone",
                    "campus",
                    "apartment",
                    "number"
            };
            String[] values = new String[]{
                    userName.getText().toString(),
                    userPwd.getText().toString(),
                    userPhone.getText().toString(),
                    campus.getSelectedItem().toString(),
                    apartments.getSelectedItem().toString(),
                    number.getSelectedItem().toString()
            };

            boolean res = check.query.insert("Client",columns,values);
            if (res){
                toast("注册成功");
            }
            else{
                toast("注册失败");
            }
        }
    }

    public void save(){
        SharedPreferences lsp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=lsp.edit();
        editor.putString("account",userName.getText().toString());
        editor.putString("password",userPwd.getText().toString());
        editor.apply();
    }

    public void back(){
        Intent intent=new Intent(Register.this,Login.class);
        startActivity(intent);
        finish();
    }

    private boolean inputeCheck() {
        if(!check.checkRAccount(userName.getText().toString())){
            toast(check.answer);
            return false;
        }
        if(!check.checkPassword(userPwd.getText().toString())){
            toast(check.answer);
            return false;
        }
        if(!check.checkPassword(recPwd.getText().toString())){
            toast(check.answer);
            return false;
        }
        if(!check.checkSame(userPwd.getText().toString(),recPwd.getText().toString())){
            toast(check.answer);
            return false;
        }
        if(!check.checkPhone(userPhone.getText().toString())){
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
