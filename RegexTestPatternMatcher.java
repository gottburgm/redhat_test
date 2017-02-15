// package iduno.apache.something.like.dat
 
import java.util.*;
import java.util.regex.*;

public class RegexTestPatternMatcher {
    public static final String TEST_DATA = "Listen 80\nListen 8000\n\nListen 192.0.2.1:80\n\nListen 192.0.2.5:8000\nIPv6 addresses must be enclosed in square brackets, as in the following example:\nListen [2001:db8::a00:20ff:fea7:ccea]:80\nblabla\nfoo\nbar\nListen *:80\nListen 0.0.0.0:8443 https\n\n";
    
    public static boolean isInteger(String s) {
        try { 
            Integer.parseInt(s); 
        } catch(NumberFormatException e) { 
            return false; 
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
    
    public static void main(String[] args) {
                Hashtable<Integer, String> listeners = new Hashtable<Integer, String>();
                Pattern listen_directives_pattern = Pattern.compile("(?!Listen) (((\\*|\\[.*:.*::.*:.*:.*:.*\\]|[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+):([0-9]+))|([0-9]+))\\W");
                Pattern host_pattern = Pattern.compile("((\\*|\\[.*:.*::.*:.*:.*:.*\\]|[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+)(?!:))");
                Matcher directives_matcher = listen_directives_pattern.matcher(TEST_DATA);
                String host = "";
                String port;
                
                while (directives_matcher.find())
                {
                    Matcher hosts_matcher = host_pattern.matcher(directives_matcher.group());
                    while(hosts_matcher.find()) {
                        host = hosts_matcher.group();
                    }
                    
                     String[] parts = directives_matcher.group().split(host + ":");
                    if(parts.length == 1) {
                        port = parts[0];
                    } else {
                        host = parts[0];
                        port = parts[1];
                    }
                    if (isInteger(port)) {
                        listeners.put(Integer.parseInt(port), host);
                    } else {
                        System.out.print("Invalid entry\n");
                    }
                    
                    System.out.print("Host : " + host + "\n");
                    System.out.print("Port : " + port + "\n");
                
                        // 1. Find a way to have something like hashtable like (perl) : $listen_directives{<host>} = (<port1>,<port2>,<portx>)
                        // 2. foreach host in listen_directives do another foreach port in host's ports array
                        // 3. foreach to test if current port is eq to another host's one
                        // 4. AND not equal to one of the "host" ports : if ($current_host_port != $another_host_port && $current_host_port != $listen_directives{"*"}{<portx>})  
                }
    }

}
