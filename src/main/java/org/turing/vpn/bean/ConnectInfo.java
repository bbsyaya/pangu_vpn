package org.turing.vpn.bean;

public class ConnectInfo{
	private String deviceId = "";
	private int purpose = 0;//操作类型 0:增量赚钱 1:增量水军 2:存量赚钱 3:存量水军
	private String tunnelType = "";//L2tp ,pptp
	private int deviceCount = 0;//模拟器数量
	public int getPurpose() {
		return purpose;
	}
	public void setPurpose(int purpose) {
		this.purpose = purpose;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getTunnelType() {
		return tunnelType;
	}
	public void setTunnelType(String tunnelType) {
		this.tunnelType = tunnelType;
	}
	public int getDeviceCount() {
		return deviceCount;
	}
	public void setDeviceCount(int deviceCount) {
		this.deviceCount = deviceCount;
	}
	
	
	
}
