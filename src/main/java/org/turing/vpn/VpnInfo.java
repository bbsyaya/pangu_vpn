package org.turing.vpn;

public class VpnInfo {
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getVpnName() {
		return vpnName;
	}
	public void setVpnName(String vpnName) {
		this.vpnName = vpnName;
	}
	public String domain = "";
	public String name = "";
	public String password = "";
	public String vpnName = "";
}
