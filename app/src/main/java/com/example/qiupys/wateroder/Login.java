package com.example.qiupys.wateroder;



import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qiupys.wateroder.tool.Check;
import com.example.qiupys.wateroder.tool.Query;

public class Login extends AppCompatActivity {
    private EditText userName;
    private EditText userPwd;
    private CheckBox checkBox;
    private EditText ipaddress;
    private Button ulogin;
    private TextView usignUp;
    private String TAG = "##########";
    boolean shouldUpdate;
    private Check check = new Check();
    private SharedPreferences loginSP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        userName=findViewById(R.id.input_user);
        userPwd=findViewById(R.id.input_password);
        ipaddress=findViewById(R.id.IPaddress);
        checkBox=findViewById(R.id.checkBox1);
        ulogin = findViewById(R.id.sign_in);
        usignUp = findViewById(R.id.sign_up);

        loginSP=getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        shouldUpdate = loginSP.getBoolean("isChecked",false);

        if(shouldUpdate){
            update();
        }

        checkBox.setChecked(shouldUpdate);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    update();
                }
            }
        });

        ulogin.setOnClickListener(listener);

        String text="没有账号？注册";
        SpannableString ss=new SpannableString(text);//对文字作显示效果处理的一个类
        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                if(!check.checkIP(ipaddress.getText().toString())){
                    toast(check.answer);
                    return;
                }
                register();
            }
        }, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        usignUp.setText(ss);
        usignUp.setMovementMethod(LinkMovementMethod.getInstance());//设置点击可激活超链接

        log(""+check.query.ConnectCheck());
    }

    private void update(){
        String user = loginSP.getString("account","123456");
        String pwd = loginSP.getString("password","666666");
        userName.setText(user);
        userPwd.setText(pwd);
    }

    private void save(){
        saveBooleanParam("isChecked",checkBox.isChecked());
        saveStringParam("account",userName.getText().toString());
        saveStringParam("password",userPwd.getText().toString());
    }

    private void saveStringParam(String name,String value){
        SharedPreferences.Editor editor=loginSP.edit();
        editor.putString(name,value);
        editor.apply();
    }

    public void saveBooleanParam(String name,boolean value){
        SharedPreferences.Editor editor=loginSP.edit();
        editor.putBoolean(name,value);
        editor.apply();
    }


    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.sign_in:
                    userLogin();
                    break;
                default:
                    break;
            }
        }
    };

    private void userLogin() {
        if (inputcheck()){
            check.query.setIPv4(ipaddress.getText().toString());
            if (check.checkLogin(userName.getText().toString(),userPwd.getText().toString())){
                toast(check.answer);
                if(checkBox.isChecked()){
                    save();
                }
                enter();
            }
        }
    }

    public void enter(){
        Intent intent=new Intent(Login.this,User.class);
        intent.putExtra("IPv4",ipaddress.getText().toString());
        intent.putExtra("username",userName.getText().toString());
        startActivity(intent);
    }

    public void register(){
        Intent intent=new Intent(Login.this,Register.class);
        intent.putExtra("IPv4",ipaddress.getText().toString());
        startActivity(intent);
    }

    //所有输入合法
    private boolean inputcheck() {
        if(accountCheck()&&pwdCheck()&&ipCheck()){
            return true;
        }
        return false;
    }

    public boolean accountCheck(){
        if(!check.checkLAccount(userName.getText().toString())){
            toast(check.answer);
            return false;
        }
        return true;
    }

    public boolean pwdCheck(){
        if(!check.checkPassword(userPwd.getText().toString())){
            toast(check.answer);
            return false;
        }
        return true;
    }

    public boolean ipCheck(){
        if(!check.checkIP(ipaddress.getText().toString())){
            toast(check.answer);
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume: 调用了onResume方法");
        super.onResume();
    }//从这个页面切出去，切换回来时，不调用onCreate，调用conResume

    @Override
    protected void onDestroy() {
        check=null;
        loginSP=null;
        super.onDestroy();
    }//销毁活动

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause: 调用了onPause方法");
        super.onPause();
    }//当本页面完全不可见时调用onPause方法

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
        Log.i(TAG, "log: "+content);
    }
}
