package sd2223.trab2.servers.java;

import sd2223.trab2.api.Message;
import sd2223.trab2.api.java.Result;
import sd2223.trab2.kafka.params.PostMessage;
import sd2223.trab2.servers.Domain;

import java.util.Arrays;

public class JavaKafka extends JavaFeedsPull{

    private static final long FEEDS_MID_PREFIX= 1_000_000_000;

    public Result<Long> postMessage(String user, String pwd, Message msg) {


        var preconditionsResult = preconditions.postMessage(user, pwd, msg);
        if( ! preconditionsResult.isOK() )
            return preconditionsResult;

        Long mid = serial.incrementAndGet() - (Domain.uuid() * FEEDS_MID_PREFIX) + FEEDS_MID_PREFIX;
        msg.setId(mid);
        msg.setCreationTime(System.currentTimeMillis());

        FeedInfo ufi = feeds.computeIfAbsent(user, FeedInfo::new );
        System.out.println("postMessage");
        //System.out.println(FEEDS_MID_PREFIX);
        //System.out.println(Domain.uuid());
        //System.out.println(mid);
        //System.out.println(user);
        //System.out.println(Arrays.toString(ufi.messages.toArray()));
        synchronized (ufi.user()) {
            ufi.messages().add(mid);
            messages.putIfAbsent(mid, msg);
        }
        //System.out.println(Arrays.toString(ufi.messages.toArray()));
        return Result.ok(mid);
    }

}
