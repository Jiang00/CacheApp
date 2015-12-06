package CacheSync;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author shantanubobhate
 */
public class IPGrabber {

    private static InetAddress ip;
    private static String hostname;
    
    public static String getMyIP()
    {
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostAddress();
            
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return(""+hostname);
    }
}
