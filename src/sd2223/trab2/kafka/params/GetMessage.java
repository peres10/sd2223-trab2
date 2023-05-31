package sd2223.trab2.kafka.params;

import sd2223.trab2.api.Message;

public class GetMessage {
    private String user;
    private long mid;

    public String getUser(){
        return user;
    }

    public long getMid(){
        return mid;
    }


    public GetMessage(String user, long mid){
        this.user = user;
        this.mid = mid;
    }
}
