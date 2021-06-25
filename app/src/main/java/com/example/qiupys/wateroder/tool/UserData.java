package com.example.qiupys.wateroder.tool;

public class UserData {
    private String User_name;//名字
    private String User_pwd;//密码
    private String Phone;//手机
    private String Address;//地址
    private int User_id;//用户编号

    public int getUser_id() {
        return User_id;
    }

    public String getUser_name() {
        return User_name;
    }

    public String getUser_pwd() {
        return User_pwd;
    }

    public void setUser_id(int user_id) {
        User_id = user_id;
    }

    public UserData(String user_name, String user_pwd){
        this.User_name=user_name;
        this.User_pwd=user_pwd;
    }
}
