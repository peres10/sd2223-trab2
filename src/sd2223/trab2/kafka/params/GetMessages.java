package sd2223.trab2.kafka.params;

import sd2223.trab2.api.Message;

public class GetMessages {
    private String user;
    private long time;

    public String getUser(){
        return user;
    }

    public long getTime(){
        return time;
    }


    public GetMessages(String user, long mid){
        this.user = user;
        this.time = mid;
    }
}
