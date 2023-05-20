package tls;

import sd2223.trab2.discovery.Discovery;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;
import java.util.logging.Logger;

public class InsecureHostnameVerifier implements HostnameVerifier {

    private static Logger Log = Logger.getLogger(InsecureHostnameVerifier.class.getName());


    static final Set<String> ALLOWED = Set.of("users0-ourorg0.sdnet", "feeds0-ourorg0.sdnet","feeds1-ourorg0.sdnet","feeds2-ourorg0.sdnet",
            "users0-ourorg1.sdnet", "feeds0-ourorg1.sdnet","feeds1-ourorg1.sdnet","feeds2-ourorg1.sdnet",
            "users0-ourorg2.sdnet", "feeds0-ourorg2.sdnet","feeds1-ourorg2.sdnet","feeds2-ourorg2.sdnet");

    @Override
    public boolean verify(String hostname, SSLSession session) {
        Log.info(hostname);
        Log.info("///\n");
        String resolvedName = null;
        try {
            resolvedName = InetAddress.getByName(hostname).getHostName();
            Log.info(resolvedName);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ALLOWED.contains(resolvedName);
    }

}
