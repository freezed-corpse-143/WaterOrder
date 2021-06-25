package com.example.qiupys.wateroder.tool;

import android.util.Log;

public class Check {
    public Query query=new Query();
    public String answer;

    public void setQuery(Query query) {
        this.query = query;
    }

    public String checkBasic(String content,String variety){
        if(content==null){
            return variety+"不能为空";
        }
        if(content.equals("")){
            return variety+"输入为空串";
        }
        return "合法";
    }

    public boolean checkRAccount(String account){
        account=account.trim();
        String re = checkBasic(account,"账号");
        if(!re.equals("合法")){
            answer=re;
            return false;
        }
        if(query.exists("Client","Account",account)){
            answer="账号已经存在";
            return false;
        }
        answer="账号合法";
        return true;
    }

    public boolean checkLAccount(String account){
        account=account.trim();
        String re = checkBasic(account,"账号");
        if(!re.equals("合法")){
            answer=re;
            return false;
        }
        answer="账号合法";
        return true;
    }

    public boolean checkPhone(String phone){
        phone =phone.trim();
        String re = checkBasic(phone,"电话");
        if(!re.equals("合法")){
            answer=re;
            return false;
        }
        if(phone.length()>11){
            answer="电话号码位数超过11位";
            return false;
        }
        if(!phone.matches("[1][345678]\\d{9}")){
            answer="电话号码为非法输入";
            return false;
        }
        answer="电话号码合法";
        return true;
    }

    //利用正则表达式判断字符是否为IP
    public boolean isCorrectIp2(String ipString) {
        String ipRegex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";	//IP地址的正则表达式
        //如果前三项判断都满足，就判断每段数字是否都位于0-255之间
        if (ipString.matches(ipRegex)) {
            String[] ipArray = ipString.split("\\.");
            for (int i = 0; i < ipArray.length; i++) {
                int number = Integer.parseInt(ipArray[i]);
                //4.判断每段数字是否都在0-255之间
                if (number <0||number>255) {
                    return false;
                }
            }
            return true;
        }
        else {
            return false;	//如果与正则表达式不匹配，则返回false
        }
    }

    public boolean checkIP(String ip){
        ip = ip.trim();
        String re = checkBasic(ip,"IP");
        if(!re.equals("合法")){
            answer=re;
            return false;
        }
        if(!isCorrectIp2(ip)){
            answer="不规范的IP地址";
            return false;
        }
        String IP =query.getIPv4();
        query.setIPv4(ip);
        if(!query.ConnectCheck()){
            answer="连接服务器失败";
            return false;
        }
        query.setIPv4(IP);
        answer="IP地址合法";
        return true;
    }

    public boolean checkPassword(String password){
        password=password.trim();
        String re = checkBasic(password,"密码");
        if(!re.equals("合法")){
            answer=re;
            return false;
        }
        if(password.length()<6){
//            Log.i("TAG", "checkPassword: "+re+"　"+re.length());
            answer="密码长度小于6位";
            return false;
        }
        answer="密码合法";
        return true;
    }

    public boolean checkLogin(String account,String password){
        String[] columns = new String[]{"Account","Password"};
        String[] values = new String[]{account,password};
        if(query.exists("Client",columns,values)){
            answer="登录成功";
            return true;
        }
        answer="登录失败";
        return false;
    }

    public boolean checkSame(String one, String two){
        if(!one.equals(two)){
            answer="密码不同";
            return false;
        }
        answer="密码相同";
        return true;
    }

    public boolean checkAdress(String address){
        address=address.trim();
        String re = checkBasic(address,"住址");
        if(!re.equals("合法")){
            answer= re;
            return false;
        }
        answer="住址合法";
        return true;
    }

    public boolean checkName(String name){
        name=name.trim();
        String re = checkBasic(name,"姓名");
        if(!re.equals("合法")){
            answer=re;
            return false;
        }
        answer="姓名合法";
        return true;
    }
}
