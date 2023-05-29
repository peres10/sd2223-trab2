package sd2223.trab2.servers.rest;

import org.glassfish.jersey.server.ResourceConfig;
import sd2223.trab2.api.java.Feeds;
import sd2223.trab2.servers.Domain;

import java.util.logging.Logger;

public class KafkaServer extends AbstractRestServer {

    public static final int PORT = 4567;
    private static Logger Log = Logger.getLogger(KafkaServer.class.getName());

    KafkaServer(){
        super(Log, Feeds.SERVICENAME, PORT);
    }

    @Override
    void registerResources(ResourceConfig config) {
        config.register(new KafkaResource(0L));
        config.register(GenericExceptionMapper.class);
    }

    public static void main(String[] args) throws Exception{
        Domain.set( args[0], Long.valueOf(args[1]));

        new KafkaServer().start();
    }


}
