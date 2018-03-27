package org.turing.vpn.bean;

public class VpnConnectInfo {
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTunnelType() {
		return tunnelType;
	}
	public void setTunnelType(String tunnelType) {
		this.tunnelType = tunnelType;
	}
	public String getAuthenticationMethod() {
		return authenticationMethod;
	}
	public void setAuthenticationMethod(String authenticationMethod) {
		this.authenticationMethod = authenticationMethod;
	}
	public String getEncryptionLevel() {
		return encryptionLevel;
	}
	public void setEncryptionLevel(String encryptionLevel) {
		this.encryptionLevel = encryptionLevel;
	}
	public String getVpnName() {
		return vpnName;
	}
	public void setVpnName(String vpnName) {
		this.vpnName = vpnName;
	}
	private String vpnName;
	private String ip;
	private String userName;
	private String password;
	private String tunnelType;//L2tp,pptp,Automatic
	private String authenticationMethod;//{Chap, MsChapv2}
	private String encryptionLevel;//Optional
	//private String l2tpIPsecAuth;//Certificate

}
