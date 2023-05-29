package sd2223.trab2.kafka.params;

import sd2223.trab2.api.Message;

public class PostMessage {
    private String user;
    private String pwd;
    private Message msg;

    public String getUser(){
        return user;
    }

    public String getPwd(){
        return pwd;
    }

    public Message getMsg(){
        return msg;
    }

    public PostMessage(String user, String pwd, Message msg){
        this.user = user;
        this.pwd = pwd;
        this.msg = msg;
    }
}
