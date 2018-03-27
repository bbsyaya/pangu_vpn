package org.turing.vpn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.UUID;

/** 
* @author 夕橘子-O
* @version 2016年7月8日 上午10:38:49 
*/
public class ForFile {
    //生成文件路径
    private static String path = "C:\\pangu\\";
    
    //文件路径+名称
    private static String filenameTemp;
    /**
     * 创建文件
     * @param fileName  文件名称
     * @param filecontent   文件内容
     * @return  是否创建成功，成功则返回true
     */
 // 创建目录
 	public static boolean createDir(String destDirName) {
 		File dir = new File(destDirName);
 		if (dir.exists()) {// 判断目录是否存在
 			System.out.println("创建目录失败，目标目录已存在！");
 			return false;
 		}
 		if (!destDirName.endsWith(File.separator)) {// 结尾是否以"/"结束
 			destDirName = destDirName + File.separator;
 		}
 		if (dir.mkdirs()) {// 创建目标目录
 			System.out.println("创建目录成功！" + destDirName);
 			return true;
 		} else {
 			System.out.println("创建目录失败！");
 			return false;
 		}
 	}
    public static boolean writeFile(String fileName,String filecontent){
        Boolean bool = false;
        filenameTemp = path+fileName+".txt";//文件路径+名称+文件类型
        File file = new File(filenameTemp);
        try {
            //如果文件不存在，则创建新的文件
            if(!file.exists()){
            	createDir(path);
                file.createNewFile();
                bool = true;
                System.out.println("success create file,the file is "+filenameTemp);
            }
          //创建文件成功后，写入内容到文件里
          writeFileContent(filenameTemp, filecontent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return bool;
    }
    
    /**
     * 向文件中写入内容
     * @param filepath 文件路径与名称
     * @param newstr  写入的内容
     * @return
     * @throws IOException
     */
    public static void writeFileContent(String filepath,String newstr){
    	try{
	        FileOutputStream fout = new FileOutputStream(filepath);
	        byte [] bytes = newstr.getBytes();
	        fout.write(bytes);
	        fout.close();
		    }
		    catch(Exception e){
		        e.printStackTrace();
		    }
    }
    
    /**
     * 删除文件
     * @param fileName 文件名称
     * @return
     */
    public static boolean delFile(String fileName){
        Boolean bool = false;
        filenameTemp = path+fileName+".txt";
        File file  = new File(filenameTemp);
        try {
            if(file.exists()){
                file.delete();
                bool = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return bool;
    }
    public static void main(String[] args) {
        UUID uuid = UUID.randomUUID();
        //createFile(uuid+"myfile", "我的梦说别停留等待,就让光芒折射泪湿的瞳孔,映出心中最想拥有的彩虹,带我奔向那片有你的天空,因为你是我的梦 我的梦");
    }
}
