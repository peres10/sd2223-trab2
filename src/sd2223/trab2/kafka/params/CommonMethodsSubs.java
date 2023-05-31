package sd2223.trab2.kafka.params;

import sd2223.trab2.api.Message;

public class CommonMethodsSubs {
    private String user;
    private String userSub;
    private String pwd;

    public String getUser(){
        return user;
    }

    public String getUserSub(){
        return userSub;
    }

    public String getPwd(){
        return pwd;
    }

    public CommonMethodsSubs(String user, String userSub, String pwd){
        this.user = user;
        this.pwd = pwd;
        this.userSub = userSub;
    }
}
