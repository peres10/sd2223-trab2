package sd2223.trab2.servers.rest;

import org.glassfish.jersey.server.ResourceConfig;
import sd2223.trab2.api.java.Feeds;
import sd2223.trab2.clients.rest.MastodonClient;
import sd2223.trab2.servers.Domain;
import sd2223.trab2.servers.java.JavaMastodon;

import java.util.Arrays;
import java.util.logging.Logger;

public class MastodonServer extends AbstractRestServer{
    public static final int PORT = 8080;

    private static Logger Log = Logger.getLogger(MastodonServer.class.getName());


    static String apiKey;
    static String apiSecret;
    static String accessTokenStr;

    MastodonServer(){
        super(Log, Feeds.SERVICENAME, PORT);
    }


    @Override
    void registerResources(ResourceConfig config) {
        //JavaMastodon j = new JavaMastodon(apiKey,apiSecret,accessTokenStr);
        config.register(new MastodonResource(apiKey,apiSecret,accessTokenStr));
        config.register(GenericExceptionMapper.class);
    }

    public static void main(String [] args) throws Exception{
        Domain.set( args[0], Long.valueOf(args[1]));


        apiKey = args[2];
        apiSecret = args[3];
        accessTokenStr = args[4];

        System.out.println(Arrays.toString(args));
        new MastodonServer().start();
    }
}
