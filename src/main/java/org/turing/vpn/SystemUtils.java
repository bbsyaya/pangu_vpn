package org.turing.vpn;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SystemUtils {
    private static String getSplitString(String str, String split, int length) {  
        int len = str.length();  
        StringBuilder temp = new StringBuilder();  
        for (int i = 0; i < len; i++) {  
            if (i % length == 0 && i > 0) {  
                temp.append(split);  
            }  
            temp.append(str.charAt(i));  
        }  
        String[] attrs = temp.toString().split(split);  
        StringBuilder finalMachineCode = new StringBuilder();  
        for (String attr : attrs) {  
            if (attr.length() == length) {  
                finalMachineCode.append(attr).append(split);  
            }  
        }  
        String result = finalMachineCode.toString().substring(0,  
                finalMachineCode.toString().length() - 1);  
        return result;  
    }  
  
    public static String bytesToHexString(byte[] src) {  
        StringBuilder stringBuilder = new StringBuilder("");  
        if (src == null || src.length <= 0) {  
            return null;  
        }  
        for (int i = 0; i < src.length; i++) {  
            int v = src[i] & 0xFF;  
            String hv = Integer.toHexString(v);  
            if (hv.length() < 2) {  
                stringBuilder.append(0);  
            }  
            stringBuilder.append(hv);  
        }  
        return stringBuilder.toString();  
    }  
  
    // ‎00-24-7E-0A-22-93  
    public static String getLocalMac() throws SocketException {
    	InetAddress ia = null;
		try {
			ia = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		//获取网卡，获取地址
		byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		System.out.println("mac数组长度："+mac.length);
		StringBuffer sb = new StringBuffer("");
		for(int i=0; i<mac.length; i++) {
			if(i!=0) {
				sb.append("-");
			}
			//字节转换为整数
			int temp = mac[i]&0xff;
			String str = Integer.toHexString(temp);
			System.out.println("每8位:"+str);
			if(str.length()==1) {
				sb.append("0"+str);
			}else {
				sb.append(str);
			}
		}
		System.out.println("本机MAC地址:"+sb.toString().toUpperCase());
		return sb.toString().toUpperCase();
		
	} 
}
