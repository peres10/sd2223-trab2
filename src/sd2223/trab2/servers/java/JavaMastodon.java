package sd2223.trab2.servers.java;

import sd2223.trab2.api.Message;
import sd2223.trab2.api.java.Feeds;
import sd2223.trab2.api.java.Result;
import sd2223.trab2.clients.rest.MastodonClient;
import sd2223.trab2.servers.rest.RestResource;

import java.util.List;
import java.util.logging.Logger;

public class JavaMastodon extends RestResource implements Feeds {

    final static Logger Log = Logger.getLogger(JavaMastodon.class.getName());

    private MastodonClient mastodonClient;

    public JavaMastodon(String apiKey, String apiSecret, String accessTokenStr){
        //super(new MastodonClient(apiKey,apiSecret,accessTokenStr));
        mastodonClient = new MastodonClient(apiKey,apiSecret,accessTokenStr);
    }

    @Override
    public Result<Long> postMessage(String user, String pwd, Message msg) {
        var res = mastodonClient.postMessage(user,pwd,msg);
        return res;
    }

    @Override
    public Result<Void> removeFromPersonalFeed(String user, long mid, String pwd) {
        var res = mastodonClient.removeFromPersonalFeed(user,mid,pwd);
        return res;
    }

    @Override
    public Result<Message> getMessage(String user, long mid) {
        var res = mastodonClient.getMessage(user,mid);
        return res;
    }

    @Override
    public Result<List<Message>> getMessages(String user, long time) {
        var res = mastodonClient.getMessages(user,time);
        return res;
    }

    @Override
    public Result<Void> subUser(String user, String userSub, String pwd) {
        var res = mastodonClient.subUser(user,userSub,pwd);
        return res;
    }

    @Override
    public Result<Void> unsubscribeUser(String user, String userSub, String pwd) {
        var res = mastodonClient.unsubscribeUser(user,userSub,pwd);
        return res;
    }

    @Override
    public Result<List<String>> listSubs(String user) {
        var res = mastodonClient.listSubs(user);
        return res;
    }

    @Override
    public Result<Void> deleteUserFeed(String user) {
        var res = mastodonClient.deleteUserFeed(user);
        //super.fromJavaResult(res);
        return res;
    }
}

