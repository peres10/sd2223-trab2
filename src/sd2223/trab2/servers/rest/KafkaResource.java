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
import sd2223.trab2.kafka.params.PostMessage;
import sd2223.trab2.kafka.sync.SyncPoint;
import sd2223.trab2.servers.java.JavaFeedsCommon;
import sd2223.trab2.servers.java.JavaFeedsPull;

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
        impl = new JavaFeedsPull();
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

    }

    @Override
    public Message getMessage(String user, long mid) {
        return null;
    }

    @Override
    public List<Message> getMessages(String user, long time) {
        return null;
    }

    @Override
    public void subUser(String user, String userSub, String pwd) {

    }

    @Override
    public void unsubscribeUser(String user, String userSub, String pwd) {

    }

    @Override
    public List<String> listSubs(String user) {
        return null;
    }

    @Override
    public void deleteUserFeed(String user) {

    }
}
