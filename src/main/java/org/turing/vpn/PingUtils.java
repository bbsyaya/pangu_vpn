package org.turing.vpn;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PingUtils {
    
    public static boolean ping(String ipAddress) throws Exception {
        int  timeOut =  3000 ;  //超时应该在3钞以上        
        boolean status = InetAddress.getByName(ipAddress).isReachable(timeOut);     // 当返回值是true时，说明host是可用的，false则不可。
        return status;
    }
    
    public static void ping02(String ipAddress) throws Exception {
        String line = null;
        try {
            Process pro = Runtime.getRuntime().exec("ping " + ipAddress);
            BufferedReader buf = new BufferedReader(new InputStreamReader(
                    pro.getInputStream()));
            while ((line = buf.readLine()) != null)
                System.out.println(line);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static boolean ping(String ipAddress, int pingTimes, int timeOut) {  
        BufferedReader in = null;  
        Runtime r = Runtime.getRuntime();  // 将要执行的ping命令,此命令是windows格式的命令  
        String pingCommand = "ping " + ipAddress + " -n " + pingTimes    + " -w " + timeOut;  
        try {   // 执行命令并获取输出  
            System.out.println(pingCommand);   
            Process p = r.exec(pingCommand);   
            if (p == null) {    
                return false;   
            }
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));   // 逐行检查输出,计算类似出现=23ms TTL=62字样的次数  
            int connectedCount = 0;   
            String line = null;   
            while ((line = in.readLine()) != null) { 
            	//System.out.println(line);   
                connectedCount += getCheckResult(line);   
            }   // 如果出现类似=23ms TTL=62这样的字样,出现的次数=测试次数则返回真  
            return connectedCount == pingTimes;  
        } catch (Exception ex) {   
            ex.printStackTrace();   // 出现异常则返回假  
            return false;  
        } finally {   
            try {    
                in.close();   
            } catch (IOException e) {    
                e.printStackTrace();   
            }  
        }
    }
    public static int pingNeedTime(String ipAddress, int pingTimes, int timeOut) {
    	BufferedReader in = null;  
    	int time = 10000;
        Runtime r = Runtime.getRuntime();  // 将要执行的ping命令,此命令是windows格式的命令  
        String pingCommand = "ping " + ipAddress + " -n " + pingTimes    + " -w " + timeOut;  
        try {   // 执行命令并获取输出   
            Process p = r.exec(pingCommand);   
            if (p == null) {    
                return time;   
            }
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));   // 逐行检查输出,计算类似出现=23ms TTL=62字样的次数  
            int connectedCount = 0;  
            int timeTotal = 0;
            int spendTime = 0;
            String line = null;   
            while ((line = in.readLine()) != null) { 
            	//System.out.println(line);  
            	spendTime = getTimes(line);
            	if( spendTime > 0){
            		timeTotal += spendTime;  
                    connectedCount++;
            	}
            }   // 如果出现类似=23ms TTL=62这样的字样,出现的次数=测试次数则返回真  
            return timeTotal/connectedCount;  
        } catch (Exception ex) {   
            ex.printStackTrace();   // 出现异常则返回假  
            return time;
        } finally {   
            try {    
                in.close();   
            } catch (IOException e) {    
                e.printStackTrace();   
            }  
        }
    }
    private static int getTimes(String line){
		//String cost = "来自 111.206.227.118 的回复: 字节=32 时间=40ms TTL=49";
    	if( 0 == getCheckResult(line)){
    		return 0;
    	}
		Pattern compile = Pattern.compile("(\\d+)ms");
        Matcher matcher = compile.matcher(line);
        if(true == matcher.find()){
        	String str = matcher.group();
        	return Integer.valueOf(Pattern.compile("[^0-9]").matcher(str).replaceAll(""));
        }
        return 10000;
    }
    //若line含有=18ms TTL=16字样,说明已经ping通,返回1,否則返回0.
    private static int getCheckResult(String line) {  // System.out.println("控制台输出的结果为:"+line);  
        Pattern pattern = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)",    Pattern.CASE_INSENSITIVE);  
        Matcher matcher = pattern.matcher(line);  
        while (matcher.find()) {
            return 1;
        }
        return 0; 
    }
    public static void main(String[] args) throws Exception {
        String ipAddress = "127.0.0.1";
        System.out.println(ping(ipAddress));
        ping02(ipAddress);
        System.out.println(ping(ipAddress, 5, 5000));
    }
}