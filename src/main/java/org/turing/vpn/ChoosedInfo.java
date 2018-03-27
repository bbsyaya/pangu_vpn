package org.turing.vpn;

import javax.swing.JLabel;

public class ChoosedInfo {
	public String getMonitorCount() {
		return monitorCount;
	}
	public void setMonitorCount(String monitorCount) {
		this.monitorCount = monitorCount;
	}
	public String getVpnConnectType() {
		return vpnConnectType;
	}
	public void setVpnConnectType(String vpnConnectType) {
		this.vpnConnectType = vpnConnectType;
	}
	public String getDynmaticName() {
		return dynmaticName;
	}
	public void setDynmaticName(String dynmaticName) {
		this.dynmaticName = dynmaticName;
	}
	private String monitorCount = "2台";
	private String vpnConnectType = "L2tp";
	private String dynmaticName = "";
	
	public String getPrint(){
		String str =  "模拟器: "+monitorCount + "--连接方式: " + vpnConnectType + "--分配VPN: " + dynmaticName;
		return str;
	}
}
