package sd2223.trab2.servers.rest;

import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import sd2223.trab2.api.Message;
import sd2223.trab2.api.java.Feeds;
import sd2223.trab2.api.java.Result;
import sd2223.trab2.api.rest.FeedsService;
import sd2223.trab2.kafka.KafkaPublisher;
import sd2223.trab2.kafka.KafkaSubscriber;
import sd2223.trab2.kafka.RecordProcessor;
import sd2223.trab2.kafka.params.*;
import sd2223.trab2.kafka.sync.SyncPoint;
import sd2223.trab2.servers.java.JavaFeedsCommon;
import sd2223.trab2.servers.java.JavaFeedsPull;
import sd2223.trab2.servers.java.JavaKafka;

import java.util.List;

public class KafkaResource extends RestResource implements FeedsService {
    private static final String KAFKA_BROKERS = "kafka:9092";
    private static String TOPIC = "kafka-rep";
    private static KafkaPublisher kpublisher;
    private static KafkaSubscriber ksubscriber;
    private Gson gson = new Gson();
    private SyncPoint syncPoint;
    private Long version;
    private final Feeds impl;


    public KafkaResource(long v){
        super();
        this.version = v;
        impl = new JavaKafka();
        generateTopic();
        this.syncPoint = new SyncPoint<>();
        ksubscriber.start(false, new RecordProcessor() {
            @Override
            public void onReceive(ConsumerRecord<String, String> r) {
                long offset = r.offset();
                String params = r.value();
                Result res = null;
                switch (r.key()){
                    case "postMessage" ->{
                        PostMessage pm = gson.fromJson(params, PostMessage.class);
                        res = impl.postMessage(pm.getUser(), pm.getPwd(), pm.getMsg());
                    }
                    case "removeFromPersonalFeed" ->{
                        RemoveFromPersonalFeed rp = gson.fromJson(params, RemoveFromPersonalFeed.class);
                        res = impl.removeFromPersonalFeed(rp.getUser(), rp.getMid(), rp.getPwd());
                    }
                    case "getMessage" ->{
                        GetMessage gm = gson.fromJson(params, GetMessage.class);
                        res = impl.getMessage(gm.getUser(), gm.getMid());
                    }
                    case "getMessages" ->{
                        GetMessages gms = gson.fromJson(params, GetMessages.class);
                        res = impl.getMessages(gms.getUser(), gms.getTime());
                    }
                    case "subUser" ->{
                        CommonMethodsSubs s = gson.fromJson(params, CommonMethodsSubs.class);
                        res = impl.subUser(s.getUser(),s.getUserSub(),s.getPwd());
                    }
                    case "unsubscribeUser" ->{
                        CommonMethodsSubs s = gson.fromJson(params, CommonMethodsSubs.class);
                        res = impl.unsubscribeUser(s.getUser(),s.getUserSub(),s.getPwd());
                    }
                    case "listSubs" ->{
                        CommonParamUserID ls = gson.fromJson(params, CommonParamUserID.class);
                        res = impl.listSubs(ls.getUser());
                    }
                    case "deleteUserFeed" ->{
                        CommonParamUserID du = gson.fromJson(params, CommonParamUserID.class);
                        res = impl.deleteUserFeed(du.getUser());
                    }
                }
                version = offset;
                syncPoint.setResult(offset,res);
            }
        });
    }

    private<T> Result<T> publish(Long version, String operation, String value){
        if(this.version == null){
            this.version = (0L);
        }

        long offset = kpublisher.publish(TOPIC, operation, value);
        this.version = offset;
        if(version != null){
            syncPoint.waitForVersion(version,500);
        }

        return (Result<T>) syncPoint.waitForResult(offset);
    }


    public void generateTopic(){
        kpublisher = KafkaPublisher.createPublisher(KAFKA_BROKERS);
        ksubscriber = KafkaSubscriber.createSubscriber(KAFKA_BROKERS, List.of(TOPIC), "earliest");
    }

    @Override
    public long postMessage(String user, String pwd, Message msg) {
        String value = gson.toJson(new PostMessage(user,pwd,msg));
        Result<Long> result = publish(version, "postMessage", value);
        return super.fromJavaResult(result);
    }

    @Override
    public void removeFromPersonalFeed(String user, long mid, String pwd) {
        String value = gson.toJson(new RemoveFromPersonalFeed(user,mid,pwd));
        Result<Void> result = publish(version, "removeFromPersonalFeed",value);
        super.fromJavaResult(result);
    }

    @Override
    public Message getMessage(String user, long mid) {
        String value = gson.toJson(new GetMessage(user,mid));
        Result<Message> result = publish(version,"getMessage",value);
        return super.fromJavaResult(result);
    }

    @Override
    public List<Message> getMessages(String user, long time) {
        String value = gson.toJson(new GetMessages(user,time));
        Result<List<Message>> result = publish(version,"getMessages",value);
        return super.fromJavaResult(result);
    }

    @Override
    public void subUser(String user, String userSub, String pwd) {
        String value = gson.toJson(new CommonMethodsSubs(user,userSub,pwd));
        Result<Void> result = publish(version,"subUser",value);
        super.fromJavaResult(result);
    }

    @Override
    public void unsubscribeUser(String user, String userSub, String pwd) {
        String value = gson.toJson(new CommonMethodsSubs(user,userSub,pwd));
        Result<Void> result = publish(version,"unsubscribeUser",value);
        super.fromJavaResult(result);
    }

    @Override
    public List<String> listSubs(String user) {
        String value = gson.toJson(new CommonParamUserID(user));
        Result<List<String>> result = publish(version,"listSubs",value);
        return super.fromJavaResult(result);
    }

    @Override
    public void deleteUserFeed(String user) {
        String value = gson.toJson(new CommonParamUserID(user));
        Result<Void> result = publish(version,"deleteUserFeed",value);
        super.fromJavaResult(result);
    }
}
