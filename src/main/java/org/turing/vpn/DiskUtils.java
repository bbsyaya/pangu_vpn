package org.turing.vpn;

import java.io.File;  
import java.io.FileWriter;  
import java.io.BufferedReader;  
import java.io.InputStreamReader;  
  
class DiskUtils {  
    private DiskUtils() {  
    }  
  
    public static String getSerialNumber(String drive) {  
        String result = "";  
        try {  
            File file = File.createTempFile("damn", ".vbs");  
            file.deleteOnExit();  
            FileWriter fw = new java.io.FileWriter(file);  
            String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"  
                    + "Set colDrives = objFSO.Drives\n"  
                    + "Set objDrive = colDrives.item(\""  
                    + drive  
                    + "\")\n"  
                    + "Wscript.Echo objDrive.SerialNumber"; // see note  
            fw.write(vbs);  
            fw.close();  
            Process p = Runtime.getRuntime().exec(  
                    "cscript //NoLogo " + file.getPath());  
            BufferedReader input = new BufferedReader(new InputStreamReader(  
                    p.getInputStream()));  
            String line;  
            while ((line = input.readLine()) != null) {  
                result += line;  
  
            }  
            input.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return result.trim();  
    }  
    public static void main(String[] args) {  
        // TODO Auto-generated method stub  
          
        //  
        System.out.println("***MAC地址***");  
        MacUtils.getMac();  
        //  
        String sn = DiskUtils.getSerialNumber("C");   
        System.out.println("***硬盘编号***");  
        System.out.println(sn);   
  
    }  
}  