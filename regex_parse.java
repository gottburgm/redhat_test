// package iduno.apache.something.like.dat
 
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTestPatternMatcher {
    public static final String TEST_DATA = "Listen 80\nListen 8000\n\nListen 192.0.2.1:80\n\nListen 192.0.2.5:8000\nIPv6 addresses must be enclosed in square brackets, as in the following example:\nListen [2001:db8::a00:20ff:fea7:ccea]:80\nblabla\nfoo\nbar\nListen *:80\nListen 0.0.0.0:8443 https\n\n";
    
    public static void main(String[] args) {
                Pattern listen_directives_pattern = Pattern.compile("Listen (((\\*|\\[.*:.*::.*:.*:.*:.*\\]|[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+):([0-9]+))|([0-9]+))\\W");
                Matcher matcher = listen_directives_pattern.matcher(TEST_DATA);
                
                while (matcher.find()) {
                        System.out.print("Start index: " + matcher.start());
                        System.out.print(" End index: " + matcher.end() + " ");
                        System.out.println(matcher.group());
                
                        // 1. Find a way to have something like hashtable like (perl) : $listen_directives{<host>} = (<port1>,<port2>,<portx>)
                        // 2. foreach host in listen_directives do another foreach port in host's ports array
                        // 3. foreach to test if current port is eq to another host's one
                        // 4. AND not equal to one of the "host" ports : if ($current_host_port != $another_host_port && $current_host_port != $listen_directives{"*"}{<portx>})  
                }
    }
