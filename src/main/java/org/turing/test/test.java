package org.turing.test;
import org.junit.Test;
import org.turing.vpn.ForFile;
import org.turing.vpn.GetComputer;
import org.turing.vpn.SMSEngine;
public class test {
	@Test
	public void ok(){
        String[] param1 = new String[]{GetComputer.getConfig()};
        //SMSEngine.getInstance().sendCCPRestSDK(param1);
        
		String encoding=System.getProperty("file.encoding");
		System.out.println("Default System Encoding: " + encoding);
		//ForFile.writeFile("reboot", "1");
		//ForFile.writeFile("reboot", "kjsdklfjaklsjflk");
		//ForFile.writeFile("reboot", "popopopo");
	}
}
