
import java.util.*;
import java.util.regex.*;

import java.net.*;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class RegexTestPatternMatcher {
    
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

    private static String readFile(String pathname) throws IOException {

    File file = new File(pathname);
    StringBuilder fileContents = new StringBuilder((int)file.length());
    Scanner scanner = new Scanner(file);
    String lineSeparator = System.getProperty("line.separator");

    try {
        while(scanner.hasNextLine()) {
            fileContents.append(scanner.nextLine() + lineSeparator);
        }
        return fileContents.toString();
    } finally {
        scanner.close();
    }
}


    public static void main(String[] args) {
                Hashtable<Integer, String> listeners = new Hashtable<Integer, String>();
                String TEST_DATA = "";
                InetAddress ip;
                String hostname = "";
                String port;

                // Read Test File
                try {
                    TEST_DATA = readFile("config.txt");
                } catch(IOException e) {
                    e.printStackTrace();
                }
                
                // Get Host And IP
                try {
                    ip = InetAddress.getLocalHost();
                    hostname = ip.getHostName();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                
                Pattern listen_directives_pattern = Pattern.compile("(?!Listen\\s)(((\\*|\\[.*:.*::.*:.*:.*:.*\\]|[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+):([0-9]+))|([0-9]+))\\W");
                Pattern host_pattern = Pattern.compile("(((\\*|\\[.*:.*::.*:.*:.*:.*\\]|[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+)):)");
                Matcher directives_matcher = listen_directives_pattern.matcher(TEST_DATA);
                String host = "";
                String directive = "";


                while (directives_matcher.find())
                {
                    Matcher hosts_matcher = host_pattern.matcher(directives_matcher.group());
                    while(hosts_matcher.find()) {
                        host = hosts_matcher.group();

                        // use 0.0.0.0 for the possible variations of localhost
                        if(host == "*" || host == hostname || host == "" || host == "localhost") {
                            host = "0.0.0.0";
                        }
                    }
                    
                    directive = directives_matcher.group().substring(directives_matcher.group().lastIndexOf(':')+1);
                    String[] parts = directive.split(":");
                    if(parts.length == 1) {
                        if(host.length() > 0) {
                            host = host.substring(0, host.length());
                        } else {
                            host = "0.0.0.0";
                        }
                        port = parts[0];
                    } else if (parts.length == 2) {
                        port = parts[1];
                    } else {
                        host = "0.0.0.0";
                        port = directives_matcher.group();
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
