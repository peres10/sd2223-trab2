package sd2223.trab2.servers.rest;

import org.glassfish.jersey.server.ResourceConfig;
import sd2223.trab2.api.java.Feeds;
import sd2223.trab2.servers.Domain;
import utils.Args;

import java.util.Arrays;
import java.util.logging.Logger;

public class KafkaServer extends RestFeedsServer {

    public static final int PORT = 7777;
    private static Logger Log = Logger.getLogger(KafkaServer.class.getName());

    KafkaServer(){
        super(Log, PORT);
    }

    @Override
    void registerResources(ResourceConfig config) {
        config.register(new KafkaResource(0L));
        config.register(GenericExceptionMapper.class);
    }

    public static void main(String[] args) throws Exception{
        //Args.use(args);
        Domain.set( args[0], 1 );
        //System.out.println(Arrays.toString(args));
        new KafkaServer().start();
    }


}
