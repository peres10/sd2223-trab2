package sd2223.trab2.clients.rest;

import sd2223.trab2.api.Message;
import sd2223.trab2.api.java.Feeds;

import com.google.gson.reflect.TypeToken;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import sd2223.trab2.api.java.Result;
import sd2223.trab2.mastodon.MastodonApi;
import sd2223.trab2.mastodon.msgs.PostStatusArgs;
import sd2223.trab2.mastodon.msgs.PostStatusResult;
import tls.InsecureHostnameVerifier;
import utils.JSON;

import javax.net.ssl.HttpsURLConnection;
import java.util.List;

import static sd2223.trab2.api.java.Result.ErrorCode.*;
import static sd2223.trab2.api.java.Result.*;

public class MastodonClient implements Feeds {

    static String MASTODON_NOVA_SERVER_URI = "http://10.170.138.52:3000";
    static String MASTODON_SOCIAL_SERVER_URI = "https://mastodon.social";

    static String MASTODON_SERVER_URI = MASTODON_NOVA_SERVER_URI;

    private final String clientKey;
    private final String clientSecret;
    private final String accessTokenStr;

    static final String STATUSES_PATH= "/api/v1/statuses";
    static final String TIMELINES_PATH = "/api/v1/timelines/home";
    static final String ACCOUNT_FOLLOWING_PATH = "/api/v1/accounts/%s/following";
    static final String VERIFY_CREDENTIALS_PATH = "/api/v1/accounts/verify_credentials";
    static final String SEARCH_ACCOUNTS_PATH = "/api/v1/accounts/search";
    static final String ACCOUNT_FOLLOW_PATH = "/api/v1/accounts/%s/follow";
    static final String ACCOUNT_UNFOLLOW_PATH = "/api/v1/accounts/%s/unfollow";

    private static final int HTTP_OK = 200;

    protected OAuth20Service service;
    protected OAuth2AccessToken accessToken;

    private static MastodonClient impl;

    public MastodonClient(String clientKey, String clientSecret, String accessTokenStr) {
        this.clientKey = clientKey;
        this.clientSecret = clientSecret;
        this.accessTokenStr = accessTokenStr;

        HttpsURLConnection.setDefaultHostnameVerifier(new InsecureHostnameVerifier());
        try {
            service = new ServiceBuilder(clientKey).apiSecret(clientSecret).build(MastodonApi.instance());
            accessToken = new OAuth2AccessToken(accessTokenStr);
        } catch (Exception x) {
            x.printStackTrace();
            System.exit(0);
        }
    }

    synchronized public MastodonClient getInstance() {
        if (impl == null)
            impl = new MastodonClient(clientKey,clientSecret, accessTokenStr);
        return impl;
    }

    private String getEndpoint(String path, Object ... args ) {
        var fmt = MASTODON_SERVER_URI + path;
        return String.format(fmt, args);
    }


    @Override
    public Result<Long> postMessage(String user, String pwd, Message msg) {
        try {
            final OAuthRequest request = new OAuthRequest(Verb.POST, getEndpoint(STATUSES_PATH));

            JSON.toMap( new PostStatusArgs(msg.getText())).forEach( (k, v) -> {
                request.addBodyParameter(k, v.toString());
            });

            service.signRequest(accessToken, request);

            Response response = service.execute(request);
            if (response.getCode() == HTTP_OK) {
                var res = JSON.decode(response.getBody(), PostStatusResult.class);
                return ok(res.getId());
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return error(INTERNAL_ERROR);
    }

    @Override
    public Result<Void> removeFromPersonalFeed(String user, long mid, String pwd) {
        try {
            final OAuthRequest request = new OAuthRequest(Verb.DELETE, getEndpoint(STATUSES_PATH + "/" + mid));

            service.signRequest(accessToken, request);

            Response response = service.execute(request);

            if(response.getCode() == HTTP_OK){
                return ok();
            }
        } catch (Exception x){
            x.printStackTrace();
        }
        return error(Result.ErrorCode.INTERNAL_ERROR);
    }

    @Override
    public Result<Message> getMessage(String user, long mid) {
        try{
            final OAuthRequest request = new OAuthRequest(Verb.GET, getEndpoint(STATUSES_PATH)+ "/" + mid);

            service.signRequest(accessToken, request);

            Response response = service.execute(request);
            if(response.getCode() == HTTP_OK){
                PostStatusResult res = JSON.decode(response.getBody(), PostStatusResult.class);
                return ok(res.toMessage());
            }
        } catch (Exception x){
            x.printStackTrace();
        }
        return error(INTERNAL_ERROR);
    }

    @Override
    public Result<List<Message>> getMessages(String user, long time) {
        try {
            final OAuthRequest request = new OAuthRequest(Verb.GET, getEndpoint(TIMELINES_PATH));

            service.signRequest(accessToken, request);

            Response response = service.execute(request);

            if (response.getCode() == HTTP_OK) {
                List<PostStatusResult> res = JSON.decode(response.getBody(), new TypeToken<List<PostStatusResult>>() {
                });

                return ok(res.stream().map(PostStatusResult::toMessage).toList());
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return error(Result.ErrorCode.INTERNAL_ERROR);
    }

    @Override
    public Result<Void> subUser(String user, String userSub, String pwd) {
        return null;
    }

    @Override
    public Result<Void> unsubscribeUser(String user, String userSub, String pwd) {
        return null;
    }

    @Override
    public Result<List<String>> listSubs(String user) {
        return null;
    }

    @Override
    public Result<Void> deleteUserFeed(String user) {
        return null;
    }
}