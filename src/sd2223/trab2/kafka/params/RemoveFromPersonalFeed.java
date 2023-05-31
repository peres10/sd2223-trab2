package sd2223.trab2.kafka.params;

import sd2223.trab2.api.Message;

public class RemoveFromPersonalFeed {
    private String user;
    private String pwd;
    private long mid;

    public String getUser(){
        return user;
    }

    public String getPwd(){
        return pwd;
    }

    public long getMid(){
        return mid;
    }

    public RemoveFromPersonalFeed(String user, long mid, String pwd){
        this.user = user;
        this.pwd = pwd;
        this.mid = mid;
    }
}
